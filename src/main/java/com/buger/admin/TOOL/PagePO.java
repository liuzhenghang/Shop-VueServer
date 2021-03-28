package com.buger.admin.TOOL;


//分页插件的返回类，包含数据总量，当前页，总页数
public class PagePO {
    private int count;
    private int page;
    private int limit;
    private Object data;

    public PagePO(int count, Object data) {
        this.count = count;
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
