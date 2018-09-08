package com.example.alice.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment2 extends Fragment implements RefreshListView.LoadListener {
    String titleType;
    private RefreshListView listview;
    private List<Map<String, String>> list;
    private mySimpleAdapter adapter;

    forData allData;
    int shownNum;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2, container, false);
        //setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        Bundle bundle = getArguments();
        titleType=(String)bundle.get("cat");
        allData = new forData(titleType);
        for (int i = 1; i < 16; i++) {
            list.add(allData.newsData.get(i));
        }
        shownNum = 15;
        adapter = new mySimpleAdapter(getActivity(), list, R.layout.news_item, new String[]{"title", "source", "time"}, new int[]{R.id.title, R.id.source, R.id.datetime});
        listview = (RefreshListView) view.findViewById(R.id.list_view);
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
                int add = 1;
                while (shownNum < allData.newsData.size() && add < 6) {
                    list.add(allData.newsData.get(shownNum + add));
                    add++;
                }
                shownNum += add-1;
                adapter.notifyDataSetChanged();
                listview.loadComplete();

            }
        }, 2000);


    }

    @Override
    public void pullLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                allData.Refresh();
                for (int i = 1; i < 16; i++) {
                    list.add(allData.newsData.get(i));
                }
                shownNum = 15;
                adapter.notifyDataSetChanged();
                listview.loadComplete();

            }
        }, 1500);

    }

    class MyListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Map<String, String> mMap = (Map<String, String>) adapter.getItem(position-1);
            mMap.put("read","1");
            String Text = mMap.get("title");
            Intent forurl = new Intent(getActivity(), forWeb.class);
            forurl.putExtra("url", mMap.get("url"));
            forurl.putExtra("id",position-1);
            startActivityForResult(forurl, 1);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), Text, Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("favor");//得到新Activity 关闭后返回的数据
        switch (requestCode) {
            case 1:
                //来自按钮1的请求，作相应业务处理
            case 2:
                //来自按钮2的请求，作相应业务处理
        }


    }
}