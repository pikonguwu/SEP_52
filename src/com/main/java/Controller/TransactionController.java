package com.main.java.Controller;

import com.main.java.Entity.Transaction;

import java.util.List;

public class TransactionController {
    public void addTransaction(List<Transaction> transactions, Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransaction(List<Transaction> transactions, Transaction transaction) {
        transactions.remove(transaction);
    }

    public void displayTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}
