package com.head;

import com.alading.util.HttpsAIUtils;
import com.alibaba.fastjson.JSONObject;
import com.huaien.core.util.FileManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CreateHead {

    public static String retriveOrGetShizhi(String code, double v) {
        return retriveOrGet(code);
    }

    public static String retriveOrGet(String code) {
        try {
            String url = "https://human-ok.com/modelapi/upload/getGenerateTaskId";
            String str = HttpsAIUtils.getGenerateTaskId(url);
            JSONObject jsonObject2 = JSONObject.parseObject(str);
            JSONObject ret = jsonObject2.getJSONObject("data");
            String taskId = ret.getString("taskId");
            return taskId;
        } catch (Exception e) {
        }
        return "";
    }

    public static JSONObject getAliyunOssSignature(String code) {
        try {
            String url = "https://human-ok.com/modelapi/upload/getAliyunOssSignature";
            String str = HttpsAIUtils.getGenerateTaskId(url);
            JSONObject ret = JSONObject.parseObject(str);
            return ret;
        } catch (Exception e) {
        }
        return null;
    }

    public static void aliyuncs(String taskId, JSONObject param, String img) {
        File tmp = new File(img);
        String dir = tmp.getParentFile().getAbsolutePath();
        String zip = dir + "\\zip.zip";
        ZipUtil.prs(img, zip);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://human-ok.oss-cn-hongkong.aliyuncs.com/");
        InputStream inputStream = null;
        try {
            String accessid = param.getString("accessid");
            String callback = param.getString("callback");
            String key = param.getString("dir") + taskId.replace("--", "/") + "/zip.zip";
            String policy = param.getString("policy");
            String signature = param.getString("signature");
            String success_action_status = "200";
            final String BOUNDARY = "----WebKitFormBoundaryQojGDHvcrkuaKXBM";
            File file = new File(zip);
            FileBody fileBody = new FileBody(file, ContentType.MULTIPART_FORM_DATA, "zip.zip");
            HttpEntity httpEntity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .setBoundary(BOUNDARY)
                    .addTextBody("OSSAccessKeyId", accessid)
                    .addTextBody("callback", callback)
                    .addTextBody("key", key)
                    .addTextBody("policy", policy)
                    .addTextBody("signature", signature)
                    .addTextBody("success_action_status", success_action_status)
                    .addPart("file", fileBody)
                    .build();
            httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            httpPost.setEntity(httpEntity);
            httpPost.setHeader("POST", "/ HTTP/1.1");
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            httpPost.setHeader("Host", "human-ok.oss-cn-hongkong.aliyuncs.com");
            httpPost.setHeader("Origin", "https://human-ok.com");
            httpPost.setHeader("Referer", "https://human-ok.com");
            httpPost.setHeader("Sec-Fetch-Dest", "empty");
            httpPost.setHeader("Sec-Fetch-Mode", "cors");
            httpPost.setHeader("Sec-Fetch-Site", "cross-site");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
            httpPost.setHeader("lng", "cn");
            httpPost.setHeader("sec-ch-ua", "\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"");
            httpPost.setHeader("Sec-ch-ua-mobile", "?0");
            httpPost.setHeader("Sec-ch-ua-platform", "\"Windows\"");

            HttpResponse response = client.execute(httpPost);
            httpEntity = response.getEntity();
            if (httpEntity != null) {
                inputStream = httpEntity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Consts.UTF_8));
                String body = null;
                while ((body = br.readLine()) != null) {
                    System.out.println(body);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static JSONObject webRebuildGenerate(String taskId, JSONObject param, String img) {
        File tmp = new File(img);
        String dir = tmp.getParentFile().getAbsolutePath();
        ImageUtil.scaleCut(img, dir + "\\" + tmp.getName().replace(".jpg", "s.jpg"), 0, 0, 64, 64);
        img = dir + "\\" + tmp.getName().replace(".jpg", "s.jpg");

        CloseableHttpClient client = HttpClients.createDefault();
        String params = String.format("taskId=%s&symmetryType=0&useSmooth=0&keepMouth=0&haveBrow=0&fillType=0&topologyType=0&withHair=0&haveFringeHair=0", taskId);
        HttpPost httpPost = new HttpPost("https://human-ok.com/modelapi/upload/webRebuildGenerate?" + params);
        InputStream inputStream = null;
        try {
            final String BOUNDARY = "------WebKitFormBoundaryg53DJkKilxD23hce";
            File file = new File(img);
            FileBody fileBody = new FileBody(file, ContentType.IMAGE_JPEG, tmp.getName());
            HttpEntity httpEntity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .setBoundary(BOUNDARY)
                    .addPart("file", fileBody)
                    .build();
            httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            httpPost.setEntity(httpEntity);
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Authorization", CreateHeadMutiview.token);
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            httpPost.setHeader("Host", "human-ok.com");
            httpPost.setHeader("Origin", "https://human-ok.com");
            httpPost.setHeader("Referer", "https://human-ok.com/model.html");
            httpPost.setHeader("Sec-Fetch-Dest", "empty");
            httpPost.setHeader("Sec-Fetch-Mode", "cors");
            httpPost.setHeader("Sec-Fetch-Site", "same-origin");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
            httpPost.setHeader("lng", "cn");
            httpPost.setHeader("sec-ch-ua", "\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"");

            httpPost.setHeader("Sec-ch-ua-mobile", "?0");
            httpPost.setHeader("Sec-ch-ua-platform", "\"Windows\"");

            HttpResponse response = client.execute(httpPost);
            httpEntity = response.getEntity();
            String output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
            JSONObject ret = JSONObject.parseObject(output);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
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

    public static void download(String url, String file) {
        try {
            CloseableHttpClient httpClient = null;
            CloseableHttpResponse httpResponse = null;
            try {
                HttpGet httpPost = new HttpGet(url);

                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
                httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("Host", "human-ok.oss-cn-hongkong.aliyuncs.com");
                httpPost.setHeader("Sec-Fetch-Dest", "empty");
                httpPost.setHeader("Sec-Fetch-Mode", "cors");
                httpPost.setHeader("Sec-Fetch-Site", "same-origin");
                httpPost.setHeader("lng", "cn");
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
                    InputStream in = httpEntity.getContent();
                    copyStream(in, file);
                } else {
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

    public static JSONObject webGenerateProccess(String taskId) {
        try {
            String params = String.format("taskId=%s", taskId);
            String url = "https://human-ok.com/modelapi/upload/webGenerateProccess?" + params;
            CloseableHttpClient httpClient = null;
            CloseableHttpResponse httpResponse = null;
            String output = "";
            try {
                HttpGet httpPost = new HttpGet(url);


                httpPost.addHeader("Content-type", "application/json; charset=utf-8");
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
                httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                httpPost.setHeader("Authorization", CreateHeadMutiview.token);
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("Host", "human-ok.com");
                httpPost.setHeader("Referer", "https://human-ok.com/model.html");
                httpPost.setHeader("Sec-Fetch-Dest", "empty");
                httpPost.setHeader("Sec-Fetch-Mode", "cors");
                httpPost.setHeader("Sec-Fetch-Site", "same-origin");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
                httpPost.setHeader("lng", "cn");
                httpPost.setHeader("sec-ch-ua", "\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"");
                httpPost.setHeader("Sec-ch-ua-mobile", "?0");
                httpPost.setHeader("Sec-ch-ua-platform", "\"Windows\"");
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
                        output = EntityUtils.toString(httpEntity, Charset.forName("UTF-8").name());
                    } else {
                        output = EntityUtils.toString(httpEntity, "UTF-8");
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
            JSONObject ret = JSONObject.parseObject(output);
            return ret;
        } catch (Exception e) {
        }
        return null;
    }


    public static String getFieName(File file) {
        String fName = file.getName();
        fName = fName.substring(0, fName.lastIndexOf("."));
        return fName;
    }

    public static String getScaleFieName(File file) {
        String fName = file.getName();
        return getFieName(file) + "_" + fName.substring(fName.lastIndexOf("."));
    }


    public static void process(String img) throws UnsupportedEncodingException, InterruptedException {
        String taskId = retriveOrGet("");
        System.out.println(taskId);
        JSONObject signature = getAliyunOssSignature("");
        System.out.println(signature);
        aliyuncs(taskId, signature, img);

        JSONObject ret = webRebuildGenerate(taskId, signature, img);
        System.out.println(ret);

        File tmp = new File(img);
        String dir = tmp.getParentFile().getAbsolutePath();
        for (int i = 0; i < 5; i++) {
            Thread.currentThread().sleep(3000);
            JSONObject obj = webGenerateProccess(taskId);
            System.out.println(obj);
            if (obj.getInteger("code") == 200) {
                obj = obj.getJSONObject("data");
                String headObj = obj.getString("headObj");
                String headObjImg = obj.getString("headObjColor");
                download(headObj, dir + "\\headObj.obj");
                download(headObjImg, dir + "\\headColorWeb.jpg");
                break;
            }
        }
    }

    public static void mainSingle(String img) throws UnsupportedEncodingException, InterruptedException {
        File tmp = new File(img);
        String dir = tmp.getParentFile().getAbsolutePath();
        String fileName = getScaleFieName(tmp);
        BufferedImage bfImage = ImageUtil.scale(img, 512);
        try {
            String result = dir + "\\" + fileName;
            File imgCutFile = new File(result);
            ImageIO.write(bfImage, "JPEG", imgCutFile);
            img = result;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ZipUtil.prs("d:/zip.zip", img);
//        ImageUtil.scaleCut(img, "d:/out.jpg", 0, 0, 64, 64);
        process(img);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        String img = "F:\\heads\\imgs";
        File tmp = new File(img);
        File[] fs = tmp.listFiles();
        Arrays.sort(fs, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
            }
        });
        for (File file : fs) {
            if(file.listFiles().length>1) {
                continue;
            }
            File afile = file.listFiles()[0];
            if (afile.getName().endsWith("jpg") || afile.getName().endsWith("jpeg")) {
                String aimg = afile.getAbsolutePath();
                System.out.println(aimg);
                mainSingle(aimg);
                System.out.println("---------------------------------------------------------"+aimg);

                Thread.currentThread().sleep(1000);
            }
        }
    }
}