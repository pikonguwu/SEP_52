package Entity;

public class Transaction {
    private String description;
    private double amount;
    private String category;

    // Constructor
    public Transaction(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "description='" + description + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                '}';
    }
}
