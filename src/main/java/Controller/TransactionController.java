package Controller;

import Entity.Transaction;

import java.util.List;

/**
 * Controller class for managing transaction operations.
 * This class provides methods to add, remove, and display transactions in a list.
 */
public class TransactionController {
    /**
     * Adds a new transaction to the transaction list.
     *
     * @param transactions The list of transactions to add to
     * @param transaction The transaction to be added
     */
    public void addTransaction(List<Transaction> transactions, Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Removes a transaction from the transaction list.
     *
     * @param transactions The list of transactions to remove from
     * @param transaction The transaction to be removed
     */
    public void removeTransaction(List<Transaction> transactions, Transaction transaction) {
        transactions.remove(transaction);
    }

    /**
     * Displays all transactions in the list.
     * Each transaction will be printed to the console using its toString() method.
     *
     * @param transactions The list of transactions to display
     */
    public void displayTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}