app.factory("MyService", function($resource){
	return $resource(contextPath + '/test123/:id', 
			{id : '@id'},
			{'update': { method:'PUT' }}	);
});