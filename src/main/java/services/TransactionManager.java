package services;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * A singleton service class that manages financial transactions and implements the Observer pattern
 * for transaction updates. This class serves as the central manager for all transaction-related
 * operations and coordinates updates between the data service and registered listeners.
 * 
 * <p>The TransactionManager follows the Singleton pattern to ensure a single instance
 * manages all transaction data across the application. It maintains a list of listeners
 * that are notified of any changes to the transaction data.
 * 
 * <p>Example usage:
 * <pre>
 * TransactionManager manager = TransactionManager.getInstance();
 * manager.addListener(new TransactionListener() {
 *     public void onTransactionAdded(String date, String desc, String amount, String type) {
 *         // Handle new transaction
 *     }
 *     // ... implement other listener methods
 * });
 * manager.addTransaction("01/01/2024", "Grocery", "$100.50", "Expense");
 * </pre>
 * 
 * @author System
 * @version 1.0
 */
public class TransactionManager {
    private static TransactionManager instance;
    private List<TransactionListener> listeners = new ArrayList<>();
    private TransactionDataService dataService;
    
    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the transaction data service.
     */
    private TransactionManager() {
        dataService = new TransactionDataService();
    }
    
    /**
     * Gets the singleton instance of TransactionManager.
     * Creates a new instance if one doesn't exist.
     * 
     * @return the singleton instance of TransactionManager
     */
    public static synchronized TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }
    
    /**
     * Registers a new transaction listener.
     * The listener will be notified of all transaction changes.
     * 
     * @param listener the listener to be registered
     */
    public void addListener(TransactionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Adds a new transaction and notifies all registered listeners.
     * 
     * @param date the transaction date
     * @param description the transaction description
     * @param amount the transaction amount
     * @param type the transaction type (Income/Expense)
     */
    public void addTransaction(String date, String description, String amount, String type) {
        dataService.addTransaction(date, description, amount, type);
        
        for (TransactionListener listener : listeners) {
            listener.onTransactionAdded(date, description, amount, type);
        }
    }
    
    /**
     * Updates an existing transaction and notifies all registered listeners.
     * 
     * @param index the index of the transaction to update
     * @param date the new transaction date
     * @param description the new transaction description
     * @param amount the new transaction amount
     * @param type the new transaction type
     */
    public void updateTransaction(int index, String date, String description, String amount, String type) {
        // Get old data
        Map<String, Object> oldTransaction = dataService.getTransactions().get(index);
        String oldDate = (String) oldTransaction.get("date");
        String oldDescription = (String) oldTransaction.get("description");
        double oldAmountValue = (Double) oldTransaction.get("amount");
        String oldType = (String) oldTransaction.get("type");
        
        // Format old amount
        String oldAmount = String.format("%.2f", oldAmountValue);
        
        // Update data service
        dataService.updateTransaction(index, date, description, amount, type);
        
        // Notify all listeners
        for (TransactionListener listener : listeners) {
            listener.onTransactionUpdated(
                oldDate, oldDescription, oldAmount, oldType,
                date, description, amount, type
            );
        }
    }
    
    /**
     * Removes a transaction and notifies all registered listeners.
     * 
     * @param date the date of the transaction to remove
     * @param description the description of the transaction to remove
     * @param amount the amount of the transaction to remove
     * @param type the type of the transaction to remove
     */
    public void removeTransaction(String date, String description, String amount, String type) {
        // Find matching transaction
        List<Map<String, Object>> transactions = dataService.getTransactions();
        int indexToRemove = -1;
        
        for (int i = 0; i < transactions.size(); i++) {
            Map<String, Object> transaction = transactions.get(i);
            String transDate = (String) transaction.get("date");
            String transDesc = (String) transaction.get("description");
            double transAmount = (Double) transaction.get("amount");
            String transType = (String) transaction.get("type");
            
            if (date.equals(transDate) && description.equals(transDesc) && 
                Math.abs(transAmount - Double.parseDouble(amount)) < 0.01 && type.equals(transType)) {
                indexToRemove = i;
                break;
            }
        }
        
        // Remove if found
        if (indexToRemove >= 0) {
            transactions.remove(indexToRemove);
            
            // Notify all listeners
            for (TransactionListener listener : listeners) {
                listener.onTransactionRemoved(date, description, amount, type);
            }
        }
    }
    
    /**
     * Retrieves all transactions.
     * 
     * @return a list of all transaction records
     */
    public List<Map<String, Object>> getAllTransactions() {
        return dataService.getTransactions();
    }
    
    /**
     * Gets the weekly spending analysis.
     * 
     * @return a map containing daily spending totals for the current week
     */
    public Map<String, Double> getWeeklySpending() {
        return dataService.getWeeklySpending();
    }
    
    /**
     * Gets the expense category analysis.
     * 
     * @return a map containing expense categories and their total amounts
     */
    public Map<String, Double> getExpenseCategories() {
        return dataService.getExpenseCategories();
    }
    
    /**
     * Interface for transaction event listeners.
     * Implementations of this interface will be notified of transaction changes.
     */
    public interface TransactionListener {
        /**
         * Called when a new transaction is added.
         * 
         * @param date the transaction date
         * @param description the transaction description
         * @param amount the transaction amount
         * @param type the transaction type
         */
        void onTransactionAdded(String date, String description, String amount, String type);

        /**
         * Called when an existing transaction is updated.
         * 
         * @param oldDate the previous transaction date
         * @param oldDescription the previous transaction description
         * @param oldAmount the previous transaction amount
         * @param oldType the previous transaction type
         * @param newDate the new transaction date
         * @param newDescription the new transaction description
         * @param newAmount the new transaction amount
         * @param newType the new transaction type
         */
        void onTransactionUpdated(
            String oldDate, String oldDescription, String oldAmount, String oldType,
            String newDate, String newDescription, String newAmount, String newType
        );

        /**
         * Called when a transaction is removed.
         * 
         * @param date the removed transaction date
         * @param description the removed transaction description
         * @param amount the removed transaction amount
         * @param type the removed transaction type
         */
        void onTransactionRemoved(String date, String description, String amount, String type);
    }
}