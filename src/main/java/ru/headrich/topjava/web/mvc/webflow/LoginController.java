package ru.headrich.topjava.web.mvc.webflow;

import org.hibernate.Session;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.repository.JPA.UserRepositoryImpl;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.service.UserService;
import ru.headrich.topjava.service.UserServiceImpl;
import ru.headrich.topjava.util.JPAHandlerUtil;
import ru.headrich.topjava.util.converters.PasswordEncryption;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Montana on 04.07.2016.
 */
@WebServlet(urlPatterns = {"/login","/logout","/logout/*","/login/*"})
public class LoginController extends HttpServlet {
    //// TODO: 04.07.2016 macking with DI and IOC
    //there should not be repository. Service shoulde be there! Buisness logic! on web flow
    //UserRepository userRepository=new UserRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory());
    UserService userService = new UserServiceImpl(new UserRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory()));



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getServletPath().equals("/logout")){
            logoutProcessing(req,resp);
        }else {
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
        //super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String uri = req.getServletPath();

            if(uri.equals("/login")){
                loginningProcess(req,resp);
            }
            if(uri.equals("/logout")){
                logoutProcessing(req,resp);
            }

        //super.doPost(req, resp);
    }


    protected void loginningProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        RequestDispatcher rd = null;
        User user = userService.getByName(username);

        if(user!=null){
            boolean authentificated = false;
            try {
                authentificated= userService.authentificateUser(user,password);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.print(e.getMessage());
            }
            if(authentificated){


                HttpSession s = req.getSession(true);
                s.setAttribute("user",user);
                s.setMaxInactiveInterval(30*60);
                Cookie loginCookie = new Cookie("user",username);
                loginCookie.setMaxAge(30*60);
                resp.addCookie(loginCookie);

                //req.setAttribute("user",user);
                if(user.getAuthorities().contains(Role.ROLE_ADMIN)){
                    //фичи админа -список юзеров
                    //s.setAttribute("users",userService.getAllUsers()); //LAZY!
                }
                s.setAttribute("meals", user.getMeals());
                resp.sendRedirect("meals");
                System.out.println("after SendRedirect to meals");

            }else {
                resp.getWriter().append("<p><font size=\"5\" color=\"red\" face=\"Arial\"> Login faild, invalid username or password. Try again. </font> </p>");
                //rd = getServletContext().getRequestDispatcher("/error.jsp");
                rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp");rd.include(req,resp);
            }
        }else{
            resp.getWriter().append("<p><font size=\"5\" color=\"red\" face=\"Arial\"> Not found user with username : "+username+" </font></p>");
            rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp");rd.include(req,resp); //there is application path

        }
    }

    protected void logoutProcessing(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Cookie[] reqCookies = req.getCookies();

        Cookie loginCookie = Arrays.asList(reqCookies).stream().filter(c->c.getName().equals("user")).findFirst().orElse(null);
        if(loginCookie!=null){
            loginCookie.setMaxAge(0);
            resp.addCookie(loginCookie);
        }
        //clear session
        HttpSession curSession = req.getSession(false);
        if(curSession!=null) curSession.invalidate();

        //resp.getWriter().append(" Good bye ");
        //resp.sendRedirect("login");
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req,resp);


       // resp.setStatus(200);
        //String url = req.getRequestURL().toString();
        //resp.sendRedirect(url.substring(0,url.lastIndexOf("/logout"))+"/login"); //there is url  top/ req.getservletpath return /login but getrequri returns /top/login with 302 error
        //getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req,resp); no 302 error

    }

}
