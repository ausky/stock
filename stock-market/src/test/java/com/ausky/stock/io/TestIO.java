/**
 * File Name:    TestIO.java
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
 * History:      2016/2/24 created by hy.ao
 */
package com.ausky.stock.io;

import junit.framework.TestCase;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/24
 * Time: 21:48
 * 文件说明：TODO
 */
public class TestIO extends TestCase
{
    public void testIO() throws Exception
    {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try
        {
            fw = new FileWriter( "f:\\test\\aa\\aa.txt" );
            bw = new BufferedWriter( fw );
        } finally
        {
            if ( fw != null )
            {
                fw.flush();
                fw.close();
            }

            if ( bw != null )
            {
                bw.flush();
                bw.close();
            }
        }
    }
}