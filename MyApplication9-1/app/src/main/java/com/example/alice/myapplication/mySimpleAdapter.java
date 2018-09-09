package com.example.alice.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mySimpleAdapter extends SimpleAdapter {

    private int[] colors1 = new int[] { 0xFFedeeef, 0xecd9d9};
    private int[] Colors2 = new int[] { 0xa64d79, 0x000000};
    private List<Map<String, String>> listitem;

    public mySimpleAdapter(Context context, List<Map<String, String>> data, int resource, String[] from, int[] to)

    {
        super(context, data, resource, from, to);
        this.listitem = data;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        if(position>=listitem.size())
            return view;
        if(listitem.get(position).get("read")=="1")
        {
            view.setBackgroundColor(colors1[0]);
        }
        else
        {
            view.setBackgroundColor(colors1[1]);
        }
        return view;

    }


}
