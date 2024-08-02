package com.alading.data;

import com.alading.util.HttpsUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GetPrice {

    public static String sendByHttp(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Host", "hq.sinajs.cn");
            httpPost.addHeader("Referer", "https://finance.sina.com.cn/realstock/company/sh600547/nc.shtml");
            httpPost.addHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
            httpPost.addHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.addHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.addHeader("Sec-Fetch-Dest", "script");
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");

            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                return jsObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpResponse.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void log(String msg, String... vals) {
        System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException {
        String content = sendByHttp("https://hq.sinajs.cn/rn=1710776893472&list=s_sz002400,s_sz000705,s_sz000063,s_sh000001,s_sh601019,s_sh600113,s_sh600905,s_sz399001,s_sz000725,s_sz000100,s_sh601919,s_sz000651,s_sz002594,s_sz000615,s_sh601318,s_sh600519,s_sh600171,s_sz002475");
        log("%s", content);
    }

}
