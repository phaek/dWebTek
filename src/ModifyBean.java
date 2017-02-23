import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;


@ManagedBean(name="ModifyBean")
@SessionScoped
public class ModifyBean {
    private String itemID;
    private Item item;
    private String fejl = "";

    @ManagedProperty("#{shopBean}")
    transient private ShopBean shopBean;

    public String modify(){

        try {
            setItemID("" + item.getItemID());
            new Controller().modifyItem(item.getItemID(), item.getItemName(), item.getItemPrice(), item.getItemURL(), item.getItemDescription());
            shopBean.rebuildProdList();
            System.out.println("Modify er godkendt");
        } catch (Exception e) {
            fejl = "Ugyldig XML. Husk at lukke alle åbne tags. Gyldige tags er <bold></bold>, <italics></italics>, <list></list> og <item></item>";
            return "WRONG"; //Håndter forkert XML
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
    public ShopBean getShopBean() {return shopBean; }
    public void setShopBean(ShopBean sb){shopBean = sb; }
    public void setFejl(String f) {fejl = f; }
    public String getFejl() { return fejl; }
}