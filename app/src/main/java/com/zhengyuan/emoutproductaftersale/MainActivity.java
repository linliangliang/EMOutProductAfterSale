package com.zhengyuan.emoutproductaftersale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhengyuan.baselib.constants.EMProApplicationDelegate;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    String sname = EMProApplicationDelegate.userInfo.getUserId();
    private ImageButton backBtn;
    private ImageButton menu;


    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.title_tv);
        if (sname != null) {
            textView.setText("驻外售后平台" + "-" + sname);
        } else {
            textView.setText("驻外售后平台");
        }

        backBtn = (ImageButton) findViewById(R.id.title_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menu = (ImageButton) findViewById(R.id.main_menu_bn);
        menu.setVisibility(View.GONE);
        init();
    }

    private void init() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(OutProductFragment.newInstance("驻外"));
        list.add(AfterSaleFragment.newInstance("售后"));
        viewPagerAdapter.setList(list);

    }

    private void initEvent() {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigationOuter:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigationAfterSale:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };

}
