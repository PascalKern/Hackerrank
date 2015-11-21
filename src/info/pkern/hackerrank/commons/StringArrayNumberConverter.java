package info.pkern.hackerrank.commons;

import java.util.ArrayList;
import java.util.List;

public class StringArrayNumberConverter {

	public static List<Integer> toInteger(String[] stringArray) {
		List<Integer> integerList = new ArrayList<>(stringArray.length);
		for (int i = 0; i < stringArray.length; i++) {
			integerList.add(Integer.parseInt(stringArray[i]));
		}
		return integerList;
	}

	public static List<Double> toDouble(String[] stringArray) {
		List<Double> doubleList = new ArrayList<>(stringArray.length);
		for (int i = 0; i < stringArray.length; i++) {
			doubleList.add(Double.parseDouble(stringArray[i]));
		}
		return doubleList;
	}

	public static StringBuilder joinWithSpaceSeparator(List<?> list) {
		return joinWithSeparator(list, " ");
	}

	public static StringBuilder joinWithSeparator(List<?> list, String separator) {
		StringBuilder sb = new StringBuilder();
		if (list.isEmpty()) {
			return sb;
		}
		String internSeparator = (null == separator)?",":separator;
		for (Object entry : list) {
			sb.append(entry.toString()).append(internSeparator);
		}
		sb.delete(sb.lastIndexOf(internSeparator), sb.length());
		return sb;
	}
}
