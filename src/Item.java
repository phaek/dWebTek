public class Item {

    private final int itemID;
    private final String itemName;
    private final String itemURL;
    private final int itemPrice;
    private final int itemStock;
    private final String itemDescription;

    Item(int itemID, String itemName, String itemURL, int itemPrice, int itemStock, String itemDescription) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.itemDescription = itemDescription;
    }

    int getItemID() {
        return itemID;
    }

    String getItemName() {
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
