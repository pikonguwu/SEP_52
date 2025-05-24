package services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link CurrencyConverterService}.
 * This class contains test cases to verify currency conversion and formatting
 * functionality.
 */
public class CurrencyConverterServiceTest {

    /**
     * Tests conversion from USD to CNY.
     * Expects 1 USD to equal 7.2 CNY.
     */
    @Test
    public void testUSDToCNY() {
        double result = CurrencyConverterService.convert(1, "USD", "CNY");
        assertEquals(7.2, result, 0.001);
    }

    /**
     * Tests conversion from CNY to USD.
     * Expects 7.2 CNY to equal 1 USD.
     */
    @Test
    public void testCNYToUSD() {
        double result = CurrencyConverterService.convert(7.2, "CNY", "USD");
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Tests conversion from EUR to HKD.
     * Based on the assumption that 1 EUR = 0.9 USD and 1 USD = 7.8 HKD.
     */
    @Test
    public void testEURToHKD() {
        double result = CurrencyConverterService.convert(0.9, "EUR", "HKD");
        double expected = 0.9 / 0.9 * 7.8; // Converted to USD then to HKD
        assertEquals(expected, result, 0.001);
    }

    /**
     * Tests conversion with an invalid currency code.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test
    public void testInvalidCurrency() {
        assertThrows(IllegalArgumentException.class, () -> {
            CurrencyConverterService.convert(100, "ABC", "USD");
        });
    }

    /**
     * Tests currency formatting for different currencies.
     * Ensures that formatted strings include the correct symbols.
     */
    @Test
    public void testFormat() {
        assertEquals("$10.00", CurrencyConverterService.format(10, "USD"));
        assertEquals("¥10.00", CurrencyConverterService.format(10, "CNY"));
        assertEquals("€10.00", CurrencyConverterService.format(10, "EUR"));
        assertEquals("HK$10.00", CurrencyConverterService.format(10, "HKD"));
    }
}
