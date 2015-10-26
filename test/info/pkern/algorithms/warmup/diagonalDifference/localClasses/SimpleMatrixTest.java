package info.pkern.algorithms.warmup.diagonalDifference.localClasses;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SimpleMatrixTest {

	@Test
	public void testInitialisation() {
		List<Integer> row1 = Arrays.asList(new Integer[]{1,2,3});
		List<Integer> row2 = Arrays.asList(new Integer[]{4,5,6});
		List<Integer> row3 = Arrays.asList(new Integer[]{6,7,8});
		List<List<Integer>> matrixData = new ArrayList<>();
		matrixData.add(row1);
		matrixData.add(row2);
		matrixData.add(row3);
		
		SimpleMatrix sm = new SimpleMatrix(matrixData);
		System.out.println(sm);
	}
}
