package services;

import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TransactionManagerTest {

    @Test
    public void testAddTransactionAffectsData() {
        TransactionManager manager = TransactionManager.getInstance();
        manager.addTransaction("18/05/2025", "test food", "88.8", "Expense");

        List<Map<String, Object>> all = manager.getAllTransactions();
        assertTrue(all.stream().anyMatch(t -> "test food".equals(t.get("description"))));
    }
}
