import org.jdom2.JDOMException;
import org.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("shop")
public class ShopJAXRS {

    private HttpSession session;
    private CloudService service;


    public ShopJAXRS(HttpServletRequest request) {
        session = request.getSession();
        session.isNew();
        service = new CloudService();
    }


    @POST
    @Path("login")
    public String login(@FormParam("username") String username, @FormParam("password") String password) {
        if((new UserBean().login(username, password)).equals("OK")) {
            session.setAttribute("sessionid", username);
            return "LOGGEDIN"; //Burde virkelig opsætte faces korrekt..
        }
        else
            return "LOGINFAIL"; //...ja...
    }


    @GET
    @Path("loggedIn")
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
    @Path("logout")
    public void logout() {
        session.setAttribute("sessionid", null);
    }


    @GET
    @Path("shopItems/{shopID}")
    public String listItems(@PathParam("shopID") int shopID) {
        ArrayList<Item> prodList;
        JSONArray prodListJSON = null;

        try {
            prodList = service.listItems(shopID);
            prodListJSON = new JSONArray(prodList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prodListJSON != null ? prodListJSON.toString() : null;
    }


    @GET
    @Path("shops")
    public String listShops() {
        ArrayList<Shop> shops;
        JSONArray shopListJSON = null;

        try {
            shops = service.listShops();
            shopListJSON = new JSONArray(shops);
        } catch (JDOMException | IOException e) {
            System.out.println("Noget gik galt med listShops(): " + e);
        }

        return shopListJSON != null ? shopListJSON.toString() : null;
    }


    @POST
    @Path("basket")
    public void addToBasket(@FormParam("itemid") String itemID, @FormParam("itemstock") int itemStock) {
        shopItem(itemID);
        System.out.println("addToBasket: " + session.getAttribute("basket"));
    }


    @POST
    @Path("checkSession")
    public boolean checkSession() {
        return session.getAttribute("sessionid") != null;
    }


    @POST
    @Path("shopItem")
    @SuppressWarnings("unchecked")
    public void shopItem(String input) {
        HashMap<String, Integer> basket = (HashMap<String, Integer>) session.getAttribute("basketHashMap");

        if(basket == null)
            basket = new HashMap<>();

        if (basket.containsKey(input))
            basket.put(input, basket.get(input) +1);
        else
            basket.put(input, 1);

        session.setAttribute("basketHashMap", basket);
    }
}