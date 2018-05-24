package com.test.framework.utils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * 从网站扒图片
 * @author blank
 *
 */
public class DownImgUtil {

	//The url of the website. This is just an example
    private static final String webSiteURL = "http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=jsoup%20jar%20%E4%B8%8B%E8%BD%BD";

    //The path of the folder that you want to save the images to
    private static final String folderPath = "D:\\image";

    public static void main(String[] args) {
        try {
            //Connect to the website and get the html
            Document doc = Jsoup.connect(webSiteURL).get();
            //Get all elements with img tag ,
            Elements imgLinks = doc.select("img[src~=.jpg|png|gif]");
           //Elements img = doc.getElementsByTag("img");
            for (Element el : imgLinks) {
                //for each element get the srs url
                String src = el.absUrl("src");
                System.out.println("Image Found!");
                System.out.println("src attribute is : "+src);
                getImages(src);
            }
        } catch (IOException ex) {
            System.err.println("There was an error");
            Logger.getLogger(DownImgUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void getImages(String src) throws IOException {

        //Exctract the name of the image from the src attribute
        int indexname = src.lastIndexOf("/");

        if (indexname == src.length()) {
            src = src.substring(0, indexname);
        }
        indexname = src.lastIndexOf("/");
        String name = src.substring(indexname, src.length());
        System.out.println(name);
        //Open a URL Stream
        URL url = new URL(src);
        InputStream in = url.openStream();
        OutputStream out = new BufferedOutputStream(new FileOutputStream( folderPath+ name));
        for (int b; (b = in.read()) != -1;) {
            out.write(b);
        }
        out.close();
        in.close();

    }
}
