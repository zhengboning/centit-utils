package com.centit.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.centit.support.network.HttpExecutor;


public class TestTcpip {
	 public static void main(String arg[]){
		 
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 Map<String ,Object> formData = new HashMap<String ,Object>();
		 formData.put("catalogCode","TestData");
		 formData.put("catalogStyle","U");
		 formData.put("catalogType","L");
		 formData.put("catalogName","测试  PUT");
		 formData.put("catalogDesc","测试修改！");
		 formData.put("fieldDesc"," 字典字段描述");
		 formData.put("needCache","1");
		 formData.put("optId","MGH");
		 
		 
		 try {
			 JSON json = (JSON)JSON.toJSON(formData);
			 String uri = "http://192.168.133.11:8180/centit/service/sys/testText";
			 String sRet = HttpExecutor.simplePost(httpclient, null, uri,
					 json.toJSONString(),false );
			 System.out.println(sRet);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	 }
	 
	 public static void testPsts(){
		 CloseableHttpClient httpclient = HttpClients.createDefault();

		 String uri = "http://192.168.133.11:8180/centit/service/sys/datacatalog/TestData";

		 Map<String ,Object> formData = new HashMap<String ,Object>();
		 formData.put("catalogCode","TestData");
		 formData.put("catalogStyle","U");
		 formData.put("catalogType","L");
		 formData.put("catalogName","测试  PUT");
		 formData.put("catalogDesc","测试修改！");
		 formData.put("fieldDesc"," 字典字段描述");
		 formData.put("needCache","1");
		 formData.put("optId","MGH");
		 
		 try {
			 String sRet = HttpExecutor.formPost(httpclient, null, uri,
					formData,true );
			 System.out.println(sRet);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		 
	 }
}
