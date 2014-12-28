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
			System.out.println("邮件正文(缩略图)：" + msg.getPreReceivedViewContent());
			System.out.println("*** 邮件正文 ***：\n" + msg.getContent());
			//
			System.out.println("是否包含附件：" + msg.getHasAttachMent());
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