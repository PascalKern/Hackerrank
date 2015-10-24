package info.pkern.sandbox.experiments;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class ArraySum {

	
	
	@Test
	public void testSumArray() {
		List<Integer> numbers = Arrays.asList(new Integer[] {1,2,3,4,5,6,7,8,9,9,8,7,6,5,4,3,2,1});
		System.out.println("SumRecurscive: " + sumArrayRecursive(numbers));
		System.out.println("SumIterative:  " + sumArrayIterative(numbers));
		System.out.println("SumIterator:   " + sumArrayIterator(numbers));
	}

	private Integer sumArrayIterative(List<Integer> numbers) {
		Integer sum = 0;
		for (Integer number : numbers) {
			sum += number;
		}
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
	private Integer sumArrayRecursive(List<Integer> numbers) {
//		System.out.println(numbers);
		int sum = 0;
		if (numbers.size() > 0) {
			sum = numbers.get(0) + numbers.get(1);
			return sum + sumArrayRecursive(numbers.subList(2, numbers.size()));
		} else {
			return sum; 
		}
	}
	
	private Integer sumArrayIterator(List<Integer> numbers) {
		Integer sum = 0;
		Iterator<Integer> iter = numbers.listIterator();
		while (iter.hasNext()) {
			sum += iter.next();
		}
		return sum;
	}
}
