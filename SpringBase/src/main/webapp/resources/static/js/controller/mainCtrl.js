app.controller("myCtrl", function(MyService, $scope, $location, $routeParams) {
	$scope.query = function(){
			MyService.query(function(data){
				console.log("-----" + data.length + "-----");
				$scope.products = data;
			});
	}
	$scope.products = $scope.query();
	
	//save
    $scope.addItem = function () {
    	var account = new Account(0, $scope.x.name, $scope.x.age);
    	MyService.save({}, account, function(result){
    		console.log(result.success);
    		$scope.products = $scope.query();
    	});
    }
    
    //remove
    $scope.removeItem = function (id) {
    	MyService.remove({id : id}, function(result){
    		console.log(result.success);
    		$scope.products = $scope.query();
    	});
        //$scope.products.splice(x, 1);
    } 
    
    //update
    $scope.edit = function (id) {
    	$location.path("/update/"+id);
    } 
});