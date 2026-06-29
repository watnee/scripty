<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Scripty - Accounts</title>
        <link rel="stylesheet" href="https://unpkg.com/missing.css@1.1.3/dist/missing.min.css">
        <link href="${pageContext.request.contextPath}/css/martinis.css" rel="stylesheet">
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    </head>
    <body hx-boost="true">
        <jsp:include page="../includes/nav.jsp">
            <jsp:param name="page" value="accounts" />
        </jsp:include>
        <main>
            <jsp:include page="../includes/logout.jsp" />
            <nav aria-label="Breadcrumb">
                <ol>
                    <li aria-current="page">Accounts</li>
                </ol>
            </nav>
            <h1>Accounts</h1>
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Name</th>
                        <th>Role</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${viewModel.users}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.firstName} ${user.lastName}</td>
                            <td>${user.admin ? 'Admin' : 'User'}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/account/edit?id=${user.id}" role="button">edit</a>
                                <a href="${pageContext.request.contextPath}/account/delete?id=${user.id}" role="button" _="on click if not confirm('Are you sure you want to delete this account?') halt">delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <p><a href="${pageContext.request.contextPath}/account/create" role="button">Create New Account</a></p>
        </main>
        <script src="https://unpkg.com/htmx.org@2.0.4"></script>
        <script src="${pageContext.request.contextPath}/js/_hyperscript.min.js"></script>
    </body>
</html>
