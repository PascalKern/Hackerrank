package info.pkern;

import info.pkern.algorithms.impl.gridSearch.localClasses.Testdata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestdataHandler<T extends AbstractTestdata> {

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

//	public TestdataHandler(Class<T> clazz, Path testInput, boolean containsExptectedResults) throws IOException {
//		this(clazz, Files.newInputStream(testInput, StandardOpenOption.READ), containsExptectedResults);
//	}

	public TestdataHandler(Class<T> clazz, File testInput, boolean containsExptectedResults) throws IOException {
		this(clazz, new FileInputStream(testInput), containsExptectedResults);
	}
	
//	public TestdataHandler(Class<T> clazz, Path testInput) throws IOException {
//		this(clazz, testInput, false);
//	}

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
				test = clazz.newInstance();
				//Solution to be used when Testdata implementation is an inner class of Solution!
//				test = clazz.getConstructor(Solution.class).newInstance(new Solution());
				test = test.newInstance(scanner);
				mayContainExpectedResults = test.containsExptectedResults();
				test.setTestNr(numberOfTests - testCounter);
				testData.add(test);
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | SecurityException ex) {
			throw new RuntimeException("Could not create test datas!", ex);
		}
		return testData;
	}
	
}
