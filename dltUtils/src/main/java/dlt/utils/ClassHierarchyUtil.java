package dlt.utils;

import java.lang.reflect.Method;
import java.util.HashSet;

/**
 * 类的层次关系辅助类 
 * @author dlt
 *
 */
public class ClassHierarchyUtil {

	public static void printClass(Class<?> aClass){
		printClass(aClass,0);
	}
	private static void printClass (Class<?> aClass,int iLevel){

		String pre =null;
		if (aClass.isInterface()){
			pre = "Interface:";
		}else{
			pre = "Class:";
		}
		System.out.println(getSpace(iLevel)+ pre +aClass.getName());
		int childLevel =iLevel+3;
		Method[] methods =aClass.getDeclaredMethods(); //aClass.getMethods();
		
		for (Method m : methods){
			System.out.println(getSpace(childLevel)+m.toString());
		}	
		Class<?> superClass = aClass.getSuperclass();

		if (superClass != null){		
			printClass(superClass,childLevel);

		}
		Class<?>[] ifs = aClass.getInterfaces();
		for (Class<?> temp:ifs){		
			printClass(temp,childLevel);
		}
	}
	private static String getSpace(int count){
		StringBuilder sb = new StringBuilder();
		
		for (int i=0;i<count;i++){
			sb.append(" ");
		}
		if (count>0){
			sb.append("|--");
		}
		return sb.toString();
	}
	

   public static   Class<?>[]  getAllInterface(Class<?> aClass){
		HashSet<Class<?>> setIF = new HashSet<Class<?>>();
		Class<?>[] ifs =  aClass.getInterfaces();
		Class<?> superClass = aClass.getSuperclass();
		Class<?>[] ifsAll =  ifs ;
		if (superClass != null) {
			ifsAll = new Class[ifs.length + 1];
			System.arraycopy(ifs, 0, ifsAll, 0, ifs.length);
			ifsAll[ifs.length] = superClass;
		}
        for(Class<?> tempClass : ifsAll){
        	if (tempClass.isInterface()){
        		setIF.add(tempClass);
        	}
        	Class<?> [] ifs2 = getAllInterface(tempClass);
        	for (Class<?> tempClass2 :ifs2){
        		setIF.add(tempClass2);
        	}
        }
        return setIF.toArray(new Class<?>[0]);
        
	}	

}

