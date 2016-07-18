<%@ page import="ru.headrich.topjava.model.User" %>
<%@ page import="ru.headrich.topjava.model.Role" %><%--
  Created by IntelliJ IDEA.
  User: Montana
  Date: 08.06.2016
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
          integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
            integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <script type="text/javascript" src="resources/js/ajaxic.js"></script>
    <title>Meals</title>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Brand</a>
        </div>
        <div class="col-md-1"><p class="navbar-text">Welcome ${sessionScope['user'].name}</p></div>
        <form class="navbar-form navbar-right" >
            <%
                User u =(User) session.getAttribute("user");
                if(u.getAuthorities().contains(Role.ROLE_ADMIN))
                {%>
            <a class="btn btn-default" href="users" role="button">users</a>
            <% } %>
            <%
                if(u.getAuthorities().contains(Role.ROLE_USER) || u.getAuthorities().contains(Role.ROLE_ADMIN) )
                {%>
            <a class="btn btn-default" href="profile" role="button">${sessionScope['user'].name}</a>
            <a class="btn btn-default" href="logout" role="button">logout</a>
            <% }
            if(u==null || u.getAuthorities().contains(Role.ROLE_GUEST))
            {%>
            <a class="btn btn-default" href="login" role="button">Sign In</a>
            <a class="btn btn-default" href="login/reg" role="button">Registrate!</a>
            <% } %>

        </form>

    </div>
</nav>
<div class="jumbotron">
    <div class="container">
        <p> There are list of meals</p>
        <div class="btn-group">
            <p> Filter </p>
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                AllMeals <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li><a href="#" class="editRow">AllMeals</a></li>
                <%if(u!=null && !u.getAuthorities().contains(Role.ROLE_GUEST))
                //((User) req.getSession().getAttribute("user")).getMeals();
                {%>
                <li><a href="#" class="removeRow">MyMeals</a></li>
                <% }%>

            </ul>
        </div>
        <table class="table">
            <tr>
                <td><b>Date</b></td>
                <td><b>Description</b></td>
                <td><b>Calories</b></td>
                <td></td>
            </tr>
            <c:forEach items="${requestScope['meals']}" var="m">

                <tr>
                    <td>${m['date']}</td>
                    <td>${m['description']}</td>
                    <td>${m['calories']}</td>
                    <%if(u!=null && !u.getAuthorities().contains(Role.ROLE_GUEST))
                    {%>
                    <td><!-- Single button  -->
                        <div class="btn-group">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                edit <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="#" class="editRow">edit</a></li>
                                <li><a href="#" class="removeRow">remove</a></li>

                            </ul>
                        </div>
                    </td>
                    <% } %>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<div class="modal fade" id="editRowModal" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Modal title</h4>
            </div>
            <div class="modal-body">
                <p>One fine body&hellip;</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
</body>
</html>
