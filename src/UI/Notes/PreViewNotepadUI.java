package UI.Notes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import Util.Email.PathManager;

public class PreViewNotepadUI extends JPanel implements MouseListener {
	/** 
	 * JButton
	 */
	// 收邮件按钮
	private JButton receivedBtn;
	/** 
	 * JPanel
	 */
	// 邮件内容正文可直接放入这里显示
	private JLabel notepadJL;
	private JLabel subjectJL;
	private JLabel sendDateJL;

	/**
	 * Variables
	 */
	boolean isRead;
	int msgNumIdx;
	private String subject;
	private Date sendDate;
	private CallBack callBack;
	private EmailMessage message;
	private static final int rows = 3;
	private static final int cols = 1;
	
	/**
	 * 
	 * @param isRead
	 * @param to
	 * @param from
	 * @param subject
	 * @param sendDate
	 */
	public PreViewNotepadUI(boolean isRead, int msgNumIdx, 
			String subject, Date sendDate, CallBack callBack) {
		this.isRead = isRead;
		this.msgNumIdx = msgNumIdx;
		this.subject = subject;
		this.sendDate = sendDate;
		this.callBack = callBack;
		
		setLayout(new BorderLayout());
		initNorm();
		initCompsListener();
		
		setVisible(true);
	}
	
	public PreViewNotepadUI(EmailMessage message, CallBack callBack) {
		this.message = message;
		this.isRead = message.getReceivedHasSeen();
		this.msgNumIdx = message.getReceivedIdx();
		this.subject = message.getSubject();
		this.sendDate = message.getSendDate();
		this.callBack = callBack;
		
		setLayout(new BorderLayout());
		initNorm();
		initCompsListener();
		
		setVisible(true);
	}
	
	private void initNorm() {
		// rows, cols
		GridLayout grid = new GridLayout(rows, cols);
		JPanel jp = new JPanel();
		jp.setLayout(grid);
		
		// notepad 
		notepadJL = new JLabel("  来自: " + NotepadUI.PreSubjectString);
		jp.add(notepadJL);
		// subject
		subjectJL = new JLabel("  主题: " +subject);
		jp.add(subjectJL);
		// sendDate
		SimpleDateFormat formerDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH-mm-ss");
		sendDateJL = new JLabel("  接收时间: " + 
				formerDateFormat.format(sendDate));
		jp.add(sendDateJL);
		//
		add(jp, BorderLayout.WEST);
		
		// 继续查看邮件按钮
		receivedBtn = new JButton(EnumType.MoreReadEmailBtnTitle 
				+ "(" + msgNumIdx + ")", new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.SeeEmailIcon));
		add(receivedBtn, BorderLayout.EAST);
	}
	
	public void initCompsListener() {
		receivedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PreViewNotepadUI.this.callBack.call(null, message);
			}
		});
		
		notepadJL.addMouseListener(this);
		subjectJL.addMouseListener(this);
		sendDateJL.addMouseListener(this);
	}
	
	/**
	 * {@link MouseListener}
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		PreViewNotepadUI.this.callBack.call(null, message);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//
		JLabel tempJLabel = (JLabel)e.getSource();
		tempJLabel.setForeground(Color.RED);
		// tempJLabel.setBackground(Color.yellow);
		tempJLabel.setFont(tempJLabel.getFont().deriveFont(Font.BOLD));
		tempJLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// 
		JLabel tempJLabel = (JLabel)e.getSource();
		tempJLabel.setForeground(Color.BLACK);
		tempJLabel.setFont(tempJLabel.getFont().deriveFont(Font.PLAIN));
		tempJLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	/**
     * CallBack
     */
    public interface CallBack{
    	public void call(Date date, EmailMessage msg);
    }
}
