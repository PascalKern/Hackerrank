package info.pkern.algorithms.impl.gridSearch.localClasses;

import java.util.ArrayList;
import java.util.List;

public class DataGrid {

	private final int numberOfRows;
	private final int numberOfColumns;
	private final List<String> grid;
	
	private int matchedRow;
	private int matchedColumn;
	
	public DataGrid(int numberOfRows, int numberOfColumns, List<String> grid) {
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		this.grid = grid;
		validateGridShape();
	}
	
	
	public boolean contains(DataGrid pattern) {
		
		List<Integer> matchOfIndices;
		boolean contained = false;
		int currentGridRow = 0;	//matchedRow
		
		while (!contained && currentGridRow < numberOfRows && pattern.numberOfRows <= numberOfRows - currentGridRow) {
			matchOfIndices = getMatches(grid.get(currentGridRow), pattern.grid.get(0));
			for (Integer matchOf : matchOfIndices) {	//matchedColumn
				contained = checkSubrowMatches(matchOf, pattern, currentGridRow);
			}
			currentGridRow++;
		}
		
		return contained;
	}

	private boolean checkSubrowMatches(Integer matchOfColumnIndex, DataGrid pattern, int startRow) {
		
		boolean contained = true;
		int startRowInGrid = startRow + 1;
		
		while (contained && (startRowInGrid - startRow) < pattern.numberOfRows) {
			String gridRow = grid.get(startRowInGrid);
			contained = matches(gridRow, pattern.grid.get(startRowInGrid - startRow), matchOfColumnIndex);
			if (contained) {
				System.out.println(String.format("String: '%s' %-26s %-10s in column: %05d, row: %05d.", 
						gridRow , "contains pattern:", pattern.grid.get(startRowInGrid - startRow), matchOfColumnIndex, startRowInGrid));
			} else {
				System.out.println(String.format("String: '%s' %-26s %-10s in column: %05d, row: %05d.", 
						gridRow , "does NOT contain pattern:", pattern.grid.get(startRowInGrid - startRow), matchOfColumnIndex, startRowInGrid));
			}
			startRowInGrid++;
		}
		return contained;
	}


	/*
	 * Better return a empty List instead of null?!
	 */
	private List<Integer> getMatches(String content, String pattern) {
		List<Integer> matchPositions = new ArrayList<>();
//		List<Integer> matchPositions = null;
		if (content.contains(pattern)) {
//			matchPositions = new ArrayList<>();
			Integer matchPos = -1;
			while (0 <= (matchPos = content.indexOf(pattern, matchPos + 1))) {
				matchPositions.add(matchPos);
			}
			System.out.println(String.format("String: '%s' %-25s  %-10s at position(s): %s.", content, 
					"contains pattern:", pattern, matchPositions));			
		} else {
			System.out.println(String.format("String: '%s' %-25s  %-10s [%s].", content, "does NOT contain pattern:", 
					pattern, matchPositions));
		}
		return matchPositions;
	}
	
	private boolean matches(String content, String pattern, int ofColumnIndex) {
		int matchedIndex = -1;
		if (content.contains(pattern)) {
//			matchedIndex = content.indexOf(pattern, ofIndex);
			matchedIndex = content.indexOf(pattern, ofColumnIndex);
//			System.out.println(String.format("String: '%s' %-26s %-10s in column: %05d.",content , "contains pattern:", pattern, ofColumnIndex));
//			matchedIndex -= ofIndex;
		} else {
//			System.out.println(String.format("String: '%s' %-26s %-10s in column: %05d.",content , "does NOT contain pattern:", pattern, ofColumnIndex));
		}
		return ofColumnIndex == matchedIndex; 
	}

	/*
	 * TODO Check every row for the correct amount of columns!
	 */
	private void validateGridShape() {
		if (numberOfColumns != grid.get(0).length()) {
			throw new IllegalStateException("Validation of testdata failed due of wrong number of "
					+ "columns in 'grid'! [exptected:"+numberOfColumns+", effective:"+grid.get(0).length()+"]");
		}
	}
}
