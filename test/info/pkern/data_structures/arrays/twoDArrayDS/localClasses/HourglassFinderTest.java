package info.pkern.data_structures.arrays.twoDArrayDS.localClasses;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class HourglassFinderTest {

	private static final List<Integer> grid = Arrays.asList(new Integer[]{1,1,1,0,0,0,0,1,0,0,0,0,1,1,1,0,0,0,0,0,2,4,4,0,0,0,0,2,0,0,0,0,1,2,4,0});
	private static HourglassFinder hourglassFinder;
	
	@BeforeClass
	public static void initClass() {
		hourglassFinder = new HourglassFinder(grid, 6);
	}
	
	@Test
	public void testSumOfHourglassAt() throws Exception {
		assertEquals("Hourglass at: 0", new Integer(7), hourglassFinder.sumOfHourglassAt(0));
		assertEquals("Hourglass at: 2", new Integer(2), hourglassFinder.sumOfHourglassAt(2));
		assertEquals("Hourglass at: 21", new Integer(16), hourglassFinder.sumOfHourglassAt(21));
	}

	@Test
	public void testIndicesOfHourglasses() throws Exception {
		assertEquals(Arrays.asList(new Integer[]{0,1,2,20,21,22}), hourglassFinder.indicesOfHourglasses());
	}

	@Test
	public void testGetMaxHourglasSum() throws Exception {
		assertEquals(new Integer(19), hourglassFinder.getMaxHourglasSum());
	}

}
