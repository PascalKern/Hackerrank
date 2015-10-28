package info.pkern.data_structures.arrays.twoDArrayDS.localClasses;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class HourglassFinderTest {

	/*
	 * 1 1 1 0 0 0
	 * 0 1 0 0 0 0
	 * 1 1 1 0 0 0
	 * 0 0 2 4 4 0
	 * 0 0 0 2 0 0
	 * 0 0 1 2 4 0
	 */
	private static final List<Integer> gridExample = Arrays.asList(1,1,1,0,0,0,0,1,0,0,0,0,1,1,1,0,0,0,0,0,2,4,4,0,0,0,0,2,0,0,0,0,1,2,4,0);

	/*
	 *  0 -4 -6  0 -7 -6 
	 * -1 -2 -6 -8 -3 -1 
	 * -8 -4 -2 -8 -8 -6 
	 * -3 -1 -2 -5 -7 -4 
	 * -3 -5 -3 -6 -6 -6 
	 * -3 -6  0 -8 -6 -7
	 */
	private static final List<Integer> gridTestCase3 = Arrays.asList(0,-4,-6,0,-7,-6,-1,-2,-6,-8,-3,-1,-8,-4,-2,-8,-8,-6,-3,-1,-2,-5,-7,-4,-3,-5,-3,-6,-6,-6,-3,-6,0,-8,-6,-7);

	/*
	 * 3 7 -3 0 1 8
	 * 1 -4 -7 -8 -6 5
	 * -8 1 3 3 5 7
	 * -2 4 3 1 2 7
	 * 2 4 -5 1 8 4
	 * 5 -7 6 5 2 8
	 * Mistake was not to check for at least three values in a row. So at index 16 (row: 3, column 5) there where only 
	 * two values (5 and 7) from there the wrong max of 34 was calculated (instead of the right max of 33 from index 21)!
	 */
	private static final List<Integer> gridTestCase5 = Arrays.asList(3,7,-3,0,1,8,1,-4,-7,-8,-6,5,-8,1,3,3,5,7,-2,4,3,1,2,7,2,4,-5,1,8,4,5,-7,6,5,2,8);
	
	private static final List<Integer> gridTestEmpty = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
	private static final List<Integer> gridTestFullOnes = Arrays.asList(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
	
	private static HourglassFinder hourglassFinder;
	
	@BeforeClass
	public static void initClass() {
		hourglassFinder = new HourglassFinder(gridExample, 6);
	}
	
	@Test
	public void testCase3() {
		HourglassFinder hourglassFinderTC3 = new HourglassFinder(gridTestCase3, 6);
		System.out.println(hourglassFinderTC3.indicesOfHourglasses());
		for (Integer index : hourglassFinderTC3.indicesOfHourglasses()) {
			System.out.println("Hourglas at index: " + index + " has value: " + hourglassFinderTC3.sumOfHourglassAt(index));
		}
		Integer maxSum = hourglassFinderTC3.getMaxHourglasSum();
		System.out.println("Max hourglass TC3 sum = " + maxSum);
		assertEquals(new Integer(-19), maxSum);
	}

	@Test
	public void testCase5() {
		HourglassFinder hourglassFinderTC5 = new HourglassFinder(gridTestCase5, 6);
		System.out.println(hourglassFinderTC5.indicesOfHourglasses());
		for (Integer index : hourglassFinderTC5.indicesOfHourglasses()) {
			System.out.println("Hourglas at index: " + index + " has value: " + hourglassFinderTC5.sumOfHourglassAt(index));
		}
		Integer maxSum = hourglassFinderTC5.getMaxHourglasSum();
		System.out.println("Max hourglass TC5 sum = " + maxSum);
		assertEquals(new Integer(33), maxSum);
	}

	@Test
	public void testCaseEmpty() {
		HourglassFinder hourglassFinderEmpty = new HourglassFinder(gridTestEmpty, 6);
		System.out.println(hourglassFinderEmpty.indicesOfHourglasses());
		for (Integer index : hourglassFinderEmpty.indicesOfHourglasses()) {
			System.out.println("Hourglas at index: " + index + " has value: " + hourglassFinderEmpty.sumOfHourglassAt(index));
		}
		Integer maxSum = hourglassFinderEmpty.getMaxHourglasSum();
		System.out.println("Max hourglass Empty sum = " + maxSum);
		assertEquals(new Integer(0), maxSum);
	}
	
	@Test
	public void testCaseFullOnes() {
		HourglassFinder hourglassFinderFullOnes = new HourglassFinder(gridTestFullOnes, 6);
		System.out.println(hourglassFinderFullOnes.indicesOfHourglasses());
		for (Integer index : hourglassFinderFullOnes.indicesOfHourglasses()) {
			System.out.println("Hourglas at index: " + index + " has value: " + hourglassFinderFullOnes.sumOfHourglassAt(index));
		}
		Integer maxSum = hourglassFinderFullOnes.getMaxHourglasSum();
		System.out.println("Max hourglass Ones sum = " + maxSum);
		assertEquals(new Integer(7), maxSum);
	}
	
	@Test
	public void testSumOfHourglassAt() throws Exception {
		Integer sum = hourglassFinder.sumOfHourglassAt(0);
		System.out.println("Hourglass at: 0, sum = " + sum);
		assertEquals("Hourglass at: 0", new Integer(7), sum);
		sum = hourglassFinder.sumOfHourglassAt(2);
		System.out.println("Hourglass at: 2, sum = " + sum);
		assertEquals("Hourglass at: 2", new Integer(2), sum);
		sum = hourglassFinder.sumOfHourglassAt(21);
		System.out.println("Hourglass at: 21, sum = " + sum);
		assertEquals("Hourglass at: 21", new Integer(14), sum);
	}

	/*
	 * Test only usable when check for specific hourglass shape/structure. Means uses
	 * the isHourglassAt(int) and not the isHourglassPossibleAt(int)!
	 * But with first one the test 1,2,5 on hackerrank fails!
	 */
//	@Test
	public void testIndicesOfHourglasses() throws Exception {
		List<Integer> expected = Arrays.asList(new Integer[]{0,1,2,20,21});
		List<Integer> actual = new ArrayList<>(hourglassFinder.indicesOfHourglasses());
		Collections.sort(expected);
		Collections.sort(actual);
		System.out.println("Hourglass indices: " + actual);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetMaxHourglasSum() throws Exception {
		Integer maxSum = hourglassFinder.getMaxHourglasSum();
		System.out.println("Max hourglass sum = " + maxSum);
		assertEquals(new Integer(19), maxSum);
	}

}
