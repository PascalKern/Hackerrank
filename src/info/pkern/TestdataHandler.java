package info.pkern;

import info.pkern.algorithms.impl.gridSearch.localClasses.Testdata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestdataHandler<T extends AbstractTestdata> {

	private final int numberOfTests;
	private List<T> testdata;
	
	
	public TestdataHandler(Class<T> clazz, InputStream in) {
		Scanner scanner = new Scanner(in);
		numberOfTests = readNumberOfTests(scanner);
		testdata = createTestData(clazz, scanner);
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
				test = clazz.newInstance();
				test = test.newInstance(scanner);
				testData.add(test);
				testCounter--;
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | SecurityException ex) {
			throw new RuntimeException("Could not create test datas!", ex);
		}
		return testData;
	}
	
}
