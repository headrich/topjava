package ru.headrich.topjava.web.mvc.webflow;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.repository.JPA.UserRepositoryImpl;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.service.UserService;
import ru.headrich.topjava.service.UserServiceImpl;
import ru.headrich.topjava.util.JPAHandlerUtil;
import ru.headrich.topjava.util.converters.PasswordEncryption;
import ru.headrich.topjava.util.exception.NotFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created by Montana on 04.07.2016.
 */
@WebServlet(urlPatterns = {"/login","/logout","/logout/*","/login/*"})
public class LoginController extends HttpServlet {

    Logger LOG = LoggerFactory.getLogger(LoginController.class);
    //// TODO: 04.07.2016 macking with DI and IOC
    //there should not be repository. Service shoulde be there! Buisness logic! on web flow
    //UserRepository userRepository=new UserRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory());
    UserService userService = new UserServiceImpl(new UserRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory()));



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info(req.getServletPath());
        LOG.info(req.getRequestURI());
        String returnPage = req.getServletPath();
        if(req.getRequestURI().equals("/top/logout")){
            logoutProcessing(req,resp);
            return;
        }
        else if(req.getRequestURI().equals("/top/login/reg")){

            returnPage="/registrate.jsp";
        }

        else if(req.getRequestURI().equals("/top/login") && req.getParameter("act")!=null && req.getParameter("act").equals("reg")) {
            returnPage="/registrate.jsp";
            }else {
            returnPage="/WEB-INF/jsp/login.jsp";
        }

        req.getRequestDispatcher(returnPage).forward(req, resp);
        //super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String uri = req.getRequestURI();


            if(uri.equals("/top/login")){
                loginningProcess(req,resp);
            }
            if(uri.equals("/top/login/reg")){
                registrateProcess(req,resp);
            }
            if(uri.equals("/top/logout")){
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

                user.setPassword(password);
                HttpSession s = req.getSession(true);
                LOG.info("Retrived Session from request");
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
            rd = getServletContext().getRequestDispatcher("/index.jsp");rd.include(req,resp); //there is application path

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
        getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);


       // resp.setStatus(200);
        //String url = req.getRequestURL().toString();
        //resp.sendRedirect(url.substring(0,url.lastIndexOf("/logout"))+"/login"); //there is url  top/ req.getservletpath return /login but getrequri returns /top/login with 302 error
        //getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req,resp); no 302 error

    }

    protected void registrateProcess(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        //todo validate existing email dinamically after focusout input with ajax

        //валидация,

            if(userService.getByEmail(email)==null){
                User newUser = new User(username,email,password,Role.ROLE_USER);
                userService.save(newUser);

                //identif
                HttpSession s = req.getSession(true);
                s.setAttribute("isNew",true);
                s.setAttribute("user",newUser);
                s.setMaxInactiveInterval(30*60);
                Cookie loginCookie = new Cookie("user",username);
                loginCookie.setMaxAge(30*60);
                resp.addCookie(loginCookie);

                resp.sendRedirect("/top/profile");


            }else {
                resp.setContentType("text/html; charset=utf-8");
                PrintWriter rw = null;
                try {
                    rw = resp.getWriter();
                    rw.append("<p><font size=\"5\" color=\"red\" face=\"Arial\">Email "+email+" уже занят!</font> </p>");
                    req.getRequestDispatcher("/registrate.jsp").include(req,resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }





    }

}
