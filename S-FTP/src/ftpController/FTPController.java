package ftpController;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
 
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import util.ZHFileBase64Key;
import util.ZHFileCipherTxst;
 
public class FTPController {
	// ftp��������ַ
	public String hostname = "127.0.0.1";
	// ftp�������˿ں�Ĭ��Ϊ21
	public Integer port = 21;
	// ftp��¼�˺�
	public String username = "anonymous";
	// ftp��¼����
	public String password = "";
 
	public FTPClient ftpClient = null;
	
	public FTPController() {
		super();
		// TODO Auto-generated constructor stub
	}
	//ֻ������ftp�������ĵ�ַ
	public FTPController(String hostname) {
		super();
		this.hostname = hostname;
	}
	//ֻ�����ftp�������ĵ�ַ�Ͷ˿ں�
	public FTPController(String hostname, Integer port) {
		super();
		this.hostname = hostname;
		this.port = port;
	}

	//�����˵�ַ���˿ںţ��˺ź�����
	public FTPController(String hostname, Integer port, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * ��ʼ��ftp������
	 */
	public void initFtpClient() {
		System.out.println(hostname+" ," +port+","+username+","+password);
		ftpClient = new FTPClient();
//		ftpClient.setControlEncoding("utf-8");
		ftpClient.setControlEncoding("GBK");
		try {
			System.out.println("connecting...ftp������:" + this.hostname + ":" + this.port);
			ftpClient.connect(hostname, port); // ����ftp������
			ftpClient.login(username, password); // ��¼ftp������
			
			int replyCode = ftpClient.getReplyCode(); // �Ƿ�ɹ���¼������
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("connect failed...ftp������:" + this.hostname + ":" + this.port);
			}
			System.out.println("connect successfu...ftp������:" + this.hostname + ":" + this.port);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * �ϴ��ļ�
	 * 
	 * @param pathname
	 *            ftp���񱣴��ַ
	 * @param fileName
	 *            �ϴ���ftp���ļ���
	 * @param originfilename
	 *            ���ϴ��ļ������ƣ����Ե�ַ�� *
	 * @return
	 */
	public boolean uploadFile(String pathname, String fileName, String originfilename) {
		InputStream inputStream = null;
		try {
			System.out.println("��ʼ�ϴ��ļ�");
			inputStream = new FileInputStream(new File(originfilename));
			initFtpClient();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			CreateDirecroty(pathname);
			ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			// ÿ����������֮ǰ��ftp client����ftp server��ͨһ���˿�����������
			ftpClient.enterLocalPassiveMode();
			// �۲��Ƿ�����ϴ��ɹ�
			boolean storeFlag = ftpClient.storeFile(fileName, inputStream);
			System.err.println("storeFlag==" + storeFlag);
			inputStream.close();
			ftpClient.logout();
			System.out.println("�ϴ��ļ��ɹ�");
		} catch (Exception e) {
			System.out.println("�ϴ��ļ�ʧ��");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
 
	/**
	 * �ϴ��ļ�
	 * 
	 * @param pathname
	 *            ftp���񱣴��ַ
	 * @param fileName
	 *            �ϴ���ftp���ļ���
	 * @param inputStream
	 *            �����ļ���
	 * @return
	 */
	public boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
		try {
			System.out.println("��ʼ�ϴ��ļ�");
			initFtpClient();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			CreateDirecroty(pathname);
			ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			ftpClient.logout();
			System.out.println("�ϴ��ļ��ɹ�");
		} catch (Exception e) {
			System.out.println("�ϴ��ļ�ʧ��");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
 
	// �ı�Ŀ¼·��
	public boolean changeWorkingDirectory(String directory) {
		boolean flag = true;
		try {
			flag = ftpClient.changeWorkingDirectory(directory);
			if (flag) {
				System.out.println("�����ļ���" + directory + " �ɹ���");
 
			} else {
				System.out.println("�����ļ���" + directory + " ʧ�ܣ���ʼ�����ļ���");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return flag;
	}
 
	// �������Ŀ¼�ļ��������ftp�������Ѵ��ڸ��ļ����򲻴���������ޣ��򴴽�
	public boolean CreateDirecroty(String remote) throws IOException {
		boolean success = true;
		String directory = remote + "/";
		// ���Զ��Ŀ¼�����ڣ���ݹ鴴��Զ�̷�����Ŀ¼
		if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			String path = "";
			String paths = "";
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
				path = path + "/" + subDirectory;
				if (!existFile(path)) {
					if (makeDirectory(subDirectory)) {
						changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("����Ŀ¼[" + subDirectory + "]ʧ��");
						changeWorkingDirectory(subDirectory);
					}
				} else {
					changeWorkingDirectory(subDirectory);
				}
 
				paths = paths + "/" + subDirectory;
				start = end + 1;
				end = directory.indexOf("/", start);
				// �������Ŀ¼�Ƿ񴴽����
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}
 
	// �ж�ftp�������ļ��Ƿ����
	public boolean existFile(String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}
 
	// ����Ŀ¼
	public boolean makeDirectory(String dir) {
		boolean flag = true;
		try {
			flag = ftpClient.makeDirectory(dir);
			if (flag) {
				System.out.println("�����ļ���" + dir + " �ɹ���");
 
			} else {
				System.out.println("�����ļ���" + dir + " ʧ�ܣ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
 
	/**
	 * * �����ļ� *
	 * 
	 * @param pathname
	 *            FTP�������ļ�Ŀ¼ *
	 * @param filename
	 *            �ļ����� *
	 * @param localpath
	 *            ���غ���ļ�·�� *
	 * @return
	 */
	public boolean downloadFile(String pathname, String filename, String localpath) {
		boolean flag = false;
		OutputStream os = null;
		try {
			System.out.println("��ʼ�����ļ�");
			initFtpClient();
			// �л�FTPĿ¼
			boolean changeFlag = ftpClient.changeWorkingDirectory(pathname);
			System.err.println("changeFlag==" + changeFlag);
 
			ftpClient.enterLocalPassiveMode();
			ftpClient.setRemoteVerificationEnabled(false);
			// �鿴����Щ�ļ��� ��ȷ���л���ftp·����ȷ
			String[] a = ftpClient.listNames();
			System.err.println(a[0]);
 
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
				if (filename.equalsIgnoreCase(file.getName())) {
					System.out.println("���ص��ļ�����"+file.getName());
					
					byte[] base64jiemi = ZHFileBase64Key.base64jiemi(file.getName());
	                String jiemiFileName = new String(base64jiemi);
	                String[] split = jiemiFileName.split("#SDJZDX_zhenghuiagsdahscasdqwFTP");
					
	                //"/"+fileName
	                System.out.println("localpath��"+localpath+"/" + split[0]);
	               
					
					
					
//					File localFile1 = new File("/" + split[0]);
//	                os = new FileOutputStream(localFile1);
//					ftpClient.retrieveFile(file.getName(), os);
					
//					ZHFileCipherTxst.decode("/" + split[0], localpath+"/" + split[0], ZHFileCipherTxst.key);
//					localFile1.delete();
					
//	                File localFile = new File(localpath + "/" + split[0]);
	                File localFile = new File("/" + split[0]);
					os = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), os);
					os.close();
					
					ZHFileCipherTxst.decode("/" + split[0], localpath+"/" + split[0], ZHFileCipherTxst.key);
					localFile.delete();
				}
			}
			ftpClient.logout();
			flag = true;
			System.out.println("�����ļ��ɹ�");
		} catch (Exception e) {
			System.out.println("�����ļ�ʧ��");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
 
	/**
	 * * ɾ���ļ� *
	 * 
	 * @param pathname
	 *            FTP����������Ŀ¼ *
	 * @param filename
	 *            Ҫɾ�����ļ����� *
	 * @return
	 */
	public boolean deleteFile(String pathname, String filename) {
		boolean flag = false;
		try {
			System.out.println("��ʼɾ���ļ�");
			initFtpClient();
			// �л�FTPĿ¼
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.dele(filename);
			ftpClient.logout();
			flag = true;
			System.out.println("ɾ���ļ��ɹ�");
		} catch (Exception e) {
			System.out.println("ɾ���ļ�ʧ��");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
 
	public static void main(String[] args) {
		FTPController ftp = new FTPController();
		// �ļ�·��дΪ�û�����ʱ ָ����Ŀ¼
		 ftp.uploadFile("/", "ceshi.png", "C:\\Users\\zhenghui\\Desktop\\QQͼƬ20190508230009.jpg");
		// ftp.downloadFile("/home/ftpFile", "123.png", "E://");
//		ftp.deleteFile("/", "11111111111111111111111111.txt");
		System.out.println("ok");
	}
}
