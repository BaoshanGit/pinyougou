package com.pinyougou.entity;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbSpecification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SpecificationOption implements Serializable {
    //封装品牌，ID= id ; name = text;
    private List<Map<String , Object>> brand;
    //封装规格
    private List<Map<String , Object>> specification;

    public List<Map<String, Object>> getBrand() {
        return brand;
    }

    public void setBrand(List<Map<String, Object>> brand) {
        this.brand = brand;
    }

    public List<Map<String, Object>> getSpecification() {
        return specification;
    }

    public void setSpecification(List<Map<String, Object>> specification) {
        this.specification = specification;
    }
}
