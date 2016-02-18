/**
 * File Name:    StockLastTranslate.java
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
 * History:      2016/2/17 created by hy.ao
 */
package com.ausky.stock.translate;

import com.ausky.stock.bean.StockMarket;
import com.ausky.stock.util.DBUtil;

import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/17
 * Time: 22:55
 * 文件说明：股票后复权
 */
public class StockLastTranslate
{
    private static final int THREAD_NUM = 50;

    /**
     * 股票复权
     *
     * @throws Exception
     */
    public static void stockTranslate() throws Exception
    {
        List<String> stockTables = getAllStockTable();

        int everyThreadStockNums = stockTables.size() / THREAD_NUM + 1;

        for ( int i = 0; i < THREAD_NUM; i++ )
        {
            List<String> threadStockInfoList = new ArrayList<String>();

            for ( int j = i * everyThreadStockNums; j < ( i + 1 ) * everyThreadStockNums && j < stockTables.size(); j++ )
            {
                threadStockInfoList.add( stockTables.get( j ) );
            }

            new StockLastTranslateThread( threadStockInfoList ).start();
        }
    }

    /**
     * 获取所有的股票表名
     *
     * @return
     * @throws Exception
     */
    private static List<String> getAllStockTable() throws Exception
    {
        List<String> result = new ArrayList<String>();
        try
        {
            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select stockcode,stockmarket from stock" );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            ResultSet queryResult = queryStatement.executeQuery();


            while ( queryResult.next() )
            {
                result.add( queryResult.getString( "stockmarket" ) + queryResult.getString( "stockcode" ) );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            DBUtil.close();
        }

        return result;
    }

    private static void stockTranslate( String stockTable ) throws Exception
    {

        try
        {
            //获取股票的最新复权日期
            String translateTable = stockTable + "F";
            StringBuilder _sql = new StringBuilder( "select tradedate,close from " + translateTable );

            PreparedStatement queryStatement = DBUtil.getConnection2().prepareStatement( _sql.toString() );

            ResultSet queryResult = queryStatement.executeQuery();

            String startDate = null;
            Double lastClose = null;

            if ( queryResult.next() )
            {
                startDate = queryResult.getString( 1 );
                lastClose = queryResult.getDouble( 2 );
            }

            //获取股票行情信息
            List<StockMarket> stockMarkets = new ArrayList<StockMarket>();

            StringBuilder _queryMarketSql = new StringBuilder( " select * from " );
            _queryMarketSql.append( stockTable ).append( " where 1=1 " );

            if ( startDate != null )
            {
                _queryMarketSql.append( " and tradedate > :tradedate" );
            }

            PreparedStatement _queryMarketStatement = DBUtil.getConnection2().prepareStatement( _queryMarketSql.toString() );
            if ( startDate != null )
            {
                _queryMarketStatement.setString( 1, startDate );
            }
            ResultSet queryMarketResult = _queryMarketStatement.executeQuery();
            while ( queryMarketResult.next() )
            {
                StockMarket tmpMarket = new StockMarket();

                tmpMarket.setTradeDate( queryMarketResult.getString( "tradedate" ) );
                tmpMarket.setOpen( queryMarketResult.getDouble( "open" ) + "" );
                tmpMarket.setClose( queryMarketResult.getDouble( "close" ) + "" );
                tmpMarket.setIncreaseMoney( queryMarketResult.getDouble( "increasemoney" ) + "" );
                tmpMarket.setIncreaseRatio( queryMarketResult.getDouble( "increaseratio" ) + "" );
                tmpMarket.setLow( queryMarketResult.getDouble( "low" ) + "" );
                tmpMarket.setHigh( queryMarketResult.getDouble( "high" ) + "" );
                tmpMarket.setTradingHand( queryMarketResult.getDouble( "tradinghand" ) + "" );
                tmpMarket.setTradingVolume( queryMarketResult.getDouble( "tradingvolume" ) + "" );
                tmpMarket.setChangeRatio( queryMarketResult.getDouble( "changeratio" ) + "" );

                stockMarkets.add( tmpMarket );
            }

            //插入新数据
            StringBuilder _insertSql = new StringBuilder( " insert into " + translateTable + "(tradedate," +
                    "open,close,increasemoney,increaseratio,low,high,tradinghand,tradingvolume,changeratio)" +
                    " values (:tradedate," +
                    ":open,:close,:increasemoney,:increaseratio,:low,:high,:tradinghand,:tradingvolume,:changeratio)" );

            PreparedStatement insertStatement = DBUtil.getConnection2().prepareStatement( _insertSql.toString() );

            for ( StockMarket stockMarketHQ : stockMarkets )
            {
                double translateRatio = lastClose == null ? 1.0 : ( lastClose * ( 1 + Double.valueOf( stockMarketHQ.getIncreaseRatio() ) ) / Double.valueOf( stockMarketHQ.getClose() ) );

                if ( lastClose == null )
                {
                    insertStatement.setString( 1, stockMarketHQ.getTradeDate() );
                    insertStatement.setDouble( 2, Double.valueOf( stockMarketHQ.getOpen() ) * translateRatio );
                    insertStatement.setDouble( 3, Double.valueOf( stockMarketHQ.getClose() ) * translateRatio );
                    insertStatement.setDouble( 4, Double.valueOf( stockMarketHQ.getIncreaseMoney() ) * translateRatio );
                    insertStatement.setDouble( 5, Double.valueOf( stockMarketHQ.getIncreaseRatio() ) );
                    insertStatement.setDouble( 6, Double.valueOf( stockMarketHQ.getLow() ) * translateRatio );
                    insertStatement.setDouble( 7, Double.valueOf( stockMarketHQ.getHigh() ) * translateRatio );
                    insertStatement.setDouble( 8, Double.valueOf( stockMarketHQ.getTradingHand() ) );
                    insertStatement.setDouble( 9, Double.valueOf( stockMarketHQ.getTradingVolume() ) * translateRatio );
                    insertStatement.setDouble( 10, Double.valueOf( stockMarketHQ.getChangeRatio() ) );

                    insertStatement.addBatch();
                }
            }

            insertStatement.executeBatch();

        } catch ( Exception ex )
        {
            ex.printStackTrace();
        } finally
        {
            DBUtil.close();
        }
    }


    /**
     * 计算MA线程
     */
    static class StockLastTranslateThread extends Thread
    {
        private List<String> stockInfos;

        public StockLastTranslateThread( List<String> stockInfos )
        {
            this.stockInfos = stockInfos;
        }

        @Override
        public void run()
        {
            try
            {
                //获取历史行情
                for ( String stockInfo : stockInfos )
                {

                    //为每个股票创建表
                    createTable( stockInfo + "F" );

                    stockTranslate( stockInfo );
                }
                System.out.println( "end!" );
            } catch ( Exception ex )
            {
                ex.printStackTrace();
            }
        }


        private static void createTable( String tablename ) throws Exception
        {
            try
            {
                StringBuilder _sql = new StringBuilder( "select * from user_tables where table_name=:table_name" );

                PreparedStatement queryStatement = DBUtil.getConnection2().prepareStatement( _sql.toString() );
                queryStatement.setString( 1, ( tablename ).toUpperCase() );

                ResultSet queryResult = queryStatement.executeQuery();

                if ( !queryResult.next() )
                {
                    StringBuilder _createSql = new StringBuilder( "create table " + ( tablename ).toUpperCase() + "(\n" +
                            "tradedate varchar2(8),\n" +
                            "open number,\n" +
                            "close number,\n" +
                            "increasemoney number,\n" +
                            "increaseratio number,\n" +
                            "low number,\n" +
                            "high number,\n" +
                            "tradinghand number,\n" +
                            "tradingvolume number,\n" +
                            "changeratio number\n" +
                            ")" );
                    PreparedStatement createStatement = DBUtil.getConnection2().prepareStatement( _createSql.toString() );
                    createStatement.executeUpdate();

                }

            } catch ( Exception ex )
            {
                ex.printStackTrace();
            } finally
            {
                DBUtil.close();
            }
        }
    }
}