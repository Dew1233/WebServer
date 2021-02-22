package com.webserver.http;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前类用于保存所有与Http协议相关的规定内容以便重用
 */
public class HttpContext {
    /**
     * 资源后缀名与响应头Content-Type值的对应关系
     * key:资源后缀名
     * value：Content-Type对应的值
     */
    private static Map<String ,String> mimeMapping = new HashMap<>();
    static {
        initMimMapping();
    }

    private static void initMimMapping(){
       /* mimeMapping.put("html","text/html");
        mimeMapping.put("css","text/css");
        mimeMapping.put("js","application/javascript");
        mimeMapping.put("png","image/png");
        mimeMapping.put("gif","image/gif");
        mimeMapping.put("jpg","image/jpeg");*/
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./config/web.xml");
            Element root = doc.getRootElement();
            System.out.println("根标签的名字："+root);
            List<Element> list = root.elements("mime-mapping");
            System.out.println("共有"+list.size()+"mime-mapping标签");

            /**
             *  <extension>中间的文本作为key
             *      <mime-type>中间的文本作为value
             *      存入mimeMapping这个Map完成初始化，初始化后mimeMapping应该有1011个元素
             */
            for (Element emple:list){
                String key = emple.elementText("extension");
                String value = emple.elementText("mime-type");
                mimeMapping.put(key,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据给定的资源后缀名获取到对应的Content-Type的值
     * @param ext
     * @return
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }
}
