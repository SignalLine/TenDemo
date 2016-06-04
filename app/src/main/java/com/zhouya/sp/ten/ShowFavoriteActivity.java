package com.zhouya.sp.ten;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zhouya.sp.ten.fragments.diagramfragments.SubDiagramFragment;
import com.zhouya.sp.ten.fragments.moviefragments.FirstFragment;
import com.zhouya.sp.ten.fragments.novelfragments.SubNovelFragment;

public class ShowFavoriteActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favorite);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("收藏展示");
            actionBar.setLogo(R.mipmap.back);
        }
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String flag = intent.getStringExtra("flag");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//        System.out.println("id = " + id + "===flag--" + flag);
        Fragment fragment = null;
        switch (flag) {

            case "影评":
                fragment = new FirstFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id",Integer.parseInt(id));
                fragment.setArguments(bundle);
                transaction.add(R.id.show_fav_layout, fragment);
                transaction.show(fragment);
                break;
            case "美文":
                fragment = new SubNovelFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("id",Integer.parseInt(id));
                fragment.setArguments(bundle1);
                transaction.add(R.id.show_fav_layout, fragment);
                transaction.show(fragment);
                break;
            case "美图":
                fragment = new SubDiagramFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("id",Integer.parseInt(id));
                fragment.setArguments(bundle2);
                transaction.add(R.id.show_fav_layout, fragment);
                transaction.show(fragment);
                break;
        }
        transaction.commit();
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
}
