package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.entity.SpecificationOption;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	@Reference
	private TypeTemplateService typeTemplateService;
	@Reference
	private SpecificationService specificationService;
	@Reference
	private BrandService brandService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbTypeTemplate> findAll(){			
		return typeTemplateService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return typeTemplateService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbTypeTemplate typeTemplate){
		try {
			typeTemplateService.add(typeTemplate);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbTypeTemplate typeTemplate){
		try {
			typeTemplateService.update(typeTemplate);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbTypeTemplate findOne(Long id){
		return typeTemplateService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			typeTemplateService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbTypeTemplate typeTemplate, int page, int rows  ){
		return typeTemplateService.findPage(typeTemplate, page, rows);		
	}


	@RequestMapping("/findBrandAndOption")
	public SpecificationOption findBrandAndOption(){
		SpecificationOption specificationOption = new SpecificationOption();
		//封装option信息为ID：xx ; text : xx
		List<Map<String ,Object>> listspe = new ArrayList<>();
		List<TbSpecification> all = specificationService.findAll();
		for (TbSpecification tbSpecification : all) {
			Map<String ,Object> map = new HashMap<>();
			map.put("id",tbSpecification.getId());
			map.put("text",tbSpecification.getSpecName());
			listspe.add(map);
		}
		specificationOption.setSpecification(listspe);

		//封装品牌信息为ID：xx ; text : xx
		List<TbBrand> brands = brandService.findAll();
		List<Map<String ,Object>> listbrand = new ArrayList<>();

		for (TbBrand brand : brands) {
			Map<String ,Object> map = new HashMap<>();
			map.put("id",brand.getId());
			map.put("text",brand.getName());
			listbrand.add(map);
		}
		specificationOption.setBrand(listbrand);
		return specificationOption;
	}

	@RequestMapping("/findAllName")
	public List findAllName(){
		List<TbTypeTemplate> all = findAll();
		List list = new ArrayList();
		for (TbTypeTemplate tbTypeTemplate : all) {
			Map map = new HashMap<>();
			map.put("id",tbTypeTemplate.getId());
			map.put("text",tbTypeTemplate.getName());
			list.add(map);
		}
		return list;
	}

	//根据id 查询数据并进行数据的封装（封装规格数据以及模板spec_id的text属性）
	@RequestMapping("/specList")
	public List<Map> specList(Long id){
	return 	typeTemplateService.specList(id);
	}
}
