package info.pkern.sandbox.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import info.pkern.ai.statistic_ml.documentClassification.localClasses.BagOfWords;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.TextClassifier;
import info.pkern.hackerrank.tools.MapUtil;

public class TextClassifierLikeExample {

	@Test
	public void oneToOneFromExample() {
		
		BagOfWords n1 = new BagOfWords();
		n1.addTerms(Arrays.asList(new String[]{"Shipment","of","gold","damaged","in","a","fire"}));
		BagOfWords n2 = new BagOfWords();
		n2.addTerms(Arrays.asList(new String[]{"Delivery","of","silver","arrived","in","a","silver","truck"}));
		BagOfWords n3 = new BagOfWords();
		n3.addTerms(Arrays.asList(new String[]{"Shipment","of","gold","arrived","in","a","truck"}));
		
		TextClassifier textClassifier = new TextClassifier(null);
		textClassifier.train(n1, "N1");
		textClassifier.train(n2, "N2");
		textClassifier.train(n3, "N3");
		
		Double delta = 0.00001d;
		Double expected = 0.0d;
		Double actual = textClassifier.calculateIDF("a");
		System.out.println(String.format("IDF of %-10s: %.5f", "a", actual));
		expected = 0.17609d;
		actual = textClassifier.calculateIDF("arrived");
		assertEquals(expected, actual, delta);
		System.out.println(String.format("IDF of %-10s: %.5f", "arrived", actual));
		expected = 0.47712d;
		actual = textClassifier.calculateIDF("damaged");
		assertEquals(expected, actual, delta);
		System.out.println(String.format("IDF of %-10s: %.5f", "damaged", actual));
		assertEquals(expected, actual, delta);
	}
	
	@Test
	public void fromExampleWithLittleClassifierTest() {
		
		BagOfWords n1 = new BagOfWords();
		n1.addTerms(Arrays.asList(new String[]{"Shipment","of","gold","damaged","in","a","fire"}));
		BagOfWords n2a = new BagOfWords();
		n2a.addTerms(Arrays.asList(new String[]{"arrived","in","a","silver","truck"}));
		BagOfWords n2b = new BagOfWords();
		n2b.addTerms(Arrays.asList(new String[]{"Delivery","of","silver","arrived"}));
		BagOfWords n3 = new BagOfWords();
		n3.addTerms(Arrays.asList(new String[]{"Shipment","of","gold","arrived","in","a","truck"}));
		
		TextClassifier textClassifier = new TextClassifier(null);
		textClassifier.train(n1, "N1");
		textClassifier.train(n2a, "N2");
		textClassifier.train(n2b, "N2");
		textClassifier.train(n3, "N3");
		
		Double delta = 0.00001d;
		Double expected = 0.0d;
		Double actual = textClassifier.calculateIDF("a");
		System.out.println(String.format("IDF of %-10s: %.5f", "a", actual));
		expected = 0.17609d;
		actual = textClassifier.calculateIDF("arrived");
		assertEquals(expected, actual, delta);
		System.out.println(String.format("IDF of %-10s: %.5f", "arrived", actual));
		expected = 0.47712d;
		actual = textClassifier.calculateIDF("damaged");
		assertEquals(expected, actual, delta);
		System.out.println(String.format("IDF of %-10s: %.5f", "damaged", actual));
		assertEquals(expected, actual, delta);
	}

	@Test
	public void fromExampleWithLittleMoreClassifierTest() {
		
		BagOfWords n1 = new BagOfWords();
		n1.addTerms(Arrays.asList(new String[]{"Shipment","of","gold","damaged","in","a","fire"}));
		BagOfWords n2a = new BagOfWords();
		n2a.addTerms(Arrays.asList(new String[]{"arrived","in","a","silver","truck"}));
		BagOfWords n2b = new BagOfWords();
		n2b.addTerms(Arrays.asList(new String[]{"Delivery","of","silver"}));
		BagOfWords n3 = new BagOfWords();
		n3.addTerms(Arrays.asList(new String[]{"Shipment","of","gold","arrived","in","a","truck"}));
		
		TextClassifier textClassifier = new TextClassifier(null);
		textClassifier.train(n1, "N1");
		textClassifier.train(n2a, "N2");
		textClassifier.train(n2b, "N2");
		textClassifier.train(n3, "N3");
		
		try {
			textClassifier.getIDF("a");
			fail();
		} catch (Exception ex) {
			String exptected = "The training of this classifier was not yet finished! Use finishTrainig() first.";
			assertEquals(exptected, ex.getMessage());
		}
		
		textClassifier.finishTraining();
		
		Double delta = 0.00001d;
		Double expected = 0.0d;
		Double actual = textClassifier.calculateIDF("a");
		System.out.println(String.format("IDF of %-10s: %.5f", "a", actual));
		expected = 0.17609d;
		actual = textClassifier.calculateIDF("arrived");
		assertEquals(expected, actual, delta);
		System.out.println(String.format("IDF of %-10s: %.5f", "arrived", actual));
		expected = 0.47712d;
		actual = textClassifier.calculateIDF("damaged");
		assertEquals(expected, actual, delta);
		System.out.println(String.format("IDF of %-10s: %.5f", "damaged", actual));
		assertEquals(expected, actual, delta);
	}
	
	@Test
	public void fromExampleClassifierTest() {
		
		BagOfWords n1 = new BagOfWords();
		n1.addTerms(Arrays.asList(new String[]{"shipment","of","gold","damaged","in","a","fire"}));
		BagOfWords n2 = new BagOfWords();
		n2.addTerms(Arrays.asList(new String[]{"delivery","of","silver","arrived","in","a","silver","truck"}));
		//Not working because then the content of N2 will be treated as within two BagOfWords. This affects the later result.
//		n2.addTerms(Arrays.asList(new String[]{"arrived","in","a","silver","truck"}));
//		BagOfWords n2b = new BagOfWords();
//		n2b.addTerms(Arrays.asList(new String[]{"delivery","of","silver"}));
		BagOfWords n3 = new BagOfWords();
		n3.addTerms(Arrays.asList(new String[]{"shipment","of","gold","arrived","in","a","truck"}));
		
		TextClassifier textClassifier = new TextClassifier(null);
		textClassifier.train(n1, "N1");
		textClassifier.train(n2, "N2");
//		textClassifier.train(n2b, "N2");
		textClassifier.train(n3, "N3");
		
		try {
			textClassifier.getIDF("a");
			fail();
		} catch (Exception ex) {
			String exptected = "The training of this classifier was not yet finished! Use finishTrainig() first.";
			assertEquals(exptected, ex.getMessage());
		}
		
		textClassifier.finishTraining();
		
		BagOfWords q = new BagOfWords();
		q.addTerms(Arrays.asList(new String[]{"gold","silver","truck"}));
		
		List<Entry<String, Double>> probabilities = textClassifier.getClassificationProbabilities(q);
		
		MapUtil.sortEntryListByValueDescending(probabilities);
		System.out.println(probabilities);
		
		Double delta = 0.0002d;
		Double expected = 0.8246d;
		assertEquals(probabilities.get(0).getKey(), "N2");
		assertEquals(expected, probabilities.get(0).getValue(),delta);
		expected = 0.3271d;
		assertEquals(probabilities.get(1).getKey(), "N3");
		assertEquals(expected, probabilities.get(1).getValue(), delta);
		expected = 0.0801d;
		assertEquals(probabilities.get(2).getKey(), "N1");
		assertEquals(expected, probabilities.get(2).getValue(), delta);
		
		System.out.println(textClassifier.classify(q));
	}
	
	
}
