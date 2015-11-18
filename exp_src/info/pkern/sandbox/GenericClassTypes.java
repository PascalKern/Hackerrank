package info.pkern.sandbox;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GenericClassTypes {

	@Test
	public void genericClass() {
		GenericClass<Double> doubleClass = new GenericClass<>(1d);
//		GenericClass<Double> doubleClassFailure = new GenericClass<>(1);
		System.out.println(doubleClass);
		System.out.println(doubleClass.getClass());
//		System.out.println(doubleClass.getMyType());
//		System.out.println(doubleClass.getMyType2());
		List<Double> list = new ArrayList<>();
		System.out.println(((ParameterizedType)list.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		System.out.println(((ParameterizedType)list.getClass().getGenericSuperclass()).getOwnerType());
		System.out.println(((ParameterizedType)list.getClass().getGenericSuperclass()).getRawType());
		System.out.println(list.getClass());
		System.out.println(Arrays.toString(list.getClass().getGenericInterfaces()));
		System.out.println(list.getClass().getGenericSuperclass());
	}
	
	private class GenericClass<T extends Number> {
		private T value;
		private T type;
		private List<T> list;
		public GenericClass(){/**/}
		public GenericClass(T value) {
			this.value = value;
		}
		public Object getMyType2() {
			return type.getClass();
		}
		@SuppressWarnings("unchecked")
		public Object getMyType() {
			return (Class<T>)
	                ((ParameterizedType)getClass()
	                .getGenericSuperclass())
	                .getActualTypeArguments()[0];
//			return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
//			return ((Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		}
		@Override
		public String toString() {
			return "value=" + value;
		}
	}
	
	@Test
	public void genericClassType() throws Exception {
	    
	    ListNumberPopulator.getGenericType(new GenericClass2(), "integerList");
	    ListNumberPopulator.getGenericType(new GenericClass2(), "stringList");
	    ListNumberPopulator.getGenericType(new GenericClass2(), "array");
	    ListNumberPopulator.getGenericType(new GenericClass<Double>(), "list");
	    
	    ListNumberPopulator.zeroPopulate(GenericClass2.integerList, 0);
	}
		
    private static class GenericClass2 {
	    private List<String> stringList;
	    static List<Integer> integerList = new ArrayList<Integer>();
	    private double[] array;
	}

	static class ListNumberPopulator {
    	
    	public static <T extends Number> void zeroPopulate(List<T> list, int numberOfElements) {
    		ParameterizedType listType = (ParameterizedType) list.getClass().getGenericSuperclass();
            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
            System.out.println(listClass);

    	}
    	
    	public static void getGenericType(Object obj, String fieldName) throws Exception {
            Field listField = obj.getClass().getDeclaredField(fieldName);
            ParameterizedType listType = (ParameterizedType) listField.getGenericType();
            System.out.println(listType);
            System.out.println(Arrays.toString(listType.getActualTypeArguments()));
            System.out.println(listType.getRawType());
            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
            System.out.println(listClass);
    	}
    	
    	public static void populateZeroedList(Object obj, String fieldName, int numberOfElements) {
    		
    	}
    	
    }
}
