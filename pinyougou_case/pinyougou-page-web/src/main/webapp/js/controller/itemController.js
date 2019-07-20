/**
	��Ʒ����ҳ�����Ʋ㣩
*/

app.controller('itemController',function($scope){
	
	//��Ʒ��������
	$scope.shopNum = 1;
	$scope.addShopNum = function(x){
		$scope.shopNum += x;
		//��׳���ж�
		if($scope.shopNum < 1){
			$scope.shopNum = 1;
		}
	}
	
	//������
	$scope.specItems = {};//��¼�û�ѡ��Ĺ��
	$scope.addSpecItem = function(name, value){
		//���û�ѡ��Ĺ��������ӵ�������
		$scope.specItems[name] = value;
		showSku();
	}
	
	//Ĭ��ҳ����ʾ
	$scope.loadSku = function(){
		$scope.sku=skuList[0];		
		$scope.specItems= JSON.parse(JSON.stringify($scope.sku.spec)) ;
	}
	
	//���ݹ��ѡ��Ĳ�ͬչʾ��Ӧ�ı���ͼ۸�
	showSku = function(){
		for(var i =0 ; i< skuList.length ; i++){
				if(matchObj($scope.specItems,skuList[i].spec)){
					$scope.sku = skuList[i];
					return;
			}
		}
		
		//û��ƥ�����
		$scope.sku = {id:0,title:'�ף�����Ʒ��ʱû��Ŷ',price:'0'};
		
	}
	
	
	//�Ƚ����������Ƿ����
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