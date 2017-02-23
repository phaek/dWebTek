import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@SessionScoped
@ManagedBean(name="userBean")

public class UserBean{

    private String[] admin = {"admin", "kode"};
    private String password = "";
    private String username = "";
    private boolean isadmin;
    private boolean loggedin;



    public UserBean() throws NoSuchAlgorithmException {
    }

    String md5crypt(String in) throws NoSuchAlgorithmException {
        try {
            return Arrays.toString(MessageDigest.getInstance("MD5").digest(in.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Kan ikke hashe null";
    }


    public String login() throws NoSuchAlgorithmException {

        username = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("username");
        password = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("password");



            //Logger ind som admin...
         if (md5crypt(username).equals(md5crypt(admin[0])) && md5crypt(password).equals(md5crypt(admin[1]))) {
            isadmin = true;
            setLoggedin(false);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isoauth", true); //Et lille hack; swap ud når admin-sessions er implementeret
             FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", "au554760");

            System.out.println("Jeg er admin!");
            return "admin";
        } else if (!username.isEmpty() && !password.isEmpty()){

            //Ingen bruger :(
            setIsadmin(false);
            setLoggedin(true);
            System.out.println("Logget ind som almindelig bruger");
            return "admin";
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

    public void setUsername(String username) {
        this.username = username;
    }
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
    public void setLoggedin(boolean loggedin) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isloggedin", true);
        this.loggedin = loggedin;
    }
    public void setIsadmin(boolean isadmin) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isadmin", true);
        this.isadmin = isadmin;
    }
}