package ru.headrich.topjava.web.mvc.webflow.filters;



import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Montana on 05.07.2016.
 */
@WebFilter(filterName = "AuthFilter", servletNames = {"MealsController","UsersController"}, urlPatterns = {"/users/*","/profile/*"})
public class AuthFilter implements Filter {
    private static final String[] ignoreCases = {"/logout","/login","/registration","/registrate","/registrate","/logout/*","/login/*","/index.jsp","/top","/","/meals"};
    static final Pattern uriPattern =   Pattern.compile("^/[0-9A-Za-z_]*(/(login|registrate|logout|index|meals|))((\\/([0-9A-Za-z_]*)?)?|(\\?([\\w-]+(=[\\w-]*)?(&[\\w-]+(=[\\w-]*)?)*)?))$");
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(res);
        wrapper.setCharacterEncoding("UTF-8"); // после коммита или вызова getwriter не работает метод.
        res.setCharacterEncoding("UTF-8");


        HttpSession s = req.getSession(false);
        System.out.println(" AuthFilter req: servlet: " + req.getServletPath() + "   url: " + req.getRequestURL() + "    cut url: "+ req.getRequestURL().replace(0,req.getRequestURL().lastIndexOf("/"),""));
        Matcher urlMatcher = uriPattern.matcher(req.getRequestURI());
        if( urlMatcher.matches()){
            filterChain.doFilter(servletRequest, servletResponse);
            System.out.println("after AuthFilter Call doFilter");
            return;
        }else
        if(s!=null) {
            if ( s.isNew() || s.getAttribute("user") == null) {
                res.setContentType("text/html; charset=utf-8");//указав charset здесь исправилась кодировка и Залогинься отображ норм.
                PrintWriter rw = res.getWriter();
                    rw.append("<p><font size=\"5\" color=\"red\" face=\"Arial\">Access denied! Залогинься! wrom response</font> </p>");


                //try(PrintWriter rw = wrapper.getWriter()) {
                    //rw.append("Access denied! from reswrapper");
               // }

                //HttpServletResponse res1 = org.apache.commons.lang3.ObjectUtils.clone(res);
                //HttpServletRequest req1 = org.apache.commons.lang3.ObjectUtils.clone(req);

                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").include(req,res);




                //res.sendRedirect("login");
                System.out.println("after AuthFilter Call doFilter and rd.include to login");
                //System.out.println(" Does req/res equals before and after rd.include : " + req1.equals(req) + "/" +res1.equals(res));//зацикливается рек-рес-рек-рес
                return;
            }else{
                User u = (User) s.getAttribute("user");
                if(!u.getAuthorities().contains(Role.ROLE_ADMIN) && req.getServletPath().equals("/users")){
                    System.out.print(req.getServletPath() + u.getAuthorities());
                      res.sendRedirect("/top/meals");

                        System.out.println("after AuthFilter Call doFilter and sendredirect to meals not admin");
                    return;
                }
            }
        }else{
            res.setContentType("text/html");
            PrintWriter rw = res.getWriter();
            rw.append("Access denied! Залогинься! wrom response");//а тут она и так норм ототбражалась Хм. НУ и похуй
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").include(req,res);
            // res.sendRedirect("login");

            System.out.println("after AuthFilter Call doFilter and sendredirect to login");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("after AuthFilter Call doFilter");

    }

    @Override
    public void destroy() {

    }
}
