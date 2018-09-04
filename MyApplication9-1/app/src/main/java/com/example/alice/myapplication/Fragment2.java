package com.example.alice.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView view=(TextView)View.inflate(getActivity(),android.R.layout.simple_list_item_1,null);
        view.setText("This is for abroad News");
        view.setBackgroundColor(Color.BLUE);
        return view;
    }
}
