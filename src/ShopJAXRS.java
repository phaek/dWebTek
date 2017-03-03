import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

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


    public ShopJAXRS(@Context HttpServletRequest request) {
        session = request.getSession();
    }


    @POST
    @Path("login")
    public String login(@FormParam("username") String username, @FormParam("password") String password) {
        if(session.isNew())
            session.setAttribute("sessionid", username);
        return "Korrekt login: " + username; //Burde virkelig opsætte faces korrekt..
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
    @Path("listShops")
    public String listShops() {
        return gson.toJson(new CloudService().listShops());
    }


    @POST
    @Path("/basket")
    public void addToBasket(@FormParam("itemid") String itemID) {
        shopItem(itemID);
        System.out.println("addToBasket: " + session.getAttribute("basket"));
    }


    @POST
    @Path("/checkSession")
    public boolean checkSession() {
        return session.getAttribute("sessionid") != null;
    }


    @POST
    @Path("/shopItem")
    @SuppressWarnings("unchecked")
    public void shopItem(String input) {
        HashMap<String, Integer> basket = (HashMap<String, Integer>) session.getAttribute("basketHashMap");

        if(basket == null)
            basket = new HashMap<>();

        if (basket.containsKey(input))
            basket.put(input, basket.get(input) +1);
        else
            basket.put(input, 1);

        session.setAttribute("basket", basket);
    }
}