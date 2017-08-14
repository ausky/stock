/**
 * File Name:    TestReadStockMarket.java
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

import com.ausky.stock.log.LogUtil;
import com.ausky.stock.read.ReadStockMarket;
import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 15-7-29
 * Time: 下午6:06
 * 文件说明：TODO
 */
public class TestReadStockMarket extends TestCase
{
    public void testRead()
    {
        try
        {
            ReadStockMarket.readHisStockMarket( null, null );
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        LogUtil.info( 111 );
    }
}
