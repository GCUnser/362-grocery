import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
            System.out.println("6. Hiring");
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
                        int i =0;
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
                                    } if (violationCount == 2) {
                                        f.meetingWithManager = true;
                                        System.out.println("Schedule a meeting with the manager: " + names.get(i));
                                    }  if (violationCount == 3) {
                                        f.isFired = true;
                                        System.out.println("Fired: " + names.get(i));
                                    }
                                } else {
                                    System.out.println("Not found in hashmap: " + line);
                                }
                            }

                            i++;}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
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
                        int i =0;
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
                            i++;}

                            }
                     catch (IOException e) {
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
