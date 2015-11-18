package info.pkern.hackerrank.tools;

import java.util.ArrayList;
import java.util.List;

//TODO Rename to StringListConverter(Util)
//TODO Move to ./util(s)
public class ListTypeConverter {

	public static List<Integer> toInteger(String[] stringArray) {
		List<Integer> integerList = new ArrayList<>(stringArray.length);
		for (int i = 0; i < stringArray.length; i++) {
			integerList.add(Integer.parseInt(stringArray[i]));
		}
		return integerList;
	}

	public static List<Integer> toInteger(List<String> stringList) {
		List<Integer> integerList = new ArrayList<>(stringList.size());
		for (String string : stringList) {
			integerList.add(Integer.parseInt(string));
		}
		return integerList;
	}
	
	public static <E> String toSpaceSeparatedString(List<E> list) {
		StringBuilder sb = new StringBuilder();
		for (E entry : list) {
			sb.append(entry);
			if (list.indexOf(entry) < list.size()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	public static double[] toPrimitiveDouble(List<Double> doubleObjects) {
		double[] primitive = new double[doubleObjects.size()];
		for (int i = 0; i < primitive.length; i++) {
			primitive[i] = doubleObjects.get(i);
		}
		return primitive;
	}
	
	public static double[] toPrimitiveDouble(String[] stringArray) {
		double[] primitive = new double[stringArray.length];
		for (int i = 0; i < primitive.length; i++) {
			primitive[i] = Double.parseDouble(stringArray[i].trim());
		}
		return primitive;
	}
	
	public static double[] getPrimitive(List<Double> object) {
		double[] primitive = new double[object.size()];
		for (int i = 0; i < primitive.length; i++) {
			primitive[i] = object.get(i);
		}
		return primitive;
	}
}
