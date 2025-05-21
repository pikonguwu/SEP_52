package services;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaiduAIServiceTest {

    @Test
    public void testGetAIResponse() {
        BaiduAIService ai = new BaiduAIService();
        String result = ai.getAIResponse("请帮我制定一个节省支出的计划");

        System.out.println("API返回结果：" + result);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.startsWith("Error:")); // 不应该返回错误信息
    }
}
