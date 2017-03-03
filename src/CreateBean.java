import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="CreateBean")
@ViewScoped
public class CreateBean {
    private String itemID;
    private String itemName;
    private int newID;

    @ManagedProperty("#{shopBean}")
    transient private ShopBean shopBean;


    public String createItem(){
        try {
            newID = new Controller().createItem(getItemName()).getResult();
            shopBean.rebuildProdList();
        } catch (Exception ignored) {
        }
        return "modifyItem.jsf?faces-redirect=true&id=" + newID;
    }

    public String getItemName(){
        return itemName;
    }
    public void setItemName(String s){
        itemName = s;
    }
    public String getItemID(){
        return itemID;
    }
    public void setItemID (String s){
        itemID = s;
    }
    public ShopBean getShopBean() { return shopBean; }
    public void setShopBean(ShopBean sb) { this.shopBean = sb; }

}