package ru.headrich.topjava.web.mvc.webflow.listeners;

import org.hibernate.Session;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.headrich.topjava.util.JPAHandlerUtil;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Montana on 10.07.2016.
 */
//todo close  hibernate session or jpa entitymanager when httpssession destroyed

    @WebListener
public class EntityManagerListener implements HttpSessionListener {
Logger LOG = LoggerFactory.getLogger(EntityManagerListener.class);




    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        LOG.info("Session created");
        //либо всем раздавать доступ к базе, либо только крутым зарегавшимся типам.
        //стоит еще понимать тот факт , что если у нас будет ридонли юзеры, то им нахер не нужен ентитиманагер, для них можно загрузить данные,
        // вручную проинициализировать все коллекции,(initialize) и пусть работает с детачами.
        // для других можно сделать и  ем пер реквест, однако, тогда дао меняется под работу с детачем, и нужно переподсоединять к контексту объекты.
        //потому лучше сделать для них сессию на сессию.

        Session s = JPAHandlerUtil.buildSessionFactory().openSession();
        httpSessionEvent.getSession().setAttribute("session",s);
        //теперь надо эту чеччию нам запихнуть куда нажо, а именно в репозиторий
        //или  в реквестфильтре-листенере, чекать хттпсессию притом оставить связывание сессии с потоком хибернейту
        //или самому связывать сессию с потоками но нахер надо? или попробовать все же тред локалом и вручную велосипедить?
        //правда непонятно как же это мы будем открывать сессию с бесконечным потоком запросов.
        //впрочем есть свои плюсы, ведь сессия не будет создаваться для всех сразу. обернеем ее в тредлокал и сами будем решать кому ее выдвать.
        //только вот тред этот связан с запросом,и закрываясь он заберет с собой сессию, а нам это не нужно..все сделаеи!

        //!!!! тогда, если мы оторвемся от привязки ск потоку запроса, т.е асинхронной задачей в сервлете, мы можем управлят ьсессией хибера, в не зависимости от потока
        // обработки запроса. или же? в общем нам нельзя хранить сессию в хттпсессии не обезаписив ее от мультипоточного доступа,
        // или нам нужно ее связать с другим потоком. Runnable = new rannable{ threadlocal session}
        // Впрочем сессия живет определенное время,указанное время, без дейсттвия, при этом если оно большое, то у нас памяти не хватит держать столько сессий. Их лучше создавать и уничтожать постоянно.
        // а еще лучше создавать сессию, и контрить ее по запросу
        //todo стандартный opensessioninview






    }
//todo заменить потом фильтры и листенеры на интерсепторы JavaEE и AOP( например на вызов метода сервиса-репозитория и тд)
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

       /* EntityManager em = (EntityManager) httpSessionEvent.getSession().getAttribute("entityManager");
        if(em!=null && em.isOpen()){
            em.clear();
            em.close();*/
        Session s = (Session) httpSessionEvent.getSession().getAttribute("session");
        s = ThreadLocalSessionContext.unbind(s.getSessionFactory());
        if(s!=null && s.isOpen()){
            s.clear();
            s.close();

            LOG.info("CLose and Release EntityManager from HttpSession with id" + httpSessionEvent.getSession().getId());
        }
        LOG.info("Session destroyed");

    }
}
