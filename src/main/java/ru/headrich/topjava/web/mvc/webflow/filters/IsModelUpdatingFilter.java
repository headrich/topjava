package ru.headrich.topjava.web.mvc.webflow.filters;

import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Montana on 06.07.2016.
 */
@WebFilter(description = "check all updating requests and hold it in state", urlPatterns = {"*edit*","*remove*","*update*","*save*"})
public class IsModelUpdatingFilter implements Filter {

    private boolean modified; //poka tak

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpRequest req = (HttpRequest) servletRequest;
        HttpResponse resp = (HttpResponse) servletResponse;

        modified = true;

    }

    @Override
    public void destroy() {

    }
}
