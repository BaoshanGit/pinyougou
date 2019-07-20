package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;

@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Map<String, Object> search(Map searchMap) {
        Map<String,Object> map = new HashMap<>();
       /* //对数据进行分词查询
        Query query = new SimpleQuery("*:*");
        Criteria criteria =  new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        Page<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        map.put("rows",page.getContent());//将查询的当前页的数据存入map集合中*/
       //按关键字查询（高亮显示）
       map.putAll(searchList(searchMap));
       //根据关键字查询商品分类
        List categoryList = secachCategoryList(searchMap);
        map.put("categoryList",categoryList);
       //查询品牌和规格
        String category = (String) searchMap.get("category");
        if (!"".equals(category)){
            //如果有分类，就按照分类进行查询
            map.putAll( searchBrandAndSpec(category));
        }else {
            //如果没有就按照第一个进行查询
            if (categoryList.size() >0){
                map.putAll( searchBrandAndSpec((String)categoryList.get(0)));
            }        }
        return map;
    }

    /**
     * 设置高亮(包含分词查询)
     */
    public Map searchList(Map searchMap){
        //创建map集合
        Map map = new HashMap();
        HighlightQuery query =  new SimpleHighlightQuery();
        //设置高亮显示
        HighlightOptions highlightOptions =  new HighlightOptions().addField("item_title");//设置高亮域
        highlightOptions.setSimplePrefix("<em style='color:red'>");//设置高亮前缀
        highlightOptions.setSimplePostfix("</em>");//设置高亮后缀
        query.setHighlightOptions(highlightOptions);
        //根据关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //按分类进行查询
        if (!"".equals(searchMap.get("category"))){
            FilterQuery filterQuery =  new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //按品牌进行查询
        if (!"".equals(searchMap.get("brand"))){
            FilterQuery filterQuery =  new SimpleFilterQuery();
            Criteria filterCriteria  = new Criteria("item_brand").is(searchMap.get("brand"));
           filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //按规格进行查询
        if (searchMap.get("spec") != null){
            Map<String,String> specMap = (Map) searchMap.get("spec");
            //遍历
            Set<String> keySet = specMap.keySet();
            for (String key : keySet) {
                FilterQuery filterQuery =  new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //按价格进行查询
        if (!"".equals(searchMap.get("price"))){
            //获取price的值，并进行切割
            String  price = (String) searchMap.get("price");
            String[] prices = price.split("-");
            //过滤查询
            if (!"0".equals(prices[0])){//起始不为零 ，查询该值之后的内容
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!"*".equals(prices[1])){//最大值不是无穷大，查询该值之前的内容
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //按价格进行排序(status : -1 不排序 0: 按降序 1：按升序)
        Integer status = (Integer) searchMap.get("status");//获取状态码
        String statusField = (String) searchMap.get("statusField");//获取排序字段
        if (status != -1 && statusField != null){//是否有排序字段
            if (status == 0){
                //降序
                Sort sort = new Sort(Sort.Direction.DESC,"item_"+statusField);
                query.addSort(sort);

            }else  if (status == 1){

                //升序
                Sort sort = new Sort(Sort.Direction.ASC,"item_"+statusField);
                query.addSort(sort);
            }
        }

        //设置分页
        Integer  pageNo = (Integer) searchMap.get("pageNo");//获取当前页
        if (pageNo == null){
            //设置默认值
            pageNo = 1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");//获取每页显示条数
        if (pageSize == null){
            //设置默认值
            pageSize=  10;
        }
        query.setOffset((pageNo-1)*pageSize);//设置从第几条开始查询
        query.setRows(pageSize);//设置每页查询条数

        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //设置高亮入口
        List<HighlightEntry<TbItem>> highlighted = highlightPage.getHighlighted();
        for (HighlightEntry<TbItem> entry : highlighted) {
            //获取实体类
            TbItem entity = entry.getEntity();
            if (entry.getHighlights() != null && entry.getHighlights().size() > 0){
                //设置title的值(高亮结果)
                entity.setTitle(entry.getHighlights().get(0).getSnipplets().get(0));
            }
        }
        int totalPages = highlightPage.getTotalPages();//总页数
        long totalElements = highlightPage.getTotalElements();//总条数
        map.put("totalPages",totalPages);
        map.put("totalElements",totalElements);
        map.put("rows",highlightPage.getContent());
        return map;
    }

    //设置商品分类
    public List secachCategoryList(Map searchMap){
        List list =  new ArrayList();
        //条件查询
        Query query =  new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");//设置分组选项
        query.setGroupOptions(groupOptions);
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        GroupResult<TbItem> category = page.getGroupResult("item_category");//获得分组结果集
        //获得分组入口页
        Page<GroupEntry<TbItem>> entries = category.getGroupEntries();
        List<GroupEntry<TbItem>> content = entries.getContent();//获取分组入口结果集
        for (GroupEntry<TbItem> groupEntry : content) {
            //将结果value封装到list集合中返回
            String value = groupEntry.getGroupValue();
            list.add(value);
        }
        return  list;
    }

    /**
     * 查询品牌和规格
     */

    public Map searchBrandAndSpec(String category){
       Map map  = new HashMap();
        /**
         * 思路分析
         *   1.通过category 在redis中itemCate键中获取，相应的typeID
         *   2.通过typeID分别在键brandList和specList键中获取品牌和规格信息并将信息封装到map集合中返回
         */

        //获取typeID
        Long typeId = (Long) redisTemplate.boundHashOps("itemCate").get(category);
        if (typeId != null){
            //获取brandList
           List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
           map.put("brandList",brandList);

           //获取specList
           List specList= (List) redisTemplate.boundHashOps("specList").get(typeId);
           map.put("specList",specList);
        }
        return map;
    }


    //更新solr库

    @Override
    public void updateSolr(List list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

}
