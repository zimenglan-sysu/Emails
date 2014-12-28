package EmailProcessor.Recieves;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.AndTerm;
import javax.mail.search.BodyTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.NotTerm;
import javax.mail.search.RecipientStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.swing.JOptionPane;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import DataInfo.MailAuthenticator;
import UI.Notes.NotepadUI;
import Util.Email.Logging.EmailLogger;
import Util.Email.CheckEmail;
import Util.Email.EmailDataManager;
import EmailProcessor.Recieves.ReceivedEmailProcessor;

/**
 * @author ddk
 * @Created 14.12.21
 */
public class ReceivedPoP3Processor {
	/**
	 * variables
	 */
	// 邮件发送协议
	private final static String DefaultProtocol = "pop3";
	// 默认收邮件文件夹
	private final static String DefaultFolder = "INBOX";
	// SMTP邮件服务器
	private final static String DefaultHost = "qq.com";
	// SMTP邮件服务器默认端口
	private final static String Port = "110";
	// 是否要求身份认证
	private final static String Is_Auth = "true";	
	// 是否启用调试模式
	// 启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息
	private final static String Is_Enable_Debug_Mod = "false";
	// 默认收邮件人
	private static String defaultSelf = 
			EmailDataManager.getDefaultEmailAddr();
	// 初始化连接邮件服务器的会话信息
	private static Properties defaultProps = null;
	private static Properties props = null;
	//
	private static Authenticator defaultAuthenticator = null;
	private static Authenticator authenticator = null;
	// session
	private static Session session = null;
	// messages
	private static Message[] messages;
	private static EmailMessage[] messageList = null;
	
	// default variables
	static {
		defaultProps = new Properties();
		defaultProps.setProperty("mail.store.protocol", 
				DefaultProtocol);
		defaultProps.setProperty("mail.pop3.host", DefaultHost);
		// maybe wrong
		defaultProps.setProperty("mail.pop3.port", Port);
		defaultProps.setProperty("mail.pop3.auth", Is_Auth);
		defaultProps.setProperty("mail.debug",Is_Enable_Debug_Mod);
		
		defaultAuthenticator = new MailAuthenticator();
	}
	/**
	 * Constructor
	 */
	private static ReceivedPoP3Processor receiver = null;
	/**
	 * Constructor
	 */
	private ReceivedPoP3Processor() {
		// nothing
	}
	
	// **************************************************************
	
	/**
	 * Session
	 */
	public static Session getSession() {
		if(session == null) {
			setSession();
		}
		
		return session;
	}
	
	public static void setSession(Session s) {
		session = s;
	}
	
	public static void setSession() {
		if(session == null) {
			// authenticate
            // session = Session.getInstance(getProperty(), 
            //		getAuthenticator());
            
            session = Session.getInstance(getProperty(), null);
		}
	}
	/**
	 * Properties
	 */
	public static Properties getProperty() {
		if(props == null) {
			setProperty();
		}
		
		return props;
	}
	
	public static void setProperty(Properties p) {
		props = p;
	}
	
	public static void setProperty() {
		if(props == null) {
			props = new Properties();
			props.setProperty("mail.store.protocol", 
					DefaultProtocol);
			//
			props.setProperty("mail.pop3.host", 
					(String)EmailDataManager.getData(
					EmailDataManager.getPop3EmailHost()));
			
			props.setProperty("mail.pop3.port", Port);
			props.setProperty("mail.pop3.auth", Is_Auth);
			props.setProperty("mail.debug",Is_Enable_Debug_Mod);
		}
	}
	/**
	 * Authenticator
	 */
	public static Authenticator getAuthenticator() {
		if(authenticator == null) {
			setAuthenticator();
		}

		return authenticator;
	}
	
	public static void setAuthenticator(Authenticator a) {
		authenticator = a;
	}
	
	public static void setAuthenticator() {
		if(authenticator == null) {
			final String username = (String)EmailDataManager.getData(
					EmailDataManager.getEmailAddr());
			final String password = (String)EmailDataManager.getData(
					EmailDataManager.getEmailPswd());
			//
			authenticator = new Authenticator() {  
	            // 在session中设置账户信息，Transport发送邮件时会使用  
				@Override
	            protected PasswordAuthentication getPasswordAuthentication() {  
	                return new PasswordAuthentication(username, password);  
	          }
			};
		}
	}
	/**
	 * Nessages & EmailMessages
	 * @throws MessagingException 
	 */
	public static Message[] getMessages(boolean refresh) 
			throws MessagingException {
		if(messages == null || refresh == true) {
			messages = geReceivedtMessage();
		}
		
		return messages;
	}
	//
	public static EmailMessage[] getEmailMessages(boolean refresh, 
			boolean isGetAttachment) throws Exception {
		if(messageList == null || refresh == true) {
			messageList = receive(isGetAttachment);
		}
		
		return messageList;
	}
	/**
	 * Instance
	 */
	public static ReceivedPoP3Processor getReceivedPoP3ProcessorInstance() {
		if(receiver == null){
			receiver = new ReceivedPoP3Processor();
		}
		
		return receiver;
	}
	
	// **********************************************************
	
	/**
	 * @throws MessagingException 
	 */
	public static Store getStore() throws MessagingException {
		// 
		Store store = getSession().getStore(DefaultProtocol);
		
		final String username = (String)EmailDataManager.getData(
				EmailDataManager.getEmailAddr());
		final String password = (String)EmailDataManager.getData(
				EmailDataManager.getEmailPswd());
		// log
		// EmailLogger.info("username: " + username);
		// EmailLogger.info("password: " + password);
		
		// connect
		store.connect(username, password);
		// check
		if(store.isConnected() == false) {
			JOptionPane.showMessageDialog(null, "连接邮件服务器失败");
			return null;
		}
		
		return store;
	}
	/**
	 * @throws MessagingException 
	 */
	public static void closeStore(Store store) throws MessagingException {
		store.close();
	}
	/**
	 * @throws MessagingException 
	 */
	public static Folder getFolder(Store store) throws MessagingException {
		// 获得收件箱
		Folder folder = store.getFolder(DefaultFolder);
		/* 打开收件箱
		 * Folder.READ_ONLY：
		 * 		只读权限 Folder.READ_WRITE：
		 * 		可读可写（可以修改邮件的状态）
		 */
		folder.open(Folder.READ_WRITE); 
		
		return folder;
	}
	/**
	 * @throws MessagingException 
	 */
	public static void closeFoler(Folder folder, boolean flag) throws MessagingException {
		folder.close(flag);
	}
	
	// **********************************************************
	
	/**
	 * 仅仅获取原始的Message数组, 即folder.getMessages()
	 * @throws MessagingException
	 */
	public static Message[] geReceivedtMessage() throws MessagingException {
		//
		int totalEmailCount, deletedEmailCount;
		int newEmailCount, readEmailCount;
		//
		Store store = getStore();
		if(store == null) {
			
			return null;
		}
		Folder folder = getFolder(store);
		
		// 由于POP3协议无法获知邮件的状态,
		// 所以getUnreadMessageCount得到的是收件箱的邮件总数
		readEmailCount = folder.getUnreadMessageCount();
		EmailLogger.info("未读邮件数: " + readEmailCount);

		// 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
		deletedEmailCount = folder.getDeletedMessageCount();
		EmailLogger.info("删除邮件数: " + deletedEmailCount);
		newEmailCount = folder.getNewMessageCount();
		EmailLogger.info("新邮件: " + newEmailCount);
		// 获得收件箱中的邮件总数
		totalEmailCount = folder.getMessageCount();
		EmailLogger.info("邮件总数: " + totalEmailCount);

		// 得到收件箱中的所有邮件,并解析
		Message[] messages = folder.getMessages();
		
		// 释放资源
		closeFoler(folder, true);
		closeStore(store);
		
		return messages;
	}
	/** 
	 * POP3： 接收邮件, 效率很低, 出现在get content, attachments 上
	 * pasring的速度很慢
	 */
	public static EmailMessage[] receive(boolean isGetAttachment) throws Exception {
		//
		int totalEmailCount, deletedEmailCount;
		int newEmailCount, readEmailCount;
		//
		Store store = getStore();
		Folder folder = getFolder(store);
		/* 打开收件箱
		 * Folder.READ_ONLY：
		 * 		只读权限 Folder.READ_WRITE：
		 * 		可读可写（可以修改邮件的状态）
		 */
		// 由于POP3协议无法获知邮件的状态,
		// 所以getUnreadMessageCount得到的是收件箱的邮件总数
		readEmailCount = folder.getUnreadMessageCount();
		EmailLogger.info("未读邮件数: " + readEmailCount);

		// 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
		deletedEmailCount = folder.getDeletedMessageCount();
		EmailLogger.info("删除邮件数: " + deletedEmailCount);
		newEmailCount = folder.getNewMessageCount();
		EmailLogger.info("新邮件: " + newEmailCount);
		// 获得收件箱中的邮件总数
		totalEmailCount = folder.getMessageCount();
		EmailLogger.info("邮件总数: " + totalEmailCount);

		// 得到收件箱中的所有邮件,并解析
		Message[] messages = folder.getMessages();
		/*
		 * Parse Email Messages
		 */
		EmailMessage[] messageList =  ReceivedEmailProcessor.
				parseMessage(messages, isGetAttachment);
		//
		EmailMessage.setTotalReceivedEmailCount(totalEmailCount);
		EmailMessage.setNewReceivedEmailCount(newEmailCount);
		EmailMessage.setReadReceivedEmailCount(readEmailCount);
		EmailMessage.setDeletedReceivedEmailCount(deletedEmailCount);
		
		// 释放资源
		closeFoler(folder, true);
		closeStore(store);
		
		return messageList;
	}
	/**
	 * 
	 * @param isGetAttachment
	 * @param pageNum
	 * @param perMsgsLen
	 * @return
	 * @throws Exception
	 */
	public static EmailMessage[] receive(boolean isGetAttachment, 
			final int pageNum, final int perMsgsLen, boolean isNotepad) throws Exception {
		//
		int totalEmailCount, deletedEmailCount;
		int newEmailCount, readEmailCount;
		//
		Store store = getStore();
		Folder folder = getFolder(store);
		/* 打开收件箱
		 * Folder.READ_ONLY：
		 * 		只读权限 Folder.READ_WRITE：
		 * 		可读可写（可以修改邮件的状态）
		 */
		// 由于POP3协议无法获知邮件的状态,
		// 所以getUnreadMessageCount得到的是收件箱的邮件总数
		readEmailCount = folder.getUnreadMessageCount();
		EmailLogger.info("未读邮件数: " + readEmailCount);

		// 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
		deletedEmailCount = folder.getDeletedMessageCount();
		EmailLogger.info("删除邮件数: " + deletedEmailCount);
		newEmailCount = folder.getNewMessageCount();
		EmailLogger.info("新邮件: " + newEmailCount);
		// 获得收件箱中的邮件总数
		totalEmailCount = folder.getMessageCount();
		EmailLogger.info("邮件总数: " + totalEmailCount);

		// 得到收件箱中的所有邮件,并解析
		// Message[] messages = folder.getMessages();
		/**
		 * 
		 */
		Message[] messages;
		EmailLogger.info("start to search emails");
		if(isNotepad == false) {
			//SearchTerm notTerm = new NotTerm(new SubjectTerm(
			//		NotepadUI.PreSubjectString));   
			// get messages
			//messages = folder.search(notTerm);
			messages = folder.getMessages();
		} else {
			String fromString = (String) EmailDataManager.getData(
					EmailDataManager.getEmailAddr());
			// String reveivedType = Message.RecipientType.TO;
			SearchTerm andTerm = new AndTerm(
					new SubjectTerm(NotepadUI.PreSubjectString), 
					new RecipientStringTerm(
							Message.RecipientType.TO, fromString)); 
			// get messages
			messages = folder.search(andTerm);
		}
		EmailLogger.info("end searching emails");
		EmailLogger.info("number of searched email: " + messages.length);
		
		final int msgLen = messages.length;
		final int startNum = pageNum * perMsgsLen;
		final int endNum = (pageNum + 1) * perMsgsLen;
		if(msgLen <= startNum) {
			return null;
		}
		//
		int realPageMsgLen;
		if(msgLen >= endNum) {
			realPageMsgLen = perMsgsLen;
		} else {
			realPageMsgLen = msgLen - startNum + 1;
		}
		
		EmailLogger.info("start to get the interval messages: [" + startNum 
				+ ", " + (startNum + realPageMsgLen - 1) + "]");
		// instance
		EmailMessage[] messageList = new EmailMessage[realPageMsgLen]; 
		/*
		 * Parse Email Messages
		 */
		int idx = 0;
		for(int i = startNum; i < startNum + realPageMsgLen; i++) {
			EmailLogger.info("正在解释第" + idx + "封邮件...");
			messageList[idx] = ReceivedEmailProcessor.
					parseOneMessage(messages[msgLen - i - 1], isGetAttachment);
			
			idx++;
		}
		//
		EmailMessage.setTotalReceivedEmailCount(totalEmailCount);
		EmailMessage.setNewReceivedEmailCount(newEmailCount);
		EmailMessage.setReadReceivedEmailCount(readEmailCount);
		EmailMessage.setDeletedReceivedEmailCount(deletedEmailCount);
		
		// 释放资源
		closeFoler(folder, true);
		closeStore(store);
		
		return messageList;
	}
	/**
	 * 获取指定邮件(messageNumber)的附件
	 * 读取输入流中的数据保存至指定目录(path)
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MessagingException 
	 * @problems: 
	 * 		可能附件已经过期了
	 */
	public static boolean saveAttachments(int messageNumber, 
			String path, String singleFileName) throws 
			FileNotFoundException, IOException, MessagingException {	
		// get store
		Store store = getStore();
		Folder folder = getFolder(store);
		
		// get correspoding message
		MimeMessage message = (MimeMessage)folder.
				getMessage(messageNumber); 
		
		EmailLogger.info("saveAttachments(*) 001 - singleFileName" + singleFileName);
		// save attachments
		String strInfo = EnumType.SuccessInfoStr;
		ReceivedEmailProcessor.getAttachments(
				message, path, singleFileName, strInfo);
		EmailLogger.info("saveAttachments(*) 002 - singleFileName" + singleFileName);
		
		// 释放资源
		closeFoler(folder, true);
		closeStore(store);
		if(singleFileName != null && 
				strInfo.equals(EnumType.SuccessInfoStr)) {
			return true;
		}
		if(strInfo.equals(EnumType.SuccessInfoStr)) {
			JOptionPane.showMessageDialog(null, "下载" 
				+ "所有附件成功");
			// log
			EmailLogger.info("download attachments successfully" +
					" - message number is: " 
					+ messageNumber + " - path to: " + path);
			return true;
		} else if(strInfo.equals(EnumType.NoneInfoStr)) {
			JOptionPane.showMessageDialog(null, "下载" 
					+ "所有附件失败 或 部分附件失败");
			return false;
		}
		
		return true;
	}
}