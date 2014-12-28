package DataInfo;

import java.util.Date;
import java.util.Properties;
import DataInfo.EmailMessage;

public class EmailMessageTest {
	public static void main(String[] args) throws Exception {
		String emailType = EnumType.SendEmailType;
		String subject = "first meeting";
		String from = "sysu512@126.com"; 
		String to = "dongdk.sysu@foxmail.com";
		String content = "hello ddk";
		Date sendDate = new Date();
		String[] fileNames = null;
		String priority = "3";
		
		EmailMessage em = new EmailMessage(emailType, subject, 
				from, to, content, sendDate, fileNames, priority);
		
		em.printInfo();
		
		System.out.println();
		
		EmailMessage.getSingleEmailMessage(emailType, subject, 
				from, to, content, sendDate, fileNames, priority).printInfo();
	}
}
