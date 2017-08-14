/**
 * File Name:    ReadStockInfoMain.java
 * <p>
 * File Desc:    TODO
 * <p>
 * Product AB:   TODO
 * <p>
 * Product Name: TODO
 * <p>
 * Module Name:  TODO
 * <p>
 * Module AB:    TODO
 * <p>
 * Author:       敖海样
 * <p>
 * History:      2017/8/12 created by hy.ao
 */
package com.ausky.stock.read;

import com.ausky.stock.util.HttpClientUtil;

/**
 * Created with IntelliJ IDEA.
 * User: hy.ao
 * Date: 2017/8/12
 * Time: 11:47
 * 文件说明：TODO
 */
public class ReadStockInfoMain
{
    public static void main( String[] args ) throws Exception
    {
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();

        LoginXueqiu.login( httpClientUtil );

        ReadStockInfo.readStockInfoFromXueQiu( httpClientUtil );
    }
}