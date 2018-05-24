package com.test.framework.utils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailUtil {

	private static Session session;
	private static Properties props = new Properties();
	private static final String HOST = "smtp.163.com";
	// SSL 465 非SSL 25
	private static int PORT = 25;
	// 发件的邮箱
	private static final String USERNAME = "yangyangtbs@163.com";
	// 邮箱授权码
	private static final String PASSWORD = "yxb56405475";

	private static final String TIMEOUT = "15000";
	private static final String DEBUG = "true";

	// 初始化session
	static {
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", true);
		props.put("username", USERNAME);
		props.put("password", PASSWORD);
		props.put("mail.smtp.timeout", TIMEOUT);
		props.put("mail.debug", DEBUG);
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.checkserveridentity", "false");
		props.put("mail.smtp.ssl.trust", HOST);

		session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});
	}

	// 发送纯文字邮件
	public static void sendTextEmail(String from, String to, String subject,
			String content) throws Exception {
		Boolean isMass = false;
		InternetAddress[] address = null;
		if (null != to && to.contains(",")) {
			isMass = true;
		}
		String[] toArr = to.split(",");
		address = new InternetAddress[toArr.length];
		for (int i = 0; i < toArr.length; i++) {
			address[i] = new InternetAddress(toArr[i]);
		}
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		if (isMass) {
			message.addRecipients(RecipientType.TO, address);
		} else {
			message.addRecipient(RecipientType.TO, new InternetAddress(to));
		}
		message.setSubject(subject);
		message.setText(content);
		message.setSentDate(new Date());
		Transport.send(message);

	}

	// 发送有HTML格式邮件
	public static void sendHtmlEmail(String from, String to, String subject,
			String htmlConent) throws Exception {
		Boolean isMass = false;
		InternetAddress[] address = null;
		if (null != to && to.contains(",")) {
			isMass = true;
		}
		String[] toArr = to.split(",");
		address = new InternetAddress[toArr.length];
		for (int i = 0; i < toArr.length; i++) {
			address[i] = new InternetAddress(toArr[i]);
		}
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		if (isMass) {
			message.addRecipients(RecipientType.TO, address);
		} else {
			message.addRecipient(RecipientType.TO, new InternetAddress(to));
		}

		Multipart multi = new MimeMultipart();
		BodyPart html = new MimeBodyPart();
		html.setContent(htmlConent, "text/html; charset=utf-8");
		multi.addBodyPart(html);
		message.setContent(multi);
		Transport.send(message);
	}

	// 发送带附件的邮件
	public static void sendFilesEmail(String from, String to, String subject,
			String htmlConent, List<File> attachments) throws Exception {
		Boolean isMass = false;
		InternetAddress[] address = null;
		if (to.contains(",") && null != to) {
			isMass = true;
		}
		String[] toArr = to.split(",");
		address = new InternetAddress[toArr.length];
		for (int i = 0; i < toArr.length; i++) {
			address[i] = new InternetAddress(toArr[i]);
		}
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		if (isMass) {
			message.addRecipients(RecipientType.TO, address);
		} else {
			message.addRecipient(RecipientType.TO, new InternetAddress(to));
		}
		message.setSubject(subject);
		message.setSentDate(new Date());
		// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
		Multipart multipart = new MimeMultipart();
		// 添加邮件正文
		BodyPart contentPart = new MimeBodyPart();
		contentPart.setContent(htmlConent, "text/html;charset=UTF-8");
		multipart.addBodyPart(contentPart);
		// 添加附件的内容
		if (null != attachments && attachments.size() > 0) {
			for (int i = 0; i < attachments.size(); i++) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				FileDataSource source = new FileDataSource(attachments.get(i));
				attachmentBodyPart.setDataHandler(new DataHandler(source));
				// MimeUtility.encodeWord可以避免文件名乱码
				attachmentBodyPart.setFileName(MimeUtility
						.encodeWord(attachments.get(i).getName()));
				multipart.addBodyPart(attachmentBodyPart);
			}

		}

		message.setContent(multipart);
		Transport.send(message);
	}

}
