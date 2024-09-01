import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutTest {

    @Test
    void testScenario1() {

        Checkout.CheckoutBuilder checkout = Checkout.builder()
                .toolCode("JAKR")
                .checkoutDate("9/3/15")
                .rentalDayCount(5);

        assertThrows(IllegalArgumentException.class, () -> checkout.discountPercent(101));
    }

    @Test
    void testScenario2() {

        Checkout checkout = Checkout.builder()
                .toolCode("LADW")
                .checkoutDate("7/2/20")
                .rentalDayCount(3)
                .discountPercent(10)
                .build();

        assertEquals(3.58, checkout.calculateFinalCharge());
        System.out.println("Scenario 2 Rental Agreement");
        checkout.getRentalAgreement().printRentalAgreement();
    }

    @Test
    void testScenario3() {

        Checkout checkout = Checkout.builder()
                .toolCode("CHNS")
                .checkoutDate("7/2/15")
                .rentalDayCount(5)
                .discountPercent(25)
                .build();

        assertEquals(3.35, checkout.calculateFinalCharge());
        System.out.println("Scenario 3 Rental Agreement");
        checkout.getRentalAgreement().printRentalAgreement();
    }

    @Test
    void testScenario4() {

        Checkout checkout = Checkout.builder()
                .toolCode("JAKD")
                .checkoutDate("9/3/15")
                .rentalDayCount(6)
                .discountPercent(0)
                .build();

        assertEquals(8.97, checkout.calculateFinalCharge());
        System.out.println("Scenario 4 Rental Agreement");
        checkout.getRentalAgreement().printRentalAgreement();
    }

    @Test
    void testScenario5() {

        Checkout checkout = Checkout.builder()
                .toolCode("JAKR")
                .checkoutDate("7/2/15")
                .rentalDayCount(9)
                .discountPercent(0)
                .build();

        assertEquals(14.95, checkout.calculateFinalCharge());
        System.out.println("Scenario 5 Rental Agreement");
        checkout.getRentalAgreement().printRentalAgreement();
    }

    @Test
    void testScenario6() {

        Checkout checkout = Checkout.builder()
                .toolCode("JAKR")
                .checkoutDate("7/2/20")
                .rentalDayCount(4)
                .discountPercent(50)
                .build();

        assertEquals(1.49, checkout.calculateFinalCharge());
        System.out.println("Scenario 6 Rental Agreement");
        checkout.getRentalAgreement().printRentalAgreement();
    }

    @Test
    void testScenario_InvalidRentalDayCount() {
        Checkout.CheckoutBuilder checkout = Checkout.builder()
                .toolCode("JAKR")
                .checkoutDate("9/3/15")
                .discountPercent(0);

        assertThrows(IllegalArgumentException.class, () -> checkout.rentalDayCount(0));
    }

    @Test
    void ytestScenario_NegativeDiscountPercent() {

        Checkout.CheckoutBuilder checkout = Checkout.builder()
                .toolCode("JAKR")
                .checkoutDate("9/3/15")
                .rentalDayCount(5);

        assertThrows(IllegalArgumentException.class, () -> checkout.discountPercent(-1));
    }

}