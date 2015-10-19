package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.stylesheets.DocumentStyle;

/**
 * Used both for learning (training) documents and for testing documents. The 
 * optional parameter learn has to be set to True, if a classificator should be 
 * trained. If it is a test document learn has to be set to False.
 * 
 * @author pkern
 *
 */
/*
 * TODO Remove! It's now just a wrapper for the BagOfWords! Or redesign to a
 * return type of the DocumentTokanizer.
 */
@Deprecated
public class Document {

	protected BagOfWords wordsAndFrequence = new BagOfWords();

//	/**
//	 * Adds a list of words to this document.
//	 * 
//	 * @param words to be added to the documents vocabulary.
//	 * @param learn true to train the words.
//	 */
//	public void read(List<String> words, boolean learn) {
//		for (String word : words) {
//			wordsAndFrequence.addWord(word);
//			if (learn) {
//				Document.vocabulary.addWord(word);
//			}
//		}
//	}
	
	public BagOfWords getWordsAndFrequence() {
		return wordsAndFrequence;
	}
	
	/**
	 * Adds a list of words to this document. The words will
	 * <strong>NOT</strong> be trained. To train the words
	 * use {@link Document#read(List, boolean)} to train! 
	 * 
	 * @param words to be added to the documents vocabulary.
	 */
	public void read(List<String> words) {
		wordsAndFrequence = new BagOfWords();
		wordsAndFrequence.addWords(words);
	}
	
	/**
	 * Method to join two Document.
	 * 
	 * @param Document to join with this document.
	 * @return new Document instance with the words of both documents.
	 */
	public Document add(Document otherDocument) {
		Document newDocument = new Document();
		newDocument.wordsAndFrequence = wordsAndFrequence;
		newDocument.wordsAndFrequence.add(otherDocument.wordsAndFrequence);
		return newDocument;
	}
	
	/**
	 * Get the length of this vocabulary.
	 * 
	 * @return the size (length) of this vocabulary.
	 */
	public Integer vocabularySize() {
		return wordsAndFrequence.size();
	}
	
	/**
	 * Get the words (key) and their frequency (values) of this document.
	 * 
	 * @return words mapped to frequency.
	 */
	public Map<String, Integer> getWordsAndFrequency() {
		return new HashMap<>(wordsAndFrequence.getDictionaryOfThisBag());
	}

	/**
	 * Lists all words in this document.
	 * 
	 * @return words in this document.
	 */
	public List<String> getWords() {
		return wordsAndFrequence.getWords();
	}
	
	/**
	 * Get the frequency of a word in this document.
	 * 
	 * @param word to get it's frequency.
	 * @return the frequency.
	 */
	public Integer getFrequenceOf(String word) {
		return wordsAndFrequence.getFrequenceOf(word);
	}
	
	/**
	 * Produces a list of all words which appears in this and the other document.
	 * 
	 * @param otherDocument document to find the intersecting words.
	 * @return list of words appearing in both documents.
	 */
	public List<String> intersection(Document otherDocument) {
		List<String> intersectingWords = new ArrayList<>();
		List<String> otherWords = otherDocument.getWords();
		for (String word : wordsAndFrequence.getWords()) {
			if (otherWords.contains(word)) {
				intersectingWords.add(word);
			}
		}
		return intersectingWords;
	}
}
