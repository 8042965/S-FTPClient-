package UI;

import BLL.*;
import ftpController.FTPController;
import ftpController.FTPTableModel;
import ftpController.ftpReadSocket;
import util.ZHFileBase64Key;
import util.ZHFileCipherTxst;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import view.ZFTP_JMenu;
import java.awt.Component;
import javax.swing.JTable;

public class MainForm extends JFrame   implements ActionListener{
	
	public static MainForm _instance;
	JPanel SearchPanel, ShowPanel, FunctPanel, TreePanel;
	private JPanel FunctPanel_1;
	JTree BigTree;
	FilesTree filesTree;
	JScrollPane ScrollShow, TreeShow;
    DefaultMutableTreeNode node;
	JButton GoBtn;
	ButtonGroup Classify;
	JTextField GuideText;
	String Sort_Items[] = {"�ļ���С","�޸�ʱ��","����ĸ"};
	String Sort_Type_Items[] = {"����","����"};
	public String Cur_URL = "";
	String Pre_URL = "";
	String LatURL = "";
	public Map<String, String> Maps = new HashMap<String,String>();
	
	//�ļ��б����ر���
	JList<String> list;
	public DefaultListModel defaultListModel;
	public Stack<String> stack, stack_return;
	JPopupMenu jPopupMenu = null;
	JPopupMenu jPopupMenu2 = null;
	JPopupMenu jPopupMenu3 = null;
	JMenuItem[] JMIs = new JMenuItem[10];
	JMenuItem[] JMIs2 = new JMenuItem[5];
	JMenuItem delete = new JMenuItem("ɾ��");
	public Icon[] AllIcons = new Icon[999999];//�洢�����õ����ļ�ͼ��
	public int Icon_Counter = 0;
	//����GB,MB,KB,B��Ӧ���ֽ��������㻻���ļ���С����λ
	long[] Sizes = {1073741824,1048576,1024,1};
	String[] Size_Names = {"GB", "MB", "KB", "B"};
	Boolean isSearching = false;
	private JTextField ftp_host;
	private JTextField ftp_port;
	private JTextField ftp_userName;
	private JTextField ftp_password;
	
    public JTextArea ftp_message = new JTextArea();//ftp��Ϣ
    private JTable table;
    // ��ȡ������������
    FTPTableModel ftpTableModel;
	
    
    ftpReadSocket frs =null;//ftp���ӵ�socket
    
    JScrollPane scrollPane_1 = null;
    private JButton btnNewButton_1;
    
    public void setFTPMessage(String message) {
    	ftp_message.append(message);
    	ftp_message.append("\r\n");
    }
    
	public MainForm(){//������
		this._instance = this;
		this.setTitle("ZH(֣��)-FTP�ͻ���-v0.6");
		this.setBounds(500, 500, 1513, 921);
		this.getContentPane().setLayout(null);
		Init();//��ʼ��
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	//��ʼ��
	public void Init(){
		ftp_userName = new JTextField();
		 
	   scrollPane_1 = new JScrollPane((Component) null);
       scrollPane_1.setBounds(5, 0, 914, 491);
		
		SearchPanel = new JPanel();
		ShowPanel = new JPanel();
		FunctPanel = new JPanel();
		TreePanel = new JPanel();
		
		Classify = new ButtonGroup();
	   
		 //���ϵ�����35
        FunctPanel_1 = new JPanel();
        FunctPanel_1.setBounds(5, 114, 516, 45);
        FunctPanel_1.setLayout(null);
        GuideText = new JTextField();
        GuideText.setBounds(64, 13, 362, 25);
        GuideText.addActionListener(this);
        GoBtn = new JButton("Go!");
        GoBtn.setFont(new Font("Serif", Font.PLAIN, 15));
        GoBtn.setBounds(440, 13, 65, 25);
        GoBtn.addActionListener(this);
        FunctPanel_1.add(GuideText);
        FunctPanel_1.add(GoBtn);
        this.getContentPane().add(FunctPanel_1);
        
        JLabel label = new JLabel("\u672C\u5730:");
        label.setFont(new Font("΢���ź�", Font.PLAIN, 19));
        label.setBounds(14, 13, 52, 18);
        FunctPanel_1.add(label);
        
		//�в��ļ��б�
        stack = new Stack<String>();
        stack_return = new Stack<String>();
        ShowPanel.setSize(291, 491);
        ShowPanel.setLocation(198, 172);
        ShowPanel.setLayout(null);    
        list = new JList<String>();
        list.setValueIsAdjusting(true);
        list.setSize(10, 50);
        jPopupMenu = new JPopupMenu();//�ļ�/�ļ��е����Բ˵�
        jPopupMenu2 = new JPopupMenu();//���̵����Բ˵�
        JMIs[0] = new JMenuItem("��");
        JMIs[1] = new JMenuItem("ɾ��");
        JMIs[2] = new JMenuItem("������");
        JMIs[3] = new JMenuItem("����");
        JMIs[4] = new JMenuItem("�ϴ�");
        for(int k = 0; k < 5; ++k){//�ļ�/�ļ��е����Բ˵���ʼ��
        	JMIs[k].addActionListener(this);
        	jPopupMenu.add(JMIs[k]);            	
        }        
        JMIs2[0] = new JMenuItem("��");
        JMIs2[1] = new JMenuItem("����");
        for(int k = 0; k < 2; ++k){//���̵����Բ˵���ʼ��
        	JMIs2[k].addActionListener(this);
        	jPopupMenu2.add(JMIs2[k]);            	
        }    
        jPopupMenu3 = new JPopupMenu();
        delete.addActionListener(this);
        jPopupMenu3.add(delete);
        list.add(jPopupMenu3);
        list.add(jPopupMenu2);
        list.add(jPopupMenu);
        
        Home_List();//��ʾ���̸�Ŀ¼
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(list.getSelectedIndex() != -1){
					if(e.getClickCount() == 1){//����listʱ�������¼�
					
					}else if(e.getClickCount() == 2){//˫��listʱ�����ļ���������Ŀ¼
						System.out.println(list.getSelectedValue());
						twoClick(list.getSelectedValue());												
					}
					if(e.getButton() == 3){//�һ�listʱ���򿪲˵���
						if(Cur_URL != ""){
							if(list.getSelectedValuesList().size() == 1){
								jPopupMenu.show(list,e.getX(),e.getY()); //����һ����ǵ����ļ��к��ļ�����Ӧ��һ��������ȫ�Ĳ˵���
							}else if(list.getSelectedValuesList().size() > 1){//���ѡ�ж���ļ��к��ļ�����ֻ֧��ɾ������
								jPopupMenu3.show(list, e.getX(), e.getY());
							}
						}		                 
						else if(Cur_URL == "" && list.getSelectedValuesList().size() == 1){
							jPopupMenu2.show(list, e.getX(), e.getY()); //����һ����Ǵ��̣��˵�����ֻ���С��򿪡��͡����ԡ�����
						}						
					}
				}
			}
		});	
	        
		ScrollShow = new JScrollPane(list);
		ShowPanel.add(ScrollShow);
		ScrollShow.setSize(277, 478);
		ScrollShow.setLocation(5, 13);
		this.getContentPane().add(ShowPanel);
		
		//���Ŀ¼��״ͼ
        TreePanel.setSize(190,491);
        TreePanel.setLocation(5, 172);
        TreePanel.setLayout(null); 
        filesTree = new FilesTree();
        TreeShow = new JScrollPane(filesTree);
        TreeShow.setBounds(5, 13, 185, 478);
        TreePanel.add(TreeShow);
        this.getContentPane().add(TreePanel);
        
      
        
        JPanel panel_2 = new JPanel();
        panel_2.setBorder(BorderFactory.createLoweredBevelBorder());  
        panel_2.setLayout(null);
        panel_2.setBounds(0, 13, 1507, 77);
        this.getContentPane().add(panel_2);
        
        JLabel label_ftp_host = new JLabel("\u5730\u5740\uFF1A");
        label_ftp_host.setBounds(14, 30, 45, 18);
        panel_2.add(label_ftp_host);
        
        ftp_host = new JTextField();
        ftp_host.setColumns(10);
        ftp_host.setBounds(58, 26, 157, 26);
        panel_2.add(ftp_host);
        
        JLabel label_ftp_port = new JLabel("\u7AEF\u53E3\uFF1A");
        label_ftp_port.setBounds(218, 25, 45, 27);
        panel_2.add(label_ftp_port);
        
        ftp_port = new JTextField();
        ftp_port.setColumns(10);
        ftp_port.setBounds(263, 25, 56, 26);
        panel_2.add(ftp_port);
        
        JLabel label_ftp_userName = new JLabel("\u7528\u6237\u540D\uFF1A");
        label_ftp_userName.setBounds(333, 25, 74, 27);
        panel_2.add(label_ftp_userName);
        
       
        ftp_userName.setColumns(10);
        ftp_userName.setBounds(400, 25, 145, 26);
        panel_2.add(ftp_userName);
        
        JLabel label_ftp_password = new JLabel("\u5BC6\u7801\uFF1A");
        label_ftp_password.setBounds(558, 25, 45, 27);
        panel_2.add(label_ftp_password);
        
        ftp_password = new JTextField();
        ftp_password.setColumns(10);
        ftp_password.setBounds(604, 25, 145, 26);
        panel_2.add(ftp_password);
        
        JButton lianjie = new JButton("\u8FDE\u63A5");
        lianjie.setIcon(new ImageIcon("E:\\eclipse-workspace\\S-FTP\\image\\lianjie.png"));
        lianjie.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String host = ftp_host.getText(); //ftp��ַ
        		String port = ftp_port.getText();//�˿ں�
        		String userName = ftp_userName.getText();//�û���
        		String password = ftp_password.getText();//����
        		
        		//�����ǰû�����ӣ�����������
        		if(frs==null) {
	        		frs = new ftpReadSocket();
	        		frs.setMain(_instance);
	        		
	        		if(host.equals("")) {
	        			 // ��Ϣ�Ի����޷���, ����֪ͨ����
	        			JOptionPane.showMessageDialog(null, "������ftp��ַ", "����������", JOptionPane.ERROR_MESSAGE);
	        		}else {
        			    String name = ftp_userName.getText();
        		        if (name.equals("")) {
        					name = "anonymous";
        				}
        		        
        		        ftpTableModel = new FTPTableModel(name);
	        		        
	        			System.out.println(host+","+port+","+userName+","+password);
	        			//��ַ��Ϊ��  �˿�Ϊ�� �˺�Ϊ��  ����Ϊ��
	        			if(!host.equals("") && port.equals("") && userName.equals("")  && password.equals("")) {
	        				System.out.println("�ߵ�1·");
        					frs.connect(host);
        				//��ַ��Ϊ��  �˿ڲ�Ϊ�� �˺�Ϊ��  ����Ϊ��
	        			}else if(!host.equals("") && !port.equals("") && userName.equals("")  && password.equals("")) {
	        				System.out.println("�ߵ�2·");
	        				if(isNumeric(port)) {//�ж϶˿ں��ǲ�������
	        					frs.connect(host, Integer.valueOf(port));
	        				}else {
	        					JOptionPane.showMessageDialog(null, "�˿�����Ĳ���", "����������", JOptionPane.ERROR_MESSAGE);
	        				}
        				//��ַ��Ϊ��  �˿ڲ�Ϊ�� �˺Ų�Ϊ��  ���벻Ϊ��
	        			}else if(!host.equals("") && !port.equals("") && !userName.equals("")  && !password.equals("")) {
	        				System.out.println("�ߵ�3·");
	        				if(isNumeric(port)) {//�ж϶˿ں��ǲ�������
	        					frs.connect(host, Integer.valueOf(port),userName,password);
	        				}else {
	        					JOptionPane.showMessageDialog(null, "�˿�����Ĳ���", "����������", JOptionPane.ERROR_MESSAGE);
	        				}
	        			}
	        			
	        			
	        			try {
							frs.listFiles("/",ftpTableModel);
							ftpTableModel.getFTPTableData();
							// JTable
					        table = new JTable(ftpTableModel);
					        scrollPane_1.setViewportView(table);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	        		}
        		}else {
        			//�����ǰ�����ӣ����ȶϿ�����������
        			frs.closeAll();
	        		frs = new ftpReadSocket();
	        		frs.setMain(_instance);
	        		
	        		if(host.equals("")) {
	        			 // ��Ϣ�Ի����޷���, ����֪ͨ����
	        			JOptionPane.showMessageDialog(null, "������ftp��ַ", "����������", JOptionPane.ERROR_MESSAGE);
	        		}else {
        			    String name = ftp_userName.getText();
        		        if (name.equals("")) {
        					name = "anonymous";
        				}
        		        
        		        ftpTableModel = new FTPTableModel(name);
	        		        
        		        System.out.println(host+","+port+","+userName+","+password);
	        			//��ַ��Ϊ��  �˿�Ϊ�� �˺�Ϊ��  ����Ϊ��
	        			if(!host.equals("") && port.equals("") && userName.equals("")  && password.equals("")) {
	        				System.out.println("�ߵ�1·");
        					frs.connect(host);
        				//��ַ��Ϊ��  �˿ڲ�Ϊ�� �˺�Ϊ��  ����Ϊ��
	        			}else if(!host.equals("") && !port.equals("") && userName.equals("")  && password.equals("")) {
	        				System.out.println("�ߵ�2·");
	        				if(isNumeric(port)) {//�ж϶˿ں��ǲ�������
	        					frs.connect(host, Integer.valueOf(port));
	        				}else {
	        					JOptionPane.showMessageDialog(null, "�˿�����Ĳ���", "����������", JOptionPane.ERROR_MESSAGE);
	        				}
        				//��ַ��Ϊ��  �˿ڲ�Ϊ�� �˺Ų�Ϊ��  ���벻Ϊ��
	        			}else if(!host.equals("") && !port.equals("") && !userName.equals("")  && !password.equals("")) {
	        				System.out.println("�ߵ�3·");
	        				if(isNumeric(port)) {//�ж϶˿ں��ǲ�������
	        					frs.connect(host, Integer.valueOf(port),userName,password);
	        				}else {
	        					JOptionPane.showMessageDialog(null, "�˿�����Ĳ���", "����������", JOptionPane.ERROR_MESSAGE);
	        				}
	        			}
	        			
	        			
	        			try {
							frs.listFiles("/",ftpTableModel);
							ftpTableModel.getFTPTableData();
							// JTable
					        table = new JTable(ftpTableModel);
					        scrollPane_1.setViewportView(table);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	        		}
	        		
        		}
        	}
        });
        lianjie.setBounds(776, 13, 115, 51);
        panel_2.add(lianjie);
        
        JButton duankai = new JButton("\u65AD\u5F00");
        duankai.setBounds(905, 13, 115, 52);
        panel_2.add(duankai);
        duankai.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frs.closeAll();
        	}
        });
        duankai.setIcon(new ImageIcon("E:\\eclipse-workspace\\S-FTP\\image\\duankai.png"));
        
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setBounds(528, 70, 22, 607);
        this.getContentPane().add(separator);
        
        JSeparator separator_1 = new JSeparator();
        separator_1.setToolTipText("");
        separator_1.setBounds(-148, 676, 1655, 20);
        this.getContentPane().add(separator_1);
        
        JPanel lianjie_ftp_file_pan = new JPanel();
        lianjie_ftp_file_pan.setLayout(null);
        lianjie_ftp_file_pan.setBounds(574, 177, 919, 491);
        this.getContentPane().add(lianjie_ftp_file_pan);
        
       
        lianjie_ftp_file_pan.add(scrollPane_1);
        
        JPanel panel_6 = new JPanel();
        panel_6.setLayout(null);
        panel_6.setBounds(5, 686, 1175, 187);
        getContentPane().add(panel_6);
        
        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(0, 0, 1175, 187);
        panel_6.add(scrollPane_2);
        
       
        scrollPane_2.setViewportView(ftp_message);
        
        JButton qingkong = new JButton("\u6E05\u7A7A");
        qingkong.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ftp_message.setText(" ");
        	}
        });
        qingkong.setBounds(1194, 681, 92, 46);
        getContentPane().add(qingkong);
        
        JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        panel_3.setBounds(574, 103, 933, 68);
        getContentPane().add(panel_3);
        
        JButton btnNewButton = new JButton("\u4E0B\u8F7D");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {

        		if(ftpTableModel.getSelectAllSum()==0) {
        			JOptionPane.showMessageDialog(null, "��ѡ����Ҫ���ص��ļ���������", "�������ϡ�", JOptionPane.ERROR_MESSAGE);
        		}else {
        			JPanel p = new JPanel();
        			JFileChooser chooser;
        			String choosertitle = "ѡ������λ��";
        			
        			int result;
    				chooser = new JFileChooser();
    				chooser.setCurrentDirectory(new java.io.File("."));
    				chooser.setDialogTitle(choosertitle);
//    				System.out.println("---" + choosertitle);
    				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    				chooser.setAcceptAllFileFilterUsed(false);
    				
    				if (chooser.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {
    					FTPController ftp = getFtpControoler();
            			String[] selectAllInfo = ftpTableModel.getSelectAllInfo();
            			for (int i = 0; i < selectAllInfo.length; i++) {
            				//linuxѧϰ·��.docx
            				//������#SDJZDX_zhenghui��Ϊ�ָ���
            				String userName = ftp_userName.getText();
        					if(userName.equals("")) {
        						userName = "anonymous";
        					}
        					String jiamiqian = selectAllInfo[i]+"#SDJZDX_zhenghuiagsdahscasdqwFTP"+userName;
        					String base64jiami_fileName = ZHFileBase64Key.base64jiami(jiamiqian.getBytes());
            				//һֱ�ڸ�Ŀ¼
    						ftp.downloadFile("/", base64jiami_fileName, String.valueOf(chooser.getSelectedFile()));
    					}
    				} else {
    					System.out.println("No Selection ");
    				}
    				
        		}
        		
        	}
        });
        btnNewButton.setIcon(new ImageIcon("E:\\eclipse-workspace\\S-FTP\\image\\xiazai.png"));
        btnNewButton.setBounds(277, 13, 115, 51);
        panel_3.add(btnNewButton);
        
        JButton shuaxin = new JButton("\u5237\u65B0");
        shuaxin.setBounds(406, 12, 115, 52);
        panel_3.add(shuaxin);
        shuaxin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
		         //ˢ��ftp�б��ļ�
        		shuaxin();
        		
        	}
        });
        shuaxin.setIcon(new ImageIcon("E:\\eclipse-workspace\\S-FTP\\image\\shuaxin.png"));
        shuaxin.setFocusPainted(false);
        shuaxin.setBorderPainted(false);
        
        btnNewButton_1 = new JButton("\u5220\u9664");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(ftpTableModel == null) {
        			JOptionPane.showMessageDialog(null, "���ȵ�¼", "�������ϡ�", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		if(ftpTableModel.getSelectAllSum()==0) {
        			JOptionPane.showMessageDialog(null, "��ѡ����Ҫɾ�����ļ�����ɾ��", "�������ϡ�", JOptionPane.ERROR_MESSAGE);
        		}else {
        		
    				FTPController ftp = getFtpControoler();
        			String[] selectAllInfo = ftpTableModel.getSelectAllInfo();
        			//ѭ��ɾ������ѡ�е��ļ�
        			for (int i = 0; i < selectAllInfo.length; i++) {
        				//��ȡѡ�е��ļ��� 
        				String selectName = selectAllInfo[i];
        				String selectNameKey = ftpTableModel.getSelectFileName(selectName);
        				
        				String userName = ftp_userName.getText();
    					if(userName.equals("")) {
    						userName = "anonymous";
    					}
        				
        				/************���ܹ���******************/
    					//������#SDJZDX_zhenghui��Ϊ�ָ���
    					String jiamiqian = selectName+"#SDJZDX_zhenghuiagsdahscasdqwFTP"+userName;
    					String base64jiami_fileName = ZHFileBase64Key.base64jiami(jiamiqian.getBytes());
        				
        				//ɾ����Ŀ¼�µ��ļ�
        				boolean deleteFile = ftp.deleteFile( "/",base64jiami_fileName);
        			}
        			shuaxin();
        		}
        		
        	}
        });
        btnNewButton_1.setIcon(new ImageIcon("E:\\eclipse-workspace\\S-FTP\\image\\shanchu.png"));
        btnNewButton_1.setBounds(535, 13, 115, 51);
        panel_3.add(btnNewButton_1);
	}
	
	
	//ˢ��ftp���ļ��б�
	public void shuaxin() {
		//�����ǰ�����ӣ����ȹر�Ȼ����������
		if(frs!=null) {
			frs.closeAll();
			
			String host = ftp_host.getText(); //ftp��ַ
    		String port = ftp_port.getText();//�˿ں�
    		String userName = ftp_userName.getText();//�û���
    		String password = ftp_password.getText();//����
    		
    		frs = new ftpReadSocket();
    		frs.setMain(_instance);
    		
    		if(host.equals("")) {
    			 // ��Ϣ�Ի����޷���, ����֪ͨ����
    			JOptionPane.showMessageDialog(null, "������ftp��ַ", "����������", JOptionPane.ERROR_MESSAGE);
    		}else {
    			String name = ftp_userName.getText();
		        if (name.equals("")) {
					name = "anonymous";
				}
		        
		        ftpTableModel = new FTPTableModel(name);
    		        
    			System.out.println(host+","+port+","+userName+","+password);
    			//��ַ��Ϊ��  �˿�Ϊ�� �˺�Ϊ��  ����Ϊ��
    			if(!host.equals("") && port.equals("") && userName.equals("")  && password.equals("")) {
    				System.out.println("�ߵ�1·");
					frs.connect(host);
				//��ַ��Ϊ��  �˿ڲ�Ϊ�� �˺�Ϊ��  ����Ϊ��
    			}else if(!host.equals("") && !port.equals("") && userName.equals("")  && password.equals("")) {
    				System.out.println("�ߵ�2·");
    				if(isNumeric(port)) {//�ж϶˿ں��ǲ�������
    					frs.connect(host, Integer.valueOf(port));
    				}else {
    					JOptionPane.showMessageDialog(null, "�˿�����Ĳ���", "����������", JOptionPane.ERROR_MESSAGE);
    				}
				//��ַ��Ϊ��  �˿ڲ�Ϊ�� �˺Ų�Ϊ��  ���벻Ϊ��
    			}else if(!host.equals("") && !port.equals("") && !userName.equals("")  && !password.equals("")) {
    				System.out.println("�ߵ�3·");
    				if(isNumeric(port)) {//�ж϶˿ں��ǲ�������
    					frs.connect(host, Integer.valueOf(port),userName,password);
    				}else {
    					JOptionPane.showMessageDialog(null, "�˿�����Ĳ���", "����������", JOptionPane.ERROR_MESSAGE);
    				}
    			}
    			
    			
    			try {
					frs.listFiles("/",ftpTableModel);
					// JTable
					ftpTableModel.getFTPTableData();
			        table = new JTable(ftpTableModel);
			        scrollPane_1.setViewportView(table);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    		}
		}
		
	}

	//�������ʱ���¼�
	public void twoClick(String choice){
		if(!isSearching){//�����ʱ��������״̬�����������ĵ������
			choice += "\\";		
			File file = new File(Cur_URL + choice);
			if(file.isDirectory()){
				Cur_URL += choice;	
				stack.push(Cur_URL);
				Go_There();
			}else{
				OpenIt(file);
			}
		}else{//�����������״̬���Ǿ�Ҫ��map����ȡ���ǵ�URL����Ϊ������˳�򶼴����ˣ��޷���һ��URL��Ӧ
			File file = new File(Maps.get(choice));
			OpenIt(file);
		}
	}
	
	//�ص���ʼ���̽���
	public void Home_List(){
		List<String> Disks = MemoryInfo.getDisk();
		defaultListModel = new DefaultListModel();
		for(int i = 0; i < Disks.size(); ++i){
			defaultListModel.addElement(Disks.get(i));
		}
		Icon[] icons = GetFileIcon.getSmallIcon("HOME");
		list.setCellRenderer(new MyCellRenderer(icons));
		GuideText.setText("");
		Cur_URL = "";
		stack.push(Cur_URL);
	}
	
	//���õ����еĳ��򡰴򿪡��ļ��ķ���
	public void OpenIt(File file){
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//��ȥ�ģ���ȥ�ģ�������ת������
	public void Go_There(){
			GuideText.setText(Cur_URL);
			if(Cur_URL != ""){//Cur_URL�ǿգ�������Ŀ��Ŀ¼
				defaultListModel.clear();
				String[] getString = GetFileNames.getFileName(Cur_URL);		
				for(int i = 0; i < getString.length; ++i){
					defaultListModel.addElement(getString[i]);		
				}	
				Icon[] icons = GetFileIcon.getSmallIcon(Cur_URL);
				list.setModel(defaultListModel);
				list.setCellRenderer(new MyCellRenderer(icons));
				
			}else{//Cur_URLΪ��ʱ������ת�ظ�Ŀ¼
				Home_List();
			}
	}
	
	//�������ܺ��ĺ���
//	public void GetAllResults(String path){
//		  if(path != ""){		    	
//				String[] getString = GetFileNames.getFileName(path);
//				for(int i = 0; i < getString.length; ++i){
//					File file = new File(path + getString[i] + "\\");						
//					if(file.isDirectory()){//�������ļ�����						
//						GetAllResults(path + getString[i] + "\\");
//					}else{
//						String prefix = getString[i].substring(getString[i].lastIndexOf('.') + 1);					
//						if(VideoType.contains(prefix) && Videos.isSelected()){//�ж��Ƿ�Ϊ��Ƶ�ļ�����Ƶ��ť��ѡ�У�����������ǵ���ʾĿ¼��
//							System.out.println(getString[i]);
//							Maps.put(getString[i], path + getString[i]);//��Maps�洢�ļ�����·���Ķ�Ӧ��ϵ
//							defaultListModel.addElement(getString[i]);
//							AllIcons[Icon_Counter++] = GetFileIcon.getSingleSmallIcon(path + getString[i]);
//						}else if(GraphType.contains(prefix) && Picture.isSelected()){//�ж��Ƿ�ΪͼƬ�ļ���ͼƬ��ť��ѡ�У�����������ǵ���ʾĿ¼��
//							Maps.put(getString[i], path + getString[i]);//��Maps�洢�ļ�����·���Ķ�Ӧ��ϵ
//							defaultListModel.addElement(getString[i]);
//							AllIcons[Icon_Counter++] = GetFileIcon.getSingleSmallIcon(path + getString[i]);
//						}else if(TxtType.contains(prefix) && Text.isSelected()){//�ж��Ƿ�Ϊ�ĵ��ļ����ĵ���ť��ѡ�У�����������ǵ���ʾĿ¼��
//							Maps.put(getString[i], path + getString[i]);//��Maps�洢�ļ�����·���Ķ�Ӧ��ϵ
//							defaultListModel.addElement(getString[i]);
//							AllIcons[Icon_Counter++] = GetFileIcon.getSingleSmallIcon(path + getString[i]);
//						}else if(MusicType.contains(prefix) && Music.isSelected()){
//							Maps.put(getString[i], path + getString[i]);//��Maps�洢�ļ�����·���Ķ�Ӧ��ϵ
//							defaultListModel.addElement(getString[i]);
//							AllIcons[Icon_Counter++] = GetFileIcon.getSingleSmallIcon(path + getString[i]);
//						}
//					}				
//				}
//		    }
//	}		
			
	public static void main(String[] args) {
        MainForm m = new MainForm();     
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		if(e.getSource() == PreBtn){//������
//			LatURL = Cur_URL;			
//			if(!stack.isEmpty()){
//				stack.pop();//ÿ�ӵ�ǰһ��Ŀ¼����֮ǰ��Ŀ¼ʱ��stack��Ҫ��ջ		
//				stack_return.push(Cur_URL);//����֮ǰ��Ŀ¼���뷵��ջstack_return
//				if(!stack.isEmpty()){					
//					Cur_URL = stack.peek();//��ջ�еõ���һ�����ʵ�Ŀ¼��������ǰĿ¼					
//				}
//				else{
//					Cur_URL = "";//���ջΪ�գ���˵��ǰ���Ǹ�Ŀ¼����ֱ���ÿ�
//				}
//				Go_There();
//			}
////			if(isSearching){//�����������״̬���Ǵ�ʱӦ�ý���
////				isSearching = false;
////				AllFiles.setSelected(true);
////			}
//		}
//		
//		else if(e.getSource() == LatBtn){//������
//			if(!stack_return.isEmpty()){//�����ߣ���ӷ���ջ����URL
//				Cur_URL = stack_return.peek();
//				stack_return.pop();
//				stack.push(Cur_URL);
//				Go_There();
//			}
////			if(isSearching){//�����������״̬���Ǵ�ʱӦ�ý���
////				isSearching = false;
////				AllFiles.setSelected(true);
////			}
//		}
		
//		else 
			if(e.getSource() == JMIs[0] || e.getSource() == JMIs2[0]){	//���ļ�/�ļ���/����
				if(!isSearching){
					String url = Cur_URL + list.getSelectedValue();
					if(Cur_URL != ""){
						url += "\\";
					}
					File file = new File(url);
					if(file.isDirectory()){
						twoClick(url);
					}else{
						OpenIt(file);				
					}
				}else{
					File file = new File(Maps.get(list.getSelectedValue()));
					OpenIt(file);
				}			
			} else if(e.getSource() == JMIs[1]){//ɾ��
				File file = new File(Cur_URL + "/" + list.getSelectedValue());
				int n;
				if(file.isFile()){
					n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ���ļ� " + file.getName() + " ô?", "�ļ�ɾ��",JOptionPane.YES_NO_OPTION);
				}else{
					n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ�� " + file.getName() + " ����Ŀ¼�µ��ļ�ô?", "�ļ���ɾ��",JOptionPane.YES_NO_OPTION);
				}
				if(n == 0){
					FileDelete.delete(Cur_URL + list.getSelectedValue() +  "\\");
					Go_There();
				}			
			} else if(e.getSource() == delete){//��ѡ�µ�ɾ��
				List<String> selected_str = list.getSelectedValuesList();
				File file;
				int num = selected_str.size();
				int n = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ�� " + selected_str.get(0) + " ��" + num + "��ô?", "�ļ�ɾ��",JOptionPane.YES_NO_OPTION);
				if(n == 0){
					if(isSearching){//�����������������MapsȡURL
						for(int i = 0; i < selected_str.size(); ++i){
							file = new File(Maps.get(selected_str.get(i)));
							FileDelete.delete(file.getAbsolutePath());
						}				
					}else{//�������Cur_URLƴ�ӻ��
						for(int i = 0; i < selected_str.size(); ++i){
							FileDelete.delete(Cur_URL + selected_str.get(i) +  "\\");
						}		
						Go_There();
					}
				}						
			}
		
			else if(e.getSource() == JMIs[2]){//������
				String before = list.getSelectedValue();
				File file = new File(Cur_URL + before + "\\");
				String after = "";
				if(file.isDirectory()){
					after = (String) JOptionPane.showInputDialog(null, "���������ļ�����:\n", "������", JOptionPane.PLAIN_MESSAGE, null, null,
			                list.getSelectedValue());
				}else{
					after = (String) JOptionPane.showInputDialog(null, "���������ļ���:\n", "������", JOptionPane.PLAIN_MESSAGE, null, null,
			                list.getSelectedValue());
				}			
				if(before != after && after != null){
					new File(Cur_URL + before + "\\").renameTo(new File(Cur_URL + after + "\\"));
					Go_There();
				}else{
					Go_There();
				}
			}
		
			else if(e.getSource() == JMIs[3]){//���ļ�/�ļ������Դ���
//				String temp_url = Cur_URL + list.getSelectedValue() + "\\";
//				File file = new File(temp_url);
//				Icon icon = GetFileIcon.getSingleSmallIcon(temp_url);			
//				String name = list.getSelectedValue();
//				long size;
//				double final_size;
//			    long File_Num = 0, Directory_Num = 0;
//			    int flag = 0;
//				String file_size = "";
//				
//				String Create_Time = FileTime.getCreateTime(temp_url);
//				String Modify_Time = FileTime.getModifiedTime(temp_url);
//				String Last_Access = FileTime.getLatestAccessTime(temp_url);
//				
//				if(file.isDirectory()){//Ŀ¼���Գ�ʼ���������
//					DirectoryInfo DInfo = new DirectoryInfo();
//					size = DInfo._instance.getDirSize(file);
//					File_Num = DInfo.File_Num;
//					Directory_Num = DInfo.Directory_Num;
//					flag = 1;
//				}else{//�ļ����Գ�ʼ���������
//					size = file.length();				
//				}			 
//				final_size = 0;				
//				for(int i = 0; i < Sizes.length; ++i){
//					if(size / Sizes[i] > 0){
//						final_size = size * 1.0 / Sizes[i];
//						DecimalFormat fnum = new DecimalFormat("##0.00");  
//						file_size = fnum.format(final_size) + Size_Names[i];
//						break;
//					}
//				}
//				if(flag == 1){
//					FileProperties properties = new FileProperties(icon, name, file_size, File_Num, Directory_Num-1, temp_url, Create_Time);
//				}else{
//					FileProperties properties = new FileProperties(icon, name, file_size, temp_url, Create_Time, Modify_Time, Last_Access);
//				}		
			}else if(e.getSource() == JMIs[4]) {//������ϴ�
				
			    if(!isSearching){
					String url = Cur_URL + list.getSelectedValue();
					if(Cur_URL != ""){
						url += "\\";
					}
				    
				    //�ϴ����ļ�  ��ftp��������
				    //�ϴ�·��
				    String uploadFile = url.substring(0, url.length()-1);
				    System.out.println("�ϴ��ļ�λ��:"+uploadFile);
				    JOptionPane.showMessageDialog(this,"�����ϴ���"+uploadFile,"��ʾ ",3);
				    
				   
				    
				    FTPController ftp = getFtpControoler();
				    
					//����File�࣬�����õ��ļ���
					File file = new File(uploadFile);
					String fileName = file.getName();
					String userName = ftp_userName.getText();
					if(userName.equals("")) {
						userName = "anonymous";
					}
					
					 //���ļ����� ���ܣ�����ʱ�����һ��λ��
				    try {
						ZHFileCipherTxst.encode(uploadFile, "/"+fileName, ZHFileCipherTxst.key);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				    
					
					
					System.out.println("�û�����"+ userName);
					System.out.println("�ϴ��ļ�����"+ fileName);
					System.out.println("�ϴ�·��2��"+ file.getPath());
					
					/************���ܹ���******************/
					//������#SDJZDX_zhenghui��Ϊ�ָ���
					String jiamiqian = fileName+"#SDJZDX_zhenghuiagsdahscasdqwFTP"+userName;
					System.out.println("δ���ܺ���ļ�����"+jiamiqian);
					
					String base64jiami_fileName = ZHFileBase64Key.base64jiami(jiamiqian.getBytes());
					System.out.println("���ܺ���ļ�����"+base64jiami_fileName);
					
					//�ϴ��ļ�  "/"+fileName
//					boolean uploadFile2 = ftp.uploadFile("/", base64jiami_fileName, uploadFile);
					boolean uploadFile2 = ftp.uploadFile("/", base64jiami_fileName, "/"+fileName);
					if (uploadFile2==true) {
						JOptionPane.showMessageDialog(null, "�ϴ��ɹ���", "��ʾ", 1);
						//ˢ��ftp���ļ��б�
						shuaxin();
					}else {
						JOptionPane.showMessageDialog(null, "�ϴ�ʧ�ܣ�", "����ʾ��", JOptionPane.ERROR_MESSAGE);
					}
					
					//ɾ���ݴ���ļ�
					File deleFile = new File("/"+fileName);
					deleFile.delete();
					
				}
			    
			}
		
			
		else if(e.getSource() == JMIs2[1]){//�������Բ鿴
			String temp_url = list.getSelectedValue() + "\\";
			Icon icon = GetFileIcon.getSingleSmallIcon(temp_url);	
			File file = new File(temp_url);			
			FileSystemView fileSys=FileSystemView.getFileSystemView();
			String name = fileSys.getSystemDisplayName(file);
			double Available = file.getFreeSpace() * 1.0 / Sizes[0];		
			double Used = file.getTotalSpace()* 1.0 / Sizes[0] - Available;
			FileProperties properties = new FileProperties(icon, name, Used, Available);
		}
		
		else if(e.getSource() == GoBtn || e.getSource() == GuideText){//ͨ����ַ�������ļ���ַ��ת
			String url = GuideText.getText();
			if(url.length() > 0){
			File file = new File(url);
			if(file.exists()){
				stack.push(Cur_URL);
				Cur_URL = url;
				Go_There();
			}else{
				JOptionPane.showConfirmDialog(null, "û���ҵ���Ŀ¼", "ȷ�϶Ի���", JOptionPane.YES_OPTION);
			}
			}else{
				Home_List();
			}
		}
		

	}
	
	/**
     * ����������ʽ�ж��ַ����Ƿ�������
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
           Pattern pattern = Pattern.compile("[0-9]*");
           Matcher isNum = pattern.matcher(str);
           if( !isNum.matches() ){
               return false;
           }
           return true;
    }
    
    /**
     * ���췵��ftp�������ķ���
     * @return
     */
    public FTPController getFtpControoler() {
    		FTPController ftp = null;
    		String host = ftp_host.getText(); //ftp��ַ
			String port = ftp_port.getText();//�˿ں�
			String userName = ftp_userName.getText();//�û���
			String password = ftp_password.getText();//����
			
			if(!host.equals("") && port.equals("") && userName.equals("") && password.equals("")) {
				ftp = new FTPController(host);
				System.out.println(1);
			}else if (!host.equals("") && !port.equals("") && userName.equals("") && password.equals("")) {
				if(isNumeric(port)) {//�ж϶˿ں��ǲ�������
					ftp = new FTPController(host, Integer.valueOf(port));
				}else {
					JOptionPane.showMessageDialog(null, "�˿�����Ĳ���", "����������", JOptionPane.ERROR_MESSAGE);
				}
				System.out.println(2);
			}else if (!host.equals("") && !port.equals("") && !userName.equals("") && !password.equals("")) {
				if(isNumeric(port)) {//�ж϶˿ں��ǲ�������
					ftp = new FTPController(host, Integer.valueOf(port),userName,password);
				}else {
					JOptionPane.showMessageDialog(null, "�˿�����Ĳ���", "����������", JOptionPane.ERROR_MESSAGE);
				}
				System.out.println(3);
			}
    	
    	return ftp;
    }
}
