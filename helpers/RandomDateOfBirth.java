import java.util.GregorianCalendar;

public class RandomDateOfBirth {

    public static void main(String[] args) {

        GregorianCalendar gc = new GregorianCalendar();

        int year = randBetween(1900, 2010);

        gc.set(GregorianCalendar.YEAR, year);

        int dayOfYear = randBetween(1, gc.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));

        gc.set(GregorianCalendar.DAY_OF_YEAR, dayOfYear);

        System.out.println(gc.get(GregorianCalendar.YEAR) + "-" 
            + (gc.get(GregorianCalendar.MONTH) + 1) + "-" 
            + gc.get(GregorianCalendar.DAY_OF_MONTH));

    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}