package DataInfo;

import java.awt.Color;
import java.awt.Font;

/**
 * 邮件发送需要的常量定义
 * @author ddk.
 * @date 2014-12-06 14:54
 */
public class EnumType {
	/**
	 * Common
	 */
	// 设置Nimbus皮肤
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
	 * SMTP常见的邮件发送协议地址
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
	 * POP3常见的邮件发送协议地址
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
	 * IMAP常见的邮件发送协议地址
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
	 * 文件及文件夹选择
	 */
	public static final String FileEmptyString = 
			"                                              " + 
			"                                              ";
	public static final String DefaultJFileChooserDir = "D:\\";
	public static final String DefaultJFileChooserMsg = 
			" **** 这里默认显示选择的文件名 **** ";
	public static final String DefaultFileChoosedMsg = 
			FileEmptyString + "  *** 成功选择文件 ***  ";
	public static final String DefaultFileDirChoosedMsg = 
			"  *** 成功选择文件夹 ***  ";
	public static final String DefaultFileNotChoosedMsg = 
			FileEmptyString + "  *** 没有选择文件 ***  ";
	public static final String DefaultFileDirNotChoosedMsg = 
			"  *** 没有选择文件夹 *** ";
	
	/**
	 * Login UI
	 */
	// 常用字体
	public static final Font LOGIN_WARN_FONT = 
			new Font("黑体", Font.BOLD, 20);
	// 常用颜色
	public static final Color LOGIN_BG_COLOR = 
		new Color(21, 121, 221);
	public static final String MSG_WRITING_INIT = 
			"请输入邮箱和密码";
	public static final String MSG_PSWD_INIT = 
			"请输入密码";
	public static final String MSG_WRITING_USERNAME = 
			"正在输入用户名 / 邮箱";
	public static final String MSG_WRITING_PSWD = 
			"正在输入密码";
	public static final String WRONG_EMAIL_ADDR_MSG = 
			"您输入邮箱不能为空";
	public static final String INVALID_EMAIL_ADDR_MSG = 
			"您输入邮箱不合法";
	public static final String WRONG_EMAIL_PSWD_MSG = 
			"您输密码不能为空";
	public static final String LOGIN_VALIDED_FAIL_MSG = 
			"验证失败：邮箱地址或密码错误";
	public static final String LOGIN_MAIN_FAIL_MSG = 
			"验证失败：邮箱地址或密码错误\n";
	public static final String LOGINING = 
			"正在登录, 请稍候...";
	public static final String LOGIN_SUCCESS_MSG = 
			"Congratulation!!!";
	// 邮件正则验证
	public static final String REGEX_EMAIL_ADDRESS = 
			"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	
	/** 
	 * Main UI
	 */
	public static final String MAIL_ACCOUNT = "邮箱账户";
	public static final String MAIL_CONTACTOR = "联系人";
	
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
	public static final String SendEmailBtnTitle = "发邮件";
	public static final String ReceiveEmailBtnTitle = "收邮件";
	public static final String NotepadEmailBtnTitle = "写记事本";
	public static final String ContractorEmailBtnTitle = "联系人";
	public static final String QuitEmailBtnTitle = "退出";
	
	public static final String EmailBoxName = "邮箱";
	public static final String SendedEmailItem = "发邮件";
	public static final String SendedFolder = "发件箱";
	public static final String ReceivedFolder = "收件箱";
	public static final String DraftFolder = "草稿箱";
	public static final String RubbishFolder = "垃圾箱";
	public static final String NotepadFolder = "记事本";
	
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
			"*** 你正在回复(邮件)... ***";
	public static final String SendedReplyAllEmailTile = SendEmailEmptyString +
			"*** 你正在回复所有(邮件)... ***";
	public static final String SendedForwardEmailTile = SendEmailEmptyString +
			"*** 你正在转发邮件... ***";
	public static final String SendedEmailTile = SendEmailEmptyString +
			"*** 你正在发邮件... ***";
	public static final String DeletedLastFileMsg = SendEmailEmptyString +
			"** 已经删除最后一个附件 **";
	public static final String DeletedAllFilesMsg = SendEmailEmptyString +
			"** 已经删除最后附件 **";
	public static final String SendedEmailReceiver = "  收件人: ";
	public static final String SendedEmailCcs = "  抄送: ";
	public static final String SendedEmailBccs = "  密送: ";
	public static final String SendedEmailSubject = "  主题: ";
	public static final String SendedEmailBodyContent = "  主体内容: ";
	public static final String SendedEmailPriority = "  优先级";
	
	public static final String SendedEmailBtnName = " 发送邮件 ";
	public static final String TimedSendedEmailBtnName = " 定时发送邮件 ";
	public static final String AnonymitySendedEmailBtnName = " 匿名发送邮件 ";
	public static final String SendedEmailAttachBtnName = " 添加附件 ";
	public static final Color SendedEmailBGColor = Color.lightGray;
	public static final int JTextFieldLength = 128;
	
	public static final String SendedEmailReceiverErrorMsg = 
			SendEmailEmptyString +"*** 收件人邮箱地址不正确/为空 ***";
	public static final String SendedEmailCcsErrorMsg = 
			SendEmailEmptyString + "*** 抄送人邮箱地址不正确/为空 ***";
	public static final String SendedEmailBccsErrorMsg =
			 SendEmailEmptyString +"*** 密抄件人邮箱地址不正确/为空 ***";
	public static final String SendedEmailSubjectErrorMsg =
			 SendEmailEmptyString +"*** 主题为空 ***";
	public static final String SendedEmailContentErrorMsg =
			 SendEmailEmptyString +"*** 内容为空 ***";
	
	
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
	public static final String MoreReadEmailBtnTitle = "查看该邮件";
	public static final String ReceivedEmailTile = SendEmailEmptyString +
			"*** 你正在查看邮件... ***";
	public static final String DownloadLastFileMsg = SendEmailEmptyString +
			"** 下载附件 **";
	public static final String DownloadAllFilesMsg =
			"  *** 成功下载全部附件 ***  ";
	public static final String CancledDownloadAllFilesMsg = 
			"  ***已经取消下载所有附件 ***  ";
	public static final String ReceivedEmailSender = "      发件人: ";
	public static final String ReceivedEmailReceiver = "      收件人: ";
	public static final String ReceivedEmailCcs = "      抄送: ";
	public static final String ReceivedEmailBccs = "      密送: ";
	public static final String ReceivedEmailSubject = "      主题: ";
	public static final String ReceivedEmailBodyContent = "      主体内容: ";
	public static final String ReceivedEmailPriority = "      优先级";
	
	public static final String ReplyReceivedEmailBtnName = " 回复 ";
	public static final String ReplyAllReceivedEmailBtnName = " 回复全部 ";
	public static final String ForwardReceivedEmailBtnName = " 转发 ";
	public static final String DownloadReceivedAttachmentBtnName = " 下载附件 ";
	public static final String DownloadReceivedAllAttachmentsBtnName = " 下载全部附件 ";
	public static final Color ReceivedEmailBGColor = Color.lightGray;
	public static final int RecievedJTextFieldLength = 128;
	public static final String ReceivedDefaultAttachments = "附件: ";
	public static final String ReceivedDefaultJFileChooserMsg = 
			"         **** 这里默认显示保存路径 ****";
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
	public static final String  SendedNotepadRecordedDate = "    时间: ";
	public static final String  SendedNotepadSubject = "    主题: ";
	public static final String  SendedNotepadBodyName = "    内容: ";
	//public static final String  SendedNotepadBtnName = "保存";
	//public static final String  SendedNotepadBtnName = "保存";
	public static final String  SendedNotepadBtnName = "保存";
	
	/**
	 * Contactors
	 */
	public static final String RecentlyContactor = "最近联系";
	public static final String UsuallyContactor = "经常联系";
	public static final String StartContactor = "星标";
}