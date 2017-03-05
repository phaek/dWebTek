import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private String[] admin = {"phaek", "password"};


    public ShopJAXRS(@Context HttpServletRequest request) {
        session = request.getSession();
    }

    @POST
    @Path("login")
    public String login(@FormParam("username") String username, @FormParam("password") String password) {
        UserBean ubean = new UserBean();
        session.setAttribute("sessionid", username);

        for (Customer c : service.listCustomers(354))
            if(c.getCustomerName().equals(username))
                session.setAttribute("usertype", "user");

        if(ubean.md5crypt(username).equals(ubean.md5crypt(admin[0])) && ubean.md5crypt(password).equals(ubean.md5crypt(admin[1])))
            session.setAttribute("usertype", "admin");

        return "Brugernavn: " + session.getAttribute("sessionid") + " (" + session.getAttribute("usertype") +")";
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
    public void done() {
        //TODO: adjustItemStock for hvert key:value-par i basket
        session.setAttribute("cachedProdList", null);
        session.setAttribute("basket", null);
        System.out.println("Køb gennemført; kurv nulstillet");
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
        basket = (HashMap<String, Integer>) session.getAttribute("basket");

        for (Map.Entry<String, Integer> entry : basket.entrySet())
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