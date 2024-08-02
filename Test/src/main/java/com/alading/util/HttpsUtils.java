package com.alading.util;

import java.io.*;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public class HttpsUtils {


    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();

    }


    public static String sendByHttpMinute(JSONObject jsonObject, String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Content-type", "text/javascript; charset=utf-8");
            httpPost.setHeader("Date", "Fri, 11 Aug 2023 14:28:07 GMT");
            httpPost.setHeader("X-Via-Ssl", "ssl.22.sinag1.yf.lb.sinanode.com");
            httpPost.setHeader(":path", "/cn/api/openapi.php/CN_MinlineService.getMinlineData?symbol=sz000063&callback=var%20t1sz000063=&dpc=1");
            httpPost.setHeader(":authority", "quotes.sina.cn");

            httpPost.setHeader("Upgrade-Insecure-Requests", "1");
            httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "no-cache");
            httpPost.setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
            httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

//            httpPost.setEntity(new StringEntity(jsonObject.toString(), Charset.forName("UTF-8")));
            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
//                System.out.println(jsObject);
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


    public static String sendProiceByHttp(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Host", "hq.sinajs.cn");
            httpPost.addHeader("Content-type", "text/*, application/xml");
            httpPost.addHeader("Referer", "https://finance.sina.com.cn/realstock/company/sh600159/nc.shtml");
            httpPost.addHeader("Sec-Ch-Ua", "\"Chromium\";v=\"112\", \"Google Chrome\";v=\"112\", \"Not:A-Brand\";v=\"99\"");
            httpPost.addHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.addHeader("Sec-Fetch-Dest", "script");
            httpPost.addHeader("Sec-Fetch-Mode", "no-cors");
            httpPost.addHeader("Sec-Fetch-Site", "cross-site");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
//            httpPost.setEntity(new StringEntity(jsonObject.toString(), Charset.forName("UTF-8")));
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

    public static String sendByHttp(JSONObject jsonObject, String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Host", "hq.sinajs.cn");
            httpPost.setHeader("Referer", "https://vip.stock.finance.sina.com.cn/");
            httpPost.setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
            httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.setHeader("content-type", "text/*");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");

//            httpPost.setEntity(new StringEntity(jsonObject.toString(), Charset.forName("UTF-8")));
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

    public static String sendByHttpDFCFLines(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "close");
            httpPost.setHeader("Cookie", "qgqp_b_id=75492a8288c8d7d2aea204af46482c89; emshistory=%5B%22%E5%B1%B1%E4%B8%9C%E9%BB%84%E9%87%91%22%5D; HAList=ty-1-688191-%u667A%u6D0B%u521B%u65B0%2Cty-0-000825-%u592A%u94A2%u4E0D%u9508%2Cty-0-003007-%u76F4%u771F%u79D1%u6280%2Cty-0-000063-%u4E2D%u5174%u901A%u8BAF%2Cty-1-600547-%u5C71%u4E1C%u9EC4%u91D1%2Cty-1-688591-N%u6CF0%u51CC%u5FAE%2Cty-1-000001-%u4E0A%u8BC1%u6307%u6570; st_pvi=27914681087523; st_sp=2023-08-27%2007%3A34%3A42; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink");
            httpPost.setHeader("Host", "push2his.eastmoney.com");
            httpPost.setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
            httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.setHeader("content-type", "text/*");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                Header contentEncodingHeader = httpEntity.getContentEncoding();
                if (contentEncodingHeader != null) {
                    HeaderElement[] encodings =contentEncodingHeader.getElements();
                    for (int i = 0; i < encodings.length; i++) {
                        if (encodings[i].getName().equalsIgnoreCase("gzip")) {
                            httpEntity = new GzipDecompressingEntity(httpEntity);
                            break;
                        }
                    }
                    String output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
                    return output;
                }else {
                    String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                    return jsObject;
                }
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

    public static String sendByHttpDFCFMinutes(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");//, br
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Cookie", "qgqp_b_id=75492a8288c8d7d2aea204af46482c89; emshistory=%5B%22%E5%B1%B1%E4%B8%9C%E9%BB%84%E9%87%91%22%5D; HAList=ty-1-688191-%u667A%u6D0B%u521B%u65B0%2Cty-0-000825-%u592A%u94A2%u4E0D%u9508%2Cty-0-003007-%u76F4%u771F%u79D1%u6280%2Cty-0-000063-%u4E2D%u5174%u901A%u8BAF%2Cty-1-600547-%u5C71%u4E1C%u9EC4%u91D1%2Cty-1-688591-N%u6CF0%u51CC%u5FAE%2Cty-1-000001-%u4E0A%u8BC1%u6307%u6570; st_pvi=27914681087523; st_sp=2023-08-27%2007%3A34%3A42; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink");
            httpPost.setHeader("Host", "push2his.eastmoney.com");
            httpPost.setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
            httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.setHeader("content-type", "text/*");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                Header contentEncodingHeader = httpEntity.getContentEncoding();
                if (contentEncodingHeader != null) {
                    HeaderElement[] encodings =contentEncodingHeader.getElements();
                    for (int i = 0; i < encodings.length; i++) {
                        if (encodings[i].getName().equalsIgnoreCase("gzip")) {
                            httpEntity = new GzipDecompressingEntity(httpEntity);
                            break;
                        }
                        if (encodings[i].getName().equalsIgnoreCase("br")) {
                            InputStream inStream = httpEntity.getContent();
                            String str = getBr(inStream);
                            return str;
                        }
                    }
                    String output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
                    return output;
                }else {
                    String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                    return jsObject;
                }
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

    public static String getBr(InputStream inStream) throws IOException {
        BufferedReader bufferedReader=null;
        BufferedReader rd = new BufferedReader(new InputStreamReader(new BrotliCompressorInputStream(inStream)));
        StringBuilder result = new StringBuilder();
        String str = null;
        while((str = bufferedReader.readLine()) != null){
            System.out.println(str);
            result.append(str);
        }
        return result.toString();
    }

    public static String sendByHttpDFCF2(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Cookie", "qgqp_b_id=75492a8288c8d7d2aea204af46482c89; st_pvi=27914681087523; st_sp=2023-08-27%2007%3A34%3A42; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink");
            httpPost.setHeader("Host", "datacenter.eastmoney.com");
            httpPost.setHeader("Sec-Ch-Ua", "\"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"");
            httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.setHeader("content-type", "text/*");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                Header contentEncodingHeader = httpEntity.getContentEncoding();
                if (contentEncodingHeader != null) {
                    HeaderElement[] encodings =contentEncodingHeader.getElements();
                    for (int i = 0; i < encodings.length; i++) {
                        if (encodings[i].getName().equalsIgnoreCase("gzip")) {
                            httpEntity = new GzipDecompressingEntity(httpEntity);
                            break;
                        }
                    }
                    String output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
                    return output;
                }else {
                    String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                    return jsObject;
                }
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

    public static String sendByHttpTHS(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Cookie", "__bid_n=1863b4c6eee48d7f304207; FPTOKEN=sMt7wpi3ZHPxhFl3U+rBQpW3xvj2LSS73Uk786mKYBq3+kVr7PdSrI83DnjwxEgN+vOPZylQv2gkGDekJuG1ZBuQTMPJG3Myuzx/KokIovPpUyW3AbBu6WbAQ4m9RfDGAq6wEOaoSNdTv+gzQ1bmZf7IgXJbUvIbe6BogH6G2Q4T/qziIlwhF4RLAUrpSr2jN2pwwaYJnLq/Vx/q8qKzu+pOPrKVzFA1Vb0NaR9BH3RaSGcXBwbBGNWO66CcC68c6cl2CExOSPLFbAVrRCTU4jo1h7HVqU9LDaNJ8gmHfyAHJgr55KrpHQI8x+Fj+NGa6P2LqHmyGfx+BbuMAgUhw4roZtlgEIMQMXNTiv2KQQ67Q3YuOu0y0ZnQFfYBLPplneZ/S0SOgFM80IrCApjw3Q==|xVDBvZBOw9UZtEM+IxZllu3ssEYgVpL9kVZ19dKVVsY=|10|df7573dbc0114bd2965f9645bf242f17; cid=f6a575aac6de5adb5bf4c6bbe74e04a11687047431; searchGuide=sg; Hm_lvt_722143063e4892925903024537075d0d=1701008902; Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1=1701008902; Hm_lvt_929f8b362150b1f77b477230541dbbc2=1701008902; reviewJump=nojump; usersurvey=1; v=AzFqHcn78COu4lw3l5LxB3SsQLbOHqWYT5NJpBNGLfgXOl8oW261YN_iWXGg");
            httpPost.setHeader("Host", "basic.10jqka.com.cn");
            httpPost.setHeader("Sec-Ch-Ua", "Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122");
            httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.setHeader("content-type", "text/*");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                Header contentEncodingHeader = httpEntity.getContentEncoding();
                if (contentEncodingHeader != null) {
                    HeaderElement[] encodings =contentEncodingHeader.getElements();
                    for (int i = 0; i < encodings.length; i++) {
                        if (encodings[i].getName().equalsIgnoreCase("gzip")) {
                            httpEntity = new GzipDecompressingEntity(httpEntity);
                            break;
                        }
                    }
                    String output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
                    return output;
                }else {
                    String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                    return jsObject;
                }
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

    public static String sendByHttpTHS2(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
//            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Cookie", "Hm_lvt_722143063e4892925903024537075d0d=1712752685; Hm_lpvt_722143063e4892925903024537075d0d=1712752685; log=; Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1=1712752685; Hm_lvt_929f8b362150b1f77b477230541dbbc2=1712752685; Hm_lpvt_929f8b362150b1f77b477230541dbbc2=1712752685; historystock=301326; spversion=20130314; searchGuide=sg; Hm_lpvt_78c58f01938e4d85eaf619eae71b4ed1=1712752872; v=A1z4vGN_VnlMoyIlbEhg_zK2LXEL1QAPgnsU2DZdbPMqB_KvXuXQj9KJ5HeF");
            httpPost.setHeader("Host", "basic.10jqka.com.cn");
            httpPost.setHeader("Sec-Ch-Ua", "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"");
            httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.setHeader("Sec-Fetch-Dest:", "document");
            httpPost.setHeader("Sec-Fetch-Mode:", "navigate");
            httpPost.setHeader("Sec-Fetch-Site:", "none");
            httpPost.setHeader("Sec-Fetch-User:", "?1");
            httpPost.setHeader("Upgrade-Insecure-Requests:", "1");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                Header contentEncodingHeader = httpEntity.getContentEncoding();
                if (contentEncodingHeader != null) {
                    HeaderElement[] encodings =contentEncodingHeader.getElements();
                    for (int i = 0; i < encodings.length; i++) {
                        if (encodings[i].getName().equalsIgnoreCase("gzip")) {
                            httpEntity = new GzipDecompressingEntity(httpEntity);
                            break;
                        }
                    }
                    String output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
                    return output;
                }else {
                    httpEntity = new GzipDecompressingEntity(httpEntity);
                    String output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
                    String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                    return jsObject;
                }
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

    public static String sendByHttpDFCF(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Cookie", "qgqp_b_id=75492a8288c8d7d2aea204af46482c89; st_si=46594678184990; st_asi=delete; HAList=ty-1-000001-%u4E0A%u8BC1%u6307%u6570%2Cty-1-600547-%u5C71%u4E1C%u9EC4%u91D1; st_pvi=27914681087523; st_sp=2023-08-27%2007%3A34%3A42; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink; st_sn=6; st_psi=20230827075903294-113200301324-2971052466");
            httpPost.setHeader("Host", "84.push2.eastmoney.com");
            httpPost.setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
            httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
            httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
            httpPost.setHeader("content-type", "text/*");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
            httpClient = HttpsUtils.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                Header contentEncodingHeader = httpEntity.getContentEncoding();
                if (contentEncodingHeader != null) {
                    HeaderElement[] encodings =contentEncodingHeader.getElements();
                    for (int i = 0; i < encodings.length; i++) {
                        if (encodings[i].getName().equalsIgnoreCase("gzip")) {
                            httpEntity = new GzipDecompressingEntity(httpEntity);
                            break;
                        }
                    }
                    String output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
                    return output;
                }else {
                    String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                    return jsObject;
                }
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

    public static String sendMsgByHttp(String url, String param) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpPost = new HttpGet(url+"?"+param);
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Content-type", "text/*, application/xml");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
//            httpPost.setEntity(new StringEntity(jsonObject.toString(), Charset.forName("UTF-8")));
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

    public static void main(String[] args) throws Exception {
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("idCard","***");
//        jsonObject.put("peopleName","***");
        System.out.println(HttpsUtils.sendByHttp(jsonObject, "https://hq.sinajs.cn/rn=pjx0m&list=sz300817,sz301192,sz300657,sz301488,sz301141,sz300475,sz301186,sz300270,sz301007,sz300008,sz000656,sz002146,sz002426,sz000691,sz000980,sz002654,sz000020,sz002636,sz002703,sz301357,sz000004,sz002976,sz002261,sz002818,sz001309,sz002428,sz002892,sz001380,sz002134,sz002403,sz300128,sz000961,sz000838,sz301163,sz300283,sz301068,sz301282,sz301359,sz300620,sz002527"));
    }

}