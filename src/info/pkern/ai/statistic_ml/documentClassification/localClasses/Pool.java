package info.pkern.ai.statistic_ml.documentClassification.localClasses;

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
	private BagOfWords vocabulary = new BagOfWords();
	
	/**
	 * The number of times all different words of a document class appear in a class
	 * 
	 * @param docClassName name of the document class.
	 * @return sum
	 */
	public Integer countOfWordsInClass(String docClassName) {
		checkDocumentClassExists(docClassName);
		Integer sum = 0;
		for (String word : documentClasses.get(docClassName).getWords()) {
			sum += vocabulary.getFrequenceOf(word);
		}
		return sum;
	}
	
	/**
	 * 
	 */
	public void learn(Document document, String docClassName) {
		DocumentClass docClass;
		if (null == (docClass = documentClasses.get(docClassName))) {
			docClass = new DocumentClass(docClassName);
			documentClasses.put(docClassName, docClass);
		}
		docClass.addDocument(document);
		vocabulary.add(document.getWordsAndFrequence());
	}


	private void checkDocumentClassExists(String docClassName) {
		if (null == docClassName) {
			throw new IllegalArgumentException("NULL is not a valide document class name!");
		}
		if (!documentClasses.keySet().contains(docClassName)) {
			throw new NoSuchElementException("No document class with name: "+docClassName+" in this pool!");
		}
	}
	
	/**
	 * Calculates the probability for a class dclass given a document doc
	 * 
	 * @param document
	 * @param documentClass
	 * @return
	 */
	public Float probability(Document document, String docClassName) {
		checkDocumentClassExists(docClassName);
		DocumentClass dclass = documentClasses.get(docClassName);
		Integer sumDClass = countOfWordsInClass(docClassName);
		Float prob = new Float(0);
		
		for (Entry<String, DocumentClass> docClassEntry_J : documentClasses.entrySet()) {
			Integer sumJ = countOfWordsInClass(docClassEntry_J.getKey());
			Float prod = new Float(1);
			for (String word : document.getWords()) {
				Integer wordFrequence_DocClass = 1 + dclass.getFrequenceOf(word);
				Integer wordFrequence_J = docClassEntry_J.getValue().getFrequenceOf(word);
				Float r = new Float(wordFrequence_J * sumDClass / (wordFrequence_DocClass * sumJ));
				prod *= r;
				prob += prod * new Float(docClassEntry_J.getValue().getNumberOfDocuments() / dclass.getNumberOfDocuments());
			}
		}
		if (prob != 0) {
			return new Float(1 / prob);
		} else {
			return new Float(-1);
		}
	}

	
	public Map<String, Float> getProbabilityForAllDocumentClasses(Document document) {
		Map<String, Float> resultMap = new HashMap<>(documentClasses.size());
		for (String docClassName : documentClasses.keySet()) {
			float probabilty = probability(document, docClassName);
			resultMap.put(docClassName, probabilty);
		}
		return resultMap;
	}
	
	
	/**
	 * 
	 * @param documentName
	 * @return
	 */
	private List<String> documentIntersectionWithClasses(String documentName) {
		throw new RuntimeException("Not yet implemented");
	}
}
