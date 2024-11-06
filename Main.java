import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GroceryStore store = new GroceryStore();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Checkout");
            System.out.println("4. Return Item");
            System.out.println("5. Exit");
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

                    System.out.println("Is the item taxable? (T/F): ");
                    String taxInput = scanner.next();
                    boolean taxable = taxInput.equalsIgnoreCase("T") || taxInput.equalsIgnoreCase("true");
                    scanner.nextLine(); // Consume newline

                    System.out.println("Is the item food-stamp eligible? (T/F): ");
                    String foodInput = scanner.next();
                    boolean foodStampEligible = foodInput.equalsIgnoreCase("T") || foodInput.equalsIgnoreCase("true");
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter expiration date for all items (e.g., DD/MM/YYYY): ");
                    String expirationDate = scanner.nextLine();

                    // Create an ArrayList with the same expiration date repeated for the quantity of items
                    ArrayList<String> expirationDates = new ArrayList<>();
                    for (int i = 0; i < quantity; i++) {
                        expirationDates.add(expirationDate);
                    }

                    // Create the item and add to the store's inventory
                    Item item = new Item(name, price, taxable, foodStampEligible, quantity, expirationDates);
                    store.addItem(item);
                    System.out.println("Item added successfully!\n");
                    break;

                case 2:
                    System.out.println("Inventory:");
                    for (Item i : store.getInventory()) {
                        System.out.printf("%s - $%.2f - Quantity: %d - Taxable: %b - Food Stamp Eligible: %b - Expiration Dates: %s%n",
                                i.getName(), i.getPrice(), i.getQuantity(), i.isTaxable(), i.isFoodStampEligible(), i.getDateList());
                    }
                    System.out.println();
                    break;

                case 3:
//                    System.out.print("Enter item name to buy: ");
//                    String itemName = scanner.nextLine();
//                    System.out.print("Enter quantity to buy: ");
//                    int requestedQuantity = scanner.nextInt();
//                    scanner.nextLine(); // Consume newline
                    System.out.println("1. Card");
                    System.out.println("2. Gift Card");
                    System.out.println("3. Cash");
                    System.out.println("4. Food Stamps");
                    System.out.print("Choose an option (default cash): ");
                    int payChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over

                    double totalCost = store.checkout(payChoice);
                    if (totalCost > 0) {
                        System.out.printf("Total cost for cart: $%.2f%n", totalCost);
                    }
                    break;
                case 4:
                    System.out.print("Enter the item name you want to return: ");
                    name = scanner.nextLine();
                    System.out.print("Enter the quantity: ");
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    store.returnItem(name, quantity);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
