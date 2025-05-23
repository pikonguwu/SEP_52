package services;

import org.junit.Test;
import services.TransactionDataService;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit test class for {@link TransactionDataService}.
 * This class tests the ability to add transactions, categorize expenses,
 * and compute weekly spending.
 */
public class TransactionDataServiceTest {

    /**
     * Tests the ability to add a transaction and retrieve weekly spending.
     * It checks whether the total weekly spending includes the added amount.
     */
    @Test
    public void testAddTransactionAndGetWeeklySpending() {
        TransactionDataService service = new TransactionDataService();
        service.addTransaction("18/05/2025", "rent", "1000", "Expense");

        Map<String, Double> weekly = service.getWeeklySpending();
        double total = weekly.values().stream().mapToDouble(Double::doubleValue).sum();

        assertTrue(total >= 1000);
    }

    /**
     * Tests automatic expense category recognition based on transaction description.
     * Verifies that "buy grocery" is categorized under "Food" with the correct amount.
     */
    @Test
    public void testCategoryRecognition() {
        TransactionDataService service = new TransactionDataService();
        service.addTransaction("18/05/2025", "buy grocery", "300", "Expense");

        Map<String, Double> categories = service.getExpenseCategories();

        System.out.println("Category result: " + categories); // Debug output
        assertTrue(categories.containsKey("Food"));
        assertEquals(300.0, categories.get("Food"), 0.01);
    }
}
