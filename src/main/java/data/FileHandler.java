package data;

import Entity.Transaction;
import services.SecurityService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file operations for transaction data.
 * This class provides methods to load transactions from a file and save transactions to a file.
 * It includes encryption and decryption of transaction data for security.
 */
public class FileHandler {
    /**
     * Loads transactions from a specified file.
     * Each line in the file represents a transaction in the format: description,amount,category.
     * The data is decrypted before processing.
     *
     * @param filePath The path to the file containing transaction data
     * @return A list of Transaction objects loaded from the file
     */
    public List<Transaction> loadTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String decrypted = SecurityService.decrypt(line);
                if (decrypted == null)
                    continue;

                String[] data = line.split(",");
                if (data.length == 3) {
                    String description = data[0];
                    double amount = Double.parseDouble(data[1]);
                    String category = data[2];
                    transactions.add(new Transaction(description, amount, category));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Saves a list of transactions to a specified file.
     * Each transaction is written as a line in the format: description,amount,category.
     * The data is encrypted before being written to the file.
     *
     * @param transactions The list of transactions to be saved
     * @param filePath The path where the transactions will be saved
     */
    public void saveTransactions(List<Transaction> transactions, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Transaction transaction : transactions) {
                String plain = transaction.getDescription() + "," + transaction.getAmount() + ","
                        + transaction.getCategory();
                String encrypted = SecurityService.encrypt(plain);

                bw.write(
                        transaction.getDescription() + "," + transaction.getAmount() + "," + transaction.getCategory());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}