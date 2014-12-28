package Util.Email.Logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * 
 */
public class EmailMessageFormatter extends Formatter {
	private static final int LEN = 1000;
	private static final String BLANK = " ";
	private static final String LINE = "\n";
	private static final String COLON = ": ";
	
    @SuppressWarnings("deprecation")
	public String format(LogRecord rec) {
       StringBuffer bufStr = new StringBuffer(LEN);
       
       //时间
       bufStr.append(new Date().toGMTString()); 
       bufStr.append(BLANK);
      //消息级别
       bufStr.append(rec.getLoggerName()); 
       bufStr.append(BLANK);
       //消息级别
       bufStr.append(rec.getLevel()); 
       bufStr.append(LINE);
       
       // 获取发出日志请求的类的名称
       bufStr.append(rec.getSourceClassName());
       bufStr.append(BLANK);
       // 获取（可能）发出日志请求的方法的名称
       bufStr.append(rec.getSourceMethodName());
       bufStr.append(COLON);
       // 行号 ????
       // 
       // 消息主体
       bufStr.append(rec.getMessage()); 
       bufStr.append(BLANK);
       
     //格式化日志记录数据
       bufStr.append(formatMessage(rec));
       bufStr.append(LINE);    //换行
       
       return bufStr.toString();
    }      
}
