package jackson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TestTypeRerence {
     public static void main(String[] args) {
         
         Type superClass = MyList.class.getGenericSuperclass();
         System.out.println(superClass.getClass());
         System.out.println(superClass);
         Type _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
         System.out.println(_type);
         
 
	}
     
     class MyList extends ArrayList{
    	 
     }
}
