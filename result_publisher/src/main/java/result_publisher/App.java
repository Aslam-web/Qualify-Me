package result_publisher;

import java.util.Set;

import result_publisher.sender.MailSender;
import result_publisher.sender.SMSSender;

public class App {

	public static void main(String[] args) throws Exception {

		// 1. create excel file for all the Strudents
		ExcelCreator excelCreator = new ExcelCreator();
		Set<Student> studentEntries = excelCreator.createExcelForStudents("data/data2.xls");
		
		// 2. Send a message along with the password for excel
//		SMSSender smsSender = new SMSSender();
//		smsSender.sendMessage(studentEntries);
		
		// 3. send the files to all the students
//		MailSender mailSender = new MailSender();
//		mailSender.sendMail(studentEntries);

		// printing read data
//		ExcelCreator.print(studentEntries);
		
		// For checking balance
//		new SMSSender().checkBalance();
	}
}