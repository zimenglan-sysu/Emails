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
		
		
		// ���������ʼ�
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
			
			System.out.println("������" + (i + 1) + "���ʼ�");
			System.out.println("idx: " + msg.getReceivedIdx());
			System.out.println("����: " + msg.getSubject());
			System.out.println("������: " + msg.getFromStr());
			System.out.println("�ռ��ˣ�" + msg.getToStr());
			if(msg.getCcs() != null && msg.getCcs().length > 0) {
				int idx = 1;
				for(String cc: msg.getCcs()) {
					System.out.println(idx + "th cc��" + cc);
					idx++;
				}
			}
			if(msg.getBccs() != null && msg.getBccs().length > 0) {
				int idx = 1;
				for(String bcc: msg.getCcs()) {
					System.out.println(idx + "th bcc��" + bcc);
					idx++;
				}
			}
			System.out.println("����ʱ�䣺" + msg.getSendDate());
			System.out.println("����ʱ�䣺" + msg.getReceivedDate());
			System.out.println("�Ƿ��Ѷ���" + msg.getReceivedHasSeen());
			System.out.println("�ʼ����ȼ���" + msg.getPriority());
			System.out.println("�Ƿ���Ҫ��ִ��" + msg.getReceivedReplySign());
			System.out.println("�ʼ���С��" + msg.getReceivedFileSize() * 1024 + "kb");
			System.out.println("�ʼ���������: " + msg.getContentType());
			// System.out.println("�ʼ�����(����ͼ)��" + msg.getPreReceivedViewContent());
			System.out.println("*** �ʼ����� ***��\n" + msg.getContent());
			//
			System.out.println("�Ƿ����������" + msg.getHasAttachMent());
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
		 * һ��Ҫ�������н����ʼ�, Ҳ�����ڽ����ʼ�������,
		 * store.connect()Ҫһֱά����
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
			System.out.println("������" + (i + 1) + "���ʼ�");
			System.out.println("idx: " + msg.getReceivedIdx());
			System.out.println("����: " + msg.getSubject());
			System.out.println("������: " + msg.getFromStr());
			System.out.println("�ռ��ˣ�" + msg.getToStr());
			if(msg.getCcs() != null && msg.getCcs().length > 0) {
				int idx = 1;
				for(String cc: msg.getCcs()) {
					System.out.println(idx + "th cc��" + cc);
					idx++;
				}
			}
			if(msg.getBccs() != null && msg.getBccs().length > 0) {
				int idx = 1;
				for(String bcc: msg.getCcs()) {
					System.out.println(idx + "th bcc��" + bcc);
					idx++;
				}
			}
			System.out.println("����ʱ�䣺" + msg.getSendDate());
			System.out.println("����ʱ�䣺" + msg.getReceivedDate());
			System.out.println("�Ƿ��Ѷ���" + msg.getReceivedHasSeen());
			System.out.println("�ʼ����ȼ���" + msg.getPriority());
			System.out.println("�Ƿ���Ҫ��ִ��" + msg.getReceivedReplySign());
			System.out.println("�ʼ���С��" + msg.getReceivedFileSize() * 1024 + "kb");
			System.out.println("�ʼ���������: " + msg.getContentType());
			// System.out.println("�ʼ�����(����ͼ)��" + msg.getPreReceivedViewContent());
			System.out.println("*** �ʼ����� ***��\n" + msg.getContent());
			//
			System.out.println("�Ƿ����������" + msg.getHasAttachMent());
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