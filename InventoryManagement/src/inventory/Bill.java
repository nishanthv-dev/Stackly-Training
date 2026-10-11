package inventory;

public class Bill {

	public double calculateTotal(Product product, int quantity) {
	    if (product != null && quantity <= product.getStock()) {
	        return product.getPrice() * quantity;
	    }

	    System.out.println("Product not available");
	    return 0;
	}
}