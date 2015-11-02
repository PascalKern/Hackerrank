package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.MediaSize.Other;

/*
 * TODO Check the improvement of the normalized term frequency! Better not used? 
 */
public class DocumentClass {

	private final String name;
	private Integer totalNumberOfBags = 0;
	private BagOfWords termFrequencies = new BagOfWords();
	
	//TODO Not nice to keep the terms twice! Once in each bag.
	//Not the proper OOP way! This violates the SRP of the BagOfWords. Here not words/terms where counted instead bags!
	private BagOfWords bagFrequencies = new BagOfWords();
	
	/*
	 * Preparation to keep / calculate a idf vector for each added document (BOW). Mostly needed to visualize later.
	 * Almost a Matrix where each row is a document and each column the frequency of the term at this index. The longer
	 * this document class lives the more indices will be added. So every new row must always be initialized with the
	 * currently number of terms (ie: Arrays.asList(new Double[termFrequencies.getTerms().size()]) with zero for every
	 * element. When the idf is calculated afterwards nonexisting elements in a row are treated as zero anyway.
	 *  
	 * Could also use a MashMap for each row with the index as key.
	 */
	private Map<String, Integer> termIndices = new HashMap<>();
	private List<List<Double>> idfPerDocument = new ArrayList<>();
	
	private Double denominatorL2Norm;
	
	public DocumentClass(String name) {
		this.name = name;
	}
	
	public void add(BagOfWords bagOfWords) {
		termFrequencies.add(bagOfWords);
		totalNumberOfBags++;
		bagFrequencies.addTerms(bagOfWords.getTerms());
		denominatorL2Norm = null;
	}
	
	public void add(DocumentClass anotherClass) {
		if (! name.equals(anotherClass.getName())) {
			throw new UnsupportedOperationException("Can only add (merge) classes with the same name! [names: this="
					+ name + ", other=" + anotherClass.getName() + "]");
		}
		totalNumberOfBags += anotherClass.totalNumberOfBags;
		termFrequencies.add(anotherClass.termFrequencies);
		bagFrequencies.add(anotherClass.bagFrequencies);
	}
	
	public Double getFrequency(String term) {
		return termFrequencies.getFrequency(term) * getBagFraction(term);
	}

	public Double getNormalizedFrequency(String term) {
		return termFrequencies.getNormalizedFrequency(term) * getBagFraction(term);
	}

	private Double getBagFraction(String term) {
		return bagFrequencies.getFrequency(term) / totalNumberOfBags.doubleValue();
	}
	
	public Double getL2NormFromFrequency(String term) {
		reCalculateDenominatorL2Norm();
		return termFrequencies.getFrequency(term) / denominatorL2Norm;
	}

	@Deprecated
	public Double getL2NormFromNormalizedFrequency(String term) {
		reCalculateDenominatorL2Norm();
		return termFrequencies.getNormalizedFrequency(term) / denominatorL2Norm;
	}
	
	//Ignore zeros because 0^2 still is 0. Also 0/x is still 0 so there is no need to include them!
	private void reCalculateDenominatorL2Norm() {
		if (null == denominatorL2Norm) {
			Double pow2Sum = 0d;
			for (String term : termFrequencies.getTerms()) {
				pow2Sum += Math.pow(termFrequencies.getFrequency(term), 2);
			}
			denominatorL2Norm = Math.sqrt(pow2Sum);
		}
	}
	
	/**
	 * Gets a copy of the terms in this document class. Changes to this set are <strong>not</strong> reflected</br>
	 * to this document class!
	 *
	 * @return the terms in this document class.
	 */
	public Set<String> getTerms() {
		return termFrequencies.getTerms();
	}

	public boolean contains(String term) {
		return termFrequencies.contains(term);
	}

	public String getName() {
		return name;
	}
}
