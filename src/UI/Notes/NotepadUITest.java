package UI.Notes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import Util.Email.EmailDataManager;

public class NotepadUITest extends JFrame {
	//
	NotepadUI notepadUI = null;
	
	public NotepadUITest(Date date, EmailMessage viewMsg) {
		if(viewMsg == null) {
			setTitle(EnumType.SendedNotepadTitle);
		} else {
			setTitle(EnumType.ReceivedNotepadTitle);
		}
	   
		notepadUI = new NotepadUI(date, viewMsg);
		this.add(notepadUI, BorderLayout.CENTER);
		
		// 设置大小
		this.setSize(new Dimension(
				EnumType.Send_Notepad_WIDTH, 
				EnumType.Send_Notepad_HEIGHT));
		
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
		
		NotepadUITest st = new NotepadUITest(new Date(), null);
		// st.setBackground(Color.red);
	}
}
