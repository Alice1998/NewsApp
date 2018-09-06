package com.example.alice.myapplication;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;


public class Fragment1 extends Fragment implements AdapterView.OnItemClickListener {

    private NestedListView mListView;
    private ScrollView mScrollView;
    private ViewFlipper flipper;

    private ArrayAdapter<String> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}