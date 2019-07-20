package com.pinyougou.shop.controller;
import java.util.List;

import com.pinyougou.entity.Goods;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	@Reference
	private ItemSearchService itemSearchService;

	@Reference
	private ItemPageService itemPageService;

	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		//获取登录名
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.getGoods().setSellerId(name);
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		//校验是否是当前商家的id
		Goods goods2 = goodsService.findOne(goods.getGoods().getId());
		//获取当前登录的商家ID
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//如果传递过来的商家ID并不是当前登录的用户的ID,则属于非法操作
		if(!goods2.getGoods().getSellerId().equals(sellerId) ||  !goods.getGoods().getSellerId().equals(sellerId) ){
			return new Result(false, "操作非法");
		}
		try {
			goodsService.update(goods);
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
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
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
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.setSellerId(sellerId);
		return goodsService.findPage(goods, page, rows);		
	}

	//商品上下架
	@RequestMapping("/putaway")
	public Result isPutaway (Long [] goodsIds,String putaway){
		try {
			goodsService.isPutaway(goodsIds,putaway);
			//更新solr库
			List<TbItem> itemList = goodsService.findByGoodsIdAndPutaway(goodsIds, putaway);
			System.out.println("----准备更新solr----");
			itemSearchService.updateSolr(itemList);
			System.out.println("----更新solr完成----");
			for (Long goodsId : goodsIds) {
				demo(goodsId);//生产静态页面
			}
			return new Result(true, "上架成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "上架失败");
		}
	}

	//静态页面生成测试
	//@RequestMapping("/demo")
	public void  demo(Long goodsId){
		itemPageService.genIetmHtml(goodsId);
	}
}
