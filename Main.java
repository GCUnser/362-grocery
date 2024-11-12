import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
            System.out.println("11. Exit");
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
                    System.out.print("Please enter the date for which you would like to remove spoiled items for in the form 'YY-mm-dd': ");
                    String date = scanner.next();
                    store.removeSpoiled(date);
                    System.out.println("Removed spoiled items from inventory!\n");
                    break;
                case 3:
                    System.out.println("Inventory:");
                    for (Item i : store.getInventory()) {
                        System.out.printf("%s - $%.2f - Quantity: %d - Taxable: %b - Food Stamp Eligible: %b - Expiration Dates: %s%n",
                                i.getName(), i.getPrice(), i.getQuantity(), i.isTaxable(), i.isFoodStampEligible(), i.getDateList());
                    }
                    System.out.println();
                    break;

                case 4:
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
                        cart.addItemToCart(itemName, quantityToAdd);  // Add item to the cart
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
                    // Create the HashMap for policies with keys and associated information
                    HashMap<Integer, List<String>> policies = new HashMap<>();

                    // Add multiple values for each key
                    List<String> policy1 = new ArrayList<>();
                    policy1.add("absent for work more than 3 times without manager approval");
                    policy1.add("Lauren");

                    List<String> policy2 = new ArrayList<>();
                    policy2.add("verbal harassment");
                    policy2.add("Jake");

                    List<String> policy3 = new ArrayList<>();
                    policy3.add("time fraud");
                    policy3.add("Molly");

                    List<String> policy4 = new ArrayList<>();
                    policy4.add("Racism");
                    policy4.add("Chase");

                    // Put the lists into the HashMap
                    policies.put(1, policy1);
                    policies.put(2, policy2);
                    policies.put(3, policy3);
                    policies.put(4, policy4);

                    // Create a BufferedReader to read user input
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                    try {
                        // Ask the user to pick a key (1, 2, 3, or 4)
                        System.out.println("Enter a key (1, 2, 3, or 4) to view the corresponding violation information:");
                        String userInput = reader.readLine();

                        // Convert the input to an integer key
                        int key = Integer.parseInt(userInput.trim());

                        // Check if the entered key exists in the HashMap
                        if (policies.containsKey(key)) {
                            // Read the dataFile.txt to count occurrences of the key
                            String fileName = "dataFile.txt";
                            int violationCount = 0;

                            // Read the file and count occurrences of the selected key
                            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    // Trim and check if the line starts with the key followed by a comma (e.g., "1,")
                                    line = line.trim();
                                    if (!line.isEmpty()) {
                                        String[] parts = line.split(",");
                                        if (parts.length > 0) {
                                            try {
                                                int currentKey = Integer.parseInt(parts[0].trim());
                                                if (currentKey == key) {
                                                    violationCount++;  // Increment if the key is found
                                                }
                                            } catch (NumberFormatException e) {
                                                // Handle the case where the key part is not an integer
                                                System.out.println("Error parsing key from line: " + line);
                                            }
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Get the policy details for the given key
                            List<String> policyDetails = policies.get(key);
                            String policyDescription = policyDetails.get(0);
                            String employeeName = policyDetails.get(1);

                            // Define the action based on the violation count
                            String violationAction;
                            if (violationCount == 1) {
                                violationAction = "Warning for " + employeeName;
                            } else if (violationCount == 2) {
                                violationAction = "Meeting scheduled with manager for " + employeeName;
                            } else if (violationCount >= 3) {
                                violationAction = "Fired " + employeeName;
                            } else {
                                violationAction = "No violations found for " + employeeName;
                            }

                            // Write the violation information to output1.txt
                            String outputFileName = "output1.txt";
                            StringBuilder contentToWrite = new StringBuilder();
                            contentToWrite.append("Violation: ").append(policyDescription).append(System.lineSeparator());
                            contentToWrite.append("Action: ").append(violationAction).append(System.lineSeparator());

                            // Write the violation information to the new output file
                            try (FileWriter writer = new FileWriter(outputFileName)) {
                                writer.write(contentToWrite.toString());
                                System.out.println("Output written to output1.txt.");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            System.out.println("Invalid key! Please enter a valid key (1, 2, 3, or 4).");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number (1, 2, 3, or 4).");
                    }

                    break;


                case 10:
                    HashMap<Integer, String> requirements = new HashMap<>();
                    requirements.put(1, "full-time student status");
                    requirements.put(2, "no criminal charges");
                    requirements.put(3, "18 or older");

                    // Read the input file (jobRequirements.txt) and process each candidate
                    try (BufferedReader br = new BufferedReader(new FileReader("jobRequirements.txt"));
                         FileWriter writer = new FileWriter("output2.txt")) {

                        String line;
                        while ((line = br.readLine()) != null) {
                            if (!line.isEmpty()) {
                                // Split the line into the candidate's name and their qualifications
                                String[] parts = line.split(", ");
                                String candidateName = parts[0].replace("\"", ""); // Remove quotes around the name

                                // Check if the candidate's qualifications match all requirements
                                int requirementsMatched = 0;
                                for (int key : requirements.keySet()) {
                                    for (int i = 1; i < parts.length; i++) {
                                        if (requirements.get(key).equals(parts[i].replace("\"", ""))) {
                                            requirementsMatched++;
                                            break; // Stop checking other parts once a match is found
                                        }
                                    }
                                }

                                // Check if the candidate met all the requirements
                                String output;
                                if (requirementsMatched == requirements.size()) {
                                    output = candidateName + " is qualified";
                                } else {
                                    output = candidateName + " is not qualified";
                                }

                                // Write the result to the output file
                                writer.write(output + System.lineSeparator());
                            }
                        }
                        System.out.println("Processing completed. Results written to output2.txt.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 11:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}

