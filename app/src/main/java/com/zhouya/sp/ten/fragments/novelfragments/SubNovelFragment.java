package com.zhouya.sp.ten.fragments.novelfragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.MySQLiteOpenHelper;
import com.zhouya.sp.ten.ConstantConfig;
import com.zhouya.sp.ten.ErrorActivity;
import com.zhouya.sp.ten.R;
import com.zhouya.sp.ten.model.MovieDetails;
import com.zhouya.sp.ten.model.NovelDetails;
import com.zhouya.sp.ten.tools.NetworkTask;
import com.zhouya.sp.ten.tools.NetworkTaskCallback;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubNovelFragment extends Fragment {
                        //标题        作者                      阅读量
    private TextView novel_text_title,novel_text_author_small,novel_text_times,
                        //总结            正文              作者              长居地
                     novel_text_summary,novel_text_text,novel_text_author,novel_text_authorbrief;

    //详细地址
    public static final String ARTICLE_DETATLS_URL = "http://api.shigeten.net/api/Novel/GetNovelContent?id=";

    //收藏按钮
    private ImageView movie_favorite_btn;

    //
    private NovelDetails nds;
    private MySQLiteOpenHelper sqLiteHelper;
    private ArrayList<String> createSQLStr;

    //判断是否收藏成功
    private boolean flag;
    private String mSaveTime;

    public SubNovelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_sub_novel, container, false);

        //初始化数据库
        createSQLStr = new ArrayList<>();
        String insertFavSql = "create table if not exists Favorites (_id integer primary key,id varchar2 not null,author varchar2 not null,title varchar2 not null,flag varchar2,detailsUrl varchar2,imageUrl varchar2,publishTime varchar2)";
        createSQLStr.add(insertFavSql);
        sqLiteHelper = new MySQLiteOpenHelper(getContext(),"MyFavorite.db",1,createSQLStr,null);

        //找到TextView组件
        novel_text_title = (TextView) ret.findViewById(R.id.novel_text_title);
        novel_text_author_small = (TextView) ret.findViewById(R.id.novel_text_author_small);
        novel_text_times = (TextView) ret.findViewById(R.id.novel_text_times);
        novel_text_summary = (TextView) ret.findViewById(R.id.novel_text_summary);
        novel_text_text = (TextView) ret.findViewById(R.id.novel_text_text);

        novel_text_author = (TextView) ret.findViewById(R.id.novel_text_author);
        novel_text_authorbrief = (TextView) ret.findViewById(R.id.novel_text_authorbrief);
        //收藏按钮
        movie_favorite_btn = (ImageView) ret.findViewById(R.id.movie_favorite_btn);
        return ret;
    }

    @Override
    public void onResume() {

        //设置字体
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
        float novelFont1 = sharedPreferences.getFloat("novelFont1", 16);
        float novelFont2 = sharedPreferences.getFloat("novelFont2", 18);
        novel_text_summary.setTextSize(novelFont1);
        novel_text_text.setTextSize(novelFont2);

        Bundle bundle = getArguments();
        int id = bundle.getInt("id");
        mSaveTime = bundle.getString("date");
        //将请求的值赋给组件
        setSubNovelInfo(id);

        int i = sqLiteHelper.selectCount("select * from Favorites where id = ?", new String[]{id+""});
        if(i > 0){
            movie_favorite_btn.setImageResource(R.mipmap.share_favorite_selected);
        }else {
            movie_favorite_btn.setImageResource(R.mipmap.share_favorite);
        }
        //获取焦点得到数据后
        movie_favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j = sqLiteHelper.selectCount("select * from Favorites where id = ?", new String[]{nds.getId()+""});
                if (j > 0) {
                    Toast.makeText(getContext(), "您已经收藏过该文章了", Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(getContext()).setTitle("收藏").setMessage("您确定要收藏这篇文章?")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == AlertDialog.BUTTON_POSITIVE) {
                                        String addFavSql = "insert into Favorites(id,author,title,flag,detailsUrl,imageUrl,publishTime) values(?,?,?,?,?,?,?)";
                                        flag = sqLiteHelper.execData(addFavSql, new String[]{nds.getId() + "", nds.getAuthor(), nds.getTitle(), "美文", ARTICLE_DETATLS_URL, ConstantConfig.PICTURE_DISPLAY_URL, mSaveTime});
                                    }
                                    //判断是否收藏成功
                                    if (flag) {
                                        movie_favorite_btn.setImageResource(R.mipmap.share_favorite_selected);
                                        Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        movie_favorite_btn.setImageResource(R.mipmap.share_favorite);
                                        Toast.makeText(getActivity(), "收藏失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).create().show();

                }
            }
        });
        super.onResume();
    }

    private void setSubNovelInfo(int novelId){
        if (novelId >= 0) {
            String url = ARTICLE_DETATLS_URL + novelId;
            new NetworkTask(getContext(), new NetworkTaskCallback() {
                @Override
                public void onTaskFinished(byte[] data) {
                    try {
                        String json = new String(data,"UTF-8");
                        JSONObject result = new JSONObject(json);
                        nds = new NovelDetails();
                        nds.parseJson(result);
                        //给组件赋值
                        novel_text_title.setText(nds.getTitle());
                        novel_text_author_small.setText("作者:" + nds.getAuthor());
                        novel_text_times.setText("阅读量:" + nds.getTimes());
                        novel_text_summary.setText(nds.getSummary());
                        novel_text_text.setText(nds.getText());
                        novel_text_author.setText(nds.getAuthor());
                        novel_text_authorbrief.setText(nds.getAuthorbrief());
                    } catch (Exception e) {
                        Intent it = new Intent(getContext(), ErrorActivity.class);
                        startActivity(it);
                        e.printStackTrace();
                    }
                }
            }).execute(url);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createSQLStr.clear();
        createSQLStr = null;
        sqLiteHelper.destroy();
    }
}
