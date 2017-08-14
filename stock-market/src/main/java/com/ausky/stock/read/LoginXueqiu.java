/**
 * File Name:    LoginXueqiu.java
 * <p>
 * File Desc:    TODO
 * <p>
 * Product AB:   TODO
 * <p>
 * Product Name: TODO
 * <p>
 * Module Name:  TODO
 * <p>
 * Module AB:    TODO
 * <p>
 * Author:       敖海样
 * <p>
 * History:      2017/8/12 created by hy.ao
 */
package com.ausky.stock.read;

import com.ausky.stock.log.LogUtil;
import com.ausky.stock.util.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hy.ao
 * Date: 2017/8/12
 * Time: 11:37
 * 文件说明：登陆雪球
 */
public class LoginXueqiu
{
    /**
     * 登陆雪球，并将 session-cookie 保存下来
     *
     * @param clientUtil
     * @throws Exception
     */
    public static void login( HttpClientUtil clientUtil ) throws Exception
    {
        StringBuilder paramStr = new StringBuilder().append( "username" ).append( "=" ).append( StockPropertyUtil.getProperty( "xueqiu.userName" ) ).
                append( "&" ).append( "password" ).append( "=" ).append( StockPropertyUtil.getProperty( "xueqiu.password" ) );


        clientUtil.post( StockPropertyUtil.getProperty( "xueqiu.loginUrl" ), paramStr.toString() );

        LogUtil.info( "登陆雪球成功" );
    }

}