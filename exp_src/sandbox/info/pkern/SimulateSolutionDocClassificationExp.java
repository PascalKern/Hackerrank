package sandbox.info.pkern;

import info.pkern.ai.statistic_ml.documentClassification.Solution;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class SimulateSolutionDocClassificationExp {

	private final String TRAININGS_DATA_FILE = "orig_trainingdata.txt";
	private final Integer TESTS_COUNT = 10;
	
	private List<TestDataEntry> testData = new ArrayList<>(TESTS_COUNT);
	private Path tempTrainingsData;
	
	
	@Test
	public void simulateSolutionTest() throws IOException {
		prepareTestData();
		
		String[] args = new String[TESTS_COUNT + 1];
		args[0] = TESTS_COUNT.toString();
		for (int i = 1; i < TESTS_COUNT; i++) {
			args[i] = testData.get(i).text;
		}
		
		System.out.println("Input lines: =======================================================");
		for (String line : args) {
			System.out.println(line);
		}
		
		//Capture output
		PrintStream oldOut = System.out; 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream newOut = new PrintStream(baos);
		System.setOut(newOut);
		
		Solution.main(args);
		
		//Reset output to default
		System.out.flush();
		System.setOut(oldOut);
		
		String output = new String(baos.toByteArray());
		System.out.println("Output lines: =======================================================");
		System.out.print(output);
		
		System.out.println("Results: ============================================================");
		String[] results = output.split(System.lineSeparator());
		Integer correctAnswerCounter = 0;
		for (int i = 0, j = 1; i < results.length; i++, j++) {
			String answerValue = results[i];
			String correctAnswer = testData.get(i).docClass.toString();
			boolean result = answerValue.equals(correctAnswer);// new Boolean(answerValue == correctAnswer);
			if (result) {
				correctAnswerCounter++;
			}
			System.out.println(String.format("Input line #%"+TESTS_COUNT.toString().length()+"d: %-5s! [Answered = %s, Expteced = %s]",j , result, answerValue, correctAnswer));
		}
		Long score = new Long(100 * (correctAnswerCounter - (TESTS_COUNT - correctAnswerCounter)) / TESTS_COUNT);
		System.out.println("=> Score: " + score);
	}
	
	
	/**
	 * Read the traingsdata beside the {@link Solution} class and prepares some random testdata from
	 * the read content. The testdata is written in to the source <strong>anc</strong> binary directory
	 * of the <code>Solution</code> file. The trainingsdata beside the {@link Solution} class does <strong>not
	 * </strong> contain the testdata afterwards. This way the test can be simulated correct like when submmissing
	 * to Hackerrank.com where the trainingdata is also beside the Solution class.
	 *  
	 * @throws IOException
	 */
	private void prepareTestData() throws IOException {
//		Path tempTrainingsData = Files.createTempFile("trainingsdata_documentClassification", null, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-r--r--")));
		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-r--r--");
		FileAttribute<Set<PosixFilePermission>> attributes = PosixFilePermissions.asFileAttribute(perms);
//		tempTrainingsData = Files.createTempFile("trainingsdata_documentClassification", null, attributes);
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
		
		Files.copy(trainingsDataBin, Paths.get("src", solutionClassPath, "trainingdata.txt"), StandardCopyOption.REPLACE_EXISTING);
	}


	private class TestDataEntry {
		private final Integer docClass;
		private final String text;
		
		public TestDataEntry(Integer docClass, String text) {
			this.docClass = docClass;
			this.text = text;
		}
	}

}
