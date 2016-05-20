package com.centit.support.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
/**
 * 
 * @author 朱晓文
 */
public class FileMD5Maker {

	public static String makeFileMD5(File file) throws NoSuchAlgorithmException, FileNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		final MessageDigest MD5 = MessageDigest.getInstance("MD5");
		/*final {
	    	try {
		        MD5 = MessageDigest.getInstance("MD5");
		    } catch (NoSuchAlgorithmException ne) {
		        ne.printStackTrace();
	        }*/
		
        try {
        //fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fis.read(buffer)) != -1) {
            	MD5.update(buffer, 0, length);
            }


            return new String(Hex.encodeHex(MD5.digest()));
        } catch (FileNotFoundException e) {
        e.printStackTrace();
            return null;
        } catch (IOException e) {
        e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null)
                	fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
