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
    private String number = null;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String[] name={"3297364807@qq.com","2392630994@qq.com","3297364807@qq.com","3216869767@qq.com","3216869767@qq.com","3297364807@qq.com"};
//        new Thread(()->{
//            try {
//                for(int i=0;i<name.length;i++){
//                    fason(new InternetAddress(name[i]));
//                    SystemClock.sleep(3000);
//                }
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        InternetAddress[] add = new InternetAddress[2];
//        add[0] = new InternetAddress();
//        add[1] = new InternetAddress();

        initShow();
        initCheck();
    }

    private void fason(InternetAddress is) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
// 得到回话对象
        Session session = Session.getInstance(properties);
// 获取邮件对象
        Message message = new MimeMessage(session);
// 设置发件人邮箱地址
        message.setFrom(new InternetAddress("2392630994@qq.com"));
//       InternetAddress[] Add = new InternetAddress("1414394854@qq.com");
//        message.setRecipient(Message.RecipientType.TO, new InternetAddress("2392630994@qq.com"));//单个收件人

        message.setRecipient(Message.RecipientType.TO, is);  //dd多个收件人
// 设置邮件标题
        message.setSubject("唐智：微哨打卡提醒");
// 设置邮件内容
        message.setText("亲爱的同学你的微哨未打卡，请尽快打卡哟 来自邮箱：3297364807提醒");
// 得到邮差对象
        Transport transport = session.getTransport();
// 连接自己的邮箱账户
        transport.connect("2392630994@qq.com", "apoxpxyrmiiueafi");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
// 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }


    private void init_Request(String Class_name) {
        new Thread(() -> {
            Http_tools tools = new Http_tools();
            try {
                String[] data = tools.mysql();

                String[] cookie = data[0].split(",");
                JSONObject json = new JSONObject(tools.get(cookie[2], Class_name));
                int len = json.getJSONObject("data").getJSONArray("users").length();
                String[] address = new String[len];
                int count = 0;
                for (int i = 0; i < len; i++) {
                    String name = json.getJSONObject("data").getJSONArray("users").getJSONObject(i).getString("user_name");
                    int report = json.getJSONObject("data").getJSONArray("users").getJSONObject(i).getInt("is_report");
                    if (report == 1) {
                        Log.e(TAG, name + "：已打卡");
                    } else {
                        Log.e(TAG, name + "：未打卡");
                        for (int j = 0; j < data.length; j++) {//找到对应的名字
                            if (data[j] != null) {
                                String[] qq_name = data[j].split(",");//qq_name[0]is name  [1]is QQ NO
                                if (name.equals(qq_name[0])) {//找到姓名.
                                    if (address.length > count) {
                                        address[count] = qq_name[1] + "@qq.com";
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                }
                Log.e(TAG, String.valueOf(count));
                for (int i = 0; i < address.length; i++) {
                    if(address[i]!=null){
                        Log.e(TAG, address[i]);
                    }

                }
//                if (count > 0) {
//                    for (int i = 0; i < address.length; i++) {
//                        if (address[i] != null) {
//                            fason(new InternetAddress(address[i]));
//                            SystemClock.sleep(3000);
//                        }
//                    }
//                    handler.post(() -> {
//                        progressDialog.dismiss();
//                        Toast.makeText(this, "发送完毕", Toast.LENGTH_SHORT).show();
//                    });
//                } else {
//                    handler.post(() -> {
//                        progressDialog.dismiss();
//                        Toast.makeText(this, "全班已打卡完成", Toast.LENGTH_SHORT).show();
//                    });
//                }


                if (count > 0) {
                    if (count < 5) {
                        for (int i = 0; i < count; i++) {
                            tools.QQMail_Send2(new InternetAddress(address[i]));
                        }
                        handler.post(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(this, "发送完毕", Toast.LENGTH_SHORT).show();
                        });
                    } else {

                        int count_ = 1;
                        for (int i = 0; i < count; i++) {

                            if (count_ % 5 == 0) {
                                InternetAddress[] name = new InternetAddress[5];
                                for (int j = i, s = 0; j > i - 5; j--, s++) {//每隔五位执行一次
                                    name[s] = new InternetAddress(address[j]);
                                }
                                Log.e(TAG, "开始执行");
                                tools.QQMail_Send(name);//这里是每5位次执行一次
                                if (count - 1 - i < 5) {//计算最后还剩几位
                                    for (int s = i; s < i + count - 1 - i; s++) {
                                        tools.QQMail_Send2(new InternetAddress(address[s]));
                                    }
                                }
                            }
                            count_++;
                        }
                        handler.post(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(this, "发送完毕", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    handler.post(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "全班已打卡完成", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();} catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
//            } catch (AddressException e) {
//                e.printStackTrace();
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }


        }).start();
    }

    private void initCheck() {
        tv1.setOnClickListener(v -> {
            number = "1649";
            dialog.show();
        });
        tv2.setOnClickListener(v -> {
            number = "1650";
            dialog.show();
        });
        tv3.setOnClickListener(v -> {
            number = "1651";
            dialog.show();
        });
        tv4.setOnClickListener(v -> {
            number = "1652";
            dialog.show();
        });
        tv5.setOnClickListener(v -> {
            number = "1653";
            dialog.show();
        });
        tv6.setOnClickListener(v -> {
            number = "1654";
            dialog.show();
        });
        tv7.setOnClickListener(v -> {
            number = "1655";
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("微哨打卡");
        progressDialog.setMessage("加载中....");
        progressDialog.setCancelable(false);
        dialog = new AlertDialog.Builder(this).setTitle("确定提醒吗").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (number != null) {
                            progressDialog.show();
                            init_Request(number);
                        } else {
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