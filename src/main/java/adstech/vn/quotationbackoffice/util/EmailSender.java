package adstech.vn.quotationbackoffice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailSSLSocketFactory;

public class EmailSender {
	private static final String DEFAULT_TEMPLATE_FOLDER = "email-template/";

	private static EmailSender instance = null;
	private static Properties properties = null;
	
	private Authenticator authenticator = null;

	private String strPassword = "adsplus@Aa12";
	private String strUserName = "baond@adsplus.vn";
	private String strMailSupport = "baond@adsplus.vn";
	private String strHost = "smtp.sendgrid.net";
	private String strPort = "587";
	private String strMailFrom = "no_reply@adsplus.vn";
	private String strMailFromName = "Báo Giá 5 Phút";

	public static EmailSender getInstance() {
		if(instance == null){
			instance = new EmailSender();
		}
		return instance;
	}

	private EmailSender() {
		initProperties();
	}

	private void initProperties() {
		properties = new Properties();
		properties.setProperty("mail.smtp.host", strHost);
		properties.setProperty("mail.smtp.port", strPort);
		properties.setProperty("mail.username", strUserName);
		properties.setProperty("mail.password", strPassword);
		properties.setProperty("mail.support", strMailSupport);
		properties.setProperty("mail.fromaddress", strMailFrom);
		authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				String username = strUserName;
				String password = strPassword;
				return new PasswordAuthentication(username, password);
			}
		};
	}
	
	public void sendEmailRequestApproval(String recipients, String approver, String createdBy, String partnerName, String partnerWebsite, String dueDate, String url) throws MessagingException {
		Map<String, String> context = new HashMap<String, String>();
		context.put("approver", approver);
		context.put("createdBy", createdBy);
		context.put("partnerName", partnerName);
		context.put("dueDate", dueDate);
		context.put("partnerWebsite", partnerWebsite);
		context.put("url", url);
		String subject = "mail.subject.approval.request";
		
		sendTemplateMail(recipients, subject, "approval_request_email.vm", context);
	}
	
	public void sendEmailNotifyApproval(String recipients, String approver, String partnerName) throws MessagingException {
		Map<String, String> context = new HashMap<String, String>();
		context.put("email", recipients);
		context.put("approver", approver);
		context.put("partnerName", partnerName);
		String subject = "mail.subject.approval";
		
		sendTemplateMail(recipients, subject, "approved_notification_email.vm", context);
	}
	
	public void sendEmailNotifyRejection(String recipients, String approver, String partnerName, String url) throws MessagingException {
		Map<String, String> context = new HashMap<String, String>();
		context.put("email", recipients);
		context.put("approver", approver);
		context.put("partnerName", partnerName);
		context.put("url", url);
		String subject = "mail.subject.rejection";
		
		sendTemplateMail(recipients, subject, "rejected_notification_email.vm", context);
	}
	
	private void sendTemplateMail(String recipients, String subjectKey, String templateFile, Map<String, String> parameters) throws MessagingException {
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		final String SOCKET_FACTORY = "javax.net.SocketFactory";
		String strPort = properties.getProperty("mail.smtp.port").trim();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.debug", "true");
		if (SMTP_SSL_PORT.equals(strPort)) {
			properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			properties.setProperty("mail.smtp.socketFactory.port", SMTP_SSL_PORT);
			properties.setProperty("javax.net.ssl.trustStore", "template/cacerts_confirm.crt");
			properties.setProperty("mail.smtp.ssl.enable", "true");
			MailSSLSocketFactory sf;
			try {
				sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
				properties.put("mail.smtp.ssl.socketFactory", sf);
			} catch (GeneralSecurityException e) {

			}
		} else {
			properties.setProperty("mail.smtp.socketFactory.class", SOCKET_FACTORY);
			properties.setProperty("mail.smtp.starttls.enable", "true");
			properties.setProperty("mail.smtp.ssl.trust", properties.getProperty("mail.smtp.host").trim());
		}

		Session session = Session.getInstance(properties, authenticator);
		session.setDebug(Boolean.parseBoolean(properties.getProperty("mail.debug", "false")));

		String from = properties.getProperty("mail.fromaddress");
		final MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from, strMailFromName));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		InternetAddress[] addressTo = new InternetAddress[1];

		addressTo[0] = new InternetAddress(recipients);

		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setRecipients(Message.RecipientType.BCC, properties.getProperty("mail.support"));

		msg.setSubject(getSubject(subjectKey), "UTF-8");
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(getContent(templateFile, parameters), "text/html; charset=utf-8");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		msg.setContent(multipart);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Transport.send(msg);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}

		});
		thread.start();
	}

	private String getSubject(String key) {
		Properties properties = new Properties();
		try {
			InputStreamReader streamReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("email-template/mail_subject.properties"), "UTF8");
			properties.load(streamReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(key, "");
	}

	private String getContent(String templateFile, Map<String, String> parameters) {
		// Reading the file contents from the JAR
		StringBuilder templateBuilder = new StringBuilder();
		String content;
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(getTemplateFolder() + templateFile);
			if (inputStream == null) {
				inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_TEMPLATE_FOLDER + templateFile);
			}
			InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				templateBuilder.append(line + "\n");
			}
		} catch (Exception e) {

		}
		content = templateBuilder.toString();
		Iterator<String> it = parameters.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (parameters.get(key) != null) {
				content = content.replaceAll("\\$" + key, parameters.get(key));
			} else {
				content = content.replaceAll("\\$" + key, "");
			}
		}

		return content;
	}

	private String getTemplateFolder() {
		return DEFAULT_TEMPLATE_FOLDER;
	}

	// Test
	private String smtpServer;
	private String port;
	private String user;
	private String password;
	private String auth;
	private String from;
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_SSL_PORT = "465";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	public EmailSender(String smtpServer, String port, String user, String password, String auth, String from) {
		super();
		this.smtpServer = smtpServer;
		this.port = port;
		this.user = user;
		this.password = password;
		this.auth = auth;
		this.from = from;
	}

	public void testSendSSLMessage(String recipients[], String subject, String message, String from) throws MessagingException {
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_SSL_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_SSL_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("test@ezsolution.vn", "123456");
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);

	}

	public void testSendEmail(String subject, String HtmlMessage, String[] to) {
		Transport transport = null;
		try {
			Properties props = prepareProperties();
			Session mailSession = Session.getDefaultInstance(props, new SMTPAuthenticator(from, password, true));
			transport = mailSession.getTransport("smtp");
			MimeMessage message = prepareMessage(mailSession, "ISO-8859-2", from, subject, HtmlMessage, to);
			transport.connect();
			Transport.send(message);
		} catch (Exception ex) {

		} finally {
			try {
				transport.close();
			} catch (MessagingException ex) {
				Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private Properties prepareProperties() {
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtpServer);
		props.setProperty("mail.smtp.port", port);
		props.setProperty("mail.smtp.user", user);
		props.setProperty("mail.smtp.password", password);
		props.setProperty("mail.smtp.auth", auth);
		return props;
	}

	private MimeMessage prepareMessage(Session mailSession, String charset, String from, String subject, String HtmlMessage, String[] recipient) {
		// Multipurpose Internet Mail Extensions
		MimeMessage message = null;
		try {
			message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);
			for (int i = 0; i < recipient.length; i++)
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient[i]));
			message.setContent(HtmlMessage, "text/html; charset=\"" + charset + "\"");
		} catch (Exception ex) {
			Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
		}
		return message;
	}

	public class SMTPAuthenticator extends Authenticator {
		private String username;
		private String password;
		private boolean needAuth;

		public SMTPAuthenticator(String username, String password, boolean needAuth) {
			this.username = username;
			this.password = password;
			this.needAuth = needAuth;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			if (needAuth)
				return new PasswordAuthentication(username, password);
			else
				return null;
		}
	}
}
