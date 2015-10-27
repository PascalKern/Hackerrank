package info.pkern.algorithms.warmup.diagonalDifference;
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
 * @category Algorithms - Warmup: Diagonal Difference
 * @see https://www.hackerrank.com/challenges/diagonal-difference
 */
public class Solution {
	
	public static void main(String[] args) {
		
		List<List<Integer>> matrixData;

		try (Scanner scanner = new Scanner(System.in)) {
			int matrixRows = scanner.nextInt();
			scanner.nextLine();
			
			matrixData = new ArrayList<>(matrixRows);
			while (scanner.hasNextLine()) {
				matrixData.add(ListTypeConverter.toInteger(scanner.nextLine().split(" ")));
			}
		}
		
		Solution solution = new Solution();
		SimpleMatrix matrix = solution.new SimpleMatrix(matrixData);
		
		System.out.println(Math.abs(matrix.getMainDiagonalSum() - matrix.getAntidiagonalSum()));
		
	}
	
	
	/* ************************************************************************************************************
	 * Classes to solve this hackerrank challenge. When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/
	
	private class SimpleMatrix {

		private List<List<Integer>> rows;
		private List<List<Integer>> columnsCached;
		
		private Integer numberOfColumns;
		
		private Integer mainDiagonalSum;
		private Integer antiDiagonalSum;
		private List<Integer> mainDiagonal;
		private List<Integer> antiDiagonal;
		
		public SimpleMatrix(List<List<Integer>> rows) {
			this.rows = rows;
			numberOfColumns = rows.get(0).size();
			columnsCached = new ArrayList<>(numberOfColumns);
			calculateDiagonalSumsAndPopulateDiagonales(rows);
		}
		
		public List<Integer> getMainDiagonal() {
			return mainDiagonal;
		}
		
		public List<Integer> getAntiDiagonal() {
			return antiDiagonal;
		}

		public Integer getMainDiagonalSum() {
			return mainDiagonalSum;
		}
		
		public Integer getAntidiagonalSum() {
			return antiDiagonalSum;
		}
		
		public List<Integer> getRow(int index) {
			return rows.get(index);
		}
		
		public List<Integer> getColumn(int index) {
			if (null == columnsCached.get(index)) {
				columnsCached.set(index, cacheRow(index));
			}
			return columnsCached.get(index);
		}
		
		private void calculateDiagonalSumsAndPopulateDiagonales(List<List<Integer>> rows) {
			
			int minSideLength = Math.min(rows.size(), numberOfColumns);
			
			mainDiagonal = Arrays.asList(new Integer[minSideLength]);
			antiDiagonal = Arrays.asList(new Integer[minSideLength]);
			mainDiagonalSum = 0;
			antiDiagonalSum = 0;
			
			int minSideLengthHalfe = minSideLength / 2;
			
			List<Integer> currentRowTop;
			List<Integer> currentRowBottom;
			for (int i = 0, j = minSideLength - 1; i < minSideLengthHalfe; i++, j--) {
				currentRowTop = rows.get(i);
				currentRowBottom = rows.get(j);
				
				mainDiagonalSum += currentRowTop.get(i) + currentRowBottom.get(j);
				mainDiagonal.set(i, currentRowTop.get(i));
				mainDiagonal.set(j, currentRowBottom.get(j));
				
				antiDiagonalSum += currentRowTop.get(j) + currentRowBottom.get(i);
				antiDiagonal.set(i, currentRowTop.get(j));
				antiDiagonal.set(j, currentRowBottom.get(i));
			}
			
			if (0 != minSideLength % 2) {
				Integer matrixCenter = rows.get(minSideLengthHalfe).get(minSideLengthHalfe);
				mainDiagonalSum += matrixCenter;
				mainDiagonal.set(minSideLengthHalfe, matrixCenter);
				antiDiagonalSum += matrixCenter;
				antiDiagonal.set(minSideLengthHalfe, matrixCenter);
			}
		}

		private List<Integer> cacheRow(int index) {
			List<Integer> column = new ArrayList<>(numberOfColumns);
			for (List<Integer> row : rows) {
				column.add(row.get(index));
			}
			return column;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("rows: %3d, columns: %3d%s", rows.size(), numberOfColumns, System.lineSeparator()));
			String format = "%6d";
			for (List<Integer> row : rows) {
				for (Integer cell : row) {
					sb.append(String.format(format, cell)).append(" ");
				}
				sb.append(System.lineSeparator());
			}
			sb.append(System.lineSeparator());
			sb.append("main diagonale: ").append(mainDiagonal).append(String.format(" [sum = %07d]%s",mainDiagonalSum, System.lineSeparator()));
			sb.append("anti diagonale: ").append(antiDiagonal).append(String.format(" [sum = %07d]",antiDiagonalSum));
			return sb.toString();
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
