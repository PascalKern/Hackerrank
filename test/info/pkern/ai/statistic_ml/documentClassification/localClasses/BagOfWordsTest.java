package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class BagOfWordsTest {

	private static List<String> words = new ArrayList<>();
	private static BagOfWords testBag = new BagOfWords();
	
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

		testBag.addTerms(words);
	}

	@Test
	public void testHumanReadable() throws Exception {
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getTermFrequence("he"));
		System.out.println(testBag.getTermFrequence("He"));
		System.out.println(testBag.getTermFrequence("has"));
		System.out.println(testBag.getNormalizedTermFrequency("He"));
		System.out.println(testBag.getNormalizedTermFrequency("has"));
		
		int freq = testBag.removeTerm("since");
		System.out.println(freq);
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getNormalizedTermFrequency("has"));
		freq = testBag.removeTerm("has");
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getNormalizedTermFrequency("has"));
		System.out.println(testBag.getNormalizedTermFrequency("he"));
		System.out.println(testBag.getNormalizedTermFrequency("He"));
		
		testBag.addTerm("blubber");
		System.out.println(testBag.getTerms());
		System.out.println(testBag.getNormalizedTermFrequency("has"));
		System.out.println(testBag.getNormalizedTermFrequency("he"));
		System.out.println(testBag.getNormalizedTermFrequency("blubber"));
	}
	
}
