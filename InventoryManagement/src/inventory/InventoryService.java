package inventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryService {

    Map<Integer, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    public Product searchProduct(int productId) {
        return products.get(productId);
    }

    public void updateProductPrice(int productId, double newPrice) {
        Product product = products.get(productId);

        if (product != null) {
            product.setPrice(newPrice);
            System.out.println("Price updated successfully");
        } else {
            System.out.println("Product not found");
        }
    }

    public void addStock(int productId, int quantity) {
        Product product = products.get(productId);

        if (product != null) {
            product.addStock(quantity);
            System.out.println("Stock added successfully");
        } else {
            System.out.println("Product not found");
        }
    }

    public void sellProduct(int productId, int quantity) {
        Product product = products.get(productId);

        if (product != null) {
            try {
                product.sellProduct(quantity);
                System.out.println("Product sold successfully");
            } catch (OutOfStockException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Product not found");
        }
    }

    public void showAllProducts() {
        for (Product product : products.values()) {
            System.out.println("ID: " + product.getProductId());
            System.out.println("Name: " + product.getProductName());
            System.out.println("Category: " + product.getCategory());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Stock: " + product.getStock());
            System.out.println("----------------------");
        }
    }

    public void showLowStock() {
        for (Product product : products.values()) {
            if (product.getStock() < 5) {
                System.out.println(product.getProductName() + " - Stock: " + product.getStock());
            }
        }
    }

    public void deleteProduct(int productId) {
        if (products.containsKey(productId)) {
            products.remove(productId);
            System.out.println("Product deleted successfully");
        } else {
            System.out.println("Product not found");
        }
    }
}