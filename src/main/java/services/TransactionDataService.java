package services;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * A service class that manages financial transaction data and provides analysis functionality.
 * This class handles the storage, retrieval, and analysis of financial transactions,
 * including daily spending patterns and expense categorization.
 * 
 * <p>The service maintains a list of transactions, where each transaction contains:
 * <ul>
 *     <li>Date (in dd/MM/yyyy format)</li>
 *     <li>Description</li>
 *     <li>Amount (with support for currency symbols and formatting)</li>
 *     <li>Type (Income/Expense)</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>
 * TransactionDataService service = new TransactionDataService();
 * service.addTransaction("01/01/2024", "Grocery shopping", "$100.50", "Expense");
 * Map<String, Double> weeklySpending = service.getWeeklySpending();
 * Map<String, Double> categories = service.getExpenseCategories();
 * </pre>
 * 
 * @author System
 * @version 1.0
 */
public class TransactionDataService {
    private List<Map<String, Object>> transactions = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * Adds a new transaction to the transaction list.
     * 
     * @param date the transaction date in dd/MM/yyyy format
     * @param description the transaction description
     * @param amount the transaction amount (can include currency symbols and formatting)
     * @param type the transaction type (Income/Expense)
     */
    public void addTransaction(String date, String description, String amount, String type) {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("date", date);
        transaction.put("description", description);
        transaction.put("amount", parseAmount(amount));
        transaction.put("type", type);
        
        transactions.add(transaction);
    }
    
    /**
     * Updates an existing transaction at the specified index.
     * 
     * @param index the index of the transaction to update
     * @param date the new transaction date in dd/MM/yyyy format
     * @param description the new transaction description
     * @param amount the new transaction amount
     * @param type the new transaction type
     */
    public void updateTransaction(int index, String date, String description, String amount, String type) {
        if (index >= 0 && index < transactions.size()) {
            Map<String, Object> transaction = transactions.get(index);
            transaction.put("date", date);
            transaction.put("description", description);
            transaction.put("amount", parseAmount(amount));
            transaction.put("type", type);
        }
    }
    
    /**
     * Parses a string amount into a double value.
     * Removes currency symbols, commas, and plus signs while preserving minus signs for expenses.
     * 
     * @param amount the amount string to parse
     * @return the parsed double value
     */
    private double parseAmount(String amount) {
        return Double.parseDouble(amount.replace("$", "")
                                      .replace(",", "")
                                      .replace("+", ""));
    }
    
    /**
     * Calculates the total spending for each day of the current week.
     * Returns a map with days (Mon-Sun) as keys and total spending as values.
     * 
     * @return a LinkedHashMap containing daily spending totals for the current week
     */
    public Map<String, Double> getWeeklySpending() {
        Map<String, Double> weeklyData = new LinkedHashMap<>();
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : days) {
            weeklyData.put(day, 0.0);
        }
        
        Calendar calendar = Calendar.getInstance();
        
        for (Map<String, Object> transaction : transactions) {
            try {
                String dateStr = (String)transaction.get("date");
                Date transDate = dateFormat.parse(dateStr);
                
                calendar.setTime(transDate);
                
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String dayName = days[(dayOfWeek + 5) % 7];
                
                if ("Expense".equals(transaction.get("type"))) {
                    double amount = Math.abs((Double)transaction.get("amount"));
                    weeklyData.put(dayName, weeklyData.get(dayName) + amount);
                }
            } catch (ParseException e) {
                System.out.println("Error parsing date: " + e.getMessage());
            }
        }
        
        return weeklyData;
    }
    
    /**
     * Analyzes and categorizes expenses based on transaction descriptions.
     * Categories include: Housing, Food, Transport, Entertainment, Savings, and Others.
     * 
     * @return a HashMap containing expense categories and their total amounts
     */
    public Map<String, Double> getExpenseCategories() {
        Map<String, Double> categoryData = new HashMap<>();
        
        for (Map<String, Object> transaction : transactions) {
            if ("Expense".equals(transaction.get("type"))) {
                String description = ((String)transaction.get("description")).toLowerCase();
                String category = getCategoryFromDescription(description);
                double amount = Math.abs((Double)transaction.get("amount"));
                
                categoryData.put(category, categoryData.getOrDefault(category, 0.0) + amount);
            }
        }
        
        return categoryData;
    }
    
    /**
     * Determines the expense category based on the transaction description.
     * Uses keyword matching to categorize transactions into predefined categories.
     * 
     * @param description the transaction description to analyze
     * @return the determined expense category
     */
    private String getCategoryFromDescription(String description) {
        if (description.contains("rent") || description.contains("mortgage") || description.contains("house")) {
            return "Housing";
        } else if (description.contains("food") || description.contains("grocery") || description.contains("restaurant")) {
            return "Food";
        } else if (description.contains("car") || description.contains("gas") || description.contains("transport")) {
            return "Transport";
        } else if (description.contains("movie") || description.contains("entertainment") || description.contains("game")) {
            return "Entertainment";
        } else if (description.contains("saving") || description.contains("deposit") || description.contains("investment")) {
            return "Savings";
        } else {
            return "Others";
        }
    }
    
    /**
     * Retrieves the complete list of transactions.
     * 
     * @return a List of Maps containing all transaction data
     */
    public List<Map<String, Object>> getTransactions() {
        return transactions;
    }
}