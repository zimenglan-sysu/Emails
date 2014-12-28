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
 * ʹ��IMAPЭ������ʼ�
 * POP3��IMAPЭ�������:
 * 		POP3Э����������ʼ��ͻ������ط������ϵ��ʼ�
 * 		�����ڿͻ��˵Ĳ���(���ƶ��ʼ�������Ѷ���)�����ᷴ������������
 * 		����ͨ���ͻ�����ȡ�������е�3���ʼ����ƶ��������ļ��У�
 * 		����������ϵ���Щ�ʼ���û��ͬʱ���ƶ���
 * 
 * 		IMAP<Э���ṩweb mail������ʼ��ͻ���֮���˫��ͨ�ţ�
 * 		�ͻ��˵Ĳ�������ͬ����Ӧ���������ϣ����ʼ����еĲ�����������	
 * 		���ʼ�Ҳ������Ӧ�Ķ����������ڿͻ�����ȡ�������е�3���ʼ�
 * 		��������һ����Ϊ�Ѷ���������������Ϊɾ������Щ������ ��ʱ��������������
 * 
 * ����Э����ȣ�IMAP ������Ϊ�û�������Ϊ��ݺͿɿ�������
 * POP3���׶�ʧ�ʼ�����������ͬ���ʼ�����IMAPͨ���ʼ��ͻ���
 * ��web mail֮���˫��ͬ�����ܺܺõر�������Щ����
 */
public class ReceivedIMAPProcessor {
	/**
	 * variables
	 */
	// �ʼ�����Э��
	private final static String DefaultProtocol = "imap";
	// Ĭ�����ʼ��ļ���
	private final static String DefaultFolder = "INBOX";
	// SMTP�ʼ�������
	private final static String DefaultHost = "qq.com";
	// SMTP�ʼ�������Ĭ�϶˿�
	private final static String Port = "143";
	// �Ƿ�Ҫ�������֤
	private final static String Is_Auth = "true";	
	// �Ƿ����õ���ģʽ
	// ���õ���ģʽ�ɴ�ӡ�ͻ������������������ʱһ��һ�����Ӧ��Ϣ
	private final static String Is_Enable_Debug_Mod = "false";
	// ���ʼ���
	private static String defaultSelf = 
			EmailDataManager.getDefaultEmailAddr();
	// ��ʼ�������ʼ��������ĻỰ��Ϣ
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
	            // ��session�������˻���Ϣ��Transport�����ʼ�ʱ��ʹ��  
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
		// ����ռ���
		if(folderName == null || folderName.length() < 1) {
			folderName = DefaultFolder;
		}
		Folder folder = store.getFolder(folderName);
		/* ���ռ���
		 * Folder.READ_ONLY��
		 * 		ֻ��Ȩ�� Folder.READ_WRITE��
		 * 		�ɶ���д�������޸��ʼ���״̬��
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
	 * ������ȡԭʼ��Message����, ��folder.getMessages()
	 * @throws MessagingException
	 */
	public static Message[] geReceivedtMessage(String folderName) throws MessagingException {
		//
		int totalEmailCount, deletedEmailCount;
		int newEmailCount, readEmailCount;
		//
		Store store = getStore();
		Folder folder = getFolder(store, folderName);
		
		/* ���ռ���
		 * Folder.READ_ONLY��
		 * 		ֻ��Ȩ�� Folder.READ_WRITE��
		 * 		�ɶ���д�������޸��ʼ���״̬��
		 */
		folder.open(Folder.READ_WRITE); 
		// getUnreadMessageCount�õ������ռ�����ʼ�����
		readEmailCount = folder.getUnreadMessageCount();
		EmailLogger.info("δ���ʼ���: " + readEmailCount);
		deletedEmailCount = folder.getDeletedMessageCount();
		EmailLogger.info("ɾ���ʼ���: " + deletedEmailCount);
		newEmailCount = folder.getNewMessageCount();
		EmailLogger.info("���ʼ�: " + newEmailCount);
		// ����ռ����е��ʼ�����
		totalEmailCount = folder.getMessageCount();
		EmailLogger.info("�ʼ�����: " + totalEmailCount);

		// �õ��ռ����е������ʼ�,������
		Message[] messages = folder.getMessages();
		
		// �ͷ���Դ
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
		// ׼�����ӷ������ĻỰ��Ϣ
		int totalEmailCount, deletedEmailCount;
		int newEmailCount, readEmailCount;
		//
		Store store = getStore();
		Folder folder = getFolder(store, folderName);

		/* ���ռ���
		 * Folder.READ_ONLY��
		 * 		ֻ��Ȩ�� Folder.READ_WRITE��
		 * 		�ɶ���д�������޸��ʼ���״̬��
		 */
		// getUnreadMessageCount�õ������ռ�����ʼ�����
		readEmailCount = folder.getUnreadMessageCount();
		EmailLogger.info("δ���ʼ���: " + readEmailCount);
		// 
		deletedEmailCount = folder.getDeletedMessageCount();
		EmailLogger.info("ɾ���ʼ���: " + deletedEmailCount);
		newEmailCount = folder.getNewMessageCount();
		EmailLogger.info("���ʼ�: " + newEmailCount);
		// ����ռ����е��ʼ�����
		totalEmailCount = folder.getMessageCount();
		EmailLogger.info("�ʼ�����: " + totalEmailCount);

		// �õ��ռ����е������ʼ�,������
		Message[] messages = folder.getMessages();

		// �����ʼ�
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
			
			// �����ʼ�
			messageList[idx] = ReceivedEmailProcessor.
					parseOneMessage(msg, isGetAttachment); 
			idx++;
			
			// �����Ѷ���־
			msg.setFlag(Flag.SEEN, true); 
		}

		// �ر���Դ
		folder.close(false);
		store.close();
		
		return messageList;
	}
	/**
	 * ��ȡָ���ʼ�(messageNumber)�ĸ���
	 * ��ȡ�������е����ݱ�����ָ��Ŀ¼(path)
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MessagingException 
	 * @problems: 
	 * 		���ܸ����Ѿ�������
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
		
		// �ͷ���Դ
		closeFoler(folder, true);
		closeStore(store);
	}
}