package com.example.test2;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class MainActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(()->{

//                QQMail_Send();

        }).start();
    }
    private static void QQMail_Send() throws AddressException,MessagingException{
        SystemClock.sleep(2000);
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
// 设置收件人邮箱地址
        InternetAddress[] Add=new InternetAddress[1];
        Add[0]=new InternetAddress("1414394854@qq.com");
        message.setRecipients(Message.RecipientType.TO, Add);//多个收件人
// 设置邮件标题
        message.setSubject("微哨打卡");
// 设置邮件内容
        message.setText("亲爱的同学你的微哨未打卡，请尽快打卡哟");
// 得到邮差对象
        Transport transport = session.getTransport();
// 连接自己的邮箱账户
        transport.connect("3297364807@qq.com", "cvxidvggrrehchdd");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
// 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}