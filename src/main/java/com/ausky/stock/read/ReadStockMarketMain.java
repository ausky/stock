/**
 * File Name:    ReadStockMarketMain.java
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
 * History:      2016/2/10 created by hy.ao
 */
package com.ausky.stock.read;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/10
 * Time: 14:29
 * 文件说明：TODO
 */
public class ReadStockMarketMain
{
    public static void main( String[] args )
    {
        try
        {
            readNew();
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }


    /**
     * 读取历史数据
     *
     * @throws Exception
     */
    private static void readHis() throws Exception
    {
        ReadStockMarket.readHisStockMarket( null, null );
    }

    /**
     * 读取最新行情
     *
     * @throws Exception
     */
    private static void readNew() throws Exception
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd" );
        String today = simpleDateFormat.format( new Date() );

        ReadStockMarket.TOADY_STR = today;

        ReadStockMarket.readHisStockMarket( today, today );
    }
}