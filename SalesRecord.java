import java.io.File;
import java.io.IOException;

/**
 * @author Gabriel_Unser
 */
public class SalesRecord {
    public static void viewSalesRecords() {
        try {
            File file = new File("salesRecords.txt");

            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine()); // Print each line of the sales records
                }
                scanner.close();
            } else {
                System.out.println("Sales records not found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading sales records: " + e.getMessage());
        }
    }

}
