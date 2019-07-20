//品牌服务
app.service("brandService",function ($http) {
    //绑定数据
    this.findPage = function (pageNum,pageSize) {
        return $http.get("../brand/findPage.do?page="+pageNum+"&size="+pageSize);
    }

    this.dele = function (checkboxStatus) {
        return $http.get("../brand/delete.do?ids="+checkboxStatus);
    }

    this.findOne = function (id) {
        return $http.get("../brand/findOne.do?id="+id);
    }

    this.search = function (page,size,like) {
        return $http.post("../brand/findLike.do?page="+page+"&size="+size,like);
    }

    this.update = function (entity) {
        return $http.post("../brand/update.do",entity);
    }

    this.save = function (entity) {
        return $http.post("../brand/save.do",entity);
    }
});