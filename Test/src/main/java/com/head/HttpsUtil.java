package com.head;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import com.alibaba.fastjson.JSONObject;

public class HttpsUtil {
    public static JSONObject httpRequest(String requestUrl) {
        String requestMethod = "GET";
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setConnectTimeout(3000);
            httpUrlConn.setReadTimeout(3000);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("Content-type", "application/json; charset=utf-8");
            httpUrlConn.setRequestProperty("Accept", "*/*");
            httpUrlConn.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
            httpUrlConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            httpUrlConn.setRequestProperty("Connection", "keep-alive");
            httpUrlConn.setRequestProperty("Host", "human-ok.oss-cn-hongkong.aliyuncs.com");
            httpUrlConn.setRequestProperty("Sec-Fetch-Dest", "empty");
            httpUrlConn.setRequestProperty("Sec-Fetch-Mode", "cors");
            httpUrlConn.setRequestProperty("Sec-Fetch-Site", "same-origin");
            httpUrlConn.setRequestProperty("lng", "cn");
            httpUrlConn.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"");
            httpUrlConn.setRequestProperty("Sec-ch-ua-mobile", "?0");
            httpUrlConn.setRequestProperty("Sec-ch-ua-platform", "\"Windows\"");
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            InputStream inputStream = httpUrlConn.getInputStream();
            copyStream(inputStream, "d:/outHead.jpg");

            httpUrlConn.disconnect();
        } catch (ConnectException ce) {
            System.out.println("Weixin server connection timed out.");
        } catch (Exception e) {
            System.out.println("https request error:{}"+e);
        }
        return jsonObject;
    }

    public static void main(String[] args) {
        httpRequest("https://human-ok.oss-cn-hongkong.aliyuncs.com/rebuildHead/web/2024-05-19/21-39-34-796460-7YkjDJiA/headColorWeb.jpg?OSSAccessKeyId=LTAI5tPoidKwAj6D57XNmVn7&Expires=1716126287&Signature=U2H7vtJxDc%2BOr0GIfOzI0vL3PSs%3D");
    }


    public static void copyStream(InputStream in, String file) throws IOException {
        OutputStream out = new FileOutputStream(new File(file));
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.close();
    }
}