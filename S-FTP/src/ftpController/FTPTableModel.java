package ftpController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import util.ZHFileBase64Key;

/**
 * FTP�ļ�Ŀ¼�ı��
 * @author zhenghui
 *
 */
public class FTPTableModel extends AbstractTableModel {
	// �����ͷ����
	String[] head = {"ѡ��","�޸�ʱ��","�ļ�����", "��С", "�ļ���"};
	
	//������ÿһ�е���������  
	Class[] typeArray = {Boolean.class,Object.class, Object.class,  Object.class, Object.class };

	//����
	Object[][] data = null;
	ArrayList<FileEntity> fileList= null;
	
	 ArrayList<FileEntity> fileList1= null;
	
	//�洢�ļ����ͽ�����Կ  K:�ļ���  V:������Կ
	Map<String, String> fileNameMap = new HashMap<>();
	
	//FTP�û���
	String ftpName= null;

	 //��ȡѡ�е�����
	 public String getSelectFileName(String  string) {
		 return fileNameMap.get(string);
	 }
	
	
	
	public void setData(ArrayList<FileEntity> fileList) {
		this.fileList = fileList;
		fileList1 = new ArrayList<>();
	}
	
	//��̬��������
	public FTPTableModel(String ftpName) {
		this.ftpName = ftpName;
		
		//�������ݿ�����
		if(fileList==null) {
			data=new Object[0][head.length];//��̬�������
		}else {
			data=new Object[fileList.size()][head.length];//��̬�������
		}
	}
	
	//��̬��������
	public void getFTPTableData() {
		
		//�������ݿ�����
		if(fileList!=null) {
			int size = 0;
			 //��װ���ݽ��
	        for(int i=0;i<fileList.size();i++){
	        	try {
	        		  String jiaMiQianFileName = fileList.get(i).getFileName();//��ȡ���ܺ���ļ���
	                  byte[] base64jiemi = ZHFileBase64Key.base64jiemi(jiaMiQianFileName);
	                  String jiemiFileName = new String(base64jiemi);
	                  String[] split = jiemiFileName.split("#SDJZDX_zhenghuiagsdahscasdqwFTP");
	                  if (split[1].equals(ftpName)) {
	                  	size++;
	                  	
	                  	String fileSize = "";
	                  	if(Integer.valueOf(fileList.get(i).getFileSise())==-1) {
	                  		fileSize=""; //��С
	  	                }else {
	  	                	fileSize= fileList.get(i).getFileSise(); //��С
	  	                }
	                  	
	                  	//�ѷ��ϵ����ݵ����ص�
	                  	fileList1.add(new FileEntity(split[0], fileSize, fileList.get(i).getFileType(), fileList.get(i).getFileUpdateDate()));
	  				}
				} catch (NullPointerException e) {
					System.out.println(fileList.get(i).getFileName()+"�ļ�������Ҫ���ѹ��ˣ�");
				}
              
	        }
			System.out.println("size:"+size);
			//��̬�������
			data=new Object[size][head.length];
		}
		
		//�����������ݵ����
		if (fileList1.size()>0) {
			 //��װ���ݽ��
	        for(int i=0;i<fileList1.size();i++){
            	try {
 	                data[i][0]=new Boolean(false);  //ѡ��
	                data[i][1]=fileList.get(i).getFileUpdateDate(); //�޸�ʱ��
	                data[i][2]=fileList.get(i).getFileType(); //�ļ�����
	               
	                String size = fileList.get(i).getFileSise();
	                
	                if(Integer.valueOf(fileList.get(i).getFileSise())==-1) {
	                	 data[i][3]=""; //��С
	                }else {
	                	 data[i][3]= size; //��С
	                }
	                
	                String jiaMiQianFileName = fileList.get(i).getFileName();//��ȡ���ܺ���ļ���
	                byte[] base64jiemi = ZHFileBase64Key.base64jiemi(jiaMiQianFileName);
	                
	                String jiemiFileName = new String(base64jiemi);
	                String[] split = jiemiFileName.split("#SDJZDX_zhenghuiagsdahscasdqwFTP");
                	data[i][4] = split[0] ; //�ļ���  ����
 	                fileNameMap.put(split[0], split[1]);  //�ļ�������Կ��Ӧ�������洢��Map��   �ļ���  ��Կ(�û���)
 	                
            	} catch (Exception e) {
            		e.printStackTrace();
				}
	        }
		}
		
	}
	
	
	// ��ñ�������
	public int getColumnCount() {
		return head.length;
	}

	// ��ñ�������
	public int getRowCount() {
		return data.length;
	}

	// ��ñ���������
	@Override
	public String getColumnName(int column) {
		return head[column];
	}

	// ��ñ��ĵ�Ԫ�������
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	// ʹ�����пɱ༭��
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	// �滻��Ԫ���ֵ
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		data[rowIndex][columnIndex] = aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	// ʵ���������boolean�Զ�ת��JCheckbox
	/*
	 * ��Ҫ�Լ���celleditor��ô�鷳�ɡ�jtable�Զ�֧��Jcheckbox��
	 * ֻҪ����tablemodel��getColumnClass����һ��boolean��class�� jtable���Զ���һ��Jcheckbox���㣬
	 * ���value��true����falseֱ�Ӷ�table���Ǹ�cell��ֵ�Ϳ���
	 */
	public Class getColumnClass(int columnIndex) {
		return typeArray[columnIndex];// ����ÿһ�е���������
	}
	
	//�Ƿ�ѡ��һ��
	public Boolean getSelectOne() {
		int sum = 0;
		
		 for (int i = 0; i < this.getRowCount(); i++) {
			 System.out.println("ѡ�е�״̬��"+Boolean.valueOf((boolean) this.getValueAt(i, 0)));
			 if(Boolean.valueOf((boolean) this.getValueAt(i, 0)) == true) {
				 sum  = sum +1;
			 }
		 }
		 
		return sum==1;
	}
	
	//��ȡѡ�е�ѧ��ID�е�����
	public String[] getSelectOneInfo() {
		String strID[] = new String[2];
		 for (int i = 0; i < this.getRowCount(); i++) {
			 if(Boolean.valueOf((boolean) this.getValueAt(i, 0)) == true) {
				 strID[0] = (String) this.getValueAt(i, 3);
				 strID[1] = (String) this.getValueAt(i, 4);
			 }
		 }
		return strID;
	}
	
	
	//�Ƿ�ѡ�е�����
	public int getSelectAllSum() {
		int sum = 0;
		
		 for (int i = 0; i < this.getRowCount(); i++) {
			 if(Boolean.valueOf((boolean) this.getValueAt(i, 0)) == true) {
				 sum  = sum +1;
			 }
		 }
		 
		return sum;
	}
	
	//��ȡ����ѡ�е��ļ�������
	public String[] getSelectAllInfo() {
		String strID[] = new String[getSelectAllSum()];
		int j = 0;
		 for (int i = 0; i < this.getRowCount(); i++) {
			 if(Boolean.valueOf((boolean) this.getValueAt(i, 0)) == true) {
				 strID[j] = (String) this.getValueAt(i, 4);
				 j = j + 1;
			 }
		 }
		return strID;
	}
	
	//��ȡ�洢���ļ�������Կ
	public Map<String, String> getFileNameMap() {
		return fileNameMap;
	}

	public void setFileNameMap(Map<String, String> fileNameMap) {
		this.fileNameMap = fileNameMap;
	}
	
}