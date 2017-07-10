package com.taotao.search.bean;

import java.util.List;

/**
 * Created by Ellen on 2017/7/10.
 */
public class SearchResult {
    private List<?> list;
    private long total;

    public SearchResult(List<?> list, long total) {
        this.list = list;
        this.total = total;
    }

    public SearchResult() {
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
