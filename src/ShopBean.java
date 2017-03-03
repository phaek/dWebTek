import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;

@ManagedBean(name="shopBean")
@ApplicationScoped


public class ShopBean {
    private int shopid = 354;
    private ArrayList<Item> prodList = new CloudService().listItems(shopid);
    private ArrayList<Shop> shopList;
    private Item item = null;
    private String message;
    private Shop shop;

    public ShopBean() {
        setShop(354);
        rebuildProdList();
    }

    public ArrayList<Shop> rebuildShopList() {return new CloudService().listShops();}
    public ArrayList<Item> rebuildProdList() {return new CloudService().listItems(shopid);}
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

        shopList = new CloudService().listShops();

        for (Shop s : shopList)
            if (s.getShopID() == shopid)
                this.shop = s;
    }
}