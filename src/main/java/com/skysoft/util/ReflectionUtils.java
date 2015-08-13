package com.skysoft.util;
import java.lang.annotation.Inherited;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;

/**
 * Java���乤����
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ReflectionUtils {
	static {
		DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(Constant.PATTERNS);
		ConvertUtils.register(dc, Date.class);
	}

	/**
	 * ����Getter����
	 */
	public static Object invokeGetterMethod(Object target, String propertyName) {
		String getterMethodName = "get" + org.apache.commons.lang3.StringUtils.capitalize(propertyName);
		return invokeMethod(target, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * ����Getter����
	 */
	public static Object invokeGetMethod(Object target, String methodName) {
		return invokeMethod(target, methodName, new Class[] {}, new Object[] {});
	}

	/**
	 * ����Setter����.ʹ��value��Class������Setter����.
	 */
	public static void invokeSetterMethod(Object target, String propertyName, Object value) {
		invokeSetterMethod(target, propertyName, value, null);
	}

	/**
	 * ����Setter����.
	 *
	 * @param propertyType
	 *            ���ڲ���Setter����,Ϊ��ʱʹ��value��Class���
	 */
	public static void invokeSetterMethod(Object target, String propertyName, Object value, Class<?> propertyType) {
		Class<?> type = propertyType != null ? propertyType : value.getClass();
		String setterMethodName = "set" + org.apache.commons.lang3.StringUtils.capitalize(propertyName);
		invokeMethod(target, setterMethodName, new Class[] { type }, new Object[] { value });
	}

	/**
	 * ֱ�Ӷ�ȡ��������ֵ, ����private/protected���η�, ������getter����.
	 */
	public static Object getFieldValue(final Object object, final String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("ֱ�Ӷ�ȡ��������ֵ�����쳣", e);
		}
		return result;
	}

	/**
	 * ֱ�����ö�������ֵ, ����private/protected���η�, ������setter����.
	 */
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("ֱ�����ö�������ֵ�����쳣", e);
		}
	}

	/**
	 * ֱ�ӵ��ö��󷽷�, ����private/protected���η�.
	 */
	public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
									  final Object[] parameters) {
		Method method = getDeclaredMethod(object.getClass(), methodName, parameterTypes);

		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
		}
		method.setAccessible(true);
		try {
			return method.invoke(object, parameters);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * ������ʱ��checked exceptionת��Ϊunchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		return convertReflectionExceptionToUnchecked(null, e);
	}

	/**
	 * ������ʱ��checked exceptionת��Ϊunchecked exception(����)
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(String desc, Exception e) {
		desc = (desc == null) ? "Unexpected Checked Exception." : desc;
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(desc, e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(desc, ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException(desc, e);
	}

	/**
	 * ѭ������ת��, ��ȡ�����DeclaredMethod. ������ת�͵�Object���޷��ҵ�, ����null.
	 */
	protected static Method getDeclaredMethod(Class target, String methodName, Class<?>[] parameterTypes) {
		if (null == target) {
			return null;
		}
		for (Class<?> superClass = target; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method���ڵ�ǰ�ඨ��,��������ת��
				continue;
			}
		}
		return null;
	}

	/**
	 * ѭ������ת��, ��ȡ�����DeclaredField. ������ת�͵�Object���޷��ҵ�, ����null.
	 */
	protected static Field getDeclaredField(final Object object, final String fieldName) {
		if (null == object || null == fieldName || fieldName.equals("")) {
			return null;
		}
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field���ڵ�ǰ�ඨ��,��������ת��
				continue;
			}
		}
		return null;
	}

	/**
	 * ǿ������Field�ɷ���
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * ͨ������, ��ö���Classʱ�����ĸ���ķ��Ͳ���������,���޷��ҵ�, ����Object.class ��public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz
	 * @param index
	 *            ���෺�Ͳ�������������0��ʼ����
	 * @return Class ���ظ���indexλ�õķ��Ͳ�����class
	 */
	public static Class getSuperClassGenricType(final Class clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

	/**
	 * ͨ������, ��ö���Classʱ�����ĸ���ķ��Ͳ��������ͣ� Ĭ��index����0,���޷��ҵ�, ����Object.class
	 *
	 * @param clazz
	 * @return Class ���ظ���indexλ�õķ��Ͳ�����class
	 */
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * ��ȡ�����еĶ����ĳ���Ե�����ֵ(ͨ��getter����), ��ϳ�List.
	 *
	 * @param collection
	 *            ���ݼ���.
	 * @param propertyName
	 *            Ҫ��ȡ��������.
	 */
	public static List convertElementPropertyToList(final Collection collection, final String propertyName) {
		List list = new ArrayList();
		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
		return list;
	}

	/**
	 * ��ȡ�����еĶ����ĳ���Ե�����ֵ(ͨ��getter����), ��ϳ�����.
	 *
	 * @param collection
	 *            ���ݼ���.
	 * @param propertyName
	 *            Ҫ��ȡ��������.
	 */
	public static Object[] convertElementPropertyToArray(final Collection collection, final String propertyName) {
		Object[] arrays = new Object[collection.size()];
		try {
			int index = 0;
			for (Object obj : collection) {
				arrays[index] = PropertyUtils.getProperty(obj, propertyName);
				index++;
			}
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
		return arrays;
	}

	/**
	 * ��ȡ�����еĶ����ĳ���Ե�����ֵ(ͨ��getter����), ��ָ���ķָ���ָ�����ַ���.
	 *
	 * @param collection
	 *            ���ݼ���.
	 * @param propertyName
	 *            Ҫ��ȡ��������.
	 * @param separator
	 *            �ָ���.
	 */
	public static String convertElementPropertyToString(final Collection collection, final String propertyName,
														final String separator) {
		List list = convertElementPropertyToList(collection, propertyName);
		return org.apache.commons.lang3.StringUtils.join(list, separator);
	}

	/**
	 * ��ȡ�����еĶ����ĳ���Ե�����ֵ(ͨ��getter����), 
	 * ��ָ���ķָ���ָ�����ַ�����Ĭ�Ϸָ����Ƕ���
	 *
	 * @param collection
	 *            ���ݼ���.
	 * @param propertyName
	 *            Ҫ��ȡ��������.
	 */
	public static String convertElementPropertyToString(final Collection collection, final String propertyName) {
		return convertElementPropertyToString(collection,propertyName,",");
	}

	/**
	 *����ժҪ�����ĳ�������Ƿ����ĳ����
	 *@param propertyName  ��������
	 *@return boolean �Ƿ����
	 */
	public static boolean hasThisFieldOfSelf(Class target,String propertyName){
		Field[] fields = target.getDeclaredFields();
		if(GerneralUtils.isEmptyArray(fields)) {
			return false;
		}
		for (Field field : fields) {
			if(field.getName().equals(propertyName)) {
				return true;
			}
		}
		return false;
	}



	/**
	 *����ժҪ�����ĳ���Ƿ����ĳ����
	 *@param propertyName  ��������
	 *@return boolean �Ƿ����
	 */
	public static boolean hasThisField(Class target,String propertyName){
		List<Field> fieldList = new ArrayList<Field>();
		getFields(fieldList, target, true);
		return true;
	}

	/**
	 * ת���ַ�������Ӧ����.
	 *
	 * @param value  ��ת�����ַ���
	 * @param toType ת��Ŀ������
	 */
	public static <T>T convertStringToObject(String value, Class<T> toType) {
		if(null == value) {
			return null;
		}
		if(value.equals("") && !toType.getName().equals(String.class.getName())) {
			return null;
		}
		try {
			return (T) ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * �ж�Child�����Ƿ���parent���͵��ӽӿڻ�ʵ����
	 * @param parent
	 * @param child
	 * @return
	 */
	public static boolean isAssignableFrom(Class parent,Class child) {
		return parent.isAssignableFrom(child);
	}

	/**
	 * �ж�ĳ���ӿ��Ƿ�ʵ��ָ���ӿ�
	 * @param target
	 * @param interfaceClazz
	 * @return
	 */
	public static boolean isImplementInterface(Class target,Class interfaceClazz) {
		if(null == target || null == interfaceClazz) {
			return false;
		}
		Class[] faces = target.getInterfaces();
		if(GerneralUtils.isEmptyArray(faces) || !Modifier.isInterface(interfaceClazz.getModifiers())) {
			return false;
		}
		//�ݹ��жϸ��ӿ��Ƿ�ʵ��ָ���ӿ�
		for (Class face : faces) {
			if(face.getName().equals(interfaceClazz.getName())) {
				return true;
			}
			Class[] parentFaces = face.getInterfaces();
			if(GerneralUtils.isEmptyArray(parentFaces)) {
				return false;
			}
			for (Class parentFace : parentFaces) {
				if(face.getName().equals(interfaceClazz.getName())) {
					return true;
				} else if(isImplementInterface(parentFace, interfaceClazz)) {
					return true;
				}
			}
		}
		//�жϸ����Ƿ�ʵ��ָ���ӿ�
		Class parentClazz = target.getSuperclass();
		if(null != parentClazz) {
			return isImplementInterface(parentClazz, interfaceClazz);
		}
		return false;
	}

	/**
	 * �ж�ĳ���Ƿ����ָ������
	 * @param methodName       ��������
	 * @param clazz            ����������
	 * @param parameterTypes   ������������
	 * @return
	 */
	public static boolean hasThisMethod(String methodName,Class clazz,Class<?>[] parameterTypes) {
		Method method = getDeclaredMethod(clazz, methodName, parameterTypes);
		return null != method;
	}

	/**
	 * �ж�ĳ�������Ƿ����ָ������(�������̳еķ���)
	 * @param methodName       ��������
	 * @param clazz            ����������
	 * @param parameterTypes   ������������
	 * @return
	 */
	public static boolean hasThisMethodOfSelf(String methodName,Class clazz,Class<?>[] parameterTypes) {
		Method method = getDeclaredMethod(clazz, methodName, parameterTypes);
		return null != method;
	}

	/**
	 * ��ȡ��ǰ��ĸ�������з�Private��static����
	 * @param clazz            ���������
	 * @return
	 */
	public static void getParentFields(List<Field> fieldList,Class clazz) {
		if(null == clazz) {
			return;
		}
		Field[] fields = clazz.getDeclaredFields();
		if(GerneralUtils.isNotEmptyArray(fields)) {
			for(Field field : fields){
				//ֻ��ӷ�Private��static����
				if(!Modifier.isPrivate(field.getModifiers()) &&
						!Modifier.isStatic(field.getModifiers())) {
					fieldList.add(field);
				}
			}
		}
		if(clazz.getSuperclass() == Object.class ) {
			return;
		}
		getParentFields(fieldList, clazz.getSuperclass());
	}

	/**
	 * ��ȡ����������� 
	 * @param methodList            ����Ŀ��洢����
	 * @param clazz                 ���������
	 * @param includeParent         �Ƿ�����Ӹ���̳е�����
	 */
	public static void getFields(List<Field> fieldList,Class clazz,boolean includeParent){
		if(null == fieldList) {
			fieldList = new ArrayList<Field>();
		}
		Field[] fields = clazz.getDeclaredFields();
		if(GerneralUtils.isNotEmptyArray(fields)) {
			for (Field method : fields) {
				fieldList.add (method);
			}
		}
		if (includeParent) {
			getParentFields(fieldList, clazz.getSuperclass());
		}
	}

	/**
	 * ��ȡ��������з�˽�зǳ����ҷǾ�̬����
	 * @param clazz     ���������
	 * @return
	 */
	public static void getParentMethods(List<Method> methodList,Class clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		if(GerneralUtils.isNotEmptyArray(methods)) {
			for(Method method : methods){
				//ֻ��ӷ�Private��abstract��static����
				if(!Modifier.isPrivate(method.getModifiers()) &&
						!Modifier.isAbstract(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
					methodList.add(method);
				}
			}
		}
		if(clazz.getSuperclass() == Object.class) {
			return;
		}
		getParentMethods(methodList, clazz.getSuperclass());
	}

	/**
	 * ��ȡ������з���
	 * @param methodList            ����Ŀ��洢����
	 * @param clazz                 ���������
	 * @param includeParent         �Ƿ�����Ӹ���̳еķ���
	 */
	public static void getMothds(List<Method> methodList,Class clazz,boolean includeParent){
		if(null == methodList) {
			methodList = new ArrayList<Method>();
		}
		Method[] methods = clazz.getDeclaredMethods();
		if(GerneralUtils.isNotEmptyArray(methods)) {
			for (Method method : methods) {
				methodList.add (method);
			}
		}
		if (includeParent) {
			getParentMethods(methodList, clazz.getSuperclass());
		}
	}

	/**
	 * ��ȡ���ж���˽������(�������̳е�����)
	 * @param clazz    ���������
	 * @return
	 */
	public static void getSelfPrivateField(List<Field> fieldList,Class clazz) {
		if(null == clazz) {
			return;
		}
		if(null == fieldList) {
			fieldList = new ArrayList<Field>();
		}
		getFields(fieldList, clazz, false);
		if(GerneralUtils.isNotEmptyCollection(fieldList)) {
			for (Iterator<Field> it = fieldList.iterator(); it.hasNext();) {
				Field field = it.next();
				//������private����,�Ӽ�����ɾ����
				if(!Modifier.isPrivate(field.getModifiers())) {
					it.remove();
				}
			}
		}
	}

	/**
	 * �ж��������Ƿ���ָ�����͵�ע��
	 * @param field              ���������
	 * @param annotationClass    ע������
	 * @return
	 */
	public static boolean hasThisAnnotationOfField(Field field,Class annotationClass) {
		if(null == field || null == annotationClass) {
			return false;
		}
		return field.isAnnotationPresent(annotationClass);
	}

	/**
	 * �жϷ������Ƿ���ָ�����͵�ע��
	 * @param method              ����ⷽ��
	 * @param annotationClass     ע������
	 * @return
	 */
	public static boolean hasThisAnnotationOfMethod(Method method,Class annotationClass) {
		if(null == method || null == annotationClass) {
			return false;
		}
		return method.isAnnotationPresent(annotationClass);
	}

	/**
	 * �ж������Ƿ���ָ�����͵�ע��
	 * @param target              ���������
	 * @param annotationClass     ע������
	 * @return
	 */
	public static boolean hasThisAnnotationOfClass(Class target,Class annotationClass) {
		if(null == target || null == annotationClass) {
			return false;
		}
		if(target.isAnnotationPresent(annotationClass)) {
			return true;
		}
		//��ָ��ע��û�����@Inheritedע�⣬����ע�ⲻ�ɱ��̳�
		if(!annotationClass.isAnnotationPresent(Inherited.class)) {
			return false;
		}
		//�ݹ��ж��Ƿ��дӸ����ͼ̳��˸�ע�⣬ǰ���Ǹ�ע���ǿɱ��̳е�ע�⣬����ע�ⱻ@Inheritedע��(ע�⣺ֻ�����϶����ע��ſ��Ա��̳�)
		Class parentClass = target.getSuperclass();
		return hasThisAnnotationOfClass(parentClass,annotationClass);
	}

	public static void main(String[] args) {
		String s = "01/31/2013 15:28:09qqq";
		Date date = convertStringToObject(s, Date.class);
		String t = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);
		System.out.println(t);
	}
}
