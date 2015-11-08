package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.print.attribute.standard.MediaSize.Other;

/*
 * TODO Check the improvement of the normalized term frequency! Better not used? 
 */
public class DocumentClass {

	private final String name;
	private BagOfWords termFrequencies = new BagOfWords();
	private boolean trained = false;
	private Map<String, Double> weightedFrequencies = new HashMap<>();
	
	private Integer totalNumberOfBags = 0;
	
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
//	private Map<String,Integer> indices = new HashMap<>();
	private List<String> indices = new ArrayList<>();
	private List<List<Double>> tfPerBag = new ArrayList<>();
	
//	private Double denominatorL2Norm;
//	private Double denominatorL2NormNormalized;
	
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
//		denominatorL2Norm = null;
//		denominatorL2NormNormalized = null;
		
		//MultiDimBag
		if (indices.isEmpty()) {
			addNewTerms(bagOfWords.getTerms());
		} else {
			Set<String> newTermsFromBag = bagOfWords.termsNotIn(indices);
			addNewTerms(newTermsFromBag);
		}
		List<Double> newBagFrequencies = populateZeroedList(indices.size());
		for (String term : bagOfWords.getTerms()) {
			int index = indices.indexOf(term);
			newBagFrequencies.set(index, bagOfWords.getL2NormalizedFrequency(term));
		}
		tfPerBag.add(newBagFrequencies);
	}
	
	//MultiDimBag
	private List<Double> populateZeroedList(int elementsCount) {
		//Does not work! Will throw a UnsuportedOperationException due the list is write through to the backed up array!!!
//		List<Double> newBagFrequencies = Arrays.asList(new Double[indices.size()]);
		List<Double> list = new ArrayList<>(elementsCount);
		while (0 < elementsCount) {
			list.add(0d);
			elementsCount--;
		}
		return list;
	}
	
	//MultiDimBag
	private void addNewTerms(Set<String> newTerms) {
		for (String term : newTerms) {
			int index;
			if (-1 != (index = indices.indexOf(null))) {
				indices.set(index, term);
			} else {
				indices.add(term);
			}
		}
	}

	//MultiDimBag
	public List<List<Double>> getAllTermFrequencyBags() {
		List<List<Double>> listCopy = new ArrayList<List<Double>>();
		listCopy.addAll(tfPerBag);
		return listCopy;
	}

	//MultiDimBag
	public void cleanUpTfPerBag(Collection<String> termsToKeep) {
		Set<String> termsToRemove = new HashSet<>(indices);
		termsToRemove.removeAll(termsToKeep);
		for (String term : termsToRemove) {
			int index = indices.indexOf(term);
			for (List<Double> bagTerms : tfPerBag) {
				bagTerms.set(index, null);
			}
		}
		for (List<Double> bagTerms : tfPerBag) {
			for (int i = bagTerms.size(); i > 0; i--) {
				bagTerms.remove(i);
			}
		}
	}
	
	
	/**
	 * Not (yet) implemented for the MultiDimBag functionality!
	 * @param anotherClass
	 */
	@Deprecated
	public void add(DocumentClass anotherClass) {
		if (! name.equals(anotherClass.getName())) {
			throw new UnsupportedOperationException("Can only add (merge) classes with the same name! [names: this="
					+ name + ", other=" + anotherClass.getName() + "]");
		}
		totalNumberOfBags += anotherClass.totalNumberOfBags;
		termFrequencies.add(anotherClass.termFrequencies);
		bagFrequencies.add(anotherClass.bagFrequencies);
	}
	
//	public Double getFrequency(String term) {
//		return termFrequencies.getFrequency(term) * getBagFraction(term);
//	}
//
//	public Double getNormalizedFrequency(String term) {
//		return termFrequencies.getFrequencyNormalizedWithMaxTermFrequency(term) * getBagFraction(term);
//	}
//
//	private Double getBagFraction(String term) {
//		return bagFrequencies.getFrequency(term) / totalNumberOfBags.doubleValue();
//	}
//	
//	public Double getL2NormFromFrequency(String term) {
//		reCalculateDenominators();
//		return termFrequencies.getFrequency(term) / denominatorL2Norm;
//	}
//
//	@Deprecated
//	public Double getL2NormFromNormalizedFrequency(String term) {
//		reCalculateDenominators();
//		return termFrequencies.getL2NormalizedFrequency(term) / denominatorL2NormNormalized;
//	}
//	
//	private void reCalculateDenominators() {
//		if (null == denominatorL2Norm) {
//			denominatorL2Norm = VectorMath.euclidianLength(termFrequencies.getFrequencies());
//			List<Double> normlizedFrequencies = new ArrayList<>();
//			for (String currentTerm : termFrequencies.getTerms()) {
//				normlizedFrequencies.add(termFrequencies.getFrequencyNormalizedWithMaxTermFrequency(currentTerm));
//			}
//			denominatorL2NormNormalized = VectorMath.euclidianLength(normlizedFrequencies);
//		}
//	}
	
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
				/* Idea to increase the "weight" of terms which appears in all documents when calculated for probability comparsion. 
				Integer bagFrequency = bagFrequencies.getFrequency(term);
				Double tf;
				if (null == bagFrequency || 0 == bagFrequency) {
					tf = 0d;
				} else {
					tf = termFrequencies.getFrequency(term) * new Double(totalNumberOfBags / bagFrequency);
				}
				Double tfidf = tf * inverseDocumentFrequency.get(term);
				*/
				Double tfidf = termFrequencies.getFrequency(term) * inverseDocumentFrequency.get(term);
				weightedFrequencies.put(term, tfidf);
				//TODO Should also unify the MultiDimBag stuff?!
			}
			weightedFrequencies = VectorMath.normlizeVectorEuclideanNorm(weightedFrequencies);
			trained = true;
		}
	}
	
	//Vector lenght
	public Double getWeightedEuclidianLength() {
		checkIsTrained();
		return VectorMath.lengthEuclideanNorm(weightedFrequencies.values());
	}
	
	private void checkIsTrained() {
		if (!trained) {
			throw new IllegalStateException("Document class not yet trained with a IDF vector! Use weigthFrequencies() first.");
		}
	}
}
