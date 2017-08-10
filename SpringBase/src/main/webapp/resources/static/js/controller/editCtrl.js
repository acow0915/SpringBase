app.controller("editCtrl", function(MyService, $scope, $routeParams, $location) {
	//alert($routeParams.id);
	$scope.get = function(){
		MyService.get({id : $routeParams.id}, function(data){
			$scope.item = data;
		});
	}
	$scope.item = $scope.get();
	
	$scope.update = function(){
		
		MyService.update({
			id : $scope.item.id,
			name : $scope.item.name,
			age : $scope.item.age
		}, function(data){
			$scope.item = data;
		});
	}
	
	$scope.close = function(){
		$location.path("/main");
	} 
});