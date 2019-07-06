package com.pinyougou.sellergoods.service;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * 品牌接口
 */
public interface BrandService {
    //查询所有品牌
    public List<TbBrand> findAll();

    //分页查询
    public PageResult findPage(int pageNum , int pageSize);

    //添加品牌数据
    public void save(TbBrand brand);

    /**
     *删除数据
     * @param id
     */
    public void dele(Long[] id);

    /**
     * 修改数据
     * @param brand
     */
    public void update(TbBrand brand);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public TbBrand findOne(Long id );

    /**
     * 条件查询
     * @param brand
     * @return
     */
    public PageResult findLike(TbBrand brand , int pageNum , int pageSize);
}
