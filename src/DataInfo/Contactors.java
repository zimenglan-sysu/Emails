package DataInfo;

import UI.Login.LoginUI;
import DataInfo.EnumType;

/**
 * use for database
 * @author ddk
 * @see 14.12.07
 */
public class Contactors {
	private int id;
	private int appCount;
	private String uuid;
    private String type;
    private String userName;
    private String emailAddr;
    private String emailSType;
    
    /**
     * 
     * @param id 
     * 		数据库里面的id
     * @param appCount 
     * 		表示id在邮件收发中出现的次数
     * @param uuid 
     * 		唯一标识符，用来标识一封唯一的邮件防止存入时重复，
     * 		其值=(subject + sendDate).hashCode()，计算算法同String.hashCode()
     * @param type 
     * 		有 '最近联系', '经常联系', '星标' 三种类型
     * 		定义在EnumType.java中
     * @param userName 
     * 		联系人的名字
     * @param emailAddr 
     * 		联系人的邮件地址
     * @param emailSType 
     * 		联系人的邮件地址的邮件服务器类型
     * 		'sina, 126', etc 
     */
    public Contactors(int id, int appCount, String uuid, String type, 
    		String userName, String emailAddr, String emailSType) {
    	this.id = id;
    	this.appCount = appCount;
    	this.uuid = uuid;
    	this.type = type;
    	this.userName = userName;
    	this.emailAddr = emailAddr;
    	this.emailSType = emailSType;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    int getId() {
    	return this.id;
    }
    
    public void setAppCount(int appCount) {
    	this.appCount = appCount;
    }
    
    int getAppCount() {
    	return this.appCount;
    }
    
    public void seUuid(String uuid) {
    	this.uuid = uuid;
    }
    
    String getUuid() {
    	return this.uuid;
    }
    
    public void setType(String type) {
    	this.type = type;
    }
    
    String getType() {
    	return this.type;
    }
    
    public void setUserName(String userName) {
    	this.userName = userName;
    }
    
    String getUserName() {
    	return this.userName;
    }
    
    public void setEmailAddr(String emailAddr) {
    	this.emailAddr = emailAddr;
    }
    
    String getEmailAddr() {
    	return this.emailAddr;
    }
    
    public void setEmailSType(String emailSType) {
    	this.emailSType = emailSType;
    }
    
    String getEmailSType() {
    	return this.emailSType;
    }
    
    /**
	  * Test
	  * @param none
	  */
	 public static void main(String[] args) {
		 // do nothing
	 }
}