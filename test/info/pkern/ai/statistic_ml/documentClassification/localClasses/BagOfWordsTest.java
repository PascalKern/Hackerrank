package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class BagOfWordsTest {

	private static List<String> words = new ArrayList<>();
	
	@BeforeClass
	public static void initClass() {
		words.add("He");
		words.add("has");
		words.add("a");
		words.add("hat");
		words.add("which");
		words.add("he");
		words.add("has");
		words.add("since");
		words.add("he");
		words.add("was");
		words.add("a");
		words.add("child");
	}

	@Test
	public void initTest() {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		
		Integer expectedNrOfWords = 9;
		Double exptectedNormalizedFrequence = 0.471404520d;
		Integer exptecedFrequency = 2;
		
		assertEquals(expectedNrOfWords, testBag.getNumberOfTerms());
		assertEquals(exptectedNormalizedFrequence, VectorMath.normlizeVectorEuclideanNorm(testBag.getFrequencies()).get("has"), 0.0000001d);
		assertEquals(exptecedFrequency, testBag.getFrequency("has"));
		exptecedFrequency = 1;
		assertEquals(exptecedFrequency, testBag.getFrequency("He"));
	}
	
	@Test
	public void testRemoveTerm() {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		testBag.removeTerm("has");

		Integer expectedNrOfWords = 8;
		Integer exptectedFrequence = 0;
		
		assertFalse(testBag.contains("has"));
		assertEquals(expectedNrOfWords, testBag.getNumberOfTerms());
		assertEquals(exptectedFrequence, testBag.getFrequency("has"));
		exptectedFrequence = 1;
		assertEquals(exptectedFrequence, testBag.getFrequency("He"));
	}

	@Test
	public void testIncreseTermFrequency() {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		
		testBag.addTerm("he", 3);
		
		Integer expectedNrOfWords = 9;
		Integer exptectedFrequency = 5;
		
		assertEquals(expectedNrOfWords, testBag.getNumberOfTerms());
		assertEquals(exptectedFrequency, testBag.getFrequency("he"));
		exptectedFrequency = 2;
		assertEquals(exptectedFrequency, testBag.getFrequency("has"));
		exptectedFrequency = 1;
		assertEquals(exptectedFrequency, testBag.getFrequency("hat"));
	}
	
	
	@Test
	public void testAdd() throws Exception {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		BagOfWords secondTestBag = new BagOfWords();
		secondTestBag.addTerms(words);
		
		testBag.add(secondTestBag);
		
		Integer expectedNrOfWords = 9;
		Double exptectedNormalizedFrequence = 0.471404520d;
		Integer exptectedFrequency = 4;
		
		assertEquals(expectedNrOfWords, testBag.getNumberOfTerms());
		assertEquals(exptectedFrequency, testBag.getFrequency("has"));
		assertEquals(exptectedNormalizedFrequence, VectorMath.normlizeVectorEuclideanNorm(testBag.getFrequencies()).get("has"), 0.0000001d);
		exptectedFrequency = 2;
		assertEquals(exptectedFrequency, testBag.getFrequency("child"));
	}

	@Test
	public void testHumanReadable() throws Exception {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getFrequency("he"));
		System.out.println(testBag.getFrequency("He"));
		System.out.println(testBag.getFrequency("has"));
		System.out.println(testBag.getFrequency("He"));
		System.out.println(testBag.getFrequency("has"));
		
		int freq = testBag.removeTerm("since");
		System.out.println(freq);
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getFrequency("has"));
		freq = testBag.removeTerm("has");
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getFrequency("has"));
		System.out.println(testBag.getFrequency("he"));
		System.out.println(testBag.getFrequency("He"));
		testBag.addTerm("he", 3);
		
		testBag.addTerm("blubber");
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getFrequency("has"));
		System.out.println(testBag.getFrequency("he"));
		System.out.println(testBag.getFrequency("blubber"));
	}

	@Test
	public void testTermsNotIn() throws Exception {
		BagOfWords bagA = new BagOfWords();
		BagOfWords bagB = new BagOfWords();
		bagA.addTerm("one");
		bagA.addTerm("two");
		bagA.addTerm("three");
		
		bagB.addTerm("two");
		bagB.addTerm("three");
		bagB.addTerm("four");
		bagB.addTerm("five");
		Set<String>  expected = new HashSet<>(Arrays.asList("one"));
		assertEquals(expected, bagA.termsNotIn(bagB));
		expected = new HashSet<>(Arrays.asList("four", "five"));
		assertEquals(expected, bagB.termsNotIn(bagA));

	}
	
}
