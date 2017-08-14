/**
 * File Name:    MABean.java
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
 * History:      2016/2/16 created by hy.ao
 */
package com.ausky.stock.bean;

import com.ausky.stock.enums.MAType;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/16
 * Time: 22:42
 * 文件说明：MA 值bean
 */
public class MABean
{
    //价格
    private double ma5price;
    private double ma20price;
    private double ma30price;
    private double ma60price;

    // 交易日
    private String tradeDate;

    public String getTradeDate()
    {
        return tradeDate;
    }

    public void setTradeDate( String tradeDate )
    {
        this.tradeDate = tradeDate;
    }

    public double getMa5price()
    {
        return ma5price;
    }

    public void setMa5price( double ma5price )
    {
        this.ma5price = ma5price;
    }

    public double getMa20price()
    {
        return ma20price;
    }

    public void setMa20price( double ma20price )
    {
        this.ma20price = ma20price;
    }

    public double getMa30price()
    {
        return ma30price;
    }

    public void setMa30price( double ma30price )
    {
        this.ma30price = ma30price;
    }

    public double getMa60price()
    {
        return ma60price;
    }

    public void setMa60price( double ma60price )
    {
        this.ma60price = ma60price;
    }

    public void setPrice( MAType maType, double price )
    {
        switch ( maType )
        {
        case MA5:
            setMa5price( price );
            break;
        case MA20:
            setMa20price( price );
            break;
        case MA30:
            setMa30price( price );
            break;
        default:
            setMa60price( price );
        }
    }

    public double getPrice( MAType maType )
    {
        double result;
        switch ( maType )
        {
        case MA5:
            result = getMa5price();
            break;
        case MA20:
            result = getMa20price();
            break;
        case MA30:
            result = getMa30price();
            break;
        default:
            result = getMa60price();
        }
        return result;
    }
}