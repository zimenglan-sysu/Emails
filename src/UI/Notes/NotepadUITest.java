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
		
		// ���ô�С
		this.setSize(new Dimension(
				EnumType.Send_Notepad_WIDTH, 
				EnumType.Send_Notepad_HEIGHT));
		
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
		
		NotepadUITest st = new NotepadUITest(new Date(), null);
		// st.setBackground(Color.red);
	}
}
