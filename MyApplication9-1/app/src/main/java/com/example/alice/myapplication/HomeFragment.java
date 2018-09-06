package com.example.alice.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    //定义布局内的控件
    RadioGroup rg;
    View vi;
    ViewPager vp;

    //数据源的集合
    List<Fragment> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载数据
        list.add(new Fragment1());
        list.add(new Fragment2());
        list.add(new Fragment2());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载自己编写的布局文件
        return View.inflate(getActivity(), R.layout.activity_frag_home, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化控件
        rg = (RadioGroup) view.findViewById(R.id.home_rg);
        vp = (ViewPager) view.findViewById(R.id.home_vp);
        vi = view.findViewById(R.id.home_fl_view);
        //创建适配器,设置的碎片管理器使用的是getChildFragmentManager()
        FragmentStatePagerAdapter adapter = new Myadapter(getChildFragmentManager());
        //给ViewPager设置适配器
        vp.setAdapter(adapter);
        //给控件设置监听事件
        rg.setOnCheckedChangeListener(this);
        //给ViewPager设置监听器使用的是add而不是set了
        vp.addOnPageChangeListener(this);
        initVi();
    }

    //屏幕一般的宽度
    int width;

    //把下划线View设置初始值
    private void initVi() {
        width = getResources().getDisplayMetrics().widthPixels / 3;
        //设置下划线View的长度
        FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        vi.setLayoutParams(par);

    }


    //单选按钮点击后触发的回调方法
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //点击后选择对应的ViewPager页面
        //vp.setCurrentItem(checkedId == R.id.News_China ? 0 : 1);
        if(checkedId==R.id.News_China)
        {
            vp.setCurrentItem(0);
            return;
        }
        else if(checkedId==R.id.News_Abroad)
        {
            vp.setCurrentItem(1);
        }
        else
            vp.setCurrentItem(2);

    }

    //屏幕滑动的回调方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //"页面:" + position + "
        // offset偏移百分比" + positionOffset
        // pix像素" + positionOffsetPixels
        //设置下划线的属性
        //设置下划线View的长度
        FrameLayout.LayoutParams par = (FrameLayout.LayoutParams) vi.getLayoutParams();
        //设置下划线距离左边的位置长度
        int left = (int) ((positionOffset + position) * width);
        par.setMargins(left, 0, 0, 0);
        vi.setLayoutParams(par);
    }

    //屏幕被选择的回调方法
    @Override
    public void onPageSelected(int position) {
        //选择页面后设置单选按钮的选择
        switch (position)
        {
            case 0:
                rg.check(R.id.News_China);
                return;
            case 1:
                rg.check(R.id.News_Abroad);
                return;

            case 2:
                rg.check(R.id.News_Finance);
                return;

             default:
                 rg.check(R.id.News_China);

        }
    }

    //这个方法不用理会
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