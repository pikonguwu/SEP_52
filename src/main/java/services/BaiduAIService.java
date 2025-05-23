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
 * The service class used to invoke the Baidu Wenxin Yiyan API.
 * This class provides the functionality of obtaining access tokens and obtaining AI responses.
 */
public class BaiduAIService {
    /**
     * The API key for accessing the Baidu API, used for authentication.
     */
    // private static final String API_KEY = "你的API_KEY";
    private static final String API_KEY = "fsc3SLgibtm12hRyO6LYSiQc";

    /**
     * The access key for accessing the Baidu API, used for authentication.
     * Note: The "Kfeak" in the key may be a spelling error, please check and correct it.
     */
    private static final String SECRET_KEY = "yTZ5k2rdFsUjxIYniDjgKbH3ffaIoqOQ";

    // private static final String SECRET_KEY = "你的SECRET_KEY";
    /**
     * The URL for obtaining the access token, requested through the API key and secret key.
     */

    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
    /**
     * The base URL for the Baidu Wenxin Yiyan API, used to send chat requests.
     */
    private static final String API_BASE_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-lite-8k";

    // private static final String API_BASE_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";

    /**
     * The method for obtaining the access token.
     * Send a request to the Baidu API to obtain an access token for subsequent API calls.
     *
     * @return The access token string
     * @throws IOException If an I/O error occurs when sending the request or processing the response
     * @throws InterruptedException If the thread is interrupted while waiting for the response
     */
    private String getAccessToken() throws IOException, InterruptedException {
        // Create a new HTTP client
        HttpClient client = HttpClient.newHttpClient();
        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ACCESS_TOKEN_URL))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Use Gson to parse the response body to a Map
        Map<String, Object> result = new Gson().fromJson(response.body(), new com.google.gson.reflect.TypeToken<HashMap<String, Object>>(){}.getType());

        if (result.containsKey("error")) {
            throw new IOException("Failed to get access token: " + result.get("error_description"));
        }

        // Extract the access token from the result and return it
        return (String) result.get("access_token");
    }

    /**
     * The method of obtaining AI responses.
     * First, obtain the access token, then build the request body and send the request to the Baidu Wenxin Yiyan API, and finally return the response from the AI.
     *
     * @param message The chat message entered by the user
     * @return The response string of AI returns an error message if an error occurs
     */
    public String getAIResponse(String message) {
        try {
            // Obtain the access token
            String accessToken = getAccessToken();
            // Build the complete API request URL
            String apiUrl = API_BASE_URL + "?access_token=" + accessToken;

            // Build a message list
            List<Map<String, String>> messages = new ArrayList<Map<String, String>>();
            Map<String, String> userMessage = new HashMap<String, String>();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.add(userMessage);

            // Build the request body
            Map<String, Object> requestBodyMap = new HashMap<String, Object>();
            requestBodyMap.put("messages", messages);
            // Use Gson to convert the request body to a JSON string
            String requestBody = new Gson().toJson(requestBodyMap);

            // Create an HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Send a request and obtain a response
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check whether the response contains errors
            Map<String, Object> responseMap = new Gson().fromJson(response.body(), new com.google.gson.reflect.TypeToken<HashMap<String, Object>>(){}.getType());
            if (responseMap.containsKey("error")) {
                throw new IOException("API Error: " + responseMap.get("error"));
            }

            // Return the response body
            return response.body();
        } catch (IOException | InterruptedException e) {
            // Print the abnormal stack information
            e.printStackTrace();
            // Return error message
            return "Error: " + e.getMessage();
        }
    }
}