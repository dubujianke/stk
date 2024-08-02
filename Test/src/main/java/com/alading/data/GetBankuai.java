package com.alading.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alading.MKNode;
import com.alading.tool.stock.Log;
import com.alading.util.FileUtil;
import com.alading.util.JsonUtil;
import com.alading.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A simple example, used on the jsoup website.
 */
public class GetBankuai {

    public static String get(String code) {
        if(code.length()>6) {
            code = code.substring(2);
        }
        String str = FileUtil.read(Config.ROOT + code + ".txt");
        return str;
    }

    public static void save(String code) throws IOException {
        code = code.substring(2);
        String str = FileUtil.read(Config.ROOT+code+".txt");
        if(!StringUtil.isNull(str)) {
            return;
        }
        try {
            Document doc = Jsoup.connect("http://basic.10jqka.com.cn/"+code+"/field.html").get();
            Elements newsHeadlines = doc.select("p.threecate");
            Element aElement = MKNode.getChildByContent(newsHeadlines, "三级行业分类");
            Element spamSlement = aElement.select("span").first();
            List<String> list = MKNode.getListStr(spamSlement, 1);
            JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
            String astr = array.toJSONString();
//            FileUtil.write(Config.ROOT+code+".txt", astr);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> read(String code) throws IOException {
        String str = FileUtil.read(Config.ROOT+code+".txt");
        JSONArray jsonObject = JSONObject.parseArray(str);
        List<String> list = JsonUtil.getList(jsonObject);
        return list;
    }


    private static void log(String msg, String... vals) {
        //System.out.println(String.format(msg, vals));
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\new_tdx\\T0001\\export\\");
        File fs[] = file.listFiles();
        for (File item : fs) {
            if (item.toString().contains("000756")) {
                int a = 0;
                Log.log(item.toString());
            }
            String code = item.getName().toLowerCase().replace("#", "").replace(".txt", "");
            GetBankuai.save(code);
//            List<String> list = GetBankuai.read(code);
//            AllModel.add("sh600547", list);
//            log(""+AllModel.nameMap);
        }

    }
}
