package com.centit.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.support.json.JSONOpt;

public class TestJsonObject {

    public static void main(String arg[]){
    	char [][] aInt = new char[5][2];
      	aInt[0][0] = 1;
      	aInt[1][0] = 2;
      	aInt[2][0] = 3;
      	aInt[3][0] = 4;
      	aInt[4][0] = 5;
      	aInt[0][1] = 10;
      	aInt[1][1] = 20;
      	aInt[2][1] = 30;
      	aInt[3][1] = 40;
      	aInt[4][1] = 50;

      	JSONArray jAray= JSONOpt.arrayToJSONArray(aInt);
    	System.out.println( jAray.toString());	
    	
    	testObjectToJson();

    }
    
    public static void testObjectToJson(){
      /*	B b = new B();
      	A a = new A();

      	a.firstValue = "a1";
      	b.firstValue = 100;
      	a.setSecondValue("a2");
      	b.setThirdValueTemp(a);
      	b.setSecondValue("b2");
      	
      	
      	char [][] aInt = new char[5][2];
      	aInt[0][0] = 1;
      	aInt[1][0] = 2;
      	aInt[2][0] = 3;
      	aInt[3][0] = 4;
      	aInt[4][0] = 5;
      	aInt[0][1] = 1;
      	aInt[1][1] = 2;
      	aInt[2][1] = 3;
      	aInt[3][1] = 4;
      	aInt[4][1] = 5;
      	System.out.println( (ReflectionOpt.isArray(aInt[0]))?"array":"simple");
      	JSONArray jAray= JSONOpt.arrayToJSONArray(aInt);
    	System.out.println( jAray.toString());	
      	
      	JSONObject jObj = JSONOpt.objectToJSONObject(b);
    	System.out.println( jObj.toString());
    	a.setSecondValue("a22");
    	JSONOpt.appendData(jObj , "thirdValue", JSONOpt.objectToJSONObject(a));
    	System.out.println( jObj.toString());*/
        JSONObject json = new JSONObject();
         //json.fromString("{a:{b:[{h:{c:\"hello\"}},{c:[\"chartDiv\",\"chartDiv2\",\"chartDiv3\"]}]}}");
        /*Map<String ,Object> map = new HashMap<String ,Object>();
 
        map.put("renderTo", new Object[]{"chartDiv","chartDiv2","chartDiv3"});
        map.put("defaultSeriesType", "line");
        
        json.appendSeries("renderTo", new Object[]{"chartDiv","chartDiv2","chartDiv3"});
        json.appendSeries("renderTo2", new Object[]{"chartDiv","chartDiv2","chartDiv3"});
        json.appendSeries("renderTo3", new Object[]{"chartDiv","chartDiv2","chartDiv3"});
        
         * //json.appendYAxisPlotLines(map);
        map = new HashMap<String ,Object>();
        map.put("renderTo2", "chartDiv");
        map.put("defaultSeriesType", "line");
        //json.appendYAxisPlotLines(map);
         map = new HashMap<String ,Object>();
        map.put("renderTo2", "chartDiv");
        map.put("defaultSeri", "line");
        JSONOpt.setAttribute(json.getChartJson(), "a.b[0]", map);
        */
        JSONOpt.setAttribute(json, "a.b.c.d.e.f.g", new Object[]{"chartDiv","chartDiv2","chartDiv3"});
        JSONOpt.setAttribute(json, "b.b", "a");
       
        JSONOpt.setAttribute(json, "b.d", "你好");
        
        //json.appendData("series", null);
        //json.appendData("series", new Object[]{"chartDiv","chartDiv2","chartDiv3"});
        //json.appendData("series", new Object[]{"chartDiv4","chartDiv5","chartDiv6"});
        //json.appendXAxisCategories( new Object[]{"chartDiv","chartDiv2","chartDiv3"});
        //json.appendXAxisCategories( new Object[]{"chartDiv","chartDiv2","chartDiv3"});

       System.out.println( json.toString() );
    }
}
