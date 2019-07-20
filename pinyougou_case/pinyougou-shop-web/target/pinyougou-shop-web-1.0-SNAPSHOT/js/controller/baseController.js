app.controller("baseController",function($scope){
    <!--设置分页控件-->
    $scope.paginationConf = {
        currentPage : 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [5,10, 20, 30, 40, 50],
        onChange : function () {
            $scope.reloadList();
        }
    }

    //刷新页面
    $scope.reloadList = function(){
        $scope.search($scope.paginationConf.currentPage , $scope.paginationConf.itemsPerPage);
    }

    //复选框状态
    //定义一个数组
    $scope.checkboxStatus = []; //存储被选中的复选框的id值
    $scope.boxStatus = function(event,id){ // 该方法用于将选中的复选框的id存入集合
        if (event.target.checked){
            if ( $scope.checkboxStatus.indexOf(id) == -1){
                $scope.checkboxStatus.push(id);//添加id
            }
        }else {
            var index = $scope.checkboxStatus.indexOf(id);
            if (index != -1){
                $scope.checkboxStatus.splice(index,1);//删除对应位置的ID
            }
        }
    }
    //回显复选框状态 ，返回一个Boolean值
    $scope.expression = function (id) {
      return  $scope.checkboxStatus.indexOf(id) != -1;
    }

    //从集合中按照key查询对象
    $scope.searchObjectByKey=function(list,key,keyValue){
        for(var i=0;i<list.length;i++){
            if(list[i][key]==keyValue){
                return list[i];
            }
        }
        return null;
    }
});