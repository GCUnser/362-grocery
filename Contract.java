import java.util.ArrayList;
import java.util.List;


public class Contract {

    private String contractName;
    private String dateExpire;
    private double renewPrice;
    private double terminatePrice;
    private double addItemPrice;
    private List<Item> inventory = new ArrayList<>();

    public Contract(String cName, String dExp, double rPrice, double tPrice, double addPrice){
        contractName = cName;
        dateExpire = dExp;
        renewPrice = rPrice;
        terminatePrice = tPrice;
        addItemPrice = addPrice;
    }

    public void updateDate(String date){
        dateExpire = date;
    }

    public void addItemToContract(Item i){
        inventory.add(i);
    }

    public String getContractName(){
        return contractName;
    }

    public String getDateExpire(){
        return dateExpire;
    }

    public double getRenewPrice(){
        return renewPrice;
    }

    public double getTerminatePrice(){
        return terminatePrice;
    }

    public double getAddItemPrice(){
        return addItemPrice;
    }

    public List<Item> getInventory(){
        return inventory;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(Item i : inventory){
            s.append(i.getName()).append(",").append(i.getCategory()).append(",").append(i.getPrice()).append(",");
        }
        return contractName + "," + dateExpire + "," + renewPrice + "," + terminatePrice + "," + addItemPrice + "," + s;
    }

    public static Contract fromString(String line){
        String[] parts = line.split(",");

        // Ensure there are enough parts to create an Item, otherwise print an error and return null
        if (parts.length < 4) {
            System.err.println("Error: Invalid line format. Skipping line: " + line);
            return null;
        }

        String contractName = parts[0];
        String dateExpire = parts[1];
        double renewPrice;
        double terminatePrice;
        double addItemPrice;
        String itemName;
        String itemCategory;
        double itemPrice;
        Contract c;

        try {
            renewPrice = Double.parseDouble(parts[2]);
            terminatePrice = Double.parseDouble(parts[3]);
            addItemPrice = Double.parseDouble(parts[4]);

            c = new Contract(contractName, dateExpire, renewPrice, terminatePrice, addItemPrice);

            // Collect any additional parts as items, if available
            for (int i = 5; i < parts.length; i = i + 3) {
                itemName = parts[i];
                itemCategory = parts[i+1];
                itemPrice = Double.parseDouble(parts[i+2]);
                c.addItemToContract(new Item(itemName, itemCategory, itemPrice));
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Unable to parse number in line. Skipping line: " + line);
            return null;
        }

        return c;
    }
}
