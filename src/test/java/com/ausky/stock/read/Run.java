/**
 * File Name:    Run.java
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
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 敖海样
 * Date: 2015/10/31
 * Time: 17:48
 * 文件说明：测试本地ip
 */
public class Run
{
    public static boolean find = false;

    public static void main( String[] args ) throws Exception
    {
        for ( int i = 0; i < 255; i++ )
        {
            List<String> commands = new ArrayList<String>();
            commands.add( "netsh interface ip set address name=\"本地连接\" source=static address=192.168.30." + i + " mask=255.255.255.0 gateway=192.168.30.1" );
            commands.add( "ping -n 1 192.168.1.1" );

            exec( commands );
            if ( find )
            {
                System.out.println( "success" );
                System.out.println( "success" );
                System.out.println( "success" );
                System.out.println( i );
            } else
            {
                System.out.println( i + " faild" );
            }
        }

    }


    public static void exec( List<String> commands ) throws Exception
    {
        StringBuilder _command = new StringBuilder( "cmd /c " );

        for ( int i = 0; i < commands.size(); i++ )
        {
            if ( i != 0 )
            {
                _command.append( " && " );
            }

            _command.append( commands.get( i ) );
        }

        Process proc = Runtime.getRuntime().exec( _command.toString() );

        final Byte[] lock = new Byte[ 0 ];
        CommandReturnInputStream errorIn = new CommandReturnInputStream( proc.getErrorStream(), CommandReturnInputStream.ERROR_TYPE, lock );
        CommandReturnInputStream infoIn = new CommandReturnInputStream( proc.getInputStream(), CommandReturnInputStream.INFO_TYPE, lock );
        errorIn.start();
        infoIn.start();

        proc.waitFor();
    }
}

class CommandReturnInputStream extends Thread
{
    // 大数组长度
    private static final int BIG_SIZE = 2048;
    // 小数组长度
    private static final int SMALL_SIZE = 1024;
    //错误信息类型
    public static final String ERROR_TYPE = "error";
    //正常信息类型
    public static final String INFO_TYPE = "info";

    //输入流
    private InputStream in;
    //流类型
    private String type;

    //锁
    private Object lock;

    public CommandReturnInputStream( InputStream in, String type, Object lock )
    {
        this.in = in;
        this.type = type;
        this.lock = lock;
    }

    @Override
    public synchronized void start()
    {
        super.start();
    }

    @Override
    public void run()
    {
        int length = -1;


        byte[] big_buffer = new byte[ BIG_SIZE ];
        byte[] small_buffer = new byte[ SMALL_SIZE ];
        byte[] buffer = new byte[ 10240 ];


        try
        {
            if ( INFO_TYPE.equals( type ) )
            {
                in.mark( BIG_SIZE + 1 );
                while ( ( length = in.read( big_buffer ) ) > 0 )
                {

                    length = in.read( small_buffer );
                    String str = new String( small_buffer, 0, length, "gbk" );

                    if ( str.contains( "0%" ) )
                    {
                        Run.find = true;
                    }

                }
            } else
            {
                while ( ( length = in.read( buffer ) ) > 0 )
                {
                    length = in.read( small_buffer );
                    String str = new String( small_buffer, 0, length, "gbk" );

                    if ( str.contains( "0%" ) )
                    {
                        Run.find = true;
                    }
                }
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
}
