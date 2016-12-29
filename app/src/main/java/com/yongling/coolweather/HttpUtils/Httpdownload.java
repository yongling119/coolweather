package com.yongling.coolweather.HttpUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yongling on 2016/7/19.
 */
public class Httpdownload {

    public static String download(String path) {

        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String data;

        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer))!=-1){
                    byteArrayOutputStream.write(buffer,0,len);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        data = new String(byteArrayOutputStream.toByteArray());
        return data;
    }

}
