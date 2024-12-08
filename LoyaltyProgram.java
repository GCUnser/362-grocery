import java.io.*;
import java.util.Scanner;

public class LoyaltyProgram {

    public static void addCustomerToLoyaltyProgram() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Loyalty Program! Let's get you signed up.");

        // Step 1: Ask for the customer's name
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        // Step 2: Ask if the customer is 18+
        System.out.print("Are you 18 years or older? (y/n): ");
        String ageResponse = scanner.nextLine().trim().toLowerCase();
        if (!ageResponse.equalsIgnoreCase("y")) {
            System.out.println("Sorry, you need to be 18+ to join the loyalty program.");
            return;
        }

        // Step 3: Collect email/phone number
        System.out.print("Please enter your email or phone number to sign up: ");
        String contactInfo = scanner.nextLine().trim();

        // Step 4: Ask about premium loyalty program
        System.out.println("We offer a Premium Loyalty Program with additional benefits.");
        System.out.print("Would you like to join the Premium Loyalty Program? (y/n): ");
        String premiumResponse = scanner.nextLine().trim().toLowerCase();

        String subscriptionType = "Basic";
        boolean premiumSuccess = false;

        if (premiumResponse.equalsIgnoreCase("y")) {
            // Handle Premium Program subscription
            premiumSuccess = handlePremiumSubscription(scanner, contactInfo);
            if (premiumSuccess) {
                subscriptionType = "Premium";
            }
        }

        if (!premiumSuccess) {
            // Handle Basic Subscription
            System.out.println("You are now signed up for the Basic Loyalty Program. Enjoy your benefits!");
        }

        // Step 5: Create the file with the user's information
        createCustomerFile(name, contactInfo, subscriptionType);

        System.out.println("Thank you, " + name + "! You are successfully signed up for the " + subscriptionType + " Loyalty Program.");
    }

    private static boolean handlePremiumSubscription(Scanner scanner, String contactInfo) {
        // Step 4: Collect payment details
        System.out.print("Please provide your payment information: ");
        String paymentInfo = scanner.nextLine().trim();

        // Step 5: Choose subscription type
        System.out.print("Would you like a Monthly or Yearly subscription? (monthly/yearly): ");
        String subscriptionType = scanner.nextLine().trim().toLowerCase();

        // Step 6: Process payment
        boolean paymentSuccess = processPayment(paymentInfo, subscriptionType);
        if (!paymentSuccess) {
            // Payment failed, ask for new payment details
            System.out.println("Payment declined. Please provide a new form of payment.");
            System.out.print("Enter new payment information: ");
            String newPaymentInfo = scanner.nextLine().trim();
            paymentSuccess = processPayment(newPaymentInfo, subscriptionType);

            if (!paymentSuccess) {
                // Payment still failed, offer basic subscription
                System.out.println("Payment failed again. You can still join the Basic Loyalty Program and upgrade later.");
                return false;
            }
        }

        // Payment succeeded
        System.out.println("Payment successful! You are now signed up for the Premium Loyalty Program.");
        System.out.println("Your benefits are active immediately. Thank you!");
        return true;
    }

    private static boolean processPayment(String paymentInfo, String subscriptionType) {
        // Simulate payment processing
        System.out.println("Processing payment for " + subscriptionType + " subscription...");
        // This is a placeholder for actual payment integration logic.
        return paymentInfo.length() > 1; // Assume payment succeeds if paymentInfo is sufficiently detailed
    }

    private static void createCustomerFile(String name, String contactInfo, String subscriptionType) {
        String filename = contactInfo.replaceAll("[^a-zA-Z0-9]", "_") + ".txt"; // Sanitize filename
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Name: " + name + "\n");
            writer.write("Contact Info: " + contactInfo + "\n");
            writer.write("Subscription Type: " + subscriptionType + "\n");
            writer.write("Points: 0\n"); // Initialize points to 0
            System.out.println("Customer file created: " + filename);
        } catch (IOException e) {
            System.err.println("Error creating customer file: " + e.getMessage());
        }

    }

    public static void viewMembership(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            System.out.println("Membership Information:");

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Could not find or open the membership file: " + filename);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void upgradeMembership() {
        Scanner scanner = new Scanner(System.in);

        // Ask for the user's contact info to determine the filename
        System.out.print("Enter your email or phone number: ");
        String contactInfo = scanner.nextLine().trim();
        String filename = contactInfo.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";

        try {
            File membershipFile = new File(filename);

            if (!membershipFile.exists()) {
                System.out.println("Membership file not found: " + filename);
                return;
            }

            // Ask for payment information
            System.out.print("Please provide your payment information: ");
            String paymentInfo = scanner.nextLine().trim();

            // Simulate payment processing
            boolean paymentSuccess = processPayment(paymentInfo, "Premium");

            if (!paymentSuccess) {
                System.out.println("Payment failed. Please provide a new form of payment.");
                System.out.print("Enter new payment information: ");
                String newPaymentInfo = scanner.nextLine().trim();
                paymentSuccess = processPayment(newPaymentInfo, "Premium");

                if (!paymentSuccess) {
                    System.out.println("Payment failed again. Cannot upgrade to Premium.");
                    return;
                }
            }

            // Read and modify the membership file to upgrade subscription
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder fileContent = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Subscription Type:")) {
                    fileContent.append("Subscription Type: Premium\n");
                } else {
                    fileContent.append(line).append("\n");
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(fileContent.toString());
            writer.close();

            System.out.println("Membership upgraded to Premium successfully!");

        } catch (IOException e) {
            System.out.println("An error occurred while upgrading your membership: " + e.getMessage());
        }
    }

    public static void deleteMembership() {
        Scanner scanner = new Scanner(System.in);

        // Ask for user's contact info to determine the filename
        System.out.print("Enter your email or phone number: ");
        String contactInfo = scanner.nextLine().trim();
        String filename = contactInfo.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";

        try {
            File membershipFile = new File(filename);

            if (membershipFile.delete()) {
                System.out.println("Membership file '" + filename + "' successfully deleted.");
            } else {
                System.out.println("Failed to delete the membership file.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while trying to delete the membership file: " + e.getMessage());
        }
    }

}

