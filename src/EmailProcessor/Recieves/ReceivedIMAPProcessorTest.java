package EmailProcessor.Recieves;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import Util.Email.EmailDataManager;
import Util.Email.PathManager;

public class ReceivedIMAPProcessorTest {
	//
	public static void main(String[] args) throws Exception  {
		EmailDataManager.addData(EmailDataManager.getEmailAddr(), 
				EmailDataManager.getDefaultEmailAddr());
		EmailDataManager.addData(EmailDataManager.getEmailPswd(), 
				EmailDataManager.getDefaultEmailPswd());
		String userName = (String) EmailDataManager.getData(EmailDataManager.getEmailAddr());
		String hostName = EnumType.MAIL_IMAP_HOSTTYPE + "." + 
				userName.substring(userName.indexOf("@") + 1);
		
		EmailDataManager.addData(
				EmailDataManager.getImapEmailHost(), hostName);
		
		boolean refresh = true;
		boolean isGetAttachment = true;
		String folderName = null;
		EmailMessage[] messageList = ReceivedIMAPProcessor.
				getReceivedIMAPProcessorInstance().
				getEmailMessages(folderName, refresh, isGetAttachment);
		
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
			System.out.println("�ʼ�����(����ͼ)��" + msg.getPreReceivedViewContent());
			System.out.println("*** �ʼ����� ***��\n" + msg.getContent());
			//
			System.out.println("�Ƿ����������" + msg.getHasAttachMent());
			// download files
			if (msg.getHasAttachMent()) {
				String strInfo = EnumType.SuccessInfoStr;
				ReceivedPoP3Processor.saveAttachments(msg.getReceivedIdx(), 
				 		PathManager.getDownloadResourcePath(), null);
			}
			System.out.println();
			System.out.println();
			// log
			System.out.println(endString);
		}
	}
}