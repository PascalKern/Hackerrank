package info.pkern.sandbox;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class ArraySum {

	private long startTime;
	private long endTime;
	
	@Test
	public void testSumArray() {
		Integer[] numbersArray = new Integer[] {1,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1};
//		Integer[] numbersArray = new Integer[] {1,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1
//				,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1
//				,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1
//				,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1};
		List<Integer> numbers = Arrays.asList(numbersArray);
		System.out.println("sumArrayWithIteration: " + sumArrayWithIteration(numbersArray));
		System.out.println(getDurationFromNanoseconds(startTime, endTime));
		System.out.println("sumListRecursive:      " + sumListRecursive(numbers));
		System.out.println(getDurationFromNanoseconds(startTime, endTime));
		System.out.println("sumListIterative:      " + sumListIterative(numbers));
		System.out.println(getDurationFromNanoseconds(startTime, endTime));
		System.out.println("sumListWithIterator:   " + sumListWithIterator(numbers));
		System.out.println(getDurationFromNanoseconds(startTime, endTime));
	}

	
	private String getDurationFromNanoseconds(long from, long to) {
		long durationNs = to - from;
		float durationMs = (float) (durationNs * Math.pow(10, 6));
		return "Duration: " + durationMs;
	}
	
	private Integer sumArrayWithIteration(Integer[] numbers) {
		startTime = System.nanoTime();
		Integer sum = 0;
		for (int i = 0; i < numbers.length; i++) {
			sum += numbers[i];
		}
		endTime = System.nanoTime();
		return sum;
	}
	
	private Integer sumListIterative(List<Integer> numbers) {
		startTime = System.nanoTime();
		Integer sum = 0;
		for (Integer number : numbers) {
			sum += number;
		}
		endTime = System.nanoTime();
		return sum;
	}
	
	/**
	 * Calculates the sum in a <code>List</code> of integers.
	 * <br/>
	 * <strong>Important:</strong> this method carries the risk of a <code>StackOverflowError</code>
	 * when the size of the List exceeds the maximal allowed Stack entries/size.
	 * 
	 * @param numbers list of numbers.
	 * @return the sum of all integers in the list.
	 */
	private Integer sumListRecursive(List<Integer> numbers) {
		startTime = System.nanoTime();
//		System.out.println(numbers);
		int sum = 0;
		if (numbers.size() == 1) {
			endTime = System.nanoTime();
			return sum + numbers.get(0);
		} else {
			sum += numbers.get(0);
			return sum + sumListRecursive(numbers.subList(1, numbers.size()));
		}
	}
	
	private Integer sumListWithIterator(List<Integer> numbers) {
		startTime = System.nanoTime();
		Integer sum = 0;
		Iterator<Integer> iter = numbers.listIterator();
		while (iter.hasNext()) {
			sum += iter.next();
		}
		endTime = System.nanoTime();
		return sum;
	}
}
