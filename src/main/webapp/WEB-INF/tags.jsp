<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>

<c:url value="/tag/delete" var="deleteTagURL" />
<c:url value="/tag/edit" var="editTagURL" />
<c:url value="/tag/create" var="createTagURL" />


<div class="container">

    <h1>My tags</h1>

    <div class="row">
        <div class="col-md-12">
            <a href="${createTagURL}" class="btn btn-info btn-sm">Add new...</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped table-hover table-bordered">
                <thead>
                <tr>
                    <th class="text-center col-md-1">Id</th>
                    <th class="text-center">Name</th>
                    <th class="text-center">Description</th>
                    <th class="text-center col-md-1">Edit</th>
                    <th class="text-center col-md-1">Delete</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${tagList}" var="tag">
                    <tr>
                        <td>${tag.id}</td>
                        <td>${tag.name}</td>
                        <td>${tag.description}</td>
                        <td> <a href="${editTagURL}/${tag.id}" class="btn btn-primary edit-btn">Edit</a> </td>
                        <td> <a href="${deleteTagURL}/${tag.id}" class="btn btn-danger delete-btn">Delete</a> </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<%@ include file="/WEB-INF/include/footer.jsp" %>
