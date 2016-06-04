package com.zhouya.sp.ten.fragments.moviefragments;


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
import com.squareup.picasso.Picasso;
import com.zhouya.sp.ten.ErrorActivity;
import com.zhouya.sp.ten.MainActivity;
import com.zhouya.sp.ten.R;
import com.zhouya.sp.ten.model.MovieDetails;
import com.zhouya.sp.ten.tools.NetworkTask;
import com.zhouya.sp.ten.tools.NetworkTaskCallback;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    //Movie
    private static final String MOVIE_DETATLS_URL = "http://api.shigeten.net/api/Critic/GetCriticContent?id=";
    //加载图片地址
    public static final String PICTURE_DISPLAY_URL = "http://api.shigeten.net/";

                        // 宣传画
    private ImageView movie_image_show0_imageforplay,
                        //第一张图          第二张图            第三张图            第四张图
                        movie_image_show1,movie_image_show2,movie_image_show3,movie_image_show4;
                    //标题            作者：                     访问量            第一段内容
    private TextView movie_text_title,movie_text_author_small,movie_text_times,movie_text_text1,
                        //剧情简介             1968年            规训与惩罚           主要内容
                        movie_text_movieInfo,movie_text_text2,movie_text_realtitle,movie_text_text3_5,
                        //作者            长居地
                        movie_text_author,movie_text_authorbrief;

    //收藏按钮
    private ImageView movie_favorite_btn;
    private int mMovieId;

    //
    private MovieDetails movieDetails;
    private MySQLiteOpenHelper sqLiteHelper;
    private ArrayList<String> createSQLStr;

    //判断是否收藏成功
    private boolean flag;
    private String mSaveTime;

    public FirstFragment() {
        // Required empty public constructor
//        sqLiteHelper = new MySQLiteOpenHelper(getContext(),"MyFavorite.db",,null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_first, container, false);

        //初始化数据库
        createSQLStr = new ArrayList<>();
        String insertFavSql = "create table if not exists Favorites (_id integer primary key,id varchar2 not null,author varchar2 not null,title varchar2 not null,flag varchar2,detailsUrl varchar2,imageUrl varchar2,publishTime varchar2)";
//        String insertCacheSql = "create table if not exists ContentCaches (_id integer primary key,id varchar2 not null,flag varchar2,contentUrl varchar2,contentJson varchar2)";
        createSQLStr.add(insertFavSql);
//        createSQLStr.add(insertCacheSql);
        sqLiteHelper = new MySQLiteOpenHelper(getContext(),"MyFavorite.db",1,createSQLStr,null);

        movie_image_show0_imageforplay = (ImageView)ret.findViewById(R.id.movie_image_show0_imageforplay);
        movie_image_show1 = (ImageView)ret.findViewById(R.id.movie_image_show1);
        movie_image_show2 = (ImageView)ret.findViewById(R.id.movie_image_show2);
        movie_image_show3 = (ImageView)ret.findViewById(R.id.movie_image_show3);
        movie_image_show4 = (ImageView)ret.findViewById(R.id.movie_image_show4);

        movie_text_title = (TextView)ret.findViewById(R.id.movie_text_title);
        movie_text_author_small = (TextView)ret.findViewById(R.id.movie_text_author_small);
        movie_text_times = (TextView)ret.findViewById(R.id.movie_text_times);
        movie_text_text1 = (TextView)ret.findViewById(R.id.movie_text_text1);
        movie_text_movieInfo = (TextView)ret.findViewById(R.id.movie_text_movieInfo);

        movie_text_text2 = (TextView)ret.findViewById(R.id.movie_text_text2);
        movie_text_realtitle = (TextView)ret.findViewById(R.id.movie_text_realtitle);
        movie_text_text3_5 = (TextView)ret.findViewById(R.id.movie_text_text3_5);
        movie_text_author = (TextView)ret.findViewById(R.id.movie_text_author);
        movie_text_authorbrief = (TextView)ret.findViewById(R.id.movie_text_authorbrief);

        //收藏按钮
        movie_favorite_btn = (ImageView) ret.findViewById(R.id.movie_favorite_btn);

        return ret;
    }

    @Override
    public void onResume() {

        //设置字体
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
        float movieFont1 = sharedPreferences.getFloat("movieFont1", 18);
        float movieFont2 = sharedPreferences.getFloat("movieFont2", 20);
//        System.out.println("movieFont2 = " + movieFont2 + "=======1" + movieFont1);
        movie_text_text1.setTextSize(movieFont1);
        movie_text_movieInfo.setTextSize(movieFont2);

        Bundle bundle = getArguments();
        mMovieId = bundle.getInt("id");
        mSaveTime = bundle.getString("date");
        //
        getMovieInfo(mMovieId);
        int i = sqLiteHelper.selectCount("select * from Favorites where id = ?", new String[]{mMovieId+""});
        if(i > 0){
            movie_favorite_btn.setImageResource(R.mipmap.share_favorite_selected);
        }else{
            movie_favorite_btn.setImageResource(R.mipmap.share_favorite);
        }
        //获取焦点得到数据后
        movie_favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j = sqLiteHelper.selectCount("select * from Favorites where id = ?", new String[]{mMovieId+""});
                if (j > 0) {
                    Toast.makeText(getContext(), "您已经收藏过该文章了", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(getContext()).setTitle("收藏").setMessage("您确定要收藏这篇文章?")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == AlertDialog.BUTTON_POSITIVE) {
                                        String addFavSql = "insert into Favorites(id,author,title,flag,detailsUrl,imageUrl,publishTime) values(?,?,?,?,?,?,?)";
                                        flag = sqLiteHelper.execData(addFavSql, new String[]{movieDetails.getId(), movieDetails.getAuthor(), movieDetails.getRealtitle(), "影评", MOVIE_DETATLS_URL, PICTURE_DISPLAY_URL, mSaveTime});
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

    private void getMovieInfo(final int movieId){
        if (movieId != 0) {
            String url = MOVIE_DETATLS_URL + movieId;
            new NetworkTask(getContext(),sqLiteHelper, new NetworkTaskCallback() {
                @Override
                public void onTaskFinished(byte[] data) {
                    try {
                        String json = new String(data,"UTF-8");
                        //缓存数据
//                        String addFavSql = "insert into ContentCaches(id,flag,contentUrl,contentJson) values(?,?,?,?)";
//                        sqLiteHelper.execData(addFavSql, new String[]{movieDetails.getId(), "影评", MOVIE_DETATLS_URL+movieDetails.getId(), json});


                        JSONObject result = new JSONObject(json);
                        movieDetails = new MovieDetails();
                        movieDetails.parseJson(result);

                        //赋值
                        String imageUrl = PICTURE_DISPLAY_URL + movieDetails.getImageforplay();
                        Picasso.with(getContext()).load(imageUrl).into(movie_image_show0_imageforplay);
                        Picasso.with(getContext()).load(PICTURE_DISPLAY_URL + movieDetails.getImage1()).into(movie_image_show1);
                        Picasso.with(getContext()).load(PICTURE_DISPLAY_URL + movieDetails.getImage2()).into(movie_image_show2);
                        Picasso.with(getContext()).load(PICTURE_DISPLAY_URL + movieDetails.getImage3()).into(movie_image_show3);
                        Picasso.with(getContext()).load(PICTURE_DISPLAY_URL + movieDetails.getImage4()).into(movie_image_show4);
                        //
                        movie_text_title.setText(movieDetails.getTitle());
                        movie_text_author_small.setText("作者:" + movieDetails.getAuthor());
                        movie_text_times.setText("阅读量:" + movieDetails.getTimes());
                        movie_text_text1.setText(movieDetails.getText1());
                        movie_text_movieInfo.setText(movieDetails.getMovieInfo());
                        movie_text_text2.setText(movieDetails.getText2());
                        movie_text_realtitle.setText(movieDetails.getRealtitle());
                        movie_text_text3_5.setText(movieDetails.getText3() + "\r\n\r\n" + movieDetails.getText4() + "\r\n\r\n" + movieDetails.getText5());
                        movie_text_author.setText(movieDetails.getAuthor());
                        movie_text_authorbrief.setText(movieDetails.getAuthorbrief());

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
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createSQLStr.clear();
        createSQLStr = null;
        sqLiteHelper.destroy();
    }
}
