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
       
       //ʱ��
       bufStr.append(new Date().toGMTString()); 
       bufStr.append(BLANK);
      //��Ϣ����
       bufStr.append(rec.getLoggerName()); 
       bufStr.append(BLANK);
       //��Ϣ����
       bufStr.append(rec.getLevel()); 
       bufStr.append(LINE);
       
       // ��ȡ������־������������
       bufStr.append(rec.getSourceClassName());
       bufStr.append(BLANK);
       // ��ȡ�����ܣ�������־����ķ���������
       bufStr.append(rec.getSourceMethodName());
       bufStr.append(COLON);
       // �к� ????
       // 
       // ��Ϣ����
       bufStr.append(rec.getMessage()); 
       bufStr.append(BLANK);
       
     //��ʽ����־��¼����
       bufStr.append(formatMessage(rec));
       bufStr.append(LINE);    //����
       
       return bufStr.toString();
    }      
}
