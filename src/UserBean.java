import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

@SessionScoped
@ManagedBean(name="userBean")

public class UserBean{

    private String[] admin = {"admin", "kode"};
    private String password = "";
    private String username = "";
    private boolean isadmin;
    private boolean loggedin;
    private static final Namespace NS = Namespace.getNamespace("http://www.cs.au.dk/dWebTek/2014");
    private static final String key = "BA2F22BE812D783D22B8EA5E";
    private static final String baseURL = "http://webtek.cs.au.dk/cloud/";
    private ArrayList<Customer> customerList = new ArrayList<>();
    private boolean isNew = false;
    private String isNewMessage = "";



    public UserBean() {
    }

    private String md5crypt(String in) {
        try {
            return Arrays.toString(MessageDigest.getInstance("MD5").digest(in.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Kan ikke hashe null";
    }

    public String createCustomer(String name, String pass) {

        String result = null;

        Element root = new Element("createCustomer", NS);
        root.addContent(new Element("shopKey", NS).setText(key));
        root.addContent(new Element("customerName", NS).setText(name));
        root.addContent(new Element("customerPass", NS).setText(pass));
        Document doc = new Document(root);

        if (new CloudService().validate(doc).isSuccess()) {
            try {
                customerList = new CloudService().listCustomers(354);
                for (Customer c : customerList)
                    if (c.getCustomerName().equals(username))
                        return "login";
                else {
                        String res = new SAXBuilder().build(new StringReader(new CloudService().postit(baseURL + "createCustomer", doc))).getRootElement().getValue();
                        isNew = true;
                        isNewMessage = "Du er oprettet som ny bruger! Brugernavn: " + username + " og ID: " + res;

                        setLoggedin(true);
                        return "admin";
                    }
            } catch (IOException | JDOMException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public String login(String username, String password) {

        setIsadmin(false);
        setLoggedin(false);

            //Logger ind som admin...
         if (md5crypt(username).equals(md5crypt(admin[0])) && md5crypt(password).equals(md5crypt(admin[1]))) {
            isadmin = true;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isoauth", true); //Et lille hack; swap ud når admin-sessions er implementeret
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", "au554760");
             return "admin";
        } else if (!username.isEmpty() && !password.isEmpty()){

            //Ingen bruger :(
             try {
                 customerList = new CloudService().listCustomers(354);
                 for (Customer c : customerList)
                     if (c.getCustomerName().equals(username)) {
                         setLoggedin(true);
                         return "OK";
                     }
                     return "login";
             } catch (IOException e) {
                 return "login";
             }
        }

        return "login";
    }


    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedin", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isadmin", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isoauth", null);
        setLoggedin(false);
        setIsadmin(false);

        return "login";
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) { this.password = password; }
    public String getUsername() {
        return username;
    }
    public boolean isIsadmin() {
        return isadmin;
    }
    public boolean isLoggedin() {
        return loggedin;
    }
    public String getPassword() {return password; }
    public boolean getIsNew() {return isNew; }
    public void setisNew(boolean isnew) {this.isNew = isnew; }
    public String getisNewMessage() {return isNewMessage; }
    public void setIsNewMessage(String msg) {this.isNewMessage = msg;}
    public void setLoggedin(boolean loggedin) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isloggedin", true);
        this.loggedin = loggedin;
    }
    public void setIsadmin(boolean isadmin) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isadmin", true);
        this.isadmin = isadmin;
    }
}