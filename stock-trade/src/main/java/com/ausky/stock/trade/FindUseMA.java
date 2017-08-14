/**
 * File Name:    FindUseMA.java
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
 * History:      2016/2/16 created by hy.ao
 */
package com.ausky.stock.trade;

import com.ausky.stock.util.DBUtil;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/16
 * Time: 23:47
 * 文件说明：TODO
 */
public class FindUseMA
{
    /**
     * 查找买入点
     *
     * @param stockcode   股票代码
     * @param stockMarket 股票市场
     * @param startDate   开始查找日期
     */
    public static String findBuy( String stockcode, String stockMarket, String startDate ) throws Exception
    {
        try
        {
            String tableName = stockMarket.toUpperCase() + stockcode + "FMA";
            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select tradedate,ma5,ma30 from " + tableName + " where tradedate >= :tradedate  order by  tradedate asc " );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            queryStatement.setString( 1, startDate );
            ResultSet queryResult = queryStatement.executeQuery();


            while ( queryResult.next() )
            {
                double ma5 = queryResult.getDouble( "ma5" );
                double ma30 = queryResult.getDouble( "ma30" );

                if ( ma5 < ma30 )
                {
                    continue;
                } else
                {
                    return queryResult.getString( "tradedate" );
                }
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            DBUtil.close();
        }

        return null;
    }

    /**
     * 查找买入点
     *
     * @param stockcode   股票代码
     * @param stockMarket 股票市场
     * @param startDate   开始查找日期
     */
    public static String findSale( String stockcode, String stockMarket, String startDate ) throws Exception
    {
        try
        {
            String tableName = stockMarket.toUpperCase() + stockcode + "FMA";
            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select tradedate,ma5,ma30 from " + tableName + " where tradedate >= :tradedate  order by  tradedate asc " );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            queryStatement.setString( 1, startDate );
            ResultSet queryResult = queryStatement.executeQuery();


            while ( queryResult.next() )
            {
                double ma5 = queryResult.getDouble( "ma5" );
                double ma30 = queryResult.getDouble( "ma30" );

                if ( ma5 > ma30 )
                {
                    continue;
                } else
                {
                    return queryResult.getString( "tradedate" );
                }
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            DBUtil.close();
        }

        return null;
    }
}