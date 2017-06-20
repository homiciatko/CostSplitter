<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>

<c:url value="/deal-create" var="createDealURL" />
<c:url value="/deals" var="dealsURL" />

<div class="container">

    <c:if test="${deal.id == null}" >
        <h1>Create Deal</h1>
    </c:if>
    <c:if test="${deal.id != null}" >
        <h1>Edit deal</h1>
    </c:if>

    <div class="row">
        <form:form commandName="deal" action="${createDealURL}" method="post" role="form" class="form-horizontal">
            <form:hidden  path="id"/>

            <div class="form-group">
                <label class="control-label col-sm-2" for="participants">participants:</label>
                <div class="col-sm-6">
                    <input name="participants" type="text" id="participants" class="form-control" placeholder="Enter participants" />
                    <%--<form:input path="participants" type="email" id="participants" class="form-control" placeholder="Enter participants" aufocus="autofocus" />--%>
                    <%--<form:errors path="participants" cssStyle="color: #ff7a37"/>--%>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="description">description:</label>
                <div class="col-sm-6">

                    <form:input path="description" type="text" id="description" class="form-control" placeholder="Enter description" />
                    <form:errors path="description" />
                </div>
                <%--<div class="col-sm-6">--%>
                    <%--<label class="control-label col-sm-2" for="paid">paid:</label>--%>
                    <%--<form:input path="paid" type="number" id="paid" class="form-control" placeholder="Enter paid amount" />--%>
                    <%--<form:errors path="paid" />--%>
                <%--</div>--%>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="dealSum">Deal Sum:</label>
                <div class="col-sm-6">
                    <form:input path="dealSum" type="number" id="dealSum" class="form-control" placeholder="Enter dealSum" />
                    <form:errors path="dealSum" />
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="paids">Paid:</label>
                <div class="col-sm-6">
                    <input name="paids" type="text" id="paids" class="form-control" placeholder="Enter paids value" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-6">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <a href="${dealsURL}" class="btn btn-danger">Cancel</a>
                </div>
            </div>
        </form:form>
    </div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp" %>