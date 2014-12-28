package DataInfo;

import java.awt.Color;
import java.awt.Font;

/**
 * �ʼ�������Ҫ�ĳ�������
 * @author ddk.
 * @date 2014-12-06 14:54
 */
public class EnumType {
	/**
	 * Common
	 */
	// ����NimbusƤ��
	public static final String NimbusLookAndFeel = 
			"javax.swing.plaf.nimbus.NimbusLookAndFeel";
	public static enum EmailType {
		SEND,
		RECEIVE
	}
	
	public static final String DefaultResPath = 
			"E:\\githubs\\Emails\\res\\";
	public static final long DayOfMillSeconds = 
			24 * 60 * 60 * 1000;
	public static final String SendEmailType = "SEND";
	public static final String ReceiveEmailType = "RECEIVE";
	public static final String EmailEegex = ";";
	public static final String EmailEegexWithLine = ";\n";
	public static final String HtmlRemovedString = "<!DOCTYPE html>";
	
	/**
	 * Email ContentType
	 */
	public static final String HTMLTYPE = "text/html";
	public static final String PLAINTYPE = "text/plain";
	public static final String IMAGETYPE = "text/image";
	
	/**
	 * Title for UI
	 */
	public static final String ZIMENGLAN_LOGIN_TITLE = 
			"zimenglan - email login";
	public static final String ZIMENGLAN_CLIENT_TITLE = 
			"zimenglan - email client";

	/** 
	 * The Type for Contator
	 */
	public static final String RecentContType = "RecentContator";
	public static final String CommonContType = "CommonContator";
	public static final String StarContType = "StarContator";
	
	/**
	 * SMTP�������ʼ�����Э���ַ
	 */
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_HOSTTYPE = "smtp";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
			
	public static final String SMTP_QQ = "smtp.qq.com";
	public static final String SMTP_163 = "smtp.163.com";
	public static final String SMTP_126 = "smtp.126.com";
	public static final String SMTP_SINA = "smtp.sina.com";
	public static final String SMTP_GMAIL = "smtp.gmail.com";

	/**
	 * POP3�������ʼ�����Э���ַ
	 */
	public static final String MAIL_POP3_HOST = "mail.pop3.host";
	public static final String MAIL_POP3_HOSTTYPE = "pop3";
	public static final String MAIL_POP3_AUTH = "mail.pop3.auth";
	
	public static final String POP3_QQ = "pop3.qq.com";
	public static final String POP3_163 = "pop3.163.com";
	public static final String POP3_126 = "pop3.126.com";
	public static final String POP3_SINA = "pop3.sina.com";
	public static final String POP3_GMAIL = "pop3.gmail.com";
	
	/**
	 * IMAP�������ʼ�����Э���ַ
	 */
	public static final String MAIL_IMAP_HOST = "mail.imap.host";
	public static final String MAIL_IMAP_HOSTTYPE = "imap";
	public static final String MAIL_IMAP_AUTH = "mail.imap.auth";
	
	public static final String IMAP_QQ = "imap.qq.com";
	public static final String IMAP_163 = "imap.163.com";
	public static final String IMAP_126 = "imap.126.com";
	public static final String IMAP_SINA = "imap.sina.com";
	public static final String IMAP_GMAIL = "imap.gmail.com";

	/**
	 * �ļ����ļ���ѡ��
	 */
	public static final String FileEmptyString = 
			"                                              " + 
			"                                              ";
	public static final String DefaultJFileChooserDir = "D:\\";
	public static final String DefaultJFileChooserMsg = 
			" **** ����Ĭ����ʾѡ����ļ��� **** ";
	public static final String DefaultFileChoosedMsg = 
			FileEmptyString + "  *** �ɹ�ѡ���ļ� ***  ";
	public static final String DefaultFileDirChoosedMsg = 
			"  *** �ɹ�ѡ���ļ��� ***  ";
	public static final String DefaultFileNotChoosedMsg = 
			FileEmptyString + "  *** û��ѡ���ļ� ***  ";
	public static final String DefaultFileDirNotChoosedMsg = 
			"  *** û��ѡ���ļ��� *** ";
	
	/**
	 * Login UI
	 */
	// ��������
	public static final Font LOGIN_WARN_FONT = 
			new Font("����", Font.BOLD, 20);
	// ������ɫ
	public static final Color LOGIN_BG_COLOR = 
		new Color(21, 121, 221);
	public static final String MSG_WRITING_INIT = 
			"���������������";
	public static final String MSG_PSWD_INIT = 
			"����������";
	public static final String MSG_WRITING_USERNAME = 
			"���������û��� / ����";
	public static final String MSG_WRITING_PSWD = 
			"������������";
	public static final String WRONG_EMAIL_ADDR_MSG = 
			"���������䲻��Ϊ��";
	public static final String INVALID_EMAIL_ADDR_MSG = 
			"���������䲻�Ϸ�";
	public static final String WRONG_EMAIL_PSWD_MSG = 
			"�������벻��Ϊ��";
	public static final String LOGIN_VALIDED_FAIL_MSG = 
			"��֤ʧ�ܣ������ַ���������";
	public static final String LOGIN_MAIN_FAIL_MSG = 
			"��֤ʧ�ܣ������ַ���������\n";
	public static final String LOGINING = 
			"���ڵ�¼, ���Ժ�...";
	public static final String LOGIN_SUCCESS_MSG = 
			"Congratulation!!!";
	// �ʼ�������֤
	public static final String REGEX_EMAIL_ADDRESS = 
			"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	
	/** 
	 * Main UI
	 */
	public static final String MAIL_ACCOUNT = "�����˻�";
	public static final String MAIL_CONTACTOR = "��ϵ��";
	
	public static final int MAIN_UI_WIDTH = 1080;
	public static final int MAIN_UI_HEIGHT = 760;
	
	public static final int CALENDER_WIDTH = 280;
	public static final int CALENDER_HEIGHT = 280;
	
	public static final int ACC_JTREE_WIDTH = 280;
	public static final int ACC_JTREE_HEIGHT = 760;
	
	public static final int ACC_JSCROLLPANE_WIDTH = 680;
	public static final int ACC_JSCROLLPANE_HEIGHT = 760;
	
	public static final int CON_JTREE_WIDTH = 680;
	public static final int CON_JTREE_HEIGHT = 760;
	//
	public static final boolean MAIN_UI_IS_RESIZE = false;
	public static final boolean Sended_UI_IS_RESIZE = true;
	public static final boolean Received_UI_IS_RESIZE = true;
	
	public static final Color MAIN_UI_BG_COLOR = 
			Color.white;
	public static final String SendEmailBtnTitle = "���ʼ�";
	public static final String ReceiveEmailBtnTitle = "���ʼ�";
	public static final String NotepadEmailBtnTitle = "д���±�";
	public static final String ContractorEmailBtnTitle = "��ϵ��";
	public static final String QuitEmailBtnTitle = "�˳�";
	
	public static final String EmailBoxName = "����";
	public static final String SendedEmailItem = "���ʼ�";
	public static final String SendedFolder = "������";
	public static final String ReceivedFolder = "�ռ���";
	public static final String DraftFolder = "�ݸ���";
	public static final String RubbishFolder = "������";
	public static final String NotepadFolder = "���±�";
	
	public static final String SendEmailIcon = "compose.jpg";
	public static final String ReceiveEmailIcon = "receive.jpg";
	public static final String QuitEmailIcon = "quit.jpg";
	public static final String SeeEmailIcon = "see.jpg";
	public static final String NotepadEmailIcon = "notepad.jpg";
	public static final String ContractorEmailIcon = "contractor.jpg";
	
	public static final String DefaultEmailCardLayout = 
			"defaultCardLayout";
	public static final String SendedEmailCardLayout = 
			"sendedCardLayout";
	public static final String PreReceivedEmailCardLayout = 
			"preReceivedCardLayout";
	public static final String PreRNotepadEmailCardLayout = 
			"preRNotepadCardLayout";
	public static final String ReceivedEmailCardLayout = 
			"receivedCardLayout";
	
	/** Send UI
	 * 
	 */
	public static final String DefaultEmailSendedContentString = 
			"hello ddk";
	public static final String SendEmailEmptyString = 
			"                                              " + 
			"                                              ";
	public static final String SendedReplyEmailTile = SendEmailEmptyString +
			"*** �����ڻظ�(�ʼ�)... ***";
	public static final String SendedReplyAllEmailTile = SendEmailEmptyString +
			"*** �����ڻظ�����(�ʼ�)... ***";
	public static final String SendedForwardEmailTile = SendEmailEmptyString +
			"*** ������ת���ʼ�... ***";
	public static final String SendedEmailTile = SendEmailEmptyString +
			"*** �����ڷ��ʼ�... ***";
	public static final String DeletedLastFileMsg = SendEmailEmptyString +
			"** �Ѿ�ɾ�����һ������ **";
	public static final String DeletedAllFilesMsg = SendEmailEmptyString +
			"** �Ѿ�ɾ����󸽼� **";
	public static final String SendedEmailReceiver = "  �ռ���: ";
	public static final String SendedEmailCcs = "  ����: ";
	public static final String SendedEmailBccs = "  ����: ";
	public static final String SendedEmailSubject = "  ����: ";
	public static final String SendedEmailBodyContent = "  ��������: ";
	public static final String SendedEmailPriority = "  ���ȼ�";
	
	public static final String SendedEmailBtnName = " �����ʼ� ";
	public static final String TimedSendedEmailBtnName = " ��ʱ�����ʼ� ";
	public static final String AnonymitySendedEmailBtnName = " ���������ʼ� ";
	public static final String SendedEmailAttachBtnName = " ��Ӹ��� ";
	public static final Color SendedEmailBGColor = Color.lightGray;
	public static final int JTextFieldLength = 128;
	
	public static final String SendedEmailReceiverErrorMsg = 
			SendEmailEmptyString +"*** �ռ��������ַ����ȷ/Ϊ�� ***";
	public static final String SendedEmailCcsErrorMsg = 
			SendEmailEmptyString + "*** �����������ַ����ȷ/Ϊ�� ***";
	public static final String SendedEmailBccsErrorMsg =
			 SendEmailEmptyString +"*** �ܳ����������ַ����ȷ/Ϊ�� ***";
	public static final String SendedEmailSubjectErrorMsg =
			 SendEmailEmptyString +"*** ����Ϊ�� ***";
	public static final String SendedEmailContentErrorMsg =
			 SendEmailEmptyString +"*** ����Ϊ�� ***";
	
	
	/** 
	 * Receive UI
	 */
	public static final boolean DefaultIsGetAttachment = true;
	public static final boolean DefaultIsGetNotepadAttachment = false;
	public static final boolean DefaultIsJTFEditable = false;
	public static final boolean DefaultRecievedIsJTFEditable = false;
	public static final long ReceivedEmailMaxDay = 365;
	public static final int DefaultPreViewLength = 100;
	public static final int DefaultPreViewPerMsgsLen = 5;
	
	public static final int DefaultReplyFlag = 0;
	public static final int DefaultReplyAllFlag = 1;
	public static final int DefaultForwardFlag = 2;
	
	public static final String ReceiveEmailEmptyString = 
			"                                      " + 
			"                                      ";
	public static final String MoreReadEmailBtnTitle = "�鿴���ʼ�";
	public static final String ReceivedEmailTile = SendEmailEmptyString +
			"*** �����ڲ鿴�ʼ�... ***";
	public static final String DownloadLastFileMsg = SendEmailEmptyString +
			"** ���ظ��� **";
	public static final String DownloadAllFilesMsg =
			"  *** �ɹ�����ȫ������ ***  ";
	public static final String CancledDownloadAllFilesMsg = 
			"  ***�Ѿ�ȡ���������и��� ***  ";
	public static final String ReceivedEmailSender = "      ������: ";
	public static final String ReceivedEmailReceiver = "      �ռ���: ";
	public static final String ReceivedEmailCcs = "      ����: ";
	public static final String ReceivedEmailBccs = "      ����: ";
	public static final String ReceivedEmailSubject = "      ����: ";
	public static final String ReceivedEmailBodyContent = "      ��������: ";
	public static final String ReceivedEmailPriority = "      ���ȼ�";
	
	public static final String ReplyReceivedEmailBtnName = " �ظ� ";
	public static final String ReplyAllReceivedEmailBtnName = " �ظ�ȫ�� ";
	public static final String ForwardReceivedEmailBtnName = " ת�� ";
	public static final String DownloadReceivedAttachmentBtnName = " ���ظ��� ";
	public static final String DownloadReceivedAllAttachmentsBtnName = " ����ȫ������ ";
	public static final Color ReceivedEmailBGColor = Color.lightGray;
	public static final int RecievedJTextFieldLength = 128;
	public static final String ReceivedDefaultAttachments = "����: ";
	public static final String ReceivedDefaultJFileChooserMsg = 
			"         **** ����Ĭ����ʾ����·�� ****";
	public static final String NoneInfoStr = "None";
	public static final String SuccessInfoStr = "Success";
	
	/**
	 * Notepad
	 */
	public static final int Send_Notepad_WIDTH = 680;
	public static final int Send_Notepad_HEIGHT = 540;
	
	public static final String  SendedNotepadTitle = 
			"zimenglan - Sending Notepad UI";
	public static final String  ReceivedNotepadTitle = 
			"zimenglan - Receiving Notepad UI";
	public static final String  SendedNotepadRecordedDate = "    ʱ��: ";
	public static final String  SendedNotepadSubject = "    ����: ";
	public static final String  SendedNotepadBodyName = "    ����: ";
	//public static final String  SendedNotepadBtnName = "����";
	//public static final String  SendedNotepadBtnName = "����";
	public static final String  SendedNotepadBtnName = "����";
	
	/**
	 * Contactors
	 */
	public static final String RecentlyContactor = "�����ϵ";
	public static final String UsuallyContactor = "������ϵ";
	public static final String StartContactor = "�Ǳ�";
}