package info.pkern.ai.statistic_ml.documentClassification.localClasses;

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
