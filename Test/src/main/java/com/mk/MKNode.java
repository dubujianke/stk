package com.mk;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MKNode {

    public static Element getChild(Element element, int idx) {
      return element.child(idx);
    }

    public static Element getChildByContent(Element element, String content) {
        Elements elements = element.children();
        for(int i=0; i<elements.size(); i++) {
            Element aelement = elements.get(i);
            if(aelement.text().contains(content)) {
                return aelement;
            }
        }
        return null;
    }

    public static Element getChildByContent(Elements elements, String content) {
        for(int i=0; i<elements.size(); i++) {
            Element aelement = elements.get(i);
            if(aelement.text().contains(content)) {
                return aelement;
            }
        }
        return null;
    }

    public static List<String> getListStr(Element elements, int from) {
        String txt = elements.text();
        List list = new ArrayList();
        String[] items = txt.split("--");
        for(String item:items) {
            if(item.contains("（")) {
                item = item.substring(0, item.indexOf("（"));
            }
            list.add(item);
        }
        return list;
    }

}
