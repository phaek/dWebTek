public class Shop {

    private int shopID;
    private String shopName;
    private String shopURL;

    public Shop(int id, String url, String name) {
        this.shopID = id;
        this.shopName = name;
        this.shopURL = url;
    }

    public int getShopID() {return shopID; }
    public String getShopName() {return shopName; }
    public String getShopURL() {return shopURL; }
    public void setShopID(int blabla) {this.shopID = blabla; }
}
