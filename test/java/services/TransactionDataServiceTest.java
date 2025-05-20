package services;

import org.junit.Test;

import services.TransactionDataService;

import java.util.Map;
import static org.junit.Assert.*;

public class TransactionDataServiceTest {

    @Test
    public void testAddTransactionAndGetWeeklySpending() {
        TransactionDataService service = new TransactionDataService();
        service.addTransaction("18/05/2025", "rent", "1000", "Expense");

        Map<String, Double> weekly = service.getWeeklySpending();
        double total = weekly.values().stream().mapToDouble(Double::doubleValue).sum();

        assertTrue(total >= 1000);
    }

    @Test
    public void testCategoryRecognition() {
        TransactionDataService service = new TransactionDataService();
        service.addTransaction("18/05/2025", "buy grocery", "300", "Expense");

        Map<String, Double> categories = service.getExpenseCategories();

        System.out.println("分类结果：" + categories); // 调试用
        assertTrue(categories.containsKey("Food"));
        assertEquals(300.0, categories.get("Food"), 0.01);
    }

}
