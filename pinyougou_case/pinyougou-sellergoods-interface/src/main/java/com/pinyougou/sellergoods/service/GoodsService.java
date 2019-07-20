package com.pinyougou.sellergoods.service;
import java.util.List;

import com.pinyougou.entity.Goods;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods);

	/**
	 * 运营商审核商品
	 * @param goodsIds
	 * @param status
	 */
	public void managerAudit(Long[] goodsIds ,String status);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

	/**
	 * 运营商删除数据
	 * @param ids
	 */
	void managerDelete(Long[] ids);


	//运营商商品审核查询
	PageResult goodsfFindPage(TbGoods goods, int pageNum, int pageSize);

	/**
	 * 商品上下架
	 * @param goodsIds
	 */
	void isPutaway(Long[] goodsIds ,String putaway);


	/**
	 * 根据id查询TbItem
	 * @param goodsIds
	 * @param status
	 * @return
	 */
	public List<TbItem> findByGoodsIdAndPutaway(Long[] goodsIds , String status);
}
