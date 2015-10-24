package info.pkern.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {

	public static enum SORT_ORDER{ASC, DESC};
	
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortByValuesDescending(
			Map<K, V> map) {
		return sortByValues(map, null);
	}
	
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortByValues(Map<K, V> map, final SORT_ORDER sortOrder) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				if (sortOrder != null && sortOrder.equals(SORT_ORDER.ASC)) {
					return e1.getValue().compareTo(e2.getValue());
				} else {
					return e2.getValue().compareTo(e1.getValue());
				}
			}
		});

		return sortedEntries;
	}
	
}
