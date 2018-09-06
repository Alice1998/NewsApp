package com.example.alice.myapplication;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 2016/8/18.
 */
public class Fragment2 extends Fragment implements RefreshListView.LoadListener {
    private RefreshListView listview;
    private List<Integer> list=new ArrayList<>();
    private ArrayAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
          View view=inflater.inflate(R.layout.fragment1,container,false);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollview);
        mListView = (NestedListView) view.findViewById(R.id.listview_1);
        flipper=(ViewFlipper)view.findViewById(R.id.viewflipper);
        //启动图片切换
        flipper.startFlipping();


        //数据部分
        List<String> array=new ArrayList<>();
        for(int i=0;i<20;i++)
            array.add("This is No. "+i);

        mAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, array);

        mListView.setAdapter(mAdapter);

        //解决未滑动时聚焦listview的问题
        mListView.setFocusable(false);

        return view;
         */

        View view=inflater.inflate(R.layout.fragment2,container,false);
        //setContentView(R.layout.activity_main);
        for (int i=1;i<20;i++){
            list.add(i);
        }
        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,list);
        listview=(RefreshListView)view.findViewById(R.id.list_view);
        listview.setInterface(this);//loadlistener
        listview.setAdapter(adapter);
        return view;
    }


    @Override
    public void onLoad() {
        //设置三秒延迟模仿延时获取数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载数据
                for (int j=1;j<11;j++){

                    list.add(j);
                }
                //更新 数据
                adapter.notifyDataSetChanged();
                //加载完毕
                listview.loadComplete();

            }
        },3000);


    }

    @Override
    public void pullLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                for (int i=1;i<20;i++){
                    list.add(i+1);
                }
                adapter.notifyDataSetChanged();
                listview.loadComplete();

            }
        },2000);

    }

}