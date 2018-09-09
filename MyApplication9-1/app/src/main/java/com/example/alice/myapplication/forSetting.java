package com.example.alice.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class forSetting extends AppCompatActivity {
    public static final String PREFERENCES_BLOCK = "block";
    ArrayList<String> keywords;
    static TreeMap<String, Boolean> mineMap;

    void loadBlockListStatus() {
        Set<String> blockSet =
                getSharedPreferences(PREFERENCES_BLOCK, Context.MODE_PRIVATE)
                        .getStringSet("block_list", new HashSet<String>());
        for (String blockSetItem : blockSet) {
            if (mineMap.containsKey(blockSetItem)) {
                mineMap.put(blockSetItem, true);
            }
        }
    }

    void writeBlockListStatus() {
        Set<String> blockSet =
                getSharedPreferences(PREFERENCES_BLOCK, Context.MODE_PRIVATE)
                        .getStringSet("block_list", new HashSet<String>());
        Set<String> newSet = new HashSet<>(blockSet);
        for (Map.Entry<String, Boolean> blockListItem : mineMap.entrySet()) {
            if (blockListItem.getValue()) {
                newSet.add(blockListItem.getKey());
            } else {
                newSet.remove(blockListItem.getKey());
            }
        }
        getSharedPreferences(PREFERENCES_BLOCK, Context.MODE_PRIVATE)
                .edit().putStringSet("block_list", newSet).apply();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<String> keywordsList =
                (ArrayList<String>) getIntent().getSerializableExtra("keywords");
        mineMap = new TreeMap<>();
        for (String key: keywordsList) {
            mineMap.put(key, false);

        }
        //
        loadBlockListStatus();
        keywords = new ArrayList<>();
        for (Map.Entry<String, Boolean>entry:mineMap.entrySet()) {
            keywords.add(entry.getKey());
        }
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.blockListView);
        recyclerView.setAdapter(new KeywordsRecyclerViewAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_block_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_bar_exit:
                writeBlockListStatus();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class KeywordsRecyclerViewAdapter
            extends RecyclerView.Adapter<KeywordsRecyclerViewAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.block_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final String keyWord = keywords.get(position);
            holder.mKeyword = keyWord;
            holder.mKeywordView.setText(keyWord);
            holder.setButtonStatus(mineMap.get(keyWord));
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean newStatus = !mineMap.get(keyWord);
                    mineMap.put(keyWord, newStatus);
                    holder.setButtonStatus(newStatus);
                }
            });
        }

        @Override
        public int getItemCount() {
            return keywords.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final Pair<String, Boolean> mItem = new Pair<>("", false);
            final View mView;
            final TextView mKeywordView;
            final Button mButton;
            String mKeyword = "";
            ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mKeywordView = (TextView) mView.findViewById(R.id.keywordView);
                mButton = (Button) mView.findViewById(R.id.blockButton);
            }

            void setButtonStatus(Boolean status) {
                if (status) {
                    mButton.setText(R.string.unblock);

                } else {
                    mButton.setText(R.string.block);

                }
            }
        }
    }
}
