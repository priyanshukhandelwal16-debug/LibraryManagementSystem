package com.library.util;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Protects all dashboard pages. Any request that is not the login page,
 * the logout servlet, or a static resource (css/js) must have a valid
 * session with an "admin" attribute, otherwise it is redirected to login.jsp
 * Registered declaratively in web.xml (see <filter> / <filter-mapping>).
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        boolean isPublicResource =
                path.equals("/login.jsp") ||
                path.equals("/LoginServlet") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.equals("/") ;

        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("admin") != null);

        if (isPublicResource || loggedIn) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(contextPath + "/login.jsp");
        }
    }
}
