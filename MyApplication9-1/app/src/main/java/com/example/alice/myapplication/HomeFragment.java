package com.example.alice.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    //定义布局内的控件
    RadioGroup rg;
    View vi;
    ViewPager vp;
    RadioButton[] types=new RadioButton[10];
    static Fragment1 fav=new Fragment1();
    static Fragment2 mineNews[]=new Fragment2[9];
    Bundle bundle[]=new Bundle[9];
    static int createflag=0;
    int toPosition[]=new int[10];
    int toButton[]=new int[9];
    Button forSearch;
    int tempOn=0;

    //数据源的集合
    List<Fragment> list = new ArrayList<>();
    FragmentStatePagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.activity_frag_home, null);
        rg = (RadioGroup) view.findViewById(R.id.home_rg);
        vp = (ViewPager) view.findViewById(R.id.home_vp);
        vi = view.findViewById(R.id.home_fl_view);
        forSearch=(Button)view.findViewById(R.id.search);


        types[0]=(RadioButton)view.findViewById(R.id.News_1);
        types[1]=(RadioButton)view.findViewById(R.id.News_2);
        types[2]=(RadioButton)view.findViewById(R.id.News_3);
        types[3]=(RadioButton)view.findViewById(R.id.News_4);
        types[4]=(RadioButton)view.findViewById(R.id.News_5);
        types[5]=(RadioButton)view.findViewById(R.id.News_6);
        types[6]=(RadioButton)view.findViewById(R.id.News_7);
        types[7]=(RadioButton)view.findViewById(R.id.News_8);

        mineNews[0]=new Fragment2();
        bundle[0]=new Bundle();
        bundle[0].putString("cat","国际新闻");
        mineNews[0].setArguments(bundle[0]);

        mineNews[1]=new Fragment2();
        bundle[1]=new Bundle();
        bundle[1].putString("cat","经济新闻");
        mineNews[1].setArguments(bundle[1]);

        mineNews[2]=new Fragment2();
        bundle[2]=new Bundle();
        bundle[2].putString("cat","文化新闻");
        mineNews[2].setArguments(bundle[2]);
        list.add(fav);


        for(int i=0;i<3;i++)
        {
            list.add(mineNews[i]);
            toPosition[i]=i+1;
            toButton[i+1]=i;
            types[i].setVisibility(View.VISIBLE);
        }

        //创建适配器,设置的碎片管理器使用的是getChildFragmentManager()
        adapter = new Myadapter(getChildFragmentManager());
        //给ViewPager设置适配器
        vp.setAdapter(adapter);
        //给控件设置监听事件
        rg.setOnCheckedChangeListener(this);
        //给ViewPager设置监听器使用的是add而不是set了
        vp.addOnPageChangeListener(this);
        initVi();

        forSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //***
                Intent intent=new Intent(getActivity(),forSearch.class);
                intent.putExtra("type",toButton[tempOn]);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(createflag==0)
        {
            return;
        }
        createflag=0;
        list.clear();
        list.add(fav);
        int pointer=1;
        String haveit;
        for(int i=0;i<8;i++)
        {
            haveit=forData.urlMap.input.get(i);
            if(forSetting.mineMap.get(haveit))
            {
                types[i].setVisibility(View.VISIBLE);
                if(mineNews[i]!=null)
                    list.add(mineNews[i]);
                else
                {
                    mineNews[i]=new Fragment2();
                    bundle[i]=new Bundle();
                    bundle[i].putString("cat",haveit);
                    mineNews[i].setArguments(bundle[i]);
                    list.add(mineNews[i]);
                }
                toPosition[i]=pointer;
                toButton[pointer]=i;
                pointer++;
            }
            else
            {
                types[i].setVisibility(View.GONE);
            }
        }
        createflag=2;
        initVi();
        adapter.notifyDataSetChanged();
    }

    //屏幕一般的宽度
    int width;

    //把下划线View设置初始值
    private void initVi() {
        if(list.size()!=0)
            width = getResources().getDisplayMetrics().widthPixels / list.size();
        else
            width=getResources().getDisplayMetrics().widthPixels;
        //设置下划线View的长度
        FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        vi.setLayoutParams(par);

    }


    //单选按钮点击后触发的回调方法
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //点击后选择对应的ViewPager页面
        if(checkedId==R.id.News_0)
        {
            vp.setCurrentItem(0);
            tempOn=0;
            return;
        }
        else
        {
            for(int i=0;i<8;i++)
            {
                if(checkedId==types[i].getId())
                {
                    tempOn=i;
                    vp.setCurrentItem(toPosition[i]);
                    return;
                }
            }
        }

    }

    //屏幕滑动的回调方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        FrameLayout.LayoutParams par = (FrameLayout.LayoutParams) vi.getLayoutParams();
        //设置下划线距离左边的位置长度
        int left = (int) ((positionOffset + position) * width);
        par.setMargins(left, 0, 0, 0);
        vi.setLayoutParams(par);
    }

    //屏幕被选择的回调方法
    @Override
    public void onPageSelected(int position) {
        // todo
        if(position==0)
        {
            rg.check(R.id.News_0);
            return;
        }
        tempOn=position;
        rg.check(types[toButton[position]].getId());
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //创建ViewPager适配器的类
    class Myadapter extends FragmentStatePagerAdapter {

        public Myadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


}