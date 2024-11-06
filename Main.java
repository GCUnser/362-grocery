import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        GroceryStore store = new GroceryStore();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Checkout");
            System.out.println("4. Exit");
            System.out.println("5. Firing");
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

                    double totalCost = store.checkout(itemName, requestedQuantity);
                    if (totalCost > 0) {
                        System.out.printf("Total cost for %s: $%.2f%n", itemName, totalCost);
                    }
                    break;

                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                case 5:
                    // Counts how many times the same violation has occurred
                    int violationCount = 0;

                    Hiring h = new Hiring("TX69", "Molly", "Cashier", true, 13);
                    Hiring h2 = new Hiring("TX69", "Molly", "Cashier", true, 13);
                    Firing f = new Firing("UC27", "Leo", true, false, false);
                    Firing f2 = new Firing("UC27", "Leo", true, false, false);

                    HashMap<Integer, String> policies = new HashMap<>();

                    // Insert policies into a HashMap
                    policies.put(1, "absent for work more than 3 times without manager approval");
                    policies.put(2, "discrimination on the basis of religion, race, linguistics, beliefs, culture or sex");
                    policies.put(3, "verbal harassment and/or violence");
                    policies.put(4, "sexual assault");
                    policies.put(5, "time fraud");

                    String filePath = "C:\\grocery\\362-grocery\\testFile.txt";

                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {

                            if (!line.isEmpty()) {
                                int firstChar = Character.getNumericValue(line.charAt(0));

                                // Check if the line is in the HashMap as a key
                                if (policies.containsKey(firstChar)) {
                                    //System.out.println("Found in hashmap: " + line);
                                    violationCount++;
                                    if (violationCount == 1) {
                                        f.warning = true;
                                        System.out.println("WARNING: " + f.employeeName);
                                    } else if (violationCount == 2) {
                                        f.meetingWithManager = true;
                                        System.out.println("Schedule a meeting with the manager: " + f.employeeName);
                                    } else if (violationCount == 3) {
                                        f.isFired = true;
                                        System.out.println("Fired: " + f.employeeName);
                                    }
                                } else {
                                    System.out.println("Not found in hashmap: " + line);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }
}
