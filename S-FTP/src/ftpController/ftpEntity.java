package ftpController;


/**
 * ����ftp�˺ŵ�ʵ����
 * @author zhenghui
 * 2019��7��5��10:09:20
 */
public class ftpEntity {

	private String URL;//����ftp�ĵ�ַ��url��
	
	private String PORT;//����ftp�Ķ˿ں�
	
	private String USER_NAME;//����ftpʹ�õ��û���
	
	private String PASSWORD;//����ftp�û�������

	//����û�ֻ���������ӵ�ַ��Ĭ�Ͼ���21�Ŷ˿����ӣ���anonymous�˻���¼
	public ftpEntity(String uRL) {
		this(uRL, "21", "anonymous", "anonymous");
	}
	
	//����û�ֻ���������ӵ�ַ�Ͷ˿ڣ�Ĭ�Ͼ���anonymous�˻���¼
	public ftpEntity(String uRL,String pORT) {
		this(uRL, pORT, "anonymous", "anonymous");
	}
	
	//����û�����ȫ�ˣ���ʹ���Զ��������
	public ftpEntity(String uRL, String pORT, String uSER_NAME, String pASSWORD) {
		super();
		URL = uRL;
		PORT = pORT;
		USER_NAME = uSER_NAME;
		PASSWORD = pASSWORD;
	}
	
	public ftpEntity() {
		super();
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getPORT() {
		return PORT;
	}

	public void setPORT(String pORT) {
		PORT = pORT;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	
	
	
	
}
