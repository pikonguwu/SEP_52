package services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BaiduAIService.
 * This class contains unit tests for various methods of BaiduAIService,
 * such as text summarization, sentiment analysis, and keyword extraction.
 */
public class BaiduAIServiceTest {

    /**
     * Tests the text summarization functionality.
     * It verifies whether the summary contains expected keywords.
     */
    @Test
    public void testTextSummarization() {
        String text = "Baidu is a Chinese multinational technology company specializing in Internet-related services, products, and artificial intelligence.";
        String summary = BaiduAIService.summarizeText(text);
        assertNotNull(summary);
        assertTrue(summary.contains("Baidu") || summary.contains("technology"));
    }

    /**
     * Tests the sentiment analysis functionality.
     * It ensures that the sentiment score is within the expected range.
     */
    @Test
    public void testSentimentAnalysis() {
        String text = "I am very happy with the service provided by Baidu.";
        double score = BaiduAIService.analyzeSentiment(text);
        assertTrue(score >= 0.0 && score <= 1.0);
    }

    /**
     * Tests the keyword extraction functionality.
     * It checks that the extracted keywords are not null and have expected size.
     */
    @Test
    public void testKeywordExtraction() {
        String text = "Artificial intelligence and machine learning are core technologies at Baidu.";
        String[] keywords = BaiduAIService.extractKeywords(text);
        assertNotNull(keywords);
        assertTrue(keywords.length > 0);
    }
}
