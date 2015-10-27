package info.pkern.algorithms.warmup.diagonalDifference.localClasses;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SimpleMatrixTest {

	@Test
	public void testSquareOddMatrix() {
		System.out.println("testSquareOddMatrix:");
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

	@Test
	public void testSquareEvenMatrix() {
		System.out.println("testSquareEvenMatrix:");
		List<Integer> row1 = Arrays.asList(new Integer[]{1,2,3,4});
		List<Integer> row2 = Arrays.asList(new Integer[]{5,6,7,8});
		List<Integer> row3 = Arrays.asList(new Integer[]{9,1,2,3});
		List<Integer> row4 = Arrays.asList(new Integer[]{4,5,6,7});
		List<List<Integer>> matrixData = new ArrayList<>();
		matrixData.add(row1);
		matrixData.add(row2);
		matrixData.add(row3);
		matrixData.add(row4);
		
		SimpleMatrix sm = new SimpleMatrix(matrixData);
		System.out.println(sm);
	}

	@Test
	public void testRectangularMatrixLandscape() {
		System.out.println("testRectangularMatrixLandscape:");
		List<Integer> row1 = Arrays.asList(new Integer[]{1,2,3,9});
		List<Integer> row2 = Arrays.asList(new Integer[]{4,5,6,9});
		List<Integer> row3 = Arrays.asList(new Integer[]{6,7,8,9});
		List<List<Integer>> matrixData = new ArrayList<>();
		matrixData.add(row1);
		matrixData.add(row2);
		matrixData.add(row3);
		
		SimpleMatrix sm = new SimpleMatrix(matrixData);
		System.out.println(sm);
	}

	@Test
	public void testRectangularMatrixPortrait() {
		System.out.println("testRectangularMatrixPortrait:");
		List<Integer> row1 = Arrays.asList(new Integer[]{1,2,3});
		List<Integer> row2 = Arrays.asList(new Integer[]{4,5,6});
		List<Integer> row3 = Arrays.asList(new Integer[]{6,7,8});
		List<Integer> row4 = Arrays.asList(new Integer[]{9,9,9});
		List<List<Integer>> matrixData = new ArrayList<>();
		matrixData.add(row1);
		matrixData.add(row2);
		matrixData.add(row3);
		matrixData.add(row4);
		
		SimpleMatrix sm = new SimpleMatrix(matrixData);
		System.out.println(sm);
	}
}
