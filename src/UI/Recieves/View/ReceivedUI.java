package UI.Recieves.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import UI.Calendar.TimedUI;
import UI.Sends.SendedUITest;
import Util.Email.CheckEmail;
import Util.Email.EmailDataManager;
import Util.Email.PathManager;
import Util.Email.Logging.EmailLogger;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import EmailProcessor.Recieves.ReceivedPoP3Processor;
import EmailProcessor.Sends.SendedSMTPProcessor;

public class ReceivedUI extends JPanel implements 
	MouseListener, ActionListener, HyperlinkListener {
	/**
	 * JTextPane
	 */
	// private JTextPane bodyContentPane;
	private JEditorPane bodyContentPane;
	/**
	 * JButton
	 */
	// 回复按钮
	private JButton replyBtn;
	private JButton replyAllBtn;
	private JButton forwardBtn;
	// 下载按钮
	private JButton addFilePathBtn;
	private JButton downloadBtn;
	private JButton downloadAllBtn;
	/**
	 * JLabel
	 */
	private JLabel titleJL;
	private JLabel fromJL;
	private JLabel toJL;
	private JLabel subjectJL;
	private JLabel ccsJL;
	private JLabel bccsJL;
	private JLabel bodyJL;
	private JLabel priorityJL;
	private JLabel storedPathJL;
	/**
	 * JFileChooser
	 */
	private JFileChooser fileJFC;
	/**
	 * JTextField
	 */
	private JTextField subjectJTF;
	private JTextField fromJTF;
	private JTextField toJTF;
	private JTextField ccsJTF;
	private JTextField bccsJTF;
	private JTextField priorityValueJTF;
	/**
	 * JPanel
	 */
	private JPanel wholePanel; 
	private JPanel topPanel; 
	private JPanel middlePanel; 
	private JPanel bottomPanel;
	/**
	 * JScrollPane 
	 */
	private JScrollPane middleJSP;
	/**
	 * Variables
	 */
	private boolean hasAttachments = false;
	private EmailMessage message;
	private static final String DefaultContentTypeStr = "text/html";
	
	// **********************************************************
	
	/**
	 * Constructor
	 */
	public ReceivedUI() {
		
	}
	// for the empty constructor
	public void setInits(EmailMessage message) {
		// set message
		this.message = message;
		
		// set layout
		setLayout(new BorderLayout());
		// setLayout(null);

		// set the ui components
		initComps();
		
		// set normally
		initNormSetting();
		
		// setBackground(Color.red);
		// set visible
		setVisible(true);
	}
	
	public ReceivedUI(EmailMessage message) {
		// set message
		this.message = message;
		
		// set layout
		setLayout(new BorderLayout());

		// set the ui components
		initComps();
		
		// set normally
		initNormSetting();
		
		// set visible
		setVisible(true);
	}

	// ****************************************************
	
	/**
	 * components
	 */
	// checkHasAttachments
	private void checkHasAttachments() {
		// attachment
		if(message.getHasAttachMent() == true) {
			Date receivedDate = message.getSendDate();
			Date nowDate = new Date();
			long dayNum = (nowDate.getTime() - receivedDate.getTime()) /
					EnumType.DayOfMillSeconds;
			
			// check 附件是否过期
			if(message.getFileNames() != null && 
					dayNum <= EnumType.ReceivedEmailMaxDay) {
				hasAttachments = true;
			// log
			} else {
				EmailLogger.warning("reveived date" + receivedDate);
				EmailLogger.warning("attachments exist, but out of date -- " 
					+ dayNum + "days");
			}
		}
	}
	// createdComps
	private void createdComps(){
		// title
		titleJL = new JLabel(
				EnumType.ReceivedEmailTile);
		titleJL.addMouseListener(this);
		// from
		fromJL = new JLabel(
				EnumType.ReceivedEmailSender, JLabel.LEFT);
		fromJL.addMouseListener(this);
		
		fromJTF = new JTextField(EnumType.RecievedJTextFieldLength);
		fromJTF.addMouseListener(this);
		// EmailLogger.info("fromStr: " + message.getFromStr());
		if(message.getFromStr() != null && message.getFromStr().length() > 0) {
			fromJTF.setText(message.getFromStr());
		}
		fromJTF.setEditable(EnumType.DefaultRecievedIsJTFEditable);
		
		// to
		toJL = new JLabel(
				EnumType.ReceivedEmailReceiver, JLabel.LEFT);
		toJL.addMouseListener(this);
		
		toJTF = new JTextField(EnumType.RecievedJTextFieldLength);
		if(message.getToStr() != null && message.getToStr().length() > 0) {
			toJTF.setText(message.getToStr());
		}
		toJTF.setEditable(EnumType.DefaultRecievedIsJTFEditable);
		toJTF.addMouseListener(this);
		// subject
		subjectJL = new JLabel(
				EnumType.ReceivedEmailSubject, JLabel.LEFT);
		subjectJL.addMouseListener(this);
		
		subjectJTF = new JTextField(EnumType.RecievedJTextFieldLength);
		if(message.getSubject() != null && message.getSubject().length() > 0) {
			subjectJTF.setText(message.getSubject());
		}
		subjectJTF.setEditable(EnumType.DefaultRecievedIsJTFEditable);
		subjectJTF.addMouseListener(this);
		// ccs
		ccsJL = new JLabel(
				EnumType.ReceivedEmailCcs, JLabel.LEFT);
		ccsJL.addMouseListener(this);
		
		ccsJTF = new JTextField(EnumType.RecievedJTextFieldLength);
		if(message.getCcsStr() != null && message.getCcsStr().length() > 0) {
			ccsJTF.setText(message.getCcsStr());
		}
		ccsJTF.setEditable(EnumType.DefaultRecievedIsJTFEditable);
		ccsJTF.addMouseListener(this);
		// bccs
		bccsJL = new JLabel(
				EnumType.ReceivedEmailBccs, JLabel.LEFT);
		bccsJL.addMouseListener(this);
		
		bccsJTF = new JTextField(EnumType.RecievedJTextFieldLength);
		if(message.getBcsStr() != null && message.getBcsStr().length() > 0) {
			bccsJTF.setText(message.getBcsStr());
		}
		bccsJTF.setEditable(EnumType.DefaultRecievedIsJTFEditable);
		bccsJTF.addMouseListener(this);
		// body content
		bodyJL = new JLabel(
				EnumType.ReceivedEmailBodyContent);
		bodyJL.addMouseListener(this);
		// priority
		priorityJL = new JLabel(
				EnumType.SendedEmailPriority);
		priorityJL.addMouseListener(this);
		
		priorityValueJTF = new JTextField(EnumType.RecievedJTextFieldLength);
		if(message.getPriority() != null && message.getPriority().length() > 1) {
			priorityValueJTF.setText(message.getPriority());
		}
		priorityValueJTF.setEditable(EnumType.DefaultRecievedIsJTFEditable);
		priorityValueJTF.addMouseListener(this);
		// JfileJFC
		// 建立一个fileJFC对象,并指定D:的目录为默认文件对话框路径
		fileJFC = new JFileChooser(EnumType.DefaultJFileChooserDir);
	}
	
	// initTopAttachmentLayout 
	private void initTopAttachmentLayout(
			GridBagLayout topLayout, GridBagConstraints c) {
		/** 
		 * attachments
		 */
		if(hasAttachments) {
			// downloadAllBtn
			c.gridwidth = 1;
			downloadAllBtn = new JButton(EnumType.
					DownloadReceivedAllAttachmentsBtnName);
			topLayout.setConstraints(downloadAllBtn, c);
			topPanel.add(downloadAllBtn);
			// storedPathJL
			storedPathJL = new JLabel();
			storedPathJL.setText(
					EnumType.ReceivedDefaultJFileChooserMsg);
			storedPathJL.addMouseListener(this);
			c.gridwidth = GridBagConstraints.REMAINDER;
			topLayout.setConstraints(storedPathJL, c);
			topPanel.add(storedPathJL);
			
			int idx0 = 0;
			final int len = message.getFileNames().length;
			EmailLogger.info("len of attachments: " + len);
			
			JLabel[] attachmentsTitleJL = new JLabel[len];
			final JTextField[] attachmentNameJTF = new JTextField[len];
			
			for(String str: message.getFileNames()) {
				final int idx = idx0;
				attachmentsTitleJL[idx] = new JLabel("　　第" +
						(idx + 1) + EnumType.ReceivedDefaultAttachments);
				attachmentsTitleJL[idx].addMouseListener(this);	
				//
				attachmentNameJTF[idx] = new JTextField(
						EnumType.RecievedJTextFieldLength);
				attachmentNameJTF[idx].setText(str);
				attachmentNameJTF[idx].setEditable(
						EnumType.DefaultRecievedIsJTFEditable);
				// get file name
				final String singleFileName = str;
				
				// add mouse listener
				attachmentNameJTF[idx].addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// error: file name always be the fisrt attachment
						// final String singleFileName = 
						// 	attachmentNameJTF[idx].getText().trim();
						if (e.getClickCount() == 2) {
							if(singleFileName == null || singleFileName.length() < 1) {
								JOptionPane.showMessageDialog(null, 
										singleFileName + "不存在, 系统出错");
								storedPathJL.setText(singleFileName + "不存在, 系统出错");
								storedPathJL.setForeground(Color.red);
								return;
							}
							
							Thread thread = new Thread() {
								@Override  
				                public void run() {  
									// donwload single file
									EmailLogger.info("第" + (idx + 1) + "附件: " + 
											attachmentNameJTF[idx].getText());
									downloads(attachmentNameJTF[idx].getText());
				                }
							};
							thread.start();
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						//
						JTextField tempJTF = (JTextField)e.getSource();
						tempJTF.setForeground(Color.RED);
						tempJTF.setFont(tempJTF.getFont().deriveFont(Font.BOLD));
						tempJTF.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}

					@Override
					public void mouseExited(MouseEvent e) {
						JTextField tempJTF = (JTextField)e.getSource();
						tempJTF.setForeground(Color.BLACK);
						tempJTF.setFont(tempJTF.getFont().deriveFont(Font.PLAIN));
						tempJTF.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
					}
				});
				
				//
				c.gridwidth = 1;
				topLayout.setConstraints(attachmentsTitleJL[idx], c);
				topPanel.add(attachmentsTitleJL[idx]);
				//
				c.gridwidth = GridBagConstraints.REMAINDER;
				topLayout.setConstraints(attachmentNameJTF[idx], c);
				topPanel.add(attachmentNameJTF[idx]);
				
				idx0++;
			}
		}
	}
	
	// initTopLayout
	private void initTopLayout() {
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
		
		// from
		c.gridwidth = GridBagConstraints.RELATIVE;
		topLayout.setConstraints(fromJL, c);
		topPanel.add(fromJL);
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(fromJTF, c);
		topPanel.add(fromJTF);
		
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
		topLayout.setConstraints(priorityValueJTF, c);
		topPanel.add(priorityValueJTF);
		
		// attachments
		initTopAttachmentLayout(topLayout, c);
		
		// body JLabel
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		topLayout.setConstraints(bodyJL, c);
		topPanel.add(bodyJL);
	}
	
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
	   
	// initMiddelLayout
	private void initMiddelLayout() {
		/**
		 * middleJSP
		 * Email Body
		 */
		//bodyContentPane = new JTextPane();
		
		if(message != null && message.getContent() != null &&
				message.getContent().trim() != null &&
				message.getContent().trim().length() > 0) {
			String contentStr = message.getContent().trim();
			//
			EmailLogger.info("content type: " + message.getContentType());
			
			bodyContentPane = new JEditorPane(DefaultContentTypeStr,
					contentStr);
			bodyContentPane.setEditable(false);
			
			//bodyContentPane.setText(contentStr);
		} else {
			bodyContentPane = new JEditorPane();
		}
		
		bodyContentPane.addHyperlinkListener(this); 
		bodyContentPane.setBackground(Color.PINK);
		middleJSP = new JScrollPane(bodyContentPane, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// middleJSP.setBackground(Color.cyan);
	}
	
	// initBottomLayout
	private void initBottomLayout() {
		/**
		 * Bottom Layout
		 */
		GridBagLayout bottomLayout = new GridBagLayout();
		GridBagConstraints c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.BOTH;
		bottomPanel = new JPanel(bottomLayout);
		/**
		 * Reply buttons
		 */
		// Button - normally reply
		c3.weightx = 0.0;
		c3.gridwidth = GridBagConstraints.RELATIVE;
		replyBtn = new JButton(EnumType.ReplyReceivedEmailBtnName);
		bottomLayout.setConstraints(replyBtn, c3);
		bottomPanel.add(replyBtn);
		// Button - reply all
		c3.gridwidth = GridBagConstraints.RELATIVE;
		replyAllBtn = new JButton(EnumType.ReplyAllReceivedEmailBtnName);
		// replyAllBtn.setEnabled(false);
		bottomLayout.setConstraints(replyAllBtn, c3);
		bottomPanel.add(replyAllBtn);
		// Button - forward
		c3.gridwidth = GridBagConstraints.REMAINDER;
		forwardBtn = new JButton(EnumType.ForwardReceivedEmailBtnName);
		bottomLayout.setConstraints(forwardBtn, c3);
		bottomPanel.add(forwardBtn);
	}
	
	// initWholeLayout
	private void initWholeLayout() {
		/**
		 * add as whole
		 */
		// 
		wholePanel = new JPanel(new BorderLayout());
		// add topPanel
		wholePanel.add(topPanel, BorderLayout.NORTH);
		// add middleJSP
		wholePanel.add(middleJSP, BorderLayout.CENTER);
		// add bottomPanel
		wholePanel.add(bottomPanel, BorderLayout.SOUTH);
		wholePanel.setBackground(Color.PINK);
		this.add(wholePanel, BorderLayout.CENTER);
		
		
		/**
		 * add ActionListener
		 */
		if(hasAttachments == true) {
			downloadAllBtn.addActionListener(this);
		}
		// 
		replyBtn.addActionListener(this);
		forwardBtn.addActionListener(this);
		replyAllBtn.addActionListener(this);
	}
	
	// initComps
	private void initComps() {
		checkHasAttachments();
		createdComps();
		initTopLayout();
		initMiddelLayout();
		initBottomLayout();
		initWholeLayout();
		
	}
	// initNormSetting
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

	// ****************************************************
	
	/**
	 * MouseListener
	 */
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

	// *****************************************************
	
	/**
	 * ActioinListener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * problem: 不能回复或者转发原邮件的附件
		 */
		if(e.getSource() == replyBtn || e.getSource() == forwardBtn ||
				e.getSource() == replyAllBtn) {

			boolean flag;
			
			int selectID = JOptionPane.showConfirmDialog(null, 
					"开始写邮件？", "等待确认",
					JOptionPane.YES_NO_OPTION);
			if (selectID == JOptionPane.OK_OPTION) {
				/**
				 * normal reply
				 */
				if(e.getSource() == replyBtn) {
					message.setForwardFlag(EnumType.DefaultReplyFlag);
				/** 
				 * reply all
				 */
				} else if(e.getSource() == replyAllBtn){
					message.setForwardFlag(EnumType.DefaultReplyAllFlag);
				/** 
				 * forward to others
				 */
				} else if(e.getSource() == forwardBtn){
					message.setForwardFlag(EnumType.DefaultForwardFlag);
				}
				
				// send
				Thread thread = new Thread() {
					@Override  
                    public void run() {  
                      new SendedUITest(message);
                    }  
				};
				thread.start();
			} else {
				JOptionPane.showMessageDialog(null, " *** 已经取消回复或者转发 *** ");
			}
		/**
		 * downloadAllBtn
		 */
		}  else if(e.getSource() == downloadAllBtn) {
			Thread thread = new Thread() {
				@Override  
                public void run() {  
                  downloadsAllAttachments();
                }
			};
			thread.start();
		}
	}
	
	// ******************************************************
	
	/**
	 * get stored path
	 */
	private String getStoredPath() {
		// 
		storedPathJL.setText("");
		fileJFC.setApproveButtonText("确定");
		fileJFC.setDialogTitle(" *** 随便选择一个文件 *** ");
		
		int selectedIdx = fileJFC.showOpenDialog(this);
		File file = null;
		
		if (selectedIdx == JFileChooser.APPROVE_OPTION) {
			file = fileJFC.getSelectedFile();
			storedPathJL.setText(EnumType.DefaultFileDirChoosedMsg);
			storedPathJL.setForeground(Color.PINK);
		} else if (selectedIdx == JFileChooser.CANCEL_OPTION) {
			storedPathJL.setText(EnumType.DefaultFileDirNotChoosedMsg);
			storedPathJL.setForeground(Color.red);
			return null;
		}
		
		String absolutePath = file.getAbsolutePath();
		if(absolutePath.lastIndexOf(PathManager.getDirSplitStr()) == -1){
			JOptionPane.showMessageDialog(null, "file stored at: " +
					PathManager.getDownloadResourcePath());
			storedPathJL.setText("  附件(*可能重复*)将保存在:　" + 
					PathManager.getDownloadResourcePath());
			storedPathJL.setForeground(Color.red);
			
			return PathManager.getDownloadResourcePath(); 
		}
		
		absolutePath = absolutePath.substring(0, 
				absolutePath.lastIndexOf(PathManager.getDirSplitStr())+ 1);
		storedPathJL.setText("  附件将保存在:　" + absolutePath);
		storedPathJL.setForeground(Color.red);
		
		return absolutePath;
	}
	/**
	 * downloads all attachments
	 */
	private void downloadsAllAttachments() {
		downloads(null);
	}
	
	private void downloads(String singleFileName) {
		// get seleted path
		String seletedPath = getStoredPath();
		if(seletedPath == null) {
			storedPathJL.setText(EnumType.CancledDownloadAllFilesMsg);
			storedPathJL.setForeground(Color.red);
			return ;
		}
		
		EmailLogger.info("downloads - singleFileName: " + singleFileName);
		// download
		try {
			seletedPath = seletedPath.trim();
			boolean flag = ReceivedPoP3Processor.saveAttachments(message.
					getReceivedIdx(), seletedPath, singleFileName);
			if(flag == true) {
				// set notes
				if(singleFileName != null) {
					storedPathJL.setText("  " + singleFileName 
							+ " 已保存在:　" + seletedPath);
				} else {
					storedPathJL.setText("  全部附件已保存在:　" 
							+ seletedPath);
				}
				storedPathJL.setForeground(Color.red);
			}
		} catch (IOException | MessagingException e1) {
			e1.printStackTrace();
		}
	}
	// ******************************************************
	
	/**
	 * sets & gets
	 */
	// message
	public EmailMessage getEmailMessage() {
		return message;
	}
	
	public void setEmailMessage(EmailMessage message) {
		this.message = message;
	}
}