package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.junit.Test;

public class PoolTest {

	@Test
	public void testLearn() throws Exception {

//		String basePath = "/Users/pkern/Google Drive/BOW";
		String basePath = "C:/Users/pascal/Google Drive/BOW";
		
		Path learnBase = Paths.get(basePath + "/learn_and_test/learn");
		Path testBase = Paths.get(basePath + "/learn_and_test/test");
		Pool pool = new Pool();
		pool.debug = false;
		
		FileVisitor<Path> fileProcessor = new ProcessFile("txt");
		Files.walkFileTree(learnBase, fileProcessor);
		
		String fileContent;
		for (Path file : ((ProcessFile) fileProcessor).getFiles()) {
			fileContent = new String(Files.readAllBytes(file)); // readAllLines(file, Charset.defaultCharset());
			BagOfWords trainBag = new BagOfWords();
			fileContent = fileContent.replaceAll("[\"]", "");
//			trainBag.addWords(Arrays.asList(fileContent.split("[^\\wäöüÄÖÜß]")));
			trainBag.addWords(Arrays.asList(fileContent.split("[\\s,\\.;':!?]")));
			pool.learn(trainBag, file.getParent().getFileName().toString());
		}

		pool.normalizePool();
		
		System.out.println("Trained finished!");
		System.out.println("Pool structcure {docClass = number of documents}: "+pool.getClassesWithDocumentCount());
		System.out.println("Total words in pool: "+pool.getNumberOfWords());
		
		fileProcessor = new ProcessFile("txt");
		Files.walkFileTree(testBase, fileProcessor);
		
		for (Path file : ((ProcessFile) fileProcessor).getFiles()) {
			String fileName = file.getFileName().toString();
			String dclass = fileName.substring(0, fileName.lastIndexOf(".")).replaceAll("[0-9]", "");
			
			fileContent = new String(Files.readAllBytes(file)); // readAllLines(file, Charset.defaultCharset());
			BagOfWords testBag = new BagOfWords();
			fileContent = fileContent.replaceAll("[\"]", "");
//			testBag.addWords(Arrays.asList(fileContent.split("[^\\wäöüÄÖÜß]")));
			testBag.addWords(Arrays.asList(fileContent.split("[\\s,\\.;':]")));
//				probability = pool.probability(testBag, dclass);
			Map<String, Float> probAllClasses = pool.probability(file.getFileName().toString(), testBag);
			entriesSortedByValues(probAllClasses);
//			System.out.println(dclass + " = " + file.getFileName().toString() + ", probybility: " + probability + ". Probabilities: " + probAllClasses.entrySet());
//			System.out.println(dclass + " = " + file.getFileName().toString() + "; Probabilities: " + probAllClasses.entrySet());
			System.out.println(file.getFileName().toString() + " = Probabilities: " + probAllClasses.entrySet());
		}
		
		
		
	}

	@Test
	public void test2() {
		Float prod = new Float(1);
		Float r = new Float(2);
		prod *= r;
		assertFalse(prod.isInfinite());
		System.out.println(prod);
	}
	
	
	@Test
	public void test3() {
		System.out.println(1.0f / 0.0f);
		System.out.println(new Float("Infinity"));
		System.out.println(Float.parseFloat("Infinity"));
		System.out.println(new Float(0.12242519153117467336066203000015));
		System.out.println(new Float(0.08145700457195284754830961450242));
		System.out.println(new Float(-1f));
	}
	
	
	@Test
	public void test() {
		Float test = new Float(0);
		System.out.println("Test: " + test);
		test += 1;
		System.out.println("Test: " + test);
		test += 0.2f;
		System.out.println("Test: " + test);
		test *= 1;
		System.out.println("Test: " + test);
		test *= new Float(0.5);
		System.out.println("Test: " + test);
	}
	
	
	
	@Test
	public void test4() {
		Map<String, Float> probAllClasses = new HashMap<>();
		probAllClasses.put("E", 1f);
		probAllClasses.put("D", 2.2f);
		probAllClasses.put("C", 0.1f);
		probAllClasses.put("B", 0.0001f);
		probAllClasses.put("A", 0.03f);
		System.out.println(entriesSortedByValues(probAllClasses));
	}
	
	
	
	
	private <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(
			Map<K, V> map) {
		return entriesSortedByValues(map, null);
	}
	
	private <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K, V> map, final String direction) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				if (direction != null && direction.equals("asc")) {
					return e1.getValue().compareTo(e2.getValue());
				} else {
					return e2.getValue().compareTo(e1.getValue());
				}
			}
		});

		return sortedEntries;
	}
	
	
	
	private final class ProcessFile extends SimpleFileVisitor<Path> {
		
		private List<Path> files = new ArrayList<>();
		private final String fileSuffix;
		
		public ProcessFile(String fileSuffix) {
			this.fileSuffix = fileSuffix;
		}
		
		public List<Path> getFiles() {
			return files;
		}
		
		@Override
		public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs)
				throws IOException {
			String fileName = aFile.getFileName().toString();
			if (fileName.substring(fileName.lastIndexOf(".")+1).equals(fileSuffix)) {
				files.add(aFile);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path aDir,
				BasicFileAttributes aAttrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	}
}
