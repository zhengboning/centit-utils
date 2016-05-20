package com.centit.support.network;

import java.util.Collection;

import org.apache.commons.lang3.StringEscapeUtils;


public class HtmlFormUtils {
	/**
	 * 请调用直接 org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4 
	 * 或者 org.springframework.web.util.HtmlUtils.htmlEscape
	 * @param value
	 * @return
	 */	
    public static String htmlValue(String value) {
    	return StringEscapeUtils.escapeHtml4(value);
 /*   	   if (value == null) {
               return null;
           }        
           StringBuffer result = new StringBuffer(value.length());
           for (int i=0; i<value.length(); ++i) {
               switch (value.charAt(i)) {
               case '<':
                   result.append("&lt;");
                   break;
               case '>': 
                   result.append("&gt;");
                   break;
               case '"': 
                   result.append("&quot;");
                   break;
               case '\'': 
                   result.append("&#39;");
                   break;
               case '%': 
                   result.append("&#37;");
                   break;
               case ';': 
                   result.append("&#59;");
                   break;
               case '(': 
                   result.append("&#40;");
                   break;
               case ')': 
                   result.append("&#41;");
                   break;
               case '&': 
                   result.append("&amp;");
                   break;
               case '+':
                   result.append("&#43;");
                   break;
               default:
                   result.append(value.charAt(i));
                   break;
               }
           }
           return result.toString();
      */
    }
    /**
     * 直接 org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4
     * 或者调用 org.springframework.web.util.HtmlUtils.htmlUnescape
     * @param value
     * @return
     */
    public static String htmlString(String value) { 
       return StringEscapeUtils.unescapeHtml4(value);
 	   /*if (value == null) {
            return null;
        }        
 	    return value.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
 	    		.replaceAll("&quot;", "\"").replaceAll("&apos;", "'")
 	    		.replaceAll("&amp;", "&");*/
   }     
    
    public static String[] htmlValue(String[] strArr) { 
    	if(strArr==null || strArr.length==0)
    		return null;
    	String  resStrArr[] = new String[strArr.length];
 	    for(int i=0;i<strArr.length;i++){   
 	    	resStrArr[i] = htmlValue(strArr[i]);   
	    }		    
	    return resStrArr; 
	}
    
    public static Object getParameterObject(Object v) {   
		if(v==null){   
		    return null;   
		}else if(v instanceof String[]){             
		    String []strArr=(String[]) v;   
		    return htmlValue(strArr);   
		}else if(v instanceof String){   
		    return htmlValue((String) v);   
		}else{   
		    return v;   
		}
	}
    
	public static String getParameterString(Object v) {   
		if(v==null){   
		    return null;   
		}else if(v instanceof String[]){             
		    String []strArr=(String[]) v;  
		    StringBuilder sb = new StringBuilder();
		    if(strArr.length>0){ 
		    	for(int i=0;i<strArr.length;i++){
		    		if(i>0)
		    			sb.append(',');
		    		sb.append(htmlValue(strArr[i]));
		    	}		      
		    	return sb.toString();   
		    }else{   
		      return null;   
		    }   
		}else if(v instanceof Collection<?>){
			@SuppressWarnings("unchecked")
			Collection<Object> objArr = (Collection<Object>) v;	
			StringBuilder sb = new StringBuilder();
			int i=0;
			for(Object obj:objArr){
				if(i>0)
	    			sb.append(',');
	    		sb.append(htmlValue(obj.toString()));
				i++;
			}
			return  sb.toString();  
		}else if(v instanceof String){   
		    return htmlValue((String) v);   
		}else{   
		    return htmlValue(v.toString());   
		}
	}
	
	public static String[] getParameterStringArray(Object v) {   
		if(v==null){   
		    return null;   
		}else if(v instanceof String[]){             
		    String []strArr=(String[]) v;   
		    if(strArr.length>0){   
		      return  strArr;   
		    }else{   
		      return null;   
		    }   
		}else if(v instanceof Object[]){       
			Object[] objArr = (Object[]) v;		       
		    if(objArr.length>0){   
		    	String []strArr= new String [objArr.length];
		    	for(int i=0;i<objArr.length;i++)
		    		strArr[i] = objArr[i].toString();
		    	return  strArr;   
		    }else{   
		      return null;   
		    }   
		}else if(v instanceof Collection<?>){
			@SuppressWarnings("unchecked")
			Collection<Object> objArr = (Collection<Object>) v;	
			String []strArr= new String [objArr.size()];
			int i=0;
			for(Object obj:objArr){
				strArr[i++]= obj.toString();
			}
			return  strArr; 
		}else if(v instanceof String){   
		    return new String[]{(String) v};   
		}else{   
		    return new String[]{v.toString()};   
		}
	}
}
