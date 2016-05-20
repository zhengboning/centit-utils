package com.centit.support.network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import com.alibaba.fastjson.JSON;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.FileSystemOpt;

public class HttpExecutor {
	
	public static final ContentType APPLICATION_FORM_URLENCODED = ContentType.create(
            "application/x-www-form-urlencoded", Consts.UTF_8);
	
	public static final String BOUNDARY="------1cC9oE7dN8eT1fI0aT2n4------";
	
	public static final String multiPartTypeHead = 
			"multipart/form-data; charset=UTF-8; boundary="+BOUNDARY;
			/*ContentType.create(
            "multipart/form-data",  new NameValuePair[]{
            		new BasicNameValuePair("charset","UTF-8"),
            		new BasicNameValuePair("boundary",BOUNDARY)
            }).toString();	*/
	
	
	public static final String applicationFormHead = 
			"application/x-www-form-urlencoded; charset=UTF-8";
	
	public static final String multiPartApplicationFormHead = 
			"multipart/x-www-form-urlencoded; charset=UTF-8; boundary="+BOUNDARY;
			/*ContentType.create(
			"application/x-www-form-urlencoded",  (new NameValuePair[]{
            		new BasicNameValuePair("charset","UTF-8"),
            		new BasicNameValuePair("boundary",BOUNDARY)
            })).toString();	*/
	public static final String applicationJSONHead = ContentType.create(
	            "application/json", Consts.UTF_8).toString();	
    public static final String plainTextHead = ContentType.create(
            "text/plain", Consts.UTF_8).toString();
    public static final String xmlTextHead = ContentType.create(
            "text/xml", Consts.UTF_8).toString();  
    
    
    public static CloseableHttpClient createHttpClient(){
    	return HttpClients.createDefault();
    }
    
    public static CloseableHttpClient createHttpClient(HttpHost httpProxy){
    	return HttpClients.custom().setProxy(httpProxy).build();
    }
    
    /**
     * 设置cookie的保存策略为CookieSpecs.NETSCAPE，这样可以保持session
     * @return
     */
    public static CloseableHttpClient createKeepSessionHttpClient(){
    	RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.NETSCAPE).build();    	
    	return HttpClients.custom().setDefaultRequestConfig(config).build();
    }
    
    public static CloseableHttpClient createKeepSessionHttpClient(HttpHost httpProxy){
    	RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.NETSCAPE).build();
    	return HttpClients.custom().setDefaultRequestConfig(config).setProxy(httpProxy).build();
    }
    
    private static TrustManager manager = new X509TrustManager(){
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
    };
    
    public static CloseableHttpClient createHttpsClient() 
    		throws NoSuchAlgorithmException, KeyManagementException{
    	SSLContext context= SSLContext.getInstance("TLS");
		context.init(null, new TrustManager[]{manager}, null);
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context,NoopHostnameVerifier.INSTANCE);
    	//RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.NETSCAPE).build();
    	Registry<ConnectionSocketFactory> socketFactoryRegistry = 
    			RegistryBuilder.<ConnectionSocketFactory>create()
    			.register("http", PlainConnectionSocketFactory.INSTANCE)
    			.register("https", socketFactory)
    			.build();   
    	PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    	return HttpClients.custom().setConnectionManager(connectionManager).build();
    }
    
    public static CloseableHttpClient createKeepSessionHttpsClient() 
    		throws NoSuchAlgorithmException, KeyManagementException{
    	SSLContext context= SSLContext.getInstance("TLS");
		context.init(null, new TrustManager[]{manager}, null);
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context,NoopHostnameVerifier.INSTANCE);
    	RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.NETSCAPE).build();
    	Registry<ConnectionSocketFactory> socketFactoryRegistry = 
    			RegistryBuilder.<ConnectionSocketFactory>create()
    			.register("http", PlainConnectionSocketFactory.INSTANCE)
    			.register("https", socketFactory)
    			.build();   
    	PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    	return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
    }
    
    
    public static String httpExecute(CloseableHttpClient httpclient,
    		HttpContext context,
    		HttpRequestBase httpRequest,HttpHost httpProxy)
    		throws ClientProtocolException, IOException {
    	
    	if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpRequest.setConfig(config);
		}
    	
    	try (CloseableHttpResponse response = httpclient.execute(httpRequest,context)) {
			String responseContent = Utf8ResponseHandler.INSTANCE
					.handleResponse(response);
			return responseContent;
		}
    }

    public static String httpExecute(CloseableHttpClient httpclient,
    		HttpContext context,
    		HttpRequestBase httpRequest)
    		throws ClientProtocolException, IOException {    	
    	return httpExecute( httpclient,context,
        		 httpRequest, null);
    } 
    
    public static String httpExecute(CloseableHttpClient httpclient,
    		HttpRequestBase httpRequest,HttpHost httpProxy)
    		throws ClientProtocolException, IOException {    	
    	return httpExecute( httpclient,null,
        		 httpRequest, httpProxy);
    } 
    
    public static String httpExecute(CloseableHttpClient httpclient,
    		HttpRequestBase httpRequest)
    		throws ClientProtocolException, IOException {    	
    	return httpExecute( httpclient,null,
        		 httpRequest, null);
    } 
    
    public static String appendParamToUrl(String uri,String queryParam){
    	if (queryParam == null || "".equals(queryParam)) 
    		return uri;
    	return uri.endsWith("?")? uri + queryParam :
    		(uri.indexOf('?') == -1 ?  uri+'?'+queryParam :  uri+'&'+queryParam );
    }
    
	public static String simpleGet(CloseableHttpClient httpclient,
			HttpContext context,String uri, String queryParam)
			throws ClientProtocolException, IOException {

		HttpGet httpGet = new HttpGet(appendParamToUrl(uri,queryParam));

		return httpExecute(httpclient,context,httpGet);
	}
	
	public static String simpleGet(CloseableHttpClient httpclient,
			String uri, String queryParam)
			throws ClientProtocolException, IOException {

		return  simpleGet( httpclient,null,
				   uri,  queryParam);
	}
	
	
	public static String simpleDelete(CloseableHttpClient httpclient,
			HttpContext context, String uri, String queryParam)
			throws ClientProtocolException, IOException {
		
    	HttpDelete httpDelete = new HttpDelete(appendParamToUrl(uri,queryParam));

		return httpExecute(httpclient,context,httpDelete);
	}

	public static String simpleDelete(CloseableHttpClient httpclient,
			 String uri, String queryParam)
			throws ClientProtocolException, IOException {
		return simpleDelete(httpclient,null,
				  uri,  queryParam);
	}

	public static String simplePut(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, String putEntity)
			throws ClientProtocolException, IOException {
		
		HttpPut httpPut = new HttpPut(uri);

		httpPut.setHeader("Content-Type",plainTextHead);	
		
		if (putEntity != null) {
			StringEntity entity = new StringEntity(putEntity, Consts.UTF_8);
			httpPut.setEntity(entity);
		}

		return httpExecute(httpclient,context,httpPut);
	}
	
	public static String simplePut(CloseableHttpClient httpclient,
			 String uri, String putEntity)
			throws ClientProtocolException, IOException {
		return simplePut( httpclient,null,
				  uri,  putEntity);
	}

	
	public static String rawPut(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, byte[] bytes)
			throws ClientProtocolException, IOException {

		HttpPut httpPut = new HttpPut(uri);
		httpPut.setHeader("Content-Type",applicationFormHead);
		
		
		if (bytes != null) {
			ByteArrayEntity entity = new ByteArrayEntity(bytes);
			httpPut.setEntity(entity);
		}

		return httpExecute(httpclient,context,httpPut);
		
	}
	
	public static String rawPut(CloseableHttpClient httpclient,
			 String uri, byte[] bytes)
			throws ClientProtocolException, IOException {
		return  rawPut( httpclient,null,
				  uri, bytes);
	}
	
	/**
	 * 在spring mvc 中的 request.getInputStream() 是不可以用的，因为spring 已经处理过这个流
	 * 所以这个方法只能在自己写的servlet中使用
	 * @param httpclient
	 * @param httpProxy
	 * @param uri
	 * @param putIS
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String requestInputStreamPut(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, InputStream putIS)
			throws ClientProtocolException, IOException {
		
		HttpPut httpPut = new HttpPut(uri);
		httpPut.setHeader("Content-Type",applicationFormHead);		
		
		if (putIS != null) {
			InputStreamEntity entity = new InputStreamEntity(putIS);
			httpPut.setEntity(entity);
		}

		return httpExecute(httpclient,context,httpPut);
	}
	
	public static String requestInputStreamPut(CloseableHttpClient httpclient,
			 String uri, InputStream putIS)
			throws ClientProtocolException, IOException {
		return requestInputStreamPut(httpclient,null,
				  uri,  putIS);
	}
	
	public static List<NameValuePair> makeRequectParams(Object obj, String prefixName){
		List<NameValuePair> params = new ArrayList<NameValuePair>();	
		if(obj==null)
			return params;
			
		if(ReflectionOpt.isScalarType(obj.getClass())){
			params.add( new BasicNameValuePair(prefixName, 
						StringBaseOpt.objectToString(obj)));
			return params;
		}else if(obj instanceof NameValuePair){
			params.add((NameValuePair)obj);
			return params;
		}else if(obj instanceof Map){
			String sFN = (prefixName==null || "".equals(prefixName))?"":prefixName+".";
			@SuppressWarnings("unchecked")
			Map<String ,Object> objMap = (Map<String ,Object>) obj;
			for(Map.Entry<String ,Object> f : objMap.entrySet()){		
				if(f.getValue()!=null){
					List<NameValuePair> subNP = makeRequectParams(f.getValue(), sFN + f.getKey());
					params.addAll(subNP);				
				}
			}//end of for
			return params;
		}else if (obj instanceof Collection) {//end of map
			Collection<?> objlist = (Collection<?>) obj;
			if(objlist.size()==1){
				Object subobj = objlist.iterator().next();
				if(subobj!=null){
					if(ReflectionOpt.isScalarType(subobj.getClass())){				
						params.add( new BasicNameValuePair(prefixName, 
								StringBaseOpt.objectToString(obj)));
					}else{
						if(subobj instanceof NameValuePair){
							params.add((NameValuePair)subobj);
						}else{
							List<NameValuePair> subNP = makeRequectParams(subobj,prefixName);
							params.addAll(subNP);
						}
					}
				}
			}else if(objlist.size()>1){
				int n=0; 
				//int complexObject=0;
				//List<String> arrayStr = new ArrayList<String>();				
				for(Object subobj: objlist){
					if(subobj!=null ){
						/*if(ReflectionOpt.isPrimitiveType(subobj.getClass())){
							arrayStr.add(StringBaseOpt.objectToString(subobj));
						}else*/ 
						if(ReflectionOpt.isScalarType(subobj.getClass())){
							params.add(new BasicNameValuePair(prefixName, 
									StringBaseOpt.objectToString(subobj)));
							//complexObject ++;
						}else if(subobj instanceof NameValuePair){
							params.add((NameValuePair)subobj);
							//complexObject ++;
						}else {
							List<NameValuePair> subNP = makeRequectParams(subobj,prefixName+"["+n+"]");
							params.addAll(subNP);
							//complexObject ++;
						}
					}//else
						//arrayStr.add("");
					n++;
				}//end of for
				/*if(complexObject == 0)
					params.add( new BasicNameValuePair(prefixName, 
							StringBaseOpt.objectToString(arrayStr)));*/
			}
			return params; //返回一个空的
		}else if (obj instanceof Object[]) {
			Object[] objs = (Object[]) obj;
			if(objs.length==1){
				Object subobj = objs[0];
				if(subobj!=null){
					if(ReflectionOpt.isScalarType(subobj.getClass())){					
						params.add( new BasicNameValuePair(prefixName, 
								StringBaseOpt.objectToString(obj)));
					}else if(subobj instanceof NameValuePair){
						params.add((NameValuePair)subobj);
					}else{					
						List<NameValuePair> subNP = makeRequectParams(subobj,prefixName);
						params.addAll(subNP);	
					}
				}
			}else if(objs.length>1){
				//int complexObject=0;
				//List<String> arrayStr = new ArrayList<String>();	
				for(int i=0;i<objs.length;i++){					
					if(objs[i]!=null ){
						/*if(ReflectionOpt.isPrimitiveType(objs[i].getClass())){
							arrayStr.add(StringBaseOpt.objectToString(objs[i]));
						}else*/ 
						if(ReflectionOpt.isScalarType(objs[i].getClass())){
							params.add(new BasicNameValuePair(prefixName, 
									StringBaseOpt.objectToString(objs[i])));
							//complexObject ++;
						}else if(objs[i] instanceof NameValuePair){
							params.add((NameValuePair)objs[i]);
							//complexObject ++;
						}else{
							List<NameValuePair> subNP = makeRequectParams(objs[i],prefixName+"["+i+"]");
							params.addAll(subNP);
							//complexObject ++;
						}
					}//else
						//arrayStr.add("");
				}//end of for
				/*if(complexObject == 0)
					params.add( new BasicNameValuePair(prefixName, 
							StringBaseOpt.objectToString(arrayStr)));*/
			}
			return params; //返回一个空的
		}else{
			 List<Method> methods = ReflectionOpt.getAllGetterMethod(obj.getClass());	
			 String sFN = (prefixName==null || "".equals(prefixName))?"":prefixName+".";
			 for(Method md : methods){				 
				 try {					 
					 Object v = md.invoke(obj);
					 if(v!=null){
						 String skey = ReflectionOpt.methodNameToField(md.getName());
						 
						 List<NameValuePair> subNP = makeRequectParams(v, sFN + skey);
						 params.addAll(subNP);	

					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
			 }
			 return params;
		}
		//return params;
	}

	public static List<NameValuePair> makeRequectParams(Object obj){		
		return makeRequectParams(obj,"");
	}
			
	public static String formPut(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Object formData)
			throws ClientProtocolException, IOException {
		HttpPut httpPut = new HttpPut(uri);		
		httpPut.setHeader("Content-Type",applicationFormHead);
		
		if (formData != null) {
			
			EntityBuilder eb = EntityBuilder.create();			
			eb.setContentType(APPLICATION_FORM_URLENCODED);
			eb.setContentEncoding("utf-8");
			//FormBodyPartBuilder formBuilder = FormBodyPartBuilder.create(formName,null);
			List<NameValuePair> params = makeRequectParams(formData,"");
			
			eb.setParameters(params);	
			httpPut.setEntity(eb.build());
		}
		
		return httpExecute(httpclient,context,httpPut);
	}
	
	public static String formPut(CloseableHttpClient httpclient,
			 String uri, Object formData)
			throws ClientProtocolException, IOException {
		return formPut(httpclient,null,
				 uri, formData);
	}
	
	public static String multiFormPut(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Object[] formObjects,Map<String,Object> extFormObjects)
			throws ClientProtocolException, IOException {

		HttpPut httpPut = new HttpPut(uri);
			
		httpPut.setHeader("Content-Type",applicationFormHead);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (formObjects != null) {
			for(int i=0;i<formObjects.length;i++){					
				if(formObjects[i]!=null ){
					List<NameValuePair> subNP = makeRequectParams(formObjects[i],"");
					params.addAll(subNP);
				}
			}//end of for			
		}
		if(extFormObjects!=null){
			List<NameValuePair> subNP = makeRequectParams(extFormObjects,"");
			params.addAll(subNP);
		}
			
		EntityBuilder eb = EntityBuilder.create();			
		eb.setContentType(APPLICATION_FORM_URLENCODED);
		eb.setContentEncoding("utf-8");
		//FormBodyPartBuilder formBuilder = FormBodyPartBuilder.create(formName,null);
		//List<NameValuePair> params = makeRequectParams(formData,"");
		eb.setParameters(params);	
		httpPut.setEntity(eb.build());
		
		return httpExecute(httpclient,context,httpPut);
	}
	
	
	public static String multiFormPut(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Object formObject,Map<String,Object> extFormObjects)
			throws ClientProtocolException, IOException {
		
		return multiFormPut( httpclient,context,
				  uri, new Object[]{formObject},extFormObjects);
	}
	
	
	public static String simplePost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, String postEntity, final boolean asPutMethod)
			throws ClientProtocolException, IOException {

		HttpPost httpPost = new HttpPost(asPutMethod ? urlAddMethodParameter(uri,"PUT") :uri);		
		httpPost.setHeader("Content-Type",plainTextHead);
		
		if (postEntity != null) {
			StringEntity entity = new StringEntity(postEntity, Consts.UTF_8);
			httpPost.setEntity(entity);
		}

		return httpExecute(httpclient,context,httpPost);
	}
	
	public static String simplePost(CloseableHttpClient httpclient,
			 String uri, String postEntity, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		return simplePost(httpclient, null,
				 uri,  postEntity, asPutMethod);
	}
	
	public static String simplePost(CloseableHttpClient httpclient,
			 String uri, String postEntity)
			throws ClientProtocolException, IOException {
		return simplePost(httpclient,null, uri, postEntity, false);
	}
	
	/**
	 * 在spring mvc 中的 request.getInputStream() 是不可以用的，因为spring 已经处理过这个流
	 * 所以这个方法只能在自己写的servlet中使用
	 * @param httpclient
	 * @param httpProxy
	 * @param uri
	 * @param postIS
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String requestInputStreamPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, InputStream postIS)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(uri);
		
		httpPost.setHeader("Content-Type",applicationFormHead);
		
		if (postIS != null) {
			InputStreamEntity entity = new InputStreamEntity(postIS);
			httpPost.setEntity(entity);
		}

		return httpExecute(httpclient,context,httpPost);
	}
	
	public static String requestInputStreamPost(CloseableHttpClient httpclient,
			 String uri, InputStream postIS)
			throws ClientProtocolException, IOException {
		return  requestInputStreamPost(httpclient,null,
				 uri,  postIS);
	}
	
	public static String rawPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, byte[] bytes,final boolean asPutMethod)
			throws ClientProtocolException, IOException {

		HttpPost httpPost = new HttpPost(asPutMethod ? urlAddMethodParameter(uri,"PUT") :uri);
	
		httpPost.setHeader("Content-Type",applicationFormHead);
		
		if (bytes != null) {
			ByteArrayEntity entity = new ByteArrayEntity(bytes);
			httpPost.setEntity(entity);
		}

		return httpExecute(httpclient,context,httpPost);
	}
	
	public static String rawPost(CloseableHttpClient httpclient,
			 String uri, byte[] bytes,final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		return rawPost( httpclient, null,  uri, bytes ,asPutMethod);
	}
	
	public static String rawPost(CloseableHttpClient httpclient,
			 String uri, byte[] bytes)
					throws ClientProtocolException, IOException {
		return rawPost( httpclient, null,  uri, bytes ,false);
	}
	
	public static String jsonPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, String jsonString, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		
		HttpPost httpPost = new HttpPost(asPutMethod ? urlAddMethodParameter(uri,"PUT") :uri);
		httpPost.setHeader("Content-Type",applicationJSONHead);
		if(jsonString!=null && !"".equals(jsonString)){
			StringEntity entity = new StringEntity(jsonString, Consts.UTF_8);
			httpPost.setEntity(entity);
		}

		return httpExecute(httpclient,context,httpPost);
	}
	
	public static String jsonPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, JSON jsonEntity, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		
		return jsonPost( httpclient,
				 context,
				  uri, jsonEntity==null?null:jsonEntity.toJSONString() ,  asPutMethod);
	}
	
	public static String jsonPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Object obj, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		
		return jsonPost( httpclient,
				 context,
				  uri, obj==null?null:JSON.toJSONString(obj) ,  asPutMethod);
	}
	
	public static String jsonPost(CloseableHttpClient httpclient,
			 String uri, String jsonString, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		return jsonPost( httpclient,null, uri,  jsonString, asPutMethod);
	}
	
	public static String jsonPost(CloseableHttpClient httpclient,
			 String uri, String jsonString)
			throws ClientProtocolException, IOException {
		return jsonPost( httpclient,null, uri,  jsonString, false);
	}
	
	public static String jsonPost(CloseableHttpClient httpclient,
			 String uri, Object obj, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		return jsonPost( httpclient,null, uri,  obj, asPutMethod);
	}
	
	public static String jsonPost(CloseableHttpClient httpclient,
			 String uri, Object obj)
			throws ClientProtocolException, IOException {
		return jsonPost( httpclient,null, uri,  obj, false);
	}
	
	
	public static String jsonPost(CloseableHttpClient httpclient,
			 String uri, JSON jsonEntity, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		return jsonPost( httpclient,null, uri,  jsonEntity, asPutMethod);
	}
	
	public static String jsonPost(CloseableHttpClient httpclient,
			 String uri, JSON jsonEntity)
			throws ClientProtocolException, IOException {
		return jsonPost( httpclient,null, uri,  jsonEntity, false);
	}
	
	public static String xmlPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, String xmlEntity,final boolean asPutMethod)
			throws ClientProtocolException, IOException {

		HttpPost httpPost = new HttpPost(asPutMethod ? urlAddMethodParameter(uri,"PUT") :uri);
		httpPost.setHeader("Content-Type",xmlTextHead);
		
		if (xmlEntity != null) {
			StringEntity entity = new StringEntity(xmlEntity, Consts.UTF_8);
			httpPost.setEntity(entity);
		}

		return httpExecute(httpclient,context,httpPost);
	}
	
	public static String xmlPost(CloseableHttpClient httpclient,
			 String uri, String xmlEntity,final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		return xmlPost( httpclient,null, uri,  xmlEntity,asPutMethod);
	}
	
	public static String xmlPost(CloseableHttpClient httpclient,
			 String uri, String xmlEntity)
			throws ClientProtocolException, IOException {
		return xmlPost( httpclient,null, uri,  xmlEntity,false);
	}
	
	public static String urlAddMethodParameter(String url,String method){
		String sUrl;// = url;
		if (url.indexOf('?') == -1) {
			sUrl = url +"?_method="+method;
		}else if(url.endsWith("?")){
			sUrl = url + "_method="+method;
		}else{
			sUrl = url +"&_method="+method;
		}
		return sUrl;
	}
	
	public static String formPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Object formData, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
	
		HttpPost httpPost = new HttpPost(asPutMethod? urlAddMethodParameter(uri,"PUT") :uri);
	
		httpPost.setHeader("Content-Type",applicationFormHead);
		
		if (formData != null) {			
			
			EntityBuilder eb = EntityBuilder.create();			
			eb.setContentType(APPLICATION_FORM_URLENCODED);
			eb.setContentEncoding("utf-8");
			//FormBodyPartBuilder formBuilder = FormBodyPartBuilder.create(formName,null);
			List<NameValuePair> params = makeRequectParams(formData,"");
			eb.setParameters(params);	
			httpPost.setEntity(eb.build());
		}
		
		return httpExecute(httpclient,context,httpPost);
	}
	
	public static String formPost(CloseableHttpClient httpclient,
			 String uri, Object formData, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
	
		return formPost(httpclient,null, uri, formData, asPutMethod);
	}

	public static String formPost(CloseableHttpClient httpclient,
			 String uri, Object formData)
			throws ClientProtocolException, IOException {
		return formPost(httpclient,null, uri, formData, false);
	}	
	
	public static String multiFormPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Object[] formObjects,Map<String,Object> extFormObjects, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
	
		HttpPost httpPost = new HttpPost(asPutMethod? urlAddMethodParameter(uri,"PUT") :uri);

		httpPost.setHeader("Content-Type",applicationFormHead);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (formObjects != null) {
			for(int i=0;i<formObjects.length;i++){					
				if(formObjects[i]!=null ){
					List<NameValuePair> subNP = makeRequectParams(formObjects[i],"");
					params.addAll(subNP);
				}
			}//end of for			
		}
		if(extFormObjects!=null){
			List<NameValuePair> subNP = makeRequectParams(extFormObjects,"");
			params.addAll(subNP);
		}
			
		EntityBuilder eb = EntityBuilder.create();			
		eb.setContentType(APPLICATION_FORM_URLENCODED);
		eb.setContentEncoding("utf-8");
		//FormBodyPartBuilder formBuilder = FormBodyPartBuilder.create(formName,null);
		//List<NameValuePair> params = makeRequectParams(formData,"");
		eb.setParameters(params);	
		httpPost.setEntity(eb.build());
		
		return httpExecute(httpclient,context,httpPost);
	}

	public static String multiFormPost(CloseableHttpClient httpclient,
			 String uri, Object[] formObjects,Map<String,Object> extFormObjects, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
	
		return multiFormPost( httpclient, null,
				  uri, (Object[]) formObjects, extFormObjects,asPutMethod);			
	}
	
	public static String multiFormPost(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Object formObject,Map<String,Object> extFormObjects, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		return multiFormPost( httpclient, context,
				  uri, new Object[]{formObject},extFormObjects,asPutMethod);				
	}

	public static String multiFormPost(CloseableHttpClient httpclient,
			 String uri, Object formObject,Map<String,Object> extFormObjects, final boolean asPutMethod)
			throws ClientProtocolException, IOException {
		return multiFormPost( httpclient, null,
				  uri, new Object[]{formObject},extFormObjects,asPutMethod);				
	}
	
	public static String multiFormPost(CloseableHttpClient httpclient,
			 String uri, Object[] formObjects,Map<String,Object> extFormObjects)
			throws ClientProtocolException, IOException {
		return multiFormPost( httpclient, null,
				  uri, formObjects,extFormObjects,false);				
	}
	
	public static String multiFormPost(CloseableHttpClient httpclient,
			 String uri, Object formObject,Map<String,Object> extFormObjects)
			throws ClientProtocolException, IOException {
		return multiFormPost( httpclient,null,
				  uri, new Object[]{formObject},extFormObjects,false);				
	}
	
	public static String inputStreamUpload(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Map<String, InputStream> files)
			throws ClientProtocolException, IOException {
		
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-Type",multiPartTypeHead);
		
		if (files != null) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setBoundary(BOUNDARY);
			for (Map.Entry<String, InputStream> file : files.entrySet()) {
				builder.addBinaryBody(file.getKey(), file.getValue(),
						ContentType.DEFAULT_BINARY,file.getKey());
			}
			httpPost.setEntity(builder.build());
		}
		
		return httpExecute(httpclient,context,httpPost);
	}

	public static String inputStreamUpload(CloseableHttpClient httpclient,
			 String uri, Map<String, InputStream> files)
			throws ClientProtocolException, IOException {
		return inputStreamUpload( httpclient,null,
				  uri,  files);
	}
	
	public static String formPostWithFileUpload(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Map<String, Object> formObjects, Map<String, File> files)
			throws ClientProtocolException, IOException {
	
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-Type",multiPartTypeHead);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setBoundary(BOUNDARY);	
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();	
		if(formObjects!=null){
			List<NameValuePair> subNP = makeRequectParams(formObjects,"");
			params.addAll(subNP);
		}
		
		for(NameValuePair param : params)
			builder.addTextBody(param.getName(), param.getValue());
		
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				builder.addBinaryBody(file.getKey(), file.getValue());
			}
		}
		httpPost.setEntity(builder.build());
		return httpExecute(httpclient,context,httpPost);
	}
	
	public static String formPostWithFileUpload(CloseableHttpClient httpclient,
			 String uri, Map<String, Object> formObjects, Map<String, File> files)
			throws ClientProtocolException, IOException {
		return formPostWithFileUpload(httpclient, null,
				 uri,  formObjects,  files);
	}
	
	public static String fileUpload(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, Map<String, File> files)
			throws ClientProtocolException, IOException {
	
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-Type",multiPartTypeHead);
		
		/*httpPost.setHeader("Content-Type",
				//ContentType.MULTIPART_FORM_DATA.toString()); 
                "multipart/form-data; boundary=" + BOUNDARY);  
		//httpPost.addHeader("boundary", BOUNDARY);
		 */	
		
		if (files != null) {			
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();			
			builder.setBoundary(BOUNDARY);			
			
			for (Map.Entry<String, File> file : files.entrySet()) {
				builder.addBinaryBody(file.getKey(), file.getValue());
			}
		
			httpPost.setEntity(builder.build());
		}
		return httpExecute(httpclient,context,httpPost);
	}
	
	public static String fileUpload(CloseableHttpClient httpclient,
			 String uri, Map<String, File> files)
			throws ClientProtocolException, IOException {
		return fileUpload( httpclient,null,
				  uri,  files);
	}

	protected static String extraFileName(CloseableHttpResponse response) {
		Header[] contentDispositionHeader = response
				.getHeaders("Content-disposition");
		Pattern p = Pattern.compile(".*filename=\"(.*)\"");
		Matcher m = p.matcher(contentDispositionHeader[0].getValue());
		m.matches();
		String fileName = m.group(1);
		return fileName;
	}

	
	public static boolean fileDownload(CloseableHttpClient httpclient,
			HttpContext context,
			 String uri, String queryParam,String filePath)
			throws ClientProtocolException, IOException {

		HttpGet httpGet = new HttpGet(appendParamToUrl(uri,queryParam));
		
		/*if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpGet.setConfig(config);
		}*/

		try (CloseableHttpResponse response = httpclient.execute(httpGet,context)) {

			Header[] contentTypeHeader = response.getHeaders("Content-Type");
			if (contentTypeHeader == null || contentTypeHeader.length < 1) {
				// 下载媒体文件出错
				if (ContentType.TEXT_PLAIN.getMimeType().equals(
						contentTypeHeader[0].getValue())) {
					String responseContent = Utf8ResponseHandler.INSTANCE
							.handleResponse(response);
					throw new RuntimeException(responseContent);
				}
			}
			InputStream inputStream = InputStreamResponseHandler.INSTANCE
					.handleResponse(response);

			// 视频文件不支持下载
			//fileName = extraFileName(response);
			return FileSystemOpt.createFile(inputStream,filePath);
		}
	}

	public static boolean fileDownload(CloseableHttpClient httpclient,
			 String uri, String queryParam,String filePath)
			throws ClientProtocolException, IOException {
		return fileDownload( httpclient,null,
				  uri,  queryParam, filePath);
	}
}
