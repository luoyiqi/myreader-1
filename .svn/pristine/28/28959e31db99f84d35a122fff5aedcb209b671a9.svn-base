package wida.reader.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import wida.reader.util.encodingdetect.BytesEncodingDetect;

public class Utils {
	/**
	 * 判断文件的编码格式
	 * @param file 
	 * @return 文件编码格式
	 */
	public static String getJavaEncode(File file){
		BytesEncodingDetect s = new BytesEncodingDetect(); 
		String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(file)];
		return fileCode;
	}
	
}
