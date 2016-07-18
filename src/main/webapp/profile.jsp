<%@ page import="ru.headrich.topjava.model.User" %>
<%@ page import="ru.headrich.topjava.model.Role" %><%--
  Created by IntelliJ IDEA.
  User: Montana
  Date: 11.07.2016
  Time: 11:34
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
    <title>Profile</title>
    <%
        User u  = (User) session.getAttribute("user");

    %>
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
            <a class="navbar-brand" href="meals">Calories!!!</a>
        </div>

        <div class="navbar-left"><p class="navbar-text">
           ${sessionScope['isNew']!=null ? "Congratulation" : "Welcome back" }
             <%=u.getName()%>
        </p></div>

        <form class="navbar-form navbar-right">
            <%if(u.getAuthorities().contains(Role.ROLE_ADMIN)) {%>
            <a class="btn btn-default" href="users" role="button">users</a>
            <% }%>

            <a class="btn btn-default" href="/top/meals?u=<%=u.getId() %>" role="button">my meals</a>
            <a class="btn btn-default" href="/top/logout" role="button">logout</a>

        </form>

    </div>
</nav>
<div class="jumbotron">
    <div class="container">
        <form class="form-horizontal" method="post" action="/top/profile/edit">
            <div class="form-group">
                <label for="InputName" class="col-sm-2 control-label">Name</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="inputName" name="username" value="<%=u.getName()%>">
                </div>
            </div>
            <div class="form-group">
                <label for="inputEmail" class="col-sm-2 control-label">Email</label>
                <div class="col-sm-10">
                    <input type="email" class="form-control" id="inputEmail" name="email" value="<%=u.getEmail()%>">
                </div>
            </div>
            <div class="form-group">
                <label for="inputRegistered" class="col-sm-2 control-label">Registered</label>
                <div  class="col-sm-10">
                    <input type="datetime" disabled class="form-control" id="inputRegistered" value="<%=u.getRegistered().toString()%>" >
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">Edit</button>
                </div>
            </div>
        </form>
    </div>
</div>


</body>
</html>
