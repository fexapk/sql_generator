import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.GregorianCalendar;
import java.util.Arrays;

public class SqlGenerator {

    static final String 
        NAME_DATA = "name",
        SURNAME_DATA = "surname",
        COUNTRY_DATA = "country",
        DOUBLE_DATA = "double",
        INTEGER_DATA = "int",
        DATE_DATA = "date";
    static final String[] dataTypes = 
        {NAME_DATA, SURNAME_DATA, 
         COUNTRY_DATA, DOUBLE_DATA, 
         INTEGER_DATA, DATE_DATA};
    // OPTIONS = index where column data types start
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

            final int 
                MIN_INT = 0, 
                MAX_INT = 1000,
                MIN_DOUBLE = 0, 
                MAX_DOUBLE = 1000000,
                START_YEAR = 1945, 
                END_YEAR = 2003,
                N_DECIMALS = 2;

            for (int i = 0; i < numberOfInserts; i++) {
                builder.append("\t(");
                for (int j = 0; j < COLUMNS_DATA_TYPE.length; j++) {
                    switch (COLUMNS_DATA_TYPE[j]) {
                        case NAME_DATA:
                            builder.append(appendColons(getName()));
                            break;
                        case SURNAME_DATA:
                            builder.append(appendColons(getSurname()));
                            break;
                        case COUNTRY_DATA:
                            builder.append(appendColons(getCountry()));
                            break;
                        case DOUBLE_DATA:
                            double n = getRandomDouble(MIN_DOUBLE, MAX_DOUBLE);
                            builder.append(appendColons(getFormatedDouble(N_DECIMALS, n)));
                            break;
                        case INTEGER_DATA:
                            builder.append(appendColons(getRandomInt(MIN_INT, MAX_INT)));
                            break;
                        case DATE_DATA:
                            builder.append(appendColons(getRandomDate(START_YEAR, END_YEAR)));
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
        return ThreadLocalRandom.current().nextInt(max - min) + min;
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

    // Menu Strings

    final static String HELP = 
        "Usage: java SqlGenerator [<args>]\n"
      + "SqlGenerator\t<output_file> <table_name> <number_of_values> [<columns>]\n"
                  + "\t        default values affect the following data-types:\n" 
                  + "\t\t        " + INTEGER_DATA + " from 0 to 1000\n"
                  + "\t\t        " + DOUBLE_DATA + " from 0 to 1 000 000 with two decimal places\n"
                  + "\t\t        " + DATE_DATA + " from the end of the WWII (1945) to the start of the second Gulf War (2003)\n";

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