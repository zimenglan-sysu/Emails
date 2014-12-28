package Util.Email;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Util.Email.Logging.EmailLogger;

import DataInfo.EnumType;

/**
 * @singleton 全局数据管理类
 * 比如本地账户邮箱地址，登陆密码等数据。
 * 以下key表示响应的全局数据
 * "LocalAddress" 登陆的本地账户
 * "LocalPassword" 本地账户的邮箱密码
 * 或者添加其他的(key, value)
 * @author ddk
 * @time 2014-12-07 
 */
public final class EmailDataManager {
	// 使用线程安全的Map
	private static Map<String, Object> emailMData = 
			new ConcurrentHashMap<String, Object>();

	private static EmailDataManager instance = null;

	// common data -- current no use
	public static Map<String, String> commonData = 
			new ConcurrentHashMap<>();
	
	private static final String emailHost = "emailHost";
	private static final String SmtpEmailHost = "smtp.emailHost";
	private static final String POP3EmailHost = "pop3.emailHost";
	private static final String ImapEmailHost = "imap.emailHost";
	private static final String emailAddr = "emailAddr";
	private static final String emailPswd = "emailPswd";
	
	public static String getSmtpEmailHost() {
		return SmtpEmailHost;
	}
	
	public static String getPop3EmailHost() {
		return POP3EmailHost;
	}
	
	public static String getImapEmailHost() {
		return ImapEmailHost;
	}
	
	//
	public static String getEmailAddr() {
		return emailAddr;
	}
	
	public static String getEmailPswd() {
		return emailPswd;
	}
	
	/**
	 * sample for test
	 * @return
	 */
	public static String getDefaultEmailAddr() {
		final String tmpEmailAddr = ""; 
		return tmpEmailAddr;
	}
	
	public static String getDefaultEmailPswd() {
		final String tmpEmailPswd = ""; 
		return tmpEmailPswd;
	}
	
	public static String getDefaultEmailToAddr() {
		final String tmpEmailAddr = "dongdk.sysu@foxmail.com"; 
		return tmpEmailAddr;
	}
	
	private EmailDataManager() {
		// 添加测试数据，方便测试
		addData(SmtpEmailHost, "126.com");
		addData(emailPswd, "sysu512");
		addData(emailAddr, "xxxxxxxxxx");

		// 初始化设置数据
		initCommonData();
	}

	private void initCommonData() {
		// do nothing
	}

	// 添加数据
	// true: 添加新数据, false: 数据已存在
	public static boolean addData(String key, Object value) {
		if (getEmailMData().containsKey(key) == false) {
			getEmailMData().put(key, value);
			return true;
		}
		
		return false;
	}

	// 获取全局数据值
	public static Object getData(String key) {
		if (getEmailMData().containsKey(key)) {
			return getEmailMData().get(key);
		}
		
		return null;
	}

	// 更新全局数据值
	// true: 更新数据, false: 失败
	public static boolean updateData(String key, Object newValue) {
		if (getEmailMData().containsKey(key)) {
			getEmailMData().put(key, newValue);
			return true;
		} else {
			addData(key, newValue);
			return false;
		}
	}

	// 删除全局数据值
	// true: 删除成功, false: 失败
	public static boolean deleteData(String key, Object newValue) {
		if (getEmailMData().containsKey(key)) {
			getEmailMData().remove(key);
			return true;
		} else {
			return false;
		}
	}
	
	// 获取emailMDataManager实例
	public static EmailDataManager getInstance() { 
		if(instance == null) {
			instance = new EmailDataManager();
		}
		
		return instance; 
	}
	
	public static Map<String, Object> getEmailMData() {
		return emailMData;
	}
	
	public static Map<String, String> getCommonData() {
		return commonData;
	}

	/**
	 * Set Default
	 */
	public static void DefaultLogin() {
		EmailDataManager.addData(EmailDataManager.getEmailAddr(), 
				EmailDataManager.getDefaultEmailAddr());
		EmailDataManager.addData(EmailDataManager.getEmailPswd(), 
				EmailDataManager.getDefaultEmailPswd());
		String userName = (String) EmailDataManager.
				getData(EmailDataManager.getEmailAddr());
		
		String smtpHostName = EnumType.MAIL_SMTP_HOSTTYPE + "." + 
				userName.substring(userName.indexOf("@") + 1);
		String pop3HostName = EnumType.MAIL_POP3_HOSTTYPE+ "." + 
				userName.substring(userName.indexOf("@") + 1);
		String imapHostName = EnumType.MAIL_IMAP_HOSTTYPE + "." + 
				userName.substring(userName.indexOf("@") + 1);
		
		EmailDataManager.addData(
				EmailDataManager.getSmtpEmailHost(), smtpHostName);
		EmailDataManager.addData(
				EmailDataManager.getPop3EmailHost(), pop3HostName);
		EmailDataManager.addData(
				EmailDataManager.getImapEmailHost(), imapHostName);
		String msg = "EmailDataManager - DefaultLogin: " +
				"set default user for sending and receiving emails";
		EmailLogger.info(msg);
	}
}