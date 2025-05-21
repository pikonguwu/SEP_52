package services;

import org.junit.Test;
import static org.junit.Assert.*;

public class CurrencyConverterServiceTest {

    @Test
    public void testUSDToCNY() {
        double result = CurrencyConverterService.convert(1, "USD", "CNY");
        assertEquals(7.2, result, 0.001);
    }

    @Test
    public void testCNYToUSD() {
        double result = CurrencyConverterService.convert(7.2, "CNY", "USD");
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testEURToHKD() {
        double result = CurrencyConverterService.convert(0.9, "EUR", "HKD");
        double expected = 0.9 / 0.9 * 7.8; // 1 USD = 7.8 HKD
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCurrency() {
        CurrencyConverterService.convert(100, "ABC", "USD"); // 不支持的货币
    }

    @Test
    public void testFormat() {
        assertEquals("$10.00", CurrencyConverterService.format(10, "USD"));
        assertEquals("¥10.00", CurrencyConverterService.format(10, "CNY"));
        assertEquals("€10.00", CurrencyConverterService.format(10, "EUR"));
        assertEquals("HK$10.00", CurrencyConverterService.format(10, "HKD"));
    }
}
