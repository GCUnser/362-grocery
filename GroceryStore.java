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


    public double calculateCartCost() {
        double totalCost = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 2) {
                    String itemName = parts[0];
                    int quantity = Integer.parseInt(parts[1]);

                    // Find the item in inventory
                    Item item = findItemInInventory(itemName);
                    if (item != null) {
                        double pricePerUnit = item.getPrice();
                        // Apply tax if the item is taxable
                        double itemCost = quantity * pricePerUnit * (item.isTaxable() ? 1.07 : 1.0);
                        totalCost += itemCost;
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


    public double checkout(int payment, double userMoney) {
        double totalCost = 0.0;
        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("Receipt:\n");
        receiptContent.append("Item\tQuantity\tUnit Price\tTotal Price\n");
        ArrayList<String> itemsToRetainInCart = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*"); // Split by comma and optional whitespace
                if (parts.length != 2) {
                    System.out.println("Invalid line format in cart.txt: " + line);
                    continue;
                }

                String itemName = parts[0];
                int requestedQuantity;

                try {
                    requestedQuantity = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity in cart.txt for item: " + itemName);
                    continue;
                }

                boolean itemFound = false;

                for (Item item : inventory) {
                    if (item.getName().equalsIgnoreCase(itemName)) {
                        itemFound = true;
                        int availableQuantity = item.getQuantity();
                        double pricePerUnit = item.getPrice();
                        double itemCost = 0.0;
                        if(payment != 4 || item.isFoodStampEligible()) {
                            if (availableQuantity >= requestedQuantity) {
                                item.setQuantity(availableQuantity - requestedQuantity);
                                item.removeDate(requestedQuantity);
                                itemCost = requestedQuantity * pricePerUnit * (item.isTaxable() ? 1.07 : 1.0);
                                totalCost += itemCost;
                                receiptContent.append(String.format("%s\t%d\t$%.2f\t$%.2f\n", itemName, requestedQuantity, pricePerUnit, itemCost));

                                // Update sales and profit
                                updateSalesAndProfit(itemName, requestedQuantity, pricePerUnit, false, false);
                                System.out.println("Purchased " + requestedQuantity + " of " + itemName);
                            } else if (availableQuantity > 0) {
                                item.setQuantity(0);
                                item.removeDate(availableQuantity);
                                itemCost = availableQuantity * pricePerUnit * (item.isTaxable() ? 1.07 : 1.0);
                                totalCost += itemCost;
                                receiptContent.append(String.format("%s\t%d\t$%.2f\t$%.2f\n", itemName, availableQuantity, pricePerUnit, itemCost));

                                // Update sales and profit
                                updateSalesAndProfit(itemName, availableQuantity, pricePerUnit, false, false);
                                System.out.println("Only " + availableQuantity + " of " + itemName + " available. Purchased all available items.");
                            } else {
                                System.out.println("Item " + itemName + " is out of stock.");
                            }
                            break;
                        } else {
                            System.out.println("Item " + itemName + " is not food stamp eligible. Pay with a different method.");
                            itemsToRetainInCart.add(line);
                        }
                    }
                }

                if (!itemFound) {
                    System.out.println("Item " + itemName + " not found in inventory.");
                }
            }
            if(totalCost > userMoney)
            {
                return totalCost;
            }

            saveInventory(); // Update inventory file after purchase
            // Clear the cart file
            clearCart(itemsToRetainInCart);
            // Generate the receipt
            generateReceipt(receiptContent.toString(), totalCost);

        } catch (IOException e) {
            System.err.println("Error reading cart.txt: " + e.getMessage());
        }

        return totalCost;
    }

    private void clearCart(ArrayList<String> itemsToRetain) {
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

    /*
    return the item given and refund the customer, do not add the item back to inventory as it can't be resold
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
                    //System.out.println("Invalid line format in receipt.txt: " + line);
                    continue;
                }

                String receiptItemName = parts[0];
                int purchasedQuantity = Integer.parseInt(parts[1]);
                double unitPrice = Double.parseDouble(parts[2].replace("$", ""));
                double itemTotalCost = Double.parseDouble(parts[3].replace("$", ""));

                // Check if the item matches and if enough quantity was purchased to allow the return
                if (receiptItemName.equalsIgnoreCase(itemName) && purchasedQuantity >= returnQuantity) {
                    itemFoundInReceipt = true;
                    boolean taxable;
                    //refund amount differs based on if tax was paid
                    if(purchasedQuantity * unitPrice == itemTotalCost) {
                        refundAmount = returnQuantity * unitPrice;
                        taxable = false;
                    } else {
                        refundAmount = returnQuantity * unitPrice * 1.07;
                        taxable = true;
                    }

                    // Adjust the receipt to show the remaining quantity after the return
                    int remainingQuantity = purchasedQuantity - returnQuantity;
                    double remainingTotalCost;
                    if(taxable) {
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
                    System.out.println("Return processed successfully. Refund amount: $" + String.format("%.2f", refundAmount));
                } else {
                    // Write the line as is if it does not match the return item or quantity was less than required
                    updatedReceiptContent.append(line).append("\n");
                }
            }

            if (!itemFoundInReceipt) {
                System.out.println("Item " + itemName + " not found on the receipt or insufficient quantity to return.");
            }

            // Update the receipt to reflect the return
            updateReceiptFile(updatedReceiptContent.toString());

        } catch (IOException e) {
            System.err.println("Error processing return: " + e.getMessage());
        }

        return refundAmount;
    }

    private void updateReceiptFile(String updatedReceiptContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("receipt.txt"))) {
            writer.write(updatedReceiptContent);
        } catch (IOException e) {
            System.err.println("Error updating receipt.txt: " + e.getMessage());
        }
    }

    // Helper method to update sales, profit, and returns
    private void updateSalesAndProfit(String itemName, int quantity, double pricePerUnit, boolean isReturn, boolean taxable) {
        double amount = quantity * pricePerUnit;
        if (isReturn) {
            // Write to returns.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("returns.txt", true))) {
                writer.write(String.format("Returned: %s, Quantity: %d, Total Refund: $%.2f\n", itemName, quantity, amount));
            } catch (IOException e) {
                System.err.println("Error writing to returns.txt: " + e.getMessage());
            }
            if(taxable) {
                updateTaxFile(amount * 0.07);
            }

            // Subtract from profit
            updateProfitFile(-amount);
        } else {
            // Write to sales.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt", true))) {
                writer.write(String.format("Sold: %s, Quantity: %d, Total Sale (without tax): $%.2f\n", itemName, quantity, amount));
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
