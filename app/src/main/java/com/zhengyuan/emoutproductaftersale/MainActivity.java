package com.zhengyuan.emoutproductaftersale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class MainActivity extends Activity {
    String sname = null;
    //sname=EMProApplicationDelegate.userInfo.getUserId();
    private ImageButton backBtn;
    private ImageButton menu;
    private Button Outer = null;
    private Button AfterSale = null;

    private ImageView testtakephoto = null;

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

        Outer = (Button) findViewById(R.id.outer);
        AfterSale = (Button) findViewById(R.id.aftersale);
        Outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OuterActivity.class);
                startActivity(intent);
            }

        });
        AfterSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AfterSaleActivity.class);
                startActivity(intent);
            }
        });


        testtakephoto = (ImageView) findViewById(R.id.testtakephoto);
        testtakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageToGallery.filePath);
                startActivityForResult(intent, 2);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            //Toast.makeText(TakePhotoAndSave.this,"RESULT_OK",Toast.LENGTH_SHORT).show();
            //拍照结果处理
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bm = (Bitmap) bundle.get("data");
                if (bm != null) {
                    ImageToGallery.saveImageToGallery(getApplicationContext(), bm);

                    Glide.with(this).load(ImageToGallery.filePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                            //图片加载完成
                            testtakephoto.setImageBitmap(bitmap);
                        }
                    });

                }
            } else {
                //Toast.makeText(Carout_details.this, "没有压缩的图片数据", Toast.LENGTH_LONG).show();
            }
        }
    }
}
