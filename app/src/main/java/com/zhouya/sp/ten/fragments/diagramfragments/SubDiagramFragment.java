package com.zhouya.sp.ten.fragments.diagramfragments;


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
import com.zhouya.sp.ten.ConstantConfig;
import com.zhouya.sp.ten.ErrorActivity;
import com.zhouya.sp.ten.R;
import com.zhouya.sp.ten.model.DiagramDetails;
import com.zhouya.sp.ten.model.NovelDetails;
import com.zhouya.sp.ten.tools.NetworkTask;
import com.zhouya.sp.ten.tools.NetworkTaskCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubDiagramFragment extends Fragment {

    public static final String PICTURE_DETATLS_URL = "http://api.shigeten.net/api/Diagram/GetDiagramContent?id=";
    private ImageView diagram_image_image1;
    private TextView diagram_text_title,diagram_text_authorbrief,
            diagram_text_text1,diagram_text_text2;

    //收藏按钮
    private ImageView diagram_favorite_btn;

    //
    private DiagramDetails dds;
    private MySQLiteOpenHelper sqLiteHelper;
    private ArrayList<String> createSQLStr;

    //判断是否收藏成功
    private boolean flag;
    private String mSaveTime;

    public SubDiagramFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_sub_diagram, container, false);

        //初始化数据库
        createSQLStr = new ArrayList<>();
        String insertFavSql = "create table if not exists Favorites (_id integer primary key,id varchar2 not null,author varchar2 not null,title varchar2 not null,flag varchar2,detailsUrl varchar2,imageUrl varchar2,publishTime varchar2)";
        createSQLStr.add(insertFavSql);
        sqLiteHelper = new MySQLiteOpenHelper(getContext(),"MyFavorite.db",1,createSQLStr,null);


        diagram_image_image1 = (ImageView) ret.findViewById(R.id.diagram_image_image1);
        diagram_text_title = (TextView)ret.findViewById(R.id.diagram_text_title);
        diagram_text_authorbrief = (TextView)ret.findViewById(R.id.diagram_text_authorbrief);
        diagram_text_text1 = (TextView)ret.findViewById(R.id.diagram_text_text1);
        diagram_text_text2 = (TextView)ret.findViewById(R.id.diagram_text_text2);


        //收藏按钮
        diagram_favorite_btn = (ImageView) ret.findViewById(R.id.diagram_favorite_btn);

        return ret;
    }

    @Override
    public void onResume() {

        //设置字体
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
        float diagramFont1 = sharedPreferences.getFloat("diagramFont1", 18);
        float diagramFont2 = sharedPreferences.getFloat("diagramFont2", 18);
        diagram_text_text1.setTextSize(diagramFont1);
        diagram_text_text2.setTextSize(diagramFont2);

        Bundle bundle = getArguments();
        int id = bundle.getInt("id");
        mSaveTime = bundle.getString("date");
        getSubDiagramInfo(id);
        int i = sqLiteHelper.selectCount("select * from Favorites where id = ?", new String[]{id+""});
        if(i > 0){
            diagram_favorite_btn.setImageResource(R.mipmap.share_favorite_selected);
        }else {
            diagram_favorite_btn.setImageResource(R.mipmap.share_favorite);
        }
        //获取焦点得到数据后
        diagram_favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j = sqLiteHelper.selectCount("select * from Favorites where id = ?", new String[]{dds.getId() + ""});
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
                                        flag = sqLiteHelper.execData(addFavSql, new String[]{dds.getId() + "", dds.getAuthorbrief(), dds.getTitle(), "美图", PICTURE_DETATLS_URL, ConstantConfig.PICTURE_DISPLAY_URL, mSaveTime});
                                    }
                                    //判断是否收藏成功
                                    if (flag) {
                                        diagram_favorite_btn.setImageResource(R.mipmap.share_favorite_selected);
                                        Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        diagram_favorite_btn.setImageResource(R.mipmap.share_favorite);
                                        Toast.makeText(getActivity(), "收藏失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).create().show();

                }
            }
        });
        super.onResume();
    }

    private void getSubDiagramInfo(int id){
        if (id > 0) {
            String url = PICTURE_DETATLS_URL + id;
            new NetworkTask(getContext(), new NetworkTaskCallback() {
                @Override
                public void onTaskFinished(byte[] data) {
                    String json = null;
                    try {
                        json = new String(data,"UTF-8");
                        JSONObject result = new JSONObject(json);
                        dds = new DiagramDetails();
                        dds.parseJson(result);
                        //给组件赋值

                        diagram_text_title.setText(dds.getTitle());
                        diagram_text_authorbrief.setText(dds.getAuthorbrief());
                        diagram_text_text1.setText(dds.getText1());
                        diagram_text_text2.setText(dds.getText2());
                        Picasso.with(getContext()).load(dds.getImage1()).into(diagram_image_image1);

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
