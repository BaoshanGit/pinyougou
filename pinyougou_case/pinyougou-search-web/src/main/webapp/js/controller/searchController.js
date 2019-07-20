app.controller('searchController',function ($scope,$location,searchService) {

    $scope.search = function () {
        $scope.searchMap.pageNo = parseInt( $scope.searchMap.pageNo);//将字符串转换成为数字
        $scope.searchMap.status = parseInt( $scope.searchMap.status);//将字符串转换成为数字
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
            $scope.isBrand();
            $scope.buildPage();
        });
    }

    //接收首页传递的搜索关键字
    $scope.indexSearch = function(){
        var keywords = $location.search()['keywords'];
        $scope.searchMap.keywords = keywords;
        $scope.search();
    }

    //隐藏品牌列表
    $scope.isBrand = function(){
        //判断搜索关键字是否包含品牌
        for (var i = 0; i <$scope.resultMap.brandList.length ; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
                //关键字是品牌，就将关键字赋值给品牌
                $scope.searchMap.brand =  $scope.searchMap.keywords;
            }
        }
    }


    //定义搜索选项
    $scope.searchMap = {keywords:'',category:'',brand:'',spec:{},price:'',pageNo:1,status:-1 , statusField:''};

    //添加搜索项
    $scope.addSearchItem =function (key,value) {
        if (key=='category' || key=='brand' || key=='price'){
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
     $scope.search( $scope.searchMap);

    }

    //撤销搜选项
    $scope.removeSearchItem = function (key) {
        if (key=='category' || key=='brand' || key=='price'){
            $scope.searchMap[key] = '';
        } else {
           delete $scope.searchMap.spec[key];
        }
        $scope.search( $scope.searchMap);
    }

    //分页设置
    $scope.buildPage = function () {
        $scope.pageList=[];//设置页标签集合
        var begin = $scope.searchMap.pageNo-2;//起始页
        var end = $scope.searchMap.pageNo+2;//结束页
        if ($scope.resultMap.totalPages > 5) {

            if (begin <= 0) {
                begin = 1;
                end = begin + 4;
            }
            if (end > $scope.resultMap.totalPages) {
                end = $scope.resultMap.totalPages;
                begin = end - 4;
            }
        }else {
            begin = 1;
            end = $scope.resultMap.totalPages;
        }

            //循环获取页标签
            for (var i = begin; i <= end; i++) {
                $scope.pageList.push(i);
            }

    }
    
    //分页查询
    $scope.searchByPage = function (pageNo) {
        //页码健壮性判断
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }

    //按价格排序(status : -1 不排序 0: 按降序 1：按升序)
    $scope.orderByPrice = function (statusField , status) {
        //设置排序字段
        if (statusField != 'price'){
           $scope.searchMap.status = status;
        }
        $scope.searchMap.statusField =  statusField;
        $scope.search();
    }

});