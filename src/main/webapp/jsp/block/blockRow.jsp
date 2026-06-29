<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<tr draggable="true" data-block-id="${block.id}" data-block-order="${block.order}">
    <td class="drag-handle" title="Drag to reorder">&#8942;&#8942;</td>
    <td class="block-content" hx-get="${pageContext.request.contextPath}/block/editInline?id=${block.id}" hx-trigger="click[!event.target.closest('a')&&!event.target.closest('form')]" hx-swap="innerHTML">
        <c:if test="${not empty block.personName}">
            <p style="margin-bottom: 0; text-align: center">
                <a href="${pageContext.request.contextPath}/character/show?id=${block.personId}" class="character-name" style="text-transform: uppercase">${block.personName}</a>
            </p>
        </c:if>
    </td>
    <td>
        <span class="nowrap">
            <a href="${pageContext.request.contextPath}/block/edit?id=${block.id}" role="button">edit</a>
            <a href="${pageContext.request.contextPath}/block/delete?id=${block.id}" role="button" _="on click if not confirm('Are you sure you want to delete this block?') halt">delete</a>
            <a href="${pageContext.request.contextPath}/block/moveDown?id=${block.id}" class="move-down" role="button">&#8595;</a>
            <a href="${pageContext.request.contextPath}/block/moveUp?id=${block.id}" class="move-up" role="button">&#8593;</a>
            <a hx-get="${pageContext.request.contextPath}/block/createBelowInline?id=${block.id}" hx-target="closest tr" hx-swap="afterend" class="create-below" role="button">+ block</a>
        </span>
    </td>
</tr>
