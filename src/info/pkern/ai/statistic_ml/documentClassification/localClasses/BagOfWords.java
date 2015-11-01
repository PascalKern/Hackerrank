package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.event.AncestorEvent;

import org.omg.CORBA.FREE_MEM;

/**
 * Implementing a bag of words, words corresponding with their frequency of usages in 
 * a "document" for usage by the Document class, DocumentClass class and the Pool class.
 * @author pkern
 *
 */
/*
 * TODO How to write this class more generic? So that it can hold any number type as "value"!
 * For example it can hold Integers or Floats.
 * -> Separate any methods they "work" (ie. calculate new values) with the contained data
 * into a separate class. So that the bag is only a bag (srp principal!) - only a littel bit
 * more intelligent bag or map.
 */
public class BagOfWords {

	private Integer numberOfWords = 0;
	@Deprecated
	private Integer sumOfWordFrequencies = 0;
	private Integer maxWordFrequence = 0;
//	private Map<String, Integer> bagVocabulary = new HashMap<>();

	//Most probably no need for this complicated stuff! BagOfWords is not much more than a wrapped map! So the list 
	//of values is almost never needed and the access always iterates over keys or has one specific key!?
	private Map<String, Integer> wordIndexLookup = new HashMap<>();
	private List<Integer> wordFrequencies = new ArrayList<>();
	private List<Float> normalizedWordFrequencies = new ArrayList<>();
	
	private Map<String, Term> bagTerms = new HashMap<>();

	
	private boolean isNormilizationDirty = true;
	
	private int getEmptyIndexPosition() {
		int wfEmptyIndex = wordFrequencies.indexOf(null);
		if (wfEmptyIndex == normalizedWordFrequencies.indexOf(null)) {
			return wfEmptyIndex;
		} else {
			return -1;
		}
	}
	
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
		mergedBag.isNormilizationDirty = bagA.isNormilizationDirty;
		mergedBag.maxWordFrequence = bagA.maxWordFrequence;
		mergedBag.normalizedWordFrequencies = bagA.normalizedWordFrequencies;
		mergedBag.numberOfWords = bagA.numberOfWords;
		mergedBag.wordFrequencies = bagA.wordFrequencies;
		mergedBag.wordIndexLookup = bagA.wordIndexLookup;
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
//		for (Entry<String, Integer> entry : anotherBag.bagVocabulary.entrySet()) {
		for (Entry<String, Integer> entry : anotherBag.wordIndexLookup.entrySet()) {
//			addWord(entry.getKey(), entry.getValue());
			addTerm(entry.getKey(), wordFrequencies.get(entry.getValue()));
		}
//		numberOfWords += anotherBag.numberOfWords;
	}

	/**
	 * Adds a word to this BagOfWords.
	 * 
	 * @param term to add to this bag.
	 */
	public void addTerm(String term) {
		addTerm(term, 1);
	}

	/**
	 * Removes a word and it's frequency from this bag.</br></br>
	 * 
	 * <strong>Note</strong></br>
	 * The method returns 0 if the word was not contained by this bag!
	 * 
	 * @param term to be removed
	 * @return the frequency which was assigned to this word. 
	 */
	public Integer removeTerm(String term) {
		return removeTermFromBag(term).getValue();
	}
	
	/**
	 * Removes a list of words and it's frequency from this bag.</br></br>
	 * 
	 * <strong>Note</strong></br>
	 * The method returns a frequency of 0 if the word was not contained by this bag!
	 * 
	 * @param terms to be removed
	 * @return a list of {@link Entry}. Each with the removed word and its frequency. 
	 */
	public List<Entry<String, Integer>> removeTerm(List<String> terms) {
		List<Entry<String, Integer>> entries = new ArrayList<>();
		for (String term : terms) {
			entries.add(removeTermFromBag(term));
		}
		return entries;
	}
	
	private Entry<String, Integer> removeTermFromBag(String word) {
		if (wordIndexLookup.containsKey(word)) {
			int wordIndex = wordIndexLookup.remove(word);
			Integer frequency = wordFrequencies.set(wordIndex, null);
			normalizedWordFrequencies.set(wordIndex, null);
			Entry<String, Integer> entry = new SimpleEntry<>(word, frequency); 
//			if (frequency.equals(maxWordFrequence)) {
				isNormilizationDirty = true;
				maxWordFrequence = findMaxTermFrequency();
//			}
			sumOfWordFrequencies -= frequency;
			numberOfWords--;
			return entry;
		} else {
			return new SimpleEntry<String, Integer>(word, 0);
		}
	}

	//	private Entry<String, Integer> removeWordFromBag(String word) {
//		if (bagVocabulary.containsKey(word)) {
//			Integer frequency = bagVocabulary.remove(word); //Not in if because in some circumstances the value itself may be null!
//			Entry<String, Integer> entry = new SimpleEntry<>(word, frequency); 
//			if (frequency.equals(maxWordFrequence)) {
//				maxWordFrequence = findMaxFrequency();
//			}
//			sumOfWordFrequencies -= frequency;
//			numberOfWords--;
//			return entry;
//		} else {
//			return new SimpleEntry<String, Integer>(word, -1);
//		}
//	}
	
	private Integer findMaxTermFrequency() {
		Integer maxFrequency = 0;
		for (Integer frequency : wordFrequencies) {
			if (null != frequency) {
				maxFrequency = Math.max(maxFrequency, frequency);
			}
		}
		return maxFrequency;
	}

	public void addTerm(String term, Integer frequency) {
		int newFrequency = frequency;
		Integer wordIndex;
		if (null == (wordIndex = wordIndexLookup.get(term))) {
			wordIndex = getEmptyIndexPosition();
			if (-1 == wordIndex) {
				wordFrequencies.add(frequency);
				wordIndex = wordFrequencies.size() -1;
			} else {
				wordFrequencies.set(wordIndex, frequency);
			}
			wordIndexLookup.put(term, wordIndex);
			numberOfWords++;
		} else {
			wordFrequencies.set(wordIndex, wordFrequencies.get(wordIndex) + frequency);
			newFrequency = wordFrequencies.get(wordIndex);
		}
		if (newFrequency > maxWordFrequence) {
			maxWordFrequence = newFrequency;
//			isNormilizationDirty = true;
		} 
		//Not nice. Better calculate the new normFrequency direct. But then must check if the freq can be added or inserted!
		isNormilizationDirty = true;
		sumOfWordFrequencies += frequency;
	}

	/**
	 * Adds a list of words to this BagOfWords. Each word will be added with
	 * a frequency of <strong>one</strong>. Added as new or summed up by one with the existing word.
	 * 
	 * @param words to add to this bag.
	 */
	public void addTerms(List<String> words) {
		for (String word : words) {
			addTerm(word);
		}
	}

	public void addTermWithoutFrequency(String word) {
		addTerm(word, 0);
	}
	
	/**
	 * Get the number of different words in this bag.
	 * 
	 * @return number of different words.
	 */
	public Integer getTermsCount() {
//		return bagVocabulary.size();
		return wordIndexLookup.size();
	}
	
	//TODO Return the Set!
	/**
	 * Returning a list of the words contained in the object. The order of the words is meaningless.
	 * 
	 * @return a list of words in this bag. 
	 */
	public List<String> getTerms() {
		List<String> words = new ArrayList<>(getTermsCount());
//		words.addAll(bagVocabulary.keySet());
		words.addAll(wordIndexLookup.keySet());
		return words;
	}
	
	@Deprecated
	public List<Integer> getFrequences() {
//		List<Integer> frequences = new ArrayList<>(bagVocabulary.size());
//		frequences.addAll(bagVocabulary.values());
//		return frequences;
		return wordFrequencies;
	}
	
	
	@Deprecated
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
	@Deprecated
	public void normalizeBag(List<String> words) {
		for (String word : words) {
			addTermWithoutFrequency(word);
		}
	}
	
	/**
	 * Returning the frequency of a word
	 * 
	 * @param term to get it's frequency in this bag.
	 * @return the frequency of the given word.
	 */
	public Integer getTermFrequence(String term) {
//		if (bagVocabulary.containsKey(word)) {
//			return bagVocabulary.get(word);
		Integer wordIndex;
		if (null != (wordIndex = wordIndexLookup.get(term))) {
			return wordFrequencies.get(wordIndex);
		} else {
			return 0;
		}
	}

	public Float getNormalizedTermFrequency(String term) {
		Integer indexOfFrequency;
		if (null == (indexOfFrequency = wordIndexLookup.get(term))) {
			return 0f;
		} else {
			if (isNormilizationDirty) {
				calculateNormalizedFrequencies();
			}
			return normalizedWordFrequencies.get(indexOfFrequency);
		}
	}
	
	private void calculateNormalizedFrequencies() {
		normalizedWordFrequencies = Arrays.asList(new Float[wordFrequencies.size()]);
		for (Entry<String, Integer> wordIndex : wordIndexLookup.entrySet()) {
			float noralized = wordFrequencies.get(wordIndex.getValue()) / (float)maxWordFrequence;
			normalizedWordFrequencies.set(wordIndex.getValue(), noralized);
		}
		isNormilizationDirty = false;
	}

	/**
	 * Returning the dictionary, containing the words (keys) with their frequency (values)
	 * 
	 * @return map as the dictionary
	 */
	@Deprecated
	public Map<String, Integer> getVocabulary() {
//		return new HashMap<String, Integer>(bagVocabulary);
		throw new RuntimeException("Method not enimore implemented!");
	}
	
	private class Term {
		private Integer frequency;
		private Float normalizedFrequency;
	}
}
