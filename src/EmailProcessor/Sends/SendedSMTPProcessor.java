package EmailProcessor.Sends;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.swing.JOptionPane;

import com.sun.mail.handlers.message_rfc822;
import com.sun.mail.smtp.SMTPSenderFailedException;
import com.sun.mail.util.MailLogger;

import DataInfo.EmailMessage;
import DataInfo.EmailMessage;
import DataInfo.MailAuthenticator;
import Util.Email.Logging.EmailLogger;
import Util.Email.CheckEmail;
import Util.Email.EmailDataManager;


/**
 * use smtp protocol to send e-mail
 * @author ddk
 * @see 14.12.11
 */
public class SendedSMTPProcessor {
	/**
	 * variables
	 */
	// 邮件发送协议
	private final static String DefaultProtocol = "smtp";
	// SMTP邮件服务器
	private final static String DefaultHost = "smtp.126.com";
	// SMTP邮件服务器默认端口
	private final static String Port = "25";
	
	// 是否要求身份认证
	private final static String Is_Auth = "true";
	private final static String Is_Anonymous_Auth = "true";
	
	// 是否启用调试模式
	// 启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息
	private final static String Is_Enable_Debug_Mod = "true";
	private final static String Is_Anonymous_Enable_Debug_Mod = "false";
	private final static String Anonymous_From_Address = "reply-to@126.com";
	
	// message
	EmailMessage message = null;
	// 发件人
	private static String defaultFrom = 
			EmailDataManager.getDefaultEmailAddr();
	private static String defaultTo = 
			EmailDataManager.getDefaultEmailToAddr();
	
	// 初始化连接邮件服务器的会话信息
	private static Properties defaultProps = null;
	private static Properties props = null;
	private static Properties anonymousProps = null;
	//
	private static Authenticator defaultAuthenticator = null;
	private static Authenticator authenticator = null;
	
	 // session
	private static Session session = null;
	private static Session anonymousSession = null;
	
	// default variables
	static {
		defaultProps = new Properties();
		defaultProps.setProperty("mail.transport.protocol", 
				DefaultProtocol);
		defaultProps.setProperty("mail.smtp.host", DefaultHost);
		// maybe wrong
		defaultProps.setProperty("mail.smtp.port", Port);
		defaultProps.setProperty("mail.smtp.auth", Is_Auth);
		defaultProps.setProperty("mail.debug",Is_Enable_Debug_Mod);
		
		defaultAuthenticator = new MailAuthenticator();
	}
	
	/**
	 * 
	 */
	private static SendedSMTPProcessor sender = null;
	
	/**
	 * Constructor
	 */
	private SendedSMTPProcessor() {
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
	 * Anonymous Session
	 */
	public static Session getAnonymousSession(String smtpHost) {
		if(anonymousSession == null) {
			setAnonymousSession(smtpHost);
		}
		
		return anonymousSession;
	}
	
	public static void setAnonymousSession(Session s) {
		anonymousSession = s;
	}
	
	public static void setAnonymousSession(String smtpHost) {
		if(anonymousSession == null) {
			// anonymous
			anonymousSession = Session.getInstance(
					getAnonymousProperty(smtpHost), null);
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
			props.setProperty("mail.transport.protocol", 
					DefaultProtocol);
			//
			props.setProperty("mail.smtp.host", 
					(String)EmailDataManager.getData(
					EmailDataManager.getSmtpEmailHost()));
			
			props.setProperty("mail.smtp.port", Port);
			props.setProperty("mail.smtp.auth", Is_Auth);
			props.setProperty("mail.debug",Is_Enable_Debug_Mod);
		}
	}
	
	/**
	 * Anonymous Property
	 */
	public static Properties getAnonymousProperty(String smtpHost) {
		if(anonymousProps == null) {
			setAnonymousProperty(smtpHost);
		}
		
		return anonymousProps;
	}
	
	public static void setAnonymousProperty(Properties p) {
		anonymousProps = p;
	}
	
	public static void setAnonymousProperty(String smtpHost) {
		if(anonymousProps == null) {
			anonymousProps = new Properties();
			anonymousProps.setProperty("mail.transport.protocol", 
					DefaultProtocol);
			// smtp host is the received host
			anonymousProps.setProperty("mail.smtp.host", smtpHost);
			
			anonymousProps.setProperty("mail.smtp.port", Port);
			anonymousProps.setProperty("mail.smtp.auth", Is_Anonymous_Auth);
			anonymousProps.setProperty("mail.debug",Is_Anonymous_Enable_Debug_Mod);
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
			
			// MailAuthenticator does not work
			// authenticator = new MailAuthenticator(username, password);
			
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
	 * EmailMessage
	 */
	public EmailMessage getEEmailMessage() {
		return message;
	}
	
	public void setEmailMessage(EmailMessage message) {
		this.message = message;
	}
	
	public void setEmailMessage(String emailType, String to[], 
			String from, String[] ccs, String[] bccs, 
			String subject, String content, 
			Date sendDate, String[] host, String[] hostType,
			String[] filesName, String priority) {
		this.message = new EmailMessage(emailType, to, from, ccs, 
				bccs, subject, content, sendDate, host, hostType,
				filesName, priority);
	}
	
	/**
	 * Instance
	 */
	public static SendedSMTPProcessor getSendedSMTPProcessorInstance() {
		if(sender == null){
			sender = new SendedSMTPProcessor();
		}
		
		return sender;
	}
	
	/**
     * Send anonymous email. 
     */
    public static boolean sendAnonymousEmail(EmailMessage message) {
        String dns = "dns://";
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
        		"com.sun.jndi.dns.DnsContextFactory");
        env.put(Context.PROVIDER_URL, dns);
        String[] tos = message.getTo();
        //
        boolean isSend = false;
        
        try {
            DirContext ctx = new InitialDirContext(env);
            for(String to: tos) {
                String domain = to.substring(to.indexOf('@') + 1);
                //Get MX(Mail eXchange) records from DNS
                Attributes attrs = ctx.getAttributes(domain, new String[] { "MX" });
                //
                if (attrs == null || attrs.size() <= 0) {
                    throw new java.lang.IllegalStateException(
                        "Error: Your DNS server has no Mail eXchange records!");
                }
                
                @SuppressWarnings("rawtypes")
                NamingEnumeration servers = attrs.getAll();
                String smtpHost = null;
                //
                StringBuffer buf = new StringBuffer();
                
                //try all the mail exchange server to send the email.
                while (servers.hasMore()) {
                    Attribute hosts = (Attribute) servers.next();
                    for (int i = 0; i < hosts.size(); ++i) {
                        // sample: 20 mx2.qq.com
                        smtpHost = (String) hosts.get(i);
                        // parse the string to get smtpHost. sample: mx2.qq.com
                        smtpHost = smtpHost.substring(smtpHost.lastIndexOf(' ') + 1);
                        // Sending email
                        try {
                        	EmailLogger.info("smtpHost - anonymous email: " + smtpHost);
                            if(sendEmail(smtpHost, message, true)) {
                            	EmailLogger.info("send anonymous email successfully");
                            	isSend = true;
                                return isSend;
                            }
                        } catch (Exception e) {
                            // LOGGER.error("", e);
                        	System.out.println(e);
                        	
                            buf.append(e.toString()).append("\r\n");
                            continue;
                        }
                    }
                }
                // failed
                if (!isSend) {
                	System.out.println("Error: Send email error." + buf.toString());
                }
            }
        } catch (NamingException e) {
            //LOGGER.error("", e);
        	System.out.println(e);
        }
        
        return isSend;
    } 
     
    /**
     * send normal email
     */
    public static boolean sendNormalEmail(EmailMessage message) {
    	// send email
    	String smtpHost = null;
    	boolean isAnonymousEmail = false; 
    	boolean flag = false;
		try {
			flag = sendEmail(smtpHost, message, isAnonymousEmail);
		} catch (SMTPSenderFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(flag) {
    		EmailLogger.info("send normal email successfully");
    	}
    	return flag;
    }
    
    /**
     * send timed email
     */
    public static void sendTimingEmail(final EmailMessage message, long duration) {
    	// 
    	long MillSeconds = 60 * 1000;
    	// get timer
    	Timer timer = new Timer();  
    	// schedule
        timer.schedule(new TimerTask() {
        	@Override
        	public void run() {  
        		String smtpHost = null;
            	boolean isAnonymousEmail = false; 
            	boolean flag = false;
				try {
					flag = sendEmail(smtpHost, message, isAnonymousEmail);
				} catch (SMTPSenderFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	if(flag) {
            		EmailLogger.info("send timed email successfully");
					JOptionPane.showMessageDialog(null, "定时邮件发送成功");
				} else {
					EmailLogger.info("send timed email failedly");
					JOptionPane.showMessageDialog(null, "定时邮件发送失败");
				}
            } 
        }, duration * MillSeconds);
    }
    
    /**
     * Send Email. 
     * @throws SMTPSenderFailedException 
     */
    private static boolean sendEmail(String smtpHost, 
    		EmailMessage message, boolean isAnonymousEmail) 
    				throws SMTPSenderFailedException {
    	//
    	boolean flag = false;
        if(message == null) {
            throw new IllegalArgumentException("Param mail can not be null.");
        }
        //
        String[] fileNames = message.getFileNames();
        
        // only needs to check the param: fileNames,
        // other params would be checked through
        // the override method.
        File[] files = null;
        if(fileNames != null && fileNames.length > 0) {
            files = new File[fileNames.length];
            for(int i = 0; i < files.length; i++) {
                File file = new File(fileNames[i]);
                files[i] = file;
            }
        }
        
        String from = null;
        if(isAnonymousEmail) {
        	from = Anonymous_From_Address;
        } else {
        	 from = message.getFrom();
        }
        // send email
        flag = sendEmail(smtpHost, message.getSubject(), 
        		from, message.getTo(), message.getCcs(), 
        		message.getBccs(), message.getContent(), 
        		files, message.getPriority(), isAnonymousEmail
        );
        
        return flag;
    }
     
    
    /**
     * Send Email. Note that content and attachments cannot be empty at the same time.
     */
    private static boolean sendEmail(String smtpHost, String subject, 
            String from, String[] tos, String[] ccs, String[] bccs, 
            String content, File[] attachments, 
            String priority, boolean isAnonymousEmail) throws SMTPSenderFailedException{
    	//
    	boolean flag = false;
        //parameter check
        if(isAnonymousEmail && smtpHost == null) {
            throw new IllegalStateException(
                "When sending anonymous email, param smtpHost cannot be null");
        }
        // subject
        if(subject == null || subject.length() == 0) {
        	subject = "Auto-generated subject";
        }
        // from
        if(from == null) {
            throw new IllegalArgumentException("Sender's address is required.");
        }
        // to
        if(tos == null || tos.length == 0) {
            throw new IllegalArgumentException(
                "At lease 1 receive address is required.");
        }
        // content & attachments
        if(content == null && (attachments == null || attachments.length == 0)) {
            throw new IllegalArgumentException(
                "Content and attachments cannot be empty at the same time");
        }
        // attachment
        if(attachments != null && attachments.length > 0) {
            List<File> invalidAttachments = new ArrayList<>();
            for(File attachment:attachments) {
                if(!attachment.exists() || attachment.isDirectory() 
                    || !attachment.canRead()) {
                    invalidAttachments.add(attachment);
                }
            }
            // 
            if(invalidAttachments.size() > 0) {
                String msg = "";
                for(File attachment:invalidAttachments) {
                    msg += "\n\t" + attachment.getAbsolutePath();
                }
                throw new IllegalArgumentException(
                    "The following attachments are invalid:" + msg);
            }
        }
        /**
         * Session
         */
        Session tempSession = null;
        if(isAnonymousEmail) {
        	tempSession = getAnonymousSession(smtpHost);
        } else {
        	EmailLogger.info("normal sending email, need password and username");
        	tempSession = getSession();
        }
        /**
         * Message
         */
        MimeMessage msg = new MimeMessage(tempSession);
        try {
            // Multipart is used to store many BodyPart objects.
            Multipart multipart = new MimeMultipart();
            
            // add content
            BodyPart part = new MimeBodyPart();
            part.setContent(content,"text/html;charset=gb2312");
            multipart.addBodyPart(part);
             
            // add attachment
            if(attachments != null && attachments.length > 0) {
                for(File attachment: attachments) {
                	// get handler
                    String fileName = attachment.getName();
                    DataSource dataSource = new FileDataSource(attachment);
                    DataHandler dataHandler = new DataHandler(dataSource);
                    
                    part = new MimeBodyPart();
                    part.setDataHandler(dataHandler);
                    
                    //solve encoding problem of attachments file name.
                    try {
                        fileName = CheckEmail.encodeEmailStr(fileName);
                    } catch (UnsupportedEncodingException e) {
                    	String msg1 = "Cannot convert the encoding of attachments file name: " +  e;
                    	EmailLogger.info(msg1);
                    }
                    
                    // set attachments the original file name. if not set, 
                    // an auto-generated name would be used.
                    part.setFileName(fileName);
                    multipart.addBodyPart(part);
                }
            }
            
            // set subject
            msg.setSubject(subject);
            // set sended date
            msg.setSentDate(new Date());
            // set sender
            msg.setFrom(new InternetAddress(from));
            // 设置优先级(1:紧急	3:普通	5:低)
         	msg.setHeader("X-Priority", "1");
         	// 要求阅读回执(收件人阅读邮件时会提示回复发件人,
         	// 表明邮件已收到,并已阅读)
         	msg.setHeader("Disposition-Notification-To", from);
         	
            //set receiver, 
            for(String to: tos) {
                msg.addRecipient(RecipientType.TO, new InternetAddress(to));
            }
            // set ccs
            if(ccs != null && ccs.length > 0) {
                for(String cc: ccs) {
                    msg.addRecipient(RecipientType.CC, new InternetAddress(cc));
                }
            }
            // set bccs
            if(bccs != null && bccs.length > 0) {
                for(String bcc: bccs) {
                    msg.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
                }
            }
            // add the whole email
            msg.setContent(multipart);
            //save the changes of email first.
            msg.saveChanges();
            /**
             * Transport
             */
            // log
            EmailLogger.info("now transport email in SendedSMTPProcessor class");
            if(isAnonymousEmail) {
            	// send
            	EmailLogger.info("now transport email use 'Transport.send(msg)' function");
                Transport.send(msg); 
            } else {
            	Transport transport = tempSession.getTransport(DefaultProtocol);
            	// 
            	transport.connect();
                if(!transport.isConnected()) {
                	// return
                    flag = false;
                    return flag;
                }
                // send
                transport.send(msg); 
                // close
                transport.close();
            }
            // log
            EmailLogger.info("Send html email success.");
        } catch (NoSuchProviderException e) {
            // LOGGER.error("Email provider config error.", e);
        	System.out.println("Email provider configuration error: " + e);
        } catch (MessagingException e) {
            // LOGGER.error("Send email error.", e);
        	System.out.println("Sending emial error: " + e);
        }
        
        flag = true;
        return flag;
    }
	
    
    // *****************************************************************
    
	/**
	 * Test
	 */
	public static void main(String[] args) throws Exception {
		// do nothing now
	}	
}