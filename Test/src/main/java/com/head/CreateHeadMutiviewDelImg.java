package com.head;

import com.alading.util.HttpsAIUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
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

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CreateHeadMutiviewDelImg {

    public static void main2(String[] args) throws UnsupportedEncodingException, InterruptedException {
        String img = "F:\\heads\\POSE2";
        File tmp = new File(img);
        File[] fs = tmp.listFiles();
        Arrays.sort(fs, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
            }
        });
        for (File file : fs) {
            for (File afile : file.listFiles()) {
                String name = afile.getName();
                if(name.contains("PM+00")) {
                    continue;
                }
                if(name.contains("PM+45")) {
                    continue;
                }
                if(name.contains("PM-45")) {
                    continue;
                }
                afile.delete();
            }
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
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
            for (File afile : file.listFiles()) {
                String name = afile.getName();
                afile.delete();
            }
        }
    }
}