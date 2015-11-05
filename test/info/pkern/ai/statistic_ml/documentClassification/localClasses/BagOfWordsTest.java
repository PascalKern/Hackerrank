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
		Double exptectedNormalizedFrequence = 1d;
		
		assertEquals(expectedNrOfWords, testBag.getNumberOfTerms());
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("has"));
		exptectedNormalizedFrequence = 0.5d;
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("He"));
	}
	
	@Test
	public void testRemoveTerm() {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		testBag.removeTerm("has");

		Integer expectedNrOfWords = 8;
		Double exptectedNormalizedFrequence = 0d;
		
		assertFalse(testBag.contains("has"));
		assertEquals(expectedNrOfWords, testBag.getNumberOfTerms());
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("has"));
		exptectedNormalizedFrequence = 0.5d;
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("He"));
	}

	@Test
	public void testIncreseTermFrequency() {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		
		testBag.addTerm("he", 3);
		
		Integer expectedNrOfWords = 9;
		Double exptectedNormalizedFrequence = 1d;
		
		assertEquals(expectedNrOfWords, testBag.getNumberOfTerms());
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("he"));
		exptectedNormalizedFrequence = 0.4d;
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("has"));
		exptectedNormalizedFrequence = 0.2d;
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("hat"));
	}
	
	
	@Test
	public void testAdd() throws Exception {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		BagOfWords secondTestBag = new BagOfWords();
		secondTestBag.addTerms(words);
		
		testBag.add(secondTestBag);
		
		Integer expectedNrOfWords = 9;
		Double exptectedNormalizedFrequence = 1d;
		
		assertEquals(expectedNrOfWords, testBag.getNumberOfTerms());
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("has"));
		exptectedNormalizedFrequence = 0.5d;
		assertEquals(exptectedNormalizedFrequence, testBag.getNormalizedFrequency("child"));
	}

	@Test
	public void testHumanReadable() throws Exception {
		BagOfWords testBag = new BagOfWords();
		testBag.addTerms(words);
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getFrequency("he"));
		System.out.println(testBag.getFrequency("He"));
		System.out.println(testBag.getFrequency("has"));
		System.out.println(testBag.getNormalizedFrequency("He"));
		System.out.println(testBag.getNormalizedFrequency("has"));
		
		int freq = testBag.removeTerm("since");
		System.out.println(freq);
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getNormalizedFrequency("has"));
		freq = testBag.removeTerm("has");
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getNormalizedFrequency("has"));
		System.out.println(testBag.getNormalizedFrequency("he"));
		System.out.println(testBag.getNormalizedFrequency("He"));
		testBag.addTerm("he", 3);
		
		testBag.addTerm("blubber");
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getNormalizedFrequency("has"));
		System.out.println(testBag.getNormalizedFrequency("he"));
		System.out.println(testBag.getNormalizedFrequency("blubber"));
	}

	@Test
			public void testTermsOnlyInOtherBag() throws Exception {
				BagOfWords bagA = new BagOfWords();
				BagOfWords bagB = new BagOfWords();
				bagA.addTerm("one");
				bagA.addTerm("two");
				bagA.addTerm("three");
				bagB.addTerm("two");
				bagB.addTerm("three");
				bagB.addTerm("four");
				bagB.addTerm("five");
				Set<String> expected = new HashSet<>(Arrays.asList(new String[]{"four","five"}));
				assertEquals(expected, bagA.termsOnlyInOtherBag(bagB));
				
			}
	
}
