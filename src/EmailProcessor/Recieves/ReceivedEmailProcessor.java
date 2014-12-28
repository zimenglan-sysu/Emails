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
	 * �����ʼ�
	 * @param messages
	 * @problem:
	 * 		������Ч�ʵ���, ���ڳ��ܷ�Χ֮��
	 */
	public static EmailMessage[] parseMessage(Message[] messages, boolean isGetAttachment)
			throws MessagingException, IOException {
		EmailLogger.info("start to parse all email message");
		
		if (messages == null || messages.length < 1) {
			JOptionPane.showMessageDialog(null, "δ�ҵ�Ҫ�������ʼ�(s)!");
			EmailLogger.info("δ�ҵ�Ҫ�������ʼ�!");
			return null;
		}
		
		EmailMessage[] messageList = new EmailMessage[messages.length];
		// ���������ʼ�
		final int MsgLen = messages.length;
		int count = MsgLen > defaultReceivedLength ? 
				defaultReceivedLength : messages.length;
		
		// ����
		int idx = 0;
		for (int i = messages.length - 1; i >= MsgLen - defaultReceivedLength; i--) {
			EmailLogger.info("���ڽ�����" + idx + "���ʼ�");
			// get original email message
			MimeMessage msg = (MimeMessage) messages[i];

			messageList[idx] = parseOneMessage(msg, isGetAttachment);
			idx++;
		}
		
		return messageList;
	}
	/**
	 * ����һ��Message
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static EmailMessage parseOneMessage(Message msg0, boolean isGetAttachment)
			throws MessagingException, IOException {
		// 
		EmailLogger.info("start to parse one email message");
		if (msg0 == null) {
			JOptionPane.showMessageDialog(null, "δ�ҵ�Ҫ�������ʼ�!");
			EmailLogger.info("δ�ҵ�Ҫ�������ʼ�!");
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
	 * ����ʼ�����
	 */
	public static String getSubject(MimeMessage msg)
			throws UnsupportedEncodingException, MessagingException {
		return CheckEmail.decodeEmailStr(msg.getSubject());
	}
	/**
	 * ����ʼ�������
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static String getFrom(MimeMessage msg) throws MessagingException,
			UnsupportedEncodingException {
		String from = "";
		Address[] froms = msg.getFrom();
		if (froms.length < 1) {
			EmailLogger.warning("û�з�����!");
			throw new MessagingException("û�з�����!");
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
	 * �����ռ������ͣ���ȡ�ʼ��ռ��ˡ����ͺ����͵�ַ��
	 * ����ռ�������Ϊ�գ��������е��ռ���
	 * 		Message.RecipientType.TO �ռ���
	 * 		Message.RecipientType.CC ����
	 * 		Message.RecipientType.BCC ����
	 * @return �ռ���<�ʼ���ַ1>;�ռ���;<�ʼ���ַ2>;...
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
				EmailLogger.warning(type + ": û���ռ���!");
				throw new MessagingException("û���ռ���!");
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
	 * �ж��ʼ����Ƿ��������
	 * ������Ҫô�� Part.ATTACHMENT, Ҫô�� Part.INLINE ʱ
	 * @return �ʼ��д��ڸ�������true�������ڷ���false
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
	 * �ж��ʼ��Ƿ��Ѷ�
	 * @throws MessagingException
	 */
	public static boolean isSeen(MimeMessage msg) throws MessagingException {
		return msg.getFlags().contains(Flags.Flag.SEEN);
	}
	/**
	 * �ж��ʼ��Ƿ���Ҫ�Ķ���ִ
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
	 * ����ʼ������ȼ�
	 * @return 1(High):���� 3:��ͨ(Normal) 5:��(Low)
	 * @throws MessagingException
	 */
	public static String getPriority(MimeMessage msg) throws MessagingException {
		String priority = "��ͨ";
		String[] headers = msg.getHeader("X-Priority");
		if (headers != null) {
			String headerPriority = headers[0];
			if (headerPriority.indexOf("1") != -1
					|| headerPriority.indexOf("High") != -1)
				priority = "����";
			else if (headerPriority.indexOf("5") != -1
					|| headerPriority.indexOf("Low") != -1)
				priority = "��";
			else if(headerPriority.indexOf("3") != -1
					|| headerPriority.indexOf("Middle") != -1)
				priority = "��ͨ";
		}
		
		return priority;
	}
	/**
	 * ����ʼ��ı�����
	 * @param part
	 * 		�ʼ���
	 * @param content
	 *      �洢�ʼ��ı����ݵ��ַ���
	 * @throws MessagingException
	 * @throws IOException
	 * @problem:
	 * 		�ı�Ϊhtml�ȸ�ʽ, ���ܵ�����ΪString���洢
	 */
	public static String getMailTextContent(Part part, StringBuffer content)
			throws MessagingException, IOException {
		// ������ı����͵ĸ�����ͨ��getContent��������ȡ���ı�����
		// ���ⲻ��������Ҫ�Ľ��������������Ҫ���ж�
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
	 * ���渽��, ��singleFileName��Ϊ������Чʱ,������singleFileName
	 * 		��������ȫ������
	 * @param part
	 * 		�ʼ��ж��������е�����һ�������
	 * @param path
	 * 		��������Ŀ¼
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @problem: ��ȡ����inputstreamʧЧ, ��Ϊ�ᵼ����ΪUnknown Source
	 * 	�����Ϊ, �����Ĳ���, Ҳ����ͨ��EmailMessage��inputstream����ȡ
	 * 	����, ���ڸ�Message��Ӧ������(��store.connect())�Ѿ��ر���, 
	 * 	����inputstreamʧЧ
	 * 	����, ���ܴ��ڸ������ڵ�����(��Ϊ����ά�������������޵�)
	 */
	public static void getAttachments(Part part, String path, 
			String singleFileName, String strInfo) 
			throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		//
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // �������ʼ�
			// �������ʼ���������ʼ���
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// ��ø������ʼ�������һ���ʼ���
				BodyPart bodyPart = multipart.getBodyPart(i);
				// ĳһ���ʼ���Ҳ�п������ɶ���ʼ�����ɵĸ�����
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
								JOptionPane.showMessageDialog(null, "����" 
										+ singleFileName + "�����ɹ�");
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
									JOptionPane.showMessageDialog(null, "����" 
											+ singleFileName + "�����ɹ�");
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
	 * ������ȡ�ļ���, ���ṩ����
	 */
	public static void getAttachments(Part part, ArrayList<String> fileNames) 
			throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		//
		EmailLogger.info("getAttachments - ������ȡ������");
		
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // �������ʼ�
			// �������ʼ���������ʼ���
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// ��ø������ʼ�������һ���ʼ���
				BodyPart bodyPart = multipart.getBodyPart(i);
				// ĳһ���ʼ���Ҳ�п������ɶ���ʼ�����ɵĸ�����
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
	 * ��ȡ�������е����ݱ�����ָ��Ŀ¼
	 * @param in ������
	 * @param fileName �ļ���
	 * @param destDir �ļ��洢Ŀ¼
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @problems: 
	 * 		���ܸ����Ѿ�������
	 */
	public static boolean saveAttachments(InputStream in, 
			String saveDir, String fileName)
			throws FileNotFoundException, IOException {
		boolean flag = false;
		if(in == null || fileName == null || fileName.length() < 1) {
			JOptionPane.showMessageDialog(null, "���� " + fileName + " ����ʧ��");
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