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

    @GET
    @Path("/loggedIn")
    public String loggedIn() {
        if (session.getAttribute("sessionid") != null)
            return session.getAttribute("sessionid").toString();
        else
            return "Ikke logget ind..";
    }


    /*



        Burde måske lave en bool-version af loggedIn til simple checks...




     */

    @POST
    @Path("/logout")
    public void logout() {
        session.setAttribute("sessionid", null);
    }


    @GET
    @Path("/listShopItems/{shopID}")
    public String listItems(@PathParam("shopID") int shopID) {
        ArrayList<Item> prodList = service.listItems(shopID);
        return gson.toJson(prodList);
    }


    @GET
    @Path("/checkBasket/{userid}")
    public HashMap<String, Integer> checkBasket(@PathParam("userid") String user) {
        if (session.getAttribute("sessionid").equals(user)) {

            for(Map.Entry<String, Integer> entry : basket.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();

                System.out.println(key + ":" + value);
            }
            return basket;
        }
        else
            return new HashMap<>();
    }


    @GET
    @Path("listShops")
    public String listShops() {
        return gson.toJson(new CloudService().listShops());
    }


    @POST
    @Path("/checkSession")
    public boolean checkSession() {
        return session.getAttribute("sessionid") != null;
    }


    @POST
    @Path("/addtobasket")
    @SuppressWarnings("unchecked")
    public void shopItem(@FormParam("id") String itemid) {
        basket = (HashMap<String, Integer>) session.getAttribute("basket");

        if(basket == null)
            basket = new HashMap<>();

        if (basket.containsKey(itemid))
            basket.put(itemid, basket.get(itemid) +1);
        else
            basket.put(itemid, 1);

        System.out.println(itemid + " er lagt i kurven (" + basket.get(itemid) + ")");
        session.setAttribute("basket", basket);
    }
}