package ru.headrich.topjava.web.mvc.webflow.filters;

import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Montana on 05.07.2016.
 */
@WebFilter(servletNames = {"MealsController","UsersController"}, urlPatterns = {""})
public class AuthFilter implements Filter {
    private static final String[] ignoreCases = {"/logout","/login","registration","/logout/*","/login/*"};
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;


        HttpSession s = req.getSession(false);
        if(Arrays.asList(ignoreCases).contains(req.getServletPath())){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }else
        if(s!=null) {
            if ( s.isNew() || s.getAttribute("user") == null) {
                res.getWriter().append("Access denied!");
                res.sendRedirect("login");
                return;
            }else{
                User u = (User) s.getAttribute("user");
                if(!u.getAuthorities().contains(Role.ROLE_ADMIN) && req.getServletPath().equals("/users")){
                    System.out.print(req.getServletPath() + u.getAuthorities());
                    res.sendRedirect("meals");
                    return;
                }
            }
        }else{
            res.sendRedirect("login");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
