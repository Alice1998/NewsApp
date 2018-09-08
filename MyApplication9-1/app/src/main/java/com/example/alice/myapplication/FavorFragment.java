package com.example.alice.myapplication;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FavorFragment extends Fragment implements RefreshListView.LoadListener {
    private RefreshListView listview;
    private List<Map<String,String>> list;
    private SimpleAdapter adapter;

    private String[] theme={"理论文章美在可信又可爱","跨省异地就医直接结算人次突破80万","多地公布蓝天保卫战具体行动计划","专家详解小学入学年龄划定问题：作统一规定不现实","多地出台国有景区降价方案 157个景区门票已降价或免费开放"};
    private String[] author={"人民网","新华社","北京青年报","腾讯","新浪"};
    private String[] time={"2018-09-05","2018-09-05","2018-09-05","2018-09-05","2018-09-05"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment2,container,false);
        //setContentView(R.layout.activity_main);
        list=new ArrayList<>();
        for(int j=0;j<4;j++)
            for (int i=1;i<5;i++){
                Map<String,String> map=new HashMap<>();
                map.put("title",theme[i]);
                map.put("source",author[i]);
                map.put("time",time[i]);
                list.add(map);
            }
        adapter=new SimpleAdapter(getActivity(),list,R.layout.news_item,new String[]{"title","source","time"},new int[]{R.id.title,R.id.source,R.id.datetime});
        listview=(RefreshListView)view.findViewById(R.id.list_view);
        listview.setInterface(this);//loadlistener
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new MyListener());
        return view;
    }


    @Override
    public void onLoad() {
        //设置三秒延迟模仿延时获取数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载数据
                for (int i=1;i<5;i++){

                    Map<String,String> map=new HashMap<>();
                    map.put("title",theme[i]);
                    map.put("source",author[i]);
                    map.put("time",time[i]);
                    list.add(map);
                }
                //更新 数据
                adapter.notifyDataSetChanged();
                //加载完毕
                listview.loadComplete();

            }
        },2000);


    }

    @Override
    public void pullLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                for(int i=0;i<5;i++)
                {
                    Map<String,String> map=new HashMap<>();
                    map.put("title",theme[i]);
                    map.put("source",author[i]);
                    map.put("time",time[i]);
                    list.add(map);
                }

                adapter.notifyDataSetChanged();
                listview.loadComplete();

            }
        },1500);

    }

    class MyListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Map<String, String> mMap = (Map<String, String>) adapter.getItem(position);
            String Text = mMap.get("title");
            startActivityForResult(new Intent(getActivity(), forWeb.class),  1);

            Toast.makeText(getActivity(), Text, Toast.LENGTH_SHORT).show();
        }


    }

}