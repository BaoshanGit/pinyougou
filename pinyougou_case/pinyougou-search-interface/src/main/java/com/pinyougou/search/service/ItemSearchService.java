package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService  {

    public Map<String , Object> search(Map searchMap);


    /**
     * 更新solr库
     */
    void updateSolr(List list);
}
