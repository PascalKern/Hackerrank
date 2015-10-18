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
	
	/**
	 * Method to join two BagOfWords.
	 * 
	 * @param bagOfWords to join with this bag.
	 * @return new BagOfWords instance with the words of both bags.
	 */
	public BagOfWords add(BagOfWords bagOfWords) {
		BagOfWords newBag = new BagOfWords();
		for (Entry<String, Integer> entry : bagOfWords.bagOfWords.entrySet()) {
			newBag.addWord(entry.getKey(), entry.getValue());
		}
		return newBag;
	}
	
	/**
	 * Adds a word to this BagOfWords
	 * 
	 * @param word to add to this BagOfWords
	 */
	public void addWord(String word) {
		addWord(word, 1);
	}
	
	private void addWord(String word, Integer frequency) {
		if (bagOfWords.containsKey(word)) {
			bagOfWords.put(word, bagOfWords.get(word) + frequency);
		} else {
			bagOfWords.put(word, frequency);
		}
		numberOfWords++;
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
