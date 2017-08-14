/**
 * File Name:    StockMarket.java
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
 * Time: 11:38
 * 文件说明：股票行情
 */
public class StockMarket
{
    //hq 行情数据  时间 ，开盘，收盘，涨幅额 ，涨跌幅，最低，最高，成交手，成交金额，换手率
    //时间 yyyy-mm-dd
    private String tradeDate;
    //开盘价 xx.xx
    private String open;
    //昨日收盘价 xx.xx
    private String close;
    //涨跌额 xx.xx
    private String increaseMoney;
    //涨跌幅 xx.xx%
    private String increaseRatio;
    //最低 xx.xx
    private String low;
    //最高 xx.xx
    private String high;
    //成交手 xx.xx
    private String tradingHand;
    //成交额
    private String tradingVolume;
    //换手率 xx.xx%
    private String changeRatio;
    //股票代码
    private String stockCode;
    //股票市场
    private String stockMarket;

    public String getTradeDate()
    {
        return tradeDate;
    }

    public void setTradeDate( String tradeDate )
    {
        this.tradeDate = tradeDate;
    }

    public String getOpen()
    {
        return open;
    }

    public void setOpen( String open )
    {
        this.open = open;
    }

    public String getClose()
    {
        return close;
    }

    public void setClose( String close )
    {
        this.close = close;
    }

    public String getIncreaseMoney()
    {
        return increaseMoney;
    }

    public void setIncreaseMoney( String increaseMoney )
    {
        this.increaseMoney = increaseMoney;
    }

    public String getIncreaseRatio()
    {
        return increaseRatio;
    }

    public void setIncreaseRatio( String increaseRatio )
    {
        this.increaseRatio = increaseRatio;
    }

    public String getLow()
    {
        return low;
    }

    public void setLow( String low )
    {
        this.low = low;
    }

    public String getHigh()
    {
        return high;
    }

    public void setHigh( String high )
    {
        this.high = high;
    }

    public String getTradingHand()
    {
        return tradingHand;
    }

    public void setTradingHand( String tradingHand )
    {
        this.tradingHand = tradingHand;
    }

    public String getTradingVolume()
    {
        return tradingVolume;
    }

    public void setTradingVolume( String tradingVolume )
    {
        this.tradingVolume = tradingVolume;
    }

    public String getChangeRatio()
    {
        return "-".equals( changeRatio ) ? "0" : changeRatio;
    }

    public void setChangeRatio( String changeRatio )
    {
        this.changeRatio = changeRatio;
    }

    public String getStockMarket()
    {
        return stockMarket;
    }

    public void setStockMarket( String stockMarket )
    {
        this.stockMarket = stockMarket;
    }

    public String getStockCode()
    {
        return stockCode;
    }

    public void setStockCode( String stockCode )
    {
        this.stockCode = stockCode;
    }
}