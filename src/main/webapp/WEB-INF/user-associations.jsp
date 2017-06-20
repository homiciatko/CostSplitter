<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/include/header.jsp" %>
<%@ include file="/WEB-INF/include/navbar.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/user/associations/add" var="addAssociationsURL" />
<c:url value="/user/associations" var="associationsURL" />

<div class="container">

    <c:if test="${user.id != null}" >
        <h1>Edit user's associations'</h1>
    </c:if>
        <form:form action="${addAssociationsURL}" method="post" role="form" class="form-horizontal">

            <input type="hidden" value="${user.id}" name="id" />

            <div class="form-group">
                <div id="associations-list">
                    <c:forEach items="${associations}" var="association">
                        <div class="row" >
                            <div class="col-sm-5">${association}</div><button type="button" class="btn btn-danger">X</button>
                        </div>
                    </c:forEach>
                </div>
                <div class="col-sm-6">
                    <input value="" name="newAssociation" type="text" id="newAssociation" class="form-control" placeholder="association ID" autofocus>
                    <button type="button" class="btn btn-primary" onclick="function () {
                                                                               var element = document.getElementById('newAssociation');
                                                                               var value = = element['value'];
                                                                               var associations = '${associations}';
                                                                               associations.push();
                                                                               '${associations}' = associations;
                                                                               $('#associations-list').load();
                                                                            }">+</button>
                </div>

                <div class="col-sm-offset-2 col-sm-6">
                    <button type="submit" class="btn btn-primary">Update</button>
                </div>
            </div>

        </form:form>
</div>
<script type="text/javascript">
 function addViewElement() {
    var element = document.getElementById('newAssociation');
    var value = element['value'];
    var associations = '${associations}';
    associations.push(value);
    $("#associations-list").load();
 }
</script>

<%@ include file="/WEB-INF/include/footer.jsp" %>