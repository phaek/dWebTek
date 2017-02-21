public class Item {


    private int itemID;
    private String itemName;
    private String itemURL;
    private String itemDescription;
    private int itemPrice;
    private int itemStock;
    private Item item;

    Item(int itemID, String itemName, String itemURL, int itemPrice, int itemStock, String itemDescription) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.itemDescription = itemDescription;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public String getItemDescription() { return itemDescription; }
    public String getItemDescription2() {
        return new xml2html().presenter(itemDescription).trim();
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemStock(int itemStock) {
        this.itemStock = itemStock;
    }

    public int getItemStock() { return itemStock; }

    public Item getItem() { return item; }
}