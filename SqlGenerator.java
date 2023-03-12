import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

import java.util.GregorianCalendar;
import java.util.Arrays;
import java.util.Scanner;

public class SqlGenerator {

    static final String 
        NAME_DATA = "name",
        SURNAME_DATA = "surname",
        COUNTRY_DATA = "country",
        DOUBLE_DATA = "double",
        INTEGER_DATA = "int",
        DATE_DATA = "date",
        ID = "id",
        DNI = "dni";

    static final String[] dataTypes = 
        {NAME_DATA, SURNAME_DATA, 
         COUNTRY_DATA, DOUBLE_DATA, 
         INTEGER_DATA, DATE_DATA,
         ID,DNI};
    // OPTIONS is also the index where options stop
    static final int OPTIONS = 3;
    public static void main(String[] args) {
        if (args.length < OPTIONS) {
            System.out.println(HELP);
            System.exit(0);
        }
        // Args 
        final String 
            ouputFileName = args[0],
            tableName = args[1];
        final int numberOfInserts = Integer.parseInt(args[2]);

        final String[] COLUMNS_DATA_TYPE = 
            Arrays.copyOfRange(args, OPTIONS, args.length);

        File outputFile = new File(ouputFileName);
        try (BufferedWriter br = new BufferedWriter(new FileWriter(outputFile))) {

            br.write(getInsertCommand(tableName));
            StringBuilder builder = new StringBuilder("");
            Scanner scan = new Scanner(System.in);

            System.out.println("Default values:\n" + DEFAULT_VALUES);

            char userChoice = getChar(scan, "Would you like to use custom values? (y/n)");

             int
                 MIN_INT = 0, 
                 MAX_INT = 1000,
                 MIN_DOUBLE = 0, 
                 MAX_DOUBLE = 1000000,
                 START_YEAR = 1945, 
                 END_YEAR = 2003,
                 N_DECIMALS = 2;

           switch (userChoice) {
                case 'y':
                    MIN_INT = getInt(scan, "Min Integer");
                    MAX_INT = getInt(scan, "Max Integer");
                    MIN_DOUBLE = getInt(scan, "Min Double"); 
                    MAX_DOUBLE = getInt(scan, "Max Double");
                    START_YEAR = getUnsignedInt(scan, "Start Year"); 
                    END_YEAR = getUnsignedInt(scan, "End Year");
                    N_DECIMALS = getUnsignedInt(scan, "Number of Decimal places");
                    break;
                default:
                    System.out.println("Default values will be used");
           }

            for (int i = 0; i < numberOfInserts; i++) {
                builder.append("\t(");
                for (int j = 0; j < COLUMNS_DATA_TYPE.length; j++) {
                    switch (COLUMNS_DATA_TYPE[j]) {
                        case NAME_DATA:
                            String name = getName();
                            builder.append(appendColons(name));
                            break;
                        case SURNAME_DATA:
                            String surname = getSurname();
                            builder.append(appendColons(surname));
                            break;
                        case COUNTRY_DATA:
                            String country = getCountry();
                            builder.append(appendColons(country));
                            break;
                        case DOUBLE_DATA:
                            double decimalNumber = getRandomDouble(MIN_DOUBLE, MAX_DOUBLE);
                            String formatedDecimal = getFormatedDouble(N_DECIMALS, decimalNumber);
                            builder.append(appendColons(formatedDecimal));
                            break;
                        case INTEGER_DATA:
                            int number = getRandomInt(MIN_INT, MAX_INT);
                            builder.append(appendColons(number));
                            break;
                        case DATE_DATA:
                            String date = getRandomDate(START_YEAR, END_YEAR);
                            builder.append(appendColons(date));
                            break;
                        case ID:
                           builder.append(appendColons(i));
                           break;
                        case DNI:
                           String dni = generateVerifiedDni();
                           builder.append(appendColons(dni));
                           break;
                        default:
                            System.err.println("INVALID DATATYPE " + COLUMNS_DATA_TYPE[j]  
                                + "\nVALID ONES " + Arrays.toString(dataTypes));
                            System.exit(1);
                    } 
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append(")");
                if (i == numberOfInserts - 1)
                    builder.append(";");
                else 
                    builder.append(",\n");
                
                br.write(builder.toString());
                builder.setLength(0);
            }
            System.out.println("\n\u001B[32mFile created succesfully!\u001B[0m");
        } catch (IOException ioe) {
            System.out.println(ioe.getLocalizedMessage());
        }
    }

    public static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max + 1 - min) + min;
    }
    
    public static double getRandomDouble(int min, int max) {
        return ThreadLocalRandom.current().nextDouble(max - min) + min;
    }

    public static String getFormatedDouble(int decimalPlaces, double n) {
        String formater = "%." + decimalPlaces + "f";
        return String.format(formater, n);
    }

    public static String getName() {
        return FIRSTNAMES[getRandomInt(0, FIRSTNAMES.length - 1)];
    }

    public static String getSurname() {
        return SURNAMES[getRandomInt(0, SURNAMES.length - 1)];
    }

    public static String getCountry() {
        return COUNTRIES[getRandomInt(0, COUNTRIES.length - 1)];
    }

    public static String getRandomDate(int startYear, int endYear) {
        GregorianCalendar gc = new GregorianCalendar();

        int year = getRandomInt(startYear, endYear);

        gc.set(GregorianCalendar.YEAR, year);

        int dayOfYear = getRandomInt(1, gc.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));

        gc.set(GregorianCalendar.DAY_OF_YEAR, dayOfYear);

        return (gc.get(GregorianCalendar.YEAR) + "-" 
            + (gc.get(GregorianCalendar.MONTH) + 1) + "-" 
            + gc.get(GregorianCalendar.DAY_OF_MONTH));
    } 

    public static String appendColons(String str) {
        return "'" + str + "',";
    }

    public static String appendColons(int str) {
        return "'" + str + "',";
    }

    public static String appendColons(double str) {
        return "'" + str + "',";
    }

    static String getInsertCommand(String tableName) {
        return "INSERT INTO " + tableName + "\nVALUES\n";
    }

    static char getChar (Scanner scan, String text) {
        System.out.print(text + " > ");
        return scan.next().charAt(0);
    }

    static int getInt (Scanner scan, String text) {
        while (true) {
            System.out.print(text + " > ");
            if (!scan.hasNextInt()) {
                scan.nextLine();
                System.out.println("Has to be a Number");
                continue;
            }
            break;
        }        
        return scan.nextInt();
    }

    static int getUnsignedInt(Scanner scan, String text) {
        int n = 1;
        while (true) {
            n = getInt(scan, text);
            if (n < 0) {
                System.out.println("Number has to be positive");
                    continue;
            }
            break;
        }
        return n;
    }

    /**
     * Genera Dni de acuerdo al ultimo char de verificacion
     * @return
     */
    public static String generateVerifiedDni() {
        String numberChain = generateNumberChain();
        numberChain += generateVerifChar(Long.parseLong(numberChain));
        if (numberChain.charAt(numberChain.length()-1) == '1') {
            System.err.println("Secuencia de numeros mal generada");
            return null;
        }
        return numberChain;
    }

    /**
     * generates a pseudo-random DNI number chain of numbers
     * @return
     */
    public static String generateNumberChain() {
        String dni = "";
        Random rand = new Random();
        
        int numerosDni = 7;
        for (int i = 0; i < numerosDni; i++) {
            dni += rand.nextInt(9 - 1) + 1;
        }
        return dni;
    }

    /**
     * Devuelve el char de verificacion del DNI
     * @param secuenciaDni (long) siete cirfras
     * @return '1' si el long no es de siete cifras
     */
    public static char generateVerifChar(long secuenciaDni) {
        int resto = (int) secuenciaDni%23;
        if (resto >=0 && resto <= 22) {
            return SECUENCIA_23[resto];
        }
        System.out.println("Numero DNI tiene que contener 7 digitos");
        return '1';
    }

    public static final char[] SECUENCIA_23 = {'T', 'R', 'W', 'A', 'G', 'M', 'Y',
         'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L',
         'C', 'K', 'E'};

    // Strings

    final static String HELP = 
        "Usage: java SqlGenerator <option> [<args>]\n"
      + "SqlGenerator\t<output_file> <table_name> <number_of_values> [<types>]\n"
                  + "\t        default values affect the following data-types\n" 
                  + "\t\t        " + INTEGER_DATA + " from 0 to 1000\n"
                  + "\t\t        " + DOUBLE_DATA + " from 0 to 1 000 000 with two decimal places\n"
                  + "\t\t        " + DATE_DATA + "from the end of the WWII(1945) to the start of the second Gulf War(2003)\n"
                  +"Types\t" + Arrays.toString(dataTypes) + '\n'
                  + "Example : \tSqlGenerator inserts.sql customers 200 name surname date double int";
    final static String DEFAULT_VALUES = 
        INTEGER_DATA + " from 0 to 1000\n"
            + DOUBLE_DATA + " from 0 to 1 000 000 with two decimal places\n"
            + DATE_DATA + " from the end of the WWII (1945) to the start of the second Gulf War (2003)\n";

    // Data arrays

    static final String[] FIRSTNAMES = {"Mya","Peter","Uriah",
        "Sonny","Marie","Danyelle",
        "Ruby","Phoenix","Citlali",
        "Ryland","Dashaun","Broderick","Weston","Tyrell",
        "Edmund","Myron","Fernanda",
        "Tate","Zain","Benjamin",
        "Constance","Bruce","Zion","Miah","Keon","Riley",
        "Caroline","Liliana","Ruth",
        "Nallely","Vincenzo","Kai",
        "Kayden","Alvaro","Travon",
        "Owen","Haylee","Jaidyn",
        "Holland","Peyton","Long",
        "Franklin","Dasia","Ileana",
        "Nikolas","Daria","Kalee","Arthur","Brylee","Sierra"};

    static final String[] SURNAMES = {"Hudgins","Stockton","Cochrane",
        "Granger","Stuckey","Bowles",
        "Metcalf","Connor","Fisher","Haddad","Geiger",
        "Lloyd","Strickland","Shipp","Faulkner","Beasley",
        "Mercer","Hadley","Downey","Conti","Shapiro","Derrick",
        "Lozano","Garibay","Burden","Taft","Hand","Kirk","Switzer",
        "Ragland","McFadden","Hester","Hirsch","Cady","Hillman","Foss",
        "Carver","Estes","Mancuso","Troutman","Levy","Ramos","Wisniewski",
        "Wozniak","Plummer","Pope","Lewis","Kinder","Trout","Eller"};

    static final String[] COUNTRIES = {"Seychelles","Rwanda","Saint Lucia",
        "Tajikistan","Trinidad and Tobago","New Caledonia",
        "Cook Islands","Bermuda","Burkina Faso",
        "Colombia","Dominica","Brazil","Belarus",
        "Australia","U.S. Virgin Islands","Faroe Islands",
        "Costa Rica","Macao","Saint Kitts and Nevis","Bhutan",
        "Eritrea","Iceland","Jersey","Thailand","Hungary"};
}   