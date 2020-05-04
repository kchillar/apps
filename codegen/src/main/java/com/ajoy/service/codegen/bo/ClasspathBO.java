package com.ajoy.service.codegen.bo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ajoy.model.codegen.ClassInfo;
import com.ajoy.model.codegen.Classes;
import com.ajoy.model.codegen.Classpath;
import com.ajoy.model.codegen.ClasspathEntry;
import com.ajoy.model.codegen.FieldInfo;
import com.ajoy.model.codegen.ResponseCode;
import com.ajoy.model.codegen.SessionInfo;
import com.ajoy.service.codegen.workflow.CallContext;

/**
 * 
 * @author kalyanc
 *
 */
public class ClasspathBO extends BaseBO
{
	private static Logger log = LogManager.getLogger(ClasspathBO.class);
	private ClasspathDAO dao = new ClasspathDAO();
	private ClassloaderBuilder builder;
	private ClassLoader classloader;

	public ClasspathBO()
	{		
	}

	public ResponseCode<Classpath> getClasspath(CallContext context)
	{
		SessionInfo sessionInfo = context.getSessionInfo();
		log.info("getClasspath() start");
		ResponseCode<Classpath> code = dao.readFromStorage(sessionInfo);
		log.info("getClasspath() end");
		return code;
	}


	public ResponseCode<Classpath> updateClasspath(Classpath classpath, CallContext context)
	{
		SessionInfo sessionInfo = context.getSessionInfo();
		log.info("updateClasspath() start"); 		
		ResponseCode<Classpath> code = dao.persistToStore(sessionInfo, classpath);
		log.info("updateClasspath() end");
		return code;
	}

	public ResponseCode<Classes> getClasses(CallContext context)
	{
		log.info("getClasses() start");
		
		ResponseCode<Classes> code = new ResponseCode<>();		
		Classes classes = new Classes();
		code.setObject(classes);

		classes.setClassInfoList(getClassloaderBuilder(context).getClassnamesList());
		code.setSuccess(true);

		log.info("getClasses() end "+classes.getClassInfoList());
		return code;
	}

	public ResponseCode<ClassInfo> getClassDetails(ClassInfo classInfo, CallContext context)
	{
		log.info("getClassInfo() start "+classInfo.getName());	
		ResponseCode<ClassInfo> code = new ResponseCode<>();		
		code.setObject(classInfo);
				
		try
		{
			classInfo.setClassname(classInfo.getName());
			
			processFieldInfo(classInfo, context);
			
			
			ArrayList<String> list = new ArrayList<>();
			
			if(classInfo.getFieldList() != null)
				for(FieldInfo childInfo: classInfo.getFieldList())
					populateList(null, list, childInfo);
			
			for(String path: list)
			{
				log.info("Path: "+path);
			}
			
			code.setSuccess(true);
		}
		catch(Exception exp)
		{
			log.error("Error ", exp);
		}
		log.info("getClassInfo() end ");
		return code;
	}

	
	private void processFieldInfo(FieldInfo fieldInfo, CallContext context)
	{
		log.info("processFieldInfo() start "+fieldInfo.getName());	
				
		try
		{			
			Class<?> objClass = getClassLoader(context).loadClass(fieldInfo.getClassname());				
						
			processClass(objClass, fieldInfo, context);
												
			Class<?> superClass = objClass.getSuperclass();
			
			if( (superClass != null) && (superClass != Object.class))
			{
				log.info("Super Class "+superClass.getName()+" for "+fieldInfo.getName()+" class: "+fieldInfo.getName());
				processClass(superClass, fieldInfo, context);
			}
		}
		catch(Exception exp)
		{
			log.error("Error ", exp);
		}
		log.info("processFieldInfo() end "+fieldInfo.getName());
		return;
	}

	
	private void processClass(Class<?> objClass, FieldInfo fieldInfo, CallContext context)
	{
		log.info("processClass() start "+objClass.getName()+" for fieldName: "+fieldInfo.getName());	
		
		if(fieldInfo.getFieldList() == null)
			fieldInfo.setFieldList(new ArrayList<FieldInfo>());
		
		Field[] fields = objClass.getDeclaredFields();		
		for(Field field: fields)
		{
			log.info("Field: "+field.getName());
			FieldInfo f = new FieldInfo();
			f.setName(field.getName());
			f.setClassname(field.getType().getName());		
			
			if(isUserDefinedClass(field.getType().getName()))
			{					
				f.setUserDefined(true);
				log.info("found User Defined Class: "+objClass.getName());
				processFieldInfo(fieldInfo, context);
				log.info("end User Defined Class: "+objClass.getName());
			}
								
			fieldInfo.getFieldList().add(f);
		}

		log.info("processClass() end "+objClass.getName());
	}
	
	
	private void populateList(String parentPath, List<String> list, FieldInfo fInfo)
	{
		log.info("parentPath: "+parentPath);
		
		if(parentPath != null)		
			parentPath = parentPath+"."+fInfo.getName();			
		else
			parentPath = fInfo.getName();
		
		list.add(parentPath);
		
		if(fInfo.getFieldList() != null)
		for(FieldInfo info: fInfo.getFieldList())
		{
			populateList(parentPath, list, info);
		}
	}
	
	
	private ClassloaderBuilder getClassloaderBuilder(CallContext context)
	{
		if(builder == null)
		{
			ResponseCode<Classpath> aCode = this.getClasspath(context);
			if(aCode.isSuccess())
			{
				if(aCode.getObject().getClasspathEntryList() != null)
				{
					builder = new ClassloaderBuilder();
					for(ClasspathEntry entry: aCode.getObject().getClasspathEntryList())				
						builder.addPath(entry.getPath());
				}
			}
		}		
		return builder;		
	}

	private ClassLoader getClassLoader(CallContext context)
	{
		if(classloader == null)
		{
			classloader = getClassloaderBuilder(context).build();
		}
		return classloader;
	}
	
	private static boolean isUserDefinedClass(String classname)
	{
		if(primitiveTypeMap.get(classname)!=null)
			return false;
		else if(classname.startsWith("java."))
			return false;
		else 
			return true;			
	}
	

	private static final HashMap<String, Object> primitiveTypeMap = new HashMap<String, Object>();
	
	static
	{
		Object obj = new Object();
		
		primitiveTypeMap.put(boolean.class.getName(), obj);
		primitiveTypeMap.put(char.class.getName(), obj);
		primitiveTypeMap.put(short.class.getName(), obj);		
		primitiveTypeMap.put(int.class.getName(), obj);
		primitiveTypeMap.put(long.class.getName(), obj);
		primitiveTypeMap.put(float.class.getName(), obj);
		primitiveTypeMap.put(double.class.getName(), obj);

		primitiveTypeMap.put(boolean[].class.getName(), obj);
		primitiveTypeMap.put(char[].class.getName(), obj);
		primitiveTypeMap.put(short[].class.getName(), obj);		
		primitiveTypeMap.put(int[].class.getName(), obj);
		primitiveTypeMap.put(long[].class.getName(), obj);
		primitiveTypeMap.put(float[].class.getName(), obj);
		primitiveTypeMap.put(double[].class.getName(), obj);
	}
	
	
}
