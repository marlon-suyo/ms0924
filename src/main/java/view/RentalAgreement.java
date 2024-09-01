package view;

import lombok.Builder;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Builder
public class RentalAgreement {

    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

    public void printRentalAgreement() {
        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Tool brand: " + toolBrand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Check out date: " + formatDate(checkoutDate));
        System.out.println("Due date: " + formatDate(dueDate));
        System.out.println("Daily rental charge: " + formatCurrency(dailyRentalCharge));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: " + formatCurrency(preDiscountCharge));
        System.out.println("Discount percent: " + formatPercent(discountPercent));
        System.out.println("Discount amount: " + formatCurrency(discountAmount));
        System.out.println("Final Charge: " + formatCurrency(finalCharge));
    }

    private String formatCurrency(double amount) {
        NumberFormat usCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return usCurrencyFormat.format(amount);
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("M/d/yy");
        return dateTimeFormat.format(date);
    }

    private String formatPercent(int percent) {
        return percent + "%";
    }
}
