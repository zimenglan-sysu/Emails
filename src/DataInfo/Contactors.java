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
     * 		���ݿ������id
     * @param appCount 
     * 		��ʾid���ʼ��շ��г��ֵĴ���
     * @param uuid 
     * 		Ψһ��ʶ����������ʶһ��Ψһ���ʼ���ֹ����ʱ�ظ���
     * 		��ֵ=(subject + sendDate).hashCode()�������㷨ͬString.hashCode()
     * @param type 
     * 		�� '�����ϵ', '������ϵ', '�Ǳ�' ��������
     * 		������EnumType.java��
     * @param userName 
     * 		��ϵ�˵�����
     * @param emailAddr 
     * 		��ϵ�˵��ʼ���ַ
     * @param emailSType 
     * 		��ϵ�˵��ʼ���ַ���ʼ�����������
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