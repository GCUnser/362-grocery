
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static String city;
    private static final String INVENTORY_FILE_PATH = "./"; // Base path for inventory files
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        int quantity;
        String name;
        String category;
        Chain chain = new Chain();
        SaleItems saleItems = new SaleItems();

        List<String> locations = new ArrayList<>();
        boolean valid = false;

        // Read locations from file
        String filePath = "locations.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Skip empty lines
                    locations.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // Ensure there are locations available
        if (locations.isEmpty()) {
            System.out.println("No locations found in the file.");
        }

        // Allow user to choose a location or add a new store
        while (!valid) {
            System.out.println("Choose an option:");
            System.out.println("1. Add a new store location");
            System.out.println("2. Add an item to Chain Inventory");
            for (int i = 0; i < locations.size(); i++) {
                System.out.println((i + 3) + ". " + locations.get(i)); // Offset by 2 since option 1 is "Add a new store"
            }
            System.out.print("Enter the number of your choice: ");
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);

                if (choice == 1) { // Add a new store location
                    System.out.print("Enter the name of the new store location: ");
                    String newLocation = scanner.nextLine();
                    try {
                        boolean newStore = chain.addNewLocation(newLocation);
                        if(newStore) {
                            locations.add(newLocation); // Update the locations list
                            System.out.println("New store added successfully.");
                        }
                    } catch (IOException e) {
                        System.out.println("Error while adding new location: " + e.getMessage());
                    }
                } else if (choice == 2) {
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
                    for (Item i : chain.getInventory()) {
                        if (i.getName().compareTo(name.toLowerCase()) == 0) {
                            System.out.println("Item exists in Inventory");
                            System.out.print("Enter item quantity to add: ");
                            quantity = scanner.nextInt();
                            if(i.getPrice() * quantity > chain.getMoney())
                            {
                                System.out.println("Not enough money to purchase requested quantity");
                                correctName = false;
                                break;
                            }
                            while (quantity <= 0) {
                                System.out.println(
                                        "Invalid quantity to add, please pick add a positive amount of inventory");
                                System.out.print("Enter item quantity to add: ");
                                quantity = scanner.nextInt();
                            }
                            System.out.print("Enter expiration date of the item in form 'YYYY-mm-dd': ");
                            String date = scanner.next();
                            chain.addItemQuantity(date, quantity, i);
                            chain.removeMoney(i.getPrice() * quantity);
                            correctName = false;
                            break;
                        }
                    }
                    if (correctName) {
                        System.out.println("No item exists by that name, please enter it into the Inventory");
                        scanner.nextLine();
                        System.out.print("Enter item category: ");
                        category = scanner.nextLine();
                        System.out.print("Enter item price: ");
                        double price = scanner.nextDouble();
                        System.out.print("Enter if item is Taxable (y or n): ");
                        boolean taxable;
                        taxable = scanner.next().equalsIgnoreCase("y");
                        System.out.print("Enter if item is applicable for Food Stamps (y or n): ");
                        boolean foodStamp;
                        foodStamp = scanner.next().equalsIgnoreCase("y");
                        System.out.print("Enter if item requires being 21 or older to purchase (y or n): ");
                        boolean twentyOnePlus;
                        twentyOnePlus = scanner.next().equalsIgnoreCase("y");
                        scanner.nextLine(); // Consume newline

                        Item item = new Item(name.toLowerCase(), category.toLowerCase(), price, taxable, foodStamp,
                                twentyOnePlus);
                        chain.addItem(item);
                        System.out.println("Item added successfully!\n");
                    }
                }
                else if (choice >= 3 && choice <= locations.size() + 2) {
                    city = locations.get(choice - 3); // Offset by 2 since option 1 is "Add a new store"
                    System.out.println("You selected: " + city);
                    valid = true;
                } else {
                    System.out.println("Invalid choice. Please select a number between 1 and " + (locations.size() + 1) + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // Initialize inventory file path based on the selected city
        String inventoryFilePath = INVENTORY_FILE_PATH + city + "/inventory.txt";
        System.out.println("Using inventory file: " + inventoryFilePath);

        // Initialize GroceryStore and Cart with the selected city
        GroceryStore store = new GroceryStore(city);
        Cart cart = new Cart(city);


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
            System.out.println("13. View Sales Records");
            System.out.println("14. Process Payroll");
            System.out.println("15. Put Item on Sale");
            System.out.println("16. Take Item off Sale");
            System.out.println("17. View Items on Sale");
            System.out.println("18. Transfer Employees");
            System.out.println("19. Employees currently on the clock");
            System.out.println("20. Remove Store from Chain");
            System.out.println("21. Exit");
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
                    for (Item i : chain.getInventory()){
                        if (i.getName().compareTo(name.toLowerCase()) == 0) {
                            System.out.println("Item exists in Chain Inventory");
                            System.out.print("Enter item quantity to add: ");
                            quantity = scanner.nextInt();
                            while (quantity <= 0) {
                                System.out.println(
                                        "Invalid quantity to add, please add a positive amount of inventory");
                                System.out.print("Enter item quantity to add: ");
                                quantity = scanner.nextInt();
                            }
                            for (Item ib : store.getInventory()) {
                                if (ib.getName().compareTo(name.toLowerCase()) == 0) {
                                    if(quantity > i.getQuantity())
                                    {
                                        for(String date : i.getDateList())
                                        {
                                            ib.addQuantity(date, 1);
                                            quantity--;
                                        }
                                        i.getDateList().clear();
                                        if(i.getPrice() * quantity < chain.getMoney()){
                                            System.out.print("Enter expiration date of the item in form 'YYYY-mm-dd': ");
                                            String date = scanner.next();
                                            store.addItemQuantity(date, quantity, i);
                                            chain.removeMoney(i.getPrice() * quantity);
                                        }
                                        else{
                                            System.out.println("Not enough money in chain to add all requested quantity.");
                                        }
                                    }
                                    else{
                                        while(quantity > 0){
                                            String date = i.getDateList().removeFirst();
                                            ib.addQuantity(date, 1);
                                            quantity--;
                                        }
                                    }
                                    break;
                                }
                            }
                            correctName = false;
                            break;
                        }
                    }
                    if (!correctName) {
                        break;
                    }
                    for (Item i : store.getInventory()) {
                        if (i.getName().compareTo(name.toLowerCase()) == 0) {
                            System.out.println("Item exists in Inventory");
                            System.out.print("Enter item quantity to add: ");
                            quantity = scanner.nextInt();
                            while (quantity <= 0) {
                                System.out.println(
                                        "Invalid quantity to add, please add a positive amount of inventory");
                                System.out.print("Enter item quantity to add: ");
                                quantity = scanner.nextInt();
                            }
                            if(i.getPrice() * quantity < chain.getMoney()){
                                System.out.print("Enter expiration date of the item in form 'YYYY-mm-dd': ");
                                String date = scanner.next();
                                store.addItemQuantity(date, quantity, i);
                                chain.removeMoney(i.getPrice() * quantity);
                            }
                            else{
                                System.out.println("Not enough money in chain to add all requested quantity.");
                            }
                            correctName = false;
                            break;
                        }
                    }
                    if (!correctName) {
                        break;
                    }
                    System.out.println("No item exists by that name, please enter it into the Inventory");
                    scanner.nextLine();
                    System.out.print("Enter item category: ");
                    category = scanner.nextLine();
                    System.out.print("Enter item price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter if item is Taxable (y or n): ");
                    boolean taxable;
                    taxable = scanner.next().equalsIgnoreCase("y");
                    System.out.print("Enter if item is applicable for Food Stamps (y or n): ");
                    boolean foodStamp;
                    foodStamp = scanner.next().equalsIgnoreCase("y");
                    System.out.print("Enter if item requires being 21 or older to purchase (y or n): ");
                    boolean twentyOnePlus;
                    twentyOnePlus = scanner.next().equalsIgnoreCase("y");
                    scanner.nextLine(); // Consume newline

                    Item item = new Item(name.toLowerCase(), category.toLowerCase(), price, taxable, foodStamp,
                            twentyOnePlus);
                    store.addItem(item);
                    System.out.println("Item added successfully!\n");
                    break;

                case 2:
                    System.out.print(
                            "Please enter the date for which you would like to remove spoiled items for in the form 'YYYY-mm-dd': ");
                    String date = scanner.next();
                    store.removeSpoiled(date);
                    System.out.println("Removed spoiled items from inventory!\n");
                    break;
                case 3:
                    System.out.println("Inventory:");
                    for (Item i : store.getInventory()) {
                        System.out.printf(
                                "%s - %s - $%.2f - Quantity: %d - Taxable: %b - Food Stamp Eligible: %b - Only 21+ can buy: %b -  Expiration Dates: %s%n",
                                i.getName(), i.getCategory(), i.getPrice(), i.getQuantity(), i.isTaxable(),
                                i.isFoodStampEligible(), i.forTwentyOnePlus(), i.getDateList());
                    }
                    System.out.println();
                    break;

                case 4:
                    System.out.println("Please pick which method you would like to sort the inventory by");
                    System.out.println("1. Alphabetical");
                    System.out.println("2. Category");
                    System.out.print("Chose which mode");
                    int toSortBy;
                    if (scanner.hasNextInt() && (toSortBy = scanner.nextInt()) == 1) {
                        System.out.println("Sorting Alphabetically");
                        store.sortInventory(toSortBy);
                    } else if (scanner.hasNextInt() && (toSortBy = scanner.nextInt()) == 2) {
                        System.out.println("Sorting By Category");
                        store.sortInventory(toSortBy);
                    } else {
                        System.out.println("Invalid input, returning to main selection.");
                    }
                    System.out.println("Inventory Sorted.\n");
                    break;

                case 5:
                    System.out.println(
                            "Would you like to remove all items with zero quantity, or remove a specific item?");
                    System.out.println("1. Remove all items with zero quantity");
                    System.out.println("2. Remove one specific item");
                    System.out.print("Please select the option you would like to perform: ");
                    String removeChoice = scanner.nextLine();
                    int countRemoved;
                    switch (removeChoice) {
                        case "1":
                            countRemoved = store.removeZeroItems();
                            System.out.println("Removed " + countRemoved + "items.\n");
                            break;
                        case "2":
                            System.out.println("Please enter the name of the item you would like to remove: ");
                            name = scanner.nextLine();
                            countRemoved = store.removeItem(name);
                            if (countRemoved == 0) {
                                System.out.println("No item with name " + name + " found, returning.");
                            } else if (countRemoved == 1) {
                                System.out.println("Successfully removed item.\n");
                            }
                            break;
                        default:
                            System.out.println("No valid input, returning to main selection.\n");
                    }

                    break;

                case 6:
                    // System.out.print("Enter item name to buy: ");
                    // String itemName = scanner.nextLine();
                    // System.out.print("Enter quantity to buy: ");
                    // int requestedQuantity = scanner.nextInt();
                    // scanner.nextLine(); // Consume newline
                    int payChoice = 0;
                    boolean validChoice = false;
                    boolean twentyonePlus = false;
                    boolean member = false;

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
                    System.out.print("Are you 21 or older? (y/n): ");
                    if (scanner.next().equalsIgnoreCase("y")) {
                        twentyonePlus = true;
                    }
                    scanner.nextLine();
                    System.out.print("Are you a member? (y/n): ");
                    if (scanner.next().equalsIgnoreCase("y")) {
                        member = true;
                    }
                    scanner.nextLine();
                    System.out.print("Enter the amount you have: $");
                    double userMoney = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    double totalCost = store.calculateCartCost(member);

                    while (totalCost > userMoney) {
                        System.out.println("You don't have enough money. Let's review your cart.");
                        cart.reviewAndRemoveItems(store, cart, totalCost, userMoney, scanner);
                        totalCost = store.calculateCartCost(member);
                    }
                    totalCost = store.checkout(payChoice, userMoney, twentyonePlus, member);
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
                        cart.addItemToCart(itemName, quantityToAdd); // Add item to the cart
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
                        System.out.println(
                                "Enter a key (1, 2, 3, or 4) to view the corresponding violation information:");
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
                                    // Trim and check if the line starts with the key followed by a comma (e.g.,
                                    // "1,")
                                    line = line.trim();
                                    if (!line.isEmpty()) {
                                        String[] parts = line.split(",");
                                        if (parts.length > 0) {
                                            try {
                                                int currentKey = Integer.parseInt(parts[0].trim());
                                                if (currentKey == key) {
                                                    violationCount++; // Increment if the key is found
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
                            contentToWrite.append("Violation: ").append(policyDescription)
                                    .append(System.lineSeparator());
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

                case 12:
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

                case 13:
                    SalesRecord.viewSalesRecords();
                    break;

                case 14:

                    // Read the input file (employeePayroll.txt) and process each employee
                    try (BufferedReader br = new BufferedReader(new FileReader("employeePayroll.txt"));
                            FileWriter writer = new FileWriter("output3.txt")) {

                        String line;
                        while ((line = br.readLine()) != null) {
                            if (!line.isEmpty()) {
                                // Split the line into the candidate's name, their wage, and their hours
                                String[] parts = line.split(", ");
                                String employeeName = parts[0].replace("\"", ""); // Remove quotes around the name
                                double employeeWage = Double.parseDouble(parts[1]);
                                int employeeHours = Integer.parseInt(parts[2]);

                                String output;
                                output = employeeName + ": $" + Payroll.calculateWage(employeeWage, employeeHours)
                                        + " deposited";

                                // Write the result to the output file
                                writer.write(output + System.lineSeparator());
                            }
                        }
                        System.out.println("Payment completed. Results written to output3.txt.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;


                case 15:
                    System.out.print("Enter the item to put on sale: ");
                    name = scanner.nextLine();
                    System.out.print("Enter the discount (0-1): ");
                    double discount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Is this discount members only? (y/n): ");
                    String yOrN = scanner.nextLine();
                    System.out.print("Is there a limit on how many items the customer can buy on sale? (y/n): ");
                    String limitExists = scanner.nextLine();
                    String limit = "N/A";
                    if(limitExists.equalsIgnoreCase("y")) {
                        System.out.print("Enter the limit: ");
                        limit = scanner.next();
                        scanner.nextLine();
                    }
                    if(yOrN.equalsIgnoreCase("y")) {
                        saleItems.addSale(city,name,discount, true, limit);
                    } else {
                        saleItems.addSale(city, name, discount, false, limit);
                    }
                    break;

                case 16:
                    System.out.print("Enter the item to take off sale: ");
                    name = scanner.nextLine();
                    saleItems.removeSale(city,name);
                    break;
                case 17:
                    saleItems.listSales(city);
                    break;

                case 18:
                    //Transfer employees to a new store

                    HashMap<Integer, String> transferEmployees = new HashMap<>();
                    HashMap<Integer, String> storeBranch = new HashMap<>();

// Load employee data from file
                    try (BufferedReader br = new BufferedReader(new FileReader("transferEmployees.txt"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (!line.isEmpty()) {
                                String[] parts = line.split(", ");
                                int id = Integer.parseInt(parts[0].trim());
                                String employeeName = parts[1].replace("\"", "").trim();
                                transferEmployees.put(id, employeeName);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

// Load store branch data from file
                    try (BufferedReader br = new BufferedReader(new FileReader("storeBranch.txt"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (!line.isEmpty()) {
                                String[] parts = line.split(", ");
                                int id = Integer.parseInt(parts[0].trim());
                                String branch = parts[1].replace("\"", "").trim();
                                storeBranch.put(id, branch);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

// Use the existing Scanner object for user input
                    try (FileWriter writer = new FileWriter("transferResults.txt")) {
                        for (int employeeId : transferEmployees.keySet()) {
                            String employeeName = transferEmployees.get(employeeId);

                            // Ask if the employee wants to transfer
                            String response = null;
                            while (true) {
                                System.out.println("Would " + employeeName + " like to transfer to a new store? (yes/no)");

                                // Use the existing scanner object for input
                                if (scanner.hasNextLine()) {
                                    response = scanner.nextLine().trim().toLowerCase();
                                    if (response.equals("yes") || response.equals("no")) {
                                        break; // Valid response, break out of the loop
                                    } else {
                                        System.out.println("Invalid response. Please type 'yes' or 'no'.");
                                    }
                                } else {
                                    System.out.println("No input detected. Skipping employee " + employeeName + ".");
                                    return; // Exit the loop or program if no input
                                }
                            }

                            if (response.equals("yes")) {
                                // Show available store branches
                                System.out.println("Available store branches:");
                                for (int branchId : storeBranch.keySet()) {
                                    System.out.println(branchId + ". " + storeBranch.get(branchId));
                                }

                                // Prompt for branch ID
                                int chosenBranchId = -1;
                                while (true) {
                                    System.out.println("Enter the ID of the store branch " + employeeName + " wants to transfer to:");

                                    // Validate user input for store branch ID
                                    if (scanner.hasNextLine()) {
                                        String input = scanner.nextLine().trim();
                                        try {
                                            chosenBranchId = Integer.parseInt(input);
                                            if (storeBranch.containsKey(chosenBranchId)) {
                                                break; // Valid branch ID
                                            } else {
                                                System.out.println("Invalid branch ID. Please try again.");
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid input. Please enter a valid number.");
                                        }
                                    } else {
                                        System.out.println("No input detected for branch ID. Skipping.");
                                        return; // Exit if no input is detected
                                    }
                                }

                                String chosenBranch = storeBranch.get(chosenBranchId);
                                writer.write(employeeName + " will transfer to " + chosenBranch + System.lineSeparator());
                                System.out.println(employeeName + " will transfer to " + chosenBranch + ".");
                            } else {
                                writer.write(employeeName + " will not transfer to a new store." + System.lineSeparator());
                            }
                        }

                        System.out.println("Transfer decisions have been recorded in transferResults.txt.");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    break;


                case 19:

                    HashMap<Integer, String> employeesWorking = new HashMap<>();
                    int empCount = 0;

                    // Read the employeesWorking.txt file
                    try (BufferedReader br = new BufferedReader(new FileReader("employeesWorking.txt"));
                         FileWriter writer = new FileWriter("employeesWorkingResult.txt")) {

                        String line;
                        while ((line = br.readLine()) != null) {
                            if (!line.isEmpty()) {
                                // Split the line into parts
                                String[] parts = line.split(", ");
                                int id = Integer.parseInt(parts[0].trim());
                                String empName = parts[1].replace("\"", "").trim();
                                String workingStatus = parts[2].replace("\"", "").trim();

                                // Add employee to the HashMap
                                employeesWorking.put(id, empName);

                                // Write the employee's status to the file
                                writer.write(empName + " is " + (workingStatus.equalsIgnoreCase("Yes") ? "currently on the clock." : "not on the clock.") + "\n");

                                // Increase empCount if workingStatus is "Yes"
                                if (workingStatus.equalsIgnoreCase("Yes")) {
                                    empCount++;
                                }
                            }
                        }

                        // Write the total number of employees on the clock
                        writer.write("Total employees on the clock: " + empCount + "\n");
                        System.out.println("Output written to employeesWorkingResult.txt.");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case 20:
                    List<Item> itemList;
                    itemList = store.removeFiles();
                    for(Item toAdd: itemList)
                    {
                        chain.addItemToStock(toAdd);
                        chain.removeLocation(city);
                    }
                    System.out.println("Removed city from chain, exiting program.");
                    ArrayList<String> end = new ArrayList<>();
                    store.clearCart(end); // clear the cart when exiting
                    scanner.close();
                    return;

                case 21:
                    ArrayList<String> empty = new ArrayList<>();
                    store.clearCart(empty); // clear the cart when exiting
                    System.out.println("Exiting...");
                    scanner.close();
                    return;



                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}