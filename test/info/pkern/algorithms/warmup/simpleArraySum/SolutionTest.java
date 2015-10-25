package info.pkern.algorithms.warmup.simpleArraySum;

import static org.junit.Assert.*;

import java.util.Scanner;

import info.pkern.algorithms.warmup.simpleArraySum.Solution.ArrayTotalizer;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testArrayTotalizer() {
		String input = "33\n1 3 4 5 6 7 8 9 9 8 7 6 5 4 3 2 1 2 3 4 5 6 7 8 9 9 8 7 6 5 4 3 2 1\n177";
//		String input = "6\n1 2 3 4 10 11\n31";
	
		try (Scanner scanner = new Scanner(input)) {
			int numberOfElements = scanner.nextInt();
			scanner.nextLine();
			String numbers = scanner.nextLine();
			int expected = scanner.nextInt();
			
			ArrayTotalizer totalizer = new ArrayTotalizer(numbers);
			
			int actual = totalizer.sumListIterative();
			System.out.println(actual);
			assertEquals(expected, actual);
			actual = totalizer.sumListRecursive();
			System.out.println(actual);
			assertEquals(expected, actual);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}
	
	
}
