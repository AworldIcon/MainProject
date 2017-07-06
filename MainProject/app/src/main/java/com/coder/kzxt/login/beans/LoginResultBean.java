package com.coder.kzxt.login.beans;

/**
 * Created by MaShiZhao on 2017/3/20
 */

public class LoginResultBean
{

    /**
     * code : 200
     * message : ok
     * item : {"auto_login_secret_key":"自动登录秘钥"}
     */

    private String code;
    private String message;
    /**
     * auto_login_secret_key : 自动登录秘钥
     */
    private Paginate paginate;

    private ItemBean item;

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

    public ItemBean getItem()
    {
        return item;
    }

    public void setItem(ItemBean item)
    {
        this.item = item;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public static class ItemBean
    {
        private String auto_login_secret_key;

        public String getAuto_login_secret_key()
        {
            return auto_login_secret_key;
        }

        public void setAuto_login_secret_key(String auto_login_secret_key)
        {
            this.auto_login_secret_key = auto_login_secret_key;
        }
    }

    public static class Paginate{
        private int currentPage;
        private int pageSize;
        private int pageNum;
        private int totalNum;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }

}
