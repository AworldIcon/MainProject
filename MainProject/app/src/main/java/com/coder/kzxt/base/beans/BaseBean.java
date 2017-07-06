package com.coder.kzxt.base.beans;

/**
 * Created by MaShiZhao on 2017/3/20
 */

public class BaseBean
{
    /**
     * code : 200
     * message : ok
     */

    private String code;
    private String message;
    /**
     * currentPage : 当前页数
     * pageSize : 每页记录数
     * pageNum : 总页数
     * totalNum : 总记录数
     */

    private PaginateBean paginate;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public PaginateBean getPaginate() {
        if(paginate==null){
            return new PaginateBean();
        }
        return paginate;
    }

    public void setPaginate(PaginateBean paginate)
    {
        this.paginate = paginate;
    }


    /**
     * currentPage : 当前页数
     * pageSize : 每页记录数
     * pageNum : 总页数
     * totalNum : 总记录数
     */
    public static class PaginateBean
    {
        private int currentPage;
        private int pageSize;
        private int pageNum;
        private int totalNum;

        public int getCurrentPage()
        {
            return currentPage;
        }

        public void setCurrentPage(int currentPage)
        {
            this.currentPage = currentPage;
        }

        public int getPageSize()
        {
            return pageSize;
        }

        public void setPageSize(int pageSize)
        {
            this.pageSize = pageSize;
        }

        public int getPageNum()
        {
            return pageNum;
        }

        public void setPageNum(int pageNum)
        {
            this.pageNum = pageNum;
        }

        public int getTotalNum()
        {
            return totalNum;
        }

        public void setTotalNum(int totalNum)
        {
            this.totalNum = totalNum;
        }
    }
}


