package com.example.alice.myapplication;

import android.app.Application;
import android.support.v4.content.res.ResourcesCompat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class myStatic{

    static public Map<String,String> map;
    static public ArrayList<String> input=new ArrayList<>();
    public myStatic()
    {
        map=new HashMap<>();
        map.put("国内新闻", "http://www.people.com.cn/rss/politics.xml");
        map.put("国际新闻", "http://www.people.com.cn/rss/world.xml");
        map.put("经济新闻", "http://www.people.com.cn/rss/finance.xml");
        map.put("文化新闻", "http://www.people.com.cn/rss/culture.xml");
        map.put("体育新闻", "http://www.people.com.cn/rss/sports.xml");
        map.put("法制新闻",  "http://www.people.com.cn/rss/legal.xml");
        map.put("传媒新闻", "http://www.people.com.cn/rss/media.xml");
        map.put("娱乐新闻", "http://www.people.com.cn/rss/ent.xml");
        map.put("观点新闻", "http://www.people.com.cn/rss/opinion.xml");
        //input.add("国内新闻");
        input.add("国际新闻");
        input.add("经济新闻");
        input.add("文化新闻");
        input.add("体育新闻");
        input.add("法制新闻");
        input.add("传媒新闻");
        input.add("娱乐新闻");
        input.add("观点新闻");
    }
}



public class forData {
    String type;
    List<Map<String,String>> newsData;
    String patternTitle;
    Pattern pTitle;
    String patternUrl;
    Pattern pUrl;
    String patternTime;
    Pattern pTime;
    String patternSource;
    Pattern pSource;
    static myStatic urlMap;
    static List<Map<String,String>> newsFavors=new LinkedList<>();
    static List<Map<String,String>> newsReads=new LinkedList<>();
    static HashSet<String> hashFavor=new HashSet<>();
    static HashSet<String> hashReads=new HashSet<>();
    int haveshown;
    static List<Map<String,String>> newsTitles=new LinkedList<>();


    forData()
    {
        urlMap=new myStatic();
    }
    forData(String i)
    {
        type=i;
        newsData=new ArrayList<>();
        patternTitle="<title><!\\[CDATA\\[(.+)\\]\\]><\\/title>";
        pTitle=Pattern.compile(patternTitle);
        patternUrl="<link>(.+)<\\/link>";
        pUrl=Pattern.compile(patternUrl);
        patternTime="<pubDate>([0-9-]+)<\\/pubDate>";
        pTime=Pattern.compile(patternTime);
        patternSource="<author>(.+)<\\/author>";
        pSource=Pattern.compile(patternSource);
        haveshown=0;
        urlMap=new myStatic();
        Refresh();


    }
    public void Refresh()
    {
        newsData.clear();
        dealUrl(urlMap.map.get(type));

    }

    static public void forTitle()
    {
        String start="<div class=\"w1000 focus_img clearfix\">";
        String forurl="<li><a href=\"(.+?)\" target=\"_blank\">";
        Pattern geturl=Pattern.compile(forurl);
        String fortitle="target='_blank'>(.+?)</a></div>";
        Pattern gettitle=Pattern.compile(fortitle);
        String forpic="<img src=\"(.+?)\" width=";
        Pattern getpic=Pattern.compile(forpic);
        try
        {
            String input="";
            URL mine=new URL("http://www.people.com.cn");
            BufferedReader in=new BufferedReader(new InputStreamReader(mine.openStream(),"GBK"));
            String inputline;
            while((inputline=in.readLine())!=null)
            {
                input+=inputline+"\n";
            }
            int index=input.indexOf(start);
            input=input.substring(index);
            Matcher m;
            Map<String,String> one=new HashMap<>();
            for(int i=0;i<5;i++)
            {
                m=geturl.matcher(input);
                if(m.find())
                one.put("url",m.group(1));
                m=getpic.matcher(input);
                if(m.find())
                    one.put("pic","http://www.people.com.cn"+m.group(1));
                m=gettitle.matcher(input);
                if(m.find())
                {
                    one.put("title",m.group(1));
                    input=input.substring(m.end());
                    newsTitles.add(one);
                }
            }
            in.close();
            return;

        }
        catch (Exception e)
        {

        }
    }

    private void dealUrl(String u)
    {
                try{
                    String input="";
                    URL mine=new URL(u);
                    BufferedReader in=new BufferedReader(new InputStreamReader(mine.openStream(),"UTF-8"));
                    String inputline;
                    while((inputline=in.readLine())!=null)
                    {
                        input+=inputline+"\n";
                    }
                    Matcher m;
                    for(int i=0;i<3;i++)
                    {
                        m=pTitle.matcher(input);
                        if(m.find())
                            input=input.substring(m.end(), input.length());
                    }
                    m=pTitle.matcher(input);
                    while(m.find())
                    {
                        Map<String,String> one=new HashMap<>();
                        one.put("title",m.group(1));

                        m=pUrl.matcher(input);
                        if(m.find())
                            one.put("url",m.group(1));
                        else
                            break;

                        m=pTime.matcher(input);
                        if(m.find())
                            one.put("time",m.group(1));
                        else
                            break;

                        m=pSource.matcher(input);
                        if(m.find())
                        {
                            one.put("source",m.group(1));
                            input=input.substring(m.end(), input.length());
                        }
                        else
                            break;
                        one.put("read","0");
                        m=pTitle.matcher(input);
                        newsData.add(one);
                    }
                    in.close();

                    return;
                }
                catch (Exception e)
                {
                    System.out.print(e);
                }




    }

}
