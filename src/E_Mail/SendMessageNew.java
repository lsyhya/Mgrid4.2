package E_Mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMessageNew {

	public  String mailProtocol="smtp";
	public  String myEmailSMTPHost = "smtp.qq.com";
	public  String myEmailAccount = "453938089@qq.com";
	public  String myEmailPassword = "sgipglsayogvcaih";	
	public  String receiveMailAccount = "leisiyang521@163.com";
	
	public  String Subject="标题";
	public  String fromName="发件人名称";
	public  String toName="收件人名称";
	public  String content="正文";
	
	
	public String getMailProtocol() {
		return mailProtocol;
	}

	public void setMailProtocol(String mailProtocol) {
		this.mailProtocol = mailProtocol;
	}

	public String getMyEmailSMTPHost() {
		return myEmailSMTPHost;
	}

	public void setMyEmailSMTPHost(String myEmailSMTPHost) {
		this.myEmailSMTPHost = myEmailSMTPHost;
	}

	public String getMyEmailAccount() {
		return myEmailAccount;
	}

	public void setMyEmailAccount(String myEmailAccount) {
		this.myEmailAccount = myEmailAccount;
	}

	public String getMyEmailPassword() {
		return myEmailPassword;
	}

	public void setMyEmailPassword(String myEmailPassword) {
		this.myEmailPassword = myEmailPassword;
	}

	public String getReceiveMailAccount() {
		return receiveMailAccount;
	}

	public void setReceiveMailAccount(String receiveMailAccount) {
		this.receiveMailAccount = receiveMailAccount;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	
	
	
	public  void  sendMessage()
	{
		System.out.println("开始了");
		Properties props = new Properties();                    // 参数配置
		
		
		
        props.setProperty("mail.transport.protocol",mailProtocol);   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        props.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                     
        
        // 3. 创建一封邮件
        MimeMessage message;
		try {
			message = createMimeMessage(session, myEmailAccount, receiveMailAccount);
			   // 4. 根据 Session 获取邮件传输对象
	        Transport transport = session.getTransport();

	        transport.connect(myEmailAccount, myEmailPassword);

	        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
	        transport.sendMessage(message, message.getAllRecipients());

	        // 7. 关闭连接
	        transport.close();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    				
	}
	
	public  MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
	        // 1. 创建一封邮件
	        MimeMessage message = new MimeMessage(session);

	        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
	        message.setFrom(new InternetAddress(sendMail,fromName, "UTF-8"));

	        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
	        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, toName, "UTF-8"));

	        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
	        message.setSubject(Subject, "UTF-8");

	        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
	 //       message.setContent("XX用户你好, 今天全场5折, 快来抢购, 错过今天再等一年。。。", "text/html;charset=UTF-8");
	        message.setText(content);

	        // 6. 设置发件时间
	        message.setSentDate(new Date());

	        // 7. 保存设置
	        message.saveChanges();

	        return message;
	}
}
