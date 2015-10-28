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
	private static final List<Integer> grid = Arrays.asList(new Integer[]{1,1,1,0,0,0,0,1,0,0,0,0,1,1,1,0,0,0,0,0,2,4,4,0,0,0,0,2,0,0,0,0,1,2,4,0});
	private static HourglassFinder hourglassFinder;
	
	@BeforeClass
	public static void initClass() {
		hourglassFinder = new HourglassFinder(grid, 6);
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

	@Test
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
