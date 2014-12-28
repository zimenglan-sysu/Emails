package EmailProcessor.Recieves;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPMessage;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import DataInfo.MailAuthenticator;
import Util.Email.CheckEmail;
import Util.Email.EmailDataManager;
import Util.Email.Logging.EmailLogger;

/**
 * @author ddk
 * @Created 14.12.21
 * 使用IMAP协议接收邮件
 * POP3和IMAP协议的区别:
 * 		POP3协议允许电子邮件客户端下载服务器上的邮件
 * 		但是在客户端的操作(如移动邮件、标记已读等)，不会反馈到服务器上
 * 		比如通过客户端收取了邮箱中的3封邮件并移动到其它文件夹，
 * 		邮箱服务器上的这些邮件是没有同时被移动的
 * 
 * 		IMAP<协议提供web mail与电子邮件客户端之间的双向通信，
 * 		客户端的操作都会同步反应到服务器上，对邮件进行的操作，服务上	
 * 		的邮件也会做相应的动作。比如在客户端收取了邮箱中的3封邮件
 * 		并将其中一封标记为已读，将另外两封标记为删除，这些操作会 即时反馈到服务器上
 * 
 * 两种协议相比，IMAP 整体上为用户带来更为便捷和可靠的体验
 * POP3更易丢失邮件或多次下载相同的邮件，但IMAP通过邮件客户端
 * 与web mail之间的双向同步功能很好地避免了这些问题
 */
public class ReceivedIMAPProcessor {
	/**
	 * variables
	 */
	// 邮件发送协议
	private final static String DefaultProtocol = "imap";
	// 默认收邮件文件夹
	private final static String DefaultFolder = "INBOX";
	// SMTP邮件服务器
	private final static String DefaultHost = "qq.com";
	// SMTP邮件服务器默认端口
	private final static String Port = "143";
	// 是否要求身份认证
	private final static String Is_Auth = "true";	
	// 是否启用调试模式
	// 启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息
	private final static String Is_Enable_Debug_Mod = "false";
	// 收邮件人
	private static String defaultSelf = 
			EmailDataManager.getDefaultEmailAddr();
	// 初始化连接邮件服务器的会话信息
	private static Properties defaultProps = null;
	private static Properties props = null;
	//
	private static Authenticator defaultAuthenticator = null;
	private static Authenticator authenticator = null;
	// messages
	private static Message[] messages;
	private static EmailMessage[] messageList;
	// session
	private static Session session = null;
	// default variables
	static {
		defaultProps = new Properties();
		defaultProps.setProperty("mail.store.protocol", 
				DefaultProtocol);
		defaultProps.setProperty("mail.imap.host", DefaultHost);
		// maybe wrong
		defaultProps.setProperty("mail.imap.port", Port);
		defaultProps.setProperty("mail.imap.auth", Is_Auth);
		defaultProps.setProperty("mail.debug",Is_Enable_Debug_Mod);
		
		defaultAuthenticator = new MailAuthenticator();
	}
	/**
	 * 
	 */
	private static ReceivedIMAPProcessor receiver = null;
	/**
	 * Constructor
	 */
	private ReceivedIMAPProcessor() {
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
            session = Session.getInstance(getProperty(), 
            		getAuthenticator());
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
			props.setProperty("mail.imap.host", 
					(String)EmailDataManager.getData(
					EmailDataManager.getImapEmailHost()));
			
			props.setProperty("mail.imap.port", Port);
			props.setProperty("mail.imap.auth", Is_Auth);
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
	public static Message[] getMessages(String folderName, boolean refresh) 
			throws MessagingException {
		if(messages == null || refresh == true) {
			messages = geReceivedtMessage(folderName);
		}
		
		return messages;
	}
	//
	public static EmailMessage[] getEmailMessages(String folderName, boolean refresh, 
			boolean isGetAttachment) throws Exception {
		if(messageList == null || refresh == true) {
			messageList = receive(folderName, isGetAttachment);
		}
		
		return messageList;
	}
	/**
	 * Instance
	 */
	public static ReceivedIMAPProcessor getReceivedPoP3ProcessorInstance() {
		if(receiver == null){
			receiver = new ReceivedIMAPProcessor();
		}
		
		return receiver;
	}
	/**
	 * Instance
	 */
	public static ReceivedIMAPProcessor getReceivedIMAPProcessorInstance() {
		if(receiver == null){
			receiver = new ReceivedIMAPProcessor();
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
		store.connect(username, password);
	
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
	public static Folder getFolder(Store store, String folderName) 
			throws MessagingException {
		// 获得收件箱
		if(folderName == null || folderName.length() < 1) {
			folderName = DefaultFolder;
		}
		Folder folder = store.getFolder(folderName);
		/* 打开收件箱
		 * Folder.READ_ONLY：
		 * 		只读权限 Folder.READ_WRITE：
		 * 		可读可写（可以修改邮件的状态）
		 */
		// folder.open(Folder.READ_WRITE); 
		
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
	public static Message[] geReceivedtMessage(String folderName) throws MessagingException {
		//
		int totalEmailCount, deletedEmailCount;
		int newEmailCount, readEmailCount;
		//
		Store store = getStore();
		Folder folder = getFolder(store, folderName);
		
		/* 打开收件箱
		 * Folder.READ_ONLY：
		 * 		只读权限 Folder.READ_WRITE：
		 * 		可读可写（可以修改邮件的状态）
		 */
		folder.open(Folder.READ_WRITE); 
		// getUnreadMessageCount得到的是收件箱的邮件总数
		readEmailCount = folder.getUnreadMessageCount();
		EmailLogger.info("未读邮件数: " + readEmailCount);
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
		folder.close(true);
		store.close();
		
		return messages;
	}
	
	/**
	 * IMAP - Received Emails
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	public static EmailMessage[] receive(String folderName, boolean isGetAttachment) throws MessagingException, IOException {
		// 准备连接服务器的会话信息
		int totalEmailCount, deletedEmailCount;
		int newEmailCount, readEmailCount;
		//
		Store store = getStore();
		Folder folder = getFolder(store, folderName);

		/* 打开收件箱
		 * Folder.READ_ONLY：
		 * 		只读权限 Folder.READ_WRITE：
		 * 		可读可写（可以修改邮件的状态）
		 */
		// getUnreadMessageCount得到的是收件箱的邮件总数
		readEmailCount = folder.getUnreadMessageCount();
		EmailLogger.info("未读邮件数: " + readEmailCount);
		// 
		deletedEmailCount = folder.getDeletedMessageCount();
		EmailLogger.info("删除邮件数: " + deletedEmailCount);
		newEmailCount = folder.getNewMessageCount();
		EmailLogger.info("新邮件: " + newEmailCount);
		// 获得收件箱中的邮件总数
		totalEmailCount = folder.getMessageCount();
		EmailLogger.info("邮件总数: " + totalEmailCount);

		// 得到收件箱中的所有邮件,并解析
		Message[] messages = folder.getMessages();

		// 解析邮件
		EmailMessage[] messageList =  new EmailMessage[messages.length];
		//
		EmailMessage.setTotalReceivedEmailCount(totalEmailCount);
		EmailMessage.setNewReceivedEmailCount(newEmailCount);
		EmailMessage.setReadReceivedEmailCount(readEmailCount);
		EmailMessage.setDeletedReceivedEmailCount(deletedEmailCount);
		
		int idx = 0;
		for (int i = messages.length - 1; i >= 0; i--) {
			Message message = messages[i];
			IMAPMessage msg = (IMAPMessage) message;
			
			// 解析邮件
			messageList[idx] = ReceivedEmailProcessor.
					parseOneMessage(msg, isGetAttachment); 
			idx++;
			
			// 设置已读标志
			msg.setFlag(Flag.SEEN, true); 
		}

		// 关闭资源
		folder.close(false);
		store.close();
		
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
	public static void saveAttachments(String folderName, int messageNumber, String path)
			throws FileNotFoundException, IOException, MessagingException {	
		// get store
		Store store = getStore();
		Folder folder = getFolder(store, folderName);
		
		// get correspoding message
		MimeMessage message = (MimeMessage)folder.getMessage(messageNumber); 
		
		// save attachments
		String strInfo = EnumType.SuccessInfoStr;
		ReceivedEmailProcessor.getAttachments(message, path, null, strInfo);
		
		// 释放资源
		closeFoler(folder, true);
		closeStore(store);
	}
}