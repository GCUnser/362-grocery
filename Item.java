import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private boolean foodStamp;
    private boolean taxable;
    private String name;
    private double price;
    private int quantity;
    private ArrayList<String> date;

    public Item(String name, double price, boolean taxable, boolean foodStamp) {
        this.name = name;
        this.price = price;
        this.taxable = taxable;
        this.foodStamp = foodStamp;
        this.quantity = 0;
    }

    public Item(String name, double price, boolean taxable, boolean foodStamp, int quantity, ArrayList<String> date) {
        this.name = name;
        this.price = price;
        this.taxable = taxable;
        this.foodStamp = foodStamp;
        this.quantity = quantity;
        this.date = new ArrayList<String>(date);
    }

    public void addQuantity(String date)
    {
        this.date.add(date);
        quantity += 1;
    }

    public void addQuantity(String date, int quantity)
    {
        this.quantity += quantity;
        for(int i = 0; i < quantity; i++)
        {
            this.date.add(date);
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
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return name + "," + String.format("%.2f", price) + "," + quantity + "," + Boolean.toString(taxable) + "," + Boolean.toString(foodStamp) + "," + String.join(",", date);
    }

    public static Item fromString(String line) {
        String[] parts = line.split(",");
        String name = parts[0];
        double price = Double.parseDouble(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        boolean taxable = Boolean.parseBoolean(parts[3]);
        boolean foodStamp = Boolean.parseBoolean(parts[4]);
        ArrayList<String> date = new ArrayList<String>();
        for(int i = 5; i < 5 + quantity; i++)
        {
            date.add(parts[i]);
        }
        return new Item(name, price, taxable, foodStamp, quantity, date);
    }
}

