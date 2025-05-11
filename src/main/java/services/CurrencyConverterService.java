package services;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterService {
    private static final Map<String, Double> exchangeRates = new HashMap<>();

    static {
        exchangeRates.put("USD", 1.0); // 基准为 USD
        exchangeRates.put("CNY", 7.2);
        exchangeRates.put("EUR", 0.9);
    }

    public static double convert(double amount, String fromCurrency, String toCurrency) {
        if (!exchangeRates.containsKey(fromCurrency) || !exchangeRates.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Unsupported currency");
        }
        double inUSD = amount / exchangeRates.get(fromCurrency);
        return inUSD * exchangeRates.get(toCurrency);
    }

    public static String format(double amount, String currency) {
        switch (currency) {
            case "USD":
                return "$" + String.format("%.2f", amount);
            case "CNY":
                return "¥" + String.format("%.2f", amount);
            case "EUR":
                return "€" + String.format("%.2f", amount);
            default:
                return amount + " " + currency;
        }
    }
}
