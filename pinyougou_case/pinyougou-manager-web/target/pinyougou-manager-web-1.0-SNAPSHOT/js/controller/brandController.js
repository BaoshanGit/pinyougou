/*品牌控制*/
app.controller("brandController",function($scope,$controller,brandService){

    //继承baseController
    $controller("baseController",{$scope:$scope});
    /* $scope.findAll = function () {
         $http.get("../brand/findAll.do").success(function (response) {
             /!*定义一个变量接收数据*!/
             $scope.list = response;
         });
     }*/

    /*<!--设置分页控件-->
    $scope.paginationConf = {
        currentPage : 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [5,10, 20, 30, 40, 50],
        onChange : function () {
            $scope.reloadList();
        }
    }*/

   /* //刷新页面
    $scope.reloadList = function(){
        $scope.search($scope.paginationConf.currentPage , $scope.paginationConf.itemsPerPage);
    }*/
    /*分页查询*/
    $scope.findPage = function (pageNum,pageSize) {
        brandService.findPage(pageNum,pageSize).success(function (response) {
            $scope.brands = response.rwos;
            $scope.paginationConf.totalItems = response.total;//更新总条数
        });
    }

    //添加数据
    $scope.save = function () {
        var obj= null;
        if ($scope.entity.id != null){
            obj = brandService.update($scope.entity);//执行修改方法
        } else {
            obj = brandService.save($scope.entity);//执行添加方法
        }
        obj.success(function (response) {
            $scope.reloadList();//重新加载
            /*if (response.success){
              $sopce.reloadList();//重新加载
            }else {
                alert(response.message);
            }*/
        });
    }

    /*//复选框状态
    //定义一个数组
    $scope.checkboxStatus = [];
    $scope.boxStatus = function($event,id){
        if ($event.target.checked){
            /!*if ($scope.checkboxStatus.length > 0 ){
                for (var i = 0; i <$scope.checkboxStatus.length ; i++) {
                    if ($scope.checkboxStatus[i] == id){
                        var index = $scope.checkboxStatus.indexOf(id);
                        $scope.checkboxStatus.splice(index,1);//删除对应位置的ID
                    }else {
                        $scope.checkboxStatus.push(id);//添加id
                    }
                }
            }else {
                $scope.checkboxStatus.push(id);//添加id
            }*!/
            $scope.checkboxStatus.push(id);//添加id
        }else {
            var index = $scope.checkboxStatus.indexOf(id);
            $scope.checkboxStatus.splice(index,1);//删除对应位置的ID
        }
    }*/
    //保存复选框状态  expression ：true为选中状态
    /*$scope.expression = function(id){
        if ($scope.checkboxStatus != null && $scope.checkboxStatus.length >0){
            //遍历设置状态
            for (var i = 0; i <$scope.checkboxStatus.length ; i++) {
                return $scope.checkboxStatus[i] == id;
            }
        }
    }*/



    //删除数据
    $scope.dele = function () {
        brandService.dele($scope.checkboxStatus).success(function (response) {
            //更新数据
            $scope.reloadList();
        });
    }

    //修改数据
    //回显数据
    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
            $scope.reloadList();
        });
    }
    //保存修改后的数据
    //调用save方法

    //条件查询

    $scope.like = {name:"",firstChar:""};
    $scope.search = function (page,size) {
        brandService.search(page,size,$scope.like).success(function (response) {
            $scope.brands = response.rwos;
            $scope.paginationConf.totalItems = response.total;//总条数
        });
    }
});