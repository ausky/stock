/**
 * File Name:    ReadStockMarket.java
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
 * History:      15-7-29 created by hy.ao
 */
package com.ausky.stock.read;

import com.ausky.stock.bean.*;
import com.ausky.stock.log.LogUtil;
import com.ausky.stock.util.*;
import net.sf.json.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 15-7-29
 * Time: 下午5:58
 * 文件说明：读取股票行情
 */
public class ReadStockMarket
{
    private static final int THREAD_NUM = 50;
    //今日日期
    public static String TOADY_STR = new SimpleDateFormat( "yyyyMMdd" ).format( new Date() );

    /**
     * 获取历史行情
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     *                  //     * @param period    行情类型， 空：日  w  周  m 月
     * @return
     * @throws Exception
     */
    public static void readHisStockMarket( String startTime, String endTime ) throws Exception
    {
        if ( StringUtils.isBlank( startTime ) )
        {
            startTime = "20010101";
        }

        if ( StringUtils.isBlank( endTime ) )
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd" );
            endTime = simpleDateFormat.format( new Date() );
        }

        //查询股票信息
        List<StockInfo> stockInfoList = getStockInfoList();

        int everyThreadStockNums = stockInfoList.size() / THREAD_NUM + 1;

        for ( int i = 0; i < THREAD_NUM; i++ )
        {
            List<StockInfo> threadStockInfoList = new ArrayList<StockInfo>();

            for ( int j = i * everyThreadStockNums; j < ( i + 1 ) * everyThreadStockNums && j < stockInfoList.size(); j++ )
            {
                threadStockInfoList.add( stockInfoList.get( j ) );
            }

            new ReadStockMarketThread( threadStockInfoList, startTime, endTime ).start();
        }
    }

    /**
     * 分析
     *
     * @param stockHQStr
     */
    private static void analysisStockHQ( String stockHQStr, String stockCode, String stockMarket ) throws Exception
    {
        JSONArray jsonArray = JSONArray.fromObject( stockHQStr );

        JSONObject jsonObject = jsonArray.getJSONObject( 0 );
        JSONArray hqListJSONArray = jsonObject.getJSONArray( "hq" );

        for ( int i = 0; hqListJSONArray != null && i < hqListJSONArray.size(); i++ )
        {
            analysisStock( hqListJSONArray.getJSONArray( i ), stockCode, stockMarket );
        }
    }

    /**
     * //hq 行情数据  时间 ，开盘，昨收，涨幅额 ，涨跌幅，最低，最高，成交手，成交金额，换手率
     *
     * @param hqArray
     */
    private static void analysisStock( JSONArray hqArray, String stockCode, String stockMarket ) throws Exception
    {
        StockMarket stockMarketHQ = new StockMarket();

        stockMarketHQ.setTradeDate( ( String ) hqArray.get( 0 ) );
        stockMarketHQ.setOpen( ( String ) hqArray.get( 1 ) );
        stockMarketHQ.setClose( ( String ) hqArray.get( 2 ) );
        stockMarketHQ.setIncreaseMoney( ( String ) hqArray.get( 3 ) );
        stockMarketHQ.setIncreaseRatio( ( String ) hqArray.get( 4 ) );
        stockMarketHQ.setLow( ( String ) hqArray.get( 5 ) );
        stockMarketHQ.setHigh( ( String ) hqArray.get( 6 ) );
        stockMarketHQ.setTradingHand( ( String ) hqArray.get( 7 ) );
        stockMarketHQ.setTradingVolume( ( String ) hqArray.get( 8 ) );
        stockMarketHQ.setChangeRatio( ( String ) hqArray.get( 9 ) );
        stockMarketHQ.setStockCode( stockCode );
        stockMarketHQ.setStockMarket( stockMarket );

        insertStockMarket( stockMarketHQ );
    }

    /**
     * 插入行情信息
     *
     * @throws Exception
     */
    private static void insertStockMarket( StockMarket stockMarketHQ ) throws Exception
    {
        try
        {
            String tableName = stockMarketHQ.getStockMarket() + stockMarketHQ.getStockCode();

            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select * from " + tableName + " where tradedate = :tradedate" );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            queryStatement.setString( 1, stockMarketHQ.getTradeDate().replaceAll( "-", "" ) );
            ResultSet queryResult = queryStatement.executeQuery();

            if ( queryResult.next() && TOADY_STR.equals( stockMarketHQ.getTradeDate().replaceAll( "-", "" ) ) )
            {
                //更新当日数据
                StringBuilder _updateSql = new StringBuilder( "update " + tableName + "set open=:open,close=:close,increasemoney=:increasemoney," +
                        "increaseratio=:increaseratio,low=:low,high=:high,tradinghand=:tradinghand,tradingvolume=:tradingvolume,changeratio=:changeratio" +
                        " where tradedate = :tradedate" );
                PreparedStatement updateStatement = connection.prepareStatement( _updateSql.toString() );
                updateStatement.setString( 10, stockMarketHQ.getTradeDate().replaceAll( "-", "" ) );
                updateStatement.setDouble( 1, Double.valueOf( stockMarketHQ.getOpen() ) );
                updateStatement.setDouble( 2, Double.valueOf( stockMarketHQ.getClose() ) );
                updateStatement.setDouble( 3, Double.valueOf( stockMarketHQ.getIncreaseMoney() ) );
                updateStatement.setDouble( 4, Double.valueOf( stockMarketHQ.getIncreaseRatio().replace( "%", "" ) ) / 100 );
                updateStatement.setDouble( 5, Double.valueOf( stockMarketHQ.getLow() ) );
                updateStatement.setDouble( 6, Double.valueOf( stockMarketHQ.getHigh() ) );
                updateStatement.setDouble( 7, Double.valueOf( stockMarketHQ.getTradingHand() ) );
                updateStatement.setDouble( 8, Double.valueOf( stockMarketHQ.getTradingVolume() ) * 10000 );
                updateStatement.setDouble( 9, Double.valueOf( stockMarketHQ.getChangeRatio().replace( "%", "" ) ) / 100 );
                updateStatement.executeUpdate();
            } else
            {

                //插入新数据
                StringBuilder _insertSql = new StringBuilder( " insert into " + tableName + "(tradedate," +
                        "open,close,increasemoney,increaseratio,low,high,tradinghand,tradingvolume,changeratio)" +
                        " values (:tradedate," +
                        ":open,:close,:increasemoney,:increaseratio,:low,:high,:tradinghand,:tradingvolume,:changeratio)" );

                PreparedStatement insertStatement = connection.prepareStatement( _insertSql.toString() );
                insertStatement.setString( 1, stockMarketHQ.getTradeDate().replaceAll( "-", "" ) );
                insertStatement.setDouble( 2, Double.valueOf( stockMarketHQ.getOpen() ) );
                insertStatement.setDouble( 3, Double.valueOf( stockMarketHQ.getClose() ) );
                insertStatement.setDouble( 4, Double.valueOf( stockMarketHQ.getIncreaseMoney() ) );
                insertStatement.setDouble( 5, Double.valueOf( stockMarketHQ.getIncreaseRatio().replace( "%", "" ) ) / 100 );
                insertStatement.setDouble( 6, Double.valueOf( stockMarketHQ.getLow() ) );
                insertStatement.setDouble( 7, Double.valueOf( stockMarketHQ.getHigh() ) );
                insertStatement.setDouble( 8, Double.valueOf( stockMarketHQ.getTradingHand() ) );
                insertStatement.setDouble( 9, Double.valueOf( stockMarketHQ.getTradingVolume() ) * 10000 );
                insertStatement.setDouble( 10, Double.valueOf( stockMarketHQ.getChangeRatio().replace( "%", "" ) ) / 100 );

                insertStatement.executeUpdate();
            }

        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            DBUtil.close();
        }
    }


    /**
     * 获取股票信息
     *
     * @return
     * @throws Exception
     */
    private static List<StockInfo> getStockInfoList() throws Exception
    {
        StringBuilder _sql = new StringBuilder( "select *\n" +
                "  from stock a\n" +
                " where not exists (select 1\n" +
                "          from finishedtable b\n" +
                "         where b.tablename = (a.stockmarket || a.stockcode)\n" +
                "           and b.finisheddate = :finisheddate)" );
        List<StockInfo> result = new ArrayList<StockInfo>();

        try
        {
            PreparedStatement queryStatement = DBUtil.getConnection2().prepareStatement( _sql.toString() );

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd" );
            queryStatement.setString( 1, simpleDateFormat.format( new Date() ) );

            ResultSet queryResult = queryStatement.executeQuery();

            while ( queryResult.next() )
            {
                StockInfo tmpStock = new StockInfo();

                tmpStock.setStockCode( queryResult.getString( "stockcode" ) );
                tmpStock.setStockMarket( queryResult.getString( "stockmarket" ) );
                tmpStock.setStockName( queryResult.getString( "stockname" ) );

                result.add( tmpStock );
            }
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        } finally
        {
            DBUtil.close();
        }
        return result;
    }

    /**
     * 插入完成记录
     *
     * @param tableName
     * @throws Exception
     */
    private static void insertFinishTable( String tableName ) throws Exception
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd" );
        try
        {
            StringBuilder _insertSql = new StringBuilder( " insert into finishedtable(tablename,finisheddate)" +
                    " values (:tableName,:finisheddate)" );

            PreparedStatement insertStatement = DBUtil.getConnection2().prepareStatement( _insertSql.toString() );
            insertStatement.setString( 1, tableName );
            insertStatement.setString( 2, simpleDateFormat.format( new Date() ) );

            insertStatement.executeUpdate();
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        } finally
        {
            DBUtil.close();
        }
    }

    static class ReadStockMarketThread extends Thread
    {
        private List<StockInfo> stockInfos;
        private String startTime;
        private String endTime;

        public ReadStockMarketThread( List<StockInfo> stockInfos, String startTime, String endTime )
        {
            this.stockInfos = stockInfos;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public void run()
        {

//        HttpGet httpget = new HttpGet( "http://q.stock.sohu.com/hisHq?code=zs_000001&start=20030101&end=20150423" );
//        [{"status":0,"hq":[["2015-10-23","3.60","3.35","0.34","0.10%","3.60","3.69","6460782","237465.94","6.76%"],"code":"cn_000630"}]
            // status 0 表示成功
            //hq 行情数据  时间 ，开盘，昨收，涨幅额 ，涨跌幅，最低，最高，成交手，成交金额，换手率


//        http://q.stock.sohu.com/hisHq?code=cn_000630&start=20150306&end=20151024&stat=1&order=D&period=d&rt=jsonp&r=random()
//callback([{"status":0,"hq":[["2015-10-23","3.60","3.69","0.34","10.15%","3.60","3.69","6460782","237465.94","6.76%"],["2015-03-06","16.55","16.93","0.41","2.48%","16.42","17.30","491576","83303.65","3.46%"]],"code":"cn_000630","stat":["累计:","2015-03-06至2015-10-23","-12.83","-77.66%",3.6,17.3,6952358,320769.59,"10.22%"]}])
            // status 0 表示成功  hq 行情： 时间，开盘，收盘，涨跌额，涨跌幅，最低，最高，成交手，成交额，换手率

            try
            {
                //获取历史行情
                for ( StockInfo stockInfo : stockInfos )
                {

                    //为每个股票创建表
                    createTable( stockInfo.getStockCode(), stockInfo.getStockMarket() );

                    int period = 100000;
                    int times = ( Integer.parseInt( endTime ) - Integer.parseInt( startTime ) ) / period + 1;

                    for ( int i = 0; i < times; i++ )
                    {
                        //http://q.stock.sohu.com/hisHq?code=zs_000001&start=20030101&end=20150423
                        StringBuilder _url = new StringBuilder( "http://q.stock.sohu.com/hisHq?period=d&code=" );

//            _url.append( stockInfo.getStockMarket().toLowerCase() ).append( "_" ).append( stockInfo.getStockCode() );
                        _url.append( "cn_" ).append( stockInfo.getStockCode() );
                        _url.append( "&start=" );
                        _url.append( period * i + Integer.parseInt( startTime ) );
                        _url.append( "&end=" );
                        _url.append( ( i + 1 ) * period + Integer.parseInt( startTime ) > Integer.parseInt( endTime ) ? endTime : "" + ( ( i + 1 ) * period + Integer.parseInt( startTime ) ) );

                        String stockHQ = HttpClientUtil.getInstance().get( _url.toString() );

                        if ( "{}".equals( stockHQ.trim() ) )
                        {
                            continue;
                        }
                        analysisStockHQ( stockHQ, stockInfo.getStockCode(), stockInfo.getStockMarket() );
                    }

                    //完成
                    insertFinishTable( ( stockInfo.getStockMarket() + stockInfo.getStockCode() ).toUpperCase() );
                }
                LogUtil.info( "end!" );
            } catch ( Exception ex )
            {
                ex.printStackTrace();
            }
        }

        private static void createTable( String stockCode, String stockMarket ) throws Exception
        {
            try
            {
                StringBuilder _sql = new StringBuilder( "select * from user_tables where table_name=:table_name" );

                PreparedStatement queryStatement = DBUtil.getConnection2().prepareStatement( _sql.toString() );
                queryStatement.setString( 1, ( stockMarket + stockCode ).toUpperCase() );

                ResultSet queryResult = queryStatement.executeQuery();

                if ( !queryResult.next() )
                {
                    StringBuilder _createSql = new StringBuilder( "create table " + ( stockMarket + stockCode ).toUpperCase() + "(\n" +
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
