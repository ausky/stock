/**
 * File Name:    StockInfo.java
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
 * History:      2016/2/10 created by hy.ao
 */
package com.ausky.stock.bean;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/10
 * Time: 11:01
 * 文件说明：股票信息
 */
public class StockInfo
{
    //股票代码
    private String stockCode;
    //股票市场
    private String stockMarket;
    //股票名称
    private String stockName;

    public String getStockCode()
    {
        return stockCode;
    }

    public void setStockCode( String stockCode )
    {
        this.stockCode = stockCode;
    }

    public String getStockMarket()
    {
        return stockMarket;
    }

    public void setStockMarket( String stockMarket )
    {
        this.stockMarket = stockMarket;
    }

    public String getStockName()
    {
        return stockName;
    }

    public void setStockName( String stockName )
    {
        this.stockName = stockName;
    }
}