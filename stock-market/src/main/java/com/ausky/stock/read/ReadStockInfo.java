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

import com.ausky.stock.bean.StockInfo;
import com.ausky.stock.log.LogUtil;
import com.ausky.stock.util.*;
import net.sf.json.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
    public static void readStockInfoFromXueQiu( HttpClientUtil clientUtil ) throws Exception
    {
        readStockSH( clientUtil );

        LogUtil.info( "查询上海股票信息成功" );

        readStockSZ( clientUtil );

        LogUtil.info( "查询深圳股票信息成功" );
    }

    /**
     * 获取上海股票信息
     */
    public static void readStockSH( HttpClientUtil clientUtil ) throws Exception
    {
        String url = "http://xueqiu.com/stock/quote_order.json?order=asc&exchange=CN&stockType=sha&column=symbol&orderBy=amount";

        readStock( clientUtil, url );

    }

    /**
     * 获取深圳股票信息
     */
    public static void readStockSZ( HttpClientUtil clientUtil ) throws Exception
    {

        String url = "http://xueqiu.com/stock/quote_order.json?order=asc&exchange=CN&stockType=sza&column=symbol&orderBy=amount";

        readStock( clientUtil, url );
    }


    /**
     * 获取股票信息
     */
    private static void readStock( HttpClientUtil clientUtil, String url ) throws Exception
    {
        int page = 1;
        int pageSize = 90;

        while ( true )
        {
            String tmpUrl = url + "&page=" + ( page++ ) + "&size=" + pageSize;

            String result = clientUtil.get( tmpUrl );

            JSONObject jsonObject = JSONObject.fromObject( result );

            JSONArray dataArray = jsonObject.getJSONArray( "data" );

            if ( dataArray.size() == 0 )
            {
                break;
            }

            for ( int i = 0; i < dataArray.size(); i++ )
            {
                JSONArray stockDataArray = dataArray.getJSONArray( i );

                String stockInfo = stockDataArray.getString( 0 );

                recordStockInfo( stockInfo.substring( 2 ), stockInfo.substring( 0, 2 ) );
            }
        }

    }


    /**
     * 记录股票信息
     *
     * @param stockCode
     * @param stockMarket
     */
    private static void recordStockInfo( String stockCode, String stockMarket ) throws Exception
    {

        if ( !checkStockExist( stockCode, stockMarket ) )
        {
            insertStockInfo( stockCode, stockMarket );
        }

    }


    /**
     * 插入 股票信息
     *
     * @param stockCode
     * @param stockMarket
     * @throws Exception
     */
    private static void insertStockInfo( String stockCode, String stockMarket ) throws Exception
    {
        StringBuilder _sql = new StringBuilder( "insert into stock(stockCode,stockMarket) values(:stockCode,:stockMarket)" );


        try
        {
            PreparedStatement queryStatement = DBUtil.getConnection2().prepareStatement( _sql.toString() );

            queryStatement.setString( 1, stockCode );
            queryStatement.setString( 2, stockMarket );

            queryStatement.executeQuery();


        } catch ( Exception ex )
        {
            ex.printStackTrace();
        } finally
        {
            DBUtil.close();
        }
    }

    /**
     * 判断 股票信息是否已经存在，存在的话
     *
     * @param stockCode
     * @param stockMarket
     * @return true:存在，false: 不存在
     */
    private static boolean checkStockExist( String stockCode, String stockMarket ) throws Exception
    {
        StringBuilder _sql = new StringBuilder( "select count(1) counts from stock where stockCode = :stockCode and upper(stockMarket) = :stockMarket" );

        try
        {
            PreparedStatement queryStatement = DBUtil.getConnection2().prepareStatement( _sql.toString() );

            queryStatement.setString( 1, stockCode );
            queryStatement.setString( 2, stockMarket.toString() );

            ResultSet queryResult = queryStatement.executeQuery();

            queryResult.next();
            return queryResult.getInt( 1 ) > 0;


        } catch ( Exception ex )
        {
            ex.printStackTrace();
            throw new RuntimeException( ex );
        } finally
        {
            DBUtil.close();
        }
    }
}