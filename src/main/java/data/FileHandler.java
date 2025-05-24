package data;

import Entity.Transaction;
import services.SecurityService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file operations for transaction data.
 * This class provides methods to load transactions from a file and save
 * transactions to a file.
 * It includes encryption and decryption of transaction data for security.
 */
public class FileHandler {
    /**
     * Loads transactions from a specified file.
     * Each line in the file represents a transaction in the format:
     * description,amount,category.
     * The data is decrypted before processing.
     *
     * @param filePath The path to the file containing transaction data
     * @return A list of Transaction objects loaded from the file
     */
    // 读取并解密存储文件
    public List<Transaction> loadTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String decrypted = SecurityService.decrypt(line);
                if (decrypted == null)
                    continue;

                String[] data = decrypted.split(",");
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

    // 解析用户上传的明文 CSV
    public List<Transaction> importTransactions(String filePath) {
        List<Transaction> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String desc = parts[0].trim();
                    double amt = Double.parseDouble(parts[1].trim());
                    String type = parts[2].trim();
                    list.add(new Transaction(desc, amt, type));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ 新增：加密写入方法
    public void saveTransactions(List<Transaction> transactions, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Transaction tx : transactions) {
                String plain = tx.getDescription() + "," + tx.getAmount() + "," + tx.getCategory();
                String encrypted = SecurityService.encrypt(plain);
                bw.write(encrypted);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}