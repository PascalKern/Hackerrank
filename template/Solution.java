package info.pkern......
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
 * @category ........
 * @see https://www.hackerrank.com/challenges/........
 */
@SuppressWarnings("unused")
public class Solution {
	
    public static void main(String[] args) {
    	
    	TestdataHandler<Testdata> inputProcessor = new TestdataHandler<>(Solution.Testdata.class, System.in);
    	
    	
    }
    
    
	
	
	
	
	/* ************************************************************************************************************
	 * Classes to solve this hackerrank challenge. When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/
//	public class Xyz {
	private static class Xyz ....
	
    
	/* ************************************************************************************************************
	 * Specific implementations of general helper classes (see bellow). When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/
	//Must be public when a inner class of Solution to be instantiated in the TestdataHandler.
	public class Testdata extends AbstractTestdata {

		@SuppressWarnings("unchecked")
		@Override
		public Testdata newInstance(Scanner scanner) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public String getExpectedString() {
			throw new RuntimeException("Not yet implemented!");
		}
		
		@Override
		public Object getExpected() {
			throw new RuntimeException("Not yet implemented!");
		}
		
		@Override
		public String getGridForSimulation() {
			throw new RuntimeException("Not yet implemented!");
		}
	}
	
	
	/* ************************************************************************************************************
	 * General helper classes for the submission of a hackerrank solution. When working on the solution locally
	 * those classes are available within the project. 
	 **************************************************************************************************************/
//	public class TestdataHandler<T extends AbstractTestdata> {
	/**
	 * @version 0.1 - (Hackerrank Solution-Environment)
	 * @author Pascal Kern
	 */
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
	/**
	 * @version 0.1 - (Hackerrank Solution-Environment)
	 * @author Pascal Kern
	 */
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
