import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


@ManagedBean
@SessionScoped
public class OAuth {

    private String username;
    private String code;


    public void sendTo(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            String getOAuth = "https://services.brics.dk/java/dovs-auth/authorize?client_id=dovs&redirect_uri=http://localhost:1337/admin/login.jsf&response_type=code";
            externalContext.redirect(getOAuth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String tokenize() throws IOException {
        String reqURL = "https://services.brics.dk/java/dovs-auth/token";
        String urlParameters = "grant_type=authorization_code&code=" + getCode() + "&client_id=dovs&client_secret=allthesecrets&redirect_uri=http://localhost:1337/admin/admin.jsf";

        byte[] POSTdata = urlParameters.getBytes(StandardCharsets.UTF_8);
        HttpURLConnection httpConnection = (HttpURLConnection) new URL(reqURL).openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setInstanceFollowRedirects(false);
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("charset", "UTF-8");
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConnection.setRequestProperty("Content-Length", Integer.toString(POSTdata.length));
        httpConnection.setUseCaches(false);
        try (DataOutputStream out = new DataOutputStream(httpConnection.getOutputStream())) {
            out.write(POSTdata);
        }


        if (httpConnection.getResponseCode() == 200) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isoauth", true);
            return "admin";
        }

        return "login";
    }


    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
    public void setUsername(String username) {this.username = username; }
    public String getUsername() {return username; }
}
