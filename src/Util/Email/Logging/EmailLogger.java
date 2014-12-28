package Util.Email.Logging;

import java.io.File;
import java.io.IOException;  
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.MemoryHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.logging.ConsoleHandler; 
import java.util.logging.FileHandler ;
import java.util.logging.SocketHandler;

import Util.Email.PathManager;
import Util.Email.Logging.EmailMessageFormatter;;

/**
 * @author ddk
 * @created 14.12.18
 * 
 * 使用JDK内置Logger可以分成三个步骤来完成： 
 * 	1. 创建Logger 
 * 	2. 创建Handler,为handler指定Formmater, 
 * 		然后将Handler添加到logger中去。 
 * 	3. 设定Level级别
 * 
 *  problem:
 *  	1 禁止向控制台输出, 因为自定义的方式没有在控制台里面出现
 *  	2 不管是SimpleFormatter, 还是自定义, 控制台只输出SimpleFormatter的形式(不懂!!!)
 *  	3 同时使用System.out
 */
public class EmailLogger {
	private static final SimpleDateFormat sdf = 
			new SimpleDateFormat("yyyy.MM.dd-hh.mm.ss");  
	// get log folder
    private static final String LOG_FOLDER_NAME = 
    		PathManager.getLogsResourcePath();
    // define the extension
    private static final String LOG_FILE_RREFIX = "mail.log.";
    private static final String LOG_FILE_SUFFIX = ".log";  
    //
    private static String LoggerName = "Util.Email.Logging.EmailLogger";
    private static Level LevelName = Level.ALL;
    // default
    private static Logger logger = null;
    // 
    private static Logger logger2 = null;
    /**
     * Instance
     */
    private static EmailLogger el;
    
    private EmailLogger() {
    	
    }
    
    public static EmailLogger getEmailLoggerInstance() {
    	if(el == null) {
    		el = new EmailLogger();
    	}
    	
    	return el;
    }
    
    // 
    public static void setLoggerName(final String loggerName) {
    	LoggerName = loggerName;
    }
    
    public static String getLoggerName() {
    	return LoggerName;
    }
    
    // 
    public static void setLevelName(final Level levelName) {
    	LevelName = levelName;
    }
    
    public static Level getLevelName() {
    	return LevelName;
    }
    
    // 
    private synchronized static String getLogFilePath() {  
        StringBuffer logFilePath = new StringBuffer();  
        logFilePath.append(LOG_FOLDER_NAME);  
  
        final String logFileDir = logFilePath.toString();
        System.out.println();
        System.out.println("log - logFileDir: " + logFileDir);
        System.out.println();
        
        File file = new File(logFileDir);   
        if (!file.exists())  {
        	// make folder
            file.mkdir();  
        }
        
        // logFilePath.append(File.separatorChar);  
        final String fileName = LOG_FILE_RREFIX + sdf.format(new Date());
        logFilePath.append(fileName);  
        logFilePath.append(LOG_FILE_SUFFIX); 
        
        final String logFileName = logFilePath.toString();
        System.out.println();
        System.out.println("log - logFileName: " + logFileName);
        System.out.println();
         
        return logFileName;  
    }  
  
    // 
    public synchronized static Logger setLoggerHanlder(Logger logger) {  
        return setLoggerHanlder(logger, LevelName);  
    }  
  
    public synchronized static Logger setLoggerHanlder(Logger logger,  
            Level level) {  
  
        FileHandler fileHandler = null;  
        try {  
        	// 文件日志内容标记为可追加  
            fileHandler = new FileHandler(getLogFilePath(), true);  
  
            //以文本的形式输出  
            fileHandler.setFormatter(new EmailMessageFormatter());
            
            // simple
            // fileHandler.setFormatter(new SimpleFormatter());  
            
            // 为记录器添加记录处理器
            logger.addHandler(fileHandler); 
            
            // 设置level
            logger.setLevel(level);  
            
            // 禁止消息处理将日志消息上传给父级处理器
            logger.setUseParentHandlers(false);
        } catch (SecurityException e) {  
            logger.severe(populateExceptionStackTrace(e));  
        } catch (IOException e) {  
            logger.severe(populateExceptionStackTrace(e));  
        }  
        
        return logger;  
    }  
  
	// default
    public synchronized static Logger getDefaultLoggerHanlder() {  
    	if(logger == null) {
    		logger = setLoggerHanlder(Logger.getLogger(LoggerName));
    	}
    	
        return logger;  
    }  
    
    // default
    public synchronized static Logger getLoggerHanlder(String LoggerName) {  
    	if(logger2 == null) {
    		logger2 = setLoggerHanlder(Logger.getLogger(LoggerName));
    	}
    	
        return logger2;  
    } 
    
    // **************************************************************************
    
    private synchronized static String populateExceptionStackTrace(Exception e) {  
        StringBuilder sb = new StringBuilder();  
        sb.append(e.toString()).append("\n");  
        
        for (StackTraceElement elem : e.getStackTrace()) {  
            sb.append("\tat ").append(elem).append("\n");  
        }  
        
        return sb.toString();  
    }  
    
    /**
     * 
     * @param msg
     */
    public synchronized static void severe(String msg) {
    	msg = "\n" + msg;
    	System.out.println(msg);
    	EmailLogger.getDefaultLoggerHanlder().severe(msg);
    }
    
    public synchronized static void warning(String msg) {
    	msg = "\n" + msg;
    	System.out.println(msg);
    	EmailLogger.getDefaultLoggerHanlder().warning(msg);
    }
    
    public synchronized static void info(String msg) {
    	msg = "\n" + msg;
    	System.out.println(msg);
    	EmailLogger.getDefaultLoggerHanlder().info(msg);
    }
    
    public synchronized static void config(String msg) {
    	msg = "\n" + msg;
    	System.out.println(msg);
    	EmailLogger.getDefaultLoggerHanlder().config(msg);
    }
    
    public synchronized static void fine(String msg) {
    	msg = "\n" + msg;
    	System.out.println(msg);
    	EmailLogger.getDefaultLoggerHanlder().fine(msg);
    }
    
    public synchronized static void finer(String msg) {
    	msg = "\n" + msg;
    	System.out.println(msg);
    	EmailLogger.getDefaultLoggerHanlder().finer(msg);
    }
    
    public synchronized static void fineset(String msg) {
    	msg = "\n" + msg;
    	System.out.println(msg);
    	EmailLogger.getDefaultLoggerHanlder().finest(msg);
    }
}