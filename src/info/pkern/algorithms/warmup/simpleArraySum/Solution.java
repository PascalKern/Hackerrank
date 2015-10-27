package info.pkern.algorithms.warmup.simpleArraySum;
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
 * @category Algorithms - Warmup: Simple Array Sum
 * @see https://www.hackerrank.com/challenges/simple-array-sum
 */
public class Solution {
	
	public static void main(String[] args) {

		try (Scanner scanner = new Scanner(System.in)) {
			int numberOfElements = scanner.nextInt();	//Could also be skipped the whole line like with the next call.
			scanner.nextLine();	//Skips the rest of the line especially the newline character after the above read int.
			String testdata = scanner.nextLine();
			ArrayTotalizer totalizer = new ArrayTotalizer(testdata);
			System.out.println(totalizer.sumListRecursive());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public static class ArrayTotalizer {

		private final List<Integer> numbers;
		
		public ArrayTotalizer(String testdata) {
			numbers = ListTypeConverter.toInteger(testdata.split(" "));
		}

		public Integer sumListIterative() {
			Integer sum = 0;
			for (Integer number : numbers) {
				sum += number;
			}
			return sum;
		}

		/**
		 * Calculates the sum in a <code>List</code> of integers. Almost the fastest approach 
		 * <br/>
		 * <strong>Important:</strong> this method carries the risk of a <code>StackOverflowError</code>
		 * when the size of the List exceeds the maximal allowed Stack entries/size.
		 * 
		 * @param numbers list of numbers.
		 * @return the sum of all integers in the list.
		 */
		public Integer sumListRecursive() {
			return sumListRecursive(numbers);
		}

		private Integer sumListRecursive(List<Integer> numbers) {
			int sum = 0;
			if (numbers.size() == 1) {
				return sum + numbers.get(0);
			} else {
				sum += numbers.get(0);
				return sum + sumListRecursive(numbers.subList(1, numbers.size()));
			}
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
	}

}
