package UI.Recieves.PreView;

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

import javax.mail.Message;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import UI.Main.MainUI;
import UI.Recieves.PreView.PreViewReceivedUI;
import UI.Recieves.View.ReceivedUITest;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import EmailProcessor.Recieves.ReceivedPoP3Processor;
import Util.Email.PathManager;
import Util.Email.Logging.EmailLogger;

public class PreReceivedUI extends JPanel {
	/**
	 * variables
	 */
	private int totalMsgLen;			// record the length of whole messages
	private int pageNum;				// record the current page number
	private static final int DefaultPerMsgsLen = 
			EnumType.DefaultPreViewPerMsgsLen; // show default value: 10
	private int perMsgsLen;		// the maximum of messages in each page 
	private int realPageMsgLen;			// current number of messages in current page
	private boolean refresh;
	private EmailMessage[] messages;
	/**
	 * Variables
	 */
	JLabel pageJLabel;
	/**
	 * JButton
	 */
	JButton preBtn;
	JButton nextBtn;
	/**
	 * JPanel
	 */
	JPanel bottomJPanel = null;
	JPanel topJPanel = null;
	/**
	 * JScrollPane
	 */
	JScrollPane topJScrollPane;
	/**
	 * PreViewReceivedUI
	 */
	PreViewReceivedUI[] preViewReceivedUI = null;
	
	// ************************************************************
	
	public PreReceivedUI() {
		pageNum = 0;
		perMsgsLen = DefaultPerMsgsLen;
		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		setVisible(true);
	}
	
	public PreReceivedUI(EmailMessage[] messages, int perMsgsLen) {	
		this.pageNum = 0;
		this.totalMsgLen = messages.length;
		this.messages = messages;
		this.perMsgsLen = perMsgsLen;
		
		setLayout(new BorderLayout());
		//
		setInits();
		
		setBackground(Color.WHITE);
		setVisible(true);
	}
	
	// **************************************************************
	
	/**
	 * addPreViewReceivedUI
	 */
	private void addPreViewReceivedUI(boolean refresh) {
		//
		realPageMsgLen = totalMsgLen > perMsgsLen 
				? perMsgsLen : totalMsgLen;
		EmailLogger.info("realPageMsgLen: " + realPageMsgLen);
		
		// rows, cols - include JLable and JSplitPane
		if(topJPanel == null) {
			topJPanel = new JPanel(new GridLayout(this.realPageMsgLen - 1, 1));
		}
		if(refresh == true) {
			topJPanel.removeAll();
			// return;
		}
		// topJPanel = new JPanel(new GridLayout(this.realPageMsgLen - 1, 1));
		
		preViewReceivedUI = new PreViewReceivedUI[realPageMsgLen];
		
		// create PreViewReceivedUI
		for(int idx = 0; idx < realPageMsgLen; idx++) {
			EmailMessage msg = messages[idx];
			if(msg == null) {
				EmailLogger.warning("message is null");
				continue;
			}
			// instance
			preViewReceivedUI[idx] = new PreViewReceivedUI(msg, 
					new PreViewReceivedUI.CallBack() {
						@Override
						public void call(final EmailMessage msg) {
							// 
							// msg as parameter for ReceivedUI
							// MainUI.getInitedMainUIInstance().
							//	showReceivedUIPanelEmailCardLayout(msg);
							
							Thread thread = new Thread() {
								@Override  
			                    public void run() {  
			                      new ReceivedUITest(msg);
			                    }  
							};
							thread.start();
						}
				}
			);
		}
		// add PreViewReceivedUI and JSplitPane
		for(int idx = 0; idx < realPageMsgLen; idx++) {
			// add
			topJPanel.add(preViewReceivedUI[idx]);	
			
			if(idx == 0) {
				continue;
			}
			
			//��ӷָ���  
			JSplitPane jSpeparator = new JSplitPane(
					JSplitPane.VERTICAL_SPLIT, preViewReceivedUI[idx - 1], 
					preViewReceivedUI[idx]); 
			
			//���÷ָ��߿��  
			final int thickness = 6;
			jSpeparator.setDividerSize(thickness);  
			
			//ʹ�ָ������������϶�  
			jSpeparator.setEnabled(false);
			jSpeparator.setBackground(Color.red);
			jSpeparator.setVisible(true);
			topJPanel.add(jSpeparator);
		}
	}
	/**
	 * addTopComps
	 */
	private void addTopComps() {
		// add pre-viewed received email UI
		addPreViewReceivedUI(false);
		
		// topJScrollPane.add(topJPanel);	// error !!
		topJScrollPane = new JScrollPane(topJPanel);
		add(topJScrollPane, BorderLayout.CENTER);
	}
	/**
	 * addBottomComps
	 */
	private void addBottomComps() {
		// Button
		preBtn = new JButton("Pre");
		nextBtn = new JButton("Next");
		//
		preBtn.setEnabled(false);
		if(totalMsgLen < perMsgsLen) {
			nextBtn.setEnabled(false);
		}
		
		// bottom JPanel
		bottomJPanel = new JPanel();
		bottomJPanel.add(preBtn);
		String pageString = "(" + pageNum + "ҳ) - [" 
				+ (pageNum * perMsgsLen) +  ": " 
				+ (pageNum * perMsgsLen + realPageMsgLen - 1)
				+ "]";
		pageJLabel = new JLabel(pageString);
		bottomJPanel.add(pageJLabel);
		bottomJPanel.add(nextBtn);
		
		// 
		add(bottomJPanel, BorderLayout.SOUTH);
		//
		JOptionPane.showMessageDialog(null, "��ʼ���ʼ�ҳ��ɹ�");
	}
	/**
	 * addComps()
	 */
	private void addComps() {
		addTopComps();
		addBottomComps();
	}
	/**
	 * Listeners
	 */
	private void addCompsListeners() {
		// Pre
		preBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(pageNum <=0) {
					JOptionPane.showMessageDialog(null, 
							"�Ѿ�������һҳ��");
					preBtn.setEnabled(false);
					if(realPageMsgLen < perMsgsLen) {
						nextBtn.setEnabled(false);
					} else {
						nextBtn.setEnabled(true);
					}
					
					return;
				}
				//
				pageNum--;
				
				boolean isGetAttachment = EnumType.DefaultIsGetAttachment;
				EmailMessage[] messageList = null;
				try {
					boolean isNotepad = false;
					messageList = ReceivedPoP3Processor.
							getReceivedPoP3ProcessorInstance().
							receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(messageList == null || messageList.length < 1) {
					JOptionPane.showMessageDialog(null, "��ȡ�ʼ�ʧ��");
					// set back
					pageNum++;
					if(pageNum != 0) {
						preBtn.setEnabled(true);
					}
					// nextBtn.setEnabled(false);
					return;
				}
				// log & set messages
				EmailLogger.info("***********************");
				EmailLogger.info("�ɹ���ȡ�ʼ�");
				EmailLogger.info("��" + pageNum + "ҳ, �ʼ�����: " 
						+ messageList.length);
				
				setEmailMessage(messageList);
				setRealPageMsgLen(messageList.length);
				
				// refresh
				boolean refresh = true;
				addPreViewReceivedUI(refresh);
				//
				String pageString = "(" + pageNum + "ҳ) - [" 
						+ (pageNum * perMsgsLen) +  ": " 
						+ (pageNum * perMsgsLen + realPageMsgLen - 1)
						+ "]";
				pageJLabel.setText(pageString);
				nextBtn.setEnabled(true);
				setVisible(true);
				JOptionPane.showMessageDialog(null, "��ת�ɹ�");
			}
		});
	
		// Next
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//
				if(realPageMsgLen < perMsgsLen) {
					JOptionPane.showMessageDialog(null, 
							"�Ѿ������һҳ��");
					nextBtn.setEnabled(false);
					return;
				} else {
					nextBtn.setEnabled(true);
				}
				
				//
				pageNum++;
				
				boolean isGetAttachment = EnumType.DefaultIsGetAttachment;
				EmailMessage[] messageList = null;
				try {
					boolean isNotepad = false;
					messageList = ReceivedPoP3Processor.
							getReceivedPoP3ProcessorInstance().
							receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(messageList == null || messageList.length < 1) {
					JOptionPane.showMessageDialog(null, "��ȡ�ʼ�ʧ��");
					// set back
					pageNum--;
					if(pageNum != 0) {
						preBtn.setEnabled(true);
					}
					// nextBtn.setEnabled(false);
					return;
				}
				
				EmailLogger.info("***********************");
				EmailLogger.info("�ɹ���ȡ�ʼ�");
				EmailLogger.info("��" + pageNum + "ҳ, �ʼ�����: " 
						+ messageList.length);
				
				setEmailMessage(messageList);
				setRealPageMsgLen(messageList.length);
				
				// refresh
				boolean refresh = true;
				addPreViewReceivedUI(refresh);
				//
				String pageString = "(" + pageNum + "ҳ) - [" 
						+ (pageNum * perMsgsLen) +  ": " 
						+ (pageNum * perMsgsLen + realPageMsgLen)
						+ "]";
				pageJLabel.setText(pageString);
				//
				preBtn.setEnabled(true);
				setVisible(true);
				JOptionPane.showMessageDialog(null, "��ת�ɹ�");
			}
		});
	}
	/**
	 * setInits
	 */
	public void setInits() {
		addComps();
		addCompsListeners();
	}
	/**
	 * RepaintView
	 */
	public void RepaintView() {
		pageNum = 0;
		
		boolean isGetAttachment = EnumType.DefaultIsGetAttachment;
		EmailMessage[] messageList = null;
		try {
			boolean isNotepad = false;
			messageList = ReceivedPoP3Processor.
					getReceivedPoP3ProcessorInstance().
					receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(messageList == null || messageList.length < 1) {
			JOptionPane.showMessageDialog(null, "��ȡ�ʼ�ʧ��");

			preBtn.setEnabled(false);
			nextBtn.setEnabled(false);
			return;
		}
		
		// log & set messages
		EmailLogger.info("***********************");
		EmailLogger.info("�ɹ���ȡ�ʼ�");
		EmailLogger.info("��" + pageNum + "ҳ, �ʼ�����: " 
				+ messageList.length);
		
		setEmailMessage(messageList);
		setRealPageMsgLen(messageList.length);
		
		// refresh
		boolean refresh = true;
		addPreViewReceivedUI(refresh);
		
		if(totalMsgLen < perMsgsLen) {
			nextBtn.setEnabled(false);
		} else {
			nextBtn.setEnabled(true);
		}
		//
		String pageString = "(" + pageNum + "ҳ) - [" 
				+ (pageNum * perMsgsLen) +  ": " 
				+ (pageNum * perMsgsLen + realPageMsgLen - 1)
				+ "]";
		pageJLabel.setText(pageString);
		//
		setVisible(true);
		JOptionPane.showMessageDialog(null, "��ʼ���ʼ�ҳ��ɹ�");
	}
	
	// **********************************************************
	
	/**
	 * gets & sets
	 */
	// EmailMessage
	public EmailMessage[] getEmailMessages() {
		return messages;
	}
	
	public void setEmailMessage(EmailMessage[] messages) {
		this.totalMsgLen = messages.length;
		this.messages = messages;
	}
	// pageNum
	public int getPaeNum() {
		return this.pageNum;
	}
	
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	} 
	// perMsgsLen
	public void setPerMsgsLen(int perMsgsLen) {
		this.perMsgsLen = perMsgsLen;
	}
	// realPageMsgLen
	public int getRealPageMsgLen() {
		return this.realPageMsgLen;
	}
	// realPageMsgLen
	public void setRealPageMsgLen(int realPageMsgLen) {
		this.realPageMsgLen = realPageMsgLen;
	}
	
	public int getPerMsgsLen() {
		return this.perMsgsLen;
	}
}