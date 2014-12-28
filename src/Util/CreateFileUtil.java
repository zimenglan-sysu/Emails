package Util;

import java.io.File;
import java.io.IOException;

import Util.Email.PathManager;
import Util.Email.Logging.EmailLogger;

public class CreateFileUtil {
   
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {
        	String msg = "创建单个文件 " + destFileName + " 失败，目标文件已存在！";
            EmailLogger.info(msg);
            
            return false;
        }
        
        if (destFileName.endsWith(File.separator)) {
        	String msg = "创建单个文件 " + destFileName + " 失败，目标文件不能为目录！";
            EmailLogger.info(msg);
            
            return false;
        }
        
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
        	EmailLogger.info("目标文件所在目录不存在，准备创建它！");
            if(!file.getParentFile().mkdirs()) {
            	EmailLogger.info("创建目标文件所在目录失败！");
            	
                return false;
            }
        }
        
        //创建目标文件
        try {
            if (file.createNewFile()) {
            	EmailLogger.info("创建单个文件 " + destFileName + " 成功！");
            	
                return true;
            } else {
            	EmailLogger.info("创建单个文件 " + destFileName + " 失败！");
            	
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            EmailLogger.info("创建单个文件 " + destFileName + " 失败！" + e.getMessage());
            
            return false;
        }
    }
   
   
    public static boolean createDirFlag(String destDirName) {
    	//
        File dir = new File(destDirName);
        if (dir.exists()) {
        	System.out.println("创建目录  " + destDirName + " 失败，目标目录已经存在");
            
        	return false;
        }
        
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        
        //创建目录
        if (dir.mkdirs()) {
        	System.out.println("创建目录  " + destDirName + " 成功！");
        	
            return true;
        } else {
        	System.out.println("创建目录  " + destDirName + " 失败！");
        	
            return false;
        }
    }
    
    public static boolean createDir(String destDirName) {
    	//
        File dir = new File(destDirName);
        if (dir.exists()) {
        	EmailLogger.info("创建目录  " + destDirName + " 失败，目标目录已经存在");
            
        	return false;
        }
        
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        
        //创建目录
        if (dir.mkdirs()) {
        	EmailLogger.info("创建目录  " + destDirName + " 成功！");
        	
            return true;
        } else {
        	EmailLogger.info("创建目录  " + destDirName + " 失败！");
        	
            return false;
        }
    }
   
   
    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        if (dirName == null) {
            try{
                //在默认文件夹下创建临时文件
                tempFile = File.createTempFile(prefix, suffix);
                //返回临时文件的路径
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                EmailLogger.info("创建临时文件失败！" + e.getMessage());
                return null;
            }
        } else {
            File dir = new File(dirName);
            //如果临时文件所在目录不存在，首先创建
            if (!dir.exists()) {
                if (!CreateFileUtil.createDir(dirName)) {
                    EmailLogger.info("创建临时文件失败，不能创建临时文件所在的目录！");
                    return null;
                }
            }
            try {
                //在指定目录下创建临时文件
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                EmailLogger.info("创建临时文件失败！" + e.getMessage());
                return null;
            }
        }
    }
   
    public static void main(String[] args) {
        //创建目录
        String dirName = "D:/work/temp/temp0/temp1";
        CreateFileUtil.createDir(dirName);
        //创建文件
        String fileName = dirName + "/temp2/tempFile.txt";
        CreateFileUtil.createFile(fileName);
        //创建临时文件
        String prefix = "temp";
        String suffix = ".txt";
        for (int i = 0; i < 10; i++) {
            EmailLogger.info("创建了临时文件："
                    + CreateFileUtil.createTempFile(prefix, suffix, dirName));
        }
        //在默认目录下创建临时文件
        for (int i = 0; i < 10; i++) {
            EmailLogger.info("在默认目录下创建了临时文件："
                    + CreateFileUtil.createTempFile(prefix, suffix, null));
        }
    }
}