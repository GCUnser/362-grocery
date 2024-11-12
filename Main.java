import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        GroceryStore store = new GroceryStore();
        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart();
        int quantity;
        String name;

        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. Remove Spoiled Items");
            System.out.println("3. View Inventory");
            System.out.println("4. Checkout");
            System.out.println("5. Return Item");
            System.out.println("6. Add Item To Cart");
            System.out.println("7. View Cart");
            System.out.println("8. View Receipt");
            System.out.println("9. Firing");
            System.out.println("10. Hiring");
            System.out.println("11. Payroll");
            System.out.println("12. Sales Records");
            System.out.println("13. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    boolean correctName = false;
                    System.out.print("Enter item name: ");
                    name = scanner.nextLine();
                    while (!correctName) {
                        System.out.println("Is this the correct name of the product you wish to add: " + name);
                        System.out.print("Please answer y or n: ");
                        String answer = scanner.next();
                        if (answer.equalsIgnoreCase("y")) {
                            correctName = true;
                        } else {
                            System.out.print("Enter item name: ");
                            name = scanner.nextLine();
                        }
                    }
                    for (Item i : store.getInventory()) {
                        if (i.getName().compareTo(name.toLowerCase()) == 0) {
                            System.out.println("Item exists in Inventory");
                            System.out.print("Enter item quantity to add: ");
                            quantity = scanner.nextInt();
                            while (quantity <= 0) {
                                System.out.println(
                                        "Invalid quantity to add, please pick add a positive amount of inventory");
                                System.out.print("Enter item quantity to add: ");
                                quantity = scanner.nextInt();
                            }
                            System.out.print("Enter expiration date of the item in form 'YY-mm-dd': ");
                            String date = scanner.next();
                            if (quantity == 1) {
                                i.addQuantity(date);
                            } else if (quantity > 1) {
                                i.addQuantity(date, quantity);
                            }
                            correctName = false;
                            break;
                        }
                    }
                    if (!correctName) {
                        break;
                    }
                    System.out.println("No item exists by that name, please enter it into the Inventory");
                    System.out.print("Enter item price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter if item is Taxable (y or n): ");
                    boolean taxable;
                    if (scanner.next().equalsIgnoreCase("y")) {
                        taxable = true;
                    } else {
                        taxable = false;
                    }
                    System.out.print("Enter if item is applicable for Food Stamps (y or n): ");
                    boolean foodStamp;
                    if (scanner.next().equalsIgnoreCase("y")) {
                        foodStamp = true;
                    } else {
                        foodStamp = false;
                    }
                    scanner.nextLine(); // Consume newline

                    Item item = new Item(name.toLowerCase(), price, taxable, foodStamp);
                    store.addItem(item);
                    System.out.println("Item added successfully!\n");
                    break;

                case 2:
                    System.out.print(
                            "Please enter the date for which you would like to remove spoiled items for in the form 'YY-mm-dd': ");
                    String date = scanner.next();
                    store.removeSpoiled(date);
                    System.out.println("Removed spoiled items from inventory!\n");
                    break;
                case 3:
                    System.out.println("Inventory:");
                    for (Item i : store.getInventory()) {
                        System.out.printf(
                                "%s - $%.2f - Quantity: %d - Taxable: %b - Food Stamp Eligible: %b - Expiration Dates: %s%n",
                                i.getName(), i.getPrice(), i.getQuantity(), i.isTaxable(), i.isFoodStampEligible(),
                                i.getDateList());
                    }
                    System.out.println();
                    break;

                case 4:
                    // System.out.print("Enter item name to buy: ");
                    // String itemName = scanner.nextLine();
                    // System.out.print("Enter quantity to buy: ");
                    // int requestedQuantity = scanner.nextInt();
                    // scanner.nextLine(); // Consume newline
                    int payChoice = 0;
                    boolean validChoice = false;

                    while (!validChoice) {
                        System.out.println("1. Card");
                        System.out.println("2. Gift Card");
                        System.out.println("3. Cash");
                        System.out.println("4. Food Stamps");
                        System.out.print("Choose an option: ");
                        payChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline left-over

                        if (payChoice >= 1 && payChoice <= 4) {
                            validChoice = true;
                        } else {
                            System.out.println("Invalid choice, please try again.");
                        }
                    }
                    System.out.print("Enter the amount you have: $");
                    double userMoney = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    double totalCost = store.calculateCartCost();

                    while (totalCost > userMoney) {
                        System.out.println("You don't have enough money. Let's review your cart.");
                        cart.reviewAndRemoveItems(store, cart, totalCost, userMoney, scanner);
                        totalCost = store.calculateCartCost();
                    }
                    totalCost = store.checkout(payChoice, userMoney);
                    break;
                case 5:
                    System.out.print("Enter the item name you want to return: ");
                    name = scanner.nextLine();
                    System.out.print("Enter the quantity: ");
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    store.returnItem(name, quantity);
                    break;
                case 6:
                    System.out.print("Enter item name to add to cart: ");
                    String itemName = scanner.nextLine();

                    System.out.print("Enter quantity to add: ");
                    int quantityToAdd = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    // Check if the item exists in the inventory before adding to the cart
                    Item itemToAdd = store.getItemByName(itemName);
                    if (itemToAdd != null && itemToAdd.getQuantity() >= quantityToAdd) {
                        cart.addItemToCart(itemName, quantityToAdd); // Add item to the cart
                        System.out.println("Item added to cart successfully!\n");
                    } else {
                        System.out.println("Item not available or insufficient quantity in inventory.\n");
                    }
                    break;
                case 7:
                    Cart.displayCartItems();
                    break;
                case 8:
                    Receipt.viewReceipt();
                    break;
                case 9:
                    // Counts how many times the same violation has occurred
                    int violationCount = 0;

                    Hiring h = new Hiring("TX69", "Molly", "Cashier", true, 13);

                    Firing f = new Firing("UC27", "Leo", true, false, false);

                    HashMap<Integer, String> policies = new HashMap<>();

                    ArrayList<String> names = new ArrayList<>();

                    names.add("Lexi");
                    names.add("Richard");
                    names.add("Lauren");

                    // Insert policies into a HashMap
                    policies.put(1, "absent for work more than 3 times without manager approval");
                    policies.put(2, "verbal harassment and/or violence");
                    policies.put(3, "time fraud");
                    policies.put(4, "Racism");

                    String filePath = "C:\\grocery\\362-grocery\\testFile.txt";

                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        int i = 0;
                        while ((line = br.readLine()) != null) {

                            if (!line.isEmpty()) {
                                int firstChar = Character.getNumericValue(line.charAt(0));

                                // Check if the line is in the HashMap as a key
                                if (policies.containsKey(firstChar)) {
                                    // System.out.println("Found in hashmap: " + line);
                                    violationCount++;
                                    if (violationCount == 1) {
                                        f.warning = true;
                                        System.out.println("WARNING: " + names.get(i));
                                    }
                                    if (violationCount == 2) {
                                        f.meetingWithManager = true;
                                        System.out.println("Schedule a meeting with the manager: " + names.get(i));
                                    }
                                    if (violationCount == 3) {
                                        f.isFired = true;
                                        System.out.println("Fired: " + names.get(i));
                                    }
                                } else {
                                    System.out.println("Not found in hashmap: " + line);
                                }
                            }

                            i++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    HashMap<Integer, String> requirements = new HashMap<>();

                    requirements.put(1, "full-time student status");
                    requirements.put(2, "no criminal charges");

                    ArrayList<String> empNames = new ArrayList<>();

                    // Add names to the ArrayList
                    empNames.add("Leo");

                    empNames.add("Emma");

                    String filePath2 = "C:\\grocery\\362-grocery\\testFile2.txt";

                    try (BufferedReader br = new BufferedReader(new FileReader(filePath2))) {
                        String line;
                        int i = 0;
                        while ((line = br.readLine()) != null) {

                            if (!line.isEmpty()) {
                                int firstChar = Character.getNumericValue(line.charAt(0));

                                // Check if the line is in the HashMap as a key
                                if (requirements.containsKey(firstChar)) {
                                    // System.out.println("Found in hashmap: " + line);
                                    System.out.println(empNames.get(i));
                                }
                            } else {
                                System.out.println("Not found in hashmap: " + line);
                            }
                            i++;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 11:

                    break;

                case 12:

                    break;

                case 13:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
