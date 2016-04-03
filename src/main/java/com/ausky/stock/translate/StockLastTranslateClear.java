/**
 * File Name:    StockLastTranslateClear.java
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
 * History:      2016/2/23 created by hy.ao
 */
package com.ausky.stock.translate;

import com.ausky.stock.util.DBUtil;

import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/23
 * Time: 21:45
 * 文件说明：TODO
 */
public class StockLastTranslateClear
{
    public static void main( String[] args ) throws Exception
    {
        clearTable();
    }

    public static void clearTable() throws Exception
    {
        try
        {
            Connection connection = DBUtil.getConnection2();
            StringBuilder _sql = new StringBuilder( "select table_name from  user_tables where table_name like '%F'" );

            PreparedStatement queryStatement = connection.prepareStatement( _sql.toString() );
            ResultSet queryResult = queryStatement.executeQuery();

            List<String> tableNameList = new ArrayList<String>();
            while ( queryResult.next() )
            {
                tableNameList.add( queryResult.getString( 1 ) );
            }

            String _dropSql = new String( " drop table " );
            for ( String tableNameStr : tableNameList )
            {
                PreparedStatement dropStatement = connection.prepareStatement( _dropSql.toString() + tableNameStr );
                dropStatement.executeUpdate();
                dropStatement.close();
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            DBUtil.close();
        }
    }
}