import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private boolean twentyOnePlus;
    private boolean foodStamp;
    private boolean taxable;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private boolean glutenFree;
    private boolean peanuts;
    private boolean vegan;
    private ArrayList<String> date; // sorted from smallest date (next to expire) to largest date (furthest from
                                    // expiration)

    public Item(String name, String category, double price, boolean taxable, boolean foodStamp, boolean tOPlus,
            boolean glutenFree, boolean peanuts, boolean vegan) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.taxable = taxable;
        this.foodStamp = foodStamp;
        twentyOnePlus = tOPlus;
        this.glutenFree = glutenFree;
        this.peanuts = peanuts;
        this.vegan = vegan;
        this.quantity = 0;
        this.date = new ArrayList<>(); // Initialize date list
    }

    public Item(String name, String category, double price, boolean taxable, boolean foodStamp, boolean tOPlus,
            boolean glutenFree, boolean peanuts, boolean vegan,
            int quantity, ArrayList<String> date) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.taxable = taxable;
        this.foodStamp = foodStamp;
        twentyOnePlus = tOPlus;
        this.glutenFree = glutenFree;
        this.peanuts = peanuts;
        this.vegan = vegan;
        this.quantity = quantity;
        this.date = new ArrayList<>(date);
    }

    public Item(String name, String category, double price){
        this.name = name;
        this.category = category;
        this.price = price;
        this.taxable = false;
        this.foodStamp = false;
        twentyOnePlus = false;
        glutenFree = false;
        peanuts = false;
        vegan = false;
        this.date = new ArrayList<>();
    }

    public void addQuantity(String date, int quantity) {
        for (int j = 0; j <= this.date.size(); j++) {
            if (j == this.date.size()) {
                for (int i = 0; i < quantity; i++) {
                    this.date.add(date);
                }
                break;
            } else if (date.compareTo(this.date.get(j)) < 0) {
                for (int i = 0; i < quantity; i++) {
                    this.date.add(j, date);
                }
                break;
            }
        }

    }

    public void removeDate(int toRemove) {
        for (int i = 0; i < toRemove; i++) {
            if (!date.isEmpty()) {
                date.removeFirst();
            }
        }
    }

    public void increasePrice(double amount) {
        this.price += amount;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return date.size();
    }

    public boolean isTaxable() {
        return taxable;
    }

    public boolean isFoodStampEligible() {
        return foodStamp;
    }

    public boolean forTwentyOnePlus() {
        return twentyOnePlus;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public boolean isPeanuts() {
        return peanuts;
    }

    public boolean isVegan() {
        return vegan;
    }

    public ArrayList<String> getDateList() {
        return new ArrayList<>(date);
    }

    @Override
    public String toString() {
        return name + "," + category + "," + String.format("%.2f", price) + "," + quantity + "," + taxable + ","
                + foodStamp + "," + twentyOnePlus + "," + glutenFree + "," + peanuts + "," + vegan + ","
                + String.join(",", date);
    }

    public static Item fromString(String line) {
        String[] parts = line.split(",");

        // Ensure there are enough parts to create an Item, otherwise print an error and
        // return null
        if (parts.length < 9) {
            System.err.println("Error: Invalid line format. Skipping line: " + line);
            return null;
        }

        String name = parts[0];
        String category = parts[1];
        double price;
        int quantity;
        boolean taxable;
        boolean foodStamp;
        boolean twentyOnePlus;
        boolean glutenFree;
        boolean peanuts;
        boolean vegan;
        ArrayList<String> date = new ArrayList<>();

        try {
            price = Double.parseDouble(parts[2]);
            quantity = Integer.parseInt(parts[3]);
            taxable = Boolean.parseBoolean(parts[4]);
            foodStamp = Boolean.parseBoolean(parts[5]);
            twentyOnePlus = Boolean.parseBoolean(parts[6]);
            glutenFree = Boolean.parseBoolean(parts[7]);
            peanuts = Boolean.parseBoolean(parts[8]);
            vegan = Boolean.parseBoolean(parts[9]);

            // Collect any additional parts as dates, if available
            for (int i = 10; i < parts.length; i++) {
                date.add(parts[i]);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Unable to parse number in line. Skipping line: " + line);
            return null;
        }

        return new Item(name, category, price, taxable, foodStamp, twentyOnePlus, glutenFree, peanuts, vegan, quantity,
                date);
    }

}
