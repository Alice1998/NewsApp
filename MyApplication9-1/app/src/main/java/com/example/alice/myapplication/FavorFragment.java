package com.example.alice.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class FavorFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载系统里面的布局文件
        TextView view = (TextView) View.inflate(getActivity(), android.R.layout.simple_list_item_1, null);
        view.setText("这是收藏页面");
        view.setBackgroundColor(Color.RED);
        return view;
    }
}