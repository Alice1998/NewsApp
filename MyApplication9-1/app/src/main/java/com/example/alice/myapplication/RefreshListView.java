package com.example.alice.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class RefreshListView extends ListView implements AbsListView.OnScrollListener{
    private View bottomview;
    private View headview;

    private int bottomViewHeight;
    private int headViewHeight;
    private int totaItemCounts;
    private int lasstVisible;
    private int fistVisiable;

    private int yload;
    boolean isLoading;
    private TextView updateInfo;
    private TextView updateTime;
    private LoadListener loadListener;
    private ProgressBar progressBar;
    public RefreshListView(Context context) {
        super(context);
        initView(context);
    }
    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //拿到头布局xml
        headview=LayoutInflater.from(context).inflate(R.layout.head_custom_listview,null);
        updateInfo=(TextView)headview.findViewById(R.id.update_info);
        updateTime=(TextView)headview.findViewById(R.id.update_time);
        progressBar=(ProgressBar)headview.findViewById(R.id.progressbar);
        updateTime.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(System.currentTimeMillis())+" update");

        //拿到尾布局xml
        bottomview=LayoutInflater.from(context).inflate(R.layout.bottom_layout,null);
        //测量bottomview的高度
        bottomview.measure(0,0);
        //拿到高度
        bottomViewHeight=bottomview.getMeasuredHeight();
        //隐藏view
        bottomview.setPadding(0,-bottomViewHeight,0,0);
        headview.measure(0,0);
        headViewHeight=headview.getMeasuredHeight();
        headview.setPadding(0,-headViewHeight,0,0);

        this.addFooterView(bottomview);
        this.addHeaderView(headview);
        //设置拉动监听
        this.setOnScrollListener(this);


    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                yload=(int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY=(int)ev.getY();
                int paddingY=-headViewHeight+(moveY-yload)/2;

                if (paddingY<0){
                    updateInfo.setText("下拉刷新...");
                    progressBar.setVisibility(View.GONE);
                }
                if (paddingY>0){
                    updateInfo.setText("松开刷新...");
                    progressBar.setVisibility(View.GONE);
                }

                headview.setPadding(0,paddingY,0,0);
                break;
//
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totaItemCounts==lasstVisible&&scrollState==SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading=true;
                // bottomview.findViewById(R.id.foot_load).setVisibility(View.VISIBLE);
                bottomview.setPadding(0,0,0,0);
                //加载数据
                loadListener.onLoad();

            }
        }
        if (fistVisiable==0){
            headview.setPadding(0,0,0,0);
            updateInfo.setText("正在刷新。。。");
            progressBar.setVisibility(View.VISIBLE);
            loadListener.pullLoad();
        }
    }



    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.fistVisiable=firstVisibleItem;
        this.lasstVisible=firstVisibleItem+visibleItemCount;
        this.totaItemCounts=totalItemCount;

    }
    //加载完成
    public void loadComplete(){
        isLoading=false;

        bottomview.setPadding(0,-bottomViewHeight,0,0);
        headview.setPadding(0,-headViewHeight,0,0);


    }
    public void setInterface(LoadListener loadListener){
        this.loadListener=loadListener;

    }
    //接口回调
    public interface LoadListener{

        void onLoad();
        void pullLoad();


    }
}