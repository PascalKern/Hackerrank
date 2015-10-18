package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pool which is used to train the document classes and supplies 
 * classification methods.
 * 
 * @author pkern
 *
 */
public class Pool {

	private Map<String, DocumentClass> documentClasses = new HashMap<>();
	private BagOfWords vocabulary;
	
	/**
	 * The number of times all different words of a document class appear in a class
	 * 
	 * @param documentClass name of the document class.
	 * @return sum
	 */
	public Integer countOfWordsInClass(String documentClass) {
		
	}
	
	/**
	 * 
	 */
	public learn() {
		
	}
	
	/**
	 * Calculates the probability for a class dclass given a document doc
	 * 
	 * @param document
	 * @param documentClass
	 * @return
	 */
	public Integer probability(Document document, DocumentClass documentClass) {
		
	}
	
	/**
	 * 
	 * @param documentName
	 * @return
	 */
	private List<String> documentIntersectionWithClasses(String documentName) {
		
	}
}
