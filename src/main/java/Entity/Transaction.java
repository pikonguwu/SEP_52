package Entity;

/**
 * Represents a financial transaction in the system.
 * This class encapsulates the essential properties of a transaction:
 * description, amount, and category.
 */
public class Transaction {
    /** The description or purpose of the transaction */
    private String description;
    
    /** The monetary amount of the transaction */
    private double amount;
    
    /** The category or type of the transaction (e.g., income, expense) */
    private String category;

    /**
     * Constructs a new Transaction with the specified details.
     *
     * @param description The description or purpose of the transaction
     * @param amount The monetary amount of the transaction
     * @param category The category or type of the transaction
     */
    public Transaction(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    /**
     * Gets the description of the transaction.
     *
     * @return The transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     *
     * @param description The new description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the amount of the transaction.
     *
     * @return The transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction.
     *
     * @param amount The new amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the category of the transaction.
     *
     * @return The transaction category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the transaction.
     *
     * @param category The new category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns a string representation of the transaction.
     * The format is: Transaction{description='...', amount=..., category='...'}
     *
     * @return A string containing the transaction details
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "description='" + description + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                '}';
    }
}