package com.example.alice.myapplication;

import android.app.Application;
import android.support.v4.content.res.ResourcesCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class myStatic{

    public Map<String,String> map;
    public myStatic()
    {
        map=new HashMap<>();
        map.put("推荐","");
        map.put("国内","http://www.people.com.cn/rss/politics.xml");
        map.put("国际","http://www.people.com.cn/rss/world.xml");
        map.put("经济", "http://www.people.com.cn/rss/finance.xml");
        map.put("文化", "http://www.people.com.cn/rss/culture.xml");
        map.put("体育", "http://www.people.com.cn/rss/sports.xml");
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
    static List<Map<String,String>> newsFavors;
    static List<Map<String,String>> newsReads;
    int haveshown;

    forData(String input)
    {
        type=input;
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

    public void getMore()
    {
        if(haveshown>=newsData.size())
        {
            //...
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
                    System.out.println(input);
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
