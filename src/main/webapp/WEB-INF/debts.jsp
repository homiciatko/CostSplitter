<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>

<c:url value="/books/delete" var="deleteBookURL" />
<c:url value="/books/edit" var="editBookURL" />
<c:url value="/pay/debt" var="PayURL" />


<c:if test="${cantPay}">
    <div class="alert alert-danger">
        allready paid
    </div>
</c:if>
<%--<c:if test="${user.id == null}" >--%>
    <%--<h1>Create user</h1>--%>
<%--</c:if>--%>

<%--<c:if test="${user.id != null}" >--%>
    <%--<h1>Edit user</h1>--%>
<%--</c:if>--%>

<div class="container">

    <h1>List of Debts</h1>

    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped table-hover table-bordered">
                <thead>
                <tr>
                    <th class="text-center col-md-1">Date</th>
                    <th class="text-center">Description</th>
                    <th class="text-center">Tag</th>
                    <th class="text-center">Paid(no refunds included)</th>
                    <th class="text-center">Status</th>
                    <sec:authorize access="hasRole('ADMIN')">
                        <th class="text-center">Edit</th>
                        <th class="text-center">Delete</th>
                    </sec:authorize>
                    <th class="text-center">Pay</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="#{dealList}" var="deal">
                <c:forEach items="${deal.debts}" var="debt">
                    <tr>
                        <td>${deal.createDateTime}</td>
                        <td>${deal.description}</td>
                        <td>${debt.tag.display()}</td>
                        <td>${debt.money}</td>
                        <c:choose>
                            <c:when test="${debt.isPaid()}" >
                                <td>Not Paid</td>
                                <td><a href="${PayURL}/${debt.id}" class="btn btn-info btn-sm">Pay</a></td>
                            </c:when>
                            <c:otherwise>
                                <td>Paid</td>
                            </c:otherwise>
                        </c:choose>

                        <%--<td>Opis</td>--%>
                        <%--<td>${debt.money}</td>--%>
                        <%----%>

                        <%--<sec:authorize access="hasRole('ADMIN')">--%>
                            <%--<td> <a href="${editDebtURL}/${debt.id}" class="btn btn-primary edit-btn">Edit</a> </td>--%>
                            <%--<td> <a href="${deleteDebtURL}/${debt.id}" class="btn btn-danger delete-btn">Delete</a> </td>--%>
                        <%--</sec:authorize>--%>
                        <%--<td class="text-center">--%>
                            <%--<c:choose>--%>
                                <%--<c:when test="${!debt.paid}">--%>
                                    <%--<a href="${PayURL}/${debt.id}" class="btn btn-info btn-sm">Pay</a>--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                    <%--????k???--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>
                        <%--</td>--%>
                    </tr>
                </c:forEach>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp" %>