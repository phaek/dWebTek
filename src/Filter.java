import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Filter implements javax.servlet.Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (Boolean.TRUE.equals(((HttpServletRequest) request).getSession().getAttribute("isoauth")) || Boolean.TRUE.equals(((HttpServletRequest) request).getSession().getAttribute("isadmin")))
            filterChain.doFilter(request, response);
        else {
            if (httpServletRequest.getRequestURI().contains("/oauthCallBack") || httpServletRequest.getRequestURI().contains("admin/login.jsf"))
                filterChain.doFilter(request, response);
            else {
                httpServletResponse.sendRedirect(((HttpServletRequest) request).getContextPath() + "login.jsf");
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}