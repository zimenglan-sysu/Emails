package UI.Sends;

import javax.mail.Message;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import EmailProcessor.Sends.SendedSMTPProcessor;
import UI.Calendar.TimedUI;
import Util.Email.Logging.EmailLogger;
import Util.Email.CheckEmail;
import Util.Email.EmailDataManager;
import DataInfo.EmailMessage;
import DataInfo.EnumType;
import UI.Calendar.TimedUI.CallBack;

/**
 * @see 写邮件的界面
 * @author ddk
 * @since 2014-12-11
 */
public class SendedUI extends JPanel 
		implements MouseListener, ActionListener {
	/**
	 * JTextPane
	 */
	// private JTextPane bodyContentPane;
	private JEditorPane bodyContentPane;
	private JTextPane fileContentPane;
	/**
	 * JButton
	 */
	private JButton addAttachedBtn;
	private JButton sendBtn;
	private JButton timedSendBtn;
	private JButton anonymitySendBtn;
	private JButton deleteBtn;
	private JButton deleteAllBtn;
	/**
	 * JLabel
	 */
	private JLabel titleJL;
	private JLabel toJL;
	private JLabel subjectJL;
	private JLabel ccsJL;
	private JLabel bccsJL;
	private JLabel bodyJL;
	private JLabel priorityJL;
	/**
	 * JComboBox
	 */
	private JComboBox<String> priorityJCB;
	/**
	 * JFileChooser
	 */
	private JFileChooser fileJFC;
	/**
	 * JTextField
	 */
	private JTextField subjectJTF;
	private JTextField toJTF;
	private JTextField ccsJTF;
	private JTextField bccsJTF;
	/**
	 * JPanel
	 */
	private JPanel wholePanel;
	private JPanel topPanel; 
	private JPanel bottomPanel;
	/**
	 * JScrollPane 
	 */
	private JScrollPane filesJSP;
	private JScrollPane middleJSP;
	/**
	 * Variables
	 */
	private static final String DefaultContentTypeStr = "text/html";
	private StringBuffer seletedFileNames = null;
	private EmailMessage forwardMsg;
	
	// **********************************************************
	
	public SendedUI(EmailMessage forwardMsg) {
		this.forwardMsg = forwardMsg;
		// set layout
		this.setLayout(new BorderLayout());

		// set the ui components
		initComps();
		
		// set normally
		initNormSetting();
		
		//setBackground(Color.red);
		// set visible
		setVisible(true);
	}
	
	// createdComps
	private void createdComps() {
		// title
		titleJL = new JLabel();
		if(forwardMsg == null) {
			titleJL.setText(EnumType.SendedEmailTile);
		} else if(forwardMsg.getForwardFlag() == 0) {
			titleJL.setText(EnumType.SendedReplyEmailTile);
		} else if(forwardMsg.getForwardFlag() == 1) {
			titleJL.setText(EnumType.SendedReplyAllEmailTile);
		} else if(forwardMsg.getForwardFlag() == 2) {
			titleJL.setText(EnumType.SendedForwardEmailTile);
		}
		titleJL.addMouseListener(this);
		//titleJL.setBounds(x, y, width, height);
		// to
		toJL = new JLabel(
				EnumType.SendedEmailReceiver, JLabel.LEFT);
		toJL.addMouseListener(this);
	
		toJTF = new JTextField(EnumType.JTextFieldLength);
		toJTF.addMouseListener(this);
		/**
		 * For Test
		 */
		if(forwardMsg == null) {
			toJTF.setText(EmailDataManager.getDefaultEmailToAddr());
		// reply
		} else if(forwardMsg.getForwardFlag() == 0){
			toJTF.setText(forwardMsg.getFromStr());
		// reply all but without ccs & bccs
		} else if(forwardMsg.getForwardFlag() == 1) {
			// need to remove itself
			String itselfStr = (String)EmailDataManager.getData(
					EmailDataManager.getEmailAddr());
			String addrStr = forwardMsg.getFromStr() +
					forwardMsg.getToStr();
			String newAddrStr = CheckEmail.
					getRemoveItselfAddrsWhenReplyAll(addrStr, itselfStr);
			toJTF.setText(newAddrStr);
		} else if(forwardMsg.getForwardFlag() == 2) {
			toJTF.setText("");
		}
		
		// subject
		subjectJL = new JLabel(
				EnumType.SendedEmailSubject, JLabel.LEFT);
		subjectJL.addMouseListener(this);
		subjectJTF = new JTextField(EnumType.JTextFieldLength);
		subjectJTF.addMouseListener(this);
		if(forwardMsg != null) {
			subjectJTF.setText("Re:  " + 
					(forwardMsg.getSubject() == null ? "" : forwardMsg.getSubject()));
		}
		
		// ccs
		ccsJL = new JLabel(
				EnumType.SendedEmailCcs, JLabel.LEFT);
		ccsJL.addMouseListener(this);
		ccsJTF = new JTextField(EnumType.JTextFieldLength);
		ccsJTF.addMouseListener(this);
		if(forwardMsg != null) {
			ccsJTF.setText(forwardMsg.getCcsStr());
		}
		
		// bccs
		bccsJL = new JLabel(
				EnumType.SendedEmailBccs, JLabel.LEFT);
		bccsJL.addMouseListener(this);
		bccsJTF = new JTextField(EnumType.JTextFieldLength);
		bccsJTF.addMouseListener(this);
		if(forwardMsg != null) {
			bccsJTF.setText(forwardMsg.getBcsStr());
		}
		
		// body content
		bodyJL = new JLabel(
				EnumType.SendedEmailBodyContent);
		bodyJL.addMouseListener(this);
		
		// priority
		priorityJL = new JLabel(
				EnumType.SendedEmailPriority);
		priorityJL.addMouseListener(this);
		
		// JComboBox<String>
		priorityJCB = new JComboBox<String>(new String[] { 
			"1",
			"2",
			"3",
			"4",
			"5",
		});
		if(forwardMsg != null && forwardMsg.getPriority() != null) {
			priorityJCB.setSelectedItem(forwardMsg.getPriority());
		} else {
			final String defaultIdx = "3";	// point to 3
			priorityJCB.setSelectedItem(defaultIdx);
		}
		
		// JfileJFC
		// 建立一个fileJFC对象,并指定D:的目录为默认文件对话框路径
		fileJFC = new JFileChooser(EnumType.DefaultJFileChooserDir);
		fileContentPane = new JTextPane(); 
		
		/**
		 * Variables
		 */
		seletedFileNames = new StringBuffer();
	}
	
	// initTopCompsLayout
	private void initTopCompsLayout() {
		/**
		 * topPanel, using GridBagLayout
		 */
		GridBagLayout topLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		topPanel = new JPanel(topLayout);
		// title
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(titleJL, c);
		topPanel.add(titleJL);
		// to
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.RELATIVE;
		topLayout.setConstraints(toJL, c);
		topPanel.add(toJL);
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(toJTF, c);
		topPanel.add(toJTF);
		// subject
		c.gridwidth = GridBagConstraints.RELATIVE;
		topLayout.setConstraints(subjectJL, c);
		topPanel.add(subjectJL);
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(subjectJTF, c);
		topPanel.add(subjectJTF);
		// ccs
		c.gridwidth = 1;
		topLayout.setConstraints(ccsJL, c);
		topPanel.add(ccsJL);
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(ccsJTF, c);
		topPanel.add(ccsJTF);
		// bccs
		c.gridwidth = 1;
		topLayout.setConstraints(bccsJL, c);
		topPanel.add(bccsJL);
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(bccsJTF, c);
		topPanel.add(bccsJTF);
		// priority
		c.gridwidth = 1;
		topLayout.setConstraints(priorityJL, c);
		topPanel.add(priorityJL);
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(priorityJCB, c);
		topPanel.add(priorityJCB);
		
		// attachment button
		// c.gridwidth = 1;
		GridBagLayout topFileLayout = new GridBagLayout();
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.BOTH;
		JPanel filePanel = new JPanel(topFileLayout);
		c2.weightx = 0.0;
		c2.gridwidth = GridBagConstraints.RELATIVE;
		addAttachedBtn = new JButton(EnumType.SendedEmailAttachBtnName);
		topFileLayout.setConstraints(addAttachedBtn, c2);
		filePanel.add(addAttachedBtn);
		//
		c2.gridwidth = GridBagConstraints.RELATIVE;
		deleteBtn = new JButton("删除最后一个附件");
		topFileLayout.setConstraints(deleteBtn, c2);
		filePanel.add(deleteBtn);
		//
		c2.gridwidth = GridBagConstraints.REMAINDER;
		deleteAllBtn = new JButton("删除全部附件");
		topFileLayout.setConstraints(deleteAllBtn, c2);
		filePanel.add(deleteAllBtn);
		// add
		c.gridwidth = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(filePanel, c);
		topPanel.add(filePanel);
		
		// c.gridwidth = GridBagConstraints.RELATIVE;
		// c.weightx = 1.0;
		c.gridwidth = 1;
		// c.gridheight = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		// fileContentPane.setEditable(false);
		filesJSP =  new JScrollPane(fileContentPane);
		// filesJSP.setSize(160, 160);
		// filesJSP.setEnabled(true);
		topLayout.setConstraints(filesJSP, c);
		topPanel.add(filesJSP);
		
		// body
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(bodyJL, c);
		topPanel.add(bodyJL);
	}
	
	// intiMiddleCompsLayout
	private void intiMiddleCompsLayout() {
		/** 
		 * middleJSP
		 * Email Body
		 */
		// bodyContentPane = new JTextPane();
		// bodyContentPane = new JEditorPane();
		if(forwardMsg == null) {
			bodyContentPane = new JEditorPane();
			bodyContentPane.setText(
					EnumType.DefaultEmailSendedContentString);
		} else {
			String contentStr = forwardMsg.getContent().trim();
			// EmailLogger.info("content type: " + forwardMsg.getContentType());
			
			bodyContentPane = new JEditorPane(DefaultContentTypeStr,
					contentStr);
		}
		
		bodyContentPane.setBackground(Color.PINK);
		bodyContentPane.setEditable(true);
		/**
		 * ***********************
		 */
		middleJSP = new JScrollPane(bodyContentPane);
	}
	
	// initBottomCompsLayout
	private void initBottomCompsLayout() {
		/**
		 * Bottom Layout
		 */
		GridBagLayout bottomLayout = new GridBagLayout();
		GridBagConstraints c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.BOTH;
		bottomPanel = new JPanel(bottomLayout);
		// send buttons
		// Button - normally send
		c3.weightx = 0.0;
		c3.gridwidth = GridBagConstraints.RELATIVE;
		sendBtn = new JButton(EnumType.SendedEmailBtnName);
		bottomLayout.setConstraints(sendBtn, c3);
		bottomPanel.add(sendBtn);
		// Button - timed send
		c3.gridwidth = GridBagConstraints.RELATIVE;
		timedSendBtn = new JButton(EnumType.TimedSendedEmailBtnName);
		bottomLayout.setConstraints(timedSendBtn, c3);
		bottomPanel.add(timedSendBtn);
		// Button - anonymity send
		c3.gridwidth = GridBagConstraints.REMAINDER;
		anonymitySendBtn = new JButton(EnumType.AnonymitySendedEmailBtnName);
		/** 
		 * smtp需要验证, 所以发不了匿名邮件
		 */
		anonymitySendBtn.setEnabled(false);
		bottomLayout.setConstraints(anonymitySendBtn, c3);
		bottomPanel.add(anonymitySendBtn);
	}
	
	//initWholeCompsLayout
	private void initWholeCompsLayout() {
		/**
		 * add as whole
		 */
		// 
		wholePanel = new JPanel(new BorderLayout());
		// add topPanel
		wholePanel.add(topPanel, BorderLayout.NORTH);
		// add middelJSP
		wholePanel.add(middleJSP, BorderLayout.CENTER);
		// add bottomPanel
		wholePanel.add(bottomPanel, BorderLayout.SOUTH);
		// set background
		wholePanel.setBackground(Color.PINK);
		
		this.add(wholePanel, BorderLayout.CENTER);
	}
	
	//
	private void initComps() {
		//
		createdComps();
		
		//
		initTopCompsLayout();
		
		//
		intiMiddleCompsLayout();
		
		//
		initBottomCompsLayout();
		
		//
		initWholeCompsLayout();
		
		/**
		 * add ActionListener
		 */
		addAttachedBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		deleteAllBtn.addActionListener(this);
		sendBtn.addActionListener(this);
		timedSendBtn.addActionListener(this);
		anonymitySendBtn.addActionListener(this);
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

	/**
	 * {@link MouseListener}
	 */
	// Click
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	// Enterd
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

	/**
	 * {@link ActionListener}
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// send email
		if(e.getSource() == sendBtn || e.getSource() == timedSendBtn ||
				e.getSource() == anonymitySendBtn) {
			
			final EmailMessage message = getSendEmailMessage();
			if(message == null) {
				return;
			}
			
			/**
			 * send email
			 */
			boolean flag;
			// normail sending
			if(e.getSource() == sendBtn) {
				flag = SendedSMTPProcessor.getSendedSMTPProcessorInstance()
						.sendNormalEmail(message);
				if(flag) {
					JOptionPane.showMessageDialog(null, "邮件发送成功");
				} else {
					JOptionPane.showMessageDialog(null, "邮件发送失败");
				}
			// timing sending
			} else if(e.getSource() == timedSendBtn){
				// Calendar UI
				TimedUI TimedUI = new TimedUI(new TimedUI.CallBack() {
					@Override
					public void call(long duration, boolean isSureOrQuit) {
						//
						if(isSureOrQuit == false) {
							JOptionPane.showMessageDialog(null, "取消了定时发送邮件");
							return;
						}
						// send email
						SendedSMTPProcessor.getSendedSMTPProcessorInstance()
								.sendTimingEmail(message, duration);
					}
				});
			// anonymity sending
			} else if(e.getSource() == anonymitySendBtn){
				flag = SendedSMTPProcessor.getSendedSMTPProcessorInstance()
						.sendAnonymousEmail(message);
				if(flag) {
					JOptionPane.showMessageDialog(null, "匿名邮件发送成功");
				} else {
					JOptionPane.showMessageDialog(null, "匿名邮件发送失败");
				}
			}
		// add attachments
		} else if(e.getSource() == addAttachedBtn) {
			String absolutePath = getSeletedAttachment();
			if(absolutePath == null) {
				JOptionPane.showMessageDialog(null, 
						"已经取消选择附件");
				return;
			}
			//
			addOneAttachment(absolutePath);
		// delete the last selected file
		}  else if(e.getSource() == deleteBtn) {
			//
			deletedOneAttachment();
		// delete all selected files
		}  else if(e.getSource() == deleteAllBtn) {
			//
			deletedAllAttachments();
		}
	}
	
	// getSendEmailMessage
	private EmailMessage getSendEmailMessage() {
		// get from
		String from = (String)EmailDataManager.getData(
				EmailDataManager.getEmailAddr());
		from = from.trim();
		// maybe multi-receivers - to
		String toStr = toJTF.getText().trim();
		String toArr[] = CheckEmail.getValidAddrs(toStr);
		System.out.println("toArr: " + toArr.toString());
		
		// maybe multi-receivers - css
		String ccsStr = ccsJTF.getText().trim();
		String cssArr[] = CheckEmail.getValidAddrs(ccsStr);
		System.out.println("cssArr: " + cssArr);
		
		// maybe multi-receivers - bcss
		String bccsStr = bccsJTF.getText().trim();
		String bccsArr[] = CheckEmail.getValidAddrs(bccsStr);
		System.out.println("bcssArr: " + bccsArr);
		
		// get subject
		String subject = subjectJTF.getText().trim();
		try {
			subject = CheckEmail.encodeEmailStr(subject);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		if(toArr == null || toArr.length < 1 ||
				cssArr == null ||cssArr.length < 1 ||
				bccsArr == null || bccsArr.length < 1) {
			// to
			if(toArr == null || toArr.length < 1) {
				titleJL.setText(EnumType.SendedEmailReceiverErrorMsg);
				titleJL.setForeground(Color.PINK);
				return null;
			} 
			// ccs
			if(cssArr == null || cssArr.length < 1) {
				titleJL.setText(EnumType.SendedEmailCcsErrorMsg);
				titleJL.setForeground(new Color(216, 16, 216));
			}
			// bccs
			if(bccsArr == null || bccsArr.length < 1) {
				titleJL.setText(EnumType.SendedEmailBccsErrorMsg);
				titleJL.setForeground(new Color(216, 16, 216));
			}
		}
		// subject
		if(subject == null || "".equalsIgnoreCase(subject)) {
			titleJL.setText(EnumType.SendedEmailSubjectErrorMsg);
			titleJL.setForeground(new Color(216, 16, 216));
		}
		
		// get email content
		String content = bodyContentPane.getText().trim();
		if(content == null || "".equalsIgnoreCase(content)) {
			titleJL.setText(EnumType.SendedEmailContentErrorMsg);
			titleJL.setForeground(new Color(216, 16, 216));
			
			// here not allow empty content to be sent
			return null;
		}
		
		// get attachments -- filenames
		String[] fileNames;
		if(seletedFileNames == null || seletedFileNames.length() < 1) {
			fileNames = null;
		} else {
			fileNames = seletedFileNames.toString()
					.split(EnumType.EmailEegex);
		}	
		// reset
		seletedFileNames = new StringBuffer();
		fileContentPane.setText("");
		
		// type
		final String emailType = EmailMessage.SEND_TYPE_STRING;
		
		// priority
		String priority = priorityJCB.getItemAt(priorityJCB
				.getSelectedIndex());
		
		// Message 
		final EmailMessage message = new EmailMessage(emailType, toArr, 
				from, cssArr, bccsArr, subject, content, 
				new Date(), fileNames, priority);
		
		// set type ? html? plain?
		
		
		// for test
		System.out.println("*********************************************");
		System.out.println("now before sending email, print message");
		message.printInfo();
		
		return message;
	}
	
	// getSeletedAttachment
	private String getSeletedAttachment() {
		// 
		fileContentPane.setText("");
		fileJFC.setApproveButtonText("确定");
		fileJFC.setDialogTitle("打开文件");
		
		int selectedIdx = fileJFC.showOpenDialog(this);
		File file = null;
		
		if (selectedIdx == JFileChooser.APPROVE_OPTION) {
			file = fileJFC.getSelectedFile();
			titleJL.setText(EnumType.DefaultFileChoosedMsg);
			titleJL.setForeground(Color.PINK);
		} else if (selectedIdx == JFileChooser.CANCEL_OPTION) {
			titleJL.setText(EnumType.DefaultFileNotChoosedMsg);
			titleJL.setForeground(Color.PINK);
			return null;
		}
		
		String absolutePath = file.getAbsolutePath();
		String filename = file.getAbsolutePath();
		return absolutePath;
	}
	
	// addOneAttachment
	private void addOneAttachment(String absolutePath){
		seletedFileNames.append(absolutePath);
		seletedFileNames.append(EnumType.EmailEegex);
		
		String toString = seletedFileNames.toString();
		EmailLogger.info("selected files - before: " + toString);
		toString = toString.replace(EnumType.EmailEegex, 
				EnumType.EmailEegexWithLine);
		EmailLogger.info("selected files - after: " + toString);
		fileContentPane.setText(toString);
		
		JOptionPane.showMessageDialog(null, 
				"成功选择附件: " +toString);
	}
	
	// deletedOneAttachment
	private void deletedOneAttachment() {
		if(seletedFileNames == null || 
				seletedFileNames.lastIndexOf(EnumType.EmailEegex) == -1) {
			fileContentPane.setText(EnumType.DefaultJFileChooserMsg);
			
			seletedFileNames = null;
			seletedFileNames = new StringBuffer();
			
			titleJL.setText(EnumType.DeletedAllFilesMsg);
			titleJL.setForeground(Color.PINK);
			return;
		}
		EmailLogger.info("selected files - deleted before: " + 
				seletedFileNames.toString());
		
		final int len = seletedFileNames.length();
		int idx = seletedFileNames.lastIndexOf(EnumType.EmailEegex);
		int idx2 = seletedFileNames.substring(0, idx).
				lastIndexOf(EnumType.EmailEegex);
		idx2++;
		seletedFileNames.delete(idx2, len);
		
		titleJL.setText(EnumType.DeletedLastFileMsg);
		titleJL.setForeground(Color.PINK);
		
		//
		if(seletedFileNames == null || seletedFileNames.length() < 1) {
			fileContentPane.setText(EnumType.DefaultJFileChooserMsg);
			
			seletedFileNames = null;
			seletedFileNames = new StringBuffer();
			
			titleJL.setText(EnumType.DeletedAllFilesMsg);
			titleJL.setForeground(Color.PINK);
			return;
		}
		
		String toString = seletedFileNames.toString();
		EmailLogger.info("selected files - deleted after: " + toString);
		toString = toString.replace(EnumType.EmailEegex, 
				EnumType.EmailEegexWithLine);
		fileContentPane.setText(toString);
	}
	
	// deletedAllAttachments
	private void deletedAllAttachments() {
		if(seletedFileNames != null && seletedFileNames.length() > 0) {
			seletedFileNames.delete(0, seletedFileNames.length());
		}
		
		fileContentPane.setText(EnumType.DefaultJFileChooserMsg);
		seletedFileNames = null;
		seletedFileNames = new StringBuffer();
		
		titleJL.setText(EnumType.DeletedAllFilesMsg);
		titleJL.setForeground(Color.PINK);
	}
}