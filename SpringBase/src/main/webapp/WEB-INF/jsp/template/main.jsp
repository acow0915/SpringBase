<div class="container" ng-controller="myCtrl">
    <ul>
        <li ng-repeat="x in products">
        	<!-- 
            id : {{x.id}} - name : {{x.name}} - age : {{x.age}}
            <span ng-click="removeItem($index)">&times;</span>
            <span ng-click="edit(x.id)">edit</span>
             -->
            <div class="row">
			  <div class="col-sm-2">{{x.id}}</div>
			  <div class="col-sm-3">{{x.name}}</div>
			  <div class="col-sm-3">{{x.age}}</div>
			  <!-- <div class="col-sm-2"><button class="btn btn-default" ng-click="removeItem($index)">&times;</button></div> -->
			  <div class="col-sm-2"><button class="btn btn-default" ng-click="removeItem(x.id)">delete</button></div>
			  <div class="col-sm-2"><button class="btn btn-default" ng-click="edit(x.id)">edit</button></div>
			</div>
        </li>
    </ul>
    {{result}}
    <!-- <input ng-model="x.id"> -->
    <input class="form-control" ng-model="x.name">
    <input class="form-control" ng-model="x.age">
    <button class="btn btn-default" ng-click="addItem()">Add</button>
</div>