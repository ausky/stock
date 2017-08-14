/**
 * File Name:    AnalysisStockUtilTest.java
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

import com.ausky.stock.log.LogUtil;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/7
 * Time: 22:25
 * 文件说明：TODO
 */
public class AnalysisStockUtilTest extends TestCase
{
    public void testAnalysis()
    {
        // 不使用连接池 时间 ：耗时：109576
        //  使用连接池的 耗时：4156
        Date now = new Date();
        AnalysisStockUtil.analysisStockInfo();
        LogUtil.info( "耗时：" + ( new Date().getTime() - now.getTime() ) );
    }
}