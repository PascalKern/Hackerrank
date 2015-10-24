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

	@SuppressWarnings("unchecked")
	@Override
	public Testdata newInstance(Scanner scanner) {
		Testdata testdata = new Testdata();
		testdata.grid = createGrid(scanner);
		testdata.pattern = createGrid(scanner);
		return testdata;
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

}
