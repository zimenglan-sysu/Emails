package Util;

import java.io.File;
import java.io.IOException;

import Util.Email.PathManager;
import Util.Email.Logging.EmailLogger;

public class CreateFileUtil {
   
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {
        	String msg = "���������ļ� " + destFileName + " ʧ�ܣ�Ŀ���ļ��Ѵ��ڣ�";
            EmailLogger.info(msg);
            
            return false;
        }
        
        if (destFileName.endsWith(File.separator)) {
        	String msg = "���������ļ� " + destFileName + " ʧ�ܣ�Ŀ���ļ�����ΪĿ¼��";
            EmailLogger.info(msg);
            
            return false;
        }
        
        //�ж�Ŀ���ļ����ڵ�Ŀ¼�Ƿ����
        if(!file.getParentFile().exists()) {
            //���Ŀ���ļ����ڵ�Ŀ¼�����ڣ��򴴽���Ŀ¼
        	EmailLogger.info("Ŀ���ļ�����Ŀ¼�����ڣ�׼����������");
            if(!file.getParentFile().mkdirs()) {
            	EmailLogger.info("����Ŀ���ļ�����Ŀ¼ʧ�ܣ�");
            	
                return false;
            }
        }
        
        //����Ŀ���ļ�
        try {
            if (file.createNewFile()) {
            	EmailLogger.info("���������ļ� " + destFileName + " �ɹ���");
            	
                return true;
            } else {
            	EmailLogger.info("���������ļ� " + destFileName + " ʧ�ܣ�");
            	
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            EmailLogger.info("���������ļ� " + destFileName + " ʧ�ܣ�" + e.getMessage());
            
            return false;
        }
    }
   
   
    public static boolean createDirFlag(String destDirName) {
    	//
        File dir = new File(destDirName);
        if (dir.exists()) {
        	System.out.println("����Ŀ¼  " + destDirName + " ʧ�ܣ�Ŀ��Ŀ¼�Ѿ�����");
            
        	return false;
        }
        
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        
        //����Ŀ¼
        if (dir.mkdirs()) {
        	System.out.println("����Ŀ¼  " + destDirName + " �ɹ���");
        	
            return true;
        } else {
        	System.out.println("����Ŀ¼  " + destDirName + " ʧ�ܣ�");
        	
            return false;
        }
    }
    
    public static boolean createDir(String destDirName) {
    	//
        File dir = new File(destDirName);
        if (dir.exists()) {
        	EmailLogger.info("����Ŀ¼  " + destDirName + " ʧ�ܣ�Ŀ��Ŀ¼�Ѿ�����");
            
        	return false;
        }
        
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        
        //����Ŀ¼
        if (dir.mkdirs()) {
        	EmailLogger.info("����Ŀ¼  " + destDirName + " �ɹ���");
        	
            return true;
        } else {
        	EmailLogger.info("����Ŀ¼  " + destDirName + " ʧ�ܣ�");
        	
            return false;
        }
    }
   
   
    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        if (dirName == null) {
            try{
                //��Ĭ���ļ����´�����ʱ�ļ�
                tempFile = File.createTempFile(prefix, suffix);
                //������ʱ�ļ���·��
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                EmailLogger.info("������ʱ�ļ�ʧ�ܣ�" + e.getMessage());
                return null;
            }
        } else {
            File dir = new File(dirName);
            //�����ʱ�ļ�����Ŀ¼�����ڣ����ȴ���
            if (!dir.exists()) {
                if (!CreateFileUtil.createDir(dirName)) {
                    EmailLogger.info("������ʱ�ļ�ʧ�ܣ����ܴ�����ʱ�ļ����ڵ�Ŀ¼��");
                    return null;
                }
            }
            try {
                //��ָ��Ŀ¼�´�����ʱ�ļ�
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                EmailLogger.info("������ʱ�ļ�ʧ�ܣ�" + e.getMessage());
                return null;
            }
        }
    }
   
    public static void main(String[] args) {
        //����Ŀ¼
        String dirName = "D:/work/temp/temp0/temp1";
        CreateFileUtil.createDir(dirName);
        //�����ļ�
        String fileName = dirName + "/temp2/tempFile.txt";
        CreateFileUtil.createFile(fileName);
        //������ʱ�ļ�
        String prefix = "temp";
        String suffix = ".txt";
        for (int i = 0; i < 10; i++) {
            EmailLogger.info("��������ʱ�ļ���"
                    + CreateFileUtil.createTempFile(prefix, suffix, dirName));
        }
        //��Ĭ��Ŀ¼�´�����ʱ�ļ�
        for (int i = 0; i < 10; i++) {
            EmailLogger.info("��Ĭ��Ŀ¼�´�������ʱ�ļ���"
                    + CreateFileUtil.createTempFile(prefix, suffix, null));
        }
    }
}