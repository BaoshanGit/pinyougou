app.controller("loginController" , function ($scope,loginService) {

    //获取用户名
    $scope.loginName = function () {
        loginService.loginName().success(function (response) {
            $scope.username = response.username;
        });
    }
})