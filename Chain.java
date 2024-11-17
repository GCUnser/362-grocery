import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class Chain {

    private static final String LOCATIONS_FILE = "locations.txt";

    /**
     * Adds a new location to the chain by updating the locations file and creating a new inventory folder.
     *
     * @param locationName Name of the new location.
     * @throws IOException if there are issues writing to the file or creating the folder.
     */
    public void addNewLocation(String locationName) throws IOException {
        // Check if location already exists
        if (locationExists(locationName)) {
            System.out.println("Location already exists in the chain.");
            return;
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
     * Main method for testing the Chain class functionality.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Chain chain = new Chain();

        System.out.print("Enter the name of the new store location: ");
        String newLocation = scanner.nextLine();

        try {
            chain.addNewLocation(newLocation);
        } catch (IOException e) {
            System.out.println("Error while adding new location: " + e.getMessage());
        }
    }
}
