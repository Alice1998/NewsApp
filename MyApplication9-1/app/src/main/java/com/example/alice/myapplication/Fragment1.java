package com.example.alice.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.sip.SipErrorCode.SERVER_ERROR;


public class Fragment1 extends Fragment implements RefreshListView.LoadListener {

    //private NestedListView mListView;
    private ScrollView mScrollView;
    private ViewFlipper flipper;

    private RefreshListView listview;
    private List<Map<String, String>> list;
    private mySimpleAdapter adapter;
    forData allData;
    private int shownNum;
    ImageView pics1;
    ImageView pics2;
    ImageView pics3;
    ImageView pics4;
    ImageView pics5;

    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    int k=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment1,container,false);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollview);
        //启动图片切换
        list = new ArrayList<>();

        allData = new forData("国内新闻");
        forData.forTitle();
        pics1=(ImageView)view.findViewById(R.id.img1);
        pics2=(ImageView)view.findViewById(R.id.img2);
        pics3=(ImageView)view.findViewById(R.id.img3);
        pics4=(ImageView)view.findViewById(R.id.img4);
        pics5=(ImageView)view.findViewById(R.id.img5);
        for (int i = 1; i < 16; i++) {
            list.add(allData.newsData.get(i));
        }
        shownNum = 15;
        for(int i=0;i<5;i++)
        {
            String url = forData.newsTitles.get(i).get("pic");
            String name=forData.newsTitles.get(i).get("title");
            setImageURL(url,i);
        }

        adapter = new mySimpleAdapter(getActivity(), list, R.layout.news_item, new String[]{"title", "source", "time"}, new int[]{R.id.title, R.id.source, R.id.datetime});
        listview = (RefreshListView) view.findViewById(R.id.list_view);
        listview.setInterface(this);//loadlistener
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new Fragment1.MyListener());
        //listview.setFocusable(false);
        flipper=(ViewFlipper)view.findViewById(R.id.viewflipper);
        flipper.startFlipping();

        pics1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                   setViewbtn(0);
                }
            });
        pics2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setViewbtn(1);
            }
        });
        pics3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setViewbtn(2);
            }
        });

        return view;
    }

    private void setViewbtn(int k)
    {
        String thisurl=forData.newsTitles.get(k).get("url");
        String Text = forData.newsTitles.get(k).get("title");
        Map<String,String> mMap=new HashMap<>();
        mMap.put("title",Text);
        mMap.put("url",thisurl);
        mMap.put("source","");
        mMap.put("time","");
        if(!allData.hashReads.contains(thisurl))
        {
            allData.hashReads.add(thisurl);
            allData.newsReads.add(0,mMap);
        }
        mMap.put("read","1");
        adapter.notifyDataSetChanged();
        Intent forurl = new Intent(getActivity(), forWeb.class);
        forurl.putExtra("url", thisurl);
        list.add(mMap);
        forurl.putExtra("position",list.size());
        forurl.putExtra("title",Text);
        forurl.putExtra("love",0);
        startActivityForResult(forurl, 1);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if((int)msg.arg1==0)
                        pics1.setImageBitmap(bitmap);
                    else if((int) msg.arg1==1)
                        pics2.setImageBitmap(bitmap);
                    else if((int)msg.arg1==2)
                        pics3.setImageBitmap(bitmap);
                    else if((int)msg.arg1==3)
                        pics4.setImageBitmap(bitmap);
                    else
                        pics5.setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(),"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void setImageURL(final String path,final int i) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        msg.arg1 =i;
                        handler.sendMessage(msg);
                        inputStream.close();
                    }else {
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //网络连接错误
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
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
            String thisurl=mMap.get("url");
            if(!allData.hashReads.contains(thisurl))
            {
                mMap.put("read","1");
                allData.hashReads.add(thisurl);
                allData.newsReads.add(0,mMap);
            }
            adapter.notifyDataSetChanged();
            String Text = mMap.get("title");
            Intent forurl = new Intent(getActivity(), forWeb.class);
            forurl.putExtra("url", thisurl);
            forurl.putExtra("position",position);
            forurl.putExtra("title",Text);
            forurl.putExtra("love",0);
            startActivityForResult(forurl, 1);
            Toast.makeText(getActivity(), Text, Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int result = data.getExtras().getInt("favor");//得到新Activity 关闭后返回的数据
        if(resultCode==2&&result==1)
        {
            int position=data.getExtras().getInt("position");
            String url=data.getStringExtra("url");
            if(!allData.hashFavor.contains(url))
            {
                Map<String, String> mMap = (Map<String, String>) adapter.getItem(position-1);
                allData.hashFavor.add(url);
                allData.newsFavors.add(0,mMap);
            }
        }


    }


}