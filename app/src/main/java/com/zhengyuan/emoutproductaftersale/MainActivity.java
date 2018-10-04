package com.zhengyuan.emoutproductaftersale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
    String sname= null;
    //sname=EMProApplicationDelegate.userInfo.getUserId();
   private ImageButton backBtn;
    private Button Outer=null;
    private Button AfterSale=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.title_tv);
        if(sname !=null){
            textView.setText("驻外售后平台"+"-"+sname);
        }else{
            textView.setText("驻外售后平台");
        }

        backBtn = findViewById(R.id.title_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Outer=(Button)findViewById(R.id.outer);
        AfterSale=(Button)findViewById(R.id.aftersale);
        Outer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OuterActivity.class);
                startActivity(intent);
            }

        });
        AfterSale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AfterSaleActivity.class);
                startActivity(intent);
            }
        });
    }
}
