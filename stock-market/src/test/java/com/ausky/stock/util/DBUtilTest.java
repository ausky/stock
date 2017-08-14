/**
 * File Name:    DBUtilTest.java
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

import com.ausky.stock.log.LogUtil;
import junit.framework.TestCase;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/9
 * Time: 21:48
 * 文件说明：TODO
 */
public class DBUtilTest extends TestCase
{
    public void testQuery() throws Exception
    {
        String sql = "select 1 from dual";

        try
        {
            Connection connection = DBUtil.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            ResultSet resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() )
            {
                LogUtil.info( resultSet.getObject( 1 ) );
            }
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        } finally
        {
            DBUtil.close();
        }
    }
    public void testQuery2() throws Exception
    {
        String sql = "select 1 from dual";

        try
        {
            Connection connection = DBUtil.getConnection2();

            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            ResultSet resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() )
            {
                LogUtil.info(( resultSet.getObject( 1 ) ));
            }
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        } finally
        {
            DBUtil.close();
        }
    }
}