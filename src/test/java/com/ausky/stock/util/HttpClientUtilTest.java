/**
 * File Name:    HttpClientUtilTest.java
 * <p/>
 * File Desc:    TODO
 * <p/>
 * Product AB:   TODO
 * <p/>
 * Product Name: TODO
 * <p/>
 * Module Name:  TODO
 * <p/>
 * Module AB:    TODO
 * <p/>
 * Author:       敖海样
 * <p/>
 * History:      2016/2/7 created by hy.ao
 */
package com.ausky.stock.util;

import junit.framework.TestCase;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/7
 * Time: 13:44
 * 文件说明：TODO
 */
public class HttpClientUtilTest extends TestCase
{
    public void testGet() throws Exception
    {
        String url = "http://xueqiu.com/stock/quote_order.json";
        Map<String, String> params = new HashMap<String, String>();

        params.put( "page", "1" );
        params.put( "size", "90" );
        params.put( "order", "asc" );
        params.put( "exchange", "CN" );
        params.put( "stockType", "sza" );
        params.put( "column", "symbol" );
        params.put( "orderBy", "amount" );
        params.put( "_", "1454824023081" );

//        HttpClientUtil.get( "http://xueqiu.com/stock/quote_order.json?page=1&size=90&order=asc&exchange=CN&stockType=sza&column=symbol%2Cname%2Ccurrent%2Cchg%2Cpercent%2Clast_close%2Copen%2Chigh%2Clow%2Cvolume%2Camount%2Cmarket_capital%2Cpe_ttm%2Chigh52w%2Clow52w%2Chasexist&orderBy=amount&_=1454824023081" );
        HttpClientUtil.get( url );
    }

    public void testGet2() throws Exception
    {
//        HttpClientUtil.get( "http://xueqiu.com/stock/quote_order.json?page=1&size=190&order=asc&exchange=CN&stockType=sza&column=symbol&orderBy=amount&_=1454824023081" );
    }

    public void testGet3() throws Exception
    {
        HttpClientUtil.get( "http://www.baidu.com/s?wd=q&rsv_spt=1&rsv_iqid=0x8e9ca3d9003171ca&issp=1&f=3&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=0&rsv_sug3=2&rsv_sug1=1&prefixsug=q&rsp=0&rsv_sug7=100&inputT=1878&rsv_sug4=1959" );
    }
}