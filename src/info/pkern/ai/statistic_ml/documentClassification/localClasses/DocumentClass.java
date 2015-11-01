package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DocumentClass {

	/*
	 * Must keep Floats! Create generic BagOfWords or extend BagOfWords to keep the total frequency and the 
	 * document count for one word -> BagOfWordWithTfIdf or so.
	 */
//	private BagOfWords classVocabulary = new BagOfWords();
	private Map<String, Float> wordsWeighted;
	
	private BagOfWords documentFrequencyPerWord = new BagOfWords();

	
	private BagOfWords totalFrequencyPerTerm = new BagOfWords();
	private BagOfWords documentFrequency = new BagOfWords();
	private Map<String, Double> inverseDocumentFrequency = new HashMap<>();
	
	private Integer totalNumberOfDocuments = 0;
	
	private final String name;
//	private final Integer maxWordsCount;
	private Integer maxWordsCount;

	private boolean isDirty = true;
	
	public DocumentClass(String name) {
	//		this(name, null);
			this.name = name;
		}

	private void calculateIdfIfDirty() {
		if (isDirty) {
			for (String term : documentFrequency.getTerms()) {
				inverseDocumentFrequency.put(term, calculateIdf(documentFrequency.getTermFrequence(term)));
			}
			isDirty = false;
		}
	}
	
	private Double calculateIdf(Integer frequency) {
		return Math.log10(totalNumberOfDocuments / frequency);
	}
	
	//Pool
	public Double calculateVectorLength(BagOfWords terms) {
		calculateIdfIfDirty();
		Double weightSum = 0d;
		for (Entry<String, Double> idfEntry : inverseDocumentFrequency.entrySet()) {
			weightSum += Math.pow(terms.getNormalizedTermFrequency(idfEntry.getKey()) * idfEntry.getValue(), 2);
		}
		return Math.sqrt(weightSum);
	}
	
	//Pool
	public Double calculateVectorDotProduct(BagOfWords terms) {
		calculateIdfIfDirty();
	}
	
//	public DocumentClass(String name, Integer maxWordsCount) {
//		this.name = name;
//		this.maxWordsCount = maxWordsCount;
//	}
	
	/**
	 * Returns the probability for a given word in this document class.
	 * 
	 * Naive-Bayes algorithm from python example:  ...
	 * 
	 * @param word to check it's probability.
	 * @return value of probability.
	 */
//	@Deprecated
//	public Float probability(String word) {
//		Integer vocabularySize = classVocabulary.getNumberOfWords();
//		Integer sumN = 0;
//		for (Integer frequency : classVocabulary.getFrequences()) {
//			sumN += frequency;
//		}
//		Integer N = classVocabulary.getFrequenceOf(word);
//		Float result = new Float(1 + N);
//		result /= vocabularySize + sumN;
//		return result;
//	}
	
	public void addDocumentToDocumentClass(BagOfWords bagOfWords) {
		totalFrequencyPerTerm.add(bagOfWords);
		documentFrequencyPerWord.addTerms(bagOfWords.getTerms());
		totalNumberOfDocuments++;
	}

//	public void addWordWithoutFrequency(String word) {
//		classVocabulary.addWordWithoutFrequency(word);
//	}
//
//	public Integer numberOfWords() {
//		return classVocabulary.getNumberOfWords();
//	}
//
//	public List<String> getWords() {
//		return classVocabulary.getWords();
//	}
//
//	public Integer getWeightOf(String word) {
//		return classVocabulary.getFrequenceOf(word);
//	}

	/**
	 * Only temporary method for testing purposes! Will be replaced with specific needed methods for the 
	 * calculation of the document in class probability. Ensure  
	 * @return
	 */
	public Map<String, Float> getWordsWeighted() {
		return wordsWeighted;
	}
	
	/*
	 * TODO Second method to update existing calculation when new Bag is added. Or something like this so
	 * that the state is always clean after adding new bag == document.
	 */
	private Map<String, Float> calculateWordWeights_TfIdf() {
		Map<String, Float> wordsWeighted = new HashMap<>(totalFrequencyPerTerm.getTermsCount());
	
		for (String word : totalFrequencyPerTerm.getTerms()) {
			int frequencyInAllDocuments = totalFrequencyPerTerm.getTermFrequence(word);
			int numberOfDocsWithWord = documentFrequencyPerWord.getTermFrequence(word);
			
		}
		throw new RuntimeException("Not yet implemented");
	}
	
	//Maybe not with return just edit the member or give the member as parameter and edit it.
	private Map<String, Float> reduceWeightedWordsToMaxWordsCount() {
		throw new RuntimeException("Not yet implemented!");
	}
	
	/**
	 * Get the number of documents in this document class.
	 * 
	 * @return documents count.
	 */
	public Integer getNumberOfDocuments() {
		return totalNumberOfDocuments;
	}

	/**
 	 * Method to join two DocumentClasses.
	 * 
	 * @param DocumentClass to join with this document class.
	 * @return new DocumentClass instance with the words of both document classes.
	 */
	@Deprecated
	public DocumentClass add(DocumentClass otherDocumentClass) {
		DocumentClass newDocumentClass = new DocumentClass(otherDocumentClass.name);
		newDocumentClass.wordsWeighted = wordsWeighted;
//		newDocumentClass.classWordWeights.add(otherDocumentClass.classWordWeights);
		newDocumentClass.wordsWeighted.putAll(otherDocumentClass.wordsWeighted);
		newDocumentClass.totalNumberOfDocuments = totalNumberOfDocuments;
		newDocumentClass.totalNumberOfDocuments += otherDocumentClass.totalNumberOfDocuments;
		return newDocumentClass;
	}

	public boolean contains(String word) {
		return wordsWeighted.keySet().contains(word);
	}

	public String getName() {
		return name;
	}

	/*
	 * Ether getSumOfWordFrequency -> classWordFrequency or getSumOfWordWeight -> clasVocabulary
	 */
//	@Deprecated
//	public Integer getSumOfFrequencies() {
//		return classVocabulary.getSumOfFrequencies();
//	}

//	@Deprecated
//	public void normalizeBag(List<String> words) {
//		classVocabulary.normalizeBag(words);
//	}

}
