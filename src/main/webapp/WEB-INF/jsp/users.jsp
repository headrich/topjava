<%--
  Created by IntelliJ IDEA.
  User: Montana
  Date: 08.06.2016
  Time: 16:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
              integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <script type="text/javascript" src="resources/js/ajaxic.js"></script>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <title>Users</title>



</head>
<body>
<%

%>
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
            <a class="btn btn-default" href="meals" role="button">meals</a>
            <a class="btn btn-default" href="profile" role="button">${sessionScope['user'].name}</a>
        </form>
        <form class="navbar-form navbar-right" action="logout" method="post">
            <button type="submit" class="btn btn-default">logout</button></form>

    </div>
</nav>
<div class="jumbotron">
    <div class="container">
        <p> There are list of meals</p>
        <p> There are list of users</p>
        <table class="table">
            <c:forEach items="${requestScope['userlist']}" var="u">
                <tr>
                    <td>${u.name}</td>
                    <td>${u.email}</td>
                    <td>${u.registered}</td>
                    <td><!-- Single button -->
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
