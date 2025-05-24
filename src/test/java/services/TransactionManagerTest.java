package services;

// import org.junit.Test;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

// import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link TransactionManager}.
 * This class verifies whether adding a transaction correctly affects the
 * internal transaction data.
 */
public class TransactionManagerTest {

    /**
     * Tests whether adding a transaction updates the transaction list.
     * It ensures that the newly added transaction is present in the retrieved list.
     */
    @Test
    public void testAddTransactionAffectsData() {
        TransactionManager manager = TransactionManager.getInstance();
        manager.addTransaction("18/05/2025", "test food", "88.8", "Expense");

        List<Map<String, Object>> all = manager.getAllTransactions();
        assertTrue(all.stream().anyMatch(t -> "test food".equals(t.get("description"))));
    }
}
