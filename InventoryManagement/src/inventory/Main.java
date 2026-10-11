package inventory;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        InventoryService inventory = new InventoryService();
        Bill bill = new Bill();

        Product p1 = new Product(101, "Laptop", Category.LAPTOP, 55000, 10);
        Product p2 = new Product(102, "Mobile", Category.MOBILE, 25000, 8);
        Product p3 = new Product(103, "Keyboard", Category.ACCESSORY, 2000, 3);

        inventory.addProduct(p1);
        inventory.addProduct(p2);
        inventory.addProduct(p3);

        int choice;

        do {
            System.out.println("\n===== Inventory Management System =====");
            System.out.println("1. Add Product");
            System.out.println("2. Search Product");
            System.out.println("3. Update Price");
            System.out.println("4. Add Stock");
            System.out.println("5. Sell Product");
            System.out.println("6. Show All Products");
            System.out.println("7. Show Low Stock");
            System.out.println("8. Calculate Bill");
            System.out.println("9. Delete Product");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

            case 1:
                System.out.print("Enter Product ID: ");
                int id = sc.nextInt();

                System.out.print("Enter Product Name: ");
                String name = sc.next();

                System.out.println("1. LAPTOP");
                System.out.println("2. MOBILE");
                System.out.println("3. ACCESSORY");
                System.out.println("4. HOME_APPLIANCE");
                System.out.print("Enter Category: ");
                int categoryChoice = sc.nextInt();

                Category category;

                if (categoryChoice == 1) {
                    category = Category.LAPTOP;
                } else if (categoryChoice == 2) {
                    category = Category.MOBILE;
                } else if (categoryChoice == 3) {
                    category = Category.ACCESSORY;
                } else {
                    category = Category.HOME_APPLIANCE;
                }

                System.out.print("Enter Price: ");
                double price = sc.nextDouble();

                System.out.print("Enter Stock: ");
                int stock = sc.nextInt();

                Product product = new Product(id, name, category, price, stock);
                inventory.addProduct(product);

                System.out.println("Product added successfully");
                break;

            case 2:
                System.out.print("Enter Product ID: ");
                int searchId = sc.nextInt();

                Product found = inventory.searchProduct(searchId);

                if (found != null) {
                    System.out.println("ID: " + found.getProductId());
                    System.out.println("Name: " + found.getProductName());
                    System.out.println("Category: " + found.getCategory());
                    System.out.println("Price: " + found.getPrice());
                    System.out.println("Stock: " + found.getStock());
                } else {
                    System.out.println("Product not found");
                }
                break;

            case 3:
                System.out.print("Enter Product ID: ");
                int priceId = sc.nextInt();

                System.out.print("Enter New Price: ");
                double newPrice = sc.nextDouble();

                inventory.updateProductPrice(priceId, newPrice);
                break;

            case 4:
                System.out.print("Enter Product ID: ");
                int stockId = sc.nextInt();

                System.out.print("Enter Quantity: ");
                int quantity = sc.nextInt();

                inventory.addStock(stockId, quantity);
                break;

            case 5:
                System.out.print("Enter Product ID: ");
                int sellId = sc.nextInt();

                System.out.print("Enter Quantity: ");
                int sellQuantity = sc.nextInt();

                inventory.sellProduct(sellId, sellQuantity);
                break;

            case 6:
                inventory.showAllProducts();
                break;

            case 7:
                inventory.showLowStock();
                break;

            case 8:
                System.out.print("Enter Product ID: ");
                int billId = sc.nextInt();

                System.out.print("Enter Quantity: ");
                int billQuantity = sc.nextInt();

                Product billProduct = inventory.searchProduct(billId);
                double total = bill.calculateTotal(billProduct, billQuantity);

                System.out.println("Bill Total: " + total);
                break;

            case 9:
                System.out.print("Enter Product ID: ");
                int deleteId = sc.nextInt();

                inventory.deleteProduct(deleteId);
                break;

            case 10:
                System.out.println("Thank you");
                break;

            default:
                System.out.println("Invalid choice");
            }

        } while (choice != 10);

        sc.close();
    }
}