package services;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class TransactionDataService {
    private List<Map<String, Object>> transactions = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public void addTransaction(String date, String description, String amount, String type) {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("date", date);
        transaction.put("description", description);
        transaction.put("amount", parseAmount(amount));
        transaction.put("type", type);
        
        transactions.add(transaction);
    }
    
    public void updateTransaction(int index, String date, String description, String amount, String type) {
        if (index >= 0 && index < transactions.size()) {
            Map<String, Object> transaction = transactions.get(index);
            transaction.put("date", date);
            transaction.put("description", description);
            transaction.put("amount", parseAmount(amount));
            transaction.put("type", type);
        }
    }
    
    private double parseAmount(String amount) {
        // 移除$符号、逗号和加号，保留减号表示支出
        return Double.parseDouble(amount.replace("$", "")
                                      .replace(",", "")
                                      .replace("+", ""));
    }
    
    // 获取一周内各天的支出数据
    public Map<String, Double> getWeeklySpending() {
        Map<String, Double> weeklyData = new LinkedHashMap<>();
        // 初始化一周每天的数据
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : days) {
            weeklyData.put(day, 0.0);
        }
        
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        
        // 处理每条交易
        for (Map<String, Object> transaction : transactions) {
            try {
                String dateStr = (String)transaction.get("date");
                Date transDate = dateFormat.parse(dateStr);
                
                // 设置日历对象为交易日期
                calendar.setTime(transDate);
                
                // 获取星期几（1=周日，2=周一...7=周六）
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String dayName = days[(dayOfWeek + 5) % 7]; // 转换为Mon-Sun
                
                // 如果是支出类型，累加金额
                if ("Expense".equals(transaction.get("type"))) {
                    double amount = Math.abs((Double)transaction.get("amount"));
                    weeklyData.put(dayName, weeklyData.get(dayName) + amount);
                }
            } catch (ParseException e) {
                // 日期解析错误，忽略此交易
                System.out.println("Error parsing date: " + e.getMessage());
            }
        }
        
        return weeklyData;
    }
    
    // 获取各类支出的占比数据
    public Map<String, Double> getExpenseCategories() {
        Map<String, Double> categoryData = new HashMap<>();
        
        // 根据交易描述推断分类
        for (Map<String, Object> transaction : transactions) {
            if ("Expense".equals(transaction.get("type"))) {
                String description = ((String)transaction.get("description")).toLowerCase();
                String category = getCategoryFromDescription(description);
                double amount = Math.abs((Double)transaction.get("amount"));
                
                // 累加到相应分类
                categoryData.put(category, categoryData.getOrDefault(category, 0.0) + amount);
            }
        }
        
        return categoryData;
    }
    
    private String getCategoryFromDescription(String description) {
        // 基于关键词推断分类
        if (description.contains("rent") || description.contains("mortgage") || description.contains("house")) {
            return "Housing";
        } else if (description.contains("food") || description.contains("grocery") || description.contains("restaurant")) {
            return "Food";
        } else if (description.contains("car") || description.contains("gas") || description.contains("transport")) {
            return "Transport";
        } else if (description.contains("movie") || description.contains("entertainment") || description.contains("game")) {
            return "Entertainment";
        } else if (description.contains("saving") || description.contains("deposit") || description.contains("investment")) {
            return "Savings";
        } else {
            return "Others";
        }
    }
    
    public List<Map<String, Object>> getTransactions() {
        return transactions;
    }
}