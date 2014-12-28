package EmailProcessor.Recieves;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import Util.Email.CheckEmail;
import Util.Email.EmailDataManager;
import Util.Email.Logging.EmailLogger;
import Util.Email.PathManager;

import DataInfo.EmailMessage;
import DataInfo.EnumType;

public class ReceivedPoP3ProcessorTest {
	//
	public static void main(String[] args) throws Exception  {
		EmailDataManager.addData(EmailDataManager.getEmailAddr(), 
				EmailDataManager.getDefaultEmailAddr());
		EmailDataManager.addData(EmailDataManager.getEmailPswd(), 
				EmailDataManager.getDefaultEmailPswd());
		String userName = (String) EmailDataManager.
				getData(EmailDataManager.getEmailAddr());
		String hostName = EnumType.MAIL_POP3_HOSTTYPE + "." + 
				userName.substring(userName.indexOf("@") + 1);
		
		EmailDataManager.addData(
				EmailDataManager.getPop3EmailHost(), hostName);
		
		boolean refresh = true;
		boolean isGetAttachment = true;
//		EmailMessage[] messageList = ReceivedPoP3Processor.
//				getReceivedPoP3ProcessorInstance().
//				getEmailMessages(refresh, isGetAttachment);
		
		int pageNum = 0;
		int perMsgsLen = 10;
		
		boolean isNotepad = false;
		EmailMessage[] messageList = ReceivedPoP3Processor.
				getReceivedPoP3ProcessorInstance().
				receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
		
		boolean flag = false;
		String endString = "******************************************";
		
		
		// 解析所有邮件
		final int maxLen = 3;
		int len = messageList.length >= maxLen ? 
				maxLen : messageList.length;
		System.out.println("show lenght: " + len);
		
		for (int i = 0; i < len; i++) {
			if(flag == false) {
				flag = true;
				System.out.println(endString);
			}
			
			EmailMessage msg = messageList[i];
			
			System.out.println("解析第" + (i + 1) + "封邮件");
			System.out.println("idx: " + msg.getReceivedIdx());
			System.out.println("主题: " + msg.getSubject());
			System.out.println("发件人: " + msg.getFromStr());
			System.out.println("收件人：" + msg.getToStr());
			if(msg.getCcs() != null && msg.getCcs().length > 0) {
				int idx = 1;
				for(String cc: msg.getCcs()) {
					System.out.println(idx + "th cc：" + cc);
					idx++;
				}
			}
			if(msg.getBccs() != null && msg.getBccs().length > 0) {
				int idx = 1;
				for(String bcc: msg.getCcs()) {
					System.out.println(idx + "th bcc：" + bcc);
					idx++;
				}
			}
			System.out.println("发送时间：" + msg.getSendDate());
			System.out.println("接收时间：" + msg.getReceivedDate());
			System.out.println("是否已读：" + msg.getReceivedHasSeen());
			System.out.println("邮件优先级：" + msg.getPriority());
			System.out.println("是否需要回执：" + msg.getReceivedReplySign());
			System.out.println("邮件大小：" + msg.getReceivedFileSize() * 1024 + "kb");
			System.out.println("邮件正文类型: " + msg.getContentType());
			// System.out.println("邮件正文(缩略图)：" + msg.getPreReceivedViewContent());
			System.out.println("*** 邮件正文 ***：\n" + msg.getContent());
			//
			System.out.println("是否包含附件：" + msg.getHasAttachMent());
			// download files
			if (msg.getHasAttachMent()) {
				ReceivedPoP3Processor.saveAttachments(msg.getReceivedIdx(), 
				 		PathManager.getDownloadResourcePath(), null);
			}
			System.out.println();
			System.out.println();
			// log
			System.out.println(endString);
		}
		
		// *********************************************************************
		
		/** error
		 * 一定要在连接中解析邮件, 也就是在解析邮件过程中,
		 * store.connect()要一直维持着
		 */
		
		/*EmailLogger.info("");
		EmailLogger.info("");
		EmailLogger.info(" **************************************************** ");
		EmailLogger.info(" another methods ");
		Message[] messages = ReceivedPoP3Processor.
				getReceivedPoP3ProcessorInstance().
				geReceivedtMessage();
		for (int i = 0; i < len; i++) {
			if(flag == false) {
				flag = true;
				System.out.println(endString);
			}
			
			EmailMessage msg =  ReceivedEmailProcessor.
					parseOneMessage(messages[i], isGetAttachment);
			System.out.println("解析第" + (i + 1) + "封邮件");
			System.out.println("idx: " + msg.getReceivedIdx());
			System.out.println("主题: " + msg.getSubject());
			System.out.println("发件人: " + msg.getFromStr());
			System.out.println("收件人：" + msg.getToStr());
			if(msg.getCcs() != null && msg.getCcs().length > 0) {
				int idx = 1;
				for(String cc: msg.getCcs()) {
					System.out.println(idx + "th cc：" + cc);
					idx++;
				}
			}
			if(msg.getBccs() != null && msg.getBccs().length > 0) {
				int idx = 1;
				for(String bcc: msg.getCcs()) {
					System.out.println(idx + "th bcc：" + bcc);
					idx++;
				}
			}
			System.out.println("发送时间：" + msg.getSendDate());
			System.out.println("接收时间：" + msg.getReceivedDate());
			System.out.println("是否已读：" + msg.getReceivedHasSeen());
			System.out.println("邮件优先级：" + msg.getPriority());
			System.out.println("是否需要回执：" + msg.getReceivedReplySign());
			System.out.println("邮件大小：" + msg.getReceivedFileSize() * 1024 + "kb");
			System.out.println("邮件正文类型: " + msg.getContentType());
			// System.out.println("邮件正文(缩略图)：" + msg.getPreReceivedViewContent());
			System.out.println("*** 邮件正文 ***：\n" + msg.getContent());
			//
			System.out.println("是否包含附件：" + msg.getHasAttachMent());
			// download files
			if (msg.getHasAttachMent()) {
				ReceivedPoP3Processor.saveAttachments(msg.getReceivedIdx(), 
				 		PathManager.getDBOthersPath());
			}
			System.out.println();
			System.out.println();
			// log
			System.out.println(endString);
		}*/
	}
}