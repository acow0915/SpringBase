<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<!-- 最新編譯和最佳化的 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
<!-- 選擇性佈景主題 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
<!-- jQuery (Bootstrap 所有外掛均需要使用) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- 最新編譯和最佳化的 JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/angular-resource.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/angular-route.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/app.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/controller/mainCtrl.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/controller/editCtrl.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/service/editService.js"></script>
<script type="text/javascript">
var contextPath = "${pageContext.request.contextPath}";
</script>
</head>
<body ng-app="myApp">
<div ng-view></div>  
<!-- 
	<div ng-controller="myCtrl">
	    <ul>
	        <li ng-repeat="x in products">
	            id : {{x.id}} - name : {{x.name}} - age : {{x.age}}
	            <span ng-click="removeItem($index)">&times;</span>
	        </li>
	    </ul>
	    {{result}}
	    <input ng-model="x.id">
	    <input ng-model="x.name">
	    <input ng-model="x.age">
	    <button ng-click="addItem()">Add</button>
	</div>
 -->
<div>
<a href="<c:url value="/logout" />">Logout</a>
</div> 	
</body>
</html>
