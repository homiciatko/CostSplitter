<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:url value="/user" var="usersURL" > </c:url>
<c:url value="/user/create" var="createUserURL" > </c:url>
<c:url value="/logout" var="logoutUserURL" ></c:url>
<c:url value="/deal-create" var="createDealURL" />
<c:url value="/myDeals" var="myDealsURL" />
<c:url value="/allDeals" var="allDealsURL" />
<c:url value="/debts" var="debtsURL" />
<c:url value="/tag" var="TagsURL" />
<c:url value="/group" var="GroupsURL" />

<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#"></a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Users <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="${createUserURL}">New...</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a href="${usersURL}">Show all</a></li>
                        </ul>
                    </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Deals <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="${createDealURL}">New...</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="${myDealsURL}">My Deals</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="${allDealsURL}">Show all</a></li>
                    </ul>
                </li>
                <li><a href="${debtsURL}">My Debts</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Settings <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="${TagsURL}">Tags</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="${GroupsURL}">Groups</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        <sec:authentication property="principal" />
                        <%-- principal.username   --%>
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">My account</a></li>
                        <li role="separator" class="divider"></li>
                        <li>
                            <a href="${logoutUserURL}">Logout</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>