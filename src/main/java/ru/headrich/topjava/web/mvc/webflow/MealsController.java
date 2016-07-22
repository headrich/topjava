package ru.headrich.topjava.web.mvc.webflow;

import org.springframework.beans.factory.annotation.Autowired;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.repository.JPA.UserMealRepositoryImpl;
import ru.headrich.topjava.service.UserMealService;
import ru.headrich.topjava.service.UserMealServiceImpl;
import ru.headrich.topjava.util.JPAHandlerUtil;
import ru.headrich.topjava.util.MyPrintWriterWrapper;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Montana on 04.07.2016.
 */
@WebServlet(name = "MealsController", urlPatterns = {"/meals","/meals/*"})
public class MealsController extends HttpServlet {
    @Autowired
    UserMealService ums;
    //= new UserMealServiceImpl(new UserMealRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory()));
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getDispatcherType().equals(DispatcherType.REQUEST)){
            if(!new MyPrintWriterWrapper(resp.getWriter()).isClosed()){
                //resp.getWriter().close();
            }
        }

        if(req.getSession().getAttribute("user")==null){
            User guest = new User("Guest","","", Role.ROLE_GUEST);
            req.getSession().setAttribute("user",guest);
        }
        req.setAttribute("meals",ums.getAllMeals());

        req.getRequestDispatcher("/meals.jsp").include(req,resp);
        System.out.println("after include rdispatcher");
        //super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        super.doPost(req, resp);
    }
}
