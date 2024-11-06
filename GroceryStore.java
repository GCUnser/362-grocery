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

    public double checkout(int payment) {
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
                                System.out.println("Purchased " + requestedQuantity + " of " + itemName);
                            } else if (availableQuantity > 0) {
                                item.setQuantity(0);
                                item.removeDate(availableQuantity);
                                itemCost = availableQuantity * pricePerUnit * (item.isTaxable() ? 1.07 : 1.0);
                                totalCost += itemCost;
                                receiptContent.append(String.format("%s\t%d\t$%.2f\t$%.2f\n", itemName, availableQuantity, pricePerUnit, itemCost));
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
                    refundAmount = returnQuantity * unitPrice;

                    // Adjust the receipt to show the remaining quantity after the return
                    int remainingQuantity = purchasedQuantity - returnQuantity;
                    double remainingTotalCost = remainingQuantity * unitPrice;
                    if (remainingQuantity > 0) {
                        updatedReceiptContent.append(String.format("%s\t%d\t$%.2f\t$%.2f\n",
                                receiptItemName, remainingQuantity, unitPrice, remainingTotalCost));
                    }

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
