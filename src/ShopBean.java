import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.util.ArrayList;

@ManagedBean(name="shopBean")
@ApplicationScoped


public class ShopBean {
    private ArrayList<Item> prodList = null;
    private Item item = null;
    private String message;
    private int shopid = 354;

    public ShopBean() {
        rebuildProdList();
    }

    ArrayList<Item> rebuildProdList() {
        try {
            prodList = new CloudService().listItems(shopid);
        } catch (IOException e) {
            System.out.println("Genopbygning af produktlisten fejlede: " + e);
            message = e.getMessage();
        }

        return prodList;
    }

    public ArrayList<Item> getProdList() {
        return prodList;
    }
    public void setItem(Item i){ item = i; }
    public Item getItem() {
        return item;
    }
}