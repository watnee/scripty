<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Scripty - Edit Account</title>
        <link rel="stylesheet" href="https://unpkg.com/missing.css@1.1.3/dist/missing.min.css">
        <link href="${pageContext.request.contextPath}/css/martinis.css" rel="stylesheet">
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    </head>
    <body hx-boost="true">
        <jsp:include page="../includes/nav.jsp" />
        <main>
            <jsp:include page="../includes/logout.jsp" />
            <h1>Edit Account</h1>
            <sf:form action="${pageContext.request.contextPath}/account/edit" method="post" modelAttribute="commandModel">
                <sf:hidden path="id" />
                <label>
                    Username
                    <sf:input type="text" path="username" />
                    <sf:errors path="username" />
                </label>
                <label>
                    Password <small>(leave blank to keep current)</small>
                    <sf:input type="password" path="password" />
                    <sf:errors path="password" />
                </label>
                <label>
                    First Name
                    <sf:input type="text" path="firstName" />
                    <sf:errors path="firstName" />
                </label>
                <label>
                    Last Name
                    <sf:input type="text" path="lastName" />
                    <sf:errors path="lastName" />
                </label>
                <label>
                    <sf:checkbox path="admin" />
                    Admin
                </label>
                <p>
                    <a href="${pageContext.request.contextPath}/account/list" role="button">Cancel</a>
                    <button type="submit">Submit</button>
                </p>
            </sf:form>
        </main>
        <script src="https://unpkg.com/htmx.org@2.0.4"></script>
        <script src="${pageContext.request.contextPath}/js/_hyperscript.min.js"></script>
    </body>
</html>
