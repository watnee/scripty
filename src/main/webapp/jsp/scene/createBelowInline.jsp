<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="create-scene-below-form">
    <form hx-post="${pageContext.request.contextPath}/scene/createBelowInline" hx-target="#create-scene-below-form" hx-swap="outerHTML" hx-trigger="keyup[key=='Enter'] from:find input[name='name'], blur[target.value.trim()!=''] from:find input[name='name']">
        <input type="hidden" name="id" value="${sceneId}" />
        <input type="text" spellcheck="true" name="name" autofocus placeholder="Scene name" _="on blur if my value.trim() is '' remove #create-scene-below-form then show #create-scene-btn" />
    </form>
</div>
