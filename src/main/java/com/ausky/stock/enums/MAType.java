/**
 * File Name:    MAType.java
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
package com.ausky.stock.enums;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/14
 * Time: 22:02
 * 文件说明：均线类型
 */
public enum MAType
{
    MA5( 5, "MA5" ), MA20( 20, "MA20" ), MA30( 30, "MA30" ), MA60( 60, "MA60" );

    //周期
    private int period;

    //列名
    private String columnName;

    private MAType( int period, String columnName )
    {
        this.period = period;
        this.columnName = columnName;
    }

    public int getPeriod()
    {
        return period;
    }

    public String getColumnName()
    {
        return columnName;
    }
}