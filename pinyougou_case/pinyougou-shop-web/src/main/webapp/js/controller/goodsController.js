 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,goodsService ,uploadService, typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.add=function(){
		//提取富文本内容
		$scope.entity.goodsDesc.introduction = editor.html();
		goodsService.add( $scope.entity).success(
			function(response){
				if(response.success){
					//重新查询
					alert(response.message);
					$scope.entity = {};
					editor.html('');
		        	//$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//获取品牌下拉菜单
	$scope.brandList = {data:[]};
	$scope.showBrand = function(){
        typeTemplateService.findBrandAndOption().success(function (response) {
			$scope.brandList = {data:response.brand};
        });
	}

	//获取上传图片地址
	$scope.uploadFile=function () {
        uploadService.uploadFile().success(function (response) {
			if (response.success){
				$scope.imageEntity.url=response.message;
			} else {
				alert(response.message);
			}
        });
    }

    //图片列表
	$scope.entity={goods:{},goodsDesc:{itemImages:[]}};

	$scope.addImage=  function () {
        $scope.entity.goodsDesc.itemImages.push($scope.imageEntity);
    }

    //移除图片
	$scope.dele_img = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    //保存图片编辑信息
	/*$scope.addImageTo*/
    
});	
