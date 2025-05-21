package Controller;

import Entity.Transaction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionControllerTest {

    @Test
    public void testAddTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        TransactionController controller = new TransactionController();

        Transaction t = new Transaction("Lunch", 20.0, "Food");
        controller.addTransaction(transactions, t);

        assertTrue(transactions.contains(t));
        assertEquals(1, transactions.size());
    }

    @Test
    public void testRemoveTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        TransactionController controller = new TransactionController();

        Transaction t = new Transaction("Rent", 1000.0, "Housing");
        transactions.add(t);
        controller.removeTransaction(transactions, t);

        assertFalse(transactions.contains(t));
        assertEquals(0, transactions.size());
    }
}
