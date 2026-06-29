<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<header>
    <nav>
        <a href="${pageContext.request.contextPath}/"><strong>Scripty</strong></a>
        <c:if test="${pageContext.request.userPrincipal != null}">
            <a href="${pageContext.request.contextPath}/project/list" ${param.page == 'projects' ? 'aria-current="page"' : ''}>Projects</a>
            <a href="${pageContext.request.contextPath}/actor/list" ${param.page == 'casting' ? 'aria-current="page"' : ''}>Casting</a>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <a href="${pageContext.request.contextPath}/account/list" ${param.page == 'accounts' ? 'aria-current="page"' : ''}>Accounts</a>
            </sec:authorize>
        </c:if>
    </nav>
</header>