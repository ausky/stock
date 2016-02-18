/**
 * File Name:    StockPropertyUtilTest.java
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

import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/9
 * Time: 21:43
 * 文件说明：TODO
 */
public class StockPropertyUtilTest extends TestCase
{
    public void testGetProperty()
    {
        System.out.println(StockPropertyUtil.getProperty( "db.url" ));
        System.out.println(StockPropertyUtil.getProperty( "db.user" ));
        System.out.println(StockPropertyUtil.getProperty( "db.password" ));
    }
}