package ru.headrich.topjava.web.mvc.webflow.listeners;

import org.hibernate.Session;
import ru.headrich.topjava.util.JPAHandlerUtil;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Montana on 12.07.2016.
 */
public class RequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpSession s = (HttpSession) ((HttpServletRequest)servletRequestEvent.getServletRequest()).getSession(false);
        if(s.getAttribute("user")!=null){


        }else{
            Session hs = JPAHandlerUtil.buildSessionFactory().openSession();

        }
    }
}
