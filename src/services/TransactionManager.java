package services;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * 交易数据管理器 - 单例模式
 * 负责管理所有交易数据并通知各个视图更新
 */
public class TransactionManager {
    private static TransactionManager instance;
    private List<TransactionListener> listeners = new ArrayList<>();
    private TransactionDataService dataService;
    
    private TransactionManager() {
        dataService = new TransactionDataService();
    }
    
    public static synchronized TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }
    
    /**
     * 添加交易监听器
     */
    public void addListener(TransactionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * 添加交易记录
     */
    public void addTransaction(String date, String description, String amount, String type) {
        // 添加到数据服务
        dataService.addTransaction(date, description, amount, type);
        
        // 通知所有监听器
        for (TransactionListener listener : listeners) {
            listener.onTransactionAdded(date, description, amount, type);
        }
    }
    
    /**
     * 更新交易记录
     */
    public void updateTransaction(int index, String date, String description, String amount, String type) {
        // 获取旧数据
        Map<String, Object> oldTransaction = dataService.getTransactions().get(index);
        String oldDate = (String) oldTransaction.get("date");
        String oldDescription = (String) oldTransaction.get("description");
        double oldAmountValue = (Double) oldTransaction.get("amount");
        String oldType = (String) oldTransaction.get("type");
        
        // 格式化旧金额
        String oldAmount = String.format("%.2f", oldAmountValue);
        
        // 更新数据服务
        dataService.updateTransaction(index, date, description, amount, type);
        
        // 通知所有监听器
        for (TransactionListener listener : listeners) {
            listener.onTransactionUpdated(
                oldDate, oldDescription, oldAmount, oldType,
                date, description, amount, type
            );
        }
    }
    
    /**
     * 移除交易记录
     */
    public void removeTransaction(String date, String description, String amount, String type) {
        // 查找匹配的交易记录
        List<Map<String, Object>> transactions = dataService.getTransactions();
        int indexToRemove = -1;
        
        for (int i = 0; i < transactions.size(); i++) {
            Map<String, Object> transaction = transactions.get(i);
            String transDate = (String) transaction.get("date");
            String transDesc = (String) transaction.get("description");
            double transAmount = (Double) transaction.get("amount");
            String transType = (String) transaction.get("type");
            
            if (date.equals(transDate) && description.equals(transDesc) && 
                Math.abs(transAmount - Double.parseDouble(amount)) < 0.01 && type.equals(transType)) {
                indexToRemove = i;
                break;
            }
        }
        
        // 如果找到匹配的记录，从列表中移除
        if (indexToRemove >= 0) {
            transactions.remove(indexToRemove);
            
            // 通知所有监听器
            for (TransactionListener listener : listeners) {
                listener.onTransactionRemoved(date, description, amount, type);
            }
        }
    }
    
    /**
     * 获取所有交易记录
     */
    public List<Map<String, Object>> getAllTransactions() {
        return dataService.getTransactions();
    }
    
    /**
     * 获取每周支出数据
     */
    public Map<String, Double> getWeeklySpending() {
        return dataService.getWeeklySpending();
    }
    
    /**
     * 获取支出分类数据
     */
    public Map<String, Double> getExpenseCategories() {
        return dataService.getExpenseCategories();
    }
    
    /**
     * 交易监听器接口
     */
    public interface TransactionListener {
        void onTransactionAdded(String date, String description, String amount, String type);
        void onTransactionUpdated(
            String oldDate, String oldDescription, String oldAmount, String oldType,
            String newDate, String newDescription, String newAmount, String newType
        );
        void onTransactionRemoved(String date, String description, String amount, String type);
    }
}