/**
 * File Name:    StockPropertyUtil.java
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

import java.net.URL;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/9
 * Time: 10:59
 * 文件说明：读取配置参数
 */
public class StockPropertyUtil
{

    private static final Properties STOCK_PROPERTIES = new Properties();

    static
    {
        URL url = Thread.currentThread().getContextClassLoader().getResource( "com/ausky/stock/config/stockconfig.properties" );
        try
        {
            STOCK_PROPERTIES.load( url.openStream() );
        } catch ( Exception ex )
        {
            ex.printStackTrace();
            throw new RuntimeException( ex );
        }
    }

    public static String getProperty( String propertyName )
    {
        return STOCK_PROPERTIES.getProperty( propertyName );
    }

    public static String getProperty( String propertyName, String defaultVal )
    {
        return STOCK_PROPERTIES.getProperty( propertyName, defaultVal );
    }

}