package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.List;

/*
 * TODO Remove extending. Replace Document with this class!
 */
public class DocumentClass {

	private BagOfWords classVocabulary = new BagOfWords();
	private Integer numberOfDocuments = 0;
	private final String name;
	
	
	public DocumentClass(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the probability for a given word in this document class.
	 * 
	 * @param word to check it's probability.
	 * @return value of probability.
	 */
	public Float probability(String word) {
		Integer vocabularySize = classVocabulary.getNumberOfWords();
		Integer sumN = 0;
		for (Integer frequency : classVocabulary.getFrequences()) {
			sumN += frequency;
		}
		Integer N = classVocabulary.getFrequenceOf(word);
		Float result = new Float(1 + N);
		result /= vocabularySize + sumN;
		return result;
	}
	
	public void addWordsToDocumentClass(BagOfWords bagOfWords) {
		classVocabulary.add(bagOfWords);
		numberOfDocuments++;
	}

	public void addWordWithoutFrequency(String word) {
		classVocabulary.addWordWithoutFrequency(word);
	}

	public Integer numberOfWords() {
		return classVocabulary.getNumberOfWords();
	}

	public List<String> getWords() {
		return classVocabulary.getWords();
	}

	public Integer getFrequenceOf(String word) {
		return classVocabulary.getFrequenceOf(word);
	}

	//	/**
	//	 * Sets the number of documents in this document class.
	//	 * 
	//	 * @param numberOfDocuments the number of documents.
	//	 */
	//	public void setNumberOfDocuments(Integer numberOfDocuments) {
	//		this.numberOfDocuments = numberOfDocuments;
	//	}
		
		
		/**
		 * Get the number of documents in this document class.
		 * 
		 * @return documents count.
		 */
		public Integer getNumberOfDocuments() {
			return numberOfDocuments;
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
		newDocumentClass.classVocabulary = classVocabulary;
		newDocumentClass.classVocabulary.add(otherDocumentClass.classVocabulary);
		newDocumentClass.numberOfDocuments = numberOfDocuments;
		newDocumentClass.numberOfDocuments += otherDocumentClass.numberOfDocuments;
		return newDocumentClass;
	}

	public boolean contains(String word) {
		return classVocabulary.getWords().contains(word);
	}

	public String getName() {
		return name;
	}
	
//	/**
//	 * Sets the number of documents in this document class.
//	 * 
//	 * @param numberOfDocuments the number of documents.
//	 */
//	public void setNumberOfDocuments(Integer numberOfDocuments) {
//		this.numberOfDocuments = numberOfDocuments;
//	}

}
