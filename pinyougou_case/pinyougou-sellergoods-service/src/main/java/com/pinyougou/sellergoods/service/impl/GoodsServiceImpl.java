package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.entity.Goods;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.data.solr.core.SolrTemplate;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbBrandMapper brandMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbSellerMapper sellerMapper;


	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		if (goods != null){
			//添加商品基本信息
			goods.getGoods().setAuditStatus("0");
			goodsMapper.insert(goods.getGoods());
			//添加商品详情信息
			//设置goodsDesc的ID
			goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
			goodsDescMapper.insert(goods.getGoodsDesc());
			saveItemList(goods);
		}
	}

		private void saveItemList(Goods goods){
			if ("1".equals(goods.getGoods().getIsEnableSpec())){

				for(TbItem item :goods.getItemList()){
					//标题
					String title= goods.getGoods().getGoodsName();
					Map<String,Object> specMap = JSON.parseObject(item.getSpec());
					for(String key:specMap.keySet()){
						title+=" "+ specMap.get(key);
					}
					item.setTitle(title);
					setItemValus(goods,item);
					itemMapper.insert(item);
				}
			}else {
				TbItem item=new TbItem();
				item.setTitle(goods.getGoods().getGoodsName());//商品KPU+规格描述串作为SKU名称
				item.setPrice( goods.getGoods().getPrice() );//价格
				item.setStatus("1");//状态
				item.setIsDefault("1");//是否默认
				item.setNum(99999);//库存数量
				item.setSpec("{}");
				setItemValus(goods,item);
				itemMapper.insert(item);
			}
		}

	private void setItemValus(Goods goods,TbItem item) {
		item.setGoodsId(goods.getGoods().getId());//商品SPU编号
		item.setSellerId(goods.getGoods().getSellerId());//商家编号
		item.setCategoryid(goods.getGoods().getCategory3Id());//商品分类编号（3级）
		item.setCreateTime(new Date());//创建日期
		item.setUpdateTime(new Date());//修改日期

		//品牌名称
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
		item.setBrand(brand.getName());
		//分类名称
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());

		//商家名称
		TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
		item.setSeller(seller.getNickName());

		//图片地址（取spu的第一个图片）
		List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class) ;
		if(imageList.size()>0){
			item.setImage ( (String)imageList.get(0).get("url"));
		}
	}


	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		goods.getGoods().setAuditStatus("0");//设置未申请状态:如果是经过修改的商品，需要重新设置状态
		goodsMapper.updateByPrimaryKey(goods.getGoods());//保存商品表
		goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());//保存商品扩展表
		//删除原有的sku列表数据
		TbItemExample example=new TbItemExample();
		com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		itemMapper.deleteByExample(example);
		//添加新的sku列表数据
		saveItemList(goods);//插入商品SKU列表数据
	}

	/**
	 * 运营商审核商品
	 * @param goodsIds
	 * @param status
	 */
	@Override
	public void managerAudit(Long[] goodsIds ,String status){
		for (Long goodsId : goodsIds) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		if (id != null){
			goods.setGoods(goodsMapper.selectByPrimaryKey(id));
			//封装goodsdesc
			goods.setGoodsDesc(goodsDescMapper.selectByPrimaryKey(id));
			//封装itemList数据
			TbItemExample example =  new TbItemExample();
			TbItemExample.Criteria criteria = example.createCriteria();
			criteria.andGoodsIdEqualTo(id);
			List<TbItem> items = itemMapper.selectByExample(example);
			goods.setItemList(items);
		}
		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}

	/**
	 * 运营商删除数据(不删除数据库的数据，改变isDelete字段的值)
	 * @param ids
	 */
	@Override
	public void managerDelete(Long[] ids) {
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setAuditStatus("1");//1代表删除
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
			if(goods!=null){
				if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
					criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 运营商商品审核查询
	 * @param goods
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageResult goodsfFindPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		if(goods!=null){
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
		}
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);
		//筛选出isDelete为1的数据
		List<TbGoods> result = page.getResult();
		for (int i = 0; i <result.size() ; i++) {
			TbGoods tbGoods = result.get(i);
			if ("1".equals(tbGoods.getIsDelete())){
				//移除以删除的数据
				result.remove(i);
			}
		}
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 商品上下架
	 * @param goodsIds
	 * @param putaway
	 */
	@Override
	public void isPutaway(Long[] goodsIds, String putaway) {
		for (Long id : goodsIds) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsMarketable(putaway);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

	//根据goodsId查询TbItem
	@Override
	public List<TbItem> findByGoodsIdAndPutaway(Long[] goodsIds , String status){
		List<Long> goodList = Arrays.asList(goodsIds);//将数组转换成为list集合，便于对元素进行操作
		for (int i = 0;i<goodList.size(); i ++) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodList.get(i));
			if (tbGoods.getIsMarketable() == null || !tbGoods.getIsMarketable().equals(status)){
				goodList.remove(i);
			}
		}
		TbItemExample example =  new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdIn(goodList);
		List<TbItem> tbItems = itemMapper.selectByExample(example);
		return tbItems;
	}

}
