package com.example.alice.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class SettingFragment extends Fragment {

    static Boolean checked=false;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) View.inflate(getActivity(), R.layout.activity_settings, null);
        Button settype=(Button)view.findViewById(R.id.blocking_settings_button);
        settype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getActivity(), forSetting.class);
                forData forinput=new forData();
                HomeFragment.createflag=1;
                intent.putExtra("keywords", forData.urlMap.input);
                startActivity(intent);
            }
        });

        final Button clear = (Button)view.findViewById(R.id.download);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forWeb delete=new forWeb();
                delete.clearWebViewCache();
                Toast.makeText(getActivity(), "clear WebView Cache finished!", Toast.LENGTH_SHORT).show();
            }
        });


        final Switch showPictures = (Switch)view.findViewById(R.id.or);
        showPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = ((Switch)v).isChecked();
                if(checked) //已读
                {
                    FavorFragment.list.clear();
                    FavorFragment.list.addAll(forData.newsReads);

                }
                else
                {
                    FavorFragment.list.clear();
                    FavorFragment.list.addAll(forData.newsFavors);
                }
            }
        });
        return view;
    }

}