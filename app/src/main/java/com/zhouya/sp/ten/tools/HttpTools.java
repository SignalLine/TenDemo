package com.zhouya.sp.ten.tools;

import android.content.Intent;
import android.util.Log;

import com.zhouya.sp.ten.ErrorActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/5/30
 */
public class HttpTools {
    private HttpTools(){

    }

    public static byte[] doGet(String url){
        byte[] ret = null;

        if (url != null) {
            HttpURLConnection conn = null;
            InputStream stream = null;
            try {
                URL u = new URL(url);
                conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                conn.connect();

                int stateCode = conn.getResponseCode();

                if(stateCode == 200){
                    stream = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while(true){
                        len = stream.read(buf);
                        if(len == -1){
                            break;
                        }
                        baos.write(buf,0,len);
                    }
                    ret = baos.toByteArray();
                    baos.close();
                }

            } catch (IOException e) {
                Log.d("msg====","HttpTools请求异常");
                e.printStackTrace();
            }finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        return ret;
    }
}
