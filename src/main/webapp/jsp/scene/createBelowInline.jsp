<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="create-scene-below-form">
    <form hx-post="${pageContext.request.contextPath}/scene/createBelowInline" hx-target="#create-scene-below-form" hx-swap="outerHTML">
        <input type="hidden" name="id" value="${sceneId}" />
        <label>
            Scene Name
            <input type="text" spellcheck="true" name="name" autofocus />
        </label>
        <p>
            <button type="button" _="on click remove #create-scene-below-form then show #create-scene-btn">Cancel</button>
            <button type="submit">Create</button>
        </p>
    </form>
</div>
