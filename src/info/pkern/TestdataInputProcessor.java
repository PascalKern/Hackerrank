package info.pkern;

import info.pkern.algorithms.impl.gridSearch.localClasses.GridSearchTestData;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestdataInputProcessor<T extends AbstractTestdata> {

	private final int numberOfTests;
	private List<T> testData;
	
	
	public TestdataInputProcessor(Class<T> clazz, InputStream in) {
		Scanner scanner = new Scanner(in);
		numberOfTests = readNumberOfTests(scanner);
		testData = createTestData(clazz, scanner);
	}

	public List<T> getTestDatas() {
		return new ArrayList<>(testData);
	}
	
	public T getTestData(int index) {
		return testData.get(index);
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
