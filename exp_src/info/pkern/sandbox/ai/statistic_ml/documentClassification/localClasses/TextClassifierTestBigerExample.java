package info.pkern.sandbox.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.assertFalse;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.BagOfWords;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.DocumentClass;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.SimpleTable;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.SimpleTableNamedColumnAdapter;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.TextClassifier;
import info.pkern.hackerrank.commons.ListTypeConverter;
import info.pkern.hackerrank.commons.MapUtil;
import info.pkern.hackerrank.commons.RecursiveSimpleFileVisitor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class TextClassifierTestBigerExample {
	
	private TextClassifier textClassifier;
//	private Path basePath = Paths.get("/Users/pkern/Google Drive/BOW/smallset");
	private Path basePath = Paths.get("/Users/pkern/Google Drive/BOW");
//	private Path basePath = Paths.get("C:/Users/pascal/Google Drive/BOW");
	
	private String learn = "learn_and_test/learn";
	private String test = "learn_and_test/test";
	
	public static void main(String[] args) throws Exception {
		TextClassifierTestBigerExample testcase = new TextClassifierTestBigerExample();
		testcase.testLearn();
		
		TsnePlotExample plotter = new TsnePlotExample();
//		TsnePlotExample plotter = new TsnePlotExample(6);
		
		Long start = System.nanoTime();
		for (DocumentClass docClass : testcase.textClassifier.getDocumenClasses()) {
			SimpleTableNamedColumnAdapter<Double> table = docClass.getNormalizedTermFrequenciesOfAllBags();
			table.filterColumns(docClass.getTfIdfWeightedFrequencies().keySet());
			SimpleTable<Double> simpleTable = table.getSimpleTable();
			simpleTable.extendTableToColumnsCount(testcase.textClassifier.getMaxNumberAllowedTerms());
			
			String lable = docClass.getName().substring(0, 2);
			int rowIndex = simpleTable.getRowsCount();
			while (rowIndex > 0) {
				plotter.addVector(lable, ListTypeConverter.toPrimitive(simpleTable.getRow(rowIndex-1)));
				rowIndex--;
			}
			/*//This "disturbs" the clustering of the visualisation
			List<Double> weightedTerms = new ArrayList<>();
			weightedTerms.addAll(docClass.getTfIdfWeightedFrequencies().values());		
			plotter.addVector(docClass.getName().toUpperCase(), ListTypeConverter.getPrimitive(weightedTerms));
			*/
		}

		BagOfWords bagGood = new BagOfWords();
		bagGood.addTerms(testcase.tokanize(testcase.basePath.resolve(testcase.test).resolve("lawyer/lawyer23.txt")));
		BagOfWords bagBad = new BagOfWords();
		bagBad.addTerms(testcase.tokanize(testcase.basePath.resolve(testcase.test).resolve("lawyer/lawyer303.txt")));
		plotter.addVector("Laweyer23", testcase.textClassifier.normalizeBagWithIDFVocabulary(bagGood));
		plotter.addVector("Laweyer303", testcase.textClassifier.normalizeBagWithIDFVocabulary(bagBad));
		
		Long writeData = System.nanoTime() - start;
		System.out.println("Write data: " + writeData/10E6);
		
		
		
		plotter.display();
	}
	
	
	@Test
	public void testLearn() throws Exception {

		Path learnBase = basePath.resolve(learn);
		Path testBase = basePath.resolve(test);
		
//		textClassifier = new TextClassifier();
		textClassifier = new TextClassifier(5000);
		
		FileVisitor<Path> fileProcessor = new RecursiveSimpleFileVisitor("txt");
		Files.walkFileTree(learnBase, fileProcessor);
		
		Long start = System.nanoTime();
		
		for (Path file : ((RecursiveSimpleFileVisitor) fileProcessor).getFiles()) {
			BagOfWords trainBag = new BagOfWords();
			trainBag.addTerms(tokanize(file));
			textClassifier.train(trainBag, file.getParent().getFileName().toString());
		}
		
		Long loadTrainData = System.nanoTime() - start;
		start = System.nanoTime();
		
		textClassifier.finishTraining();
		
		Long trained = System.nanoTime() - start; 
		
		System.out.println("Trained finished!");
		
		fileProcessor = new RecursiveSimpleFileVisitor("txt");
		Files.walkFileTree(testBase, fileProcessor);

		
		Map<String, Results> results = new HashMap<>();
		results.put(Boolean.toString(true), new Results());
		results.put(Boolean.toString(false), new Results());
		
		start = System.nanoTime();

		for (Path file : ((RecursiveSimpleFileVisitor) fileProcessor).getFiles()) {
			BagOfWords testBag = new BagOfWords();
			testBag.addTerms(tokanize(file));
			
			List<Entry<String, Double>> probabilities = textClassifier.getClassificationProbabilities(testBag);
			MapUtil.sortByValueDescending(probabilities);
			
			Result res = new Result();
			res.fileName = file.getFileName().toString();
			res.exptected = res.fileName.substring(0, res.fileName.indexOf('.')).replaceAll("[0-9]", "");
			res.isRight = res.exptected.equals(probabilities.get(0).getKey());
			res.results = probabilities;
			results.get(Boolean.toString(res.isRight)).results.add(res);
		}
		
		Long probCalc = System.nanoTime() - start;
		
		System.out.println(results.get(Boolean.toString(true)));
		int right = results.get(Boolean.toString(true)).results.size();
		System.out.println(results.get(Boolean.toString(false)));
		int failures = results.get(Boolean.toString(false)).results.size();
		float total = right + failures;
		System.out.println("Total right: " + right + " " + new Double((total / 100)*right) + "%");
		System.out.println("Total failures: " + failures + " " + new Double((total / 100)*failures) + "%");
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("Load data:  ").append(loadTrainData/10E6).append(System.lineSeparator());
		sb.append("Trained:    ").append(trained/10E6).append(System.lineSeparator());
		sb.append("Prob calc:  ").append(probCalc/10E6).append(System.lineSeparator());

		sb.append("TOTAL:      ").append((loadTrainData + trained + probCalc)/10E6);

//		start = System.nanoTime();
//		textClassifier.writeOutVectorsForVisualisation(Paths.get("./noGit"), "example.txt");
//		Long writeData = System.nanoTime() - start;
//		sb.append("Write data: ").append(writeData/10E6).append(System.lineSeparator());
//		sb.append("TOTAL:      ").append((loadTrainData + trained + probCalc + writeData)/10E6);
		

		System.out.println(sb.toString());
	}

	
	
	
	private List<String> tokanize(Path file) throws UnsupportedEncodingException,
			IOException {
		String fileContent;
		fileContent = new String(Files.readAllBytes(file), "UTF-8"); // readAllLines(file, Charset.defaultCharset());
		fileContent = fileContent.replaceAll("[\\r\\n\\t\",;:?!.\\(\\){}]", " ");	//\p{Punct} or \\W
		fileContent = fileContent.replaceAll("'s", "");
		fileContent = fileContent.replaceAll("\\b-\\b", "");
		fileContent = fileContent.replaceAll("[0-9]", "");
		fileContent = fileContent.replaceAll("[_-]", " ");
		fileContent = fileContent.replaceAll(" {2,}", " ");
		fileContent = fileContent.trim();
		fileContent = fileContent.toLowerCase(Locale.ENGLISH);
		return Arrays.asList(fileContent.split("[\\s]"));
	}
	
	private class Results {
		private List<Result> results = new ArrayList<>();
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Result result : results) {
				sb.append(result).append(System.lineSeparator());
			}
			sb.delete(sb.length() -1, sb.length());
			return sb.toString();
		}
	}
	
	private class Result {
		private String exptected;
		private String fileName;
		private boolean isRight;
		private List<Entry<String, Double>> results;
		 
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, Double> entry : results) {
				sb.append(String.format("%-9s%.10e", entry.getKey() + ":", entry.getValue())).append(", ");
			}
			sb.delete(sb.lastIndexOf(","), sb.length());
			return String.format("%-14s %-6s %-14s %s", fileName, isRight, exptected, sb.toString());
		}
	}
	
}
