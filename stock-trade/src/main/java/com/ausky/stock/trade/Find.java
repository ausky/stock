/**
 * File Name:    Find.java
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
 * History:      2016/2/14 created by hy.ao
 */
package com.ausky.stock.trade;

import com.ausky.stock.log.LogUtil;
import com.ausky.stock.util.DBUtil;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/14
 * Time: 22:22
 * 文件说明：查找买入卖出点
 */
public class Find
{
    //最少连跌5天
    private static final int MIN_CONTINUS_DAY = 3;
    //单日止跌幅度
    private static final double MAX_DAY_FALL_RATIO = 0.025;
    //相对买入止跌
    private static final double MAX_TOTAL_FALL_RATIO = 0.05;


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
            String tableName = stockMarket.toUpperCase() + stockcode + "F";
            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select tradedate,close,open from " + tableName + " where tradedate >= :tradedate  order by  tradedate asc " );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            queryStatement.setString( 1, startDate );
            ResultSet queryResult = queryStatement.executeQuery();

            //如果连续跌超过5天 反弹则买入
            double lastClose = 0;
            double lastOpen = 0;
            int continuous = 0;

            while ( queryResult.next() )
            {
                double todayClose = queryResult.getDouble( "close" );
                double todayOpen = queryResult.getDouble( "open" );

                if ( todayClose < lastClose )
                {
                    continuous += 1;
                } else
                {
                    if ( continuous >= MIN_CONTINUS_DAY && todayOpen > lastOpen )
                    {
                        LogUtil.info(( "买入日期:" + queryResult.getString( "tradedate" ) + ";买入价格:" + todayClose ));
                        return queryResult.getString( "tradedate" );
                    }
                    continuous = 0;
                }

                lastOpen = todayOpen;
                lastClose = todayClose;
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
            String tableName = stockMarket.toUpperCase() + stockcode + "F";
            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select tradedate,close from " + tableName + " where tradedate >= :tradedate  order by  tradedate asc " );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            queryStatement.setString( 1, startDate );
            ResultSet queryResult = queryStatement.executeQuery();

            //如果跌破昨日的5%则卖出

            Double buyPrice = null;
            double lastClose = 0;

            while ( queryResult.next() )
            {
                double todayClose = queryResult.getDouble( "close" );

                if ( buyPrice == null )
                {
                    buyPrice = todayClose;
                    lastClose = todayClose;
                    continue;
                }

                double day_fall = ( lastClose - todayClose ) / lastClose;
                double total_fall = ( buyPrice - todayClose ) / buyPrice;

                if ( day_fall >= MAX_DAY_FALL_RATIO || total_fall >= MAX_TOTAL_FALL_RATIO )
                {
                    LogUtil.info(( "卖出日期:" + queryResult.getString( "tradedate" ) + ";卖出价格:" + todayClose ));
                    return queryResult.getString( "tradedate" );
                }

                lastClose = todayClose;
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