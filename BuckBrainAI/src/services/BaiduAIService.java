package services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

/**
 * 用于调用百度文心一言 API 的服务类。
 * 该类提供了获取访问令牌和获取 AI 响应的功能。
 */
public class BaiduAIService {
    /**
     * 访问百度 API 的密钥，用于身份验证。
     */
    private static final String API_KEY = "fsc3SLgibtm12hRyO6LYSiQc";
    /**
     * 访问百度 API 的密钥，用于身份验证。
     * 注意：该密钥中的 "Kfeak" 可能是拼写错误，建议检查并修正。
     */
    private static final String SECRET_KEY = "yTZ5k2rdFsUjxIYniDjgKbH3ffaIoqOQ";
    /**
     * 用于获取访问令牌的 URL，通过 API 密钥和密钥来请求。
     */
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
    /**
     * 百度文心一言 API 的基础 URL，用于发送聊天请求。
     */
    private static final String API_BASE_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-lite-8k";
    // private static final String API_BASE_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/app-pU7SeP20";

    /**
     * 获取访问令牌的方法。
     * 向百度 API 发送请求，获取用于后续 API 调用的访问令牌。
     * 
     * @return 访问令牌字符串
     * @throws IOException 如果在发送请求或处理响应时发生 I/O 错误
     * @throws InterruptedException 如果在等待响应时线程被中断
     */
    private String getAccessToken() throws IOException, InterruptedException {
        // 创建一个新的 HTTP 客户端
        HttpClient client = HttpClient.newHttpClient();
        // 构建 HTTP 请求
        HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(ACCESS_TOKEN_URL))
               .header("Content-Type", "application/json")
               .header("Accept", "application/json")
               .POST(HttpRequest.BodyPublishers.ofString(""))
               .build();

        // 发送请求并获取响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // 使用 Gson 将响应体解析为 Map
        Map<String, Object> result = new Gson().fromJson(response.body(), HashMap.class);
        // 从结果中提取访问令牌并返回
        return (String) result.get("access_token");
    }

    /**
     * 获取 AI 响应的方法。
     * 先获取访问令牌，然后构建请求体并发送请求到百度文心一言 API，最后返回 AI 的响应。
     * 
     * @param message 用户输入的聊天消息
     * @return AI 的响应字符串，如果发生错误则返回错误信息
     */
    public String getAIResponse(String message) {
        try {
            String accessToken = getAccessToken();
            String apiUrl = API_BASE_URL + "?access_token=" + accessToken;
    
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.add(userMessage);
    
            Map<String, Object> requestBodyMap = new HashMap<>();
            requestBodyMap.put("messages", messages);
            String requestBody = new Gson().toJson(requestBodyMap);
    
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
    
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
            // 修复：正确解析JSON响应中的result字段
            Map<String, Object> resultMap = new Gson().fromJson(response.body(), HashMap.class);
            return (String) resultMap.get("result");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "请求出现错误：" + e.getMessage();
        }
    }
}
