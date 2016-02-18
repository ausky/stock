/**
 * File Name:    DBUtil.java
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
 * History:      2016/2/9 created by hy.ao
 */
package com.ausky.stock.util;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/9
 * Time: 10:52
 * 文件说明：TODO
 */
public class DBUtil
{
    private static final ThreadLocal<Connection> THREAD_LOCAL_DBUTIL = new ThreadLocal<Connection>();
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final BasicDataSource BASIC_DATA_SOURCE = new BasicDataSource();

    static
    {
        try
        {
            Class.forName( "oracle.jdbc.driver.OracleDriver" );

            BASIC_DATA_SOURCE.setUrl( StockPropertyUtil.getProperty( DB_URL ) );
            BASIC_DATA_SOURCE.setUsername( StockPropertyUtil.getProperty( DB_USER ) );
            BASIC_DATA_SOURCE.setPassword( StockPropertyUtil.getProperty( DB_PASSWORD ) );
        } catch ( Exception ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /**
     * 获取connection
     *
     * @return
     */
    public static Connection getConnection() throws Exception
    {
        Connection connection = THREAD_LOCAL_DBUTIL.get();
        if ( connection == null )
        {
            connection = DriverManager.getConnection( StockPropertyUtil.getProperty( DB_URL ), StockPropertyUtil.getProperty( DB_USER ), StockPropertyUtil.getProperty( DB_PASSWORD ) );
            THREAD_LOCAL_DBUTIL.set( connection );
        }
        return connection;
    }

    /**
     * 获取connection
     *
     * @return
     */
    public static Connection getConnection2() throws Exception
    {
        Connection connection = THREAD_LOCAL_DBUTIL.get();
        if ( connection == null )
        {
            connection = BASIC_DATA_SOURCE.getConnection();

            THREAD_LOCAL_DBUTIL.set( connection );
        }
        return connection;
    }

    /**
     * 关闭 connection
     *
     * @throws Exception
     */
    public static void close() throws Exception
    {
        Connection connection = THREAD_LOCAL_DBUTIL.get();
        if ( connection != null )
        {
            connection.close();
            THREAD_LOCAL_DBUTIL.remove();
        }
    }

}