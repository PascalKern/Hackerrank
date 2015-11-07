package info.pkern.sandbox.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.assertFalse;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.BagOfWords;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.TextClassifier;
import info.pkern.hackerrank.tools.MapUtil;
import info.pkern.hackerrank.tools.RecursiveSimpleFileVisitor;

import java.nio.charset.Charset;
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

//		String basePath = "/Users/pkern/Google Drive/BOW/smallset";
		String basePath = "/Users/pkern/Google Drive/BOW";
//		String basePath = "C:/Users/pascal/Google Drive/BOW";
		
		Path learnBase = Paths.get(basePath + "/learn_and_test/learn");
		Path testBase = Paths.get(basePath + "/learn_and_test/test");
		
//		TextClassifier textClassifier = new TextClassifier();
		TextClassifier textClassifier = new TextClassifier(5000);
		
		FileVisitor<Path> fileProcessor = new RecursiveSimpleFileVisitor("txt");
		Files.walkFileTree(learnBase, fileProcessor);
		
		Long start = System.nanoTime();
		
		String fileContent;
		for (Path file : ((RecursiveSimpleFileVisitor) fileProcessor).getFiles()) {
			fileContent = new String(Files.readAllBytes(file), "UTF-8"); // readAllLines(file, Charset.defaultCharset());
			fileContent = fileContent.replaceAll("[\\r\\n\\t\",;:?!.\\(\\){}]", " ");	//\p{Punct} or \\W
			fileContent = fileContent.replaceAll("'s", "");
			fileContent = fileContent.replaceAll("\\b-\\b", "");
			fileContent = fileContent.replaceAll("[0-9]", "");
			fileContent = fileContent.replaceAll("[_-]", " ");
			fileContent = fileContent.replaceAll(" {2,}", " ");
			fileContent = fileContent.trim();
			fileContent = fileContent.toLowerCase(Locale.ENGLISH);
			BagOfWords trainBag = new BagOfWords();
			trainBag.addTerms(Arrays.asList(fileContent.split("[\\s]")));
			textClassifier.train(trainBag, file.getParent().getFileName().toString());
		}
		
		Long loadTrainData = System.nanoTime() - start;
		start = System.nanoTime();
		
		textClassifier.finishTraining();
		
		Long trained = System.nanoTime() - start; 
		
		System.out.println("Trained finished!");
		
		fileProcessor = new RecursiveSimpleFileVisitor("txt");
		Files.walkFileTree(testBase, fileProcessor);
		
		start = System.nanoTime();

		int countFailures = 0;
		int countRights = 0;
		for (Path file : ((RecursiveSimpleFileVisitor) fileProcessor).getFiles()) {
			fileContent = new String(Files.readAllBytes(file), "UTF-8"); // readAllLines(file, Charset.defaultCharset());
			fileContent = fileContent.replaceAll("[\\r\\n\\t\",;:?!.\\(\\){}]", " ");	//\p{Punct} or \\W
			fileContent = fileContent.replaceAll("'s", "");
			fileContent = fileContent.replaceAll("\\b-\\b", "");
			fileContent = fileContent.replaceAll("[0-9]", "");
			fileContent = fileContent.replaceAll("[_-]", " ");
			fileContent = fileContent.replaceAll(" {2,}", " ");
			fileContent = fileContent.trim();
			fileContent = fileContent.toLowerCase(Locale.ENGLISH);
			BagOfWords testBag = new BagOfWords();
			testBag.addTerms(Arrays.asList(fileContent.split("[\\s]")));
			
			List<Entry<String, Double>> probabilities = textClassifier.getClassificationProbabilities(testBag);
			MapUtil.sortEntryListByValueDescending(probabilities);
			
			String filename = file.getFileName().toString();
			String className = filename.substring(0, filename.indexOf('.')).replaceAll("[0-9]", "");
			boolean isRight = className.equals(probabilities.get(0).getKey());
			System.out.println(String.format("%-14s %-6s %s", filename, isRight, probabilities));
			countFailures += (isRight)?0:1;
			countRights += (isRight)?1:0;
		}
		
		Long probCalc = System.nanoTime() - start;
		
		System.out.println("Total right: " + countRights);
		System.out.println("Total failures: " + countFailures);

		
		Path location = Paths.get("noGit");
		if (! Files.exists(location, LinkOption.NOFOLLOW_LINKS)) {
			Files.createDirectory(location, new FileAttribute[]{});
		}

		start = System.nanoTime();
		
		textClassifier.writeOutVectorsForVisualisation(location, "example.txt");
		
		Long writeData = System.nanoTime() - start;

		StringBuilder sb = new StringBuilder();
		sb.append("Load data:  ").append(loadTrainData/10E6).append(System.lineSeparator());
		sb.append("Trained:    ").append(trained/10E6).append(System.lineSeparator());
		sb.append("Prob calc:  ").append(probCalc/10E6).append(System.lineSeparator());
		sb.append("Write data: ").append(writeData/10E6).append(System.lineSeparator());
		sb.append("TOTAL:      ").append((loadTrainData + trained + probCalc + writeData)/10E6);
		System.out.println(sb.toString());
	}
}
