/**
 * Created by Rasmus Dalsgaard on 2/12/17.
 */
public class Item {

    private int itemID;
    private String itemName;
    private String itemURL;
    private int itemPrice;
    private int itemStock;
    private String itemDescription;

    public Item (int itemID, String itemName, String itemURL, int itemPrice, int itemStock, String itemDescription) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.itemDescription = itemDescription;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemURL() {
        return itemURL;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getItemStock() {
        return itemStock;
    }

    public String getItemDescription() { return itemDescription; }
}
