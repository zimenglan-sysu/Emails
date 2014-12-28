package UI.Sends;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.mail.Message;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import Util.Email.EmailDataManager;

import DataInfo.EmailMessage;
import DataInfo.EnumType;

public class SendedUITest extends JFrame {
	//
	SendedUI sendUI = null;
	
	public SendedUITest(EmailMessage message) {
		super("zimenglan - send email UI");
	   
		sendUI = new SendedUI(message);
		// sendUI.setBackground(Color.yellow);
		this.add(sendUI, BorderLayout.CENTER);
		
		// 设置大小
		this.setSize(new Dimension(
				EnumType.ACC_JSCROLLPANE_WIDTH, 
				EnumType.ACC_JSCROLLPANE_HEIGHT));
		
		// 禁止拉动窗口大小
		setResizable(EnumType.Sended_UI_IS_RESIZE);
		
		// 居中窗口
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2, 
				(screen.height - getSize().height) / 2);
		
		// 设置可见
		this.setVisible(true);
		
		// 关闭窗口
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// login
		EmailDataManager.DefaultLogin();
		
		SendedUITest st = new SendedUITest(new EmailMessage());
		// st.setBackground(Color.red);
	}
}