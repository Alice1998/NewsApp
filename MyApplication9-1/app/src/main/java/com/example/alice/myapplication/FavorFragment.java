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
    private mySimpleAdapter adapter;
    private int shownitems;
    String titleType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment2,container,false);
        Bundle bundle = getArguments();
        titleType=(String)bundle.get("cat");
        if(titleType.equals("favor"))
            list=forData.newsFavors;
        else
            list=forData.newsReads;
        adapter=new mySimpleAdapter(getActivity(),list,R.layout.news_item,new String[]{"title","source","time"},new int[]{R.id.title,R.id.source,R.id.datetime});
        listview=(RefreshListView)view.findViewById(R.id.list_view);
        listview.setInterface(this);//loadlistener
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new MyListener());
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        listview.deferNotifyDataSetChanged();
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
                mMap.put("read","1");
                forData.hashReads.add(thisurl);
                forData.newsReads.add(0,mMap);
            }
            String Text = mMap.get("title");
            Intent forurl = new Intent(getActivity(), forWeb.class);
            forurl.putExtra("url", thisurl);
            forurl.putExtra("position",position);
            forurl.putExtra("title",Text);
            if(titleType.equals("favor"))
            {
                forurl.putExtra("love",1);
                startActivityForResult(forurl, 3);
            }
            else
            {
                forurl.putExtra("love",0);
                startActivityForResult(forurl, 4);
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), Text, Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int result = data.getExtras().getInt("favor");//得到新Activity 关闭后返回的数据
        if(requestCode==3&&resultCode==2&&result==0)
        {
            int position=data.getExtras().getInt("position");
            String url=data.getStringExtra("url");
            forData.newsFavors.remove(position-1);
            forData.hashFavor.remove(url);
            adapter.notifyDataSetChanged();
        }
        else if(requestCode==4&&resultCode==2&&result==1)
        {
            int position=data.getExtras().getInt("position");
            String url=data.getStringExtra("url");
            if(!forData.hashFavor.contains(url))
            {
                Map<String, String> mMap = (Map<String, String>) adapter.getItem(position-1);
                forData.hashFavor.add(url);
                forData.newsFavors.add(mMap);
            }
        }

    }


}