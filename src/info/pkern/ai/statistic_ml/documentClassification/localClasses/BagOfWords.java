package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementing a bag of words, words corresponding with their frequency of usages in 
 * a "document" for usage by the Document class, DocumentClass class and the Pool class.
 * @author pkern
 *
 */
public class BagOfWords {

	private Integer numberOfWords = 0;
	private Map<String, Integer> bagOfWords = new HashMap<>();
	
//	/**
//	 * Constructs a BagOfWords with the given words as initial content.
//	 * 
//	 * @param words to add to this bag.
//	 */
//	public BagOfWords(List<String> words) {
//		addWords(words);
//	}

	/**
	 * Method to join two BagOfWords. Adds the content of another bag
	 * to this bag.
	 * 
	 * @param anotherBag to join with this bag.
	 */
	public void add(BagOfWords anotherBag) {
		for (Entry<String, Integer> entry : anotherBag.bagOfWords.entrySet()) {
			addWord(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Adds a list of words to this BagOfWords.
	 * 
	 * @param words to add to this bag.
	 */
	public void addWords(List<String> words) {
		for (String word : words) {
			addWord(word);
		}
	}
	
	/**
	 * Adds a word to this BagOfWords.
	 * 
	 * @param word to add to this bag.
	 */
	public void addWord(String word) {
		addWord(word, 1);
	}
	
	private void addWord(String word, Integer frequency) {
		if (bagOfWords.containsKey(word)) {
			bagOfWords.put(word, bagOfWords.get(word) + frequency);
		} else {
			bagOfWords.put(word, frequency);
			numberOfWords++;
		}
	}
	
	/**
	 * Get the number of different words in this bag.
	 * 
	 * @return number of different words.
	 */
	public Integer size() {
		return bagOfWords.size();
	}
	
	/**
	 * Returning a list of the words contained in the object
	 * 
	 * @return a list of words in this bag. 
	 */
	public List<String> getWords() {
		List<String> words = new ArrayList<>(bagOfWords.size());
		words.addAll(bagOfWords.keySet());
		return words;
	}
	
	/**
	 * Returning the dictionary, containing the words (keys) with their frequency (values)
	 * 
	 * @return map as the dictionary
	 */
	public Map<String, Integer> getDictionaryOfThisBag() {
		return new HashMap<String, Integer>(bagOfWords);
	}
	
	/**
	 * Returning the frequency of a word
	 * 
	 * @param word to get it's frequency in this bag.
	 * @return the frequency of the given word.
	 */
	public Integer getFrequenceOf(String word) {
		if (bagOfWords.containsKey(word)) {
			return bagOfWords.get(word);
		} else {
			return 0;
		}
	}
	
}
