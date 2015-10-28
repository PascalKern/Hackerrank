package info.pkern.data_structures.arrays.twoDArrayDS;
/* ============================================================================
 * Copyright (c) 2015 Pascal Kern
 * 
 * http://github.com/PascalKern/Hackerrank_java7
 * http://www.pkern.info/
 * ============================================================================
 */

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/**
 * @version 0.1 - (Hackerrank Solution-Environment)
 * @author Pascal Kern
 * @category Data Structurs - Arrays: 2D Array - DS
 * @see https://www.hackerrank.com/challenges/2d-array
 */
public class Solution {
	
    public static void main(String[] args) {
    	
    	List<String> data;
    	try (Scanner scanner = new Scanner(System.in)) {
    		data = new ArrayList<>();
    		while (scanner.hasNextLine()) {
    			data.addAll(Arrays.asList(scanner.nextLine().split(" ")));
    		}
    	}
    	
    	Solution solution = new Solution();
    	HourglassFinder hourglassFinder = solution.new HourglassFinder(ListTypeConverter.toInteger(data), 6);
    	
    	System.out.println(hourglassFinder.getMaxHourglasSum());
    	
    }
    
    
	
	
	
	
	/* ************************************************************************************************************
	 * Classes to solve this hackerrank challenge. When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/

	private class HourglassFinder {

		private final int gridSize;
		private List<Integer> grid;

		private Integer[] hourglassShape;

		private Map<Integer, Integer> hourglassSums = new HashMap<>();

		public HourglassFinder(List<Integer> grid, int gridSize) {
			this.gridSize = gridSize;
			this.grid = grid;
			hourglassShape = createHourglassShape(gridSize);
			hourglassSums = findHourglassesAndSum();
		}

		public Integer sumOfHourglassAt(int topLeftHourglassIndex) {
			if (hourglassSums.keySet().contains(topLeftHourglassIndex)) {
				return hourglassSums.get(topLeftHourglassIndex);
			} else {
				throw new NoSuchElementException(
						"No hourglass found for index: "
								+ topLeftHourglassIndex);
			}
		}

		public Set<Integer> indicesOfHourglasses() {
			return hourglassSums.keySet();
		}

		public Integer getMaxHourglasSum() {
			List<Integer> values = new ArrayList<>(hourglassSums.values());
			Collections.sort(values, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o2.intValue() - o1.intValue();
				}
			});
			return values.get(0);
		}

		private boolean isHourglassAt(int topLeftHourglassIndex) {
			boolean isHourglass = false;
			int bottomLeftHourglasIndex = 2 * gridSize + topLeftHourglassIndex;

			isHourglass = grid.get(topLeftHourglassIndex) > 0
					&& grid.size() > bottomLeftHourglasIndex + 2;
			if (isHourglass) {
				isHourglass = grid.get(bottomLeftHourglasIndex) > 0;
			}
			return isHourglass;
		}

		private Map<Integer, Integer> findHourglassesAndSum() {
			Map<Integer, Integer> hourglassSums = new HashMap<>();
			for (int i = 0; i < grid.size(); i++) {
				if (isHourglassAt(i)) {
					hourglassSums.put(i, calculateHourglassSum(i));
				}
			}
			return hourglassSums;
		}

		private Integer calculateHourglassSum(int topLeftHourglassIndex) {
			int sum = 0;
			for (int i = 0; i < hourglassShape.length; i++) {
				sum += grid.get(topLeftHourglassIndex + hourglassShape[i]);
			}
			return sum;
		}

		private Integer[] createHourglassShape(int gridSize) {
			return new Integer[] { 0, 1, 2, gridSize + 1, 2 * gridSize,
					2 * gridSize + 1, 2 * gridSize + 2 };
		}
	}
    
	/* ************************************************************************************************************
	 * General helper classes for the submission of a hackerrank solution. When working on the solution locally
	 * those classes are available within the project. 
	 **************************************************************************************************************/

    private static class ListTypeConverter {

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
    }
}