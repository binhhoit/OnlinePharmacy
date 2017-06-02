
var myApp = angular.module('myModule', []);

//show and add
function drugStoreHTML($scope, $http) {

    var refresh = function () {
        $http.get('/api/drugstorelist').success(function (response) {
            console.log("start log");
            $scope.drugstorelist = response;
            console.log(response);
            $scope.drugstore = "";
        });
    };
    refresh();
    $scope.addData = function () {
        $http.post('/api/drugstorelist', $scope.drugstore).success(function (response) {
            refresh();
        });
        console.log($scope.drugstore);
    };

    $scope.removeData = function (id) {
        console.log(id);
        $http.delete('/api/drugstorelist/' + id).success(function (response) {
            refresh();
        });
    };

};

function prescriptionHTML($scope, $http) {

    var refresh = function () {
        var id = "Long"
        var number_buy = "2"
        $http.get('/api/prescription/' + id + "/" + number_buy).success(function (response) {
            console.log(response + "prescription");
            $scope.prescriptionlist = response; //mảng các toa thuốc
            $scope.prescriptionlist_x = $scope.prescriptionlist[0].prescription // phân tích mãng nhỏ bên trong
            var list = response;
            for (var i = 0; i < list.lenght; i++) {

            }
            console.log(response);
            $scope.prescription = "";
        });
    };
    refresh();
    $scope.addData = function () {
        console.log($scope.prescription + "  ++ prescription");
        $http.post('/api/prescription', $scope.prescription).success(function (response) {
            refresh();
        });
        console.log($scope.prescription);
    };

    $scope.removeData = function (id) {
        console.log(id);
        $http.delete('/api/prescription/' + id).success(function (response) {
            refresh();
        });
    };

};


function Pay($scope, $http) {

  
    $scope.submit = function () {
       
        $http.post('/api/1pay', $scope.cardInfo).success(function (response) {
            
            console.log(response);
        });
       
    };


};