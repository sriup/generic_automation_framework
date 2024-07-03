package framework.utilities;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/**
 * This class will hold all methods related to outlook
 */
public class OutlookEmailUtil {
	public OutlookEmailUtil() {
	}
	
	private  String username;
	private String password;
	
	private Session session;
	
	public void connect(){
		
		SecurityUtil secUtil = new SecurityUtil();
		
		String outlookId = System.getenv("OUTLOOK_ID");
		outlookId = (outlookId != null && !outlookId.isEmpty()) ? outlookId : System.getProperty("OUTLOOK_ID");
		
		String outlookPassword = System.getenv("OUTLOOK_PWD");
		outlookPassword = (outlookPassword != null && !outlookPassword.isEmpty()) ? outlookPassword : System.getProperty("OUTLOOK_PWD");
		
		username = (secUtil.decrypt(outlookId) != null) ? secUtil.decrypt(outlookId) : outlookId;
		password = (secUtil.decrypt(outlookPassword) != null)? secUtil.decrypt(outlookPassword) : outlookPassword;
		
		Properties props = new Properties();
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "587");
		
		session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
	}
	
	/**
	 * Sends an email to the recipients
	 *
	 * @param recipients	to recipient(s)
	 * @param subject	the subject of the email
	 * @param emailContent	the content of the email in the email body
	 * @param fileAttachmentPath 	the file attachment path
	 * @param fileName 	the filename what it has to be attached in the email
	 * @throws Exception	the exception
	 */
	public void sendEmail(Address[] recipients, String subject, String emailContent,
						  String fileAttachmentPath, String fileName) throws Exception{
				
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username));
		message.setRecipients(Message.RecipientType.TO, recipients);
		
		message.setSubject(subject);
		message.setText(emailContent);
		
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setText((emailContent != null) ? emailContent : "");
		
		Multipart multipart = new MimeMultipart();
		
		multipart.addBodyPart(mimeBodyPart);
		
		if(fileAttachmentPath != null && !fileAttachmentPath.isEmpty()){
			
			mimeBodyPart = new MimeBodyPart();
			
			String file = fileAttachmentPath + File.separatorChar + fileName;
			
			DataSource source = new FileDataSource(file);
			mimeBodyPart.setDataHandler(new DataHandler(source));
			mimeBodyPart.setFileName(fileName);
			
			multipart.addBodyPart(mimeBodyPart);
			
		}
		
		message.setContent(multipart, "text/html");
		
		Transport.send(message);
		
		System.out.println("Email has been sent!");
		
	}
	
	/**
	 * Sends an email to the recipients
	 *
	 * @param recipients	to recipient(s)
	 * @param subject	the subject of the email
	 * @param emailContent	the content of the email in the email body
	 * @throws Exception	the exception
	 */
	public void sendEmail(Address[] recipients, String subject, String emailContent)
			throws Exception{
		
		sendEmail(recipients, subject, emailContent,
				null, null);
		
	}
	
}
