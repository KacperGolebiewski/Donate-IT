<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<footer id="contact" class="admin">
    <div class="contact">

    </div>
    <div class="bottom-line">
        <span class="bottom-line--copy">Copyright &copy; 2018</span>
        <div class="bottom-line--icons">
            <a href="#" class="btn btn--small"><img src="<c:url value="${pageContext.request.contextPath}/resources/images/icon-facebook.svg"/>"/></a> <a href="#"
                                                                                                                                                          class="btn btn--small"><img
                src="<c:url value="${pageContext.request.contextPath}/resources/images/icon-instagram.svg"/>"/></a>
        </div>
    </div>
</footer>

<script src="<c:url value="${pageContext.request.contextPath}/resources/js/app.js"/>"></script>
</body>
</html>