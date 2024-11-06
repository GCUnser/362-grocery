import java.util.ArrayList;
public class Cart {
    ArrayList<Item> items = new ArrayList<>();

    private void addItemToCart(Item item) {
        items.add(item);
    }
}
