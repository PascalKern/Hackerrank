package info.pkern.algorithms.warmup.diagonalDifference.localClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleMatrix {

	private List<List<Integer>> rows;
	private List<List<Integer>> columnsCached;
	
	private Integer columnCount;
	
	private Integer mainDiagonalSum;
	private Integer antiDiagonalSum;
	private List<Integer> mainDiagonal;
	private List<Integer> antiDiagonal;
	
//	public SimpleMatrix(List<Integer> matrixContent, int columnCount) {
//		columnCount = columnCount;
//		calculateDiagonalSumsAndPopulateDiagonales(matrixContent, columnCount);
//	}
	
	public SimpleMatrix(List<List<Integer>> rows) {
		this.rows = rows;
		columnCount = rows.get(0).size();
		columnsCached = new ArrayList<>(columnCount);
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
		mainDiagonal = new ArrayList<>(rows.size());
//		Collections.fill(mainDiagonal, 1);
		antiDiagonal = new ArrayList<>(rows.size());
//		Collections.fill(antiDiagonal, 1);
		mainDiagonalSum = 0;
		antiDiagonalSum = 0;
		
		int rowsHalfe = rows.size() / 2;
		
		List<Integer> currentRowTop;
		List<Integer> currentRowBottom;
		for (int i = 0, j = rows.size() - 1; i < rowsHalfe; i++, j--) {
			currentRowTop = rows.get(i);
			currentRowBottom = rows.get(i);
			
			mainDiagonalSum += currentRowTop.get(i) + currentRowBottom.get(j);
			mainDiagonal.set(i, currentRowTop.get(i));
			mainDiagonal.set(j, currentRowBottom.get(j));
			
			antiDiagonalSum += currentRowTop.get(j) + currentRowBottom.get(i);
			antiDiagonal.set(i, currentRowTop.get(j));
			antiDiagonal.set(j, currentRowBottom.get(i));
		}
		
		if (0 != rows.size() % 2) {
			Integer matrixCenter = rows.get(rowsHalfe).get(rowsHalfe);
			mainDiagonalSum += matrixCenter;
			mainDiagonal.set(rowsHalfe, matrixCenter);
			antiDiagonalSum += matrixCenter;
			antiDiagonal.set(rowsHalfe, matrixCenter);
		}
	}

	private List<Integer> cacheRow(int index) {
		List<Integer> column = new ArrayList<>(columnCount);
		for (List<Integer> row : rows) {
			column.add(row.get(index));
		}
		return column;
	}

//	private List<List<Integer>> calculateDiagonalSumsAndPopulateDiagonales(List<Integer> matrixContent, int columnCount) {
//	
//	}
//	OR - But then keep the matrixContent as list in the rows member!
//	private void calculateDiagonalSumsAndPopulateDiagonales(List<Integer> matrixContent, int columnCount) { 
//		
//	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("rows: %3d, columns: %3d%s", rows.size(), columnCount, System.lineSeparator()));
		String format = "%6d";
		for (List<Integer> row : rows) {
			for (Integer cell : row) {
				sb.append(String.format(format, cell)).append(" ");
			}
			sb.append(System.lineSeparator());
		}
		sb.append(System.lineSeparator());
		sb.append("main diagonale: ").append(mainDiagonal).append(String.format(" [sum = %7d]%s",mainDiagonalSum, System.lineSeparator()));
		sb.append("anti diagonale: ").append(antiDiagonal).append(String.format(" [sum = %7d]",antiDiagonalSum));
		return sb.toString();
	}
	
}
