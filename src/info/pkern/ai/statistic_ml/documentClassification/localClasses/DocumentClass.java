package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//TODO Introduce a max term count value? Specially for the weighted frequencies.
public class DocumentClass {

	private final String name;
	private BagOfWords termFrequencies = new BagOfWords();
	
	private Map<String, Double> weightedFrequencies = new HashMap<>();
	
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

	public void add(BagOfWords bagOfWords) {
		termFrequencies.add(bagOfWords);
		totalNumberOfBags++;
		bagFrequencies.addTerms(bagOfWords.getTerms());
		
		updateDocClassDetails(bagOfWords);

		weightedFrequencies = null;
	}

	//TODO Find a way to keep weighting rolling up to date?!
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
			Integer docFrequency = bagFrequencies.getFrequency(term);
			Double docFraction = totalNumberOfBags / (1 + docFrequency.doubleValue());
			inverseDocumentFrequency.put(term, Math.log10(docFraction));
		}
		return inverseDocumentFrequency;
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

	public Map<String, Double> getWeightedFrequencies() {
		if (null == weightedFrequencies || weightedFrequencies.isEmpty()) {
			weightedFrequencies = weightInternal();
		}
		return weightedFrequencies;
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
