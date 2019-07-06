package com.pinyougou.entity;
/**
 * 分页实体类
 */

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {

    private long total; //总条数
    private List rwos; //结果集

    public PageResult() {
    }

    public PageResult(long total, List rwos) {
        this.total = total;
        this.rwos = rwos;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRwos() {
        return rwos;
    }

    public void setRwos(List rwos) {
        this.rwos = rwos;
    }
}
