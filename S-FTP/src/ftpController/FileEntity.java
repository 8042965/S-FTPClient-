package ftpController;

/**
 * FTP��ȡ���ļ��Լ��ļ���ʵ����
 * @author zhenghui
 *
 */
public class FileEntity {

	String fileName;//�ļ���
	
	String fileSise; //�ļ���С
	
	String fileType;//�ļ�����
	
	String fileUpdateDate;//�ļ��޸ĵ�����
	

	public FileEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileEntity(String fileName, String fileSise, String fileType, String fileUpdateDate) {
		super();
		this.fileName = fileName;
		this.fileSise = fileSise;
		this.fileType = fileType;
		this.fileUpdateDate = fileUpdateDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSise() {
		return fileSise;
	}

	public void setFileSise(String fileSise) {
		this.fileSise = fileSise;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileUpdateDate() {
		return fileUpdateDate;
	}

	public void setFileUpdateDate(String fileUpdateDate) {
		this.fileUpdateDate = fileUpdateDate;
	}
	
	
	
	
}
