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

    public ShopBean() {
        rebuildProdList();
    }


    ArrayList<Item> rebuildProdList() {
        try {
            prodList = new CloudService().listItems();
        } catch (IOException e) {
            message = e.getMessage();
        }

        return prodList;
    }


    public ArrayList<Item> getProdList() {
        return prodList;
    }

    public Item getItem() {
        return item;
    }
}
