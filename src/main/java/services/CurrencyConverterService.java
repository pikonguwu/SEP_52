package services;

import java.util.HashMap;
import java.util.Map;

/**
 * A service class that provides currency conversion functionality.
 * This class maintains a static map of exchange rates relative to USD (United
 * States Dollar)
 * and offers methods to convert between different currencies and format
 * currency amounts.
 * 
 * <p>
 * The exchange rates are stored relative to USD as the base currency (1.0).
 * Currently supported currencies include USD, CNY (Chinese Yuan), EUR (Euro),
 * and HKD (Hong Kong Dollar).
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * double amount = CurrencyConverterService.convert(100, "USD", "EUR");
 * String formatted = CurrencyConverterService.format(amount, "EUR");
 * </pre>
 * 
 * @author System
 * @version 1.0
 */
public class CurrencyConverterService {

    private static final Map<String, Double> TO_USD_RATES = new HashMap<>();

    static {
        TO_USD_RATES.put("USD", 1.0);// the banchmark is USD
        TO_USD_RATES.put("CNY", 1 / 7.2);
        TO_USD_RATES.put("EUR", 1 / 0.9);
        TO_USD_RATES.put("HKD", 1 / 7.8);// ✅ New exchange rate of Hong Kong dollars
    }

    /**
     * Converts an amount from one currency to another using the stored exchange
     * rates.
     * 
     * @param amount       the amount to convert
     * @param fromCurrency the source currency code (e.g., "USD", "EUR")
     * @param toCurrency   the target currency code (e.g., "CNY", "HKD")
     * @return the converted amount in the target currency
     * @throws IllegalArgumentException if either currency is not supported
     */

    public static double convert(double amount, String fromCurrency, String toCurrency) {
        Double fromRate = TO_USD_RATES.get(fromCurrency);
        Double toRate = TO_USD_RATES.get(toCurrency);
        if (fromRate == null || toRate == null) {
            throw new IllegalArgumentException("Invalid currency code.");
        }

        // Convert to USD first, then to target currency
        double usdAmount = amount * fromRate;
        return usdAmount / toRate;
    }

    /**
     * Formats a currency amount according to the specified currency's conventions.
     * Adds the appropriate currency symbol and formats the number to two decimal
     * places.
     * 
     * @param amount   the amount to format
     * @param currency the currency code (e.g., "USD", "EUR", "CNY", "HKD")
     * @return a formatted string representation of the amount with currency symbol
     *         (e.g., "$100.00", "€90.00", "¥720.00", "HK$780.00")
     */
    public static String format(double amount, String currency) {
        switch (currency) {
            case "USD":
                return "$" + String.format("%.2f", amount);
            case "CNY":
                return "¥" + String.format("%.2f", amount);
            case "EUR":
                return "€" + String.format("%.2f", amount);
            case "HKD":
                return "HK$" + String.format("%.2f", amount); // ✅ New format for Hong Kong dollars added
            default:
                return amount + " " + currency;
        }
    }
}
