package info.pkern.sandbox;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class TestGeneric {

    List<String> stringList = new ArrayList<String>();
    static List<Integer> integerList = new ArrayList<Integer>();

    public static void main(String... args) throws Exception {
        Field stringListField = TestGeneric.class.getDeclaredField("stringList");
        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        System.out.println(stringListClass); // class java.lang.String.

        Field integerListField = TestGeneric.class.getDeclaredField("integerList");
        ParameterizedType integerListType = (ParameterizedType) integerListField.getGenericType();
        Class<?> integerListClass = (Class<?>) integerListType.getActualTypeArguments()[0];
        System.out.println(integerListClass); // class java.lang.Integer.
        
        ListNumberPopulator.zeroPopulate(new TestGeneric(), "integerList", 0);
        
        ListNumberPopulator.zeroPopulate(integerList, 0);
        
    }
    
    
    static class ListNumberPopulator {
    	
    	public static <T extends Number> void zeroPopulate(List<T> list, int numberOfElements) {
    		ParameterizedType listType = (ParameterizedType) list.getClass().getGenericSuperclass();
            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
            System.out.println(listClass);

    	}
    	
    	public static void zeroPopulate(Object obj, String fieldName, int numberOfElements) throws Exception {
            Field listField = obj.getClass().getDeclaredField(fieldName);
            ParameterizedType listType = (ParameterizedType) listField.getGenericType();
            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
            System.out.println(listClass);
    	}
    	
    }
}