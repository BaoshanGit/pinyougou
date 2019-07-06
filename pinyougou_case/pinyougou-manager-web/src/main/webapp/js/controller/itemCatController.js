 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.gradeEntity= response;
			}
		);				
	}
	
	//保存
	$scope.parentId = 0;
    $scope.gradeEntity={name:'',typeId:''};
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.gradeEntity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.gradeEntity ); //修改
		}else{
            $scope.gradeEntity.parentId = $scope.parentId;//设置封装数据
			serviceObject=itemCatService.add( $scope.gradeEntity  );//增加
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.findByParentId($scope.parentId);
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(parentId){
		//获取选中的复选框			
		itemCatService.dele( $scope.checkboxStatus ).success(
			function(response){
				if(response.success){
					//$scope.reloadList();//刷新列表
                    $scope.findByParentId(parentId);
					$scope.checkboxStatus=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rwos;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//定义数据存储对象
	//设置等级
	$scope.grade= 1;
	$scope.setGrade=function(value){
			$scope.grade = value;
	}
	//设置显示名称
	    $scope.newShop = {entity_01:'',entity_02:''};
        $scope.showGrade = function(entity){
		if ($scope.grade == 1){
			$scope.entity_1 = null;
			$scope.entity_2 = null;
		}
		if ($scope.grade == 2){
			$scope.entity_1 = entity;
			$scope.entity_2 = null;
            $scope.newShop.entity_01=entity.name;
		}
		if ($scope.grade == 3){
			$scope.entity_2 = entity;
            $scope.newShop.entity_02=entity.name;
		}
		$scope.findByParentId(entity.id);
	}

	//新建商品分类

//新建模块数据显示
    $scope.template = {data:[]}
    $scope.findAllName = function(){
        itemCatService.findAllName().success(function (response) {
            $scope.template = {data:response};
        });
    }

    //设置显示级别名称
	$scope.newShopName = function(grade){
        	if (grade ==1){
                $scope.newShop.entity_01 = null;
                $scope.newShop.entity_02 = null;
			}else if (grade ==2) {
                $scope.newShop.entity_02 = null;
            }
    }



	//分类管理数据查询

    $scope.findByParentId=function(parentId){
        	$scope.parentId=parentId;//存储parentid
        itemCatService.findByParentId(parentId).success(
            function(response){
                $scope.list=response;
            }
        );
    }
});	
