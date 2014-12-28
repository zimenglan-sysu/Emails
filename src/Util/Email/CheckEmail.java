package Util.Email;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

import Util.Email.EmailDataManager;
import Util.Email.Logging.EmailLogger;
import DataInfo.EnumType;

/**
 * 获取邮件账号授权
 * 问题列表: 
 *   1.验证失败有几种可能，要给调用者充足的信息 
 *   2.如果没有打开smtp，也要给提示
 */
public class CheckEmail {
	/** 
	 * SMTP
	 * 根据邮箱地址、密码、协议、服务器地址验证邮箱账户是否正确
	 */
	public static boolean checkSMTPEmailAccount(String host, String protocol,
			String email, String password) {
		Properties props = new Properties();
		props.put(EnumType.MAIL_SMTP_HOST, protocol + "." + host);
		props.put(EnumType.MAIL_SMTP_AUTH, "true"); 
		// get session
		Session session = Session.getDefaultInstance(props);
		// get transport
		Transport transport = null;
		
		try {
			transport = session.getTransport(protocol);
			transport.connect(email, password);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("transport.getURLName(): " 
				+ transport.getURLName());
		boolean flag = false;
		try {
			flag = transport.isConnected();
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			return flag;
		}
	}
	/**
	 *  POP3
	 * 根据邮箱地址、密码、协议、
	 * 服务器地址验证邮箱账户是否正确
	 */
	public static boolean checkPOP3EmailAccount(String host, String protocol,
			String email, String password) {
		Properties props = new Properties();
		props.put(EnumType.MAIL_POP3_HOST, protocol + "." + host);
		props.put(EnumType.MAIL_POP3_AUTH, "true"); 
		// get session
		Session session = Session.getDefaultInstance(props);
		// Get the store
		Store store = null;
		
		try {
			store = session.getStore("pop3");
			store.connect(host, email, password);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("transport.getURLName(): " 
				+ store.getURLName());
		boolean flag = false;
		try {
			flag = store.isConnected();
		} finally {
			try {
				store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			return flag;
		}
	}
	/** 
	 * SMTP or POP3
	 * 根据邮箱地址、密码、协议、
	 * 服务器地址验证邮箱账户是否正确
	 */
	public static boolean checkEmailAccount(String hostType, String host, 
			String protocol, String email, String password) {
		Properties props = new Properties();
		
		if(hostType.equalsIgnoreCase(EnumType.MAIL_SMTP_HOSTTYPE)) {
			props.put(EnumType.MAIL_SMTP_HOST, protocol + "." + host);
			props.put(EnumType.MAIL_SMTP_AUTH, "true"); 
			
			// get session
			Session session = Session.getDefaultInstance(props);
			// get transport
			Transport transport = null;
			
			try {
				transport = session.getTransport(protocol);
				transport.connect(email, password);
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			EmailLogger.info("transport.getURLName(): " 
					+ transport.getURLName());
			boolean flag = false;
			try {
				flag = transport.isConnected();
			} finally {
				try {
					// don't forget close the connection
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				
				return flag;
			}
		} else if(hostType.equalsIgnoreCase(EnumType.MAIL_POP3_HOSTTYPE)) {
			props.put(EnumType.MAIL_POP3_HOST, protocol + "." + host);
			props.put(EnumType.MAIL_POP3_AUTH, "true"); 
			
			// get session
			Session session = Session.getDefaultInstance(props);
			// Get the store
			Store store = null;
			
			try {
				store = session.getStore("pop3");
				store.connect(host, email, password);
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("transport.getURLName(): " 
					+ store.getURLName());
			boolean flag = false;
			try {
				flag = store.isConnected();
			} finally {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				
				return flag;
			}
		} else {
			System.out.println("invalid mail type: " + hostType);
			return false;
		}
	}
	
	/**
	 * convert a String Arre which contains multi-addresses
	 * to a long String
	 */
	public static String getValidAddrs(String[] addrArr) {
		StringBuffer bs = new StringBuffer();
		int count = 0;
		for(String str: addrArr) {
			if(matchAddr(str)) {
				bs.append(str);
				bs.append(EnumType.EmailEegex);
				count++;
			}
		}
		if(bs == null || bs.length() < 1) {
			return null;
		}
		
		return bs.toString();
	}
	
	/**
	 * get valid sddress
	 * @throws Exception
	 */
	public static String[] getValidAddrs(String addr) {
		// check valid or not
		if(addr == null || addr.length() < 1) {
			return null;
		}
		
		//
		StringBuffer bs = new StringBuffer();
		
		// process the fomrat, 
		// like "收件人1<邮件地址1>;收件人2<邮件地址2>;..."
		addr = addr.trim();
		// first remove '>'
		addr = addr.replace(">", "");
		
		String addrArr[] = addr.split(EnumType.EmailEegex);
		int count = 0;
		for(String str: addrArr) {
			// second remove 'personal name<'
			str = str.trim();
			str = str.substring(str.indexOf("<") + 1);
			// match
			if(matchAddr(str)) {
				bs.append(str);
				bs.append(EnumType.EmailEegex);
				count++;
			}
		}
		
		if(bs == null || bs.length() < 1) {
			return null;
		}
		
		String newAddrStr = bs.toString();
		String[] newAddtArr = newAddrStr.split(EnumType.EmailEegex);
		if(count != newAddtArr.length) {
			return null;
		}
		 return newAddtArr;
	}
	
	/**
	 * match email format
	 */
	public static boolean matchAddr(String str) {
		return str.matches(EnumType.REGEX_EMAIL_ADDRESS);
	}
	
	/**
	 * encode email address
	 */
	public static String encodeEmailStr(String str) 
			throws UnsupportedEncodingException {
		str = str.trim();
		if(str == null || str.equalsIgnoreCase("")) {
			return null;
		}
		String encodedStr = MimeUtility.encodeText(str);
		return encodedStr;
	}
	/**
	 * decode email address
	 */
	public static String decodeEmailStr(String str) 
			throws UnsupportedEncodingException {
		str = str.trim();
		if(str == null || str.equalsIgnoreCase("")) {
			return null;
		}
		String decodedStr = MimeUtility.decodeText(str);
		return decodedStr;
	}
	
	/**
	 * format date
	 */
	public static String formatDateString(Date date, String pattern){ 
		if (date == null)
			return "";

		if (pattern == null || "".equals(pattern)) {
			pattern = "yyyy.MM.dd-E-hh:mm:ss ";
		}

		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 获取对应附件类型的默认存储路径
	 */
	public static String getSaveDir4Attachment(String filename) {
		if(filename.endsWith(".jpg") || filename.endsWith(".png") 
				|| filename.endsWith(".jpeg")) {
			return PathManager.getDBImagesPath();
		} else if(filename.endsWith(".html") || filename.endsWith(".htm")) {
			return PathManager.getDBHtmlsPath();
		} if(filename.endsWith(".pdf")) {
			return PathManager.getDBPdfsPath();
		} else if(filename.endsWith(".txt") || filename.endsWith(".doc") 
				|| filename.endsWith(".log") || filename.endsWith(".docx")
				|| filename.endsWith(".md") || filename.endsWith(".pptx")) {
			return PathManager.getDBFilesPath();
		}
		
		return PathManager.getDBOthersPath();
	}
	
	/**
	 * 获取回复邮件的所有除了用户的收件人
	 */
	public static String getRemoveItselfAddrsWhenReplyAll(
			String addrStr, String itselfAddr) {
		//
		if(addrStr == null) {
			return null;
		}
		EmailLogger.info("getRemoveItselfAddrsWhenReplyAll - addrStr: " 
				+ addrStr);
		if(itselfAddr == null) {
			return addrStr;
		}
		EmailLogger.info("getRemoveItselfAddrsWhenReplyAll - itselfAddr: " 
				+ itselfAddr);
		String[] addrStrArr = addrStr.split(EnumType.EmailEegex);
		String newAddrStr = "";
		for(String str: addrStrArr) {
			int idx = str.indexOf(itselfAddr);
			if(idx != -1) {
				continue;
			}
			newAddrStr = newAddrStr + str + EnumType.EmailEegex;
		}
		
		return newAddrStr;
	}
	
	/**
	 * Test
	 */
	public static void main(String[] args) throws Exception {
		//
		Properties props = new Properties();
		props.put("mail.smtp.host", "126.com");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		Transport transport = session.getTransport("smtp");
		
		// 测试账号
		transport.connect(EmailDataManager.getData(
				EmailDataManager.getInstance().getEmailAddr()).toString(), 
				EmailDataManager.getData(EmailDataManager.getInstance().getEmailPswd())
				.toString()
		);
		
		System.out.println(transport.getURLName());
		transport.close();
	}
}