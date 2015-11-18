package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.tools.ListTypeConverter;
import info.pkern.hackerrank.tools.MapUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.print.attribute.standard.MediaSize.Other;

public class DocumentClass {

	private final String name;
	private BagOfWords termFrequencies = new BagOfWords();
	private boolean trained = false;
	private Map<String, Double> weightedFrequencies = new HashMap<>();
	
	private Integer totalNumberOfBags = 0;
	private BagOfWords bagFrequencies = new BagOfWords();
	private SimpleTableNamedColumnAdapter<Integer> tfPerBag = new SimpleTableNamedColumnAdapter<>(Integer.class);
	private SimpleTableNamedColumnAdapter<Double> tfPerBagNormalized;
	
	
	public DocumentClass(String name) {
		this.name = name;
	}
	
	public boolean isTrained() {
		return trained;
	}

	public void setTrained(boolean trained) {
		this.trained = trained;
	}

	public void add(BagOfWords bagOfWords) {
		termFrequencies.add(bagOfWords);
		totalNumberOfBags++;
		bagFrequencies.addTerms(bagOfWords.getTerms());
		
		tfPerBag.extendTableColumns(bagOfWords.getTerms());
//		tfPerBag.addRow(VectorMath.normlizeVectorEuclideanNorm(bagOfWords.getFrequencies()));
		tfPerBag.addRow(bagOfWords.getFrequencies());
		trained = false;
		tfPerBagNormalized = null;
	}

	public SimpleTableNamedColumnAdapter<Integer> getTermFrequenciesOfAllBags() {
		return tfPerBag.copy();
	}
	
	public SimpleTableNamedColumnAdapter<Double> getNormalizedTermFrequenciesOfAllBags() {
		if (null == tfPerBagNormalized) {
			tfPerBagNormalized = new SimpleTableNamedColumnAdapter<Double>(Double.class);
			tfPerBagNormalized.extendTableColumns(tfPerBag.getHeader());
			for (int rowIndex = 0; rowIndex < tfPerBag.getRowsCount(); rowIndex++) {
				Map<String,Double> rowNormalized = VectorMath.normlizeVectorEuclideanNorm(tfPerBag.getRow(rowIndex));
				tfPerBagNormalized.addRow(rowNormalized);
			}
		}
		return tfPerBagNormalized;
	}
	
	public void add(DocumentClass anotherClass) {
		throw new RuntimeException("Not yet implemented!");
	}
	
	/**
	 * Gets a copy of the terms in this document class. Changes to this set are <strong>not</strong> reflected</br>
	 * to this document class!
	 *
	 * @return the terms in this document class.
	 */
	public Set<String> getTerms() {
		return new HashSet<>(termFrequencies.getTerms());
	}

	public boolean contains(String term) {
		return termFrequencies.contains(term);
	}

	public String getName() {
		return name;
	}

	public Map<String, Double> getTfIdfWeightedFrequencies() {
		checkIsTrained();
		return new HashMap<>(weightedFrequencies);
	}
	
	public Double getTfIdfWeightedFrequency(String term) {
		checkIsTrained();
		return weightedFrequencies.get(term);
	}
	
	public void calculateWeightedFrequenciesWithIDF(Map<String, Double> inverseDocumentFrequency) {
		if (!trained) {
			for (String term : inverseDocumentFrequency.keySet()) {
				Double idf = inverseDocumentFrequency.get(term);
				Double tfidf = termFrequencies.getFrequency(term) * ((null == idf)?0d:idf);
				weightedFrequencies.put(term, tfidf);
			}
//			weightedFrequencies = VectorMath.normlizeVectorEuclideanNorm(weightedFrequencies);
			trained = true;
		}
	}
	
	private void checkIsTrained() {
		if (!trained) {
			throw new IllegalStateException("Document class not yet trained with a IDF vector! Use weigthFrequencies() first.");
		}
	}
}
