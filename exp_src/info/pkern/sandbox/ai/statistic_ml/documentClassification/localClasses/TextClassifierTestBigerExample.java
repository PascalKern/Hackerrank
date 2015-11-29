package info.pkern.sandbox.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.assertFalse;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.BagOfWords;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.DocumentClass;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.DocumentClassDetails;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.DocumentTokanizer;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.ENStopwordFilter;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.MinLengthFilter;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.SimpleTable;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.SimpleTableNamedColumnAdapter;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.TextClassifier;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.VectorMath;
import info.pkern.hackerrank.commons.ListTypeConverter;
import info.pkern.hackerrank.commons.MapUtil;
import info.pkern.hackerrank.commons.NumberUtil;
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

import javax.swing.plaf.ListUI;

import org.junit.Test;

public class TextClassifierTestBigerExample {
	
	private TextClassifier textClassifier;
	private Path basePath = Paths.get("exp_src/info/pkern/sandbox/ai/statistic_ml/documentClassification/localClasses/bowPythonTestdata");
	
	private DocumentTokanizer tokanizer;
	
	private String learn = "learn_and_test/learn";
	private String test = "learn_and_test/test";
	
	public static void main(String[] args) throws Exception {
		TextClassifierTestBigerExample testcase = new TextClassifierTestBigerExample();
		testcase.testLearn();
		
		
		/*Visualize some data*/
		TsnePlotExample plotter = new TsnePlotExample();
//		TsnePlotExample plotter = new TsnePlotExample(6);
		
		Long start = System.nanoTime();
		for (DocumentClass docClass : testcase.textClassifier.getDocumenClasses()) {
			if (docClass.hasDocClassDetials()) {
				SimpleTableNamedColumnAdapter<Integer> table = docClass.getDocClassDetails().getTermFrequenciesOfAllBags();
				//TODO Maybe use the classifier dictionary or its idf or reduced idf?!
//				table.filterColumns(docClass.getTfIdfWeightedFrequencies().keySet());
				SimpleTable<Integer> simpleTable = table.getSimpleTable();
				simpleTable.extendTableToColumnsCount(testcase.textClassifier.getDictionarySize());
				String lable = docClass.getName().substring(0, 2);
				int rowIndex = simpleTable.getRowsCount();
				while (rowIndex > 0) {
					List<Double> normalizedTermFrequencies = VectorMath.normlizeVectorEuclideanNorm(simpleTable.getRow(rowIndex-1));
					plotter.addVector(lable, ListTypeConverter.toPrimitive(normalizedTermFrequencies));
					rowIndex--;
				}
			}
			//Not a big difference!
			List<Double> weightedTerms = VectorMath.normlizeVectorEuclideanNorm(docClass.getTermFrequencies().getFrequencies().values());
//			List<Double> weightedTerms = VectorMath.normlizeVectorEuclideanNorm(docClass.getWeightedFrequencies().values());
			plotter.addVectorExtendedToCurrentPlotterSize(docClass.getName().toUpperCase(), ListTypeConverter.toPrimitive(weightedTerms));
		}
		BagOfWords bagGood = new BagOfWords();
		bagGood.addTerms(testcase.tokanize(testcase.basePath.resolve(testcase.test).resolve("lawyer/lawyer23.txt")));
		BagOfWords bagBad = new BagOfWords();
		bagBad.addTerms(testcase.tokanize(testcase.basePath.resolve(testcase.test).resolve("lawyer/lawyer303.txt")));
		plotter.addVectorExtendedToCurrentPlotterSize("Laweyer23", ListTypeConverter.toPrimitive(VectorMath.normlizeVectorEuclideanNorm(bagGood.getFrequencies()).values().toArray(new Double[]{})));
		plotter.addVectorExtendedToCurrentPlotterSize("Laweyer303", ListTypeConverter.toPrimitive(VectorMath.normlizeVectorEuclideanNorm(bagBad.getFrequencies()).values().toArray(new Double[]{})));
		
		Long writeData = System.nanoTime() - start;
		System.out.println("Write data: " + writeData/10E6);

		plotter.display();
	}
	
	
	@Test
	public void testLearn() throws Exception {

		Path learnBase = basePath.resolve(learn);
		Path testBase = basePath.resolve(test);
		
//		textClassifier = new TextClassifier(null, true);
		textClassifier = new TextClassifier(5000, true);
		
		FileVisitor<Path> fileProcessor = new RecursiveSimpleFileVisitor("txt");
		Files.walkFileTree(learnBase, fileProcessor);
		
		tokanizer = new DocumentTokanizer();
//		tokanizer.addFilter(new ENStopwordFilter());
		
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
		System.out.println("Total right: " + right + " " + new Double((100 / total)*right) + "%");
		System.out.println("Total failures: " + failures + " " + new Double((100 / total)*failures) + "%");
		
		
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
		
		return tokanizer.tokanize(fileContent);
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
		private String fileName;
		private boolean isRight;
		private List<Entry<String, Double>> results;
		 
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%-12s => %.5e ", "diff max touple: " , results.get(0).getValue() - results.get(1).getValue()));
			for (Entry<String, Double> entry : results) {
				sb.append(String.format("%-9s%.10e", entry.getKey() + ":", entry.getValue())).append(", ");
			}
			sb.delete(sb.lastIndexOf(","), sb.length());
			return String.format("%-14s %-6s %-14s %s", fileName, isRight, exptected, sb.toString());
		}
	}
	
}
