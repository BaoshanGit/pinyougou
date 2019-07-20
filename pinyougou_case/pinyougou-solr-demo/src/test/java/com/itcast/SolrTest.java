package com.itcast;

import com.itcast.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-Solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 增加
     */
    @Test
    public void addTest(){
        TbItem tbItem = new TbItem();
        tbItem.setId(1L);
        tbItem.setBrand("华为");
        tbItem.setCategory("手机");
        tbItem.setPrice(new BigDecimal(2000));
        tbItem.setGoodsId(20L);
        tbItem.setSeller("华为2号连锁店");
        tbItem.setTitle("华为迈特9");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();
    }

    /**
     * 查询
     *
     */
    @Test
    public void selectTest(){
        TbItem byId = solrTemplate.getById(1, TbItem.class);
        System.out.println(byId.getTitle()+"  "+byId.getPrice());
    }

    /**
     * 按住键删除
     *
     */
    @Test
    public void deleTest(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    /**
     * 分页查询
     */

    @Test
    public  void addListTest(){
        List<TbItem>  list = new ArrayList();
        for (int i = 0; i <50 ; i++) {
            TbItem tbItem = new TbItem();
            tbItem.setId(i+1L);
            tbItem.setBrand("华为");
            tbItem.setCategory("手机");
            tbItem.setPrice(new BigDecimal(2000 + i));
            tbItem.setGoodsId(20L + i);
            tbItem.setSeller("华为2号连锁店");
            tbItem.setTitle("华为迈特9"+i);
            list.add(tbItem);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();

    }

    /**
     * 分页查询
     */
    @Test
    public void pageTest(){
        Query query = new SimpleQuery("*:*");
        query.setOffset(10);//开始索引
        query.setRows(20);//每页显示条数
      Page<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println("总页数:"+page.getTotalPages());//总页数
        System.out.println("总记录数:"+page.getTotalElements());
        List<TbItem> content = page.getContent();
        showList(content);
    }


    /**
     * 条件查询
     * @param
     */
    @Test
    public  void searchTest(){
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_title").contains("2");
        query.addCriteria(criteria);
        Page<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println("总页数:"+page.getTotalPages());//总页数
        System.out.println("总记录数:"+page.getTotalElements());
        List<TbItem> content = page.getContent();
        showList(content);
    }

    /**
     * 删除全部
     * @param
     */
    @Test
    public void deleAllTest(){
        SolrDataQuery query =  new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    public void showList( List<TbItem> content){
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getId()+":   "+tbItem.getTitle());
        }
        System.out.println("----结束----");
    }
}
