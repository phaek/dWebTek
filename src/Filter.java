import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Filter implements javax.servlet.Filter {

    String[] allowed = {"au554760", "au553122", "au554107", "au523910"};


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        /* try {
            if (Boolean.TRUE.equals(((HttpServletRequest) request).getSession().getAttribute("isoauth")) && (((HttpServletRequest) request).getSession().getAttribute("username").equals("au554760") || ((HttpServletRequest) request).getSession().getAttribute("username").equals("au554107") || ((HttpServletRequest) request).getSession().getAttribute("username").equals("au553122") || ((HttpServletRequest) request).getSession().getAttribute("username").equals("au523910")))
                filterChain.doFilter(request, response);
            else {
                if (httpServletRequest.getRequestURI().contains("/oauthCallBack") || httpServletRequest.getRequestURI().contains("admin/login.jsf"))
                    filterChain.doFilter(request, response);
                else {
                    if (httpServletRequest.getRequestURI().contains("/admin/"))
                        httpServletResponse.sendRedirect(((HttpServletRequest) request).getContextPath() + "/admin/login.jsf");
                }
            }
        } catch (Exception e) {
            System.out.println("Filterfejl: " + e.getMessage());
        } */

        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}