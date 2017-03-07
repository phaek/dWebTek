import com.google.gson.Gson;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;


@Path("shop")
public class ShopJAXRS {

    private HttpSession session;
    private CloudService service = new CloudService();
    private Gson gson = new Gson();
    private HashMap<String, Integer> basket;
    private ArrayList<Item> prodList = service.listItems(354);
    private String[] admin = {"admin", "password"};


    public ShopJAXRS(@Context HttpServletRequest request) {
        session = request.getSession();
    }


    @POST
    @Path("addStock")
    public void addStock(@FormParam("itemid") int itemid, @FormParam("stock") int stock) {
        new Controller().adjustItemStock(itemid, stock);
    }

    @POST
    @Path("createCustomer")
    public String createCustomer(@FormParam("username") String username, @FormParam("password") String password) {
        return new UserBean().createCustomerClean(username, password);
    }


    @POST
    @Path("login")
    public String login(@FormParam("username") String username, @FormParam("password") String password) {
        UserBean ubean = new UserBean();

        for (Customer c : service.listCustomers(354))
            if(c.getCustomerName().equals(username)) {
                try {
                    Document doc = new SAXBuilder().build(new StringReader(service.login(username, password)));
                    username = doc.getRootElement().getDescendants(new ElementFilter("customerName")).next().getValue();
                        session.setAttribute("usertype", "user");
                        session.setAttribute("sessionid", username);
                        return username;

                } catch (JDOMException | IOException e) {
                    System.out.println("Problem med login: " + e);
                }
            }

        if(ubean.md5crypt(username).equals(ubean.md5crypt(admin[0])) && ubean.md5crypt(password).equals(ubean.md5crypt(admin[1]))) {
            session.setAttribute("usertype", "admin");
            session.setAttribute("sessionid", username);
            return username;
        }

        return "FAIL";
    }

    @POST
    @Path("logout")
    public String logout() {
        session.setAttribute("sessionid", null);
        session.setAttribute("usertype", null);

        return "Du er nu logget ud";
    }


    @POST
    @Path("done")
    @SuppressWarnings("unchecked")
    public void done() {

        Controller con = new Controller();

        for (Map.Entry<String, Integer> entry : ((HashMap<String, Integer>) session.getAttribute("basket")).entrySet())
            for (Item i : prodList)
                if (i.getItemID() == Integer.parseInt(entry.getKey()))
                    con.adjustItemStock(i.getItemID(), -entry.getValue());



        session.setAttribute("cachedProdList", null);
        session.setAttribute("basket", null);
    }


    @GET
    @Path("/listShopItems/{shopID}")
    public String listItems(@PathParam("shopID") int shopID) {
        ArrayList<Item> prodList = service.listItems(shopID);
        return gson.toJson(prodList);
    }

    @GET
    @Path("getTotal")
    @SuppressWarnings("unchecked")
    public int getTotal() {
        int total = 0;

        for (Map.Entry<String, Integer> entry : ((HashMap<String, Integer>) session.getAttribute("basket")).entrySet())
            for (Item i : prodList)
                if (i.getItemID() == Integer.parseInt(entry.getKey()) && i.getItemStock() > 0)
                    total += i.getItemPrice() * entry.getValue();

        return total;
    }


    @GET
    @Path("checkBasket")
    @SuppressWarnings("unchecked")
    public String checkBasket() {
        String out = "";

        if(session.getAttribute("sessionid") == null || session.getAttribute("sessionid").equals(""))
            return "NOSESSION";

        for (Map.Entry<String, Integer> entry : ((HashMap<String, Integer>) session.getAttribute("basket")).entrySet())
            for (Item i : prodList)
                if (Integer.toString(i.getItemID()).equals(entry.getKey()))
                    out += "<b>" + i.getItemName() + "</b> x" + entry.getValue() + "<br />";

        return out;
    }


    @GET
    @Path("listShops")
    public String listShops() {

        return gson.toJson(new CloudService().listShops());
    }


    @POST
    @Path("/addtobasket")
    @SuppressWarnings("unchecked")
    public String shopItem(@FormParam("id") String itemid) {
        basket = (HashMap<String, Integer>) session.getAttribute("basket");
        HashMap<String, Integer> cachedProdList = (HashMap<String, Integer>) session.getAttribute("cachedProdList");

        if(basket == null) {
            basket = new HashMap<>();
        }

        if(cachedProdList == null) {
            cachedProdList = new HashMap<>();

            for (Item i : prodList) {
                cachedProdList.put(Integer.toString(i.getItemID()), i.getItemStock());
            }
        }

        /* Check */

        if (basket.containsKey(itemid) && cachedProdList.get(itemid) > 0) {
            basket.put(itemid, basket.get(itemid) + 1);
            cachedProdList.put(itemid, cachedProdList.get(itemid) -1);
        }
        else if (cachedProdList.get(itemid) > 0) {
            basket.put(itemid, 1);
            cachedProdList.put(itemid, cachedProdList.get(itemid) -1);
        }
        else
            System.out.println("Varen (" + itemid + ") er ikke på lager :(");

        session.setAttribute("cachedProdList", cachedProdList);
        session.setAttribute("basket", basket);

        return session.getAttribute("sessionid").toString();
    }
}