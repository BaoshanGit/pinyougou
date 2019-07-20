app.controller('contentController',function ($scope,contentService) {

    //定义广告集合
    $scope.contentList=[];
    //根据分类ID查询广告
    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId] = response;
        });
    }
})