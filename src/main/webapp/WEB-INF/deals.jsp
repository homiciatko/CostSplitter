<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>

<c:url value="/deals/delete" var="deletedealURL" />
<c:url value="/deals/detail" var="dealdetailsURL" />
<c:url value="/debt/deal" var="debtURL" />


<c:if test="${cantdebt}">
    <div class="alert alert-danger">
        Brak dostepu, przykro nam :(
    </div>
</c:if>

<div class="container">

    <h1>My deals</h1>

    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped table-hover table-bordered">
                <thead>
                <tr>
                    <th class="text-center col-md-1">Debts</th>
                    <th class="text-center">Description</th>
                    <th class="text-center">Deal Date</th>
                    <th class="text-center">Tags</th>
                    <%--<sec:authorize access="hasRole('ADMIN')">--%>
                        <th class="text-center">Details</th>
                        <th class="text-center">Delete</th>
                    <%--</sec:authorize>--%>
                    <th class="text-center">Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${dealList}" var="deal">
                    <tr>
                        <td>
                            <c:forEach items="${deal.debts}" var="debt">
                                <p>${debt.display()}</p>
                            </c:forEach>
                        </td>
                        <td>${deal.description}</td>
                        <td>${deal.createDateTime}</td>
                        <td>${deal.tags}</td>
                        <%--<sec:authorize access="hasRole('ADMIN')">--%>
                            <td> <a href="${dealdetailsURL}/${deal.id}" class="btn btn-primary edit-btn">Details</a> </td>
                            <td> <a href="${deletedealURL}/${deal.id}" class="btn btn-danger delete-btn">Delete</a> </td>
                        <%--</sec:authorize>--%>
                        <td class="text-center">
                            <%--<c:choose>--%>
                                <%--<c:when test="${deal.available > 0}">--%>
                                    <a href="${debtURL}/${deal.id}" class="btn btn-info btn-sm">Pay</a>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                    <%--zaplacone--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp" %>