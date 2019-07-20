package com.pinyougou.page.service;

/**
 * 商品详情页接口
 */
public interface ItemPageService {

    /**
     * 生成商品的详情页
     * @param goodsId
     * @return
     */
    public boolean genIetmHtml(Long goodsId);
}
