package ru.headrich.topjava.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Montana on 07.06.2016.
 */
public class HomeServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(HomeServlet.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().print("<h3> Hellodddddd, i am printer! </h3>");
            LOG.info("printing output");
            response.getWriter().close();
        } catch (IOException e) {
            LOG.error("OutputStrehhham error",e);
            e.printStackTrace();
        }

    }
}
