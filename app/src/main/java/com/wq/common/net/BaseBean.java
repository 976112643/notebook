package com.wq.common.net;

/**
 * Created by WQ on 2017/9/22.
 */

public class BaseBean<T> {

    /**
     * status : 1
     * msg : 请求成功
     * page_count : 0
     * page : 1
     * page_size : 10
     */
    public int status;
    public String msg;
    public int page_count;
    public int page;
    public int page_size;
    public T info;

}
