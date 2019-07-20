var app = angular.module('pinyougou',[]);//定义模块

//使用sce服务构造过滤器
app.filter('trustHtml',['$sce',function ($sce) {
    return function (data) {
        return $sce.trustAsHtml(data);
    }
}]);