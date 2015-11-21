package info.pkern.hackerrank.commons;

import info.pkern.ai.statistic_ml.documentClassification.localClasses.SimpleTable;

import java.util.ArrayList;
import java.util.List;

public class NumberUtil {
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T numberAdder(T leftValue, T rightValue) {
		if (null == leftValue || null == rightValue) {
			throw new IllegalArgumentException("Can not add NULL values! [leftValue="+leftValue
					+ ", rightValue="+rightValue+"]");
		}
		if (leftValue instanceof Integer) {
			return (T) Integer.valueOf(leftValue.intValue() + rightValue.intValue());
		} else if (leftValue instanceof Double) {
			return (T) Double.valueOf(leftValue.doubleValue() + rightValue.doubleValue());
		} else {
			throw  new IllegalStateException("Only integer and double supported yet!");
		}
	}

	/**
	 * Subtracts the right value from the left!
	 * @param leftValue
	 * @param rightValue
	 * @return
	 */
	//TODO Very ugly code duplication! 
	@SuppressWarnings("unchecked")
	public static <T extends Number> T numberSubtractor(T leftValue, T rightValue) {
		if (null == leftValue || null == rightValue) {
			throw new IllegalArgumentException("Can not add NULL values! [leftValue="+leftValue
					+ ", rightValue="+rightValue+"]");
		}
		if (leftValue instanceof Integer) {
			return (T) Integer.valueOf(leftValue.intValue() - rightValue.intValue());
		} else if (leftValue instanceof Double) {
			return (T) Double.valueOf(leftValue.doubleValue() - rightValue.doubleValue());
		} else {
			throw  new IllegalStateException("Only integer and double supported yet!");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> List<T> extendListWithZeroToFixElementCount(List<T> list, int finalElementCount) {
		if (list.isEmpty()) {
			throw new IllegalArgumentException("The list to be extended must contain at least one element! "
					+ "Maybe pupulateZeroedList(int, Class<T>) can be used instead?");
		}
		Class<T> type;
		try {
			type = (Class<T>) list.get(0).getClass().asSubclass(Number.class);
		} catch (Exception ex) {
			throw new ClassCastException("The type of the list is not a subclass of Number!"); 
		}
		int missingElementsCount = finalElementCount - list.size();
		List<T> newList = new ArrayList<>();
		if (null != list) {
			newList.addAll(list);
		}
		List<T> listFillup = NumberUtil.populateZeroedList(missingElementsCount, type);
		newList.addAll(newList.size(), listFillup);
		return newList;
	}
	
	public static <T extends Number> List<T> populateZeroedList(int elementsCount, Class<T> numberType) {
		List<T> list = new ArrayList<>(elementsCount);
		while (0 < elementsCount) {
			list.add(zeroValueCreator(numberType));
			elementsCount--;
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Number> T zeroValueCreator(Class<T> numberType) {
		if (null == numberType) {
			throw new IllegalArgumentException("Can not set NULL to zero!");
		}
		if (Integer.class.equals(numberType)) {
			return (T) new Integer(0);
		} else if (Double.class.equals(numberType)) {
			return (T) new Double(0);
		} else {
			throw new RuntimeException("Only integer and double supported yet! [type="+numberType.getClass()+"]");
		}
	}

	public static <T extends Number> String[] toFormatedStringArray(List<T> numbers, Integer precision, Integer fieldWith) {
		if (numbers.isEmpty()) {
			throw new IllegalArgumentException("The list of numberst must not be null or empty! [numbers="+numbers+"]");
		}
		String[] result = new String[numbers.size()];
		String formatString = "%";
		formatString += (null == fieldWith)?"":fieldWith;
		formatString += (null == precision)?".10":"." + precision + "E";
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) numbers.get(0).getClass().asSubclass(Number.class);
		for (int i = 0; i < numbers.size(); i++) {
			T currentValue = numbers.get(i);
			if (null == currentValue) {
				currentValue = zeroValueCreator(type);
			}
			result[i] = String.format(formatString, currentValue);
		}
		return result;
	}
}
