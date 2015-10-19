package info.pkern.ai.statistic_ml.documentClassification.localClasses;

/*
 * TODO Remove extending. Replace Document with this class!
 */
public class DocumentClass extends Document {

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
	public Integer probability(String word) {
		Integer vocabularySize = vocabularySize();
		Integer sumN = 0;
		for (Integer frequency : wordsAndFrequence.getDictionaryOfThisBag().values()) {
			sumN += frequency;
		}
		Integer N = getFrequenceOf(word);
		Integer result = 1 + N;
		result /= vocabularySize + sumN;
		return result;
	}
	
	public void addDocument(Document document) {
		wordsAndFrequence.add(document.getWordsAndFrequence());
		numberOfDocuments++;
	}

	/**
 	 * Method to join two DocumentClasses.
	 * 
	 * @param DocumentClass to join with this document class.
	 * @return new DocumentClass instance with the words of both document classes.
	 */
	public DocumentClass add(DocumentClass otherDocumentClass) {
		DocumentClass newDocumentClass = new DocumentClass(otherDocumentClass.name);
		newDocumentClass.wordsAndFrequence = wordsAndFrequence;
		newDocumentClass.wordsAndFrequence.add(otherDocumentClass.wordsAndFrequence);
		newDocumentClass.numberOfDocuments = numberOfDocuments;
		newDocumentClass.numberOfDocuments += otherDocumentClass.numberOfDocuments;
		return newDocumentClass;
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
}
