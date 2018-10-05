package com.zhengyuan.emoutproductaftersale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhengyuan.baselib.constants.EMProApplicationDelegate;

/**
 * Created by xgs on 2018/9/5.
 */

public class OuterActivity extends Activity {
    String sname= EMProApplicationDelegate.userInfo.getUserId();
    private ImageButton backBtn;
    private ImageButton menu;
    private Button QulityEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outer);
        backBtn = findViewById(R.id.title_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        menu=(ImageButton)findViewById(R.id.main_menu_bn);
        menu.setVisibility(View.GONE);

        TextView textView = findViewById(R.id.title_tv);
        textView.setText("驻外工资结算依据录入"+"-"+sname);
        QulityEvent = findViewById(R.id.qulityEvent);
        QulityEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OuterActivity.this,QulityEventActivity.class);
                startActivity(intent);
            }
        });
    }
}
