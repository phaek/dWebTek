import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;

@ManagedBean(name="shopBean")
@ApplicationScoped


public class ShopBean {
    private ArrayList<Item> prodList = null;
    private ArrayList<Shop> shopList = null;
    private Item item = null;
    private String message;
    private int shopid = 354;
    private Shop shop;

    public ShopBean() {

        setShop(354);
        rebuildProdList();
        rebuildShopList();
    }

    ArrayList<Shop> rebuildShopList() {
        return new CloudService().listShops();
    }

    ArrayList<Item> rebuildProdList() {
        return new CloudService().listItems(shopid);
    }

    public ArrayList<Item> getProdList() {
        return prodList;
    }
    public ArrayList<Shop> getShopList() { return shopList; }
    public void setItem(Item i){ item = i; }
    public Item getItem() {
        return item;
    }
    public Shop getShop() {
        return shop;
    }

    public void setShop(int shopid) {

            for (Shop s : new CloudService().listShops())
                if (s.getShopID() == shopid)
                    this.shop = s;
    }
}