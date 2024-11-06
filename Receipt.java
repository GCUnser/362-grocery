import java.util.*;
import java.io.*;
public class Receipt {
    public static void viewReceipt() {
        try {
            File file = new File("receipt.txt");

            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine()); // Print each line of the receipt
                }
                scanner.close();
            } else {
                System.out.println("Receipt not found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading receipt: " + e.getMessage());
        }
    }
}
