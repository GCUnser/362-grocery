import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private boolean twentyOnePlus;
    private boolean foodStamp;
    private boolean taxable;
    private String name;
    private String category;
    private double price;
    private ArrayList<String> date; //sorted from smallest date (next to expire) to largest date (furthest from expiration)

    public Item(String name, String category, double price, boolean taxable, boolean foodStamp, boolean tOPlus) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.taxable = taxable;
        this.foodStamp = foodStamp;
        twentyOnePlus = tOPlus;
        this.date = new ArrayList<>(); // Initialize date list
    }

    public Item(String name, String category, double price, boolean taxable, boolean foodStamp, boolean tOPlus, int quantity, ArrayList<String> date) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.taxable = taxable;
        this.foodStamp = foodStamp;
        twentyOnePlus = tOPlus;
        this.date = new ArrayList<>(date);
    }

    public void addQuantity(String date, int quantity) {
        for(int j = 0; j <= this.date.size(); j++)
        {
            if(j == this.date.size())
            {
                for (int i = 0; i < quantity; i++) {
                    this.date.add(date);
                }
                break;
            }
            else if(date.compareTo(this.date.get(j)) < 0)
            {
                for (int i = 0; i < quantity; i++) {
                    this.date.add(j, date);
                }
                break;
            }
        }

    }

    public void removeDate(int toRemove)
    {
        for(int i = 0; i < toRemove; i++) {
            if (!date.isEmpty()) {
                date.removeFirst();
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getCategory(){
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

    public ArrayList<String> getDateList() {
        return new ArrayList<>(date);
    }

    @Override
    public String toString() {
        return name + "," + category + "," + String.format("%.2f", price) + "," + date.size() + "," + taxable + "," + foodStamp + "," + twentyOnePlus + "," + String.join(",", date);
    }

    public static Item fromString(String line) {
        String[] parts = line.split(",");

        // Ensure there are enough parts to create an Item, otherwise print an error and return null
        if (parts.length < 6) {
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
        ArrayList<String> date = new ArrayList<>();

        try {
            price = Double.parseDouble(parts[2]);
            quantity = Integer.parseInt(parts[3]);
            taxable = Boolean.parseBoolean(parts[4]);
            foodStamp = Boolean.parseBoolean(parts[5]);
            twentyOnePlus = Boolean.parseBoolean(parts[6]);

            // Collect any additional parts as dates, if available
            for (int i = 7; i < parts.length; i++) {
                date.add(parts[i]);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Unable to parse number in line. Skipping line: " + line);
            return null;
        }

        return new Item(name, category, price, taxable, foodStamp, twentyOnePlus, quantity, date);
    }

}
