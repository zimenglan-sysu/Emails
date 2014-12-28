package UI.Recieves.PreView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
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


public class PreViewReceivedUI extends JPanel implements MouseListener {
	/** 
	 * JButton
	 */
	// 收邮件按钮
	private JButton receivedBtn;
	/** 
	 * JPanel
	 */
	// 邮件内容正文可直接放入这里显示
	private JLabel toJLabel;
	private JLabel fromJLabel;
	private JLabel subjectJLabel;
	private JLabel sendDateJLabel;

	/**
	 * Variables
	 */
	boolean isRead;
	int msgNumIdx;
	private String to;
	private String from;
	private String subject;
	private Date sendDate;
	private CallBack callBack;
	private EmailMessage message;
	private static final int rows = 4;
	private static final int cols = 1;
	
	/**
	 * 
	 * @param isRead
	 * @param to
	 * @param from
	 * @param subject
	 * @param sendDate
	 */
	public PreViewReceivedUI(boolean isRead, int msgNumIdx, 
			String to, String from, 
			String subject, Date sendDate, CallBack callBack) {
		this.isRead = isRead;
		this.msgNumIdx = msgNumIdx;
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.sendDate = sendDate;
		this.callBack = callBack;
		
		setLayout(new BorderLayout());
		initNorm();
		initCompsListener();
		
		setVisible(true);
	}
	
	public PreViewReceivedUI(EmailMessage message, CallBack callBack) {
		this.message = message;
		this.isRead = message.getReceivedHasSeen();
		this.msgNumIdx = message.getReceivedIdx();
		this.to = message.getToStr();
		this.from = message.getFromStr();
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
		
		// to
		toJLabel = new JLabel("  收邮件人: " + to);
		toJLabel.setBackground(Color.WHITE);
		jp.add(toJLabel);
		// from
		fromJLabel = new JLabel("  发邮件人: " + from);
		jp.add(fromJLabel);
		// subject
		subjectJLabel = new JLabel("  主题: " +subject);
		jp.add(subjectJLabel);
		// sendDate
		SimpleDateFormat formerDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH-mm-ss");
		sendDateJLabel = new JLabel("  接收时间: " + 
				formerDateFormat.format(sendDate));
		jp.add(sendDateJLabel);
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
				PreViewReceivedUI.this.callBack.call(message);
			}
		});
		
		toJLabel.addMouseListener(this);
		fromJLabel.addMouseListener(this);
		subjectJLabel.addMouseListener(this);
		sendDateJLabel.addMouseListener(this);
	}
	
	/**
	 * {@link MouseListener}
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		PreViewReceivedUI.this.callBack.call(message);
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
    	public void call(EmailMessage msg);
    }
}