package info.pkern.hackerrank.tools;

import java.util.ArrayList;
import java.util.List;

public class ListTypeConverter {

	public static List<Integer> toInteger(String[] stringArray) {
		List<Integer> integerList = new ArrayList<>(stringArray.length);
		for (int i = 0; i < stringArray.length; i++) {
			integerList.add(Integer.parseInt(stringArray[i]));
		}
		return integerList;
	}
	
}
