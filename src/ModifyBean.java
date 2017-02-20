import org.jdom2.Element;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="ModifyBean")
@SessionScoped
public class ModifyBean {
    private String itemID;
    private Item item;
    private String message;

    @ManagedProperty("#{shopBean}")
    transient private ShopBean shopBean;

    public ShopBean getShopBean() {return shopBean; }

    public void setShopBean(ShopBean sb){shopBean = sb; }


    public String modify(){
        try {
            setItemID(""+item.getItemID());
            new Controller().modifyItem(item.getItemID(), item.getItemName(), item.getItemPrice(), item.getItemURL(), item.getItemDescription());
            shopBean.rebuildProdList();
        } catch (Exception e) {
            System.out.println("Modify gik galt :(\n" + e.getMessage());
            System.out.println(e.getMessage());
        }
        return "admin";
    }

    public String delete() {
        try {
            new Controller().deleteItem(item.getItemID());
            shopBean.rebuildProdList();
            System.out.println("Item " + item.getItemID() + " marked for deletion!");
        } catch (Exception e) {
            System.out.println("ModifyBean.delete() fejlede :(\n" + e);
        }

        return "admin";
    }

    public String getItemID(){
        return itemID;
    }

    public void setItemID (String s){
        itemID = s;
        for(Item i : shopBean.getProdList()) {
            if(Integer.toString(i.getItemID()).equals(itemID))
                item = i;
        }
    }

    public Item getItem(){
        return item;
    }

    public void setItem (Item i) {item = i; }
}