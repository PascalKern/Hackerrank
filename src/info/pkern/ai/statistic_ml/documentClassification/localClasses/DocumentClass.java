package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.Set;

import javax.print.attribute.standard.MediaSize.Other;

public class DocumentClass {

	private final String name;
	private Integer totalNumberOfBags = 0;
	private BagOfWords terms = new BagOfWords();
	
	//TODO Not nice to keep the terms twice! Once in each bag.
	//Not the proper OOP way! This violates the SRP of the BagOfWords. Here not words/terms where counted instead bags!
	private BagOfWords bagFrequencies = new BagOfWords();
	
	public DocumentClass(String name) {
		this.name = name;
	}
	
	public void add(BagOfWords bagOfWords) {
		terms.add(bagOfWords);
		totalNumberOfBags++;
		bagFrequencies.addTerms(bagOfWords.getTerms());
	}
	
	public void add(DocumentClass anotherClass) {
		if (! name.equals(anotherClass.getName())) {
			throw new UnsupportedOperationException("Can only add (merge) classes with the same name! [names: this="
					+ name + ", other=" + anotherClass.getName() + "]");
		}
		totalNumberOfBags += anotherClass.totalNumberOfBags;
		terms.add(anotherClass.terms);
		bagFrequencies.add(anotherClass.bagFrequencies);
	}
	
	public Double getFrequency(String term) {
		Double bagFraction = bagFrequencies.getFrequency(term) / totalNumberOfBags.doubleValue();
		return bagFraction * terms.getFrequency(term);
	}

	public Double getNormalizedFrequency(String term) {
		Double bagFraction = bagFrequencies.getFrequency(term) / totalNumberOfBags.doubleValue();
		return bagFraction * terms.getNormalizedFrequency(term);
	}

	/**
	 * Gets a copy of the terms in this document class. Changes to this set are <strong>not</strong> reflected</br>
	 * to this document class!
	 *
	 * @return the terms in this document class.
	 */
	public Set<String> getTerms() {
		return terms.getTerms();
	}

	public boolean contains(String term) {
		return terms.contains(term);
	}

	public String getName() {
		return name;
	}
}
