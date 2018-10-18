package com.zhengyuan.emoutproductaftersale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengyuan.baselib.constants.EMProApplicationDelegate;
import com.zhengyuan.baselib.listener.NetworkCallbacks;
import com.zhengyuan.emoutproductaftersale.utils.DrawableUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xgs on 2018/9/4.
 */

public class QulityEventActivity extends Activity {
    String sname = EMProApplicationDelegate.userInfo.getUserId();
    private ImageButton backBtn;
    private ImageButton menu;
    //父布局
    private LinearLayout qulityInfoListLinearLayout = null;
    private int qualityInfoCounts = 0;//父布局的item数目
    private TextView emptyTextView;//没有数据的时候，提示用户

    //从服务器取得数据
    private String[] contens = null;
    private String[] contens2 = null;
    private int itemIncludeRecords = 6;//一条记录几个字段
    private int items = 0;//记录数
    private int items2 = 0;//记录数

    //记录哪个单选框被选中，没有则是-1
    private int whichOneChecked = -1;

    //下拉框的数据和数量
    private Spinner companyNameSpinner = null;
    private List<String> companyList = null;
    private ArrayAdapter<String> companyAdapter = null;
    private String[] company = null;
    private int companyCount = 0;
    private String chooseCompany = null;

    private String result1;
    private Handler handler1;
    private String result2;
    private Handler handler2;
    private Handler qulityInfoByCarHandler;
    private String qulityInfoByCar;

    private EditText twoCarsHD;
    private EditText CompanyMoney;
    private EditText OtherMoney;
    private EditText NoHPCL;

    public ImageView serch;
    private EditText SercherBy;

    //进度条
    private Loading_view loadingProressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qulity_info);
        //调用数据
        getSelectInfo();
        backBtn = findViewById(R.id.title_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menu = (ImageButton) findViewById(R.id.main_menu_bn);
        menu.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.title_tv);
        textView.setText("质量事件处理" + "-" + sname);


        twoCarsHD = findViewById(R.id.twoCars);
        CompanyMoney = findViewById(R.id.CompanyMoney);
        OtherMoney = findViewById(R.id.OtherMoney);
        NoHPCL = findViewById(R.id.NoExchange);
        serch = findViewById(R.id.SercherInfo);
        SercherBy = findViewById(R.id.SercherBy);
        //为搜索框中的图标添加响应事件
        DrawableUtil drawableUtil = new DrawableUtil(SercherBy, new DrawableUtil.OnDrawableListener() {
            @Override
            public void onLeft(View v, Drawable left) {
            }

            @Override
            public void onRight(View v, Drawable right) {
                //获取数据
                String res = SercherBy.getText().toString();
                String contents[] = res.trim().split(" ");
                if (res == null || res.equals("")) {
                    Toast.makeText(QulityEventActivity.this, "请输入车型，车号后搜索", Toast.LENGTH_SHORT).show();
                    return;
                } else if (contents.length == 1) {
                    Toast.makeText(QulityEventActivity.this, "请空格隔开，输入车型和车号", Toast.LENGTH_SHORT).show();
                    return;
                    //解析用户输入的数据
                } else if (contents.length == 2) {
                    ClearContents();
                    getQulityEventInfoByCar(contents[1], contents[0]);
                    showProgressBar();
                } else {
                    Toast.makeText(QulityEventActivity.this, "请按照正确的格式输入数据", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        //
        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取数据
                String res = SercherBy.getText().toString();
                String contents[] = res.trim().split(" ");
                if (res == null || res.equals("")) {
                    Toast.makeText(QulityEventActivity.this, "请输入车型，车号后搜索", Toast.LENGTH_SHORT).show();
                    return;
                } else if (contents.length == 1) {
                    Toast.makeText(QulityEventActivity.this, "请空格隔开，输入车型或者车号", Toast.LENGTH_SHORT).show();
                    return;
                    //解析用户输入的数据
                } else if (contents.length == 2) {
                    ClearContents();
                    getQulityEventInfoByCar(contents[1], contents[0]);
                    showProgressBar();
                } else {
                    Toast.makeText(QulityEventActivity.this, "请按照正确的格式输入数据", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        twoCarsHD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    requestDispacher("1", "两车互倒");
                }
                return false;
            }
        });

        CompanyMoney.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    requestDispacher("2", "利用公司资产");
                }
                return false;
            }
        });

        OtherMoney.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    requestDispacher("3", "利用段方资产");
                }
                return false;
            }
        });

        NoHPCL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    requestDispacher("4", "未更换配件");
                }
                return false;
            }
        });


        //初始化控件
        init();

        companyNameSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                chooseCompany = companyAdapter.getItem(arg2);
                ClearContents();
                getQulityEventInfo(chooseCompany);
                showProgressBar();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                chooseCompany = null;
            }
        });

        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (result1.equals("") || result1 == null) {
                    Toast.makeText(QulityEventActivity.this, "系统故障,请联系信息中心!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(QulityEventActivity.this, result1, Toast.LENGTH_SHORT).show();
                    getCompanyName();//获取下拉框的数据，填充到company[]。

                }
            }
        };
        handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (result2.equals("null") || result2.equals("")) {
                    Toast.makeText(QulityEventActivity.this, "没有查询到数据！", Toast.LENGTH_SHORT).show();
                } else {
                    contens = result2.split("=");
                    if (contens.length % 6 != 0) {//获取的数据存在问题
                        items = 0;
                        contens = null;
                        Toast.makeText(QulityEventActivity.this, "获取数据失败！Error:getContents has an error", Toast.LENGTH_LONG).show();
                    } else {
                        items = (int) contens.length / 6;
                    }
                    //Toast.makeText(QulityEventActivity.this, "" + result2, Toast.LENGTH_SHORT).show();
                    showContentsList();//显示数据
                    sortQulityInfoItem();//为子项动态添加监听器。
                    dissProgressBar();
                }
            }
        };
        qulityInfoByCarHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (qulityInfoByCar.equals("null") || qulityInfoByCar.equals("")) {
                    Toast.makeText(QulityEventActivity.this, "没有查询到数据！", Toast.LENGTH_SHORT).show();
                } else {
                    contens = qulityInfoByCar.split("=");
                    if (contens.length % 6 != 0) {//获取的数据存在问题
                        items = 0;
                        contens = null;
                        Toast.makeText(QulityEventActivity.this, "获取数据失败！Error:getContents has an error", Toast.LENGTH_LONG).show();
                    } else {
                        items = (int) contens.length / 6;
                    }
                    // Toast.makeText(QulityEventActivity.this, "" + qulityInfoByCar, Toast.LENGTH_SHORT).show();
                    showContentsList();//显示数据
                    sortQulityInfoItem();//为子项动态添加监听器。
                    dissProgressBar();
                }
            }
        };
    }

    private void init() {
        qulityInfoListLinearLayout = (LinearLayout) findViewById(R.id.qulityInfoListLinearLayout);
        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        companyNameSpinner = (Spinner) findViewById(R.id.companyNameSpinner);
        companyList = new ArrayList<String>();

        companyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, companyList);
        companyNameSpinner.setAdapter(companyAdapter);
    }

    public void getCompanyName() {
        //访问服务器，获取数据填充company[]
        //测试
        company = result1.split(",");
        companyCount = company.length;
        for (int i = 1; i < companyCount; i++) {
            companyList.add(company[i]);
        }
        companyAdapter.notifyDataSetChanged();
    }

    private void ClearContents() {
        qulityInfoListLinearLayout.removeAllViews();
        qualityInfoCounts = 0;
        contens = null;
        items = 0;
        whichOneChecked = -1;
    }


    private void showContentsList() {
        if (items > 0) {
            emptyTextView.setVisibility(View.GONE);
            for (int i = 0; i < items; i++) {
                //显示一个item
                View qulityItem = View.inflate(this, R.layout.qulityinfoitem, null);
                ((EditText) qulityItem.findViewById(R.id.ZZXXH_Edit)).setText(contens[itemIncludeRecords * i + 0]);
                ((EditText) qulityItem.findViewById(R.id.CXCH_Edit)).setText(contens[itemIncludeRecords * i + 1] + "/" + contens[itemIncludeRecords * i + 3]);
                ((EditText) qulityItem.findViewById(R.id.ZZXXLX_Edit)).setText(contens[itemIncludeRecords * i + 2]);
                //少一条记录，问题描述不显示
                ((EditText) qulityItem.findViewById(R.id.CLZT_Edit)).setText(contens[itemIncludeRecords * i + 4]);//处理状态
                //添加生成一个item
                ((EditText) qulityItem.findViewById(R.id.WTMS_EditText)).setText(contens[itemIncludeRecords * i + 5]);
                qulityInfoListLinearLayout.addView(qulityItem);
                qualityInfoCounts++;
            }
        } else {
            emptyTextView.setVisibility(View.VISIBLE);//显示没有数据
        }
    }

    private void sortQulityInfoItem() {//添加弹出框点击事件
        Log.v("QulityEventActivity", "begin:sortQulityInfoItem");
        if (qulityInfoListLinearLayout != null) {
            for (int i = 0; i < qulityInfoListLinearLayout.getChildCount(); i++) {
                final View view = qulityInfoListLinearLayout.getChildAt(i);
                final int position = i;

                final EditText WTMS_EditText = (EditText) view.findViewById(R.id.WTMS_EditText);
                final String QuestionDescriptionDetail = contens[itemIncludeRecords * position + 5];
                if (WTMS_EditText != null) {
                    /*WTMS_EditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            detailDialog(QuestionDescriptionDetail);
                        }
                    });*/
                    WTMS_EditText.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                detailDialog(QuestionDescriptionDetail);
                            }
                            return false;
                        }
                    });
                }
                //质量信息号
                final EditText ZLXXH_Edit = (EditText) view.findViewById(R.id.ZZXXH_Edit);
                final String ZLXXH_EditDetail = contens[itemIncludeRecords * position + 0];
                if (ZLXXH_Edit != null) {
                    /*ZLXXH_Edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            detailDialog(ZLXXH_EditDetail);
                        }
                    });*/
                    ZLXXH_Edit.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                detailDialog(ZLXXH_EditDetail);
                            }
                            return false;
                        }
                    });
                }
                final EditText CXCH_Edit = (EditText) view.findViewById(R.id.CXCH_Edit);
                final String CXCH_EditDetail = contens[itemIncludeRecords * position + 1] + "/" + contens[itemIncludeRecords * position + 3];
                if (CXCH_Edit != null) {
                    /*CXCH_Edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            detailDialog(CXCH_EditDetail);
                        }
                    });*/
                    CXCH_Edit.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                detailDialog(CXCH_EditDetail);
                            }
                            return false;
                        }
                    });
                }
                //质量信息类型
                final EditText ZLXXLX_Edit = (EditText) view.findViewById(R.id.ZZXXLX_Edit);
                final String ZLXXLX_EditDetail = contens[itemIncludeRecords * position + 2];
                if (ZLXXLX_Edit != null) {
                    /*ZLXXLX_Edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            detailDialog(ZLXXLX_EditDetail);
                        }
                    });*/
                    ZLXXLX_Edit.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                detailDialog(ZLXXLX_EditDetail);
                            }
                            return false;
                        }
                    });
                }
                //单选框的选择
                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.GX_CHECK);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            //清除其它被选中的checkbox
                            //whechOneChecked
                            if (whichOneChecked >= 0 && whichOneChecked <= items) {
                                //之前有被选中
                                ((CheckBox) (qulityInfoListLinearLayout.getChildAt(whichOneChecked)).findViewById(R.id.GX_CHECK)).setChecked(false);
                                whichOneChecked = position;
                            } else {
                                //之前没有被选中
                                whichOneChecked = position;
                            }
                        } else {
                            whichOneChecked = -1;
                        }
                    }
                });
            }
        } else {
            Log.v("QulityEventActivity", "null");
        }
        Log.v("QulityEventActivity", "end:sortQulityInfoItem");
    }

    /*
    //问题描述细节
    private void QuestionDescriptionDialog(final int position) {
        Log.v("QulityEventActivity", "start:QuestionDescriptionDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.tip);
        builder.setTitle("问题描述");
        builder.setMessage(""+contens[itemIncludeRecords*position+5]);
        builder.setPositiveButton("返回",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.v("QulityEventActivity", "end:QuestionDescriptionDialog");
    }
   //ZLXXH_EditDialog质量信息号弹出框
    private void ZLXXH_EditDialog(final int position) {
        Log.v("QulityEventActivity", "start:ZLXXH_EditDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.tip);
        builder.setTitle("质量信息号");
        builder.setMessage(""+contens[itemIncludeRecords*position+0]);
        builder.setPositiveButton("返回",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.v("QulityEventActivity", "end:ZLXXH_EditDialog");
    }*/
    // 细节弹出框
    private void detailDialog(final String detail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.tip);
        builder.setTitle("信息展示");
        builder.setMessage(detail);
        builder.setPositiveButton("返回",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getSelectInfo() {
        DataObtainer.INSTANCE.getSelectInfomation(
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

    public void getQulityEventInfo(String s1) {
        DataObtainer.INSTANCE.getQulityInfo(s1,
                new NetworkCallbacks.SimpleDataCallback() {
                    @Override
                    public void onFinish(boolean b, String s, Object o) {
                        result2 = (String) o;
                        Message m = handler2.obtainMessage();
                        handler2.sendMessage(m);
                    }
                }

        );
    }

    //通过车型 车号来获取质量信息
    public void getQulityEventInfoByCar(String carId, String carType) {
        DataObtainer.INSTANCE.getQulityInfoByCars(carId, carType,
                new NetworkCallbacks.SimpleDataCallback() {
                    @Override
                    public void onFinish(boolean b, String s, Object o) {
                        qulityInfoByCar = (String) o;
                        Message m = qulityInfoByCarHandler.obtainMessage();
                        qulityInfoByCarHandler.sendMessage(m);
                    }
                }

        );
    }

    public void showProgressBar() {
        loadingProressBar = new Loading_view(this, R.style.CustomDialog);
        loadingProressBar.show();
    }

    public void dissProgressBar() {
        loadingProressBar.dismiss();
    }

    public void requestDispacher(String type, String dealType) {
        //有选中的值
        if (0 <= whichOneChecked && whichOneChecked < qualityInfoCounts) {

            String ZLXXH = contens[whichOneChecked * itemIncludeRecords + 0];
            String CX = contens[whichOneChecked * itemIncludeRecords + 1];
            String ZLXXLX = contens[whichOneChecked * itemIncludeRecords + 2];
            String CH = contens[whichOneChecked * itemIncludeRecords + 3];
            String WTMS = contens[whichOneChecked * itemIncludeRecords + 4];
            String FYR = contens[whichOneChecked * itemIncludeRecords + 5];
            String dataDeail = type + "=" + ZLXXH + "=" + CX + "=" + ZLXXLX + "=" + CH + "=" + WTMS + "=" + FYR + "=" + dealType;
            Intent intent = new Intent(QulityEventActivity.this, DealDataActivity.class);
            intent.putExtra("data", dataDeail);
            startActivity(intent);
        } else {
            //没有选中的值
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.tip);
            builder.setTitle("信息展示");
            builder.setMessage("请您先勾选未完成的质量信息！");
            builder.setPositiveButton("好的",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /*//解决handle内存泄漏的问题
    static class MyHandler extends Handler {
        WeakReference<QulityEventActivity> mWeakReference;

        public MyHandler(QulityEventActivity activity) {
            mWeakReference = new WeakReference<QulityEventActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final QulityEventActivity activity = mWeakReference.get();
            if (activity != null) {
                if (msg.what == 1) {

                }
            }
        }
    }*/

}

