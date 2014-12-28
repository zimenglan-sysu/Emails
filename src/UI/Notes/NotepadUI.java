package UI.Notes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import Util.Email.EmailDataManager;
import Util.Email.Logging.EmailLogger;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import EmailProcessor.Sends.SendedSMTPProcessor;

public class NotepadUI  extends JPanel 
		implements MouseListener, ActionListener, HyperlinkListener {
	/**
	 * JButton
	 */
	private JButton storedBtn;
	/**
	 * JLabel
	 */
	private JLabel timedJL;
	private JLabel subjectJL;
	private JLabel contentJL;
	/**
	 * JTextField
	 */
	private JTextField subjectJTF;
	private JTextField timedJTF;
	/**
	 * JEditorPane
	 */
	private JEditorPane bodyContentPane;
	/**
	 * JPanel
	 */
	private JPanel wholePanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	/**
	 * JScrollPane 
	 */
	private JScrollPane contentJSP;
	/**
	 * Variables
	 */
	public static String PreSubjectString = "我的记事本: ";
	private static final String DefaultContentTypeStr = "text/html";
	Date date;
	EmailMessage viewMsg;
	
	// ********************************************
	
	public NotepadUI(Date date, EmailMessage viewMsg) {
		this.date = date;
		this.viewMsg = viewMsg;
		
		// set layout
		this.setLayout(new BorderLayout());

		// set the ui components
		initComps();
		
		// set normally
		initNormSetting();
		
		setVisible(true);
	}
	
	// ********************************************
	
	/**
	 * url: http://blog.csdn.net/casularm/article/details/3826324
	 * 超链监听器，处理对超级链接的点击事件，但对按钮的点击还捕获不到  
	 */
	public void hyperlinkUpdate(HyperlinkEvent e)  {  
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)  {  
			JEditorPane pane = (JEditorPane) e.getSource();  
	        if (e instanceof HTMLFrameHyperlinkEvent)  {  
	        	HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;  
	            HTMLDocument doc = (HTMLDocument) pane.getDocument();  
	            doc.processHTMLFrameHyperlinkEvent(evt);  
	         } else {  
	        	 try {  
	        		 pane.setPage(e.getURL());  
	        	 } catch (Throwable t) {  
	        		 t.printStackTrace();  
	             }  
	        }  
		}  
	}  
   
	private void initComps() {
		GridBagLayout contentLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		topPanel = new JPanel(contentLayout);
		
		// record date
		if(viewMsg == null) {
			// timedJL
			c.weightx = 0.0;
			timedJL = new JLabel(EnumType.SendedNotepadRecordedDate);
			timedJL.addMouseListener(this);
			contentLayout.setConstraints(timedJL, c);
			topPanel.add(timedJL);
			
			// timedJTF
			c.gridwidth = GridBagConstraints.REMAINDER;
			String sytleStr = " * yyyy 年 MM 月 dd 日 HH 时 mm 分 ss 秒  * ";
			SimpleDateFormat formatter = new SimpleDateFormat(sytleStr); 
			String dateStr = formatter.format(date);
			timedJTF = new JTextField(dateStr);
			timedJTF.addMouseListener(this);
			contentLayout.setConstraints(timedJTF, c);
			topPanel.add(timedJTF);
		}
		
		// subject
		c.gridwidth = 1;
		subjectJL = new JLabel(EnumType.SendedNotepadSubject);
		subjectJL.addMouseListener(this);
		contentLayout.setConstraints(subjectJL, c);
		topPanel.add(subjectJL);
		// subjectJTF
		c.gridwidth = GridBagConstraints.REMAINDER;
		if(viewMsg == null) {
			subjectJTF = new JTextField();
		} else {
			subjectJTF = new JTextField(viewMsg.getSubject());
			subjectJTF.setEditable(false);
		}
		subjectJTF.addMouseListener(this);
		contentLayout.setConstraints(subjectJTF, c);
		topPanel.add(subjectJTF);
		
		// body label
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		contentJL = new JLabel(EnumType.SendedNotepadBodyName);
		contentJL.addMouseListener(this);
		contentLayout.setConstraints(contentJL, c);
		topPanel.add(contentJL);
		
		// bodyContentPane
		if(viewMsg == null) {
			bodyContentPane = new JEditorPane();
			bodyContentPane.setEditable(true);
		} else {
			String contentStr = viewMsg.getContent().trim();
			EmailLogger.info("content type: " + viewMsg.getContentType());
			EmailLogger.info("content: " + contentStr);
			EmailLogger.info("****************************");
			
			bodyContentPane = new JEditorPane(
					DefaultContentTypeStr, contentStr);
			// set unable
			bodyContentPane.setEditable(false);
		}
		bodyContentPane.addHyperlinkListener(this);
		
		
		// contentJSP
		contentJSP = new JScrollPane(bodyContentPane);
		
		// 
		storedBtn = new JButton(EnumType.SendedNotepadBtnName);
		if(viewMsg == null) {
			storedBtn.addActionListener(this);
		} else {
			storedBtn.setEnabled(false);
		}
		topPanel.add(storedBtn);
		bottomPanel = new JPanel();
		bottomPanel.add(storedBtn, BorderLayout.CENTER);
		
		
		
		//
		wholePanel = new JPanel(new BorderLayout());
		wholePanel.add(topPanel, BorderLayout.NORTH);
		wholePanel.add(contentJSP, BorderLayout.CENTER);
		wholePanel.add(bottomPanel, BorderLayout.SOUTH);
		wholePanel.setBackground(Color.PINK);
		//
		this.add(wholePanel, BorderLayout.CENTER);
	}
	
	private void initNormSetting() {
		try {
			// 设置Nimbus皮肤
			UIManager.setLookAndFeel(
					EnumType.NimbusLookAndFeel);
		} catch (ClassNotFoundException e) {
			// 表示运行环境非JRE7
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);

		// set bg color
		setBackground(EnumType.SendedEmailBGColor);
	}
	
	// ********************************************
	
	// actionPerformed
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == storedBtn) {
			// instance
			EmailMessage message = new EmailMessage();
			
			// from
			String from = (String)EmailDataManager.getData(
					EmailDataManager.getEmailAddr());
			from = from.trim();
			message.setFrom(from);
			
			String[] to = new String[1];
			to[0] = (String)EmailDataManager.
					getData(EmailDataManager.getEmailAddr());
			message.setTo(to);
			
			String sytleStr = " (yyyy.MM.dd-HH.mm.ss) - ";
			SimpleDateFormat formatter = new SimpleDateFormat(sytleStr); 
			String dateStr = formatter.format(date);
			String subject = PreSubjectString + dateStr
					+ subjectJTF.getText().trim();
			message.setSubject(subject);
			
			String content;
			if(date != null) {
				String dateInfoStr = "<font color=red>" +
						"记录 " + date.toString() 
						+ " 这天的事情: " +
						"</font>";
				content = dateInfoStr + "\n\n" + 
						bodyContentPane.getText().trim();
			} else {
				content = bodyContentPane.getText().trim();
			}
			message.setContent(content);
			
			EmailLogger.info("notepad - subject: " + subject);
			EmailLogger.info("notepad - content: " + content);
			/**
			 * 以邮件的方式发送给自己
			 */
			boolean flag = SendedSMTPProcessor.getSendedSMTPProcessorInstance()
					.sendNormalEmail(message);
			if(flag) {
				JOptionPane.showMessageDialog(null, "保存记事本成功");
			} else {
				JOptionPane.showMessageDialog(null, "保存记事本失败");
			}
		}
		
	}
	// Clicked
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	// Entered
	@Override
	public void mouseEntered(MouseEvent e) {
		//
		if(e.getSource() instanceof JLabel) {
			//
			JLabel tempJL= (JLabel)e.getSource();
			tempJL.setForeground(Color.RED);
			tempJL.setFont(tempJL.getFont().deriveFont(Font.BOLD));
			tempJL.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() instanceof JTextField) {
			//
			JTextField tempJTF = (JTextField)e.getSource();
			tempJTF.setForeground(Color.RED);
			tempJTF.setFont(tempJTF.getFont().deriveFont(Font.BOLD));
			tempJTF.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			// To Do
		}
	}
	// Exited
	@Override
	public void mouseExited(MouseEvent e) {
		//
		if(e.getSource() instanceof JLabel) {
			//
			JLabel tempJL = (JLabel)e.getSource();
			tempJL.setForeground(Color.BLACK);
			tempJL.setFont(tempJL.getFont().deriveFont(Font.PLAIN));
			tempJL.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if(e.getSource() instanceof JTextField) {
			//
			JTextField tempJTF = (JTextField)e.getSource();
			tempJTF.setForeground(Color.BLACK);
			tempJTF.setFont(tempJTF.getFont().deriveFont(Font.PLAIN));
			tempJTF.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	
		} else {
			// To Do
		}
	}
	// Pressed
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	// Released
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}