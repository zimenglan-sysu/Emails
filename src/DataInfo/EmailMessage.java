package DataInfo;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import UI.Login.LoginUI;
import Util.Email.Logging.EmailLogger;

import DataInfo.EnumType;

/**
 * Data of Email Info
 * can be shared by sending or receiving
 * by using emialType member to distinguish
 */
/**
 * Represents a Mail message object which contains all the massages needed
 * by an email.
 */
public class EmailMessage {
	// distinguish send or reveive
	public static final String SEND_TYPE_STRING = "SEND";
	public static final String RECEIVE_TYPE_STRING = "RECEIVE";
	// value: SEND, RECEIVE
	private String emailType;
	// 发送者邮件地址
	private String fromPersonalName;
	private String from;
	// 发送者邮件地址
	private String fromStr;
	// 接收者邮件地址
	private String toStr;
	private String[] to;
	// 抄送者邮件地址
	private String ccsStr;
	private String[] ccs;
	// 密件抄送者邮件地址
	private String bccsStr;
	private String[] bccs;
	// 接收者邮件地址
	private String singleTo;
	// 抄送者邮件地址
	private String singleCcs;
	// 密件抄送者邮件地址
	private String singleBccs;
	// 邮件主题
	private String subject;
	// 邮件正文类型
	private String contentType;
	// 邮件正文
	private String content;
	// 发送日期
	private Date sendDate;
	// 邮件服务器地址
	private String[] host;
	// 邮件服务器类型
	private String[] hostType;
	// 邮件服务器地址
	private String singleHost;
	// 邮件服务器类型
	private String singleHostType;
	// 是否有附件
	private boolean hasAttachMent;
	// 附件名称
	private String[] fileNames;
	// Priority
	private String priority;
	// 合法的收件人的个数(暂时没有考虑ccs和bccs)
	private int validCount;
	private boolean isSingle = false; 
	
	/**
	 * Receive Parts
	 */
	private static int totalReceivedEmailCount;
	private static int deletedReceivedEmailCount;
	private static int newReceivedEmailCount;
	private static int readReceivedEmailCount;
	private boolean receivedReplySign;
	private boolean receivedHasSeen;
	private int receivedIdx;
	private int receivedFileSize;
	private String preReceivedViewContent;
	private Date receivedDate;
	
	private int forwardFlag;
	
	// ******************************************************************
	
	public EmailMessage() {
		emailType = null;
		fromStr = null;
		from = null;
		toStr = null;
		to = null;
		ccsStr = null;
		ccs = null;
		bccsStr = null;
		bccs = null;
		singleTo = null;
		singleCcs = null;
		singleBccs = null;
		subject = null;
		content = null;
		contentType = null;
		sendDate = null;
		host = null;
		hostType = null;
		singleHost = null;
		singleHostType = null;
		hasAttachMent = false;
		fileNames = null;
		priority = null;
		validCount = 0;
		isSingle = false;
		
		//
		totalReceivedEmailCount = 0;
		deletedReceivedEmailCount = 0;
		newReceivedEmailCount = 0;
		readReceivedEmailCount = 0;
		receivedReplySign = false;
		receivedHasSeen = false;
		receivedIdx = 0;
		receivedFileSize = 0;
		preReceivedViewContent = null;
		receivedDate = null;
		// 0: reply, 1: reply all, 2: forword
		forwardFlag = 0;
	}
	
    public static EmailMessage getSingleEmailMessage(String emailType,
    		String subject, String from, 
    		String to, String content,
    		Date sendDate, String[] fileNames, String priority) {
    	// instance
    	EmailMessage em = new EmailMessage();
    	
    	em.validCount = 0;
    	em.emailType = emailType;
    	em.subject = subject;
    	em.from = from;
        if (!to.matches(EnumType.REGEX_EMAIL_ADDRESS)) { 
			throw new IllegalArgumentException("Wrong to email address : "
					+ to);
		}
        em.singleTo = to;
        String to2 = to.substring(to.indexOf("@") + 1);
        em.singleHost = "smtp." + to2;
        em.singleHostType = to2.substring(0, to2.indexOf("."));
        em.content = content;
        em.sendDate = sendDate;
        if(fileNames == null || fileNames.length < 1) {
			em.hasAttachMent = false;
			em.fileNames = null;
		} else {
			em.hasAttachMent = true;
			em.fileNames = fileNames;
		}
        
        em.validCount++;
        em.isSingle = true;
        em.priority = priority;
        
        return em;
    }
    
    public EmailMessage(String emailType, String subject, 
    		String from, String to, String content,
    		Date sendDate, String[] fileNames, String priority) {
    	//
    	this.validCount = 0;
    	this.emailType = emailType;
        this.subject = subject;
        this.from = from;
        if (!to.matches(EnumType.REGEX_EMAIL_ADDRESS)) { 
			throw new IllegalArgumentException("Wrong to email address : "
					+ to);
		}
        this.to = new String[]{to};
        String to2 = to.substring(to.indexOf("@") + 1);
		this.host = new String[]{"smtp." + to2};
		this.hostType = new String[]{to2.substring(0, to2.lastIndexOf("."))};
        this.content = content;
        this.sendDate = sendDate;
        if(fileNames == null || fileNames.length < 1) {
			this.hasAttachMent = false;
			this.fileNames = null;
		} else {
			this.hasAttachMent = true;
			this.fileNames = fileNames;
		}
        
        this.priority = priority;
        this.validCount++;
        this.isSingle = true;
    }
    
	public EmailMessage(String emailType, String to[], 
			String from, String[] ccs, String[] bccs, 
			String subject, String content, Date sendDate, String[] host, 
			String[] hostType, String[] fileNames, String priority) {
		//
		this.emailType = emailType;
		this.to = to;
		this.from = from;
		this.ccs = ccs;
		this.bccs = bccs;
		this.subject = subject;
		this.content = content;
		this.sendDate = sendDate;
		this.host = host;
		this.hostType = hostType;
		if(fileNames == null || fileNames.length < 1) {
			this.hasAttachMent = false;
			this.fileNames = null;
		} else {
			this.hasAttachMent = true;
			this.fileNames = fileNames;
		}
		
		this.priority = priority;
		//
		this.validCount = 0;
		if(to != null) {
			this.validCount += to.length;
		}
		if(ccs != null) {
			this.validCount += ccs.length;
		}
		if(bccs != null) {
			this.validCount += bccs.length;
		}
		this.isSingle = false;
	}
	
	public EmailMessage(String emailType, String to[], 
			String from, String[] ccs, String[] bccs, 
			String subject, String content, 
			Date sendDate, String[] fileNames, String priority) {
		//
		this.emailType = emailType;
		this.to = to;
		this.from = from;
		this.ccs = ccs;
		this.bccs = bccs;
		this.subject = subject;
		this.content = content;
		this.sendDate = sendDate;
		
		// validCount
		this.validCount = 0;
		if(to != null) {
			this.validCount += to.length;
		}
		if(ccs != null) {
			this.validCount += ccs.length;
		}
		if(bccs != null) {
			this.validCount += bccs.length;
		}
		
		// host & hostType
		this.host = new String[this.validCount];
		this.hostType = new String[this.validCount];
		int idx = 0;
		for(String to2: to) {
			to2 = to2.substring(to2.indexOf("@") + 1);
			this.host[idx] = "smtp." + to2;
			this.hostType[idx] =to2.substring(0, to2.lastIndexOf("."));
			idx++;
		}
		if(ccs != null) {
			for(String to2: ccs) {
				to2 = to2.substring(to2.indexOf("@") + 1);
				this.host[idx] = "smtp." + to2;
				this.hostType[idx] =to2.substring(0, to2.lastIndexOf("."));
				idx++;
			}
		}
		if(bccs != null) {
			for(String to2: bccs) {
				to2 = to2.substring(to2.indexOf("@") + 1);
				this.host[idx] = "smtp." + to2;
				this.hostType[idx] =to2.substring(0, to2.lastIndexOf("."));
				idx++;
			}
		}
		// fileNames
		if(fileNames == null || fileNames.length < 1) {
			this.hasAttachMent = false;
			this.fileNames = null;
		} else {
			this.hasAttachMent = true;
			this.fileNames = fileNames;
		}
		// priority
		this.priority = priority;
		// single
		this.isSingle = false;
	}
	
	// *******************************************************************
	
	// EmailType
	public String getEmailType() {
		return this.emailType;
	}
	
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	// Single To
 	public String getSingleTo() {
		return this.singleTo;
	}
	
	public void setSingleTo(String singleTo) {
		this.singleTo = singleTo;
	}
	// ToStr
	public String getToStr() {
		return this.toStr;
	}
	
	public void setToStr(String toStr) {
		this.toStr = toStr;
	}
	// To
	public String[] getTo() {
		return this.to;
	}
	
	public void setTo(String[] to) {
		int idx = 0;
		this.to = new String[to.length];
		this.host = new String[to.length];
		this.hostType = new String[to.length];
		
		// 判断邮件地址是否合法
		for(String t : to) {
			if (!t.matches(EnumType.REGEX_EMAIL_ADDRESS)) { 
				throw new IllegalArgumentException("Wrong to email address : "
						+ t);
			}
			this.to[idx] = t;
			String t2 = t.substring(t.indexOf("@") + 1);
			this.host[idx] = "smtp." + t2;
			this.hostType[idx] = t2.substring(0, t2.lastIndexOf("."));
			
			idx++;
		}
		this.validCount = idx;
	}
	// fromPersonalName
	public String getrFomPersonalName() {
		return fromPersonalName;
	}

	public void setFromPersonalName(String fromPersonalName) {
		
		this.fromPersonalName = fromPersonalName;

	}
	// fromStr
	public String getFromStr() {
		return fromStr;
	}

	public void setFromStr(String fromStr) {
		
		this.fromStr = fromStr;

	}
	// From
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		// 判断邮件地址是否合法
		if (!from.matches(EnumType.REGEX_EMAIL_ADDRESS)) {
			throw new IllegalArgumentException(
					"Wrong Sender Email Address : " + from);
		}
		this.from = from;
	}
	// Single Ccs
	public String getSingleCcs() {
		return this.singleCcs;
	}

	public void setSingleCcs(String singleCcs) {
		this.singleCcs = singleCcs;
	}
	// CcsStr
	public String getCcsStr() {
		return this.ccsStr;
	}
	
	public void setCcsStr(String ccsStr) {
		this.ccsStr = ccsStr;
	}
	// Ccs
	public String[] getCcs() {
		return this.ccs;
	}
	
	public void setCcs(String[] ccs) {
		this.ccs = ccs;
	}
	// Single Bccs
	public String getSingleBccs() {
		return this.singleBccs;
	}
	
	public void setSingleBccs(String singleBccs) {
		this.singleBccs = singleBccs;
	}
	// CcsStr
	public String getBcsStr() {
		return this.bccsStr;
	}
	
	public void setBccsStr(String bccsStr) {
		this.bccsStr = bccsStr;
	}
	// Bccs
	public String[] getBccs() {
		return this.bccs;
	}
	
	public void setBccs(String[] bccs) {
		this.bccs = bccs;
	}
	// Subject
	public String getSubject() {
		return this.subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	// Content
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	// ContentType
	public String getContentType() {
		return this.contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	// Date
	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	// SingleHost
	public String getSingleHost() {
		return this.singleHost;
	}
	
	public void setSingleHost(String singleHost) {
		this.singleHost = singleHost;
	}
	// Host
	public String[] getHost() {
		return this.host;
	}

	public void setHost(String[] host) {
		this.host = host;
	}
	// SingleHostType
	public String getSingleHostType() {
		return this.singleHostType;
	}
	
	public void setSingleHostType(String singleHostType) {
		this.singleHostType = singleHostType;
	}
	// HostType
	public String[] getHostType() {
		return this.hostType;
	}

	public void setHostType(String[] hostType) {
		this.hostType = hostType;
	}
	// HasAttachment
	public boolean getHasAttachMent() {
		return this.hasAttachMent;
	}
	
	public void setHasAttachMent(boolean hasAttachMent) {
		this.hasAttachMent = hasAttachMent;
	}
	// FileNames
	public String[] getFileNames() {
        return fileNames;
    }
	
    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }
    // Priority
    public String getPriority() {
		return this.priority;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
    // ValidCount
	public int getvalidCount() {
		return this.validCount;
	}
	
	public void setvalidCount(int validCount) {
		this.validCount = validCount;
	}
	// IsSingle
	public boolean getIsSingle() {
		return this.isSingle;
	}
	
	public void setvalidCount(boolean isSingle) {
		this.isSingle = isSingle;
	}
	
	// *******************************************************
	
	/**
	 * sets & gets
	 */
	public static int getTotalReceivedEmailCount() {
		return totalReceivedEmailCount;
	}
	
	public static void setTotalReceivedEmailCount(int c) {
		totalReceivedEmailCount = c;
	}
	// deteted
	public static int getDeletedReceivedEmailCount() {
		return deletedReceivedEmailCount;
	}
	
	public static void setDeletedReceivedEmailCount(int c) {
		deletedReceivedEmailCount = c;
	}
	// new
	public static int getNewReceivedEmailCount() {
		return newReceivedEmailCount;
	}
	
	public static void setNewReceivedEmailCount(int c) {
		newReceivedEmailCount = c;
	}
	// read
	public static int getReadReceivedEmailCount() {
		return readReceivedEmailCount;
	}
	
	public static void setReadReceivedEmailCount(int c) {
		readReceivedEmailCount = c;
	}
	// receivedReplySign
	public boolean getReceivedReplySign() {
		return receivedReplySign;
	}
	
	public void setReceivedReplySign(boolean r) {
		receivedReplySign = r;
	}
	// receivedIdx
	public int getReceivedIdx() {
		return receivedIdx;
	}
	
	public void setReceivedIdx(int ri) {
		receivedIdx = ri;
	}
	// receivedFileSize
	public int getReceivedFileSize() {
		return receivedFileSize;
	}
	
	public void setReceivedFileSize(int rfs) {
		receivedFileSize = rfs;
	}
	// preReceivedViewContent
	public String getPreReceivedViewContent() {
		return preReceivedViewContent;
	}
	
	public void setPreReceivedViewContent(String prvc) {
		preReceivedViewContent = prvc;
	}
	// receivedDate
	public Date getReceivedDate() {
		return receivedDate;
	}
	
	public void setReceivedDate(Date rd) {
		receivedDate = rd;
	}
	// receivedHasSeen
	public boolean getReceivedHasSeen() {
		return receivedHasSeen;
	}
	
	public void setReceivedHasSeen(boolean rhs) {
		receivedHasSeen = rhs;
	}
	
	// forwardFlag
	public void setForwardFlag(int forwardFlag) {
		this.forwardFlag = forwardFlag;
	}
	
	public int getForwardFlag() {
		return this.forwardFlag;
	}
	
	// *******************************************************
	
	public void printInfo() {
		// System.out.println();
		// System.out.println();
		
		// emailType
		System.out.println("email type: " + this.emailType);
		// from
		System.out.println("from: " + this.from);
		
		if(this.isSingle) {
			// singleTo
			System.out.println("singleTo: " + this.singleTo);
			// subject
			System.out.println("subject: " + this.subject);
			// singleCcs
			if(this.singleCcs != null && !this.singleCcs.equals("")){
					System.out.println("singleCcs: " + this.singleCcs);
			}
			// singleBccs
			if(this.singleBccs != null && !this.singleBccs.equals("")){
				System.out.println("singleBccs: " + this.singleBccs);
			}
			// sendDate
			System.out.println("sendDate: " + this.sendDate);
			// singleHost
			System.out.println("singleHost: " + this.singleHost);
			// singleHostType
			System.out.println("singleHostType: " + this.singleHostType);
		} else {
			// to
			if(this.to != null) {
				for(String t : this.to) {
					System.out.println("to: " + t);
				}
			}
			// subjcet
			System.out.println("subject: " + this.subject);
			// ccs
			if(this.ccs != null) {
				for(String c : this.ccs) {
					if(c != null && !c.equals("")){
						System.out.println("ccs: " + c);
					}	
				}
			}
			// bccs
			if(this.bccs != null) {
				for(String bc : this.bccs){
					if(bc != null && !bc.equals("")){
						System.out.println("bccs: " + bc);
					}
				}
			}
			// send date
			System.out.println("sendDate: " + this.sendDate);
			// host
			if(this.host != null) {
				for(String sh : this.host){
					System.out.println("host: " + sh);
				}
			}
			// hostType
			if(this.hostType != null) {
				for(String sht : this.hostType) {
					System.out.println("hostType: " + sht);
				}
			}
		}
		// priority
		System.out.println("priority: " + priority);
		// content
		System.out.println();
		System.out.println("content: ");
		System.out.println("  " + this.content);
		System.out.println();
		// attachment
		if(this.hasAttachMent) {
			for(String fn : this.fileNames) {
				System.out.println(fn);
				System.out.println();
			}
		}
	}
	
	/**
	  * Test
	  * @param none
	  */
	 public static void main(String[] args) {
		 // do nothing
	 }
}