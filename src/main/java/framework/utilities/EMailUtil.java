package framework.utilities;

import framework.constants.CommonVariables;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;


public class EMailUtil {

	// user session
	private final Session session;

	// user name
	private final String userName;

	/**
	 * creates user session based on the username and password
	 * @param userName	the gmail user name
	 * @param password  the gmail password
	 * 		<br><b>Note:</b>Make sure to send <i>decrypted</i> password.
	 */
	public EMailUtil(String userName, String password){

		this.userName = userName;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

			this.session = Session.getInstance(props, new javax.mail.Authenticator() {
			 @Override
			 protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
	}

	/**
	 * sends the email to the specified recipients
	 * @param emailDetails the email details map
	 * <br><b>Note:</b> <sup>*</sup> required key.<br>
	 *         <ul>
	 *             <li><sup>*</sup>{@link CommonVariables#EMAIL_RECIPIENTS} - Recipients (comma separated)</li>
	 *             <li><sup>*</sup>{@link CommonVariables#EMAIL_SUBJECT} - the subject line</li>
	 *             <li>{@link CommonVariables#EMAIL_BODY} - the body content (optional)</li>
	 *             <li>{@link CommonVariables#EMAIL_ATTACHMENT_FILE_PATH} - the attachment absolute file path
	 *                        (Send it as {@link java.util.List})</li>
	 *         </ul>
	 * 			<br>Below is the sample map for reference.<br>
	 *         <pre>
	 * Map <String, Object> emailDetails = new HashMap<>();<br>
	 * emailDetails.put(CommonVariables.EMAIL_RECIPIENTS, "user1@mail.com, user2@mail.com");<br>
	 * emailDetails.put(CommonVariables.EMAIL_SUBJECT, "Subject goes here");<br>
	 * emailDetails.put(CommonVariables.EMAIL_BODY, "Body text goes here");<br><br>
	 *
	 * List<String> attachments = new ArrayList<>();<br>
	 * attachments.add("C:/sample/attachment1.png");<br>
	 * attachments.add("C:/sample/subfolder/attachment2.json");<br><br>
	 *
	 * emailDetails.put(CommonVariables.EMAIL_ATTACHMENT_FILE_PATH, attachments);<br>
	 *         </pre>
	 */
	public void sendEmail(Map<String, Object> emailDetails) {

		try {
			String recipients = (String) emailDetails.get(CommonVariables.EMAIL_RECIPIENTS);
			String subject = (String) emailDetails.get(CommonVariables.EMAIL_SUBJECT);
			String emailBody = (String) emailDetails.get(CommonVariables.EMAIL_BODY);
			List<String> attachmentAbsPath = (List<String>) emailDetails.get(CommonVariables.EMAIL_ATTACHMENT_FILE_PATH);


			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.userName));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipients != null? recipients: this.userName));
			message.setSubject(subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(emailBody != null? emailBody: "");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if(attachmentAbsPath != null) {
				messageBodyPart = new MimeBodyPart();
				for(String attachmentPath : attachmentAbsPath){
					DataSource source = new FileDataSource(attachmentPath);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(source.getName());

					multipart.addBodyPart(messageBodyPart);
				}

			}

			message.setContent(multipart, "text/html");

			Transport.send(message);

		} catch (MessagingException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}