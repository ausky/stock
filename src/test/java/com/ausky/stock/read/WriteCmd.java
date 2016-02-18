/**
 * File Name:    WriteCmd.java
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
 * History:      2015/10/31 created by hy.ao
 */
package com.ausky.stock.read;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2015/10/31
 * Time: 17:24
 * 文件说明：TODO
 */
public class WriteCmd
{
    public static void main( String[] args )
    {
        File file = new File( "d:\\test.bat" );

        FileOutputStream outputStream = null;

        try
        {
            outputStream = new FileOutputStream( file );
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( outputStream,"gbk") );

            for ( int i = 1; i <= 255; i++ )
            {

            }
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        } finally
        {
            if ( outputStream != null )
            {
                try
                {
                    outputStream.close();
                } catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        }
    }
}