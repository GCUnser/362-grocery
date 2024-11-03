import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GroceryStore {
    private static final String FILE_NAME = "inventory.txt";
    private List<Item> inventory;

    public GroceryStore() {
        this.inventory = new ArrayList<>();
        loadInventory();
    }

    public void addItem(Item item) {
        inventory.add(item);
        saveInventory();
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public double checkout(String itemName, int requestedQuantity) {
        boolean itemFound = false;
        double totalCost = 0.0;

        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                itemFound = true;
                int availableQuantity = item.getQuantity();
                double pricePerUnit = item.getPrice();

                if (availableQuantity >= requestedQuantity) {
                    item.setQuantity(availableQuantity - requestedQuantity);
                    totalCost = requestedQuantity * pricePerUnit;
                    System.out.println("Purchased " + requestedQuantity + " of " + itemName);
                } else if (availableQuantity > 0) {
                    item.setQuantity(0);
                    totalCost = availableQuantity * pricePerUnit;
                    System.out.println("Only " + availableQuantity + " of " + itemName + " available. Purchased all available items.");
                } else {
                    System.out.println("Item " + itemName + " is out of stock.");
                }
                break;
            }
        }
        if (!itemFound) {
            System.out.println("Item " + itemName + " not found in inventory.");
        }

        saveInventory(); // Update inventory file
        return totalCost;
    }

    private void loadInventory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                inventory.add(Item.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    private void saveInventory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Item item : inventory) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }
}
