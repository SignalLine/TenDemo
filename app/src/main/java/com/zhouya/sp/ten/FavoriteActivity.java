package com.zhouya.sp.ten;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private MySQLiteOpenHelper mSQLiteOpenHelper;
    private ArrayList<String> mCreateSQLStr;
    private List<Map<String, String>> mLists;
    private int mPosition;
    private SimpleAdapter mAdapter;
    private String mContentId;
    private int mSize;
    private TextView mMFavorite_tv_num;
    private LinearLayout mLinearLayout;
    private LinearLayout mLinearLayout2;
    private String mTitle;
    private String mFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("我的收藏");
            actionBar.setLogo(R.mipmap.back);
        }
        //初始化数据库
        mCreateSQLStr = new ArrayList<>();
        String insertFavSql = "create table if not exists Favorites (_id integer primary key,id varchar2 not null,author varchar2 not null,title varchar2 not null,detailsUrl varchar2,imageUrl varchar2,publishTime varchar2)";
        mCreateSQLStr.add(insertFavSql);
        mSQLiteOpenHelper = new MySQLiteOpenHelper(this,ConstantConfig.MY_FAVORITE,1, mCreateSQLStr,null);

        mLinearLayout = (LinearLayout) findViewById(R.id.favorite_ll);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.favorite_lv_ll);
        ListView mListView = (ListView) findViewById(R.id.favorite_lv);
        mMFavorite_tv_num = (TextView)findViewById(R.id.favorite_tv_num);

//        TextView favorite_tv_flag = (TextView)findViewById(R.id.favorite_tv_flag);
//        TextView favorite_tv_publishTime = (TextView)findViewById(R.id.favorite_tv_publishTime);


        String selectSql = "select id,author,title,flag,publishTime from " + ConstantConfig.FAVORITES;
        mLists = mSQLiteOpenHelper.selectList(selectSql, null);
        mSize = mLists.size();
        if (mListView != null) {
            if(mLinearLayout != null && mLinearLayout2 != null ){
                if (mLists != null && mMFavorite_tv_num != null) {
                    if(mSize > 0){
                        mLinearLayout2.setVisibility(View.VISIBLE);
                        mLinearLayout.setVisibility(View.INVISIBLE);
                        //获取id

                        mMFavorite_tv_num.setText(mSize + "个内容被你收藏，愿他们曾伴你好梦");
                        mAdapter = new SimpleAdapter(this,mLists, R.layout.item_favorites,new String[]{"id","author","title","flag","publishTime"},new int[]{R.id.favorite_lv_id,R.id.favorite_lv_author,R.id.favorite_lv_title,R.id.favorite_tv_flag,R.id.favorite_tv_publishTime});
                        mListView.setAdapter(mAdapter);

                        registerForContextMenu(mListView);
                    }else{
                        mLinearLayout.setVisibility(View.VISIBLE);
                        mLinearLayout2.setVisibility(View.INVISIBLE);
                    }

                }else{
                    mLinearLayout.setVisibility(View.VISIBLE);
                    mLinearLayout2.setVisibility(View.INVISIBLE);
                }
            }

            //mListView监听事件
            mListView.setOnItemClickListener(this);

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.main, menu);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        mPosition = acmi.position;
        Map<String, String> stringMap = mLists.get(mPosition);
        mContentId = stringMap.get("id");
        mTitle = stringMap.get("title");
        mFlag = stringMap.get("flag");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        mLists.remove(mPosition);
        if (mContentId != null) {
            mSQLiteOpenHelper.execData("delete from Favorites where id=?", new String[]{mContentId});
            mSize--;
            if(mSize == 0){
                mLinearLayout.setVisibility(View.VISIBLE);
                mLinearLayout2.setVisibility(View.INVISIBLE);
            }else{
                mLinearLayout.setVisibility(View.INVISIBLE);
                mLinearLayout2.setVisibility(View.VISIBLE);

                mMFavorite_tv_num.setText(mSize + "个内容被你收藏，愿他们曾伴你好梦");
                mAdapter.notifyDataSetChanged();
            }
        }
        Toast.makeText(FavoriteActivity.this, "您删除了--" + mTitle, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = true;
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return ret;
    }

    @Override
    protected void onDestroy() {
        mLinearLayout2.clearFocus();
        mLinearLayout2 = null;
        mLinearLayout.clearFocus();
        mLinearLayout = null;

        mCreateSQLStr.clear();
        mCreateSQLStr = null;
        mSQLiteOpenHelper.destroy();
        mAdapter = null;
        mLists.clear();
        mLists = null;
        super.onDestroy();
    }



    //-listView点击事件----------------------
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> stringMap = mLists.get(position);
        mContentId = stringMap.get("id");
        mTitle = stringMap.get("title");
        mFlag = stringMap.get("flag");
        Intent it = new Intent(this,ShowFavoriteActivity.class);
        it.putExtra("id",mContentId);
        it.putExtra("flag",mFlag);
        startActivity(it);
    }
}
