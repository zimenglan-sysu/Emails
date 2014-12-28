package Util.Email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;  
import java.util.logging.SocketHandler; 
import Util.Email.PathManager;
import Util.Email.Logging.EmailLogger;

public class LoadConfigProps {
	// common
	private static String ConfigFileName = "config.properties";
	private static String SendHostType;
	private static String ReceiveHostType;
	// smtp
	private static String SMTPServer;
	private static String SMTPUserName;
	private static String SMTPPassWord;
	// pop3
	private static String POP3Server;
	private static String POP3UserName;
	private static String POP3PassWord;
	
	private static LoadConfigProps lcfp;
	
	private LoadConfigProps() {
		// lcfp = null;
	}
	
	public static LoadConfigProps getLoadConfigPropsInstance() {
		if(lcfp == null) {
			lcfp = new LoadConfigProps();
		}
		
		return lcfp;
	}
	
	/**
	 * common
	 */
	public static String getConfigFileName() {
		return ConfigFileName;
	}
	
	public static String getSendHostType() {
		return SendHostType;
	}
	
	public String getReceiveHostType() {
		return ReceiveHostType;
	}
	
	/**
	 * SMTP
	 */
	public static String getSMTPServer() {
		return SMTPServer;
	}
	
	public static String getSMTPUserName() {
		return SMTPUserName;
	}
	
	public static String getSMTPPassWord() {
		return SMTPPassWord;
	}
	
	/**
	 * POP3
	 */
	public static String getPOP3Server() {
		return POP3Server;
	}
	
	public static String getPOP3UserName() {
		return POP3UserName;
	}
	
	public static String getPOP3PassWord() {
		return POP3PassWord;
	}
	
	/**
	 * Load configuration properties to initialize attributes.
	 */
	private static void loadConfigProperties(String propsFileName) {
	    //get current path
	    File f = new File("");
	    String absolutePath = f.getAbsolutePath();
	    String propertiesPath = PathManager.getPropsResourcePath() 
	    		+ propsFileName;
	    
	    f = new File(propertiesPath);
	    if(!f.exists()) {
	        throw new RuntimeException(
	            "Porperties file not found at: " + f.getAbsolutePath());
	    }
	    
	    Properties props = new Properties();
	    try {
	        props.load(new FileInputStream(f));
	        // common
	        SendHostType = props.getProperty("mail.default.sendHostType");
	        ReceiveHostType = props.getProperty("mail.default.receiveHostType");
	        
	        // smtp
	        SMTPServer = props.getProperty("mail.default.smpt");
	        SMTPUserName = props.getProperty("mail.default.smpt.user");
	        SMTPPassWord = props.getProperty("mail.default.smpt.password");
	        
	        // pop3
	    
	        POP3Server = props.getProperty("mail.default.pop3");
	        POP3UserName = props.getProperty("mail.default.pop3.user");
	        POP3PassWord = props.getProperty("mail.default.pop3.password");
	    } catch (FileNotFoundException e) {
	        // LOGGER.error("File not found at " + f.getAbsolutePath(), e);
	    	
	        System.out.println("File not found at " + f.getAbsolutePath() + e);
	    } catch (IOException e) {
	        // LOGGER.error("Error reading config file " + f.getName(), e);
	    	
	        System.out.println("Error reading config file " + f.getName() + e);
	    }
	}
	
	/**
     * Test
     * @param args
     */
    public static void main(String[] args) {  
        //
    	EmailLogger.getDefaultLoggerHanlder().info(
    			"JDK Logger is logging information at INFO Level"); 
    	getLoadConfigPropsInstance().loadConfigProperties(ConfigFileName);
    	
    	EmailLogger.getDefaultLoggerHanlder().info(
    			getLoadConfigPropsInstance().getReceiveHostType());
   }  
}
