package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * A pool which is used to train the document classes and supplies 
 * classification methods.
 * 
 * @author pkern
 *
 */
public class Pool {

	private Map<String, DocumentClass> documentClasses = new HashMap<>();
	private BagOfWords poolVocabulary = new BagOfWords();
	
	/**
	 * The number of times all different words of a document class appear in a class
	 * 
	 * @param docClassName name of the document class.
	 * @return sum
	 */
	public Integer countOfWordsInClass(String docClassName) {
		checkDocumentClassExists(docClassName);
		Integer sum = 0;
		
		DocumentClass docClass = documentClasses.get(docClassName);
		for (String word : poolVocabulary.getWords()) {
			if (docClass.contains(word)) {
				sum += docClass.getFrequenceOf(word); 
			}
		}
		return sum;
	}

	public Integer countOfWordsInClassOld(String docClassName) {
		checkDocumentClassExists(docClassName);
		Integer sum = 0;
		for (String word : documentClasses.get(docClassName).getWords()) {
			sum += poolVocabulary.getFrequenceOf(word);
		}
		return sum;
	}
	
	/**
	 * 
	 */
	public void learn(BagOfWords bag, String docClassName) {
		DocumentClass docClass;
		if (null == (docClass = documentClasses.get(docClassName))) {
			docClass = new DocumentClass(docClassName);
			documentClasses.put(docClassName, docClass);
		}
		int oldSizeClass = docClass.numberOfWords();
		docClass.addWordsToDocumentClass(bag);
		int oldSize = poolVocabulary.getNumberOfWords();
		poolVocabulary.add(bag);
//		System.out.println("Learned bag! Class: " + docClassName + " Bag size: " + bag.numberOfWords() 
//				+ ", class vocabulary old: " + oldSizeClass + ", new: " + docClass.numberOfWords()
//				+ ", pool vocabulary old: " + oldSize + ", new: " + poolVocabulary.numberOfWords());
	}


	private void checkDocumentClassExists(String docClassName) {
		if (null == docClassName) {
			throw new IllegalArgumentException("NULL is not a valide document class name!");
		}
		if (!documentClasses.keySet().contains(docClassName)) {
			throw new NoSuchElementException("No document class with name: "+docClassName+" in this pool!");
		}
	}
	
	public Collection<String> getClasses() {
		return documentClasses.keySet();
	}
	
	public Map<String, Integer> getClassesWithDocumentCount() {
		Map<String, Integer> map = new HashMap<>();
		for (String docClass : getClasses()) {
			map.put(docClass, documentClasses.get(docClass).getNumberOfDocuments());
		}
		return map;
	}
	
	public Integer getNumberOfWords() {
		return poolVocabulary.getNumberOfWords();
	}

	/**
	 * Calculates the probability for a class dclass given a document doc
	 * 
	 * @param document
	 * @param documentClass
	 * @return
	 */
	public Float probability(BagOfWords bag, String docClassName) {
		checkDocumentClassExists(docClassName);
		
		Long startTime = System.nanoTime();
		
		DocumentClass dClass = documentClasses.get(docClassName);
		Integer sum_dClass = countOfWordsInClass(docClassName);
		
		Float probability = new Float(0);
		
		for (DocumentClass current_dClass : documentClasses.values()) {
			Integer sum_current_dClass = countOfWordsInClass(current_dClass.getName());				//sum_j
			Float prod = new Float(1);																//prod
			for (String word : bag.getWords()) {
				Integer wordFrequence_dClass = 1 + dClass.getFrequenceOf(word);						//wf_dclass
				Integer wordFrequence_current_dClass = 1 + current_dClass.getFrequenceOf(word);		//wf
				Float r = new Float(wordFrequence_current_dClass + sum_current_dClass / (wordFrequence_dClass * sum_dClass));	//R
				prod *= r;
				probability += prod * current_dClass.getNumberOfDocuments() / dClass.getNumberOfDocuments();
			}
		}
		
		System.out.println((System.nanoTime() - startTime) / Math.pow(10, 6) + "ms Duration for probability calculation for dClass: " + docClassName + ", with bag of " + bag.getNumberOfWords() + " words!");
		
		if (probability != 0) {
			return 1 / probability;
		} else {
			return new Float(-1f);
		}
		
//		for (Entry<String, DocumentClass> docClassEntry_J : documentClasses.entrySet()) {
//			Integer sumJ = countOfWordsInClass(docClassEntry_J.getKey());
//			Float prod = new Float(1);
//			for (String word : bag.getWords()) {
//				Integer wordFrequence_DocClass = 1 + dClass.getFrequenceOf(word);
//				Integer wordFrequence_J = docClassEntry_J.getValue().getFrequenceOf(word);
//				Float r = new Float(wordFrequence_J * sum_dClass / (wordFrequence_DocClass * sumJ));
//				prod *= r;
//				probability += prod * new Float(docClassEntry_J.getValue().getNumberOfDocuments() / dClass.getNumberOfDocuments());
//			}
//		}
//		if (probability != 0) {
//			return new Float(1f / probability);
//		} else {
//			return new Float(-1f);
//		}
	}

	
	public Map<String, Float> probability(BagOfWords bag) {
		Map<String, Float> probabilityMap = new HashMap<>(documentClasses.size());
		
		Long startTime = System.nanoTime();
		
		for (String current_dClassName : documentClasses.keySet()) {
			float probabilty = probability(bag, current_dClassName);
			probabilityMap.put(current_dClassName, probabilty);
		}
		
		System.out.println((System.nanoTime() - startTime) / Math.pow(10, 6) + "ms Duration for probability map calculation with bag of " + bag.getNumberOfWords() + " words!");
		
		return probabilityMap;
	}
	
	
//	/**
//	 * Produces a list of all words which appears in this and the other document.
//	 * 
//	 * @param otherDocument document to find the intersecting words.
//	 * @return list of words appearing in both documents.
//	 */
//	private List<String> documentIntersectionWithClasses(String documentName) {
//		List<String> intersectingWords = new ArrayList<>();
//		List<String> otherWords = otherDocument.getWords();
//		for (String word : wordsAndFrequence.getWords()) {
//			if (otherWords.contains(word)) {
//				intersectingWords.add(word);
//			}
//		}
//		return intersectingWords;
//	}
}
