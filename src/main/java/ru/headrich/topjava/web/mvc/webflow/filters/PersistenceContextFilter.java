package ru.headrich.topjava.web.mvc.webflow.filters;


import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.FlushModeType;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.hibernate.context.spi.AbstractCurrentSessionContext;
import org.hibernate.context.spi.CurrentSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.headrich.topjava.util.JPAHandlerUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Montana on 12.07.2016.
 */


//WebFilter(mapping - all servlets using repository)
public class PersistenceContextFilter implements Filter{
Logger LOG = LoggerFactory.getLogger(PersistenceContextFilter.class);
    private static final  String END_OF_CONVERSION_FLAG = "end_of_conversion";
    private static final String Session="persistenceContext";
    SessionFactory sf;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         sf = JPAHandlerUtil.buildSessionFactory();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Session s = null;

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession httpSessionContext = req.getSession(false);
        Boolean eocf = false;

        if (httpSessionContext != null) {

            if(!req.getHeader("Referer").contains(req.getServletPath())){

               // httpSessionContext.setAttribute(END_OF_CONVERSION_FLAG,true);
                eocf = true;

            }

//todo end of conversion устанавилвается при преходах с одного сервлета на другой. Организовать перехват вызова forward, include, sendredirect, servlet.service, dofilter
            //или что еще, проверять значения req или сохранять в сессии предидущий запрос, он возможно там и есть. вот именно это уже сделано. меньше ввелосипедов.
            //написать то ты их сможешь, но надо ли?) в ленивом, обессиленном состоянии прогается как-то уверенеей. ибо похуй становится на перфекционизм, и хочется все быстрее и проще сделать.
            //я почти готов ! Осталось только поднабраться знаинй немножкечко еще. и подготовиться по впорсоам, как к экзаменам.
            if (httpSessionContext.getAttribute(END_OF_CONVERSION_FLAG) != null || eocf) {
                httpSessionContext.removeAttribute(Session);
                s = sf.openSession();
            } else {
                //s = sf.getCurrentSession();
                s = (Session) httpSessionContext.getAttribute(Session);
                if (s == null) {
                    s = sf.openSession();
                    httpSessionContext.setAttribute(Session, s);

                }

            }
            if (s != null) {
                s.setHibernateFlushMode(FlushMode.MANUAL);
            }
            ThreadLocalSessionContext.bind(s);
            Transaction tx;
            tx = s.beginTransaction(); // SPring @Transactional избавляет от написания этого кода -открытия и закрытия транзакции.

            //ну или auto_commit = true
            // ну или использование entity manager который сам  в рамках одной транзакции открывается
            try {
                filterChain.doFilter(servletRequest, servletResponse);

                //now we have one session for one user httpsession / and one transaction for request/
                // значит мы можем баоловаться с нашими managed объектами, и все будет хорошо так все в рамках одной сессии.
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                LOG.info("Something went wrong. Rollback transaction");
            } finally {

                if (httpSessionContext.getAttribute(END_OF_CONVERSION_FLAG) != null || eocf)
                    httpSessionContext.removeAttribute(Session);
            }


            //нужно зарыть сессию теперь - либо в листенере, и взять ее из httpsession , либо как то еще?
            // нужно закрыть ее когда? вовремя! например отработав с милсами, или отработав с юзерами, т.е на уровне сервлета, на уровне окнтроллера а не запроса,
            // как логический блок работы с бд- контекст, http-транзакция.
            //значит мы будем открывать и закрывать сессию при переходе из одного Servletpath в другой
            //как закрыть сессию , если пользователь ушел нахер от компа.у нас соединение с базой открыто. или не открыто? настройки может етсь какие?


        }
    }

    @Override
    public void destroy() {

    }
}
