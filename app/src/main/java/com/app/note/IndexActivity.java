package com.app.note;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.app.note.activity.LoginActivity;
import com.app.note.base.BaseActivity;
import com.app.note.fragment.CollectionFragment;
import com.app.note.fragment.HomeFragment;
import com.app.note.fragment.MapViewFragment;
import com.app.note.fragment.MineFragment;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    private static final String TAG = "okgo======";
    private HomeFragment mHomeFragment;
    private CollectionFragment mCollectionFragment;
    private MineFragment mMineFragment;
    private BottomNavigationBar mBottomNavigationBar;
    private MapViewFragment mMapViewFragment;


    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private boolean needCheckBackLocation = false;
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;


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
                .addItem(new BottomNavigationItem(R.mipmap.ic_user_map, "地图").setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.ic_user_map_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_user_collection, "收藏").setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.ic_user_collection_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.ic_user_mine, "我的").setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.ic_user_mine_normal)))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    @Override
    protected void initData() {
        selectedFragment(0);


        if(Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACKGROUND_LOCATION_PERMISSION
            };
        }
    }

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }
    }

    /**
     *
     * @param permissions
     * @since 2.5.0
     *
     */
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});

                    method.invoke(this, array, PERMISSON_REQUESTCODE);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     *
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23){
            try {
                for (String perm : permissions) {
                    Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                            String.class);
                    if ((Integer)checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean)shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                        if(!needCheckBackLocation
                                && BACKGROUND_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {

            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     *
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     *
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     *  启动应用的设置
     *
     * @since 2.5.0
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
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
        } else if (position==1){
            if (mMapViewFragment == null) {
                mMapViewFragment = new MapViewFragment();
                fragmentTransaction.add(R.id.content, mMapViewFragment);
            } else {
                fragmentTransaction.show(mMapViewFragment);
            }
        }

        else if (position == 2) {
            if (mCollectionFragment == null) {
                mCollectionFragment = new CollectionFragment();
                fragmentTransaction.add(R.id.content, mCollectionFragment);
            } else {
                fragmentTransaction.show(mCollectionFragment);
                mCollectionFragment.refreshData();
            }
        } else if (position == 3) {
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
        if (null != mMapViewFragment) {
            beginTransaction.hide(mMapViewFragment);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2000 && mHomeFragment != null) {
            mHomeFragment.refreshData();
        }
        if (resultCode == 3000 && mMapViewFragment != null) {
            Log.e(TAG, "onActivityResult: 22222222222222222222");
            mMapViewFragment.refreshData();
        }
    }


}