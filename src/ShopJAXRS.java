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


    public ShopJAXRS(@Context HttpServletRequest request) {
        session = request.getSession();
    }


    @POST
    @Path("login")
    public String login(@FormParam("username") String username, @FormParam("password") String password) {
        session.setAttribute("sessionid", username);
        System.out.println(username + " er er logget ind");

        return "Brugernavn: " + session.getAttribute("sessionid");
    }


    /*



        Burde måske lave en bool-version af loggedIn til simple checks...




     */

    @POST
    @Path("logout")
    public void logout() {
        session.setAttribute("sessionid", null);
    }

    @POST
    @Path("done")
    public void done() {
        basket.clear();
        basket = null;
        session.setAttribute("sessionid", null);
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

        for (Map.Entry<String, Integer> entry : basket.entrySet()) {
            for (Item i : new CloudService().listItems(354)) {
                if (i.getItemID() == Integer.parseInt(entry.getKey()) && i.getItemStock() > 0) {
                    new Controller().adjustItemStock(i, -1);
                    total += i.getItemPrice() * entry.getValue();
                }
            }
        }

        return total;
    }


    @GET
    @Path("checkBasket")
    @SuppressWarnings("unchecked")
    public String checkBasket() {
        String out = "";
        ArrayList<Item> prodList = new CloudService().listItems(354);

        basket = (HashMap<String, Integer>) session.getAttribute("basket");

        if (basket != null) {
            for (Map.Entry<String, Integer> entry : basket.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                for (Item i : prodList) {
                    if (i.getItemID() == Integer.parseInt(key) && i.getItemStock() > 0) {
                        new Controller().adjustItemStock(i, -1);
                        out += i.getItemName().substring(0, 27) + " x" + value;
                    }
                }
            }
            return out;
        }

        else
            return "fail";
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

        if(basket == null)
            basket = new HashMap<>();

        if (basket.containsKey(itemid))
            basket.put(itemid, basket.get(itemid) +1);
        else
            basket.put(itemid, 1);

        session.setAttribute("basket", basket);
        return session.getAttribute("sessionid").toString();
    }
}