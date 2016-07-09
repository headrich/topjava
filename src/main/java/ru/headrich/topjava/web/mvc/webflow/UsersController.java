package ru.headrich.topjava.web.mvc.webflow;


import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.repository.JPA.UserRepositoryImpl;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.service.UserService;
import ru.headrich.topjava.service.UserServiceImpl;
import ru.headrich.topjava.util.JPAHandlerUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Montana on 04.07.2016.
 */
@WebServlet(name = "UsersController" , urlPatterns = {"/users","/users/*"})
public class UsersController extends HttpServlet {

    UserService us = new UserServiceImpl(new UserRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory()));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*HttpSession session = req.getSession(false);
        if(session.getAttribute("userList")==null ){//but how to update? - hibernate interceptors!(JPA eventListeners) - но как пробросить изменения -никак
            // or javaInterceptors to service or repository flow
            // or Servlet filters on edit/update urls
            //and // TODO: 06.07.2016  Если я хочу хранит в сесси эти списки , то если будут много пользователей - это сожрет всю память.
            // - можно сделать кеширование  и хранить все так, но зачем если все и так в бд хранится. Потому нужно хранить такие данные как списки, не
            // в персональной для каждого клиента шттпсессии, а  одним объектом на всех. только вот выдават ьим копии, а при изменениях, аттачить.
            //http://programmers.stackexchange.com/questions/214772/storing-large-data-in-http-session-java-application
            //https://gopalakrishnadurga.wordpress.com/2012/06/08/filter-vs-interceptor/

            session.setAttribute("userList",us.getAllUsers());
        }*/

        req.setAttribute("userlist",us.getAllUsers());
        req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").include(req,resp);

        //super.doGet(req, resp);
    }

}
