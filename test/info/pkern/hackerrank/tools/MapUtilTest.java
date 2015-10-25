package info.pkern.hackerrank.tools;

import static org.junit.Assert.*;
import info.pkern.hackerrank.tools.MapUtil;
import info.pkern.hackerrank.tools.MapUtil.SORT_ORDER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;

public class MapUtilTest {

	private static Map<String, Float> probAllClasses = new HashMap<>();
	private static List<Entry<String,Float>> sortedList;
	
	@BeforeClass
	public static void initClass() {
		probAllClasses.put("E", 1f);
		probAllClasses.put("D", 2.2f);
		probAllClasses.put("F", 2.2001f);
		probAllClasses.put("C", 0.1f);
		probAllClasses.put("A", 0.03f);
		probAllClasses.put("B", 0.0001f);
	}
	
	@Test
	public void sortMapByValueDescending() {
		sortedList = MapUtil.sortByValuesDescending(probAllClasses);
		assertEquals(sortedList.get(0).getValue(), (probAllClasses.get("F")));
		assertEquals(sortedList.get(2).getValue(), (probAllClasses.get("E")));
		assertEquals(sortedList.get(5).getValue(), (probAllClasses.get("B")));
		System.out.println("Descending: " + sortedList);
	}

	@Test
	public void sortMapByValueAscending() {
		sortedList = MapUtil.sortByValues(probAllClasses, SORT_ORDER.ASC);
		assertEquals(sortedList.get(0).getValue(), (probAllClasses.get("B")));
		assertEquals(sortedList.get(2).getValue(), (probAllClasses.get("C")));
		assertEquals(sortedList.get(5).getValue(), (probAllClasses.get("F")));
		System.out.println("Ascending: " + sortedList);
	}
}
