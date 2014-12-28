package UI.Calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import DataInfo.EnumType;


public class CalendarUITest extends JFrame {
	CalendarUI calendarUI = new CalendarUI();
	
	public CalendarUITest() {
		super("������");
	   
		this.add(calendarUI, BorderLayout.CENTER);
		
		// ���ô�С
		this.setSize(new Dimension(
				EnumType.CALENDER_WIDTH, EnumType.CALENDER_HEIGHT));
		
		// ��ֹ�������ڴ�С
		setResizable(false);
		
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
		new CalendarUITest();
	}
}
