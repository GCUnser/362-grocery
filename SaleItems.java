import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SaleItems {

    private static final String LOCATIONS_FILE = "locations.txt";

    /**
     * Adds a new sale to the specified city's `saleItems.txt` file.
     *
     * @param cityName Name of the city where the sale is added.
     * @param itemName Name of the item on sale.
     * @param discount Discount rate (e.g., 0.1 for 10% off).
<<<<<<< HEAD
     * @throws IOException if there are issues writing to the file.
     */
    public void addSale(String cityName, String itemName, double discount, boolean memberOnly) throws IOException {
=======
     * @param memberOnly is this discount for members onlu
     * @param limit the max number of items a person can buy (N/A for no limit)
     * @throws IOException if there are issues writing to the file.
     */
    public void addSale(String cityName, String itemName, double discount, boolean memberOnly, String limit) throws IOException {
>>>>>>> origin/main
        if (discount <= 0 || discount >= 1) {
            System.out.println("Invalid discount value. It must be between 0 and 1.");
            return;
        }

        Path saleFilePath = Paths.get(cityName, "saleItems.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saleFilePath.toString(), true))) {
<<<<<<< HEAD
            writer.write(itemName + "," + discount + "," + memberOnly);
=======
            writer.write(itemName + "," + discount + "," + memberOnly + "," + limit);
>>>>>>> origin/main
            writer.newLine();
        }
        if(memberOnly) {
            System.out.println("Added sale: " + itemName + " with a discount of " + (discount * 100) + "% in " + cityName + " that is members only");

        } else {
            System.out.println("Added sale: " + itemName + " with a discount of " + (discount * 100) + "% in " + cityName + " that is for everyone");
        }
    }

    /**
     * Removes a sale for the specified item from the city's `saleItems.txt` file.
     *
     * @param cityName Name of the city where the sale is removed.
     * @param itemName Name of the item to remove from the sale.
     * @throws IOException if there are issues reading or writing to the file.
     */
    public void removeSale(String cityName, String itemName) throws IOException {
        Path saleFilePath = Paths.get(cityName, "saleItems.txt");

        if (!Files.exists(saleFilePath)) {
            System.out.println("No sales file found for " + cityName + ".");
            return;
        }

        List<String> sales = new ArrayList<>();
        boolean found = false;

        // Read the current sales
        try (BufferedReader reader = new BufferedReader(new FileReader(saleFilePath.toString()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue; // Skip malformed lines
                if (parts[0].trim().equalsIgnoreCase(itemName.trim())) {
                    found = true; // Mark item as found
                } else {
                    sales.add(line); // Keep non-matching sales
                }
            }
        }

        if (!found) {
            System.out.println("Item " + itemName + " is not on sale in " + cityName + ".");
            return;
        }

        // Write the updated sales back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saleFilePath.toString()))) {
            for (String sale : sales) {
                writer.write(sale);
                writer.newLine();
            }
        }

        System.out.println("Removed sale for: " + itemName + " in " + cityName);
    }

    /**
     * Lists all items on sale in the specified city.
     *
     * @param cityName Name of the city.
     * @throws IOException if there are issues reading the sales file.
     */
    public void listSales(String cityName) throws IOException {
        Path saleFilePath = Paths.get(cityName, "saleItems.txt");

        if (!Files.exists(saleFilePath)) {
            System.out.println("No sales file found for " + cityName + ".");
            return;
        }

        System.out.println("Current sales in " + cityName + ":");
        try (BufferedReader reader = new BufferedReader(new FileReader(saleFilePath.toString()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}

