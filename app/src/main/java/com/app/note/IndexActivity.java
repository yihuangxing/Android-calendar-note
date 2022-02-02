package com.app.note;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.app.note.base.BaseActivity;
import com.app.note.fragment.CollectionFragment;
import com.app.note.fragment.HomeFragment;
import com.app.note.fragment.MineFragment;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class IndexActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    private HomeFragment mHomeFragment;
    private CollectionFragment mCollectionFragment;
    private MineFragment mMineFragment;
    private BottomNavigationBar mBottomNavigationBar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mBottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setTabSelectedListener(this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setActiveColor("#2c2c2c") //选中颜色
                .setInActiveColor("#515151") //未选中颜色
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setBarBackgroundColor("#ffffff");//导航栏背景色


        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_user_home, "首页").setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.ic_user_home_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_user_collection, "收藏").setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.ic_user_collection_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_user_mine, "我的").setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.ic_user_mine_normal)))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    @Override
    protected void initData() {
        selectedFragment(0);
    }


    @Override
    public void onTabSelected(int position) {
        selectedFragment(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void selectedFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        if (position == 0) {
            if (mHomeFragment == null) {
                mHomeFragment = new HomeFragment();
                fragmentTransaction.add(R.id.content, mHomeFragment);
            } else {
                fragmentTransaction.show(mHomeFragment);
            }
        } else if (position == 1) {
            if (mCollectionFragment == null) {
                mCollectionFragment = new CollectionFragment();
                fragmentTransaction.add(R.id.content, mCollectionFragment);
            } else {
                fragmentTransaction.show(mCollectionFragment);
                mCollectionFragment.refreshData();
            }
        } else if (position == 2) {
            if (mMineFragment == null) {
                mMineFragment = new MineFragment();
                fragmentTransaction.add(R.id.content, mMineFragment);
            } else {
                fragmentTransaction.show(mMineFragment);

            }
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction beginTransaction) {
        if (null != mHomeFragment) {
            beginTransaction.hide(mHomeFragment);
        }

        if (null != mCollectionFragment) {
            beginTransaction.hide(mCollectionFragment);
        }
        if (null != mMineFragment) {
            beginTransaction.hide(mMineFragment);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2000 && mHomeFragment!=null) {
            mHomeFragment.refreshData();
        }
    }
}