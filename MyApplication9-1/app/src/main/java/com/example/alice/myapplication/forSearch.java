package com.example.alice.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class forSearch extends AppCompatActivity {

    private List<Map<String,String>> list;
    private List<Map<String,String>> data;
    private SearchView mSearchView;
    private ListView lListView;
    private mySimpleAdapter adapter;
    private Button backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchview);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        lListView = (ListView) findViewById(R.id.listView);
        backbtn=(Button) findViewById(R.id.for_back);
        Intent intent=getIntent();
        int inputtype=intent.getIntExtra("type",-1);
        if(inputtype==-1||inputtype==0)
            data=HomeFragment.fav.allData.newsData;
        else
            data=HomeFragment.mineNews[inputtype].allData.newsData;
        list=new LinkedList<>(data);
        list.remove(0);
        adapter=new mySimpleAdapter(this,list, R.layout.news_item, new String[]{"title", "source", "time"}, new int[]{R.id.title, R.id.source, R.id.datetime});
        lListView.setAdapter(adapter);
        lListView.setOnItemClickListener(new MyListener());
        //lListView.setTextFilterEnabled(true);
        mSearchView.setIconified(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                list.clear();
                String title;
                for(int i=0;i<data.size();i++)
                {
                    title=data.get(i).get("title");
                    if(title.indexOf(query)!=-1)
                    {
                        list.add(data.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
                if (mSearchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    mSearchView.clearFocus(); // 不获取焦点
                }
                return true;
            }

            // 当搜索内容改变时触发该方法
            // @Override
             public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    lListView.setFilterText(newText);
                }else{
                    lListView.clearTextFilter();
                }
                return false;
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                forSearch.this.finish();
            }
        });

    }





    class MyListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Map<String, String> mMap = (Map<String, String>) adapter.getItem(position);
            String thisurl=mMap.get("url");
            if(!forData.hashReads.contains(thisurl))
            {
                forData.hashReads.add(thisurl);
                forData.newsReads.add(0,mMap);
            }
            mMap.put("read","1");
            adapter.notifyDataSetChanged();
            String Text = mMap.get("title");
            Intent forurl = new Intent(forSearch.this, forWeb.class);
            forurl.putExtra("url", thisurl);
            forurl.putExtra("position",position);
            forurl.putExtra("title",Text);
            forurl.putExtra("love",0);
            startActivityForResult(forurl, 1);
            Toast.makeText(forSearch.this, Text, Toast.LENGTH_SHORT).show();
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
            if(!forData.hashFavor.contains(url))
            {
                Map<String, String> mMap = (Map<String, String>) adapter.getItem(position-1);
                mMap.put("read","0");
                forData.hashFavor.add(url);
                forData.newsFavors.add(0,mMap);
            }
        }
    }
}