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

import info.pkern.AbstractTestdata;

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
