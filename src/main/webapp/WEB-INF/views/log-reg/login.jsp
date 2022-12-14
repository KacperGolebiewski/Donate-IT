<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../header.jsp"/>
<section class="login-page">
    <h2><spring:message code="text.login"/></h2>
    <form method="post" action="/login">
        <div class="form-group">
            <spring:message code="text.email" var="placeholderEmail"/>
            <input type="email" name="email" placeholder='${placeholderEmail}'/>
        </div>
        <div class="form-group">
            <spring:message code="text.password" var="placeholderPassword"/>
            <input type="password" name="password" placeholder='${placeholderPassword}'/>
            <a href="<c:url value="/forgot-password"/>"
               class="btn btn--small btn--without-border reset-password"><spring:message
                    code="text.forgotPassword"/></a>
        </div>

        <div class="form-group form-group--buttons">
            <a href="<c:url value="/register"/>" class="btn btn--without-border"><spring:message
                    code="text.signUp"/></a>
            <button class="btn" type="submit"><spring:message code="text.login"/></button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </div>
    </form>
</section>
<jsp:include page="../footer.jsp"/>
