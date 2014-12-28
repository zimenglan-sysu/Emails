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
		
		// 设置大小
		this.setSize(new Dimension(
				EnumType.ACC_JSCROLLPANE_WIDTH, EnumType.ACC_JSCROLLPANE_HEIGHT));
		
		// 禁止拉动窗口大小
		setResizable(EnumType.Received_UI_IS_RESIZE);
		
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
		
		new ReceivedUITest(new EmailMessage());
	}
}
