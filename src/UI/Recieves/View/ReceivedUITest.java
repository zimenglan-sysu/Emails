package UI.Recieves.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import Util.Email.EmailDataManager;

public class ReceivedUITest extends JFrame {
	//
	ReceivedUI receivedUI = null;
	
	public ReceivedUITest(EmailMessage message) {
		super("zimenglan - Received Email UI");
	   
		receivedUI = new ReceivedUI(message);
		// receivedUI.setBackground(Color.red);
		this.add(receivedUI, BorderLayout.CENTER);
		
		// ���ô�С
		this.setSize(new Dimension(
				EnumType.ACC_JSCROLLPANE_WIDTH, EnumType.ACC_JSCROLLPANE_HEIGHT));
		
		// ��ֹ�������ڴ�С
		setResizable(EnumType.Received_UI_IS_RESIZE);
		
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
		
		new ReceivedUITest(new EmailMessage());
	}
}
