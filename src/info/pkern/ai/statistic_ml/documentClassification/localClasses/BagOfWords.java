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
	private Integer sumOfWordFrequencies = 0;
	private Map<String, Integer> bagVocabulary = new HashMap<>();
	
//	/**
//	 * Constructs a BagOfWords with the given words as initial content.
//	 * 
//	 * @param words to add to this bag.
//	 */
//	public BagOfWords(List<String> words) {
//		addWords(words);
//	}

	public static BagOfWords mergeBags(BagOfWords bagA, BagOfWords bagB) {
		BagOfWords mergedBag = new BagOfWords();
		mergedBag.numberOfWords = bagA.numberOfWords;
		mergedBag.bagVocabulary = bagA.bagVocabulary;
		mergedBag.add(bagB);
		return mergedBag;
	}
	
	/**
	 * Method to join two BagOfWords. Adds the content of another bag
	 * to this bag.
	 * 
	 * @param anotherBag to join with this bag.
	 */
	public void add(BagOfWords anotherBag) {
		for (Entry<String, Integer> entry : anotherBag.bagVocabulary.entrySet()) {
			addWord(entry.getKey(), entry.getValue());
		}
//		numberOfWords += anotherBag.numberOfWords;
	}

	/**
	 * Adds a word to this BagOfWords.
	 * 
	 * @param word to add to this bag.
	 */
	public void addWord(String word) {
		addWord(word, 1);
	}

	public void addWord(String word, Integer frequency) {
		if (bagVocabulary.containsKey(word)) {
			bagVocabulary.put(word, bagVocabulary.get(word) + frequency);
		} else {
			bagVocabulary.put(word, frequency);
			numberOfWords++;
		}
		sumOfWordFrequencies += frequency;
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

	public void addWordWithoutFrequency(String word) {
		addWord(word, 0);
	}
	
	/**
	 * Get the number of different words in this bag.
	 * 
	 * @return number of different words.
	 */
	public Integer getNumberOfWords() {
		return bagVocabulary.size();
	}
	
	/**
	 * Returning a list of the words contained in the object
	 * 
	 * @return a list of words in this bag. 
	 */
	public List<String> getWords() {
		List<String> words = new ArrayList<>(bagVocabulary.size());
		words.addAll(bagVocabulary.keySet());
		return words;
	}
	
	public List<Integer> getFrequences() {
		List<Integer> frequences = new ArrayList<>(bagVocabulary.size());
		frequences.addAll(bagVocabulary.values());
		return frequences;
	}
	
	public Integer getSumOfFrequencies() {
		return sumOfWordFrequencies;
	}
	
	/**
	 * Normalizes this bag to a given set of words. After the normalization
	 * every word within the list will be in this bag.<br/>
	 * Any word from the list which was not part of the bag before will be
	 * added with a frequency of 0!
	 * 
	 * @param words list of words to normalize with.
	 */
	public void normalizeBag(List<String> words) {
		for (String word : words) {
			addWordWithoutFrequency(word);
		}
	}
	
	/**
	 * Returning the frequency of a word
	 * 
	 * @param word to get it's frequency in this bag.
	 * @return the frequency of the given word.
	 */
	public Integer getFrequenceOf(String word) {
		if (bagVocabulary.containsKey(word)) {
			return bagVocabulary.get(word);
		} else {
			return 0;
		}
	}

	/**
	 * Returning the dictionary, containing the words (keys) with their frequency (values)
	 * 
	 * @return map as the dictionary
	 */
	@Deprecated
	public Map<String, Integer> getVocabulary() {
		return new HashMap<String, Integer>(bagVocabulary);
	}
	
}
