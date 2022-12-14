<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../header-admin.jsp"/>
<div class="body flex-grow-1 px-3 py-5" style="font-size: 1.5rem;!important;">
    <div class="container py-5">
        <c:if test="${totalPages != 1}">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link"
                               href="<c:url value="/admin/users/${currentPage-1}?sortField=${sortField}&sortDir=${sortDir}"/>"><spring:message
                                    code="text.previous"/></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item disabled">
                            <a class="page-link"><spring:message code="text.previous"/></a>
                        </li>
                    </c:otherwise>
                </c:choose>
                <c:forEach begin="1" end="${totalPages}" varStatus="status">
                    <c:choose>
                        <c:when test="${currentPage != status.index}">
                            <li class="page-item">
                                <a class="page-link"
                                   href="<c:url value="/admin/users/${status.index}?sortField=${sortField}&sortDir=${sortDir}"/>">${status.index}</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item disabled"><a class="page-link">${status.index}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link"
                               href="<c:url value="/admin/users/${currentPage+1}?sortField=${sortField}&sortDir=${sortDir}"/>"><spring:message
                                    code="text.next"/></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item disabled">
                            <a class="page-link"><spring:message code="text.next"/></a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
        </c:if>
        <div class="card mb-4">
            <div class="card-header px-4 py-3 custom-display">
                <span><strong><spring:message code="text.manageUsers"/></strong></span>
                <span><a style="font-size: 1.5rem;!important;" class="btn btn-outline-success px-3 py-2"
                         href="<c:url value="/admin/users/add"/>"
                         role="button"><i class="fa fa-plus px-2"></i><spring:message code="text.add"/></a></span>
            </div>
            <div class="card-body">
                <div class="tab-content rounded-bottom">
                    <div class="tab-pane p-3 active preview" role="tabpanel" id="preview-1055">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th scope="col">
                                    <a href="<c:url value="/admin/users/${currentPage}?sortField=firstName&sortDir=${reverseSortDir}"/>"><i
                                            class="fa fa-fw fa-sort"></i></a>
                                    <span><spring:message code="text.firstName"/></span>
                                <th scope="col">
                                    <a href="<c:url value="/admin/users/${currentPage}?sortField=lastName&sortDir=${reverseSortDir}"/>"><i
                                            class="fa fa-fw fa-sort"></i></a>
                                    <span><spring:message code="text.lastName"/></span>
                                <th scope="col">
                                    <a href="<c:url value="/admin/users/${currentPage}?sortField=email&sortDir=${reverseSortDir}"/>"><i
                                            class="fa fa-fw fa-sort"></i></a>
                                    <span><spring:message code="text.email"/></span>
                                </th>
                                <th scope="col"><spring:message code="text.enabled"/></th>
                                <th scope="col"><spring:message code="text.blocked"/></th>
                                <th scope="col">
                                    <div align="middle">
                                        <span><spring:message code="text.actions"/></span>
                                    </div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${users}" var="user">
                                <tr>
                                    <td>${user.firstName}</td>
                                    <td>${user.lastName}</td>
                                    <td>${user.email}</td>
                                    <td>${user.enabled}</td>
                                    <td>${user.locked}</td>
                                    <td align="right">
                                        <a style="font-size: 1.5rem;!important;"
                                           class="btn btn-link text-danger text-gradient px-3 mb-0 py-2"
                                           href="<c:url value="/admin/users/confirm-delete/${user.id}"/>"><i
                                                class="align-middle fa fa-trash px-2 text-sm me-2"></i><spring:message
                                                code="text.delete"/></a>
                                        <a style="font-size: 1.5rem;!important;"
                                           class="btn btn-link text-dark px-3 mb-0 py-2"
                                           href="<c:url value="/admin/users/edit/${user.id}"/>"><i
                                                class="align-middle fa fa-edit px-2 text-sm me-2"></i><spring:message
                                                code="text.edit"/></a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${totalPages != 1}">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link"
                               href="<c:url value="/admin/users/${currentPage-1}?sortField=${sortField}&sortDir=${sortDir}"/>"><spring:message
                                    code="text.previous"/></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item disabled">
                            <a class="page-link"><spring:message code="text.previous"/></a>
                        </li>
                    </c:otherwise>
                </c:choose>
                <c:forEach begin="1" end="${totalPages}" varStatus="status">
                    <c:choose>
                        <c:when test="${currentPage != status.index}">
                            <li class="page-item">
                                <a class="page-link"
                                   href="<c:url value="/admin/users/${status.index}?sortField=${sortField}&sortDir=${sortDir}"/>">${status.index}</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item disabled"><a class="page-link">${status.index}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link"
                               href="<c:url value="/admin/users/${currentPage+1}?sortField=${sortField}&sortDir=${sortDir}"/>"><spring:message
                                    code="text.next"/></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item disabled">
                            <a class="page-link"><spring:message code="text.next"/></a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
        </c:if>
<jsp:include page="../footer-admin.jsp"/>