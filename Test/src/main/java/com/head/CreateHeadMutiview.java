package com.head;

import com.alading.util.HttpsAIUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CreateHeadMutiview {

    public static String token = "Bearer " +
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MjAwMTY4MzcuMzg2MTA4NCwiaWF0IjoxNzE4ODA3MjM3LjM4NjEwODksImlzcyI6Ik1yYXpndW8iLCJkYXRhIjp7InVzZXJOYW1lIjoid3VIaHVPY1ZLRFFxTVJKRW9SSWI3T2lMZUhXalRXT3l2aVlWUXI3WkRoelZmOFBZTjFzUWQzaUIxdGk4cXk4U2tOSjF5bkFZM3N4Mzk4ZVowbVNDNG1NUnRRMUNHY2pJIiwidXNlcklkIjo0NjgsInRpbWVzdGFtcCI6MTcxODgwNzIzNy4zODYxMTA1fX0.BaprKmOQ6VlkIsqpbfmdvCuVSamOOZcqW2LnDrWkvlM"
            ;

//    public static String retriveOrGetShizhi(String code, double v) {
//        return retriveOrGet(code);
//    }

    public static JSONObject retriveOrGet(String code) {
        try {
            String url = "https://human-ok.com/modelapi/upload/getGenerateTaskId?";
            url = url + "token="+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MTkyMzE1MDkuMDU1MTA0LCJpYXQiOjE3MTgwMjE5MDkuMDU1MTA0NywiaXNzIjoiTXJhemd1byIsImRhdGEiOnsidXNlck5hbWUiOiJwQWxBeVVRWjlhTnA5RHM1VmVjME5scmlPTVdJNkhQcm5uVHR1MlVpbE1wT3BveERoQnJzbVN6eHk1VTNXS2pUTjJUbTJEQjltNnpWR1FNQ1dpY0hKVXdVMVE3dEVJOVgiLCJ1c2VySWQiOjI3OCwidGltZXN0YW1wIjoxNzE4MDIxOTA5LjA1NTEwNjR9fQ.Lmi4YrMZIdQf_Jllk8F96olKYXM2IfEf9Mp6Kf_9M1o";
            url = url +"&lng=cn";
            String str = HttpsAIUtils.getGenerateTaskId(url);
            JSONObject jsonObject2 = JSONObject.parseObject(str);
            JSONObject ret = jsonObject2.getJSONObject("data");

            return ret;
        } catch (Exception e) {
        }
        return new JSONObject();
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

    public static void aliyuncsOption(String uploadUrl) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpOptions httpPost = new HttpOptions(uploadUrl);
        InputStream inputStream = null;
        try {
            HttpEntity httpEntity = null;
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Access-Control-Request-Headers", "content-type");
            httpPost.setHeader("Access-Control-Request-Method", "PUT");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Content-Type", "application/zip");
            httpPost.setHeader("Host", "human-ok-save-1304673684.cos.accelerate.myqcloud.com");
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


    public static void aliyuncs(String uploadUrl, String taskId, JSONObject param, List<File> list) {
//        aliyuncsOption(uploadUrl);
        String dir = list.get(0).getParentFile().getAbsolutePath();
        String zip = dir + "\\zip.zip";
        ZipUtil.prss(list, zip);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPost = new HttpPut(uploadUrl);
        InputStream inputStream = null;
        try {
            File file = new File(zip);
            FileEntity fileEntity = new FileEntity(file, ContentType.create("application/zip"));
            HttpEntity httpEntity = null;
            httpPost.setEntity(fileEntity);
//            httpPost.setHeader("PUT", "/ HTTP/1.1");
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Content-Type", "application/zip");
            httpPost.setHeader("Host", "human-ok-save-1304673684.cos.accelerate.myqcloud.com");
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
                httpPost.setHeader("Host", "human-ok-save-1304673684.cos.accelerate.myqcloud.com");
                httpPost.setHeader("Origin", "https://human-ok.com");
                httpPost.setHeader("Referer", "https://human-ok.com/");
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

    public static boolean isTokenOK = true;
    public static void process(List<File> list) throws UnsupportedEncodingException, InterruptedException {
        JSONObject aret = retriveOrGet("");
        String taskId = aret.getString("taskId");
        String uploadUrl = aret.getString("uploadUrl");
        System.out.println(taskId);
        JSONObject signature = new JSONObject();//getAliyunOssSignature("");
        System.out.println(signature);
        aliyuncs(uploadUrl, taskId, signature, list);

        JSONObject ret = webRebuildGenerate(taskId, signature, list.get(0).getAbsolutePath());
        System.out.println(ret);
        if (ret.getString("msg").contains("Insufficient")) {
            int a = 0;
            isTokenOK = false;
            return;
        }

        File tmp = list.get(0);
        String dir = tmp.getParentFile().getAbsolutePath();
        String objDest = dir + "\\headObj.obj";
        String imgDest = dir + "\\headColorWeb.jpg";
        for (int i = 0; i < 10; i++) {
            Thread.currentThread().sleep(3000);
            JSONObject obj = webGenerateProccess(taskId);

            if (obj.getInteger("code") == 200) {
                obj = obj.getJSONObject("data");
                String headObj = obj.getString("headObj");
                String headObjImg = obj.getString("headObjColor");
                System.out.println(headObj);
                System.out.println(headObjImg);
                download(headObj, objDest);
                download(headObjImg, imgDest);
                break;
            }
        }
    }

    public static void mainSingle(List<File> list) throws UnsupportedEncodingException, InterruptedException {
//        String dir = list.get(0).getParentFile().getAbsolutePath();
//        String zip = dir + "\\zip.zip";
//        ZipUtil.prss(list, zip);
//
//        String img = list.get(0).getAbsolutePath();
//        File tmp = new File(img);
//        String dir2 = tmp.getParentFile().getAbsolutePath();
//        ImageUtil.scaleCut(img, dir2 + "\\" + tmp.getName().replace(".jpg", "s.jpg"), 0, 0, 64, 64);
        process(list);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {

        double dbl = 0.25*Math.sqrt(5.0/Math.PI);

//        login("dubujianke01@outlook.com");
        String img = "F:\\heads\\POSE3";
        File tmp = new File(img);
        File[] fs = tmp.listFiles();
        Arrays.sort(fs, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
            }
        });
        for (File file : fs) {
            if(!isTokenOK) {
                System.out.println("------------------------------------>token is error");
                return;
            }
            List<File> list = new ArrayList<>();
            list.add(StringUtils.getFrontFileName(file));
            list.add(StringUtils.getRightFileName(file));
            list.add(StringUtils.getLeftFileName(file));

            File atmp = list.get(0);
            String dir = atmp.getParentFile().getAbsolutePath();
            String objDest = dir + "\\headObj.obj";
            String imgDest = dir + "\\headColorWeb.jpg";
            if (new File(imgDest).exists()) {
                continue;
            }
            mainSingle(list);
            System.out.println("-------------------------------------------------------------------------------------" + file.getAbsolutePath());
            Thread.currentThread().sleep(1000);
        }
    }
}