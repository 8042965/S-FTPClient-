package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  ʹ��Base64���ļ����м��ܽ���
 * @author zhenghui
 *
 */
public class ZHFileBase64Key {
	
	//base64����
	public static String base64jiami(byte[] input) {
		String result = null;
		try {
			//����
			Class class1= Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
			Method method = class1.getMethod("encode", byte[].class);
			result = (String) method.invoke(null, input);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//����
	public static byte[] base64jiemi(String input) {
		byte[]  output = null;
		try {
			Class class1= Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
			Method method = class1.getMethod("decode", String.class);
			output = (byte[]) method.invoke(null, input);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return output;
	}
	
}
