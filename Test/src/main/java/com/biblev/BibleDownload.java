package com.biblev;

import com.alading.util.HttpsAIUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.head.ImageUtil;
import com.head.StringUtils;
import com.head.ZipUtil;
import com.huaien.core.util.FileManager;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BibleDownload {

    public static void download(String url, String file) {
        try {
            CloseableHttpClient httpClient = null;
            CloseableHttpResponse httpResponse = null;
            try {

                HttpHost proxy = new HttpHost("127.0.0.1", 7897);
                RequestConfig config = RequestConfig.custom()
                        .setProxy(proxy)
                        .build();

                HttpGet httpPost = new HttpGet(url);
                httpPost.setConfig(config);
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
                httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("Sec-Fetch-Dest", "empty");
                httpPost.setHeader("Sec-Fetch-Mode", "cors");
                httpPost.setHeader("Sec-Fetch-Site", "same-origin");
                httpPost.setHeader("sec-ch-ua", "\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"");
                httpPost.setHeader("Sec-ch-ua-mobile", "?0");
                httpPost.setHeader("Sec-ch-ua-platform", "\"Windows\"");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
                httpClient = HttpsAIUtils.createSSLClientDefault();
                httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    Header contentEncodingHeader = httpEntity.getContentEncoding();
                    if (contentEncodingHeader != null) {
                        HeaderElement[] encodings = contentEncodingHeader.getElements();
                        for (int i = 0; i < encodings.length; i++) {
                            if (encodings[i].getName().equalsIgnoreCase("gzip")) {
                                httpEntity = new GzipDecompressingEntity(httpEntity);
                                break;
                            }
                        }
                    } else {
                    }
                    InputStream inputStream = httpEntity.getContent();
                    try (BufferedInputStream in = new BufferedInputStream(inputStream);
                         FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        byte dataBuffer[] = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    httpResponse.close();
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 2722; i++) {
            File file = new File("D:\\stk\\stk\\biblev\\" + i + "\\" + i + ".json");
            if (!file.exists()) {
                continue;
            }
            String content = FileManager.read(file.getAbsolutePath());
            JSONObject jsonObject = JSONObject.parseObject(content);
            String url = "https:" + jsonObject.getJSONObject("offline").getString("url");
            String dest = "D:\\stk\\stk\\biblev2\\" + i;
            FileManager.mkdirs(dest);
            dest = dest + "\\" + i + ".zip";
            if(new File(dest).exists()) {
                continue;
            }
            System.out.println(dest);
            Thread.currentThread().sleep(300);
            download(url, dest);
        }
    }

}