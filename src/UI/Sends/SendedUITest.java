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
		
		// ���ô�С
		this.setSize(new Dimension(
				EnumType.ACC_JSCROLLPANE_WIDTH, 
				EnumType.ACC_JSCROLLPANE_HEIGHT));
		
		// ��ֹ�������ڴ�С
		setResizable(EnumType.Sended_UI_IS_RESIZE);
		
		// ���д���
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2, 
				(screen.height - getSize().height) / 2);
		
		// ���ÿɼ�
		this.setVisible(true);
		
		// �رմ���
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// login
		EmailDataManager.DefaultLogin();
		
		SendedUITest st = new SendedUITest(new EmailMessage());
		// st.setBackground(Color.red);
	}
}