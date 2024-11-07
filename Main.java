import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GroceryStore store = new GroceryStore();
        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart();
        int quantity;
        String name;

        while (true) {
            System.out.println("1. Add Item");
            System.out.println("2. Remove Spoiled Items");
            System.out.println("3. View Inventory");
            System.out.println("4. Checkout");
            System.out.println("5. Return Item");
            System.out.println("6. Add Item To Cart");
            System.out.println("7. View Cart");
            System.out.println("8. View Receipt");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    boolean correctName = false;
                    System.out.print("Enter item name: ");
                    name = scanner.nextLine();
                    while(!correctName) {
                        System.out.println("Is this the correct name of the product you wish to add: " + name);
                        System.out.print("Please answer y or n: ");
                        String answer = scanner.next();
                        if(answer.equalsIgnoreCase("y"))
                        {
                            correctName = true;
                        }
                        else {
                            System.out.print("Enter item name: ");
                            name = scanner.nextLine();
                        }
                    }
                    for (Item i : store.getInventory()) {
                        if(i.getName().compareTo(name.toLowerCase()) == 0)
                        {
                            System.out.println("Item exists in Inventory");
                            System.out.print("Enter item quantity to add: ");
                            quantity = scanner.nextInt();
                            while(quantity <= 0)
                            {
                                System.out.println("Invalid quantity to add, please pick add a positive amount of inventory");
                                System.out.print("Enter item quantity to add: ");
                                quantity = scanner.nextInt();
                            }
                            System.out.print("Enter expiration date of the item in form 'YY-mm-dd': ");
                            String date = scanner.next();
                            if(quantity == 1)
                            {
                                i.addQuantity(date);
                            }
                            else if(quantity > 1)
                            {
                                i.addQuantity(date, quantity);
                            }
                            correctName = false;
                            break;
                        }
                    }
                    if(!correctName)
                    {
                        break;
                    }
                    System.out.println("No item exists by that name, please enter it into the Inventory");
                    System.out.print("Enter item price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter if item is Taxable (y or n): ");
                    boolean taxable;
                    if(scanner.next().equalsIgnoreCase("y"))
                    {
                        taxable = true;
                    }
                    else
                    {
                        taxable = false;
                    }
                    System.out.print("Enter if item is applicable for Food Stamps (y or n): ");
                    boolean foodStamp;
                    if(scanner.next().equalsIgnoreCase("y"))
                    {
                        foodStamp = true;
                    }
                    else
                    {
                        foodStamp = false;
                    }
                    scanner.nextLine(); // Consume newline

                    Item item = new Item(name.toLowerCase(), price, taxable, foodStamp);
                    store.addItem(item);
                    System.out.println("Item added successfully!\n");
                    break;

                case 2:
                    System.out.print("Please enter the date for which you would like to remove spoiled items for in the form 'YY-mm-dd': ");
                    String date = scanner.next();
                    store.removeSpoiled(date);
                    System.out.println("Removed spoiled items from inventory!\n");
                    break;
                case 3:
                    System.out.println("Inventory:");
                    for (Item i : store.getInventory()) {
                        System.out.printf("%s - $%.2f - Quantity: %d - Taxable: %b - Food Stamp Eligible: %b - Expiration Dates: %s%n",
                                i.getName(), i.getPrice(), i.getQuantity(), i.isTaxable(), i.isFoodStampEligible(), i.getDateList());
                    }
                    System.out.println();
                    break;

                case 4:
//                    System.out.print("Enter item name to buy: ");
//                    String itemName = scanner.nextLine();
//                    System.out.print("Enter quantity to buy: ");
//                    int requestedQuantity = scanner.nextInt();
//                    scanner.nextLine(); // Consume newline
                    int payChoice = 0;
                    boolean validChoice = false;

                    while (!validChoice) {
                        System.out.println("1. Card");
                        System.out.println("2. Gift Card");
                        System.out.println("3. Cash");
                        System.out.println("4. Food Stamps");
                        System.out.print("Choose an option: ");
                        payChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline left-over

                        if (payChoice >= 1 && payChoice <= 4) {
                            validChoice = true;
                        } else {
                            System.out.println("Invalid choice, please try again.");
                        }
                    }
                    System.out.print("Enter the amount you have: $");
                    double userMoney = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    double totalCost = store.calculateCartCost();

                    while (totalCost > userMoney) {
                        System.out.println("You don't have enough money. Let's review your cart.");
                        cart.reviewAndRemoveItems(store, cart, totalCost, userMoney, scanner);
                        totalCost = store.calculateCartCost();
                    }
                    totalCost = store.checkout(payChoice, userMoney);
                    break;
                case 5:
                    System.out.print("Enter the item name you want to return: ");
                    name = scanner.nextLine();
                    System.out.print("Enter the quantity: ");
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    store.returnItem(name, quantity);
                    break;
                case 6:
                    System.out.print("Enter item name to add to cart: ");
                    String itemName = scanner.nextLine();

                    System.out.print("Enter quantity to add: ");
                    int quantityToAdd = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    // Check if the item exists in the inventory before adding to the cart
                    Item itemToAdd = store.getItemByName(itemName);
                    if (itemToAdd != null && itemToAdd.getQuantity() >= quantityToAdd) {
                        cart.addItemToCart(itemName, quantityToAdd);  // Add item to the cart
                        System.out.println("Item added to cart successfully!\n");
                    } else {
                        System.out.println("Item not available or insufficient quantity in inventory.\n");
                    }
                    break;
                case 7:
                    Cart.displayCartItems();
                    break;
                case 8:
                    Receipt.viewReceipt();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }

    }

}
