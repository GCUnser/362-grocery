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
        String category;

        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. Remove Spoiled Items");
            System.out.println("3. View Inventory");
            System.out.println("4. Sort Inventory");
            System.out.println("5. Remove Items From Inventory");
            System.out.println("6. Checkout");
            System.out.println("7. Return Item");
            System.out.println("8. Add Item To Cart");
            System.out.println("9. View Cart");
            System.out.println("10. View Receipt");
            System.out.println("11. Firing");
            System.out.println("12. Hiring");
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
                                System.out.println("Invalid quantity to add, please pick add a positive amount of inventory");
                                System.out.print("Enter item quantity to add: ");
                                quantity = scanner.nextInt();
                            }
                            System.out.print("Enter expiration date of the item in form 'YYYY-mm-dd': ");
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
                    System.out.print("Enter item name: ");
                    category = scanner.nextLine();
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
                    System.out.print("Enter if item requires being 21 or older to purchase (y or n): ");
                    boolean twentyOnePlus;
                    if (scanner.next().equalsIgnoreCase("y")) {
                        twentyOnePlus = true;
                    } else {
                        twentyOnePlus = false;
                    }
                    scanner.nextLine(); // Consume newline

                    Item item = new Item(name.toLowerCase(), category.toLowerCase(), price, taxable, foodStamp, twentyOnePlus);
                    store.addItem(item);
                    System.out.println("Item added successfully!\n");
                    break;

                case 2:
                    System.out.print("Please enter the date for which you would like to remove spoiled items for in the form 'YYYY-mm-dd': ");
                    String date = scanner.next();
                    store.removeSpoiled(date);
                    System.out.println("Removed spoiled items from inventory!\n");
                    break;
                case 3:
                    System.out.println("Inventory:");
                    for (Item i : store.getInventory()) {
                        System.out.printf("%s - %s - $%.2f - Quantity: %d - Taxable: %b - Food Stamp Eligible: %b - Only 21+ can buy: %b -  Expiration Dates: %s%n",
                                i.getName(), i.getPrice(), i.getPrice(), i.getQuantity(), i.isTaxable(), i.isFoodStampEligible(), i.forTwentyOnePlus(), i.getDateList());
                    }
                    System.out.println();
                    break;

                case 4:
                    System.out.println("Please pick which method you would like to sort the inventory by");
                    System.out.println("1. Alphabetical");
                    System.out.println("2. Category");
                    System.out.print("Chose which mode");
                    int toSortBy;
                    if(scanner.hasNextInt() && (toSortBy = scanner.nextInt()) == 1)
                    {
                        System.out.println("Sorting Alphabetically");
                        store.sortInventory(toSortBy);
                    }
                    else if(scanner.hasNextInt() && (toSortBy = scanner.nextInt()) == 2)
                    {
                        System.out.println("Sorting By Category");
                        store.sortInventory(toSortBy);
                    }
                    else
                    {
                        System.out.println("Invalid input, returning to main selection.");
                    }
                    System.out.println("Inventory Sorted.\n");
                    break;

                case 5:
                    System.out.println("Would you like to remove all items with zero quantity, or remove a specific item?");
                    System.out.println("1. Remove all items with zero quantity");
                    System.out.println("2. Remove one specific item");
                    System.out.print("Please select the option you would like to perform: ");
                    String removeChoice = scanner.nextLine();
                    int countRemoved;
                    switch (removeChoice){
                        case "1":
                            countRemoved = store.removeZeroItems();
                            System.out.println("Removed " + countRemoved + "items.\n");
                            break;
                        case "2":
                            System.out.println("Please enter the name of the item you would like to remove: ");
                            name = scanner.nextLine();
                            countRemoved = store.removeItem(name);
                            if(countRemoved == 0)
                            {
                                System.out.println("No item with name " + name + " found, returning.");
                            }
                            else if(countRemoved == 1)
                            {
                                System.out.println("Successfully removed item.\n");
                            }
                            break;
                        default:
                            System.out.println("No valid input, returning to main selection.\n");
                    }



                    break;

                case 6:
//                    System.out.print("Enter item name to buy: ");
//                    String itemName = scanner.nextLine();
//                    System.out.print("Enter quantity to buy: ");
//                    int requestedQuantity = scanner.nextInt();
//                    scanner.nextLine(); // Consume newline
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
                case 7:
                    System.out.print("Enter the item name you want to return: ");
                    name = scanner.nextLine();
                    System.out.print("Enter the quantity: ");
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    store.returnItem(name, quantity);
                    break;
                case 8:
                    System.out.print("Enter item name to add to cart: ");
                    String itemName = scanner.nextLine();

                    System.out.print("Enter quantity to add: ");
                    int quantityToAdd = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    // Check if the item exists in the inventory before adding to the cart
                    Item itemToAdd = store.getItemByName(itemName);
                    if (itemToAdd != null && itemToAdd.getQuantity() >= quantityToAdd) {
                        cart.addItemToCart(itemName, quantityToAdd);  // Add item to the cart
                        System.out.println("Item added to cart successfully!\n");
                    } else {
                        System.out.println("Item not available or insufficient quantity in inventory.\n");
                    }
                    break;
                case 9:
                    Cart.displayCartItems();
                    break;
                case 10:
                    Receipt.viewReceipt();
                    break;
                case 11:
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
                                    //System.out.println("Found in hashmap: " + line);
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
                case 12:
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
                                    //System.out.println("Found in hashmap: " + line);
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

