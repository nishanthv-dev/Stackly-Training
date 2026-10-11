package inventory;

public class Product {

    private int productId;
    private String productName;
    private Category category;
    private double price;
    private int stock;

    public Product(int productId, String productName, Category category, double price, int stock) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Category getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addStock(int quantity) {
        stock = stock + quantity;
    }

    public void sellProduct(int quantity) throws OutOfStockException {
        if (quantity <= stock) {
            stock = stock - quantity;
        } else {
            throw new OutOfStockException("Insufficient stock");
        }
    }
}