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
    private HashMap<String, Integer> cachedProdList;
    private ArrayList<Item> prodList = service.listItems(354);


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

    @POST
    @Path("logout")
    public void logout() {
        session.setAttribute("sessionid", null);
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

        for (Map.Entry<String, Integer> entry : basket.entrySet()) {
            for (Item i : prodList) {
                if (i.getItemID() == Integer.parseInt(entry.getKey()) && i.getItemStock() > 0) {
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
        basket = (HashMap<String, Integer>) session.getAttribute("basket");


        for (Map.Entry<String, Integer> entry : basket.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            for (Item i : prodList)
                if (Integer.toString(i.getItemID()).equals(key))
                    out += "<b>" + i.getItemName() + "</b> x" + value + "<br />";
        }

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
        cachedProdList = (HashMap<String, Integer>) session.getAttribute("cachedProdList");

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