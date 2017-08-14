/**
 * File Name:    LogUtil.java
 * <p>
 * File Desc:    TODO
 * <p>
 * Product AB:   TODO
 * <p>
 * Product Name: TODO
 * <p>
 * Module Name:  TODO
 * <p>
 * Module AB:    TODO
 * <p>
 * Author:       敖海样
 * <p>
 * History:      2017/8/12 created by hy.ao
 */
package com.ausky.stock.log;

/**
 * Created with IntelliJ IDEA.
 * User: hy.ao
 * Date: 2017/8/12
 * Time: 12:31
 * 文件说明：TODO
 */
public class LogUtil
{
    public static void info( Object message )
    {
        System.out.println( message );
    }

    public static void error( String message )
    {
        System.out.println( message );
    }

    public static void error( String message, Throwable throwable )
    {
        System.out.println( message );
        if ( throwable != null )
        {
            throwable.printStackTrace();
        }
    }
}