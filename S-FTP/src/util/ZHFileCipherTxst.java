package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ZHFileCipherTxst {
	
	public static byte[] key = new byte[] { 49, 38, -88, -75, 103, -50, 94, -92 }; // �ֽ���������8��������
	
	/**
	 * DES���ܽ��� DES��һ�ֶԳƼ����㷨����ν�ԳƼ����㷨�������ܺͽ���ʹ����ͬ��Կ���㷨��
	 * ע�⣺DES���ܺͽ��ܹ����У���Կ���ȶ�������8�ı���
	 */
	private static String Algorithm = "DES"; // ���� �����㷨,����DES,DESede,Blowfish

	// static {
	// Security.addProvider(new com.sun.crypto.provider.SunJCE());
	// }

	// ������Կ, ע��˲���ʱ��Ƚϳ�
	public static byte[] getKey() throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
		keygen.init(new SecureRandom());
		SecretKey deskey = keygen.generateKey();
		return deskey.getEncoded();
	}

	/**
	 * ����
	 * 
	 * @param enfile Ҫ���ܵ��ļ�
	 * @param defile ���ܺ���ļ�
	 * @param key    ��Կ
	 * @throws Exception
	 */
	public static void encode(String enfile, String defile, byte[] key) throws Exception {
		// ���ܣ��Գƣ���Կ(SecretKey�̳�(key))
		// ���ݸ������ֽ����鹹��һ����Կ��
		SecretKey deskey = new SecretKeySpec(key, Algorithm);
		// ����һ��ʵ��ָ��ת���� Cipher ����Cipher����ʵ����ɼӽ��ܲ���
		Cipher c = Cipher.getInstance(Algorithm);
		// ����Կ��ʼ���� cipher
		c.init(Cipher.ENCRYPT_MODE, deskey);

		byte[] buffer = new byte[1024];
		FileInputStream in = new FileInputStream(enfile);
		OutputStream out = new FileOutputStream(defile);

		CipherInputStream cin = new CipherInputStream(in, c);
		int i;
		while ((i = cin.read(buffer)) != -1) {
			out.write(buffer, 0, i);
		}
		out.close();
		cin.close();
	}

	// ����
	public static void decode(String file, String defile, byte[] key) throws Exception {

		// DES�㷨Ҫ����һ�������ε������Դ
		SecureRandom sr = new SecureRandom();
		// ����һ�� DESKeySpec ����,ָ��һ�� DES ��Կ
		DESKeySpec ks = new DESKeySpec(key);
		// ����ָ��������Կ�㷨�� SecretKeyFactory ����
		SecretKeyFactory factroy = SecretKeyFactory.getInstance(Algorithm);
		// �����ṩ����Կ�淶����Կ���ϣ����� SecretKey ����,������Կ������DESKeySpecת����һ��SecretKey����
		SecretKey sk = factroy.generateSecret(ks);
		// ����һ��ʵ��ָ��ת���� Cipher ����Cipher����ʵ����ɼӽ��ܲ���
		Cipher c = Cipher.getInstance(Algorithm);
		// ����Կ�����Դ��ʼ���� cipher
		c.init(Cipher.DECRYPT_MODE, sk, sr);

		byte[] buffer = new byte[1024];
		FileInputStream in = new FileInputStream(file);
		OutputStream out = new FileOutputStream(defile);
		CipherOutputStream cout = new CipherOutputStream(out, c);
		int i;
		while ((i = in.read(buffer)) != -1) {
			cout.write(buffer, 0, i);
		}
		cout.close();
		in.close();
	}

	public static void main(String[] args) throws Exception {
		// byte[] b = getKey();
		// System.out.println(b.toString());
		
		// �ļ�����
//        encode("D:/a.bat", "D:/b.bat", key);
		encode("E:/a.txt", "E:/b.txt", key);

		// �ļ�����
//        decode("D:/b.bat", "D:/c.bat", key);
		decode("E:/b.txt", "E:/c.txt", key);
	}
}