package UI.Main;

import javax.swing.JFrame;

import UI.Main.MainUI;
import DataInfo.EnumType;
import UI.Login.LoginUI;
import Util.CreateFileUtil;
import Util.Email.PathManager;

public class MainUITest {
	 private static MainUI mainUI;
	 
	 private static void printUsageInfo() {
		 System.out.println("*******************************");
		 System.out.println("  Usage: ");
		 System.out.println("  		java -jar pathTo/../xxx.jar resPath");
		 System.out.println("  For example");
		 System.out.println("  		java -jar xxx.jar D:/ddk.log/");
		 System.out.println("*******************************");
	 }
	 
	 /**
	  * Test
	  * @param none
	  */
	public static void main(String[] args) {
		 System.out.println("  length: " + args.length);
		 System.out.println("  args: " + args);
		 boolean flag = false;
		 String defaultResPath = null;
		 if(args.length != 1){
		 	 printUsageInfo();
		 	 return;
		 } else if(args.length == 1) {
			 defaultResPath = args[0];
			 flag = true;
		 }
		 
		 System.out.println("defaultResPath: "  + defaultResPath);
		 CreateFileUtil.createDirFlag(defaultResPath);
		 
		 PathManager.SetIsSetclsPath(flag, defaultResPath);
		
		 // create login ui
		 LoginUI loginUI = new LoginUI();
		 
		 return;
	 }
}
/**/