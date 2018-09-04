package com.example.alice.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment1 extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle saveInstanceState)
    {
        TextView view=(TextView)View.inflate(getActivity(),android.R.layout.simple_list_item_single_choice,null);
        view.setText("this is a fragment for Chinese News");
        view.setBackgroundColor(Color.GRAY);
        return view;
    }
}