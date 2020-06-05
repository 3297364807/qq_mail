package com.example.test2;
import android.app.ProgressDialog;
import android.os.Handler;
import android.widget.Toast;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http_tools {
    private static final String TAG = "Http_tools";
    public String get(String cookie, String id) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"type\":\"org\",\"identity\":\"\",\"para\":{\"organization_id\":\"" + id + "\",\"organization_path_str\":\"1649,1650,1651,1652,1653,1654,1655\"},\"date\":\"2020-06-05\",\"activityid\":\"3195\",\"flag\":0,\"domain\":\"cqcfe\",\"stucode\":\"032018030186\"}");
        Request request = new Request.Builder()
                .url("https://lightapp.weishao.com.cn/api/reportstatistics/reportstatistics/getStatistical")
                .method("POST", body)
                .addHeader("Content-Length", "214")
                .addHeader("Content-Type", "application/json")
                .addHeader("Referer", "https://lightapp.weishao.com.cn/reportstatistics/reportMember")
                .addHeader("Cookie", cookie)
                .addHeader("Host", "lightapp.weishao.com.cn")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String[] mysql() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://39.106.133.87:3306/meigui", "root", "admin");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from name_qq");
        String[] data = new String[1000];
        int i = 0;
        while (resultSet.next()) {
            if (i == 0) {
                data[i] = resultSet.getString("name") + "," + resultSet.getString("qq") + "," + resultSet.getString("cookie");
            } else {
                data[i] = resultSet.getString("name") + "," + resultSet.getString("qq");
            }
            i++;
        }
        connection.close();
        return data;
    }

    public void QQMail_Send(InternetAddress[] addresses, MainActivity mainActivity, Handler handler, ProgressDialog progressDialog) throws AddressException, MessagingException {
        int count = 0;
        for (int i = 0; i < addresses.length; i++) {//计算非空值数量
            if (addresses[i] != null) {
                count++;
            }
        }
        InternetAddress[] add = new InternetAddress[count];
        for (int i = 0; i < add.length; i++) {//重新赋值
            add[i] = addresses[i];
        }
        if(add.length>0){
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
            message.setFrom(new InternetAddress("3297364807@qq.com"));
//        Add[0] = new InternetAddress("1414394854@qq.com");
//        message.setRecipient(Message.RecipientType.TO, new InternetAddress(""+QQ+"@qq.com"));//单个收件人
            message.setRecipients(Message.RecipientType.TO, add);  //dd多个收件人
// 设置邮件标题
            message.setSubject("唐智：微哨打卡");
// 设置邮件内容
            message.setText("亲爱的同学你的微哨未打卡，请尽快打卡哟！来自3297364807的提醒");
// 得到邮差对象
            Transport transport = session.getTransport();
// 连接自己的邮箱账户
            transport.connect("3297364807@qq.com", "cvxidvggrrehchdd");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
// 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            handler.post(()-> {
                progressDialog.dismiss();
                Toast.makeText(mainActivity, "发送完毕", Toast.LENGTH_SHORT).show();
            });
        }else {
            handler.post(()->{
                progressDialog.dismiss();
                Toast.makeText(mainActivity ,"全班已打卡完成", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
