/**
 * File Name:    ReadStockInfo.java
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
 * History:      2015/10/27 created by hy.ao
 */
package com.ausky.stock.read;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2015/10/27
 * Time: 23:13
 * 文件说明：TODO
 */
public class ReadStockInfo
{

    /**
     * 从雪球 获取 股票信息
     */
    public static void readStockInfoFromXueQiu() throws Exception
    {
        readStockSH();
        readStockSZ();
    }

    /**
     * 获取上海股票信息
     */
    public static void readStockSH() throws Exception
    {
        int page = 1;
        int pageSize = 30;
        String url = "http://xueqiu.com/hq#type=sha&exchange=CN&order=asc&orderby=amount&page=" + page;

        do
        {
            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet httpget = new HttpGet( url );
            HttpResponse response = httpClient.execute( httpget );

            HttpEntity entity = response.getEntity();
            if ( entity != null )
            {
                InputStream instream = entity.getContent();
                int l;
                byte[] tmp = new byte[ 2048 ];
                while ( ( l = instream.read( tmp ) ) != -1 )
                {
                    System.out.println( new String( tmp, 0, l ) );
                }
            }
        } while ( false );

    }

    /**
     * 获取深圳股票信息
     */
    public static void readStockSZ()
    {
    }


    /**
     * 记录股票信息
     *
     * @param stockCode
     * @param stockType
     */
    public static void recordStockInfo( String stockCode, String stockType )
    {

    }


    /**
     * 创建股票代码
     *
     * @param stockCode
     */
    public static void createStockMarkTableIfNotExist( String stockCode, String stockType )
    {

    }
}