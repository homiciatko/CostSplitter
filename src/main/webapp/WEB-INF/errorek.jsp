<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>

<c:url value="/main" var="mainURL" />


<c:if test="${cantPay}">
    <div class="alert alert-danger">
        allready paid
    </div>
</c:if>

<div class="container">

    <h1>Smth went wrong, pls refresh page:</h1>

    <div class="row">
        <div class="col-md-12">
            <a href="${mainURL}" class="btn btn-info btn-sm">MAIN</a>
        </div>
    </div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp" %>