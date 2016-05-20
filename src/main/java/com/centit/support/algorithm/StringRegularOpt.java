package com.centit.support.algorithm;

import java.util.Calendar;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringRegularOpt {


	final static public String  trimString(String szWord)
	{
		if(szWord==null)
			return "";
		String strWord = szWord.trim();
		
		int	sl = strWord.length();
		if(sl >= 2 && (( strWord.charAt(0)=='\"' && strWord.charAt(sl-1)=='\"') || 
				   ( strWord.charAt(0)=='\'' && strWord.charAt(sl-1)=='\'')) ){
			if(sl>2)
				strWord = strWord.substring(1,sl-1);
			else
				strWord = "";
		}
		return strWord;
	}
	
		
	final static public String  quotedString(String szWord)
	{
		if(szWord==null)
			return "\"\"";
		return "\"" + StringUtils.replace(szWord.trim(), "\"", "'") + "\"";
	}
	
	final static public boolean isNumber(String pszNum)
	{
		String szNum = trimString(pszNum);
		int sl = szNum.length();
		if (sl<1) return false;
		int sp=0;
		while(sp<sl && (szNum.charAt(sp)==' ' ||  szNum.charAt(sp)== 9)) sp++;
		if(sp<sl && (szNum.charAt(sp)=='-' || szNum.charAt(sp)=='+')) sp++;
		if( sp==sl || (sp+1==sl && szNum.charAt(sp)=='.')) return false;//
		while(sp<sl){
			if(szNum.charAt(sp)>='0' &&  szNum.charAt(sp)<= '9'){
				sp++;
				continue;
			}
			if(szNum.charAt(sp) == '.') {
				sp++;
				break;
			}
			return false;
		}

		//if ((sp==sl) && (sl>1) && (szNum.charAt(sp-1)!='.') && (szNum.charAt(0)=='0')) return false;;

		while(sp<sl){
			if(szNum.charAt(sp)>='0' &&  szNum.charAt(sp)<= '9'){
				sp++;
			}else
				return false;
		}	
		return true;
	}
	
	final static public boolean isString(String szWord)
	{
		if(szWord==null)
			return false;		
		String strWord = szWord.trim();
		int	sl = strWord.length();
		return (sl >= 2 && (( strWord.charAt(0)=='\"' && strWord.charAt(sl-1)=='\"') || 
				   ( strWord.charAt(0)=='\'' && strWord.charAt(sl-1)=='\'')) );
	}
	
	/**
	 * 判断字符串是否为空(null || ""),是：true,否：false
	 * 和StringUtils中的isBlank等价
	 * @param str
	 * @return boolean
	 */
	public static boolean isNvl(String str) {
		return (str == null) || "".equals(str.trim());
	}


	final static public boolean isTrue(String szCondition )
	{
		if(szCondition==null)
			return false;
		String szC = trimString(szCondition);
		if(szC.equalsIgnoreCase("true") ) return true;
		if(! isNumber(szCondition)) return false;
		int bRes = Double.valueOf(szCondition).intValue();
		return bRes!=0;
	}

	final static public boolean isDatetime(String szTime ,Calendar  t_time)
	{
		if(szTime==null)
			return false;
		//t_time.setTime(time)
		int sl = szTime.length();
		int sp=0;
		int s=0;
		String c="";
		int y,m,d,h,min,sec;
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<4) 
					c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime[sp] == '-') 
				sp++; 
			break;
		}//check year		
		y = Integer.valueOf(c);
		if(y < 1970 || y>2038) return false;
		
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		s=0; c="";
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2)
					c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime.charAt(sp) == '-') 
			sp++; 
			break;
		}//check month
		if(s > 2) return false;
		m = Integer.valueOf(c);
		if(m<1 || m >12) return false;
		
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		s=0; c="";
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2) c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime.charAt(sp) == '-') 
				sp++; 
			break;
		}//check day
		if(s > 2) return false;
		d = Integer.valueOf(c);
		if(d<1 || d >31) return false;
		
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		s=0; c="";
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2) c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime.charAt(sp) == ':') 
				sp++; 
			break;
		}//check hour
		if(s > 2) return false;
		h = Integer.valueOf(c);
		if(h<0 || h >24) return false;
		
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		s=0; c="";
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2) c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime.charAt(sp) == ':') 
				sp++; 
			break;
		}//check minute
		if(s > 2) return false;
		min = Integer.valueOf(c);
		if(min<0 || min >60) return false;
		s=0;  c="";
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2) c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			break;
		}//check second
		if(s > 2) return false;
		sec = Integer.valueOf(c);
		if(sec<0 || sec >60) return false;
		t_time.set(y,m,d,h,min,sec);
		return true;
	}
	
	final static public boolean isDatetime(String szTime ){
		Calendar t_time = Calendar.getInstance();
		boolean b = isDatetime(szTime ,t_time);
		return b;
	}
	
	final static public boolean isDate(String szTime ,Calendar  t_time)
	{
		if(szTime==null)
			return false;
		//t_time.setTime(time)
		int sl = szTime.length();
		int sp=0;
		int s=0;
		String c="";
		int y,m,d;
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<4) 
					c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime[sp] == '-') 
				sp++; 
			break;
		}//check year		
		y = Integer.valueOf(c);
		if(y < 1970 || y>2038) return false;
		
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		s=0; c="";
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2)
					c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime.charAt(sp) == '-') 
			sp++; 
			break;
		}//check month
		if(s > 2) return false;
		m = Integer.valueOf(c);
		if(m<1 || m >12) return false;
		
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		s=0; c="";
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2) c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime.charAt(sp) == '-') 
				sp++; 
			break;
		}//check day
		if(s > 2) return false;
		d = Integer.valueOf(c);
		if(d<1 || d >31) return false;
		
		t_time.set(y,m,d,0,0,0);	
		return true;
	}

	final static public boolean isDate(String szTime ){
		Calendar t_time = Calendar.getInstance();
		boolean b = isDate(szTime ,t_time);
		return b;
	}
	
	final static public boolean isTime(String szTime ,Calendar   t_time)
	{
		if(szTime==null)
			return false;
		//t_time.setTime(time)
		int sl = szTime.length();
		int sp=0;
		int s=0;
		String c="";
		int h,min,sec;
		
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		s=0; c="";
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2) c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime.charAt(sp) == ':') 
				sp++; 
			break;
		}//check hour
		if(s > 2) return false;
		h = Integer.valueOf(c);
		if(h<0 || h >24) return false;
		
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		s=0; c="";
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2) c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			//if(szTime.charAt(sp) == ':') 
				sp++; 
			break;
		}//check minute
		if(s > 2) return false;
		min = Integer.valueOf(c);
		if(min<0 || min >60) return false;
		s=0;  c="";
		while(sp<sl && (szTime.charAt(sp)<'0' || szTime.charAt(sp)> '9')) sp++;
		while(sp<sl ){
			if(szTime.charAt(sp)>='0' &&  szTime.charAt(sp)<= '9'){
				if(s<2) c += szTime.charAt(sp);
				sp++;s++;
				continue;
			}
			break;
		}//check second
		if(s > 2) return false;
		sec = Integer.valueOf(c);
		if(sec<0 || sec >60) return false;
		t_time.set(2010,10,10,h,min,sec);
		return true;
	}

	final static public boolean isTime(String szTime ){
		Calendar t_time = Calendar.getInstance();
		boolean b = isTime(szTime ,t_time);
		return b;
	}
	
	final static public String trimDateString(String szDateStr)
	{
		if(szDateStr==null)
			return null;
		int sl = szDateStr.length();
		StringBuilder sB = new StringBuilder();
		String sTmp2 ="";
		int nPart = 0;
		boolean bDot = false;
		for( int j=0; j< sl; j++){
			if (szDateStr.charAt(j) >= '0' && szDateStr.charAt(j) <= '9'){
				if (bDot){
					if(!"".equals(sTmp2)){
						
						if(nPart>0 && nPart<3)
							sB.append('-'); 
						else if(nPart==3)
							sB.append(' ');
						else if(nPart>3 && nPart<6)
							sB.append(':');
						
						nPart ++;
						
						if(sTmp2.length() == 1){
							sB.append('0');
						}
						
						if(sTmp2.length()>0)
							sB.append(sTmp2);
						
						sTmp2 ="";
						
						
					}
					bDot = false;
				}
				if(nPart>=6)
					break;
				sTmp2 = sTmp2 + szDateStr.charAt(j);
			}else{ //if(! sTmp2.equals(""))
				bDot = true;				
			}
			
		}
		
		if(!"".equals(sTmp2)){
			
			if(nPart>0 && nPart<3)
				sB.append('-'); 
			else if(nPart==3)
				sB.append(' ');
			else if(nPart>3 && nPart<6)
				sB.append(':');
			
			if(sTmp2.length() == 1){
				sB.append('0');
			}
			
			if(sTmp2.length()>0)
				sB.append(sTmp2);
		
		}
		
		return sB.toString();
	}
	
	final static public String trimDigits(String szDigits)
	{
		if(szDigits==null)
			return null;
		int sl = szDigits.length();
		StringBuilder sTmp2 = new StringBuilder("");
		for( int j=0; j< sl; j++){
			if (szDigits.charAt(j) >= '0' && szDigits.charAt(j) <= '9')
				sTmp2.append(szDigits.charAt(j));
		}
		return sTmp2.toString();
	}
	
	
	
	final static public String trimNumber(String szNumber)
	{
		if(szNumber==null)
			return null;
		int sp=0;
		int sl = szNumber.length();
		StringBuilder sTmp2 = new StringBuilder("");
		for( int j=0; j< sl; j++){
			if (szNumber.charAt(j) >= '0' && szNumber.charAt(j) <= '9')
				sTmp2.append(szNumber.charAt(j));
			else if(sp==0 && szNumber.charAt(j) == '.'){
				sTmp2.append(szNumber.charAt(j));
				sp=1;
			}
		}
		return sTmp2.toString();
	}
	
	final static public String sqlMatchToRegex(String sTempl){
		return "^"+sTempl.replaceAll("%", "\\\\S*").replaceAll("_", "\\\\S")+"$";
	}
	
	/*{
			if(szValue==null || szTempl==null)
				return false;	
			int nLV = szValue.length();
			int nLT = szTempl.length();
			if(nLV == 0){
				for(int i=0;i<nLT;i++)
					if(szTempl.charAt(i)!='*')
						return  false;
				return true;
			}
			int i =0,j=0;
			while(i<nLV && j<nLT){
				if( (szTempl.charAt(j) != '*' && szValue.charAt(i) == szTempl.charAt(j)) ||
					szTempl.charAt(j) == '?'){
					i++;j++;
				}else if(szTempl.charAt(j) == '*'){
					if(szTempl.charAt(j+1) == '*'){
						if (szValue.charAt(i) == '*'){
							i++; j+=2;
						}else return false;
					} else {
						j++;
						while(j<nLT && (szTempl.charAt(j)=='?' )){
							j++;
							i++;
						}
						int nStart = j;
						if(nStart == nLT) return true;
						while(j<nLT && szTempl.charAt(j)!='?' && szTempl.charAt(j)!='*') j++;
						int nSubStr = j-nStart;
	
						for(int k=i;k<=nLV-nSubStr;k++){
							boolean bMatchSub = true;
							for(int l=nStart;l<j;l++)
								if(szValue.charAt(k+l-nStart) != szTempl.charAt(l)){
									bMatchSub = false;
									break;
								}
							if(bMatchSub && innerMatch(szValue+k+nSubStr ,szTempl+j)) return true;
						}
						return false;
					}
				}else
					return false;
			}
			return (i==nLV && j==nLT);
		}
	*/
	final static public boolean  isMatch(String szValue , String szTempl)
	{// ?_  *% 是通配符
		if( szValue == null || szTempl == null) return false;
		if( szValue.equals(szTempl)) return true;
		int nLV = szValue.length();
		int nLT = szTempl.length();
		if(nLV == 0 && nLT == 0) return true;

		szValue = trimString(szValue);
		szTempl = trimString(szTempl);
		
		//return Pattern.matches(sqlMatchToRegex(szTempl), szValue);//
		return Pattern.compile(sqlMatchToRegex(szTempl)).matcher(szValue).find();
	}

	/**
	 * 判断是否为中文
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
	/**
	 * 判断是否为中文，剔除标点符号
	 * @param c
	 * @return
	 */
	public static boolean isChineseEscapeSymbol(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS 
        		|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A 
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                //|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION 
                //|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                //|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                ) {
            return true;
        }
        return false;
    }
	/**
	 * 获得首个中文位置 
	 * @param strName
	 * @return -1 没有中文
	 */
    public static int getFirstChinesePos(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
	 * 获得首个中文位置 ，标点符号不算
	 * @param strName
	 * @return -1 没有中文
	 */
    public static int getFirstChinesePosEscapeSymbol(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChineseEscapeSymbol(c)) {
                return i;
            }
        }
        return -1;
    }
	
}
