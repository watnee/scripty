<%@page contentType="text/html" pageEncoding="UTF-8"%>
<tr id="create-scene-row">
    <td colspan="2">
        <form hx-post="${pageContext.request.contextPath}/scene/createInline" hx-target="#create-scene-row" hx-swap="outerHTML">
            <input type="hidden" name="projectId" value="${projectId}" />
            <label>
                Scene Name
                <input type="text" spellcheck="true" name="name" autofocus />
            </label>
            <p>
                <button type="button" _="on click remove #create-scene-row then show #create-scene-btn">Cancel</button>
                <button type="submit">Create</button>
            </p>
        </form>
    </td>
</tr>
