package DataInfo;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import Util.Email.EmailDataManager;

/**
 * this does not work
 * @author ddk
 *
 */
public class MailAuthenticator extends Authenticator {
   //
   private String username = null;
   /**
    * just for record the whole username, 
    * including the smtp server name
    */
   private String username2 = null;
   /**
    * Represents the password of sending SMTP sever.
    * More explicitly, the password is the password of username.
    */
   private String password = null;

   private PasswordAuthentication pssdAutn = null;
   
   // *************************************************************
   
   public MailAuthenticator() {
	   username = EmailDataManager.getDefaultEmailAddr();
	   password = EmailDataManager.getDefaultEmailPswd();
	   int idx = username.indexOf("@");
	   if(idx != -1) {
		   this.username = username.substring(0, idx - 1);
		   // this.username2 = username.substring(idx + 1);
		   this.username2 = username;
	   } else {
		   this.username = username;
		   this.username2 = null;
	   }
   }
   
   public MailAuthenticator(String username, String pass) {
	   int idx = username.indexOf("@");
	   if(idx != -1) {
		   this.username = username.substring(0, idx - 1);
		   // this.username2 = username.substring(idx + 1);
		   this.username2 = username;
	   } else {
		   this.username = username;
		   this.username2 = null;
	   }
	   this.password = pass;
   }
   
   @Override
   protected PasswordAuthentication getPasswordAuthentication() {
	   if(this.pssdAutn == null) {
		   this.pssdAutn =  new PasswordAuthentication(
				   this.username, this.password);
	   }
	   
	   return this.pssdAutn;
   }
   
   /**
    * gets & sets
    */
   public String getUsername() {
	   return this.username;
   }
   
   public void setUsername(String username) {
	   this.username = this.username;
   }
   
   public String getUsername2() {
	   return this.username2;
   }
   
   public void setUsername2(String username) {
	   this.username2 = this.username;
   }
   
   public String getPassword() {
	   return this.password;
   }
   
   public void setPassword(String password) {
	   this.password = this.password;
   }
}