package ru.headrich.topjava.web.mvc.webflow.filters;


import antlr.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Montana on 06.07.2016.
 */
@WebFilter(filterName = "IsModelUpdatingFilter", description = "check all updating requests and hold it in state", urlPatterns = {"/*"})
public class IsModelUpdatingFilter implements Filter {

    private boolean modified; //poka tak
    static final Pattern uriPattern = Pattern.compile("^(\\/[A-Za-z0-9_]*)*(\\/(edit|update|remove|delete|save|add))(\\/([A-Za-z0-9_]*))?$");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        //HttpServletResponse resp = (HttpServletResponse) servletResponse;
        System.out.println(" InModelUpdateFilter  req "+ req.getServletPath()  + "   url: " + req.getRequestURL());
        Matcher m = uriPattern.matcher(req.getRequestURI());

        filterChain.doFilter(servletRequest,servletResponse);
        //if(((HttpServletResponse) servletResponse).getStatus()!=200){
            //servletResponse.getWriter().append("Somthing went wrong.");
          //  servletRequest.getRequestDispatcher("/error.jsp").include(servletRequest,servletResponse);
            //return;
       // }else {
            if (m.matches()) {

                if (req.getMethod().equals("POST")) modified = true;
                System.out.println("the data was modified");
                //return;
            }
        System.out.println("after IsNodulateFilter Call doFilter");
       // }




    }

    @Override
    public void destroy() {

    }
}
