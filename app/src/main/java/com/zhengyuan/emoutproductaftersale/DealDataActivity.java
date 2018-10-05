package com.zhengyuan.emoutproductaftersale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.common.zxing.CaptureActivity;
import com.zhengyuan.baselib.constants.Constants;
import com.zhengyuan.baselib.constants.EMProApplicationDelegate;
import com.zhengyuan.baselib.listener.NetworkCallbacks;
import com.zhengyuan.baselib.utils.FileManagerUtil;
import com.zhengyuan.baselib.utils.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by zy on 2018/9/6.
 */

public class DealDataActivity extends Activity implements View.OnClickListener {
    String snameId = EMProApplicationDelegate.userInfo.getUserId();
    String username = EMProApplicationDelegate.userInfo.getUserName();

    private ImageButton backBtn;
    private ImageButton menu;

    private EditText qualityId;
    private EditText Ju;
    private EditText Duan;
    private EditText CarType;
    private EditText CarNumber;
    private EditText DealState;
    private EditText GZDisplay = null;
    private String result1;
    private Handler handler1;
    private String result2;
    private Handler handler2;
    private String result3;
    private Handler handler3;
    private ImageView takePhoto;
    public static final int REQUST_TAKE_PHOTTO_CODE2 = 2;
    private Button submitButton = null;
    private String info = "";
    private String flag = "";
    String[] photoPath = new String[3];
    String[] photoName = new String[3];

    String[] allPhotoName = new String[150];
    String[] allPhotoPath = new String[150];

    static int IMAGE = 0;
    ;
    private LinearLayout firsthuanshanghuanxiaitemLayout = null;
    private boolean showhuanshanghuanxiaitem = false;//默认是前三种情况，不显示换上换下
    private LinearLayout huanshanghuanxiaitemLayout = null;
    private Button addButton = null;
    private int huanshanghuanxiaitemCount = 0;//换上换下的记录数
    private String[][] contents = new String[100][6];//String[i][0] 换上物料代码，String[i][1]换上物料名称。String[i][2]换上序列号
    private Bitmap[][] imagesBitmap = new Bitmap[100][2];//ImageView[i][0];换上照片。ImageView[i][1];换下照片
    private EditText HSWLDMEdit = null;
    private EditText HSWLMCEdit = null;
    private EditText HSXLHEdit = null;
    private EditText HXWLDMEdit = null;
    private EditText HXWLMCEdit = null;
    private EditText HXXLHEdit = null;
    private ImageView HSScanImageView = null;
    private ImageView HSTakePhotoImageView = null;
    private ImageView HXScanImageView = null;
    private ImageView HXTakePhotoImageView = null;
    private EditText HSWLDMEditText = null;
    private EditText HXWLDMEditText = null;
    private String MaterialInfo = null;
    private Handler MaterialInfoHandler = null;
    private String index;
    private Button NESubmitButton = null;
    private Button TCSubmitButton = null;
    private EditText GZDealInfo = null;
    private int indexImage = 0;
    private String GZCLType = null;
    private String pathFor = null;
    private EditText BeiZhu = null;
    private String BeiZhuInfo = null;
    String imagePath = "";
    String smaoInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String[] datas = data.split("=");
        if (datas[0].equals("4")) {
            setContentView(R.layout.noexchange);
            takePhoto = findViewById(R.id.photoId);
            takePhoto.setTag(3);
            if (takePhoto != null) {
                takePhoto.setOnClickListener(this);
            }
            GZCLType = datas[7];
        } else {
            setContentView(R.layout.twocarshd);
            showhuanshanghuanxiaitem = true;
            GZCLType = datas[7];

        }
        backBtn = findViewById(R.id.title_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        TextView textView = findViewById(R.id.title_tv);
        textView.setText("质量信息工资依据录入" + "-" + snameId);

        init();

        //填充数据
        getJuDuanInfo(datas[1]);
        qualityId.setText(datas[1]);
        CarType.setText(datas[2]);
        CarNumber.setText(datas[4]);
        DealState.setText(datas[5]);
        GZDisplay.setText(datas[6]);

        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (result1.equals("") || result1 == null) {
                    Toast.makeText(DealDataActivity.this, "系统故障,请联系信息中心!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DealDataActivity.this, result1, Toast.LENGTH_SHORT).show();
                    String[] res = result1.split("=");
                    Ju.setText(res[0]);
                    Duan.setText(res[1]);
                }
            }
        };
        handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (result2.equals("true")) {
                    submitButton.setBackgroundResource(R.drawable.shape_rectangle_radius_theme);
                    submitButton.setEnabled(true);
                    Toast.makeText(DealDataActivity.this, "数据提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DealDataActivity.this, "系统故障,提交失败，请联系信息中心!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        handler3 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (result3.equals("true")) {
                    submitButton.setBackgroundResource(R.drawable.shape_rectangle_radius_theme);
                    submitButton.setEnabled(true);
                    Toast.makeText(DealDataActivity.this, "数据提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DealDataActivity.this, "系统故障,提交失败，请联系信息中心!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        MaterialInfoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (MaterialInfo.equals("") || MaterialInfo == null) {
                    Toast.makeText(DealDataActivity.this, "序列号有误，没有找到物料信息！", Toast.LENGTH_SHORT).show();
                } else {
                    if (index.equals("hs")) {
                        HSWLDMEdit.setText(MaterialInfo.split("=")[0]);
                        HSWLMCEdit.setText(MaterialInfo.split("=")[1]);
                    } else if (index.equals("hx")) {
                        HXWLDMEdit.setText(MaterialInfo.split("=")[0]);
                        HXWLMCEdit.setText(MaterialInfo.split("=")[1]);
                    }
                    //Toast.makeText(DealDataActivity.this, MaterialInfo, Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Add_Button:
                if (InfoIsOkAddToArrayAndShow()) {
                    Toast.makeText(DealDataActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    sortHuanShangHuanXiaItem();
                }
                break;
            case R.id.photoId:
                IMAGE = (Integer) view.getTag();
                takePhoto();
                indexImage++;
                break;
            case R.id.HSSacnImageView:
                flag = "HSScan";
                sweep(view);
                break;
            case R.id.HXSacnImageView:
                flag = "HXScan";
                sweep(view);
                break;
            case R.id.HSTakePhotoImageView:
                IMAGE = (Integer) view.getTag();
                takePhoto();
                break;
            case R.id.HXTakePhotoImageView:
                IMAGE = (Integer) view.getTag();
                takePhoto();
                break;
            default:
                break;
        }
    }


    private void init() {

        menu=(ImageButton)findViewById(R.id.main_menu_bn);
        menu.setVisibility(View.GONE);

        qualityId = findViewById(R.id.QualityId);
        Ju = findViewById(R.id.Ju);
        Duan = findViewById(R.id.Duan);
        CarType = findViewById(R.id.CarType);
        CarNumber = findViewById(R.id.CarNumber);
        DealState = findViewById(R.id.DealState);
        GZDisplay = findViewById(R.id.GZDisplay);

        if (GZDisplay != null) {//为edit添加touch事件的监听
            GZDisplay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    /*加这个判断，防止该事件被执行两次*/
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        showInfoDetailDialog(GZDisplay.getText().toString());
                    }
                    return false;
                }
            });
        }

        if (showhuanshanghuanxiaitem) {//如果要显示换上换下
            submitButton = findViewById(R.id.TCSubmitButton);
            firsthuanshanghuanxiaitemLayout = (LinearLayout) findViewById(R.id.firsthuanshanghuanxiaitemLayout);
            huanshanghuanxiaitemLayout = (LinearLayout) findViewById(R.id.huanshanghuanxiaitemLayout);

            View firsthuanshanghuanxiaitem = View.inflate(this, R.layout.huanshanghuanxiaitem, null);
            HSWLDMEdit = (EditText) firsthuanshanghuanxiaitem.findViewById(R.id.HSWLDMEditText);
            HSWLMCEdit = (EditText) firsthuanshanghuanxiaitem.findViewById(R.id.HSWLMCEditText);
            HSXLHEdit = (EditText) firsthuanshanghuanxiaitem.findViewById(R.id.HSXLHEditText);
            HXWLDMEdit = (EditText) firsthuanshanghuanxiaitem.findViewById(R.id.HXWLDMEditText);
            HXWLMCEdit = (EditText) firsthuanshanghuanxiaitem.findViewById(R.id.HXWLMCEditText);
            HXXLHEdit = (EditText) firsthuanshanghuanxiaitem.findViewById(R.id.HXXLHEditText);
            HSScanImageView = (ImageView) firsthuanshanghuanxiaitem.findViewById(R.id.HSSacnImageView);
            HSTakePhotoImageView = (ImageView) firsthuanshanghuanxiaitem.findViewById(R.id.HSTakePhotoImageView);
            HXScanImageView = (ImageView) firsthuanshanghuanxiaitem.findViewById(R.id.HXSacnImageView);
            HXTakePhotoImageView = (ImageView) firsthuanshanghuanxiaitem.findViewById(R.id.HXTakePhotoImageView);
            //HSXLHEditText HXXLHEditText

            firsthuanshanghuanxiaitemLayout.addView(firsthuanshanghuanxiaitem);
            HSTakePhotoImageView.setTag(1);
            HXTakePhotoImageView.setTag(2);

            if (HSScanImageView != null) {
                HSScanImageView.setOnClickListener(this);
            }
            if (HXScanImageView != null) {
                HXScanImageView.setOnClickListener(this);
            }
            if (HSTakePhotoImageView != null) {
                HSTakePhotoImageView.setOnClickListener(this);
            }
            if (HXTakePhotoImageView != null) {
                HXTakePhotoImageView.setOnClickListener(this);
            }
            //为换上换下的序列号 添加焦点失去的事件
            if (HSXLHEdit != null) {
                HSXLHEdit.setOnFocusChangeListener(new android.view.View.
                        OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            // 此处为得到焦点时的处理内容
                            return;
                        } else {
                            //发送获取物料代码的请求
                            String temp = HSXLHEdit.getText().toString().trim();
                            if (temp != null && temp.length() > 0) {
                                getMaterialInfo(temp);
                                index = "hs";
                            } else {
                                HSWLDMEdit.setText("");
                                HSWLMCEdit.setText("");
                            }
                        }
                    }
                });
            }
            if (HXXLHEdit != null) {
                HXXLHEdit.setOnFocusChangeListener(new android.view.View.
                        OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            // 此处为得到焦点时的处理内容
                            return;
                        } else {
                            //发送获取物料代码的请求
                            String temp = HXXLHEdit.getText().toString().trim();
                            if (temp != null && temp.length() > 0) {
                                getMaterialInfo(temp);
                                index = "hx";
                            } else {
                                HXWLDMEdit.setText("");
                                HXWLMCEdit.setText("");
                            }
                        }
                    }
                });
            }
            addButton = firsthuanshanghuanxiaitem.findViewById(R.id.Add_Button);
            addButton.setOnClickListener(this);
            //获取提交信息的按钮
            TCSubmitButton = findViewById(R.id.TCSubmitButton);
            BeiZhu = findViewById(R.id.BeiZhu);
            TCSubmitButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BeiZhuInfo = BeiZhu.getText().toString();
                    if (BeiZhuInfo.equals("")) {
                        Toast.makeText(DealDataActivity.this, "请完善备注信息！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    smaoInfo = getDealBreakdownDataInfo().toString();
                    if (!(smaoInfo.toString().equals(""))) {

                        TCSubmitButton.setBackgroundResource(R.drawable.shape_rectangle_radius_gray);
                        TCSubmitButton.setEnabled(false);
                        imagePath = getImagePath().toString();
                        //上传图片到服务器
                        new Thread(new Runnable() {
                            //处理图片上传的功能
                            @Override
                            public void run() {
                                for (int i = 0; i < 2 * huanshanghuanxiaitemCount; i++) {
                                    if (allPhotoPath[i] != null && !allPhotoPath[i].equals("")) {
                                        boolean result = FileManagerUtil.uploadFileByUrl(new File(allPhotoPath[i]),
                                                allPhotoName[i],
                                                Constants.UPLOAD_IMAGE_URL);
                                    }
                                }

                                submitData2(DataInfo(), BeiZhuInfo, snameId, username, GZCLType, imagePath, smaoInfo);
                            }
                        }).start();

                    } else {
                        Toast.makeText(DealDataActivity.this, "请完善备注信息！", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            submitButton = findViewById(R.id.NESubmitButton);
            //事件进行监听
            GZDealInfo = findViewById(R.id.GZDealInfo);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photoName.length >= 2) {
                        pathFor = photoName[2];
                    }
                    //提交信息的处理
                    if (!(GZDealInfo.getText().toString().equals(""))) {
                        //暂时不判断图片是否存在的情况
                        //提交数据 data
                        //禁用按钮
                        submitButton.setBackgroundResource(R.drawable.shape_rectangle_radius_gray);
                        submitButton.setEnabled(false);
                        //上传图片到服务器
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (photoPath[2] != null && !photoPath[2].equals("")) {
                                    boolean result = FileManagerUtil.uploadFileByUrl(new File(photoPath[2]),
                                            photoName[2],
                                            Constants.UPLOAD_IMAGE_URL);
                                }
                                submitData(DataInfo(), GZDealInfo.getText().toString().trim(), snameId, username, GZCLType, pathFor);
                            }
                        }).start();

                    } else {
                        Toast.makeText(DealDataActivity.this, "请完善故障处理措施信息！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    //添加一条item
    private boolean InfoIsOkAddToArrayAndShow() {
        //临时变量
        String HSWLDMText = HSWLDMEdit.getText().toString().trim();
        String HSWLMCText = HSWLMCEdit.getText().toString().trim();
        String HSXLHText = HSXLHEdit.getText().toString().trim();
        String HXWLDMText = HXWLDMEdit.getText().toString().trim();
        String HXWLMCText = HXWLMCEdit.getText().toString().trim();
        String HXXLHText = HXXLHEdit.getText().toString().trim();

        String HSImage = photoName[0];
        String HXImage = photoName[1];

        //如果所有数据填充完毕，则添加条目
        if ((!"".equals(HSWLDMText)) & (null != HSWLDMText) & (!"".equals(HSWLMCText)) & (null != HSWLMCText)
                & ((!"".equals(HSXLHText)) & (null != HSXLHText)) & ((!"".equals(HXWLDMText)) & (null != HXWLDMText))
                & ((!"".equals(HXWLMCText)) & (null != HXWLMCText)) & ((!"".equals(HXXLHText)) & (null != HXXLHText))
                & HSImage != null & HXImage != null) {

            if (huanshanghuanxiaitemCount < 100) {

                //添加一条数据暂存
                contents[huanshanghuanxiaitemCount][0] = HSWLDMText;
                contents[huanshanghuanxiaitemCount][1] = HSWLMCText;
                contents[huanshanghuanxiaitemCount][2] = HSXLHText;
                contents[huanshanghuanxiaitemCount][3] = HXWLDMText;
                contents[huanshanghuanxiaitemCount][4] = HXWLMCText;
                contents[huanshanghuanxiaitemCount][5] = HXXLHText;
                imagesBitmap[huanshanghuanxiaitemCount][0] = ((BitmapDrawable) HSTakePhotoImageView.getDrawable()).getBitmap();
                imagesBitmap[huanshanghuanxiaitemCount][1] = ((BitmapDrawable) HXTakePhotoImageView.getDrawable()).getBitmap();
                //显示一个item
                View huanshanghuanxiadeleteitem = View.inflate(this, R.layout.huanshanghuanxiadeleteitem, null);
                ((EditText) huanshanghuanxiadeleteitem.findViewById(R.id.HSWLDMDeleteEditText)).setText(contents[huanshanghuanxiaitemCount][0]);
                ((EditText) huanshanghuanxiadeleteitem.findViewById(R.id.HSWLMCDeleteEditText)).setText(contents[huanshanghuanxiaitemCount][1]);
                ((EditText) huanshanghuanxiadeleteitem.findViewById(R.id.HSXLHDeleteEditText)).setText(contents[huanshanghuanxiaitemCount][2]);
                ((EditText) huanshanghuanxiadeleteitem.findViewById(R.id.HXWLDMDeleteEditText)).setText(contents[huanshanghuanxiaitemCount][3]);
                ((EditText) huanshanghuanxiadeleteitem.findViewById(R.id.HXWLMCDeleteEditText)).setText(contents[huanshanghuanxiaitemCount][4]);
                ((EditText) huanshanghuanxiadeleteitem.findViewById(R.id.HXXLHDeleteEditText)).setText(contents[huanshanghuanxiaitemCount][5]);
                ((ImageView) huanshanghuanxiadeleteitem.findViewById(R.id.HSTakePhotoDeleteImageView)).setImageBitmap(imagesBitmap[huanshanghuanxiaitemCount][0]);
                ((ImageView) huanshanghuanxiadeleteitem.findViewById(R.id.HXTakePhotoDeleteImageView)).setImageBitmap(imagesBitmap[huanshanghuanxiaitemCount][1]);
                huanshanghuanxiaitemLayout.addView(huanshanghuanxiadeleteitem);
                photoName[0] = null;//添加成功后清除变量
                photoName[1] = null;//添加成功后清除变量
                huanshanghuanxiaitemCount++;


                //清空输入框内容，只保留负责人栏目
                HSWLDMEdit.setText("");
                HSWLMCEdit.setText("");
                HSXLHEdit.setText("");
                HXWLDMEdit.setText("");
                HXWLMCEdit.setText("");
                HXXLHEdit.setText("");
                HSTakePhotoImageView.setImageResource(R.mipmap.takephoto);
                HXTakePhotoImageView.setImageResource(R.mipmap.takephoto);
                return true;

            } else {
                Toast.makeText(DealDataActivity.this, "记录数大于100，请先提交", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DealDataActivity.this, "请填入完整信息！", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //遍历为item的button添加删除的监听器
    private void sortHuanShangHuanXiaItem() {
        Log.v("DealDataActivity", "sortHuanShangHuanXiaItem");
        if (huanshanghuanxiaitemLayout != null) {
            for (int i = 0; i < huanshanghuanxiaitemLayout.getChildCount(); i++) {
                final View view = huanshanghuanxiaitemLayout.getChildAt(i);
                final int position = i;
                final Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItemDialog(position);
                        sortHuanShangHuanXiaItem();
                    }
                });
            }
        } else {
        }
        Log.v("DealDataActivity", "sortHuanShangHuanXiaItem");
    }

    private void deleteItemDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.tip);
        builder.setTitle("删除提示");
        builder.setMessage("确认删除该项");
        builder.setPositiveButton("确认删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //刷新显示，删除数组中的记录
                        huanshanghuanxiaitemLayout.removeViewAt(position);
                        deleteFromContents(position);
                        Log.v("确认删除", "确认删除");
                    }
                }
        );
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteFromContents(int position) {
        for (int i = position; i < huanshanghuanxiaitemCount; i++) {
            if (i + 1 < huanshanghuanxiaitemCount) {
                contents[i] = contents[i + 1];
            }
        }
        huanshanghuanxiaitemCount--;
        return;
    }

    private void showInfoDetailDialog(String info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.tip);
        builder.setTitle("详细详细");
        builder.setMessage(info);
        builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getJuDuanInfo(String s1) {
        DataObtainer.INSTANCE.getJDInfo(s1,
                new NetworkCallbacks.SimpleDataCallback() {
                    @Override
                    public void onFinish(boolean b, String s, Object o) {
                        result1 = (String) o;
                        Message m = handler1.obtainMessage();
                        handler1.sendMessage(m);
                    }
                }
        );
    }

    //getMaterialInfo 通过序列号查询物料代码和物料名称
    public void getMaterialInfo(String severialNumber) {
        if (severialNumber != null && severialNumber.length() > 0) {
            //如果序列号不为空，查询
            DataObtainer.INSTANCE.getMaterialInfo(severialNumber,
                    new NetworkCallbacks.SimpleDataCallback() {
                        @Override
                        public void onFinish(boolean b, String s, Object o) {
                            MaterialInfo = (String) o;
                            Message m = MaterialInfoHandler.obtainMessage();
                            MaterialInfoHandler.sendMessage(m);
                        }
                    }
            );
        }
    }

    //显示照片
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            info = data.getStringExtra("result");
            info = recode(info);
            if (info != null && info != "") {
                if (flag.equals("HSScan")) {
                    //把值对应起来
                    HXWLDMEditText.setText(info);
                } else if (flag.equals("HSScan")) {
                    HSWLDMEditText.setText(info);
                }
            } else {
                Utils.showToast("未扫描");
            }
        } else if (requestCode == REQUST_TAKE_PHOTTO_CODE2 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bm = (Bitmap) bundle.get("data");
                if (bm != null) {
                    switch (IMAGE) {
                        case 1:
                            ImageToGallery.saveImageToGallery(getApplicationContext(), bm);
                            photoPath[IMAGE - 1] = ImageToGallery.filePath;
                            photoName[IMAGE - 1] = ImageToGallery.fileName;
                            Glide.with(this).load(ImageToGallery.filePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                    //图片加载完成
                                    HSTakePhotoImageView.setImageBitmap(bitmap);
                                }
                            });
                            //暂存图片
                            allPhotoName[huanshanghuanxiaitemCount * 2 + 0] = photoName[0];
                            allPhotoPath[huanshanghuanxiaitemCount * 2 + 0] = photoPath[0];

                            break;
                        case 2:
                            ImageToGallery.saveImageToGallery(getApplicationContext(), bm);
                            photoPath[IMAGE - 1] = ImageToGallery.filePath;
                            photoName[IMAGE - 1] = ImageToGallery.fileName;
                            Glide.with(this).load(ImageToGallery.filePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                    //图片加载完成
                                    HXTakePhotoImageView.setImageBitmap(bitmap);
                                }
                            });
                            allPhotoName[huanshanghuanxiaitemCount * 2 + 1] = photoName[1];
                            allPhotoPath[huanshanghuanxiaitemCount * 2 + 1] = photoPath[1];
                            break;
                        case 3:
                            ImageToGallery.saveImageToGallery(getApplicationContext(), bm);
                            photoPath[IMAGE - 1] = ImageToGallery.filePath;
                            photoName[IMAGE - 1] = ImageToGallery.fileName;
                            Glide.with(this).load(ImageToGallery.filePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                    //图片加载完成
                                    takePhoto.setImageBitmap(bitmap);
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
            } else {
                //Toast.makeText(Carout_details.this, "没有压缩的图片数据", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void sweep(View view) {
        Intent intent = new Intent();
        intent.setClass(this, CaptureActivity.class);
        intent.putExtra("autoEnlarged", true);
        startActivityForResult(intent, 0);
    }

    //解决乱码问题
    public String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            } else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }

    //调用相机
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageToGallery.filePath);
        startActivityForResult(intent, REQUST_TAKE_PHOTTO_CODE2);
    }

    //获取所有的组件
/*    public void getComponent(){
        GZDealInfo  = findViewById(R.id.GZDealInfo);
         submitButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                //提交信息的处理
                  if(GZDealInfo.getText().equals("")||GZDealInfo==null){
                     //暂时不判断图片是否存在的情况
                      //提交数据 data
                      //禁用按钮
                      submitButton.setBackgroundResource(R.drawable.shape_rectangle_radius_gray);
                      submitButton.setEnabled(false);
                      //上传图片到服务器
                      new Thread(new Runnable() {
                          @Override
                          public void run() {
                              // TODO Auto-generated method stub
                                  if (photoPath[0] != null && !photoPath.equals("")) {
                                      boolean result = FileManagerUtil.uploadFileByUrl(new File(photoPath[0]),
                                              photoName[0],
                                              Constants.UPLOAD_IMAGE_URL);
                              }
                              submitData(DataInfo(),GZDealInfo.getText().toString().trim(),snameId,username,GZCLType);
                          }
                      }).start();

                  }else{
                      Toast.makeText(DealDataActivity.this, "请完善故障处理措施信息！", Toast.LENGTH_SHORT).show();
                  }
              }
          });
    }*/
    //获取信息，并且把信息拼接起来
    public String DataInfo() {
        String res = "";
        qualityId = findViewById(R.id.QualityId);
        Ju = findViewById(R.id.Ju);
        Duan = findViewById(R.id.Duan);
        CarType = findViewById(R.id.CarType);
        CarNumber = findViewById(R.id.CarNumber);
        DealState = findViewById(R.id.DealState);
        GZDisplay = findViewById(R.id.GZDisplay);
        res = qualityId.getText() + "=" + Ju.getText() + "=" + Duan.getText() + "=" + CarType.getText() + "=" + CarNumber.getText() + "=" + GZDisplay.getText();
        return res;
    }

    public String getDealBreakdownDataInfo() {
        String result = "";
        //先获取已经添加的
        if (huanshanghuanxiaitemLayout != null) {
            int itemsCount = huanshanghuanxiaitemLayout.getChildCount();//添加换上换下的个数
            if (itemsCount > 0) {
                for (int i = 0; i < itemsCount; i++) {
                    View tem = huanshanghuanxiaitemLayout.getChildAt(i);
                    result += ((EditText) tem.findViewById(R.id.HSWLDMDeleteEditText)).getText().toString() + "=";
                    result += ((EditText) tem.findViewById(R.id.HSWLMCDeleteEditText)).getText().toString() + "=";
                    result += ((EditText) tem.findViewById(R.id.HSXLHDeleteEditText)).getText().toString() + "=";
                    result += ((EditText) tem.findViewById(R.id.HXWLDMDeleteEditText)).getText().toString() + "=";
                    result += ((EditText) tem.findViewById(R.id.HXWLMCDeleteEditText)).getText().toString() + "=";
                    result += ((EditText) tem.findViewById(R.id.HXXLHDeleteEditText)).getText().toString() + "=";
                }
            }

        }
        //再获取最后一条记录
        if (firsthuanshanghuanxiaitemLayout != null) {
            View view = firsthuanshanghuanxiaitemLayout.getChildAt(0);
            String t1 = ((EditText) view.findViewById(R.id.HSWLDMEditText)).getText().toString();
            String t2 = ((EditText) view.findViewById(R.id.HSWLMCEditText)).getText().toString();
            String t3 = ((EditText) view.findViewById(R.id.HSXLHEditText)).getText().toString();
            String t4 = ((EditText) view.findViewById(R.id.HXWLDMEditText)).getText().toString();
            String t5 = ((EditText) view.findViewById(R.id.HXWLMCEditText)).getText().toString();
            String t6 = ((EditText) view.findViewById(R.id.HXXLHEditText)).getText().toString();


            if ((t1 != "" && t1 != null) && (t2 != "" && t2 != null) && (t3 != "" && t3 != null) && (t4 != "" && t4 != null) && (t4 != "" && t4 != null) && (t4 != "" && t4 != null)) {
                result += t1 + "=";
                result += t2 + "=";
                result += t3 + "=";
                result += t4 + "=";
                result += t5 + "=";
                result += t6 + "=";
                huanshanghuanxiaitemCount++;
            }
        }

        if (result != "" && result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private void submitData(String s1, String s2, String snameId, String username, String GZCLType, String pathFor) {
        //获取并提交数据
        DataObtainer.INSTANCE.sendInfoByFour(s1, s2, snameId, username, GZCLType, pathFor,
                new NetworkCallbacks.SimpleDataCallback() {
                    @Override
                    public void onFinish(boolean b, String s, Object o) {
                        if (o.equals("")) {
                            Utils.showToast("没有找到！");
                            return;
                        }
                        result2 = (String) o;
                        Message m = handler2.obtainMessage();
                        handler2.sendMessage(m);
                    }
                }
        );
    }

    //提交第二次的页面的数据
    private void submitData2(String s1, String s2, String snameId, String username, String GZCLType, String pathFor, String hshxInfo) {
        //获取并提交数据
        DataObtainer.INSTANCE.sendInfoByAll(s1, s2, snameId, username, GZCLType, pathFor, hshxInfo,
                new NetworkCallbacks.SimpleDataCallback() {
                    @Override
                    public void onFinish(boolean b, String s, Object o) {
                        if (o.equals("")) {
                            Utils.showToast("没有找到！");
                            return;
                        }
                        result3 = (String) o;
                        Message m = handler3.obtainMessage();
                        handler3.sendMessage(m);
                    }
                }
        );
    }

    //获取上传图片的路径
    public String getImagePath() {
        String allpath = "";
        for (int i = 0; i < 2 * huanshanghuanxiaitemCount; i++) {
            allpath += allPhotoName[i] + "=";
        }
        if (allpath != "" && allpath.length() > 0) {
            allpath = allpath.substring(0, allpath.length() - 1);
        }
        return allpath;
    }

}


