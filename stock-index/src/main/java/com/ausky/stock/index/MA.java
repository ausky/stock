/**
 * File Name:    MA.java
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
package com.ausky.stock.index;

import com.ausky.stock.bean.MABean;
import com.ausky.stock.enums.MAType;
import com.ausky.stock.util.DBUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/14
 * Time: 21:59
 * 文件说明：计算均线 Moving Average
 */
public class MA
{
    private static final int THREAD_NUM = 50;

    public static void calcMA() throws Exception
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

            new MAThread( threadStockInfoList ).start();
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
                result.add( queryResult.getString( "stockmarket" ) + queryResult.getString( "stockcode" ) + "F" );
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


    /**
     * 计算均线
     *
     * @param maTypeEnum
     * @param originTable
     * @param targetTable
     * @throws Exception
     */
    public static void calcMA( String originTable, String targetTable, MAType... maTypeEnum ) throws Exception
    {
        List<MABean> allMA = new ArrayList<MABean>();
        try
        {
            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select tradedate,close from " + originTable + " order by  tradedate asc " );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            ResultSet queryResult = queryStatement.executeQuery();

            //用于存放 均线行情值
            int maSize = maTypeEnum.length;
            List<double[]> allMAHq = new ArrayList<double[]>();
            int[] periods = new int[ maSize ];
            int[] indexs = new int[ maSize ];
            for ( int i = 0; i < maSize; i++ )
            {
                int period = maTypeEnum[ i ].getPeriod();
                allMAHq.add( new double[ period ] );
                periods[ i ] = period;
                indexs[ i ] = 0;
            }

            while ( queryResult.next() )
            {
                double todayClose = queryResult.getDouble( "close" );

                MABean tmpBean = new MABean();
                tmpBean.setTradeDate( queryResult.getString( "tradedate" ) );

                for ( int i = 0; i < maSize; i++ )
                {
                    allMAHq.get( i )[ indexs[ i ] ] = todayClose;
                    indexs[ i ] = ( indexs[ i ] + 1 ) % periods[ i ];

                    tmpBean.setPrice( maTypeEnum[ i ], calcAvg( allMAHq.get( i ) ) );
                }
                allMA.add( tmpBean );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            DBUtil.close();
        }

        insertMA( targetTable, allMA, maTypeEnum );
    }

    /**
     * 计算平均值
     *
     * @param prices
     * @return
     */
    private static double calcAvg( double[] prices )
    {
        double sum = 0.0;
        for ( double price : prices )
        {
            sum += price;
        }
        return sum / prices.length;
    }


    /**
     * 插入MA指标
     *
     * @param tableName
     * @param maType
     * @param maBeanList
     * @throws Exception
     */
    private static void insertMA( String tableName, List<MABean> maBeanList, MAType... maType ) throws Exception
    {
        try
        {
            Connection connection = DBUtil.getConnection2();

            StringBuilder _insertSql = new StringBuilder( "insert into  " + tableName + " (tradedate" );
            for ( int i = 0; i < maType.length; i++ )
            {
                _insertSql.append( "," ).append( maType[ i ].getColumnName() );
            }
            _insertSql.append( ") values(:tradedate" );
            for ( int i = 0; i < maType.length; i++ )
            {
                _insertSql.append( "," ).append( " :ma" + i ).append( " " );
            }
            _insertSql.append( ")" );
            PreparedStatement insertStatement = connection.prepareStatement( _insertSql.toString() );

            for ( MABean maBean : maBeanList )
            {
                insertStatement.setString( 1, maBean.getTradeDate() );
                for ( int i = 0; i < maType.length; i++ )
                {
                    insertStatement.setDouble( i + 2, maBean.getPrice( maType[ i ] ) );
                }

                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        } finally

        {
            DBUtil.close();
        }

    }

    /**
     * 计算MA线程
     */
    static class MAThread extends Thread
    {
        private List<String> stockInfos;

        public MAThread( List<String> stockInfos )
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
                    createTable( stockInfo + "MA" );

                    calcMA( stockInfo, stockInfo + "MA", new MAType[]{ MAType.MA5, MAType.MA20, MAType.MA30, MAType.MA60 } );
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
                            "ma5 number,\n" +
                            "ma20 number,\n" +
                            "ma30 number,\n" +
                            "ma60 number\n" +
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