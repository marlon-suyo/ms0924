package util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class HolidayChecker {

    public static boolean isObservedHoliday(LocalDate currentDate) {
        LocalDate observedIndependenceDayHolidayDate = HolidayChecker.independenceDayObservedHoliday(currentDate.getYear());
        LocalDate observedLaborDayHolidayDate = HolidayChecker.laborDayObservedHoliday(currentDate.getYear());

        return currentDate.isEqual(observedIndependenceDayHolidayDate)
                || currentDate.isEqual(observedLaborDayHolidayDate);
    }

    // holiday is July 4;
    // observed holiday will be on July 3 if holiday on Saturday
    // observed holiday will be on July 5 if holiday on Monday
    private static LocalDate independenceDayObservedHoliday(int year) {
        DayOfWeek day = LocalDate.of(year, Month.JULY, 4).getDayOfWeek();

        switch (day) {
            case SUNDAY: // July 4 is on Sunday; observe on Monday July 5
                return LocalDate.of(year, Month.JULY, 5);
            case MONDAY: // July 4 is on Monday
            case TUESDAY: // July 4 is on Tuesday
            case WEDNESDAY: // July 4 is on Wednesday
            case THURSDAY: // July 4 is on Thursday
            case FRIDAY: // July 4 is on Friday
                return LocalDate.of(year, Month.JULY, 4);
            default: // July 4 is on Saturday; observe on Friday July 3
                return LocalDate.of(year, Month.JULY, 3);
        }
    }

    // first Monday of September
    private static LocalDate laborDayObservedHoliday(int year) {
        DayOfWeek day = LocalDate.of(year, Month.SEPTEMBER, 1).getDayOfWeek();

        switch (day) {
            case SUNDAY: // Sep 1 is on Sunday
                return LocalDate.of(year, Month.SEPTEMBER, 2);
            case MONDAY: // Sep 1 is on Monday
                return LocalDate.of(year, Month.SEPTEMBER, 1);
            case TUESDAY: // Sep 1 is on Tuesday
                return LocalDate.of(year, Month.SEPTEMBER, 7);
            case WEDNESDAY: // Sep 1 is on Wednesday
                return LocalDate.of(year, Month.SEPTEMBER, 6);
            case THURSDAY: // Sep 1 is on Thursday
                return LocalDate.of(year, Month.SEPTEMBER, 5);
            case FRIDAY: // Sep 1 is on Friday
                return LocalDate.of(year, Month.SEPTEMBER, 4);
            default: // Sep 1 is on Saturday
                return LocalDate.of(year, Month.SEPTEMBER, 3);
        }
    }
}
