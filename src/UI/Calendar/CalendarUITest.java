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
		super("万年历");
	   
		this.add(calendarUI, BorderLayout.CENTER);
		
		// 设置大小
		this.setSize(new Dimension(
				EnumType.CALENDER_WIDTH, EnumType.CALENDER_HEIGHT));
		
		// 禁止拉动窗口大小
		setResizable(false);
		
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
		new CalendarUITest();
	}
}
