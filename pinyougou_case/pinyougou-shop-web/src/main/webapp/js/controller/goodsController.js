 //控制层 
app.controller('goodsController' ,function($scope,$controller ,$location  ,goodsService ,uploadService ,itemCatService ,typeTemplateService){
	
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
	$scope.findOne=function(){
		var id = $location.search()['id'];
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				editor.html(response.goodsDesc.introduction)//富文本内容
				$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages)//设置图片显示
				//customAttributeItems:[{"text":"内存大小","value":"10M"},{"text":"颜色","value":"红色"}]
               /* for (var i = 0; i < $scope.entity.goodsDesc.customAttributeItems ; i++) {
                    $scope.entity.goodsDesc.customAttributeItems[i]=JSON.parse(response.goodsDesc.customAttributeItems[i]);
                }*/
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems)//设置扩展属性
				//specificationItems:[{"attributeName":"网络制式","attributeValue":["移动4G"]},{"attributeName":"屏幕尺寸","attributeValue":["5.5寸","4.5寸"]}]
                $scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems)//设置规格
				//spec:"{\"网络\":\"移动3G\"}"
                for (var i = 0; i <$scope.entity.itemList.length ; i++) {
                	//转换成json对象
                    $scope.entity.itemList[i].spec =  JSON.parse($scope.entity.itemList[i].spec);
                }
			}
		);				
	}
	
	//保存 
	$scope.save=function(){
		//提取富文本内容
		$scope.entity.goodsDesc.introduction = editor.html();
        var serviceObject;//服务层对象
        if($scope.entity.goods.id!=null){//如果有ID
            serviceObject=goodsService.update( $scope.entity ); //修改
        }else{
            serviceObject=goodsService.add( $scope.entity  );//增加
        }
        serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
					alert(response.message);
		        	//$scope.reloadList();//重新加载
					location.href="goods.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.checkboxStatus ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.checkboxStatus=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rwos;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
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
	$scope.entity={goods:{},goodsDesc:{specificationItems:[],itemImages:[]},itemList:[]};

	$scope.addImage=  function () {
        $scope.entity.goodsDesc.itemImages.push($scope.imageEntity);
    }

    //移除图片
	$scope.dele_img = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    //查询一级商品分类
	$scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
			$scope.itemCat1List = response;
        });
    }
    //查询二级商品分类
	$scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat2List = response;
        });
    })

	//查询三级商品分类
	$scope.$watch('entity.goods.category2Id', function (newValue , oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat3List = response;
        });
    })
	//查询模板ID
	$scope.$watch('entity.goods.category3Id',function (newValue ,oldValue) {
        itemCatService.findOne(newValue).success(function (response) {
            $scope.entity.goods.typeTemplateId = response.typeId;
        });
    });

	//查询品牌
	$scope.$watch('entity.goods.typeTemplateId',function (newValue,oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
        	$scope.template = response;//获取模板
			$scope.template.brandIds = JSON.parse($scope.template.brandIds) ;
			if ($location.search()['id'] == null){
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse( $scope.template.customAttributeItems) ;
            }
            $scope.specList(newValue);
        });
    })

	//根据模板ID查询规格数据
	//数据返回个数： [{"id":27,"text":"网络"},option:[{},{}]]
	$scope.specList = function (id) {
        typeTemplateService.specList(id).success(function (response) {
			$scope.specList = response;
        });
    }
    //回显规格数据状态
    $scope.checkAttributeValue=function(specName,optionName){
        var items= $scope.entity.goodsDesc.specificationItems;
        var object= $scope.searchObjectByKey(items,'attributeName',specName);
        if(object==null){
            return false;
        }else{
            if(object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }
    }

    //添加勾选状态
	$scope.updateSpecAttribute = function($event,name,value){
		var obj = $scope.searchObjectByKey( $scope.entity.goodsDesc.specificationItems,'attributeName',name);
		if (obj != null){
			if ($event.target.checked){
               obj.attributeValue.push(value);
			} else {
				//取消勾选
                obj.attributeValue.splice(obj.attributeValue.indexOf(value),1);
                if (obj.attributeValue.length == 0){
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(obj),1);
				}
			}
		}else{
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
        }
    }

    //创建SKU列表
    $scope.createItemList=function(){
        $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0' } ];//初始
        var items=  $scope.entity.goodsDesc.specificationItems;
        for(var i=0;i< items.length;i++){
            $scope.entity.itemList = addColumn( $scope.entity.itemList,items[i].attributeName,items[i].attributeValue );
        }
    }
//添加列值
    addColumn=function(list,columnName,conlumnValues){
        var newList=[];//新的集合
        for(var i=0;i<list.length;i++){
            var oldRow= list[i];
            for(var j=0;j<conlumnValues.length;j++){
                var newRow= JSON.parse( JSON.stringify( oldRow )  );//深克隆
                newRow.spec[columnName]=conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }

	
	//查询商品管理中一级、二级、三级商品名称
	$scope.brandList = [];
	$scope.categoryList = function () {
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i <response.length ; i++) {
				$scope.brandList[response[i].id] = response[i].name;
            }
        });
    }

    //商品上下架
    $scope.putawayStatus = '';
    $scope.putaway = function () {
        goodsService.putaway($scope.checkboxStatus,$scope.putawayStatus).success(function (response) {
            if(response.success){
                $scope.reloadList();//重新加载
                $scope.checkboxStatus=[];
                $scope.putawayStatus = '';
            }else{
                alert(response.message);
            }
        });
    }

});	
