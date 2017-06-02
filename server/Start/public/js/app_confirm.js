
var myApp = angular.module('myModule', ['ui.bootstrap']);


controllerModal.$inject = ['$scope', 'prescriptions', 'id', '$uibModalInstance', '$http'];
myApp.controller('controllerModal', controllerModal);
function controllerModal($scope, prescriptions, id, $uibModalInstance, $http) {
    $scope.prescriptions = prescriptions;
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    }
    $scope.add = function () {
        console.log(id);
        console.log($scope.presciption);
        
        $http.put('/api/confirmList/' + id, $scope.presciption).then(function (response) {
            // console.log($scope.prescription);
            // refresh();
        }, function (err) {
            console.log(err);
        });
        // console.log($scope.prescription);
    };
}


comfirmPresciptionHTML.$inject = ['$scope', '$http', '$uibModal'];
myApp.controller('comfirmPresciptionHTML', comfirmPresciptionHTML)
//show and add
function comfirmPresciptionHTML($scope, $http, $uibModal) {

    var refresh = function () {
       
        $http.get('/api/confirmList/false').then(function (response) {
            $scope.presciptionlist = response.data;
            console.log(response.data);
            $scope.open = function (prescriptions, id) {
                console.log(prescriptions);
                $scope.prescriptions = prescriptions;
                var modalInstance = $uibModal.open({
                    animation: true,
                    backdrop: 'static',
                    keyboard: false,
                    templateUrl: './viewModal',
                    controller: controllerModal,
                    resolve: {
                        prescriptions: function () {
                            return prescriptions;
                        },
                        id: function () {
                            return id;
                        }  
                    }

                });
            }

        }, function (err) {
            console.log(err);
        });

    };

    refresh();
    

    $scope.removeData = function (id) {
        console.log(id);
        $http.delete('/api/drugstorelist/' + id).success(function (response) {
            refresh();
        });
    };

};



