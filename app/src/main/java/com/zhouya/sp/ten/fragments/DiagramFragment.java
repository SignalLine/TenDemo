package com.zhouya.sp.ten.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhouya.sp.ten.ErrorActivity;
import com.zhouya.sp.ten.R;
import com.zhouya.sp.ten.fragments.diagramfragments.SubDiagramFragment;
import com.zhouya.sp.ten.tools.CommonFragmentAdapter;
import com.zhouya.sp.ten.tools.NetworkTask;
import com.zhouya.sp.ten.tools.NetworkTaskCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiagramFragment extends Fragment implements ViewPager.OnPageChangeListener {
    public static final String PICTURE_URL = "http://api.shigeten.net/api/Diagram/GetDiagramList";
    private ViewPager mViewPager;
    private CommonFragmentAdapter mAdapter;
    private List<Fragment> mFragments;
    private Fragment fragment;
    private Bundle mBundle;//传递数据

    //设置title日期
    private SimpleDateFormat sdf;
    private int month;
    private ImageView imageDate1,imageDate2,imageWeek,imageMonth;

    private Map<Integer,Integer> imageData;
    private int mPageNum;

    public DiagramFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments = new ArrayList<>();
        mAdapter = new CommonFragmentAdapter(getChildFragmentManager(),mFragments);

        sdf = new SimpleDateFormat("MM-dd/E");
        //获取父组件
        imageMonth = (ImageView) getActivity().findViewById(R.id.main_tab_movie_month);
        imageDate1 = (ImageView)getActivity().findViewById(R.id.main_tab_movie_date1);
        imageDate2 = (ImageView)getActivity().findViewById(R.id.main_tab_movie_date2);
        imageWeek = (ImageView)getActivity().findViewById(R.id.main_tab_movie_week);

        //用于存储日期的方便进行设置图片
        imageData = new HashMap<>();
        //TODO:设置时间方法
        //设置title今天的日期
//        setTitleTime(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_diagram, container, false);

        mViewPager = (ViewPager) ret.findViewById(R.id.diagram_view_pager);

        //异步请求数据id
        getDiagramId();

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(10);
        mViewPager.addOnPageChangeListener(this);

//        setTitleTime(0);
        return ret;
    }


    private void getDiagramId(){
        new NetworkTask(getContext(), new NetworkTaskCallback() {
            @Override
            public void onTaskFinished(byte[] data) {
                try {
                    String json = new String(data, "UTF-8");
                    //解析数据
                    JSONObject jo = new JSONObject(json);
                    JSONArray results = jo.getJSONArray("result");
                    int length = results.length();
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            JSONObject diagram = results.getJSONObject(i);
                            int id = diagram.getInt("id");
                            mBundle = new Bundle();
                            mBundle.putInt("id", id);
                            mBundle.putString("date",new SimpleDateFormat("dd/MM").format(new Date()));
                            fragment = new SubDiagramFragment();
                            fragment.setArguments(mBundle);
                            mFragments.add(fragment);

                            mBundle = null;
                            fragment = null;

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }catch (Exception e) {
                    Intent it = new Intent(getContext(), ErrorActivity.class);
                    startActivity(it);
                    e.printStackTrace();
                }
            }
        }).execute(PICTURE_URL);
    }

    //------------------------------------------
    //设置月 日 星期
    private void setTitleTime(int value){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, value);
        long timeInMillis = calendar.getTimeInMillis();
        String getTime = sdf.format(new Date(timeInMillis));
        month = Integer.parseInt(getTime.substring(0, getTime.indexOf("-")));


        imageData.clear();
        int date = Integer.parseInt(getTime.substring(getTime.indexOf("-") + 1, getTime.indexOf("/")));
        imageData.put(1,R.mipmap.date_1);
        imageData.put(2,R.mipmap.date_2);
        imageData.put(3,R.mipmap.date_3);
        imageData.put(4,R.mipmap.date_4);
        imageData.put(5,R.mipmap.date_5);
        imageData.put(6,R.mipmap.date_6);
        imageData.put(7,R.mipmap.date_7);
        imageData.put(8,R.mipmap.date_8);
        imageData.put(9,R.mipmap.date_9);
        imageData.put(0,R.mipmap.date_0);
        //重新设置星期，方便以后修改
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        //设置月
        if(month > 0 && month <= 12){
            switch (month) {
                case 1:
                    imageMonth.setImageResource(R.mipmap.month_1); break;
                case 2:
                    imageMonth.setImageResource(R.mipmap.month_2); break;
                case 3:
                    imageMonth.setImageResource(R.mipmap.month_3); break;
                case 4:
                    imageMonth.setImageResource(R.mipmap.month_4); break;
                case 5:
                    imageMonth.setImageResource(R.mipmap.month_5); break;
                case 6:
                    imageMonth.setImageResource(R.mipmap.month_6); break;
                case 7:
                    imageMonth.setImageResource(R.mipmap.month_7); break;
                case 8:
                    imageMonth.setImageResource(R.mipmap.month_8); break;
                case 9:
                    imageMonth.setImageResource(R.mipmap.month_9); break;
                case 10:
                    imageMonth.setImageResource(R.mipmap.month_10); break;
                case 11:
                    imageMonth.setImageResource(R.mipmap.month_11); break;
                case 12:
                    imageMonth.setImageResource(R.mipmap.month_12); break;
            }

        }
        //设置日期
        if(date > 0 && date <32){
            int date1 = date%10;
            if(date >= 1 && date < 10){
                imageDate1.setImageResource(R.mipmap.date_0);
            }else if(date >= 10 && date < 20){
                imageDate1.setImageResource(R.mipmap.date_1);
            }else if(date >= 20 && date < 30){
                imageDate1.setImageResource(R.mipmap.date_2);
            }else{
                imageDate1.setImageResource(R.mipmap.date_3);
            }
            imageDate2.setImageResource(imageData.get(date1));
        }
        //设置星期
        switch (week) {
            case 1:
                imageWeek.setImageResource(R.mipmap.week_7);
                break;
            case 2:imageWeek.setImageResource(R.mipmap.week_1);
                break;
            case 3:imageWeek.setImageResource(R.mipmap.week_2);
                break;
            case 4:imageWeek.setImageResource(R.mipmap.week_3);
                break;
            case 5:imageWeek.setImageResource(R.mipmap.week_4);
                break;
            case 6:imageWeek.setImageResource(R.mipmap.week_5);
                break;
            case 7:imageWeek.setImageResource(R.mipmap.week_6);
                break;
            default:imageWeek.setImageResource(R.mipmap.week_7);
                break;
        }
    }

    @Override
    public void onResume() {
        setTitleTime(-mPageNum);
        super.onResume();
    }

    //--页面切换状态事件的监听-----------------------------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPageNum = position;
        setTitleTime(-position);
    }

    @Override
    public void onPageSelected(int position) {
        setTitleTime(-position);
        switch (position) {
            case 0:
                Toast.makeText(getContext(), "当前已是最新内容", Toast.LENGTH_SHORT).show();
                break;
            case 9:Toast.makeText(getContext(), "当前已是最后内容", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //------------------------------------------
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager.clearOnPageChangeListeners();
        mViewPager.clearFocus();
        mViewPager = null;
        mFragments.clear();
        mFragments = null;
        mBundle.clear();
        mBundle = null;
        month = 0;
        fragment.onDestroyView();
        fragment = null;
        sdf = null;
        imageData.clear();
        imageData = null;

        imageDate1.clearFocus();
        imageDate1 = null;
        imageDate2.clearFocus();
        imageDate2 = null;
        imageWeek.clearFocus();
        imageWeek = null;
        imageMonth.clearFocus();
        imageMonth = null;
    }
}
