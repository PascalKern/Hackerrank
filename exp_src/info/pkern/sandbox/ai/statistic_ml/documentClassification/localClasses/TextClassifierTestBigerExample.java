package info.pkern.sandbox.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.assertFalse;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.BagOfWords;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.TextClassifier;
import info.pkern.hackerrank.tools.MapUtil;
import info.pkern.hackerrank.tools.RecursiveSimpleFileVisitor;

import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class TextClassifierTestBigerExample {

	@Test
	public void testLearn() throws Exception {

//		String basePath = "/Users/pkern/Google Drive/BOW";
		String basePath = "C:/Users/pascal/Google Drive/BOW";
		
		Path learnBase = Paths.get(basePath + "/learn_and_test/learn");
		Path testBase = Paths.get(basePath + "/learn_and_test/test");
		
		TextClassifier textClassifier = new TextClassifier();
//		TextClassifier textClassifier = new TextClassifier(500);
		
		FileVisitor<Path> fileProcessor = new RecursiveSimpleFileVisitor("txt");
		Files.walkFileTree(learnBase, fileProcessor);
		
		String fileContent;
		for (Path file : ((RecursiveSimpleFileVisitor) fileProcessor).getFiles()) {
			fileContent = new String(Files.readAllBytes(file)); // readAllLines(file, Charset.defaultCharset());
			fileContent.toLowerCase(Locale.ENGLISH);
			fileContent = fileContent.replaceAll("[\\r\\n\\t\",;:?!.\\(\\)]", " ");
			fileContent = fileContent.replaceAll("'s", "");
			fileContent = fileContent.replaceAll("\\b-\\b", "");
			fileContent = fileContent.replaceAll("[0-9]", "");
			fileContent = fileContent.replaceAll("[_-]", " ");
			fileContent = fileContent.replaceAll(" {2,}", " ");
			BagOfWords trainBag = new BagOfWords();
			trainBag.addTerms(Arrays.asList(fileContent.trim().split("[\\s]")));
			textClassifier.train(trainBag, file.getParent().getFileName().toString());
		}
		
//		textClassifier.setUseNormalizedFrequences(true);
		textClassifier.finishTraining();
		
		System.out.println("Trained finished!");
		
		fileProcessor = new RecursiveSimpleFileVisitor("txt");
		Files.walkFileTree(testBase, fileProcessor);
		
		int countFailures = 0;
		int countRights = 0;
		for (Path file : ((RecursiveSimpleFileVisitor) fileProcessor).getFiles()) {
			fileContent = new String(Files.readAllBytes(file)); // readAllLines(file, Charset.defaultCharset());
			fileContent.toLowerCase(Locale.ENGLISH);
			fileContent = fileContent.replaceAll("[\\r\\n\\t\",;:?!.\\(\\)]", " ");
			fileContent = fileContent.replaceAll("'s", "");
			fileContent = fileContent.replaceAll("\\b-\\b", "");
			fileContent = fileContent.replaceAll("[0-9]", "");
			fileContent = fileContent.replaceAll("[_-]", " ");
			fileContent = fileContent.replaceAll(" {2,}", " ");
			BagOfWords testBag = new BagOfWords();
			testBag.addTerms(Arrays.asList(fileContent.trim().split("[\\s]")));
			
			List<Entry<String, Double>> probabilities = textClassifier.getClassificationProbabilities(testBag);
			MapUtil.sortEntryListByValueDescending(probabilities);
			
			String filename = file.getFileName().toString();
			String className = filename.substring(0, filename.indexOf('.')).replaceAll("[0-9]", "");
			boolean isRight = className.equals(probabilities.get(0).getKey());
			System.out.println(String.format("%-14s %-6s %s", filename, isRight, probabilities));
			countFailures += (isRight)?0:1;
			countRights += (isRight)?1:0;
		}
		System.out.println("Total right: " + countRights);
		System.out.println("Total failures: " + countFailures);
		
		Path location = Paths.get("noGit");
		if (! Files.exists(location, LinkOption.NOFOLLOW_LINKS)) {
			Files.createDirectory(location, new FileAttribute[]{});
		}
		textClassifier.writeOutVectorsForVisualisation(location, "example.txt");
		
	}
}
