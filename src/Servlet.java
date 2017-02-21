import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@ManagedBean
@ApplicationScoped
public class Servlet extends HttpServlet {

    private String code;
    private String username;


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Class-navn: " + this.getClass().getName());
        HttpServletRequest httpServletRequest = request;
        HttpSession session = httpServletRequest.getSession();

        code = request.getParameter("code");
        username = request.getParameter("username");

        System.out.println("Code: " + code + "\nUsername: " + username);

        String body = "grant_type=authorization_code" + "&code=" + code + "&client_id=dovs" + "&client_secret=hello" + "&redirect_uri=http://localhost:1337/oauthCallBack";

        //Post stuff
        String reqUrl = "https://services.brics.dk/java/dovs-auth/token";
        HttpURLConnection connection = (HttpURLConnection) new URL(reqUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.write(body.getBytes());

        dataOutputStream.flush();
        dataOutputStream.close();

        if (connection.getResponseCode() == 200) {
            System.out.println("Response code: " + connection.getResponseCode());
            request.getSession().setAttribute("isoauth", true);
            request.getSession().setAttribute("username", username);
            response.sendRedirect("admin/oauth.jsf");
        } else {
            String responseMessage = connection.getResponseMessage();
        }
    }

    public String getUsername() { return username; }
    public String getCode() { return code; }
    public void setCode(String in) { this.code = in; }
    public void setUsername(String in) { this.username = in; }
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {}
}
