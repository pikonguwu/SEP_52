package Controller;

import Entity.Transaction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link TransactionController}.
 * <p>
 * Verifies that transactions can be added to and removed from a list correctly.
 */
public class TransactionControllerTest {

    /**
     * Tests that invoking {@link TransactionController#addTransaction(List, Transaction)}
     * adds the given transaction to the provided list.
     */
    @Test
    public void testAddTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        TransactionController controller = new TransactionController();

        Transaction t = new Transaction("Lunch", 20.0, "Food");
        controller.addTransaction(transactions, t);

        assertTrue(transactions.contains(t));
        assertEquals(1, transactions.size());
    }

    /**
     * Tests that invoking {@link TransactionController#removeTransaction(List, Transaction)}
     * removes the given transaction from the provided list.
     */
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
