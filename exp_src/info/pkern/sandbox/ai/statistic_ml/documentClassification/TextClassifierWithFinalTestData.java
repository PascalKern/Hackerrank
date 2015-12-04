package info.pkern.sandbox.ai.statistic_ml.documentClassification;

import info.pkern.ai.statistic_ml.documentClassification.Solution;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.BagOfWords;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.DocumentTokanizer;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.ENStopwordFilter;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.TextClassifier;
import info.pkern.hackerrank.commons.MapUtil;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class TextClassifierWithFinalTestData {

	private final static String TRAININGS_DATA_FILE = "orig_trainingdata.txt";
	private final static Integer TESTS_COUNT = 10;

	private static List<TestDataEntry> testData;
	private static Path tempTrainingsData;

	private boolean runMultipleTimes = false;
	
	@BeforeClass
	public static void init() throws IOException {
		prepareTestData();
	}
	
	private int overAllRight = 0;
	private int overAllFalse = 0;
	
	@Test
	public void simulateMultiTest() throws IOException {
		runMultipleTimes = true;
		int counter = 10;
		while (counter > 0) {
			System.out.println("Test run #"+counter);
			prepareTestData();
			simulateSolutionTest();
			counter--;
		}
		float total = overAllRight + overAllFalse;
		System.out.println("------------------------------------------------------");
		System.out.println("Over all results:");
		System.out.println("Total right: " + overAllRight + " = " + new Double((100 / total)*overAllRight) + "%");
		System.out.println("Total failures: " + overAllFalse + " = " + new Double((100 / total)*overAllFalse) + "%");
		System.out.println("------------------------------------------------------");
	}
	
	@Test
	public void simulateSolutionTest() throws IOException {
		
		DocumentTokanizer tokanizer = new DocumentTokanizer();
//		tokanizer.addFilter(new ENStopwordFilter());	//Increases accuracy of the classifier only from 97 to 98% with ten test-entries and ten testruns.
		
//		TextClassifier classifier = new TextClassifier(0.85);
		TextClassifier classifier = new TextClassifier();
		
		System.out.println("Start training...");
		InputStream is = Solution.class.getResourceAsStream(TRAININGS_DATA_FILE);
		BufferedReader trainingsDataReader = new BufferedReader(new InputStreamReader(is));
		trainingsDataReader.readLine();
		String line ;
		while (null != (line = trainingsDataReader.readLine())) {
			String[] data = line.split(" ", 2);
			BagOfWords bow = new BagOfWords();
			bow.addTerms(tokanizer.tokanize(data[1]));
			classifier.train(bow, data[0]);
		}
		
		System.out.println("Start analyze of data...");
		classifier.finishTraining();
		
		System.out.println("Start propbability evaluation...");
		Map<String, Results> results = new HashMap<>();
		results.put(Boolean.toString(true), new Results());
		results.put(Boolean.toString(false), new Results());
		for (TestDataEntry testEntry : testData) {
			BagOfWords testBag = new BagOfWords();
			testBag.addTerms(tokanizer.tokanize(testEntry.text));
			
			List<Entry<String, Double>> probabilities = classifier.getClassificationProbabilities(testBag);
//			MapUtil.sortByValueDescending(probabilities);
			
			Result res = new Result();
			res.exptected = testEntry.docClass.toString();
			res.isRight = res.exptected.equals(probabilities.get(0).getKey());
			res.falseContent = (res.isRight)?null:testBag;
			res.results = probabilities;
			results.get(Boolean.toString(res.isRight)).results.add(res);
		}

		if (!runMultipleTimes) {
			System.out.println(results.get(Boolean.toString(true)));
			System.out.println(results.get(Boolean.toString(false)));
		}
		int right = results.get(Boolean.toString(true)).results.size();
		overAllRight += right;
		int failures = results.get(Boolean.toString(false)).results.size();
		overAllFalse += failures;
		float total = right + failures;
		System.out.println("Total right: " + right + " = " + new Double((100 / total)*right) + "%");
		System.out.println("Total failures: " + failures + " = " + new Double((100 / total)*failures) + "%");
		
	}

	
	
	private class Results {
		private List<Result> results = new ArrayList<>();
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Result result : results) {
				sb.append(result).append(System.lineSeparator());
			}
			if (!results.isEmpty()) {
				sb.delete(sb.lastIndexOf(System.lineSeparator())-1, sb.length());
			}
			return sb.toString();
		}
	}
	
	private class Result {
		private String exptected;
		private BagOfWords falseContent;
		private boolean isRight;
		private List<Entry<String, Double>> results;
		 
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%-12s%f ", "diff max touple: " , results.get(0).getValue() - results.get(1).getValue()));
			for (Entry<String, Double> entry : results) {
				sb.append(String.format("%-9s%.10e", entry.getKey() + ":", entry.getValue())).append(", ");
			}
			sb.delete(sb.lastIndexOf(","), sb.length());
//			return String.format("%-6s %-14s %s%s", isRight, exptected, sb.toString(), (isRight)?"":"\n"+falseContent);
			return String.format("%-6s %-14s %s", isRight, exptected, sb.toString());
		}
	}
	
	
	
	
	private static void prepareTestData() throws IOException {
		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-r--r--");
		FileAttribute<Set<PosixFilePermission>> attributes = PosixFilePermissions.asFileAttribute(perms);
		String solutionClassPath = Solution.class.getPackage().getName().replace(".", "/");
		Path trainingsDataBin = Paths.get("bin", solutionClassPath, "trainingdata.txt");
		Files.deleteIfExists(trainingsDataBin);
		tempTrainingsData = Files.createFile(trainingsDataBin, attributes);
		InputStream is = Solution.class.getResourceAsStream(TRAININGS_DATA_FILE);
		BufferedReader trainingsDataReader = new BufferedReader(new InputStreamReader(is));

		Integer origLineCount = Integer.parseInt(trainingsDataReader.readLine());
		Integer newLineCount = origLineCount - TESTS_COUNT;

		Set<Integer> testLines = new HashSet<Integer>(TESTS_COUNT);
		Random random = new Random();
		for (int i = 0; i < TESTS_COUNT; i++) {
			testLines.add(random.nextInt(origLineCount));
		}

		testData = new ArrayList<>();
		try (FileWriter tempTrainingsdataDataWriter = new FileWriter(tempTrainingsData.toFile())) {
			tempTrainingsdataDataWriter.write(newLineCount + "\n");

			String line;
			Integer lineCounter = 1;
			while (null != (line = trainingsDataReader.readLine())) {
				if (testLines.contains(lineCounter)) {
					String text = line.substring(line.indexOf(" ") + 1);
					Integer docClass = Integer.valueOf(line.substring(0, line.indexOf(" ")));
					testData.add(new TestDataEntry(docClass, text));
				} else {
					tempTrainingsdataDataWriter.write(line + "\n");
				}
				lineCounter++;
			}
		} catch (IOException ex) {
			throw ex;
		}

		Files.copy(trainingsDataBin, Paths.get("src", solutionClassPath, "trainingdata.txt"),
				StandardCopyOption.REPLACE_EXISTING);
	}

	private static class TestDataEntry {
		private final Integer docClass;
		private final String text;

		public TestDataEntry(Integer docClass, String text) {
			this.docClass = docClass;
			this.text = text;
		}
	}

}
