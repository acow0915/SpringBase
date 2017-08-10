//使用$resource 要宣告 ngResource
//使用route 需載入ngRoute
var app = angular.module("myApp", ['ngRoute', 'ngResource']);

function Account(id, name, age){
	this.id = id;
	this.name = name;
	this.age = age;
}

app.config(function($routeProvider){
	$routeProvider
	.when("/main", {
		templateUrl : contextPath + "/main",
		controller : "myCtrl"
	})
	.when("/update/:id", {
		templateUrl : contextPath + "/updatePage"
		//controller : "myCtrl"
	})
	.otherwise({redirectTo : '/main'});
});




