package info.pkern.algorithms.impl.gridSearch.localClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataGrid {
	//Must be full qualified to not an additional import java.util.logging.*; to the Solution class! 
	private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DataGrid.class.getName());
	
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

	public String positionWhereGridContainsPattern(DataGrid pattern) {
		if (contains(pattern)) {
			return String.format("row: %-3s, column: %-3s", firstMatchedRow, firstMatchedColumn);
		} else {
			return "Grid does NOT contain the pattern!";
		}
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


	private List<Integer> getMatches(String content, String pattern) {
		List<Integer> matchPositions = new ArrayList<>();
		if (content.contains(pattern)) {
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
			matchedIndex = content.indexOf(pattern, ofColumnIndex);
			logger.finer(String.format("String: '%s' %-26s %-10s in column: %05d.",content , "contains pattern:", pattern, ofColumnIndex));
		} else {
			logger.finer(String.format("String: '%s' %-26s %-10s in column: %05d.",content , "does NOT contain pattern:", pattern, ofColumnIndex));
		}
		return ofColumnIndex == matchedIndex; 
	}

	private void validateGridShape() {
		if (numberOfColumns != grid.get(0).length()) {
			throw new IllegalStateException("Validation of testdata failed due of wrong number of "
					+ "columns in 'grid'! [exptected: "+numberOfColumns+", effective: "+grid.get(0).length()
					+ "grid: "+grid.toString()+"]");
		}
		for (int i = 0; i < grid.size(); i++) {
			if (grid.get(i).length() != numberOfColumns) {
				throw new IllegalStateException("Not not all rows of the grid have the same column counts!"
						+ "[row: "+i+", columns actual: "+grid.get(i).length()+", exptectd: "+numberOfColumns+"]");
			}
		}
	}
	
	public String gridToSimulationString(GridType type) {
		StringBuilder sb = new StringBuilder();
		sb.append(numberOfRows).append(type.getShapeSizeSeparator()).append(numberOfColumns).append(System.lineSeparator());
		for (int i = 0, j = 1; i < grid.size(); i++, j++) {
			if (j < grid.size()) {
				sb.append(grid.get(i)).append(System.lineSeparator());
			} else {
				sb.append(grid.get(i));
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return gridToSimulationString(GridType.GRID);
	}
}
