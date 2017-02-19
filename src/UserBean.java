import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

@SessionScoped
@ManagedBean(name="userBean")

public class UserBean{

    private String[] admin = {"admin", "kode"};
    private String password = "";
    private boolean isOauth;
    private String username = "";
    private boolean isadmin;
    private boolean loggedin;


    public UserBean() throws NoSuchAlgorithmException {
    }

    String md5crypt(String in) throws NoSuchAlgorithmException {
        try {
            return Arrays.toString(MessageDigest.getInstance("MD5").digest(in.getBytes()));
        } catch (Exception e) {

        }

        return "Failed";
    }


    public String login() throws NoSuchAlgorithmException {

        username = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("username");
        password = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("password");

        try {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isoauth").toString().contains(""))
                setIsOauth(true);
            else
                setIsOauth(false);
        } catch (Exception ignored) {
        }

        if (isOauth) {
            isOauth = true;
            isadmin = true;
            setLoggedin(false);

            System.out.println("Jeg er OAuth!");

            return "admin";
        } else if (md5crypt(username).equals(md5crypt(admin[0])) && md5crypt(password).equals(md5crypt(admin[1]))) {
            isadmin = true;
            setLoggedin(false);
            setIsOauth(false);

            System.out.println("Jeg er admin!");
            return "admin";
        } else {

            setIsOauth(false);
            setIsadmin(false);
            setLoggedin(true);
            System.out.println("Jeg har ingen bruger :(");
        }

        return "login";
    }


    public String logout() {
        setLoggedin(false);
        setIsadmin(false);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedin", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isadmin", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isoauth", null);

        System.out.println("Alt er logget ud");

        return "login";
    }

    public void setIsOauth(boolean isOauth) {
        this.isOauth = isOauth;
    }

    public boolean isIsOauth() {
        return isOauth;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public boolean isLoggedin() {
        return loggedin;
    }
}