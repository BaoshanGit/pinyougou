package com.pinyougou.manager.controller;

/**
 * 品牌web层
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService service;

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/findAll.do")
    /* @ResponseBody*/
    public List<TbBrand> findAll() {
        List<TbBrand> list = service.findAll();
        return list;
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPage.do")
    /* @ResponseBody*/
    public PageResult findPage(@RequestParam(name = "page") int pageNum, @RequestParam(name = "size") int pageSize) {
        PageResult page = service.findPage(pageNum, pageSize);
        return page;
    }


    /**
     * 添加数据
     *
     * @param brand
     * @return
     */
    @RequestMapping("/save.do")
    public Result save(@RequestBody TbBrand brand) {
        try {
            service.save(brand);
            return new Result(true, "添加数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "添加数据失败");
    }

    /**
     *删除数据信息
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result dele(Long[] ids) {
        try {
            if (ids != null && ids.length > 0) {
                service.dele(ids);
                return new Result(true,"数据删除成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"数据删除失败");
    }


    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public TbBrand findOne(Long id){
        TbBrand brand = null;
        if (id != 0){
             brand = service.findOne(id);
        }
        return brand;
    }


    /**
     * 修改数据
     * @param brand
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody TbBrand brand){
        try {
            if (brand != null){
                service.update(brand);
                return new Result(true,"修改信息成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"修改信息失败");
    }


    /**
     * 条件查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/findLike.do")
    public PageResult findLike(@RequestBody TbBrand brand,@RequestParam(name = "page") int page , @RequestParam(name = "size") int size){
        PageResult result = null;
        try {
            if (brand !=  null){
                 result = service.findLike(brand, page, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
