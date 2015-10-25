package info.pkern.algorithms.impl.gridSearch;
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
 * @category Algorithms - Implementation: The Grid Search
 * @see https://www.hackerrank.com/challenges/the-grid-search
 */
@SuppressWarnings("unused")
public class Solution {
	
    public static void main(String[] args) {
    	
    	TestdataHandler<Testdata> inputProcessor = new TestdataHandler<>(Solution.Testdata.class, System.in);
    	
    	boolean testResult;
    	for (Testdata testdata : inputProcessor.getAllTestdata()) {
    		testResult = testdata.getGrid().contains(testdata.getPattern());
    		System.out.println(evaluateHackerrankResultString(testResult));
    	}
    	
    }
    
	public static String evaluateHackerrankResultString(boolean result) {
		if (result) {
			return "YES";
		} else {
			return "NO";
		}
	}
    
	
	
	
	
	/* ************************************************************************************************************
	 * Classes to solve this hackerrank challenge. When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/
//	public class DataGrid {
	private static class DataGrid {
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

//	public enum GridType {	
	private enum GridType {
		
		GRID(" "), 
		PATTERN("\t");

		private String shapeSizeSeparator;

		private GridType(String shapeSizeSeparator) {
			this.shapeSizeSeparator = shapeSizeSeparator;
		}

		public String getShapeSizeSeparator() {
			return shapeSizeSeparator;
		}
	}
	
	/* ************************************************************************************************************
	 * Specific implementations of general helper classes (see bellow). When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/
	//Must be public when a inner class of Solution to be instantiated in the TestdataHandler.
	public class Testdata extends AbstractTestdata {

		private DataGrid grid;
		private DataGrid pattern;
		private String exptected;

		@SuppressWarnings("unchecked")
		@Override
		public Testdata newInstance(Scanner scanner) {
			Testdata testdata = new Testdata();
			testdata.grid = createGrid(scanner);
			testdata.pattern = createGrid(scanner);
			testdata.exptected = readExptectedResult(scanner);
			return testdata;
		}

		@Override
		public String getExpectedString() {
			return exptected;
		}
		
		@Override
		public Object getExpected() {
			return getExpectedString();
		}
		
		public DataGrid getGrid() {
			return grid;
		}
		
		public DataGrid getPattern() {
			return pattern;
		}
		
		@Override
		public String getGridForSimulation() {
			return grid.gridToSimulationString(GridType.GRID)
					+System.lineSeparator()
					+pattern.gridToSimulationString(GridType.PATTERN);
		}
		
		private DataGrid createGrid(Scanner scanner) {
			int rows;
			int columns;
			List<String> gridData;
			try {
				//Skip possible empty lines followed a test data block 
				while (!scanner.hasNextInt()) {
					scanner.nextLine();
				}
				rows = scanner.nextInt();
				columns = scanner.nextInt();
			} catch (Exception ex) {
				throw new RuntimeException("Could not read grid rows or columns from stream!", ex);
			}
			gridData = readGridRows(scanner, rows);
			return new DataGrid(rows, columns, gridData);
		}
		
		private List<String> readGridRows(Scanner scanner, int numberOfRows) {
			List<String> rows = new ArrayList<>(numberOfRows);
			int rowCounter = numberOfRows;
			try {
				while (rowCounter > 0) {
					rows.add(scanner.next());
					rowCounter--;
				}
			} catch (Exception ex) {
				throw new RuntimeException("Could not create rows! Error while processing input for row: " + (numberOfRows - rowCounter), ex);
			}
			return rows;
		}

		private String readExptectedResult(Scanner scanner) {
			Integer numberOfResultLines = followedByExptectedResultLines(scanner);
			if (null == numberOfResultLines) {
				return null;
			} else {
				return scanner.next();
			}
		}
	}
	
	
	/* ************************************************************************************************************
	 * General helper classes for the submission of a hackerrank solution. When working on the solution locally
	 * those classes are available within the project. 
	 **************************************************************************************************************/
//	public class TestdataHandler<T extends AbstractTestdata> {
	private static class TestdataHandler<T extends AbstractTestdata> {

		public static final String TESTDATA_FILE_NAME = "test_input.txt";
		
		private final int numberOfTests;
		private List<T> testdata;
		private boolean mayContainExpectedResults;
		
		public TestdataHandler(Class<T> clazz, InputStream in, boolean containsExptectedResults) {
			this.mayContainExpectedResults = containsExptectedResults;
			try (Scanner scanner = new Scanner(in)) {
				numberOfTests = readNumberOfTests(scanner);
				testdata = createTestData(clazz, scanner);
			} catch (Exception ex) {
				throw new RuntimeException("Could not read input from stream!", ex);
			}
		}
		
		public TestdataHandler(Class<T> clazz, InputStream in) {
			this(clazz, in, false);
		}

		public TestdataHandler(Class<T> clazz, File testInput, boolean containsExptectedResults) throws IOException {
			this(clazz, new FileInputStream(testInput), containsExptectedResults);
		}
		
		public TestdataHandler(Class<T> clazz, File testInput) throws IOException {
			this(clazz, testInput, false);
		}
		
		public List<T> getAllTestdata() {
			return new ArrayList<>(testdata);
		}
		
		public T getTestdata(int index) {
			return testdata.get(index);
		}
		
		public int getNumberOfTests() {
			return numberOfTests;
		}
		
		public String getTestdataForSimulation() {
			StringBuilder sb = new StringBuilder();
			sb.append(numberOfTests).append(System.lineSeparator());
			for (int i = 0, j = 1; i < testdata.size(); i++, j++) {
				if (j < testdata.size()) {
					sb.append(testdata.get(i).getGridForSimulation()).append(System.lineSeparator());
				} else {
					sb.append(testdata.get(i).getGridForSimulation());
				}
			}
			return sb.toString();
		}
		
		public boolean testsMayContainExptectedResult() {
			return mayContainExpectedResults;
		}
		
		private int readNumberOfTests(Scanner scanner) {
			try {
				return Integer.parseInt(scanner.nextLine());
			} catch (Exception ex) {
				throw new RuntimeException("Could not read the number of tests!", ex);
			}
		}

		private List<T> createTestData(Class<T> clazz, Scanner scanner) {
			List<T> testData = new ArrayList<>(numberOfTests);
			int testCounter = numberOfTests;
			T test;
			try {
				while (testCounter > 0) {
					testCounter--;
					//Works only when Testdata implementation is not a inner class!
//					test = clazz.newInstance();
					//Solution to be used when Testdata implementation is an inner class of Solution!
					test = clazz.getConstructor(Solution.class).newInstance(new Solution());
					test = test.newInstance(scanner);
					mayContainExpectedResults = test.containsExptectedResults();
					test.setTestNr(numberOfTests - testCounter);
					testData.add(test);
				}
			} catch (InstantiationException | IllegalAccessException
					| java.lang.reflect.InvocationTargetException | NoSuchMethodException
					| IllegalArgumentException | SecurityException ex) {
				throw new RuntimeException("Could not create test datas!", ex);
			}
			return testData;
		}
	}

//	public abstract class AbstractTestdata {
	private abstract class AbstractTestdata {
		
		private final Pattern numberOfExptectedResultLines = Pattern.compile("_expected#\\d+");
		private int testNr;
		private boolean containsExptectedResults = false;

		
		public abstract <T extends AbstractTestdata> T newInstance(Scanner scanner);
		public abstract Object getExpected();
		public abstract String getExpectedString();
		public abstract String getGridForSimulation();
		
		public boolean containsExptectedResults() {
			return containsExptectedResults;
		}

		public int getTestNr() {
			return testNr;
		}
		
		protected Integer followedByExptectedResultLines(Scanner scanner) {
			if (scanner.hasNext(numberOfExptectedResultLines)) {
				String expectedResults = scanner.next(numberOfExptectedResultLines);
				return Integer.valueOf(expectedResults.split("#")[1]);
			} else {
				return null;
			}
		}
		
		protected void setContainsExptectedResults() {
			containsExptectedResults = true;
		}
		protected void setTestNr(int testNr) {
			this.testNr = testNr;
		}
	}
}
