/**
	商品详情页（控制层）
*/

app.controller('itemController',function($scope){
	
	//商品数量操作
	$scope.shopNum = 1;
	$scope.addShopNum = function(x){
		$scope.shopNum += x;
		//健壮性判断
		if($scope.shopNum < 1){
			$scope.shopNum = 1;
		}
	}
	
	//规格操作
	$scope.specItems = {};//记录用户选择的规格
	$scope.addSpecItem = function(name, value){
		//将用户选择的规格属性添加到对象中
		$scope.specItems[name] = value;
		showSku();
	}
	
	//默认页面显示
	$scope.loadSku = function(){
		$scope.sku=skuList[0];		
		$scope.specItems= JSON.parse(JSON.stringify($scope.sku.spec)) ;
	}
	
	//根据规格选择的不同展示相应的标题和价格
	showSku = function(){
		for(var i =0 ; i< skuList.length ; i++){
				if(matchObj($scope.specItems,skuList[i].spec)){
					$scope.sku = skuList[i];
					return;
			}
		}
		
		//没有匹配的项
		$scope.sku = {id:0,title:'亲，该商品暂时没货哦',price:'0'};
		
	}
	
	
	//比较两个对象是否相等
	matchObj = function(map1,map2){
		if(map1.length == map2.length){
			for(var key in map1){
				if(map1[key] != map2[key]){
					return false;
				}
			}
			return true;
		}
	}
});