/**
 * File Name:    MATest.java
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

import com.ausky.stock.enums.MAType;
import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/14
 * Time: 22:15
 * 文件说明：TODO
 */
public class MATest extends TestCase
{
    public void testMA() throws Exception
    {

        MA.calcMA( "SH600870", "SH600870MA", new MAType[]{ MAType.MA30 } );


    }
}