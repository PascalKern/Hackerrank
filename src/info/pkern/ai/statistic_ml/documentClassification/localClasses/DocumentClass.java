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
	
	private Integer totalNumberOfBags = 0;
	private BagOfWords bagFrequencies = new BagOfWords();
	private SimpleTableNamedColumnAdapter<Integer> tfPerBag = new SimpleTableNamedColumnAdapter<>(Integer.class);
	private SimpleTableNamedColumnAdapter<Double> tfPerBagNormalized;
	
	
	public DocumentClass(String name) {
		this.name = name;
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
		
		//Very expensive!!! Should create a subclass (ExtendedDocumentClass) which keeps this information only.
		//The text classifier then must have a switch to signal which DocClass should be used!
//		tfPerBag.extendTableColumns(bagOfWords.getTerms());
//		tfPerBag.addRow(bagOfWords.getFrequencies());
		isWeighted = false;
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
}
