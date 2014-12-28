package EmailProcessor.Recieves;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import Util.Email.CheckEmail;
import Util.Email.Logging.EmailLogger;
import Util.Email.PathManager;

/**
 * @author ddk
 * based operators
 */
public class ReceivedEmailProcessor {
	private final static long defaultVersion = 1234758215;
	private final static int defaultReceivedLength = 5;
	
	/**
	 * 解析邮件
	 * @param messages
	 * @problem:
	 * 		解析的效率低下, 不在承受范围之内
	 */
	public static EmailMessage[] parseMessage(Message[] messages, boolean isGetAttachment)
			throws MessagingException, IOException {
		EmailLogger.info("start to parse all email message");
		
		if (messages == null || messages.length < 1) {
			JOptionPane.showMessageDialog(null, "未找到要解析的邮件(s)!");
			EmailLogger.info("未找到要解析的邮件!");
			return null;
		}
		
		EmailMessage[] messageList = new EmailMessage[messages.length];
		// 解析所有邮件
		final int MsgLen = messages.length;
		int count = MsgLen > defaultReceivedLength ? 
				defaultReceivedLength : messages.length;
		
		// 降序
		int idx = 0;
		for (int i = messages.length - 1; i >= MsgLen - defaultReceivedLength; i--) {
			EmailLogger.info("正在解析第" + idx + "封邮件");
			// get original email message
			MimeMessage msg = (MimeMessage) messages[i];

			messageList[idx] = parseOneMessage(msg, isGetAttachment);
			idx++;
		}
		
		return messageList;
	}
	/**
	 * 解析一个Message
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static EmailMessage parseOneMessage(Message msg0, boolean isGetAttachment)
			throws MessagingException, IOException {
		// 
		EmailLogger.info("start to parse one email message");
		if (msg0 == null) {
			JOptionPane.showMessageDialog(null, "未找到要解析的邮件!");
			EmailLogger.info("未找到要解析的邮件!");
			return null;
		}
		// get original email message
		MimeMessage msg = (MimeMessage)msg0;
		EmailMessage message = new EmailMessage();
		
		/**
		 * convert Message to EmailMessage
		 */
		// email type
		message.setEmailType(EmailMessage.RECEIVE_TYPE_STRING);
		// index
		message.setReceivedIdx(msg.getMessageNumber());
		// subject
		message.setSubject(getSubject(msg));
		// log
		EmailLogger.info("subject: " + message.getSubject());
		
		// fromStr
		message.setFromStr(getFrom(msg) + EnumType.EmailEegex);
		// from
		String fromStr = message.getFromStr();
		String from = fromStr.substring(fromStr.indexOf("<") + 1, 
				fromStr.indexOf(">"));
		message.setFrom(from);
		
		// toStr
		message.setToStr(getReceiveAddress(msg, 
				Message.RecipientType.TO));
		// to
		String[] to = CheckEmail.getValidAddrs(
				message.getToStr());
		message.setTo(to);
		// ccsStr
		message.setCcsStr(getReceiveAddress(msg, 
				Message.RecipientType.CC));
		// ccs
		String[] ccs = CheckEmail.getValidAddrs(
				getReceiveAddress(msg, Message.RecipientType.CC));
		message.setCcs(ccs);
		// bccsStr
		message.setBccsStr(getReceiveAddress(msg, 
				Message.RecipientType.BCC));
		// bccs
		String[] bccs = CheckEmail.getValidAddrs(
				getReceiveAddress(msg, Message.RecipientType.BCC));
		message.setBccs(bccs);
		// has seen
		message.setReceivedHasSeen(isSeen(msg));
		// priority
		message.setPriority(getPriority(msg));
		// reply sign
		message.setReceivedReplySign(isReplySign(msg));
		// content
		StringBuffer content = new StringBuffer(30);
		getMailTextContent(msg, content);
		message.setContent(content.toString());
		// content type
		message.setContentType(msg.getContentType());
		
		// pre-view of content
		final int mLen = message.getContent().length();
		String preViewContentString = mLen
				> EnumType.DefaultPreViewLength ?
				content.toString().substring(
						0, EnumType.DefaultPreViewLength)
				+ "..." : content.toString();
		// System.out.println("pre-view: " + preViewContentString);
		message.setPreReceivedViewContent(preViewContentString);
		
		// send & receive date
		message.setSendDate(msg.getSentDate());
		// can't find the received date, i don't know why
		message.setReceivedDate(msg.getReceivedDate());
		
		if(isGetAttachment == false) {
			return message;
		}
		
		/**
		 * test efficiency of the parsing attachments
		 */
		// EmailLogger.info("test efficiency of the parsing attachments");
		
		// is attachment
		message.setHasAttachMent(isHasAttachments(msg));
		// get fileNames & inputStreams
		if(message.getHasAttachMent() == true) {
			ArrayList<String> fileNames = new ArrayList<String>();
			// only get file name
			getAttachments(msg, fileNames);
			
			// set filenames
			if(fileNames.size() > 0) {
				String[] files = new String[fileNames.size()];
				int idx = 0;
				for(String fn: fileNames) {
					files[idx] = fn;
					idx++;
				}
				
				message.setFileNames(files);
			}
		}
		
		return message;
	}
	
	// **************************************************************************************
	
	/**
	 * 获得邮件主题
	 */
	public static String getSubject(MimeMessage msg)
			throws UnsupportedEncodingException, MessagingException {
		return CheckEmail.decodeEmailStr(msg.getSubject());
	}
	/**
	 * 获得邮件发件人
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static String getFrom(MimeMessage msg) throws MessagingException,
			UnsupportedEncodingException {
		String from = "";
		Address[] froms = msg.getFrom();
		if (froms.length < 1) {
			EmailLogger.warning("没有发件人!");
			throw new MessagingException("没有发件人!");
		}
			
		InternetAddress address = (InternetAddress) froms[0];
		String person = address.getPersonal();
		if (person != null) {
			person = CheckEmail.decodeEmailStr(person) + "";
		} else {
			person = "";
		}
		from = person + "<" + address.getAddress().toString() + ">";

		return from;
	}
	/**
	 * 根据收件人类型，获取邮件收件人、抄送和密送地址。
	 * 如果收件人类型为空，则获得所有的收件人
	 * 		Message.RecipientType.TO 收件人
	 * 		Message.RecipientType.CC 抄送
	 * 		Message.RecipientType.BCC 密送
	 * @return 收件人<邮件地址1>;收件人;<邮件地址2>;...
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	public static String getReceiveAddress(MimeMessage msg, 
			Message.RecipientType type)
			throws MessagingException, UnsupportedEncodingException {
		//
		StringBuffer addrsArr = new StringBuffer();
		Address[] addresss = null;
		if (type == null) {
			addresss = msg.getAllRecipients();
		} else {
			addresss = msg.getRecipients(type);
		}
		// check valid or not
		if (addresss == null || addresss.length < 1) {
			if(type.equals(Message.RecipientType.TO)) {
				EmailLogger.warning(type + ": 没有收件人!");
				throw new MessagingException("没有收件人!");
			} else {
				return null;
			}
		}
		// get addresses
		for (Address address : addresss) {
			InternetAddress internetAddress = (InternetAddress) address;
			String person = internetAddress.getPersonal();
			if (person != null) {
				person = CheckEmail.decodeEmailStr(person) + "";
			} else {
				person = "";
			}
			String addr = person + "<" + internetAddress.getAddress().
					toString() + ">";
			
			addrsArr.append(addr);
			// add
			addrsArr.append(EnumType.EmailEegex);
		}
		
		return addrsArr.toString();
	}
	/**
	 * 判断邮件中是否包含附件
	 * 当配置要么是 Part.ATTACHMENT, 要么是 Part.INLINE 时
	 * @return 邮件中存在附件返回true，不存在返回false
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static boolean isHasAttachments(Part part)
			throws MessagingException, IOException {
		//
		boolean flag = false;
		if (part.isMimeType("multipart/*")) {
			// 
			MimeMultipart multipart = (MimeMultipart) part.getContent();
			
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				// determine whether contain attachments
				String disp = bodyPart.getDisposition();
				if (disp != null
						&& (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp
								.equalsIgnoreCase(Part.INLINE))) {
					flag = true;
				} else if (bodyPart.isMimeType("multipart/*")) {
					flag = isHasAttachments(bodyPart);
				} else {
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("application") != -1) {
						flag = true;
					}

					if (contentType.indexOf("name") != -1) {
						flag = true;
					}
				}

				if (flag)
					break;
			}
		} else if (part.isMimeType("message/rfc822")) {
			flag = isHasAttachments((Part) part.getContent());
		}
		
		// return
		return flag;
	}
	/**
	 * 判断邮件是否已读
	 * @throws MessagingException
	 */
	public static boolean isSeen(MimeMessage msg) throws MessagingException {
		return msg.getFlags().contains(Flags.Flag.SEEN);
	}
	/**
	 * 判断邮件是否需要阅读回执
	 * @throws MessagingException
	 */
	public static boolean isReplySign(MimeMessage msg)
			throws MessagingException {
		boolean replySign = false;
		String[] headers = msg.getHeader("Disposition-Notification-To");
		if (headers != null) {
			replySign = true;
		}
		
		return replySign;
	}
	/**
	 * 获得邮件的优先级
	 * @return 1(High):紧急 3:普通(Normal) 5:低(Low)
	 * @throws MessagingException
	 */
	public static String getPriority(MimeMessage msg) throws MessagingException {
		String priority = "普通";
		String[] headers = msg.getHeader("X-Priority");
		if (headers != null) {
			String headerPriority = headers[0];
			if (headerPriority.indexOf("1") != -1
					|| headerPriority.indexOf("High") != -1)
				priority = "紧急";
			else if (headerPriority.indexOf("5") != -1
					|| headerPriority.indexOf("Low") != -1)
				priority = "低";
			else if(headerPriority.indexOf("3") != -1
					|| headerPriority.indexOf("Middle") != -1)
				priority = "普通";
		}
		
		return priority;
	}
	/**
	 * 获得邮件文本内容
	 * @param part
	 * 		邮件体
	 * @param content
	 *      存储邮件文本内容的字符串
	 * @throws MessagingException
	 * @throws IOException
	 * @problem:
	 * 		文本为html等格式, 不能单纯作为String来存储
	 */
	public static String getMailTextContent(Part part, StringBuffer content)
			throws MessagingException, IOException {
		// 如果是文本类型的附件，通过getContent方法可以取到文本内容
		// 但这不是我们需要的结果，所以在这里要做判断
		boolean isContainTextAttach = part.getContentType()
				.indexOf("name") > 0;
		// 
		String type = null;
		if (part.isMimeType("text/*") && !isContainTextAttach) {
			content.append(part.getContent().toString());
		//
		} else if (part.isMimeType("message/rfc822")) {
			getMailTextContent((Part) part.getContent(), content);
		// multiple parts
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				getMailTextContent(bodyPart, content);
			}
		}
		
		return type;
	}
	
	// **************************************************************************************
	
	 
	/**
	 * 保存附件, 当singleFileName不为空且有效时,仅下载singleFileName
	 * 		否则将下载全部附件
	 * @param part
	 * 		邮件中多个组合体中的其中一个组合体
	 * @param path
	 * 		附件保存目录
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @problem: 获取到的inputstream失效, 因为会导致其为Unknown Source
	 * 	简单理解为, 后续的操作, 也就是通过EmailMessage的inputstream来获取
	 * 	附件, 由于该Message对应的连接(即store.connect())已经关闭了, 
	 * 	导致inputstream失效
	 * 	另外, 可能存在附件过期的因素(因为邮箱维护附件是有期限的)
	 */
	public static void getAttachments(Part part, String path, 
			String singleFileName, String strInfo) 
			throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		//
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
			// 复杂体邮件包含多个邮件体
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// 获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);
				// 某一个邮件体也有可能是由多个邮件体组成的复杂体
				String disp = bodyPart.getDisposition();
				if (disp != null
						&& (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp
								.equalsIgnoreCase(Part.INLINE))) {
					
					InputStream in = bodyPart.getInputStream();
					String filename= CheckEmail.decodeEmailStr(
							bodyPart.getFileName());
					
					// 
					if(singleFileName != null) {
						if(singleFileName.equals(filename)){
							EmailLogger.info("getAttachments 001 - singleFileName: " + singleFileName);
							boolean flag2 = saveAttachments(in, path, singleFileName);
							if(flag2 == true) {
								JOptionPane.showMessageDialog(null, "下载" 
										+ singleFileName + "附件成功");
							} else {
								strInfo = EnumType.NoneInfoStr;
							}
							
							return;
						}
					} else if(saveAttachments(in, path, filename) == false) {
						EmailLogger.info("getAttachments 0010 - filename: " + filename);
						strInfo = EnumType.NoneInfoStr;
					}
				//	
				} else if (bodyPart.isMimeType("multipart/*")) {
					getAttachments(bodyPart, path, singleFileName, strInfo);
				// 
				} else {
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("name") != -1
							|| contentType.indexOf("application") != -1) {

						InputStream in = bodyPart.getInputStream();
						String filename= CheckEmail.decodeEmailStr(
								bodyPart.getFileName());						
						// 
						if(singleFileName != null) {
							if(singleFileName.equals(filename)){
								EmailLogger.info("getAttachments 001 - singleFileName: " + singleFileName);
								boolean flag2 = saveAttachments(in, path, singleFileName);
								if(flag2 == true) {
									JOptionPane.showMessageDialog(null, "下载" 
											+ singleFileName + "附件成功");
								} else {
									strInfo = EnumType.NoneInfoStr;
								}
								
								return;
							}
						} else if(saveAttachments(in, path, filename) == false) {
							EmailLogger.info("getAttachments 0020 - filename: " + filename);
							strInfo = EnumType.NoneInfoStr;
						}
					}
				}
			}
		//
		} else if (part.isMimeType("message/rfc822")) {
			getAttachments((Part)part.getContent(), 
					path, singleFileName, strInfo);
		}
	}
	/**
	 * 仅仅获取文件名, 不提供下载
	 */
	public static void getAttachments(Part part, ArrayList<String> fileNames) 
			throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		//
		EmailLogger.info("getAttachments - 仅仅获取附件名");
		
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
			// 复杂体邮件包含多个邮件体
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// 获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);
				// 某一个邮件体也有可能是由多个邮件体组成的复杂体
				String disp = bodyPart.getDisposition();
				if (disp != null
						&& (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp
								.equalsIgnoreCase(Part.INLINE))) {
					String filename = bodyPart.getFileName();
					if(filename == null || filename.trim().length() < 1) {
						continue;
					}
					filename= CheckEmail.decodeEmailStr(
							bodyPart.getFileName());
					fileNames.add(filename);
				//	
				} else if (bodyPart.isMimeType("multipart/*")) {
					getAttachments(bodyPart, fileNames);
				// 
				} else {
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("name") != -1
							|| contentType.indexOf("application") != -1) {
						String filename= CheckEmail.decodeEmailStr(
								bodyPart.getFileName());
						fileNames.add(filename);
					}
				}
			}
		//
		} else if (part.isMimeType("message/rfc822")) {
			getAttachments((Part)part.getContent(), fileNames);
		}
	}
	
	// *************************************************************************************
	
	/**
	 * 读取输入流中的数据保存至指定目录
	 * @param in 输入流
	 * @param fileName 文件名
	 * @param destDir 文件存储目录
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @problems: 
	 * 		可能附件已经过期了
	 */
	public static boolean saveAttachments(InputStream in, 
			String saveDir, String fileName)
			throws FileNotFoundException, IOException {
		boolean flag = false;
		if(in == null || fileName == null || fileName.length() < 1) {
			JOptionPane.showMessageDialog(null, "下载 " + fileName + " 附件失败");
			return flag;
		}
		// get input stream
		BufferedInputStream cin = new BufferedInputStream(in);
		
		// 
		int subIdx = fileName.lastIndexOf(".");
		String filePath = saveDir + fileName;
		File filer = new File(filePath);
		
		int idx = 1;
		while(filer.exists()) {
			filePath = saveDir + fileName.substring(0, subIdx) + "(" +
					Integer.toString(idx) + ")" + fileName.substring(subIdx);
			filer = new File(filePath);
		}
		
		String msg = "saveAttachments " + fileName + " is saved as " + filePath;
		EmailLogger.info(msg);
		EmailLogger.info("*************************");
		
		// get output stream
		BufferedOutputStream cout = new BufferedOutputStream(
				new FileOutputStream(filer));
		int len = -1;
		while ((len = cin.read()) != -1) {
			cout.write(len);
			cout.flush();
		}
		
		// close
		cout.close();
		cin.close();
		
		flag = true;
		return flag;
	}
}