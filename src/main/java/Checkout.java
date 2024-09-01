import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import model.ToolType;
import model.Tool;
import util.HolidayChecker;
import view.RentalAgreement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

@Builder
public class Checkout {

    @Getter
    private String toolCode;

    @Getter
    private int rentalDayCount;

    @Getter
    private int discountPercent;

    @Getter
    private LocalDate checkoutDate;

    private RentalAgreement.RentalAgreementBuilder rentalAgreement;

    public static class CheckoutBuilder {

        private int rentalDayCount;
        private int discountPercent;
        private LocalDate checkoutDate;

        public CheckoutBuilder rentalDayCount(int rentalDayCount) {
            this.rentalDayCount = rentalDayCount;
            if (rentalDayCount < 1) {
                throw new IllegalArgumentException("The day count must be 1 or greater.");
            }
            return this;
        }

        public CheckoutBuilder discountPercent(int discountPercent) {
            this.discountPercent = discountPercent;
            if (discountPercent < 0 || discountPercent > 100) {
                throw new IllegalArgumentException("The discount percentage must be between 0 and 100.");
            }
            return this;
        }

        public CheckoutBuilder checkoutDate(String checkoutDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
            this.checkoutDate = LocalDate.parse(checkoutDate, formatter);
            return this;
        }
    }

    public double calculateFinalCharge() {
        Map<String, Tool> toolMap = new HashMap<>();
        toolMap.put("CHNS", new Tool("CHNS","Chainsaw","Stihl"));
        toolMap.put("LADW", new Tool("LADW","Ladder","Werner"));
        toolMap.put("JAKD", new Tool("JAKD","Jackhammer","DeWalt"));
        toolMap.put("JAKR", new Tool("JAKR","Jackhammer","Ridgid"));

        Map<String, ToolType> toolTypeMap = new HashMap<>();
        toolTypeMap.put("Ladder", new ToolType("Ladder",1.99, true, true, false));
        toolTypeMap.put("Chainsaw", new ToolType("Chainsaw",1.49,true,false,true));
        toolTypeMap.put("Jackhammer", new ToolType("Jackhammer",2.99,true,false,false));

        Tool tool = toolMap.get(toolCode);
        ToolType toolType = toolTypeMap.get(tool.getToolType());

        // count charge days starting day after checkout date
        LocalDate currentDate = checkoutDate.plusDays(1);

        double originalCharge = 0.00;
        int chargeDays = 0;

        for (int i=1; i<=rentalDayCount; i++) {

            DayOfWeek day = DayOfWeek.of(currentDate.get(ChronoField.DAY_OF_WEEK));
            if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY) { // day is a weekend
                if (toolType.isWeekendCharge()) {
                    originalCharge += toolType.getDailyCharge();
                    chargeDays++;
                }
            } else { // day is weekday
                // check if weekday is an observed holiday
                if (HolidayChecker.isObservedHoliday(currentDate)) {
                    if (toolType.isHolidayCharge()) {
                        originalCharge += toolType.getDailyCharge();
                        chargeDays++;
                    }
                } else {
                    if (toolType.isWeekdayCharge()) {
                        originalCharge += toolType.getDailyCharge();
                        chargeDays++;
                    }
                }
            }

            // increment current date until day before due date
            if (i < rentalDayCount)
                currentDate = currentDate.plusDays(1);
        }

        rentalAgreement = RentalAgreement.builder()
                .toolCode(toolCode)
                .toolType(tool.getToolType())
                .toolBrand(tool.getBrand())
                .rentalDays(rentalDayCount)
                .checkoutDate(checkoutDate)
                .dueDate(currentDate)
                .dailyRentalCharge(toolType.getDailyCharge())
                .chargeDays(chargeDays)
                .preDiscountCharge(originalCharge)
                .discountPercent(discountPercent);

        BigDecimal originalAmount = new BigDecimal(originalCharge);
        BigDecimal discountAmount = BigDecimal.valueOf(discountPercent)
                .divide(BigDecimal.valueOf(100))
                .multiply(originalAmount);
        discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);

        rentalAgreement.discountAmount(discountAmount.doubleValue());

        BigDecimal finalCharge = originalAmount.subtract(discountAmount);
        finalCharge = finalCharge.setScale(2, RoundingMode.HALF_UP);
        rentalAgreement.finalCharge(finalCharge.doubleValue());

        return finalCharge.doubleValue();
    }

    public RentalAgreement getRentalAgreement() {
        return rentalAgreement.build();
    }
}
