<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>




<c:if test="${cantPay}">
    <div class="alert alert-danger">
        allready paid
    </div>
</c:if>

<div class="container">

    <h1>You paid Deal:</h1>

    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped table-hover table-bordered">
                <thead>
                <tr>
                    <th class="text-center col-md-1">Description</th>
                    <th class="text-center">Deal date</th>
                    <th class="text-center">Paid</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${dealList}" var="deal">
                    <tr>
                        <td>${deal.description}</td>
                        <td>${deal.createDateTime}</td>
                        <td>${debtList[0].money}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp" %>