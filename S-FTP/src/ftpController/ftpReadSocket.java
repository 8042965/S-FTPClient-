package ftpController;

import java.awt.List;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.InflaterInputStream;

import javax.swing.JFrame;

import UI.MainForm;

import ftpController.*;
import util.ZHFileBase64Key;

/**
 * 
 * @author zhenghui
 * FTP�ͻ���
 */
public class ftpReadSocket {

	private Socket socket = null;//
	
	private BufferedReader reader = null;
	
//	private BufferedWriter writer = null;
	
	public PrintWriter ctrlWriter; //��������õ��� 
	
	MainForm mainForm = null;

	public void setMain(MainForm mainForm) {
		this.mainForm = mainForm;
	}

	//����û�ֻ����Զ�̵�ַ
	public void connect(String host) {
		connect(host,21);
	}
	
	public void connect(String host, int port) {
		connect(host, port, "anonymous", "");
	}

	//����ftp������
	public void connect(String host, int port, String userName, String password){
		try {
			//��ftp��������
			socket = new Socket(host,port);
			//��
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//д
//			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("��������" + host+"....");
			mainForm.setFTPMessage("��������" + host+"....");
			
			ctrlWriter = new PrintWriter(socket.getOutputStream()); 
			
			String line = null;
			line = reader.readLine();
			System.out.println("��Ӧ��"+line);
			mainForm.setFTPMessage("��Ӧ��"+line);
			
			//������������û�����
			System.out.println("���USER "+userName);
			mainForm.setFTPMessage("���USER "+userName);
			
			sendCommand("USER "+userName);
			line = reader.readLine();
			System.out.println("��Ӧ��"+line);
			mainForm.setFTPMessage("��Ӧ��"+line);

			//���������������
			sendCommand("PASS "+password);
			line = reader.readLine();
			System.out.println("���PASS "+password);
			System.out.println("��Ӧ��"+line);
			mainForm.setFTPMessage("���PASS "+password);
			mainForm.setFTPMessage("��Ӧ��"+line);
					
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�������FTP������
	public void sendCommand(String com) throws IOException {
		//���ж��Ƿ�������
		if(socket == null) {
			throw new IOException("FTPδ����");
		}
		//�������ʧ��
		try {
			//��������
//			writer.write(com+"\r\n");
//			writer.flush();
			ctrlWriter.println(com);
			ctrlWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//��ѯ���е��ļ�
	public void  selectAll() throws IOException {
		if(socket == null) {
			throw new IOException("FTPδ����");
		}
		
		//�������ʧ��
		try {
			//��������
			sendCommand("PWD");
			sendCommand("PASV");
			sendCommand("TYPE A");
			sendCommand("LIST");
			
			String line = null;
			while ((line=reader.readLine())!=null) {
				System.out.println(line);
			}
			
		} catch (Exception e) {
			socket = null;
			throw e;
		}
		
		
	}
	
  	//����ָ��
  	public void sendOrder(String order) throws IOException {
  		sendCommand("PASV");//����һ���������ӵ�
//  		sendCommand("PORT");//����һ���������ӵ�
        //ȡ�ñ�������ģʽ�Ķ˿ڵ���Ϣ
        String  response = reader.readLine();
        System.out.println(response);
        
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        String ip_port = response.substring(opening+1,closing);

        //        System.out.println(closing);
        //��ȡ227 Entering Passive Mode (127,0,0,1,250,80).  �е�IP��ַ�Ͷ˿ں�
        //IP��127.0.0.1
        //�˿ڣ�250*254+80
        String ip = null;
        int port = -1;
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port = Integer.parseInt(tokenizer.nextToken()) * 256  + Integer.parseInt(tokenizer.nextToken());
            } catch (Exception e) {
                throw new IOException("FTP���յ����������������Ϣ: "
                        + response);
            }
        }
        
        System.out.println("�ϴ��ļ���IP��"+ip + "  �˿ںţ�" + port);
        mainForm.setFTPMessage("�ϴ��ļ���IP��"+ip + "  �˿ںţ�" + port);
        sendCommand("PORT "+ip_port);//��������

        System.out.println(reader.readLine());
        sendCommand(order);//��������

        String str1 = null;
        while ((str1=reader.readLine())!=null) {
        	System.out.println("str1:"+str1);
        }
  	}
 
 
//dataConnection���� 
//��������������������õ�Socket 
//����PORT����˿�֪ͨ������ 
public Socket dataConnection(String ctrlcmd,int port) { 
	 String cmd = "PORT "; // PORT�����PORT��������ݵı��� 
	 int i; 
	 Socket dataSocket = null;// ����������Socket 
	 try { 
	  // �õ��Լ��ĵ�ַ 
	  byte[] address = InetAddress.getLocalHost().getAddress(); 
	  // ���ʵ��Ķ˿ںŹ�������� 
	  ServerSocket serverDataSocket = new ServerSocket(port,1); 
	 
	  // ���ÿ����õ�������PORT���� 
	  ctrlWriter.println(cmd+""+port); 
	  ctrlWriter.flush(); 
	  // ����������ʹ����������(LIST,RETR,��STOR) 
	  ctrlWriter.println(ctrlcmd); 
	  ctrlWriter.flush(); 
	 
	  // ����������������� 
	         
	  dataSocket = serverDataSocket.accept(); 
	  serverDataSocket.close(); 
	 } catch (Exception e) { 
	  e.printStackTrace(); 
	  System.exit(1); 
	 } 
 return dataSocket; 
}

  	//��ȡFTP������
	public void listFiles(String string, FTPTableModel ftpTableModel) throws IOException {
		    sendCommand("cwd  " + string);
	        String string1 = reader.readLine();
	        System.out.println("��Ӧ��" + string1);
	        mainForm.setFTPMessage("��Ӧ��"+ string1);
	        sendCommand("PASV");
	        
	        //ȡ��Ŀ¼��Ϣ 
	        String  response = reader.readLine();
	        String[] ip_PORT = getIP_PORT(response);
	        String ip =ip_PORT[0];
	        int port = Integer.valueOf(ip_PORT[1]);
	        
	        
	        System.out.println("listFilesIP��"+ip + "  listFiles�˿ںţ�" + port);
	        sendCommand("LIST" );
	        mainForm.setFTPMessage("listFilesIP��"+ip + "  listFiles�˿ںţ�" + port);

	        Socket dataSocket = new Socket(ip, port);
	        
	        DataInputStream dis = new DataInputStream(dataSocket.getInputStream());
	        
	        String s = "";
	        ArrayList<FileEntity> fileList = new ArrayList<FileEntity>();
	        
	        //����Ŀ¼
	        while ((s = dis.readLine()) != null) {
	            String l = new String(s.getBytes("ISO-8859-1"), "GB2312");//ת���ַ����ͣ������������
	            //�����Ŀ¼
	            if(l.indexOf("<DIR>")!=-1) {
	            	 //��Ŀ¼��ʱ����ַ����ָ�õ��ļ�����
	            	 String[] split = l.split("       <DIR>");
	            	 String string2 = "���ڣ�"+split[0]+",�ļ���,�ļ�����"+split[1].split("          ")[1];
	            	 String fileName = split[1].split("          ")[1];
	            	 String fileSise = "-1";
	            	 String fileType = "�ļ���";
	            	 String fileUpdateDate = split[0];
	            	 //�浽List��
	            	 fileList.add(new FileEntity(fileName, fileSise, fileType, fileUpdateDate));
	            }else{//�������Ŀ¼
	           	 	String[] split = l.split("             ");
	           	    String[] split2 = split[1].trim().split(" ");
		           	 String string2 ="���ڣ�"+split[0]+",�ļ�,��С��"+split2[0]+",�ļ�����"+split2[1];
//		           	 mainForm.setFTPMessage(string2);
		           	 String fileName = split2[1];
	            	 String fileSise = String.valueOf(split2[0]);
	            	 String fileType = "�ļ�";
	            	 String fileUpdateDate = split[0];
	            	 
		           	 //�浽List��
	            	 fileList.add(new FileEntity(fileName, fileSise, fileType, fileUpdateDate));
	            }
	        }
	        
	        ftpTableModel.setData(fileList);
	        
	        
	        
	        
	        dis.close();
	        dataSocket.close();
	        System.out.println(reader.readLine());
	        System.out.println(reader.readLine());
//	        String str1 = null;
//	        while ((str1=reader.readLine())!=null) {
//			}
	        
	}
	
	
	 //��ȡIP��ַ�Ͷ˿ں�
	 public String[] getIP_PORT(String  response) throws IOException {
		 
		 String ip = null;
	     int port = -1;
	     int opening = response.indexOf('(');
	     int closing = response.indexOf(')', opening + 1);

	     if (closing > 0) {
	         String dataLink = response.substring(opening + 1, closing);
	         StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
	         try {
	             ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
	                     + tokenizer.nextToken() + "." + tokenizer.nextToken();
	             port = Integer.parseInt(tokenizer.nextToken()) * 256
	                     + Integer.parseInt(tokenizer.nextToken());
	         } catch (Exception e) {
	             throw new IOException("FTP���յ����������������Ϣ: " + response);
	         }
	     }
	     
	     String [] str= {ip,String.valueOf(port)};
		 return str;
	 }
	
	//�ر����е�����
	public void closeAll() {
		if(socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("�ر�socketʧ��");
				e.printStackTrace();
			}
		}

		if(reader!=null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(ctrlWriter!=null) {
			ctrlWriter.close();
		}
	}
	
	
}
