<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/tag/create" var="createTagURL" />
<c:url value="/tag" var="tagURL" />

<div class="container">

    <c:if test="${tag.id == null}" >
        <h1>Create Tag</h1>
    </c:if>

    <c:if test="${tag.id != null}" >
        <h1>Edit Tag</h1>
    </c:if>



    <div class="row">
        <form:form action="${createTagURL}" method="post" role="form" class="form-horizontal">

            <input type="hidden" value="${tag.id}" name="id" />

            <div class="form-group">
                <label class="control-label col-sm-2" for="name">Tage name:</label>
                <div class="col-sm-6">
                    <input value="${tag.name}" name="name" type="text" id="name" class="form-control" placeholder="Enter Tag name" autofocus>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-sm-2" for="description">Tag description:</label>
                <div class="col-sm-6">
                    <input value="${tag.description}" name="description" type="text" id="description" class="form-control" placeholder="Enter last name" >
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-6">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <a href="${tagURL}" class="btn btn-danger">Cancel</a>
                </div>
            </div>

        </form:form>
    </div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp" %>