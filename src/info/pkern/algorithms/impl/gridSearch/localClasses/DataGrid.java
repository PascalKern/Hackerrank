package info.pkern.algorithms.impl.gridSearch.localClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataGrid {

	private static Logger logger = Logger.getLogger(DataGrid.class.getName());
	
	private final int numberOfRows;
	private final int numberOfColumns;
	private final List<String> grid;

	private Integer firstMatchedRow;
	private Integer firstMatchedColumn;
	
	public DataGrid(int numberOfRows, int numberOfColumns, List<String> grid) {
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		this.grid = grid;
		validateGridShape();
	}
	
	
	public boolean contains(DataGrid pattern) {
		
		List<Integer> matchOfColumneIndices;
		boolean contained = false;
		int currentGridRow = 0;
		
		while (!contained && currentGridRow < numberOfRows && pattern.numberOfRows <= numberOfRows - currentGridRow) {
			matchOfColumneIndices = getMatches(grid.get(currentGridRow), pattern.grid.get(0));
			for (Integer matchOfColumneIndex : matchOfColumneIndices) {
				firstMatchedColumn = matchOfColumneIndex;
				contained = checkSubrowMatches(matchOfColumneIndex, pattern, currentGridRow);
				if (contained) {
					break;
				}
			}
			currentGridRow++;
		}
		
		firstMatchedRow = currentGridRow - 1;
		
		return contained;
	}

	private boolean checkSubrowMatches(Integer matchOfColumnIndex, DataGrid pattern, int startRow) {
		
		boolean contained = true;
		int startRowInGrid = startRow + 1;
		
		while (contained && (startRowInGrid - startRow) < pattern.numberOfRows) {
			String gridRow = grid.get(startRowInGrid);
			contained = matches(gridRow, pattern.grid.get(startRowInGrid - startRow), matchOfColumnIndex);
			if (contained) {
				logger.fine(String.format("String: '%s' %-26s %-10s in column: %05d, row: %05d.", 
						gridRow , "contains pattern:", pattern.grid.get(startRowInGrid - startRow), matchOfColumnIndex, startRowInGrid));
			} else {
				logger.fine(String.format("String: '%s' %-26s %-10s in column: %05d, row: %05d.", 
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
			logger.fine(String.format("String: '%s' %-25s  %-10s at position(s): %s.", content, 
					"contains pattern:", pattern, matchPositions));			
		} else {
			logger.fine(String.format("String: '%s' %-25s  %-10s [%s].", content, "does NOT contain pattern:", 
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
