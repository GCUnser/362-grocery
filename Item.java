import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private boolean foodStamp;
    private boolean taxable;
    private String name;
    private double price;
    private int quantity;
    private ArrayList<String> date; //sorted from smallest date (next to expire) to largest date (furthest from expiration)

    public Item(String name, double price, boolean taxable, boolean foodStamp) {
        this.name = name;
        this.price = price;
        this.taxable = taxable;
        this.foodStamp = foodStamp;
        this.quantity = 0;
        this.date = new ArrayList<>(); // Initialize date list
    }

    public Item(String name, double price, boolean taxable, boolean foodStamp, int quantity, ArrayList<String> date) {
        this.name = name;
        this.price = price;
        this.taxable = taxable;
        this.foodStamp = foodStamp;
        this.quantity = quantity;
        this.date = new ArrayList<>(date);
    }

    public void addQuantity(String date) {
        this.date.add(date);
        quantity += 1;
    }

    public void addQuantity(String date, int quantity) {
        this.quantity += quantity;
        for (int i = 0; i < quantity; i++) {
            this.date.add(date);
        }
    }

    public void removeQuantity(int toRemove)
    {
        for(int i = 0; i < toRemove; i++)
        {
            if(quantity > 0)
            {
                date.removeFirst();
                quantity = quantity - 1;
            }
        }
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public boolean isFoodStampEligible() {
        return foodStamp;
    }

    public ArrayList<String> getDateList() {
        return new ArrayList<>(date);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return name + "," + String.format("%.2f", price) + "," + quantity + "," + taxable + "," + foodStamp + "," + String.join(",", date);
    }

    public static Item fromString(String line) {
        String[] parts = line.split(",");

        // Ensure there are enough parts to create an Item, otherwise print an error and return null
        if (parts.length < 5) {
            System.err.println("Error: Invalid line format. Skipping line: " + line);
            return null;
        }

        String name = parts[0];
        double price;
        int quantity;
        boolean taxable;
        boolean foodStamp;
        ArrayList<String> date = new ArrayList<>();

        try {
            price = Double.parseDouble(parts[1]);
            quantity = Integer.parseInt(parts[2]);
            taxable = Boolean.parseBoolean(parts[3]);
            foodStamp = Boolean.parseBoolean(parts[4]);

            // Collect any additional parts as dates, if available
            for (int i = 5; i < parts.length; i++) {
                date.add(parts[i]);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Unable to parse number in line. Skipping line: " + line);
            return null;
        }

        return new Item(name, price, taxable, foodStamp, quantity, date);
    }

}
