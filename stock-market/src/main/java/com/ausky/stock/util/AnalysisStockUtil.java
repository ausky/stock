/**
 * File Name:    AnalysisStockUtil.java
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

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/7
 * Time: 17:11
 * 文件说明：TODO
 */
public class AnalysisStockUtil
{
    private static final String END_STR = "#end";

    public static void analysisStockInfo()
    {
        URL url = Thread.currentThread().getContextClassLoader().getResource( "com/ausky/stock/hq" );
        File stockHQDir = new File( url.getFile() );

        File[] stockFileList = stockHQDir.listFiles();

        for ( File stockFile : stockFileList )
        {
            readStockHQFile( stockFile );
        }
    }

    /**
     * 读取行情文件
     *
     * @param hqFile
     */
    private static void readStockHQFile( File hqFile )
    {
        FileInputStream fileInputStream = null;

        try
        {
            fileInputStream = new FileInputStream( hqFile );
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( fileInputStream ) );

            //行情在第一行
            String hqStr = bufferedReader.readLine();

            while ( !( StringUtils.isNotBlank( hqStr ) && END_STR.equals( hqStr.trim() ) ) )
            {

                if ( StringUtils.isNotBlank( hqStr ) )
                {

                    JSONObject jsonObject = JSONObject.fromObject( hqStr );

                    String data = jsonObject.getString( "data" ).replaceAll( "[\\[\\]\"]", "" );

                    StringTokenizer stringTokenizer = new StringTokenizer( data, "\r\t," );
                    while ( stringTokenizer.hasMoreElements() )
                    {
                        String stockCode = ( String ) stringTokenizer.nextElement();
                        insertOrUpdateStock( stockCode );
                    }

                }
                hqStr = bufferedReader.readLine();
            }

        } catch ( Exception ex )
        {
            ex.printStackTrace();
        } finally
        {
            if ( fileInputStream != null )
            {
                try
                {
                    fileInputStream.close();
                } catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 插入股票信息
     *
     * @param stockCode
     * @throws Exception
     */
    private static void insertOrUpdateStock( String stockCode ) throws Exception
    {

        try
        {
            String code = stockCode.substring( 2 );
            String market = stockCode.substring( 0, 2 );
            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select * from stock where stockcode = :stockcode " );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            queryStatement.setString( 1, code );
            ResultSet queryResult = queryStatement.executeQuery();

            if ( !queryResult.next() )
            {
                StringBuilder _updateSql = new StringBuilder( " insert into stock values (:stockcode,:stockmarket,:stockname)" );

                PreparedStatement updateStatement = connection.prepareStatement( _updateSql.toString() );
                updateStatement.setString( 1, code );
                updateStatement.setString( 2, market.toUpperCase() );
                updateStatement.setString( 3, "" );

                updateStatement.executeUpdate();
            }

        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            DBUtil.close();
        }

    }

}