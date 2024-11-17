import java.io.*;
import java.util.*;

public class Cart {
    private static String FILE_NAME = "inventory.txt";
    public Cart(String city) {
        FILE_NAME = "./" + Main.city + "/inventory.txt";
    }


    // Cart items are stored in cart.txt in the format: ItemName, Quantity
    public void addItemToCart(String itemName, int quantityToAdd) {
        // Temporary list to store cart items from the cart.txt file
        List<String> updatedCart = new ArrayList<>();
        boolean itemFound = false;
        double itemPrice = getPriceForItem(itemName);

        if (itemPrice == -1.0) {
            System.out.println("Item not found in inventory. Please check the item name.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;

            // Read all the lines from the cart.txt and check for the item
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 2) {
                    String cartItemName = parts[0];
                    int cartItemQuantity = Integer.parseInt(parts[1]);

                    // If the item is found, add the new quantity to the existing quantity
                    if (cartItemName.equalsIgnoreCase(itemName)) {
                        cartItemQuantity += quantityToAdd;
                        updatedCart.add(cartItemName + ", " + cartItemQuantity);
                        itemFound = true;
                    } else {
                        updatedCart.add(line);  // Keep existing items unchanged
                    }
                }
            }

            // If the item was not found in the cart, add it as a new item
            if (!itemFound) {
                updatedCart.add(itemName + ", " + quantityToAdd);
            }

            // Rewrite the cart.txt with the updated content
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart.txt"))) {
                for (String updatedLine : updatedCart) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }

            System.out.println("Item added to the cart successfully!");

        } catch (IOException e) {
            System.err.println("Error reading/writing to cart.txt: " + e.getMessage());
        }
    }
    public double removeItemFromCart(String itemName, int quantityToRemove) {
        double removedAmount = 0.0;
        try {
            // Read the current items in the cart
            File inputFile = new File("cart.txt");
            File tempFile = new File("cart_temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                String cartItemName = parts[0];
                int cartItemQuantity = Integer.parseInt(parts[1]);

                if (cartItemName.equalsIgnoreCase(itemName)) {
                    // Item found in cart, adjust the quantity
                    if (cartItemQuantity >= quantityToRemove) {
                        // Reduce the quantity or remove item if necessary
                        cartItemQuantity -= quantityToRemove;
                        removedAmount = quantityToRemove * getPriceForItem(cartItemName);
                        if (cartItemQuantity > 0) {
                            writer.write(cartItemName + ", " + cartItemQuantity);
                            writer.newLine();
                        }
                        System.out.println("Item removed or quantity reduced.");
                    } else {
                        System.out.println("Not enough quantity to remove.");
                        writer.write(line);
                        writer.newLine();
                    }
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();

            // Replace original cart.txt with updated cart_temp.txt
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            }
        } catch (IOException e) {
            System.err.println("Error updating cart.txt: " + e.getMessage());
        }
        return removedAmount;
    }

    private double getPriceForItem(String cartItemName) {
        double price = -1.0;  // Default value indicating the item is not found

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");  // Split by comma and optional whitespace

                if (parts.length > 1) {
                    String itemName = parts[0];

                    // Check if item names match (case insensitive)
                    if (itemName.equalsIgnoreCase(cartItemName)) {
                        // If item found, parse the price (third element in the line)
                        if(parts[4].equals("true")) {
                            price = Double.parseDouble(parts[2]) * 1.07;
                        } else {
                            price = Double.parseDouble(parts[2]);
                        }
                        break;  // Exit loop once the item is found
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading inventory.txt: " + e.getMessage());
        }

        // Return the price (or -1.0 if not found)
        return price;
    }


    public static void displayCartItems() {
        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;
            System.out.println("Current items in cart:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading cart.txt: " + e.getMessage());
        }
    }

    public static void reviewAndRemoveItems(GroceryStore store,Cart cart, double totalCost, double userMoney, Scanner scanner) {
        // Show current items in the cart
        System.out.println("Here are the items in your cart:");
        cart.displayCartItems();

        while (totalCost > userMoney) {
            System.out.print("Enter the name of the item you want to remove or reduce quantity: ");
            String itemToRemove = scanner.nextLine();
            System.out.print("Enter the quantity to remove: ");
            int quantityToRemove = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Try to remove the item or reduce quantity
            double removedAmount = cart.removeItemFromCart(itemToRemove, quantityToRemove);

            if (removedAmount > 0) {
                totalCost -= removedAmount;
                System.out.printf("Removed %d of %s, you have reduced the total cost by $%.2f.%n", quantityToRemove, itemToRemove, removedAmount);

                if (totalCost <= userMoney) {
                    System.out.println("You now have enough money to complete the purchase.");
                } else {
                    System.out.printf("Remaining total cost: $%.2f%n", totalCost);
                }
            } else {
                System.out.println("Item not found in cart or quantity to remove is too large. Try again.");
            }
            System.out.println("Would you like to remove another item? (yes/no)");
            String continueChoice = scanner.nextLine();

            if (continueChoice.equalsIgnoreCase("no")) {
                break;
            }
        }

        // Final check if the user has enough money
        if (totalCost <= userMoney) {
            System.out.printf("You have enough money to complete the purchase of $%.2f.%n", totalCost);
        } else {
            System.out.println("You still do not have enough money. Consider removing more items.");
        }
    }


}
