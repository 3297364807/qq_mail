package com.example.test2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    private AlertDialog dialog;
    private String number=null;
    private ProgressDialog progressDialog;
    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initShow();
        initCheck();
    }
    private void init_Request(String Class_name) {
        new Thread(() -> {
            Http_tools tools = new Http_tools();
            try {
                String[] data = tools.mysql();

                String[] cookie = data[0].split(",");
                JSONObject json = new JSONObject(tools.get(cookie[2], Class_name));
                int len = json.getJSONObject("data").getJSONArray("users").length();
                InternetAddress[] address = new InternetAddress[len];
                int count = 0;
                for (int i = 0; i < len; i++) {
                    String name = json.getJSONObject("data").getJSONArray("users").getJSONObject(i).getString("user_name");
                    int report = json.getJSONObject("data").getJSONArray("users").getJSONObject(i).getInt("is_report");
                    if (report == 1) {
                        System.out.println(name + "：已打卡");
                    } else {
                        Log.e(TAG, name + "：未打卡");
                        for (int j = 0; j < data.length; j++) {//找到对应的名字
                            if (data[j] != null) {
                                String[] qq_name = data[j].split(",");//qq_name[0]is name  [1]is QQ NO
                                System.out.println(data[j] + ">>>>>");
                                if (name.equals(qq_name[0])) {//找到姓名
                                    address[count] = new InternetAddress(qq_name[1] + "@qq.com");
                                    count++;
                                }
                            }
                        }
                    }
                }
                //start request

                tools.QQMail_Send(address,this,handler,progressDialog);

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
//                QQMail_Send();
        }).start();
    }

    private void initCheck() {
        tv1.setOnClickListener(v -> {
            number="1649";
            dialog.show();
        });
        tv2.setOnClickListener(v -> {
            number="1650";
            dialog.show();
        });
        tv3.setOnClickListener(v -> {
            number="1651";
            dialog.show();
        });
        tv4.setOnClickListener(v -> {
            number="1652";
            dialog.show();
        });
        tv5.setOnClickListener(v -> {
            number="1653";
            dialog.show();
        });
        tv6.setOnClickListener(v -> {
            number="1654";
            dialog.show();
        });
        tv7.setOnClickListener(v -> {
            number="1655";
            dialog.show();
        });
    }

    private void initShow() {
        tv1 = findViewById(R.id.btn1);
        tv2 = findViewById(R.id.btn2);
        tv3 = findViewById(R.id.btn3);
        tv4 = findViewById(R.id.btn4);
        tv5 = findViewById(R.id.btn5);
        tv6 = findViewById(R.id.btn6);
        tv7 = findViewById(R.id.btn7);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("微哨打卡");
        progressDialog.setMessage("加载中....");
        progressDialog.setCancelable(false);
        dialog = new AlertDialog.Builder(this).setTitle("确定提醒吗").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(number!=null){
                            progressDialog.show();
                            init_Request(number);
                        }else {
                            Toast.makeText(MainActivity.this, "班级为空的", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
    }


}