package ru.headrich.topjava.web.mvc.webflow.listeners;

/**
 * Created by Montana on 10.07.2016.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.headrich.topjava.util.JPAHandlerUtil;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * creating instance of SessioNFactory  when context initiating
 */
@WebListener
public class SessionFactoryContextListener implements ServletContextListener{
Logger LOG = LoggerFactory.getLogger(SessionFactoryContextListener.class);
    //hibernate.current_session_context_class=thread реализует threadlocal session pattern в котором на каждый поток порождается своя сессия, и дает гарантию?
    // что каждый поток оплучит свой экземпляр сессии

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("try to initate spring context");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");


        LOG.info("Creating EntityManagerFactory");
        EntityManagerFactory entityManagerFactory = JPAHandlerUtil.buildEntityManagerFactory();
        servletContextEvent.getServletContext().setAttribute("emFactory",entityManagerFactory);
        LOG.info("Binding  EntityManagerFactory as ServletContextAttribute ");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        EntityManagerFactory entityManagerFactory= JPAHandlerUtil.buildEntityManagerFactory();
        if(entityManagerFactory!=null && entityManagerFactory.isOpen()){
            entityManagerFactory.close();
            servletContextEvent.getServletContext().removeAttribute("emFactory");
            LOG.info("Release EntityManagerFactory");
        }
    }
}
