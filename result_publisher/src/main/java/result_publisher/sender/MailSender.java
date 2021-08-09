package result_publisher.sender;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import result_publisher.Student;

public class MailSender {

	Properties props;
	Set<Student> studentDetails;
	Session session;

	public MailSender() {
		props = new Properties();
	}

	/*
	 * gets the set of students
	 * creates session
	 * initiates the sending process by calling startOperation() method
	 */
	public void sendMail(Set<Student> studentDetails) throws Exception {
		this.studentDetails = studentDetails;

		// loads the neccessary properties to connect to the smtp server from file
		props.load(new FileInputStream("config.properties"));

		session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("EMAIL"), props.getProperty("PASSWORD"));
			}
		});

		// starts operation
		startOperation();
	}

	// for each student the mail is sent, the looping is achieved with the help of iterator
	private void startOperation() {

		System.out.println("----------------------------------------\n" + "Preparing to send email...");

		Iterator<Student> iter = this.studentDetails.iterator();
		while (iter.hasNext()) {
			send(iter.next());
		}

		System.out.println("----------------------------------------");
	}

	// Creates a message [i.e via createMessage()] and sends message
	private void send(Student s) {

		try {
			Message message = createMessage(s);		// calls the createMessage() method
			Transport.send(message);
			System.out.printf("Message successfully sent to <%s>\n", s.getParentEmail());
		} catch (Exception e) {
			System.out.printf("Failed to send message to : <%s>\t\t\t", s.getParentEmail());
			System.out.println("Problem : " + e.getMessage());
		}
	}

	// Constructs(creates) the message
	private Message createMessage(Student s) {

		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(props.getProperty("EMAIL")));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(s.getParentEmail()));
			message.setSubject("Results Have been Published for the academic year 2020-2021");

			Multipart multipart = new MimeMultipart();

			// text part
			MimeBodyPart text1 = new MimeBodyPart();
			text1.setText(MessageBody.getEmailBody(s));		// gets the message body from MessageBody class

			// attachment
			MimeBodyPart file = new MimeBodyPart();
			file.attachFile(s.getExcelFileLocation());

			multipart.addBodyPart(text1);
			multipart.addBodyPart(file);
			message.setContent(multipart);

			return message;
		} catch (Exception e) {
			System.out.printf("SOME ERROR OCCURED IN CREATING EMAIL FOR %s!!!", s.getParentEmail());
			e.printStackTrace();
		}

		return message;
	}

}