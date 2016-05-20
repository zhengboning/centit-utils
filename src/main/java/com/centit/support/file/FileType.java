package com.centit.support.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Hex;

/**
 * 
 * @author 朱晓文
 *
 */
public class FileType {
	
	public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();
	static {
		// images
		mFileTypes.put("FFD8FF", "jpg");
		mFileTypes.put("89504E47", "png");
		mFileTypes.put("47494638", "gif");
		mFileTypes.put("49492A00", "tif");
		mFileTypes.put("424D", "bmp");
		//
		mFileTypes.put("41433130", "dwg"); // CAD
		mFileTypes.put("38425053", "psd");
		mFileTypes.put("7B5C727466", "rtf"); // 日记本
		mFileTypes.put("3C3F786D6C", "xml");
		mFileTypes.put("68746D6C3E", "html");
		mFileTypes.put("44656C69766572792D646174653A", "eml"); // 邮件
		mFileTypes.put("D0CF11E0", "doc/xls");
		mFileTypes.put("5374616E64617264204A", "mdb");
		mFileTypes.put("252150532D41646F6265", "ps");
		mFileTypes.put("255044462D312E", "pdf");
		mFileTypes.put("504B0304", "docx/xlsx");
		mFileTypes.put("52617221", "rar");
		mFileTypes.put("57415645", "wav");
		mFileTypes.put("41564920", "avi");
		mFileTypes.put("2E524D46", "rm");
		mFileTypes.put("000001BA", "mpg");
		mFileTypes.put("000001B3", "mpg");
		mFileTypes.put("6D6F6F76", "mov");
		mFileTypes.put("3026B2758E66CF11", "asf");
		mFileTypes.put("4D546864", "mid");
		mFileTypes.put("1F8B08", "gz");
	}
   
    /** 
     * 得到文件头 
     *  
     * @param filePath 文件路径 
     * @return 文件头 
     * @throws IOException 
     */  
    private static String getFileHeadContent(File file) throws IOException {  
          
        byte[] b = new byte[28];  
          
        InputStream inputStream = null;  
          
        try {  
            inputStream = new FileInputStream(file);  
            inputStream.read(b, 0, 28);  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            if (inputStream != null) {  
                try {  
                    inputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw e;  
                }  
            }  
        }  
        return String.valueOf(Hex.encodeHex(b,false));  
    }  
      
    /** 
     * 判断文件类型 
     *  
     * @param filePath 文件路径 
     * @return 文件类型 
     * @throws IOException 
     */  
    public static String getFileType(File file) throws IOException {
    	String fileHead = getFileHeadContent(file);  
        
        if (fileHead == null || fileHead.length() == 0) {  
            return null;  
        }  
          
        fileHead = fileHead.toUpperCase();  
        for(Entry<String,String> entry : mFileTypes.entrySet()){
        	if(fileHead.startsWith(entry.getKey())){
        		if("doc/xls".equals(entry.getValue())){
        			if(file.getName().endsWith("doc")){
        				//System.out.println("\ndoc");
        				return "doc";
        			}else{
        				//System.out.println("\nxls");
        				return "xls";
        			}
        		}else if("docx/xlsx".equals(entry.getValue())){
        			if(file.getName().endsWith("docx")){
        				//System.out.println("\ndocx");
        				return "docx";
        			}else{
        				//System.out.println("\nxlsx");
        				return "xlsx";
        			}
        		}else{
        			//System.out.println("\n"+entry.getValue());
        			return entry.getValue();
        		}
        	}
        }  
        //System.out.println("\n空");
        return ""; 
    }
  
}
