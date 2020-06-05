package com.example.test2;

import android.os.Bundle;
import android.os.SystemClock;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(() -> {
            Http_tools tools = new Http_tools();
            try {
                String[] data = tools.mysql();

                String cookie = "[{\"key\":\"Cookie\",\"value\":\"whistle-spaserver=s%3A_b2ZastbhP5EIfyamKc0iQZyc4uWLp-Q.y%2FiP1kj00cYmXLvX1zKx2FP2zab4kKgy9PdylVFPnzs;\",\"description\":\"\",\"type\":\"text\",\"enabled\":true}]";
                JSONObject json = new JSONObject(tools.get(cookie, "1649"));
                for(int i=0;i<json.getJSONObject("data").getJSONArray("users").length();i++){
                    String name = json.getJSONObject("data").getJSONArray("users").getJSONObject(i).getString("user_name");
                    int report = json.getJSONObject("data").getJSONArray("users").getJSONObject(i).getInt("is_report");
                    if (report == 1) {
                        System.out.println(name + "：已打卡");
                    } else {
                        System.out.println(name + "：未打卡");
                        for(int j=0;j<data.length;j++){
                            String[] qq_name=data[j].split(",");//qq_name[0]是姓名  [1]是qq号
                            if(name.equals(qq_name[0])){//找到姓名


                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//                QQMail_Send();
        }).start();
    }


}