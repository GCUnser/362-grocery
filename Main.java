import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GroceryStore store = new GroceryStore();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Checkout");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter item price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter item quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Item item = new Item(name, price, quantity);
                    store.addItem(item);
                    System.out.println("Item added successfully!\n");
                    break;

                case 2:
                    System.out.println("Inventory:");
                    for (Item i : store.getInventory()) {
                        System.out.printf("%s - $%.2f - Quantity: %d%n", i.getName(), i.getPrice(), i.getQuantity());
                    }
                    System.out.println();
                    break;

                case 3:
                    System.out.print("Enter item name to buy: ");
                    String itemName = scanner.nextLine();
                    System.out.print("Enter quantity to buy: ");
                    int requestedQuantity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    double totalCost = store.checkout(itemName, requestedQuantity) * 1.07; //1.07 is tax
                    if (totalCost > 0) {
                        System.out.printf("Total cost for %s: $%.2f%n", itemName, totalCost);
                    }
                    break;

                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
