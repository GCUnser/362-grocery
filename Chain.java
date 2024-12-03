import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Chain {

    private static final String LOCATIONS_FILE = "locations.txt";
    private static final String INVENTORY_FILE = "chainInventory.txt";
    private static final List<Item> inventory = new ArrayList<>();

    public Chain()
    {
        loadInventory();
    }

    /**
     * Adds a new location to the chain by updating the locations file and creating a new inventory folder.
     *
     * @param locationName Name of the new location.
     * @throws IOException if there are issues writing to the file or creating the folder.
     */
    public boolean addNewLocation(String locationName) throws IOException {
        // Check if location already exists
        if (locationExists(locationName)) {
            System.out.println("Location already exists in the chain.");
            return false;
        }

        // Add the new location to locations.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOCATIONS_FILE, true))) {
            writer.write(locationName);
            writer.newLine();
        }

        // Create a new folder for the location
        Path locationFolder = Paths.get(locationName);
        Files.createDirectories(locationFolder);

        // Create an empty inventory.txt file in the new folder
        Path inventoryFile = locationFolder.resolve("inventory.txt");
        Files.createFile(inventoryFile);

        System.out.println("Successfully added new location: " + locationName);

        // Ask if user wants to stock the store
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to stock the new store? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("y")) {
            stockStore(inventoryFile.toString());
        }

        return true;
    }

    public void removeLocation(String locationName) throws IOException
    {
        if(locationExists(locationName))
        {
            ArrayList<String> names = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(LOCATIONS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if(!line.trim().equalsIgnoreCase(locationName.trim()))
                    {
                        names.add(line);
                    }

                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOCATIONS_FILE))) {
                for(int i = 0; i < names.size(); i++)
                {
                    writer.write(names.get(i));
                    writer.newLine();
                }
            }
        }
        System.out.printf("Removed location with name %s\n", locationName);

    }

    public void addItemToStock(Item toAdd)
    {
        for(Item i: inventory)
        {
            if(i.getName().equalsIgnoreCase(toAdd.getName()))
            {
                String date = "";
                int k = 0;
                for(int j = 0; j < toAdd.getQuantity(); j++)
                {
                    if(date.equals(toAdd.getDateList().get(j)))
                    {
                        k++;
                    }
                    else if(!date.isEmpty() && !date.equals(toAdd.getDateList().get(j)))
                    {
                        i.addQuantity(date, k);
                        date = toAdd.getDateList().get(j);
                        k++;
                    }
                    else
                    {
                        date = toAdd.getDateList().get(j);
                        k = 1;
                    }
                }

            }
            break;
        }
        saveInventory();
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        inventory.add(item);
        saveInventory();
    }

    public void addItemQuantity(String date, int quantity, Item i)
    {
        i.addQuantity(date, quantity);
        saveInventory();
    }

    public double getMoney()
    {
        double currentProfit = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader("profit.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentProfit = Double.parseDouble(line);
            }
        } catch (IOException e) {
            System.err.println("Error getting profit.txt: " + e.getMessage());
        }
        return currentProfit;
    }

    public void removeMoney(double amount)
    {
        try {
            // Read the current profit value
            double currentProfit = 0.0;
            try (BufferedReader reader = new BufferedReader(new FileReader("profit.txt"))) {
                String line = reader.readLine();
                if (line != null) {
                    currentProfit = Double.parseDouble(line);
                }
            }

            // Update the profit
            currentProfit = currentProfit - amount;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("profit.txt"))) {
                writer.write(String.format("%.2f", currentProfit));
            }
        } catch (IOException e) {
            System.err.println("Error updating profit.txt: " + e.getMessage());
        }
    }

    /**
     * Checks if a location already exists in the locations file.
     *
     * @param locationName Name of the location to check.
     * @return true if the location exists, false otherwise.
     * @throws IOException if there are issues reading the file.
     */
    private boolean locationExists(String locationName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOCATIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equalsIgnoreCase(locationName.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Stocks the store by allowing the user to add items to the inventory.
     *
     * @param inventoryFilePath Path to the inventory file.
     * @throws IOException if there are issues writing to the file.
     */
    private void stockStore(String inventoryFilePath) throws IOException {
        Scanner scanner = new Scanner(System.in);

        boolean addingItems = true;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFilePath, true))) {
            while (addingItems) {
                System.out.println("\nAdd a new item to the store:");

                System.out.print("Item name: ");
                String name = scanner.nextLine();

                System.out.print("Category: ");
                String category = scanner.nextLine();

                System.out.print("Price: ");
                double price = Double.parseDouble(scanner.nextLine());

                boolean taxable = getYesNoInput(scanner, "Is the item taxable? (y/n): ");
                boolean foodStamp = getYesNoInput(scanner, "Is the item eligible for food stamps? (y/n): ");
                boolean twentyOnePlus = getYesNoInput(scanner, "Is the item for 21+ only? (y/n): ");

                System.out.print("Quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());

                System.out.print("Expiration date for all items (yyyy-mm-dd): ");
                String expirationDate = scanner.nextLine();

                // Create the date list with the same expiration date for all items
                ArrayList<String> dateList = new ArrayList<>();
                for (int i = 0; i < quantity; i++) {
                    dateList.add(expirationDate);
                }

                // Create a new Item instance
                Item item = new Item(name, category, price, taxable, foodStamp, twentyOnePlus, quantity, dateList);

                // Write item to the inventory file
                writer.write(item.toString());
                writer.newLine();

                System.out.println("Item added: " + item);

                // Ask if user wants to add another item
                addingItems = getYesNoInput(scanner, "Do you want to add another item? (y/n): ");
            }
        }

        System.out.println("Finished stocking the store.");
    }

    /**
     * Helper method to get a "yes" or "no" input from the user.
     *
     * @param scanner Scanner for user input.
     * @param prompt  Prompt message.
     * @return true if the user responds with "y", false otherwise.
     */
    private boolean getYesNoInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y")) {
                return true;
            } else if (response.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }

    private void loadInventory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                inventory.add(Item.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    private void saveInventory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
            for (Item item : inventory) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }
}
