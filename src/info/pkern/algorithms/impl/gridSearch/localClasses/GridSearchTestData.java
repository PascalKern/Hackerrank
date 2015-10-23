package info.pkern.algorithms.impl.gridSearch.localClasses;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import info.pkern.AbstractTestData;

public class GridSearchTestData extends AbstractTestData {

	private int numberGridOfRows;
	private int numberGridOfColumns;
	private List<String> gridRows;
	private int numberPatternOfRows;
	private int numberPatternOfColumns;
	private List<String> patternRows;
	

	@Override
	public GridSearchTestData newInstance(Scanner scanner) {
		GridSearchTestData testdata = new GridSearchTestData();
		try {
			testdata.numberGridOfRows = scanner.nextInt();
			testdata.numberGridOfColumns = scanner.nextInt();
		} catch (Exception ex) {
			throw new RuntimeException("Could not read gridRows or columns from stream!", ex);
		}
		testdata.gridRows = createPattern(scanner, testdata.numberGridOfRows);
		try {
			testdata.numberPatternOfRows = scanner.nextInt();
			testdata.numberPatternOfColumns = scanner.nextInt();
		} catch (Exception ex) {
			throw new RuntimeException("Could not read gridRows or columns from stream!", ex);
		}
		testdata.patternRows = createPattern(scanner, testdata.numberPatternOfRows);
		testdata.validate();
		return testdata;
	}

	public List<String> getGrid() {
		return gridRows;
	}
	
	public List<String> getPattern() {
		return patternRows;
	}
	
	private void validate() {
		if (numberGridOfColumns != gridRows.get(0).length()) {
			throw new IllegalStateException("Validation of testdata failed due of wrong number of "
					+ "columns in 'grid'! [exptected:"+numberGridOfColumns+", effective:"+gridRows.get(0).length()+"]");
		}
		if (numberPatternOfColumns != patternRows.get(0).length()) {
			throw new IllegalStateException("Validation of testdata failed due of wrong number of "
					+ "columns in 'pattern'! [exptected:"+numberPatternOfColumns+", effective:"+patternRows.get(0).length()+"]");
		}
	}
	
	private List<String> createPattern(Scanner scanner, int numberOfRows) {
		List<String> rows = new ArrayList<>(numberOfRows);
		int rowCounter = numberOfRows;
		try {
			while (rowCounter > 0) {
				rows.add(scanner.next());
				rowCounter--;
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not create rows! Error while processing input for row: " + (numberGridOfRows - rowCounter), ex);
		}
		return rows;
	}

	//	public GridSearchTestData(InputStream in) throws IOException {
	//	StreamTokenizer tokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(in)));
	////	tokenizer.parseNumbers();
	//	tokenizer.eolIsSignificant(false);
	//	try {
	//		tokenizer.nextToken();
	//		numberGridOfRows = (int) tokenizer.nval;
	//		tokenizer.nextToken();
	//		numberGridOfColumns = (int) tokenizer.nval;
	//	} catch (IOException ex) {
	//		throw new RuntimeException("Could not read gridRows or columns from stream! Failed to cast type: " + tokenizer.ttype + " to int!", ex);
	//	}
	//	gridRows = createPattern(in, numberGridOfRows);
	//	try {
	//		tokenizer.nextToken();
	//		numberPatternOfRows = (int) tokenizer.nval;
	//		tokenizer.nextToken();
	//		numberPatternOfColumns = (int) tokenizer.nval;
	//	} catch (IOException ex) {
	//		throw new RuntimeException("Could not read gridRows or columns from stream! Failed to cast type: " + tokenizer.ttype + " to int!", ex);
	//	}
	//	gridRows = createPattern(in, numberPatternOfRows);
	//}

}
