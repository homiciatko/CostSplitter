<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/group/create" var="createGroupURL" />
<c:url value="/group" var="groupURL" />

<div class="container">

    <c:if test="${group.id == null}" >
        <h1>Create group</h1>
    </c:if>

    <c:if test="${group.id != null}" >
        <h1>Edit group</h1>
    </c:if>



    <div class="row">
        <form:form action="${createGroupURL}" method="post" role="form" class="form-horizontal">

            <input type="hidden" value="${group.id}" name="id" />

            <div class="form-group">
                <label class="control-label col-sm-2" for="name">groupe name:</label>
                <div class="col-sm-6">
                    <input value="${group.name}" name="name" type="text" id="name" class="form-control" placeholder="Enter group name" autofocus>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-2" for="description">group description:</label>
                <div class="col-sm-6">
                    <input value="${group.description}" name="description" type="text" id="description" class="form-control" placeholder="Enter last name" >
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-6">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <a href="${groupURL}" class="btn btn-danger">Cancel</a>
                </div>
            </div>

        </form:form>
    </div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp" %>