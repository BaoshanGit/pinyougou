package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper mapper;
    //查询所有品牌信息
    @Override
    public List<TbBrand> findAll() {
        List<TbBrand> list = mapper.selectByExample(null);
        return list;
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        if (pageNum >= 1 && pageSize >=1){
            PageHelper.startPage(pageNum,pageSize);
            List<TbBrand> list = mapper.selectByExample(null);
            Page page = (Page) list;
            long total = page.getTotal();
            return new PageResult(total,list);
        }
       return null;
    }

    /**
     * 添加品牌信息
     * @param brand
     */
    public void save(TbBrand brand){
        if (brand != null){
            mapper.insert(brand);
        }
    }

    /**
     * 删除数据
     * @param ids
     */
    @Override
    public void dele(Long[] ids) {
        if (ids != null && ids.length >0){
            //遍历调方法删除数据
            for (Long id : ids) {
                mapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 修改数据
     * @param brand
     */
    @Override
    public void update(TbBrand brand) {
        if (brand != null){
            mapper.updateByPrimaryKey(brand);
        }
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        TbBrand brand = null;
        if (id != 0){
             brand = mapper.selectByPrimaryKey(id);
        }
        return brand;
    }

    /**
     * 条件查询
     * @param brand
     * @return
     */
    @Override
    public PageResult findLike(TbBrand brand ,int pageNum ,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (brand != null){
            if (brand.getName() != null && brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar() != null && brand.getFirstChar().length() == 1){
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
            List<TbBrand> brands = mapper.selectByExample(example);
            Page page = (Page) brands;
            long total = page.getTotal();
            return new PageResult(total,brands);
        }
        return null;
    }
}
