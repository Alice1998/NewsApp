package com.example.alice.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Fragment2 extends Fragment {
//unsolve refreshing true false
    private static final int NEWS_LIST_LOADER_ID = 0;
    private boolean isLoadingMore = false;
    private boolean noMore = false;
    private int loadedPage = 0;
    private int expectPage = 1;

    @Nullable
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //Log.d("frag " + categoryType, "onCreateView, restoring saved state");
            // loadedPage = savedInstanceState.getInt("loadedPage");
            loadedPage = 0; // TODO(twd2)
            expectPage = savedInstanceState.getInt("expectPage");
            noMore = savedInstanceState.getBoolean("noMore");
            //Log.d("frag " + categoryType, "saved state = " + loadedPage + "/" + expectPage);
        }


        final SwipeRefreshLayout refreshLayout=(SwipeRefreshLayout) inflater.inflate(R.layout.news_list,container,false);
        refreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        refreshLayout.setRefreshing(false);

        RecyclerView minereycle=(RecyclerView) refreshLayout.findViewById(R.id.news_list);
        //
        setupRecyclerView(minereycle);

        final LinearLayoutManager mineliner=(LinearLayoutManager) minereycle.getLayoutManager();
        minereycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView minerecycler,int x,int y)
            {
                /*super.onScrolled(minerecycler,x,y);
                final int itemCount=mineliner.getItemCount();
                int lastSeen=mineliner.findLastVisibleItemPosition();
                if(!isLoadingMore&&!noMore&&(itemCount<=lastSeen+5)&&getActivity()!=null)
                {
                    isLoadingMore=true;
                    minerecycler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(getActivity()!=null)
                            {
                                ((NewsItemRecyclerViewAdapter)minerecycler.getAdapter()).mine.add("new add number 21");
                                minerecycler.getAdapter().notifyItemInserted(itemCount);
                                getLoaderManager().restartLoader()
                            }
                        }
                    })
                }
                */
            }
        });

        //getLoaderManager().restartLoader(NEWS_LIST_LOADER_ID, null, newsListCallbacks);

        return refreshLayout;

    }

    private void setupRecyclerView(@Nullable RecyclerView minerecycle)
    {
        minerecycle.setAdapter(new NewsItemRecyclerViewAdapter());
    }

    public void doRefresh()
    {
        if(getView()==null)
        {
            return;
        }
        else
        {
            loadedPage=0;
            expectPage=1;

            SwipeRefreshLayout refreshLayout=(SwipeRefreshLayout)getView().findViewById(R.id.refreshLayout);
            refreshLayout.setRefreshing(false);
            //getLoaderManager().restartLoader();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Log.d("frag " + categoryType, "onSaveInstanceState");
        outState.putInt("loadedPage", loadedPage);
        outState.putInt("expectPage", expectPage);
        outState.putBoolean("noMore", noMore);
        // outState.putString("category", categoryType.toString());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //Log.d("frag " + categoryType, "onDestroyView " + getLoaderManager().hasRunningLoaders());
        //getLoaderManager().destroyLoader(NEWS_LIST_LOADER_ID);

        // I think this is workaround.
        try {
            SwipeRefreshLayout refreshLayout =
                    (SwipeRefreshLayout)getView().findViewById(R.id.refreshLayout);
            refreshLayout.setRefreshing(false);
            refreshLayout.setVisibility(View.INVISIBLE);
            RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.news_list);
            recyclerView.setVisibility(View.INVISIBLE);
            getView().setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class NewsItemRecyclerViewAdapter extends RecyclerView.Adapter<NewsItemRecyclerViewAdapter.ViewHolder>{
        private final List<String> mine;
        public NewsItemRecyclerViewAdapter()
        {
            mine=new ArrayList<>();
            for(int i=0;i<20;i++)
                mine.add("this is NO. "+i);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            return new ViewHolder(view);

        }




        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (getActivity() == null) {
                return;
            }
        }
            /*

            holder.mView = mine.get(position);

            if (holder.mItem.special) {
                holder.mSourceView.setText("");
                holder.mDatetimeView.setText("");
                holder.mTitleView.setTextColor(ResourcesCompat.getColor(
                        getResources(),
                        R.color.primaryTextDark,
                        getContext().getTheme()));
                holder.mTitleView.setText(holder.mItem.specialType);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.mItem.specialType != R.string.loading_more) {
                            doRefresh();
                        }
                    }
                });
                return;
            }

            try {
                holder.mSourceView.setText(mine.get(position).obj.getString("news_Author"));
                String timeString = mine.get(position).obj.getString("news_Time");
                if (timeString.length() >= 8) {
                    holder.mDatetimeView.setText(timeString.substring(0, 8));
                } else {
                    holder.mDatetimeView.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.mTitleView.setText(mine.get(position).title);
            if (!mine.get(position).read) {
                holder.mTitleView.setTextColor(ResourcesCompat.getColor(
                        getResources(),
                        R.color.primaryTextDark,
                        getContext().getTheme()));
            } else {
                holder.mTitleView.setTextColor(ResourcesCompat.getColor(
                        getResources(),
                        R.color.newsTitleRead,
                        getContext().getTheme()));
            }
            */
            /*
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mItem.read = true;
                    notifyItemChanged(holder.getAdapterPosition());
                    Context context = v.getContext();
                    Intent intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra(NewsDetailFragment.ARG_CATEGORY, categoryType.toString());
                    intent.putExtra(NewsDetailFragment.ARG_NEWS_ID, holder.mItem.id);
                    context.startActivity(intent);
                }

        }); */


        @Override
        public int getItemCount() {
            return mine.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mSourceView, mDatetimeView;
            public String singleitem;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.title);
                mSourceView = (TextView) view.findViewById(R.id.source);
                mDatetimeView = (TextView) view.findViewById(R.id.datetime);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }

    }
}


