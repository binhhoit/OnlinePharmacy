
var myApp = angular.module('myModule', []);

myApp.controller('myController', function ($scope) {
    var employee = {
        FirstName: "Thanh Binh",
        LastName: "Ho",
        Gender: "Male",

    }

    //$scope.mix = mix;
    $scope.employee = employee;
    $scope.onclick = function () {
        $scope.mix = $scope.employee.FirstName + "  " + $scope.employee.LastName;
    }
});

angular.module('directoryApp', ['ui.router'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('home', {
                url: '/home',
                templateUrl: '/Views/Home.html',
                
            })
            .state('about', {
                url: '/about',
                template: '<h1>about2</h1>'
            })
        $urlRouterProvider.otherwise('/');
    });



//show and add
function DataTest($scope, $http) {

    var refresh = function () {
        $http.get('/contactlist').success(function (response) {
            console.log("start log");
            $scope.contactlist = response;
            console.log(response);
            $scope.contact = "";
        });
    };
    refresh();
    $scope.addData = function () {
        $http.post('/contactlist', $scope.contact).success(function (response) {
            refresh();
        });
    };

     $scope.removeData = function (id) {
         console.log(id);
        $http.delete('/contactlist/'+ id).success(function (response) {
            refresh();
        });
    };

};
