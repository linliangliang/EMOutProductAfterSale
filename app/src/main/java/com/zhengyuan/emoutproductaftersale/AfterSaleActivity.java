package com.zhengyuan.emoutproductaftersale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhengyuan.baselib.constants.EMProApplicationDelegate;
import com.zhengyuan.emoutproductaftersale.R;

/**
 * Created by zy on 2018/8/9.
 */

public class AfterSaleActivity extends Activity {
    String sname = EMProApplicationDelegate.userInfo.getUserId();
    private ImageButton backBtn;
    private ImageButton menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersale);
        backBtn = findViewById(R.id.title_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        menu = (ImageButton) findViewById(R.id.main_menu_bn);
        menu.setVisibility(View.GONE);

        TextView textView = findViewById(R.id.title_tv);
        textView.setText("售后工资结算依据录入" + "-" + sname);
    }
}
