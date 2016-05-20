package com.centit.support.algorithm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  提供一些反射方面缺失功能的封装.
 */
public class ReflectionOpt  {

	protected static final Log log = LogFactory.getLog(ReflectionOpt.class);

	private ReflectionOpt() {
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
		assert(object != null); 
		assert(propertyName != null && !propertyName.isEmpty());

		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Class<?> clazz, String propertyName) throws NoSuchFieldException {
		assert(clazz != null); 
		assert(propertyName != null && !propertyName.isEmpty());
		//Assert.notNull(object);
		//Assert.hasText(propertyName);
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field f = superClass.getDeclaredField(propertyName);
				if(f!=null)
					return f;
			} catch (NoSuchFieldException e) {
				log.debug(e.getMessage());
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}

	/**
	 * 获得对象的属性值
	 * @param object
	 * @param field
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Object forceGetFieldValue(Object object,Field field )  throws NoSuchFieldException {
		assert(object != null); 
	
		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			log.info("error wont' happen."+ e.getMessage());
		}
		field.setAccessible(accessible);
		return result;
	}
	

	/**
	 * 获得get field value by getter
	 */
	public static Object getFieldValue(Object obj,  String fieldName) {
		Method md=null;
		try {
			
			md = obj.getClass().getMethod("get" + StringUtils.capitalize(fieldName));
		}catch (NoSuchMethodException noGet ){
			try {			
				md = obj.getClass().getMethod("is" + StringUtils.capitalize(fieldName));
			}catch (Exception e ){
				log.error(e.getMessage(), e);
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		if(md==null)
			return null;
			
		try{
			return md.invoke(obj);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获得get field value by getter
	 */
	public static Object getFieldValue(Object obj,  Field field) {
		try {
			return field.get(obj);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 获取对象变量值,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
		assert(object != null); 
		assert(propertyName != null && !propertyName.isEmpty());

		Field field = getDeclaredField(object, propertyName);
/*		if(field==null){
			log.debug("property not found. (没有找到对应的属性) 对象：" + object.toString() +" 属性 ："+ propertyName);
			return null;
		}*/
		return  forceGetFieldValue(object,field);
	}
	
	/**
	 * 设置对象变量值,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static void forceSetProperty(Object object, String propertyName, Object newValue)
			throws NoSuchFieldException {
		assert(object != null); 
		assert(propertyName != null && !propertyName.isEmpty());

		Field field = getDeclaredField(object, propertyName);
/*		if(field==null){
			log.debug("property not found. (没有找到对应的属性) 对象：" + object.toString() +" 属性 ："+ propertyName);
			return;
		}*/
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			log.info("Error won't happen."+e.getMessage());
		}
		field.setAccessible(accessible);
	}

	/**
	 * 调用对象函数,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchMethodException 如果没有该Method时抛出.
	 */
	public static Object invokePrivateMethod(Object object, String methodName, Object... params)
			throws NoSuchMethodException {
		assert(object != null); 
		assert(methodName != null && !methodName.isEmpty());
		
		Class<?>[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class<?> clazz = object.getClass();
		Method method = null;
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				log.debug("方法不在当前类定义,继续向上转型");
				// 方法不在当前类定义,继续向上转型
			}
		}

		if (method == null)
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			//ReflectionUtils.handleReflectionException(e);
			log.error(e.getMessage() );
			e.printStackTrace();
		}
		method.setAccessible(accessible);
		return result;
	}

	/**
	 * 取得Field列表.
	 */
	public static Field[] getFields(Object object) {
		return object.getClass().getDeclaredFields();
	}
	
	/**
	 * 按Filed的类型取得Field列表.
	 */
	public static List<Field> getFieldsByType(Object object, Class<?> type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * 按FiledName获得Field的类型.
	 */
	public static Class<?> getPropertyType(Class<?> type, String name) throws NoSuchFieldException {
		return getDeclaredField(type, name).getType();
	}

	/**
	 * 获得field的getter函数名称.
	 */
	public static String getGetterName(Class<?> type, String fieldName) {
		assert(type != null); 
		assert(fieldName != null && !fieldName.isEmpty());
		//Assert.notNull(type, "Type required");
		//Assert.hasText(fieldName, "FieldName required");

		if (type.getName().equals("boolean")) {
			return "is" + StringUtils.capitalize(fieldName);
		} else {
			return "get" + StringUtils.capitalize(fieldName);
		}
	}

	/**
	 * 获得field的getter函数名称.
	 */
	public static String methodNameToField(String methodName) {
		if(methodName== null)
			return null;
		int sl = methodName.length();
		
		if( sl > 3 && (methodName.startsWith("get") || methodName.startsWith("set") ))
			return methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		
		if( sl > 2 && methodName.startsWith("is"))
			return methodName.substring(2,3).toLowerCase() + methodName.substring(3);
		
		return methodName;
	}
	/**
	 * 获得field的getter函数,如果找不到该方法,返回null.
	 */
	public static Method getGetterMethod(Class<?> classType, Class<?> propertyType, String fieldName) {
		try {
			return classType.getMethod(getGetterName(propertyType, fieldName));
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获得field的getter函数,如果找不到该方法,返回null.
	 */
	public static Method getGetterMethod(Class<?> classType,  String fieldName) {
		try {
			return classType.getMethod("get" + StringUtils.capitalize(fieldName));
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 获得 对象的 属性
	 * @param sourceObj
	 * @param expression
	 * @return
	 */
	public static Object attainExpressionValue(Object sourceObj , String expression){
		if(sourceObj==null || expression==null || "".equals(expression))
			return null;
		if(".".equals(expression))
			return sourceObj;
	
		int nPos = expression.indexOf('.');
		String fieldValue;
		String restExpression=".";
		if(nPos>0){
			fieldValue = expression.substring(0,nPos);
			if(expression.length()>nPos+1)
				restExpression = expression.substring(nPos+1);
		}else
			fieldValue = expression;
		int nAarrayInd = -1;
		nPos = fieldValue.indexOf('[');
		if(nPos>0){
			String sArrayInd = fieldValue.substring(nPos+1,fieldValue.length()-1);
			if(StringRegularOpt.isNumber(sArrayInd))
				nAarrayInd = Double.valueOf(sArrayInd).intValue();
			fieldValue = fieldValue.substring(0,nPos);
		}
		
		Object retObj = null;
		if(sourceObj instanceof Map ){
			@SuppressWarnings("unchecked")
			Map<String ,Object> objMap = (Map<String ,Object>) sourceObj;
			retObj = objMap.get(fieldValue);
		}else{
			retObj = ReflectionOpt.getFieldValue(sourceObj,fieldValue);
		}
		
		if(retObj==null)
			return null;
		
		if(retObj instanceof Collection){
			Collection<?> objlist = (Collection<?>) retObj;
			int objSize = objlist.size();
			if(objSize<1)
				return null;
			
			if(nAarrayInd>=0){
				if( nAarrayInd <objSize){
					int i=0;
					for(Object obj: objlist){
						if(nAarrayInd==i)
							return attainExpressionValue(obj,restExpression); 
						i++;
					}
				}
				return null;
			}else{
				Object [] retObjArray = new Object[objSize];
				int i=0;
				for(Object obj: objlist){
					retObjArray[i] = attainExpressionValue(obj,restExpression); 
					i++;
				}
				return retObjArray;
			}			
		}else if(retObj instanceof Object[]){
			Object[] objs = (Object[]) retObj;
			int objSize = objs.length;
			if(objSize<1)
				return null;
			
			if(nAarrayInd>=0){
				if(nAarrayInd <objSize)
					return attainExpressionValue(objs[nAarrayInd],restExpression); 
				return null;
			}else{
				Object [] retObjArray = new Object[objSize];
				int i=0;
				for(Object obj: objs){
					retObjArray[i] = attainExpressionValue(obj,restExpression); 
					i++;
				}
				return retObjArray;
			}					
		}else{
			return attainExpressionValue(retObj,restExpression);
		}			
	}
	
	/**
	 * 获得get boolean field value by getter
	 */
	public static Boolean getBooleanFieldValue(Object obj,  String fieldName) {
		try {
			Method md = obj.getClass().getMethod("is" + StringUtils.capitalize(fieldName));
			if(md==null)
				return null;
			Object objValue = md.invoke(obj);
			if(objValue==null)
				return null;
			if(objValue instanceof Boolean)
				return (Boolean)objValue;
			return Boolean.valueOf(objValue.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获得boolean 型 field的getter函数,如果找不到该方法,返回null.
	 */
	public static Method getBooleanGetterMethod(Class<?> classType,  String fieldName) {
		try {
			return classType.getMethod("is" + StringUtils.capitalize(fieldName));
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 获取所有getMethod方法
	 */
	public static List<Method> getAllGetterMethod(Class<?> type) {
		try {
			Method[] mths =  type.getMethods();
			List<Method> getMths = new ArrayList<Method>();
			for (Method mth : mths) 
				if( ( mth.getName().startsWith("get") ||  mth.getName().startsWith("is") )
						&& ! mth.getName().equals("getClass")
						&& ! mth.getReturnType().getName().equals("void")
						&& mth.getGenericParameterTypes().length < 1 )
					getMths.add(mth);
			return getMths;
			
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取所有setMethod方法
	 */
	public static List<Method> getAllSetterMethod(Class<?> type) {
		try {
			Method[] mths =  type.getMethods();
			List<Method> setMths = new ArrayList<Method>();
			for (Method mth : mths) 
				if(  mth.getName().startsWith("set") 
						&& mth.getReturnType().getName().equals("void")
						&& mth.getGenericParameterTypes().length == 1 )
					setMths.add(mth);
			return setMths;
			
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 调用对象的无参数函数
	 */
	public static void  invokeNoParamFunc(Object demander,String smethod) {
		try {
			//"copyNotNullProperty"
			Method setV = demander.getClass().getMethod(smethod);
			//Class rt = d.getReturnType();
/*			if(setV == null) 
				return;	*/			
			setV.invoke(demander);			
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.info("没有函数 "+smethod );
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 调用相同类型的类之间的二元操作
	 */
	public static <T extends Object>  void  invokeBinaryOpt(T demander,String smethod,T param) {
		try {
			//"copyNotNullProperty"
			Method setV = demander.getClass().getMethod(smethod ,demander.getClass());
			//Class rt = d.getReturnType();
/*			if(setV == null) 
				return;	*/			
			setV.invoke(demander,param);			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic declaration,start from 0.
	 * @return the index generic declaration, or <code>Object.class</code> if
	 *         cannot be determined
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class<?>) params[index];
	}
	

	/** isPrimitiveType
	 * 判断一个类型是否是基础类型 int boolean void float double char
	 * 或者单值类型 String Date Integer Float Double，  scalar
	 * @param tp
	 * @return
	 */
	public static boolean isPrimitiveType(Class<?>  tp){
		return tp.isPrimitive() || tp.getName().startsWith("java.lang.");
	}
	/** isScalarType
	 * 判断一个类型是否是基础类型 int boolean void float double char
	 * 或者单值类型 String Date Integer Float Double，  scalar
	 * @param tp
	 * @return
	 */
	public static boolean isScalarType(Class<?>  tp){
		if(tp.isPrimitive())
			return true;
		if(tp.getName().startsWith("java.lang."))
			return true;
		if(tp.getName().startsWith("java.sql."))
			return true;
		if("java.util.Date".equals(tp.getName()))
			return true;
		if("java.util.UUID".equals(tp.getName()))
			return true;

		return false;		
	}
	
	public static boolean isNumberType(Class<?>  tp){
		return tp.getSuperclass().equals(Number.class) || tp.equals(Number.class);	
	}
	/**
	 * 判断一个对象是否是 数组[]、Collection(List)
	 * @param tp
	 * @return
	 */
	public static boolean isArray(Object obj){
		Class<?>  tp = obj.getClass();
		if(tp.isArray())
			return true;
		//if(obj instanceof Object[])
		//	return true;
		if(obj instanceof Collection<?>)
			return true;
		return false;
	}
	
	/**
	 * 将两个对象加+起来，可能是数字相加，也可能是字符串连接
	 * @param a
	 * @param b
	 * @return
	 */
	public static Object addTwoObject(Object a,Object b){
		if(a==null)
			return b;
		if(b==null)
			return a;
		
		if( a instanceof java.lang.Number &&  b instanceof java.lang.Number)
			return ((Number) a).doubleValue() + ((Number) b).doubleValue();
		return a.toString() + b.toString();
	}
	
	/**
	 *  得到当前方法的名字
	 * @return
	 */
	public static String getCurrentMethodName(){
		return Thread.currentThread().getStackTrace()[1].getMethodName(); 
	}
}

