<div ng-controller="editCtrl">
    <input type="hidden" ng-model="item.id">
    <input class="form-control" ng-model="item.name">
    <input class="form-control" ng-model="item.age">
    <button class="btn btn-default" ng-click="update()">update</button>
    <button class="btn btn-default" ng-click="close()">close</button>
</div>