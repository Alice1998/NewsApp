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
    private int shownitems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment2,container,false);
        list=new ArrayList<>();
        for(int i=0;i<8&&i<forData.newsFavors.size();i++)
        {
            list.add(forData.newsFavors.get(i));
            shownitems=i;
        }
        adapter=new SimpleAdapter(getActivity(),forData.newsFavors,R.layout.news_item,new String[]{"title","source","time"},new int[]{R.id.title,R.id.source,R.id.datetime});
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
                listview.loadComplete();
                return;
            }
        },1500);


    }

    @Override
    public void pullLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listview.loadComplete();
                return;
            }
        },1000);

    }

    class MyListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Map<String, String> mMap = (Map<String, String>) adapter.getItem(position-1);
            String thisurl=mMap.get("url");
            if(!forData.hashReads.contains(thisurl))
            {
                forData.hashReads.add(thisurl);
                forData.newsReads.add(0,mMap);
            }
            mMap.put("read","1");
            String Text = mMap.get("title");
            Intent forurl = new Intent(getActivity(), forWeb.class);
            forurl.putExtra("url", thisurl);
            forurl.putExtra("position",position);
            startActivityForResult(forurl, 3);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), Text, Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int result = data.getExtras().getInt("favor");//得到新Activity 关闭后返回的数据
        if(resultCode==4&&result==1)
        {
            int position=data.getExtras().getInt("position");
            String url=data.getStringExtra("url");
            if(!forData.hashFavor.contains(url))
            {
                Map<String, String> mMap = (Map<String, String>) adapter.getItem(position-1);
                forData.hashFavor.add(url);
                forData.newsFavors.add(0,mMap);
            }
        }


    }


}