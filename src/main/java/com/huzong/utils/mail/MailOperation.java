package com.huzong.utils.mail;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;/**
 * 邮件发送操作类
 *
 * @author zhangdi
 *
 */
public class MailOperation {

    private final String user = "****@qq.com";
    private final String password = "****";
    private final String host = "smtp.qq.com";
    private final String from = "***@qq.com";
//    private final String to = "962073795@qq.com";// 收件人
    //邮箱主题
    private final String subject = "验证码.";

    /**
     * 发送邮件
     *  user 发件人邮箱
     *  password 授权码（注意不是邮箱登录密码）
     *  host
     *  from 发件人
     * @param to 接收者邮箱
     * @param content 邮件内容
     * @return success 发送成功 failure 发送失败
     */
    public String sendMail(String to, String content) {
        if (to != null){
            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", true);
            MailAuthenticator auth = new MailAuthenticator();
            MailAuthenticator.USERNAME = user;
            MailAuthenticator.PASSWORD = password;
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                if (!to.trim().equals(""))
                    message.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(to.trim()));
                message.setSubject(subject);
                MimeBodyPart mbp1 = new MimeBodyPart(); // 正文
                mbp1.setContent(content, "text/html;charset=utf-8");
                Multipart mp = new MimeMultipart(); // 整个邮件：正文+附件
                mp.addBodyPart(mbp1);
                message.setContent(mp);
                message.setSentDate(new Date());
                message.saveChanges();
                Transport trans = session.getTransport("smtp");
                trans.send(message);
                System.out.println(message.toString());
            } catch (Exception e){
                e.printStackTrace();
                return "failure";
            }
            return "success";
        }else{
            return "failure";
        }
    }

}
