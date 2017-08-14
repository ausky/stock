/**
 * File Name:    HttpClientUtil.java
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
 * History:      2016/2/7 created by hy.ao
 */
package com.ausky.stock.util;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.*;
import org.apache.http.params.BasicHttpParams;

import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2016/2/7
 * Time: 13:39
 * 文件说明：httpclient 工具类
 */
public class HttpClientUtil
{

    public static String get( String url ) throws Exception
    {
        StringBuilder _result = new StringBuilder();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet( url );

        HttpResponse response = httpClient.execute( httpget );
        HttpEntity entity = response.getEntity();
        if ( entity != null )
        {
            InputStream instream = entity.getContent();
            int l;
            byte[] tmp = new byte[ 2048 ];
            while ( ( l = instream.read( tmp ) ) != -1 )
            {
                _result.append( new String( tmp, 0, l ) );
            }
        }

        return _result.toString();
    }
}