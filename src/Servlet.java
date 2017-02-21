import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@ManagedBean
@ApplicationScoped
public class Servlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String body = "grant_type=authorization_code" + "&code=" + request.getParameter("code") + "&client_id=dovs" + "&client_secret=hello" + "&redirect_uri=http://localhost:1337/oauthCallBack";

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
            System.out.println(request.getParameter("username") + " er logget ind (OAuth)");
            request.getSession().setAttribute("isoauth", true);
            request.getSession().setAttribute("username", request.getParameter("username"));
            response.sendRedirect("admin/oauth.jsf");
        } else {
            System.out.println("Serveren afviste forespørgslen: " + connection.getResponseMessage());
        }
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {}
}
