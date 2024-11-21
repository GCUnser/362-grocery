import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GroceryStore {
    private String fileName;
    private static String BASE = "";
    private List<Item> inventory;

    public GroceryStore() {
        this.inventory = new ArrayList<>();
        loadInventory();
    }
    public GroceryStore(String city) {
        this.inventory = new ArrayList<>();
        fileName = "./" + city + "/inventory.txt";
        BASE = "./" + city;
        loadInventory();
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

    public void sortInventory(int choice) {

        if (choice == 1) {
            // Alphabet
        } else if (choice == 2) {
            // Category
            int sorted = 0;
            String category = "";
            while (sorted != inventory.size()) {
                for (int i = 0; i < inventory.size() - sorted; i++) {
                    if (category.isEmpty()) {
                        category = inventory.get(i).getCategory();
                    }
                    if (inventory.get(i).getCategory().equals(category)) {
                        Collections.swap(inventory, i, (inventory.size() - 1 - sorted));
                        sorted++;
                    }
                }
                category = "";
            }

        }
    }

    public int removeZeroItems() {
        int j = 0;
        for (Item i : inventory) {
            if (i.getQuantity() == 0) {
                inventory.remove(i);
                j++;
            }
        }
        saveInventory();
        return j;
    }

    public int removeItem(String name) {
        int j = 0;
        for (Item i : inventory) {
            if (i.getName().compareTo(name) == 0) {
                inventory.remove(i);
                j = 1;
                break;
            }
        }
        saveInventory();
        return j;
    }

    // Gets an item by name from the inventory
    public Item getItemByName(String name) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null; // Item not found
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public Item findItemInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null; // Return null if the item is not found
    }

    public double calculateCartCost(boolean member) {
        double totalCost = 0.0;
        Map<String, Double> allSaleItems = new HashMap<>();
        Map<String, Double> nonMemberSaleItems = new HashMap<>();
        Map<String, Integer> saleLimits = new HashMap<>();

        // Load sales from saleItems.txt for the specified city
        Path saleFilePath = Paths.get(BASE, "saleItems.txt");
        if (Files.exists(saleFilePath)) {
            try (BufferedReader saleReader = new BufferedReader(new FileReader(saleFilePath.toString()))) {
                String line;
                while ((line = saleReader.readLine()) != null) {
                    String[] parts = line.split(",\\s*");
                    if (parts.length >= 4) {
                        String itemName = parts[0].trim().toLowerCase();
                        double discount = Double.parseDouble(parts[1]);
                        boolean isMemberOnly = Boolean.parseBoolean(parts[2]);
                        int limit = parts[3].equalsIgnoreCase("N/A") ? Integer.MAX_VALUE : Integer.parseInt(parts[3]);

                        allSaleItems.put(itemName, discount);
                        saleLimits.put(itemName, limit);
                        if (!isMemberOnly) {
                            nonMemberSaleItems.put(itemName, discount);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading saleItems.txt: " + e.getMessage());
            }
        }

        // Calculate cart cost considering sales and limits
        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 2) {
                    String itemName = parts[0];
                    int requestedQuantity;

                    try {
                        requestedQuantity = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity in cart.txt for item: " + itemName);
                        continue;
                    }

                    // Find the item in inventory
                    Item item = findItemInInventory(itemName);
                    if (item != null) {
                        double pricePerUnit = item.getPrice();

                        // Determine discount and limit
                        double discount = member
                                ? allSaleItems.getOrDefault(itemName.toLowerCase(), 0.0)
                                : nonMemberSaleItems.getOrDefault(itemName.toLowerCase(), 0.0);
                        int limit = saleLimits.getOrDefault(itemName.toLowerCase(), Integer.MAX_VALUE);

                        // Adjust quantity based on limit
                        int eligibleQuantity = Math.min(requestedQuantity, limit);

                        double discountedPrice = pricePerUnit * (1 - discount);
                        double itemCost = eligibleQuantity * discountedPrice * (item.isTaxable() ? 1.07 : 1.0);

                        totalCost += itemCost;

                        // Inform if remaining quantity exceeded limit
                        if (requestedQuantity > limit) {
                            System.out.println("Note: Sale limit of " + limit + " applied to " + itemName + ".");
                        }
                    } else {
                        System.out.println("Item not found in inventory: " + itemName);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading cart.txt: " + e.getMessage());
        }

        return totalCost;
    }


    public double checkout(int payment, double userMoney, boolean twentyonePlus, boolean member) {
        double totalCost = 0.0;
        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("Receipt:\n");
        receiptContent.append("Item\tQuantity\tUnit Price\tDiscount\tTotal Price\n");
        ArrayList<String> itemsToRetainInCart = new ArrayList<>();
        Map<String, Double> allSaleItems = new HashMap<>();
        Map<String, Double> nonMemberSaleItems = new HashMap<>();
        Map<String, Integer> saleLimits = new HashMap<>();

        // Load sales for the specified city
        Path saleFilePath = Paths.get(BASE, "saleItems.txt");
        if (Files.exists(saleFilePath)) {
            try (BufferedReader saleReader = new BufferedReader(new FileReader(saleFilePath.toString()))) {
                String line;
                while ((line = saleReader.readLine()) != null) {
                    String[] parts = line.split(",\\s*");
                    if (parts.length >= 4) {
                        String itemName = parts[0].trim().toLowerCase();
                        double discount = Double.parseDouble(parts[1]);
                        boolean isMemberOnly = Boolean.parseBoolean(parts[2]);
                        int limit = parts[3].equalsIgnoreCase("N/A") ? Integer.MAX_VALUE : Integer.parseInt(parts[3]);

                        allSaleItems.put(itemName, discount);
                        saleLimits.put(itemName, limit);
                        if (!isMemberOnly) {
                            nonMemberSaleItems.put(itemName, discount);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading saleItems.txt: " + e.getMessage());
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 2) {
                    String itemName = parts[0];
                    int requestedQuantity;

                    try {
                        requestedQuantity = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity in cart.txt for item: " + itemName);
                        continue;
                    }

                    for (Item item : inventory) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            int availableQuantity = item.getQuantity();
                            double pricePerUnit = item.getPrice();

                            // Determine discount and limit
                            double discount = member
                                    ? allSaleItems.getOrDefault(itemName.toLowerCase(), 0.0)
                                    : nonMemberSaleItems.getOrDefault(itemName.toLowerCase(), 0.0);
                            int limit = saleLimits.getOrDefault(itemName.toLowerCase(), Integer.MAX_VALUE);

                            // Adjust quantity based on limit
                            int eligibleQuantity = Math.min(requestedQuantity, limit);

                            double discountedPrice = pricePerUnit * (1 - discount);
                            double itemCost = 0.0;

                            if (payment != 4 || item.isFoodStampEligible()) {
                                if (twentyonePlus || !item.forTwentyOnePlus()) {
                                    if (availableQuantity >= eligibleQuantity) {
                                        item.removeDate(eligibleQuantity);
                                        itemCost = eligibleQuantity * discountedPrice * (item.isTaxable() ? 1.07 : 1.0);
                                        totalCost += itemCost;
                                        receiptContent.append(String.format("%s\t%d\t$%.2f\t%.2f%%\t$%.2f\n", itemName,
                                                eligibleQuantity, pricePerUnit, discount * 100, itemCost));
                                    } else if (availableQuantity > 0) {
                                        item.removeDate(availableQuantity);
                                        itemCost = availableQuantity * discountedPrice * (item.isTaxable() ? 1.07 : 1.0);
                                        totalCost += itemCost;
                                        receiptContent.append(String.format("%s\t%d\t$%.2f\t%.2f%%\t$%.2f\n", itemName,
                                                availableQuantity, pricePerUnit, discount * 100, itemCost));
                                    } else {
                                        System.out.println("Item " + itemName + " is out of stock.");
                                    }
                                } else {
                                    System.out.println("Item " + itemName + " requires the customer to be 21 or older.");
                                }
                            } else {
                                System.out.println("Item " + itemName + " is not food stamp eligible.");
                                itemsToRetainInCart.add(line);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading cart.txt: " + e.getMessage());
        }

        if (totalCost > userMoney) {
            return totalCost;
        }

        saveInventory();
        clearCart(itemsToRetainInCart);
        generateReceipt(receiptContent.toString(), totalCost);

        return totalCost;
    }




    public void clearCart(ArrayList<String> itemsToRetain) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart.txt"))) {
            for (String itemLine : itemsToRetain) {
                writer.write(itemLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating cart.txt: " + e.getMessage());
        }
    }

    private void generateReceipt(String receiptContent, double totalCost) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("receipt.txt"))) {
            writer.write(receiptContent);
            writer.write(String.format("\nTotal Cost: $%.2f\n", totalCost));
            writer.write("Thank you for shopping with us!\n");
        } catch (IOException e) {
            System.err.println("Error writing to receipt.txt: " + e.getMessage());
        }
    }

    private void generateSalesRecord(String recordsContent, double totalCost) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("salesRecords.txt"))) {
            writer.write(recordsContent);
            writer.write(String.format("\nTotal Cost: $%.2f\n", totalCost));
        } catch (IOException e) {
            System.err.println("Error writing to salesRecords.txt: " + e.getMessage());
        }
    }

    /*
     * return the item given and refund the customer, do not add the item back to
     * inventory as it can't be resold
     */
    public double returnItem(String itemName, int returnQuantity) {
        double refundAmount = 0.0;
        boolean itemFoundInReceipt = false;
        StringBuilder updatedReceiptContent = new StringBuilder();
        updatedReceiptContent.append("Receipt:\n");
        updatedReceiptContent.append("Item\tQuantity\tUnit Price\tTotal Price\n");

        try (BufferedReader receiptReader = new BufferedReader(new FileReader("receipt.txt"))) {
            String line;
            receiptReader.readLine(); // Skip "Receipt:" header
            receiptReader.readLine(); // Skip column headers

            while ((line = receiptReader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length != 4) {
                    // System.out.println("Invalid line format in receipt.txt: " + line);
                    continue;
                }

                String receiptItemName = parts[0];
                int purchasedQuantity = Integer.parseInt(parts[1]);
                double unitPrice = Double.parseDouble(parts[2].replace("$", ""));
                double itemTotalCost = Double.parseDouble(parts[3].replace("$", ""));

                // Check if the item matches and if enough quantity was purchased to allow the
                // return
                if (receiptItemName.equalsIgnoreCase(itemName) && purchasedQuantity >= returnQuantity) {
                    itemFoundInReceipt = true;
                    boolean taxable;
                    // refund amount differs based on if tax was paid
                    if (purchasedQuantity * unitPrice == itemTotalCost) {
                        refundAmount = returnQuantity * unitPrice;
                        taxable = false;
                    } else {
                        refundAmount = returnQuantity * unitPrice * 1.07;
                        taxable = true;
                    }

                    // Adjust the receipt to show the remaining quantity after the return
                    int remainingQuantity = purchasedQuantity - returnQuantity;
                    double remainingTotalCost;
                    if (taxable) {
                        remainingTotalCost = remainingQuantity * unitPrice * 1.07;
                    } else {
                        remainingTotalCost = remainingQuantity * unitPrice;
                    }
                    if (remainingQuantity > 0) {
                        updatedReceiptContent.append(String.format("%s\t%d\t$%.2f\t$%.2f\n",
                                receiptItemName, remainingQuantity, unitPrice, remainingTotalCost));
                    }

                    // Update returns and profit
                    updateSalesAndProfit(itemName, returnQuantity, unitPrice, true, taxable);
                    System.out.println(
                            "Return processed successfully. Refund amount: $" + String.format("%.2f", refundAmount));
                } else {
                    // Write the line as is if it does not match the return item or quantity was
                    // less than required
                    updatedReceiptContent.append(line).append("\n");
                }
            }

            if (!itemFoundInReceipt) {
                System.out
                        .println("Item " + itemName + " not found on the receipt or insufficient quantity to return.");
            }

            // Update the receipt to reflect the return
            updateReceiptFile(updatedReceiptContent.toString());

        } catch (IOException e) {
            System.err.println("Error processing return: " + e.getMessage());
        }

        return refundAmount;
    }

    public void removeSpoiled(String spoiled) {
        for (Item i : inventory) {
            int j = 0;
            for (String date : i.getDateList()) {
                if (date.compareTo(spoiled) <= 0) {
                    j++;
                }
            }
            i.removeDate(j);
        }
    }

    public List<Item> removeFiles()
    {
        File f = new File(BASE);
        if(f.isDirectory())
        {
            File[] files = f.listFiles();
            assert files != null;
            for(int i = files.length - 1; i >= 0; i--)
            {
                files[i].delete();
            }
        }
        f.delete();
        return inventory;
    }

    private void updateReceiptFile(String updatedReceiptContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("receipt.txt"))) {
            writer.write(updatedReceiptContent);
        } catch (IOException e) {
            System.err.println("Error updating receipt.txt: " + e.getMessage());
        }
    }

    // Helper method to update sales, profit, and returns
    private void updateSalesAndProfit(String itemName, int quantity, double pricePerUnit, boolean isReturn,
            boolean taxable) {
        double amount = quantity * pricePerUnit;
        if (isReturn) {
            // Write to returns.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("returns.txt", true))) {
                writer.write(
                        String.format("Returned: %s, Quantity: %d, Total Refund: $%.2f\n", itemName, quantity, amount));
            } catch (IOException e) {
                System.err.println("Error writing to returns.txt: " + e.getMessage());
            }
            if (taxable) {
                updateTaxFile(amount * 0.07);
            }

            // Subtract from profit
            updateProfitFile(-amount);
        } else {
            // Write to sales.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt", true))) {
                writer.write(String.format("Sold: %s, Quantity: %d, Total Sale (without tax): $%.2f\n", itemName,
                        quantity, amount));
            } catch (IOException e) {
                System.err.println("Error writing to sales.txt: " + e.getMessage());
            }

            // Add to profit
            updateProfitFile(amount);
        }
    }

    private void updateProfitFile(double amount) {
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
            currentProfit += amount;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("profit.txt"))) {
                writer.write(String.format("%.2f", currentProfit));
            }
        } catch (IOException e) {
            System.err.println("Error updating profit.txt: " + e.getMessage());
        }
    }

    private void updateTaxFile(double amount) {
        try {
            // Read the current profit value
            double currentProfit = 0.0;
            try (BufferedReader reader = new BufferedReader(new FileReader("returnTaxOwed.txt"))) {
                String line = reader.readLine();
                if (line != null) {
                    currentProfit = Double.parseDouble(line);
                }
            }

            // Update the profit
            currentProfit += amount;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("returnTaxOwed.txt"))) {
                writer.write(String.format("%.2f", currentProfit));
            }
        } catch (IOException e) {
            System.err.println("Error updating returnTaxOwed.txt: " + e.getMessage());
        }
    }

    private void loadInventory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                inventory.add(Item.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    private void saveInventory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Item item : inventory) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }
}
