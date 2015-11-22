package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DocumentClass {

	private final String name;
	private BagOfWords termFrequencies = new BagOfWords();
	private boolean isWeighted = false;
	
	private Map<String, Double> weightedFrequencies = new HashMap<>();
	private Map<String, Double> internalWeightedFrequencies = new HashMap<>();
	
	private Integer totalNumberOfBags = 0;
	private BagOfWords bagFrequencies = new BagOfWords();
	private final DocumentClassDetails documentClassDetails;
	
	
	public DocumentClass(String name) {
		this(name, false);
	}

	public DocumentClass(String name, boolean includeDocClassDetails) {
		this.name = name;
		if (includeDocClassDetails) {
			this.documentClassDetails = new DocumentClassDetails(this);
		} else {
			documentClassDetails = null;
		}
	}
	
	public boolean isWeighted() {
		return isWeighted;
	}

	public void setWeighted(boolean trained) {
		this.isWeighted = trained;
	}

	public void add(BagOfWords bagOfWords) {
		termFrequencies.add(bagOfWords);
		totalNumberOfBags++;
		bagFrequencies.addTerms(bagOfWords.getTerms());
		
		updateDocClassDetails(bagOfWords);
		
		isWeighted = false;
		internalWeightedFrequencies = null;
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

	public BagOfWords getTermFrequencies() {
		BagOfWords bagCopy = new BagOfWords();
		bagCopy.add(termFrequencies);
		return bagCopy;
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
		if (!isWeighted) {
			for (String term : inverseDocumentFrequency.keySet()) {
				Double idf = inverseDocumentFrequency.get(term);
				Double tfidf = termFrequencies.getFrequency(term) * ((null == idf)?0d:idf);
				weightedFrequencies.put(term, tfidf);
			}
//			weightedFrequencies = VectorMath.normlizeVectorEuclideanNorm(weightedFrequencies);
			isWeighted = true;
		}
	}
	
	private void checkIsTrained() {
		if (!isWeighted) {
			throw new IllegalStateException("Document class not yet trained with a IDF vector! Use weigthFrequencies() first.");
		}
	}
	
	public Map<String, Double> getInternalWeightedFrequencies() {
		if (null == internalWeightedFrequencies || internalWeightedFrequencies.isEmpty()) {
			internalWeightedFrequencies = weightInternal();
		}
		return internalWeightedFrequencies;
	}
	
	private Map<String, Double> weightInternal() {
		Map<String, Double> internalWeightedFrequencies = new HashMap<>();
		Map<String, Double> idfs = calculateIDF();
		for (String term : termFrequencies.getTerms()) {
			Double idf = idfs.get(term);
			Integer tf = termFrequencies.getFrequency(term);
			internalWeightedFrequencies.put(term, tf * idf);
		}
		return internalWeightedFrequencies;
	}
	
	private Map<String, Double> calculateIDF() {
		Map<String, Double> inverseDocumentFrequency = new HashMap<>(bagFrequencies.getNumberOfTerms());
		for (String term : bagFrequencies.getTerms()) {
			inverseDocumentFrequency.put(term, calculateIDF(term));
		}
		return inverseDocumentFrequency;
	}

	private Double calculateIDF(String term) {
		Integer docFrequency = bagFrequencies.getFrequency(term);
		Double docFraction = totalNumberOfBags / (1 + docFrequency.doubleValue());
		return Math.log10(docFraction);
	}

	private void updateDocClassDetails(BagOfWords bagOfWords) {
		if (null != documentClassDetails) {
			documentClassDetails.add(bagOfWords);
		}
	}
	
	public boolean hasDocClassDetials() {
		return null != documentClassDetails;
	}
	
	public DocumentClassDetails getDocClassDetails() {
		return documentClassDetails;
	}
}
