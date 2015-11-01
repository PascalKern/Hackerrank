package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.tools.MapUtil;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TextClassifier {

	//Maybe make changeable?
	private final Integer maxNumberOfTerms;
	private Map<String, DocumentClass> classes = new HashMap<>();

	//TODO Not nice to keep the terms multiple times! Once in each bag and Map.
	/*
	 * Optional to cache the calculated IDF if the calculation is to slow else! After
	 * training the classifier this can be calculated for faster classification of bags/documents. 
	 * BUT! This implements a state for this classifier which is not preferred. Especially not
	 * for to use this classifier in a multithreading environment!
	 */
	private Map<String, Double> inverseDocumentFrequency;
	//Could maybe added to the document class itself! Or better calculate it inside the document class with a 
	//given IDF Map. Also the vector length!
	private Map<String, Map<String, Double>> termWeightVectorsPerClass;
	private Map<String, Double> termWeightVectorLengthPerClass;
	private boolean trainingFinished = false;

	//Not the proper OOP way! This violates the SRP of the BagOfWords. Here not words/terms where counted instead doc classes!
	private  BagOfWords docClassFrequencies = new BagOfWords();
	
	private List<Entry<String, Double>> lastClassificationResult;
	private int lastClassifiedBag;
	
	private boolean useNormalizedFrequences = false;

	public TextClassifier() {
		this(null);
	}
	public TextClassifier(Integer maxNumberOfTerms) {
		if (null == maxNumberOfTerms) {
			this.maxNumberOfTerms = Integer.MAX_VALUE;
		} else {
			this.maxNumberOfTerms = maxNumberOfTerms;
		}
	}
	
	public void train(DocumentClass docClass) {
		String className = docClass.getName();
		DocumentClass existing = classes.get(className);
		if (null == existing) {
			 classes.put(className, docClass);
			 docClassFrequencies.addTerms(docClass.getTerms());
		} else {
			existing.add(docClass);
			classes.put(className, existing);
			Set<String> newTerms = docClass.getTerms();
			newTerms.removeAll(existing.getTerms());
			if (! newTerms.isEmpty()) {
				docClassFrequencies.addTerms(newTerms);
			}
		}
		trainingFinished = false;
	}
	
	public void train(BagOfWords bag, String docClassName) {
		DocumentClass docClass = new DocumentClass(docClassName);
		docClass.add(bag);
		train(docClass);
	}

	public Entry<String, Double> classify(BagOfWords queryBag) {
		checkReadyForClassification();
		List<Entry<String, Double>> classifications;
		if (lastClassifiedBag == queryBag.hashCode()) {
			classifications = lastClassificationResult;
		} else {
			classifications = getClassificationProbabilities(queryBag);
		}
		Collections.sort(classifications, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		return classifications.get(0);
	}
	
	public List<Entry<String, Double>> getClassificationProbabilities(BagOfWords queryBag) {
		checkReadyForClassification();
		List<Entry<String, Double>> classifications;
		
		if (lastClassifiedBag == queryBag.hashCode()) {
			classifications = new ArrayList<>(lastClassificationResult);
		} else {
			DocumentClass query = new DocumentClass("Query");
			query.add(queryBag);
			Map<String, Double> queryWeights = calculateTfIdf(query);
			Double vectorLength = calculateTermWeightVectorLength(queryWeights.values());
			
			classifications = new ArrayList<>(classes.size());
			for (Entry<String, Map<String, Double>> classWeights : termWeightVectorsPerClass.entrySet()) {
				Double dotProduct = calculateDotProduct(queryWeights, classWeights.getValue());
				
				Double classificationProbability = dotProduct / 
						(Math.abs(vectorLength) * Math.abs(termWeightVectorLengthPerClass.get(classWeights.getKey())));
				
				classifications.add(new SimpleEntry<>(classWeights.getKey(), classificationProbability));
			}
			lastClassificationResult = new ArrayList<>(classifications);
			lastClassifiedBag = queryBag.hashCode();
		}
		return classifications;
	}
	
	public boolean isUseNormalizedFrequences() {
		return useNormalizedFrequences;
	}

	public void setUseNormalizedFrequences(boolean useNormalizedFrequences) {
		this.useNormalizedFrequences = useNormalizedFrequences;
	}

	public Double getIDF(String term) {
		checkReadyForClassification();
		return getIDFOrZero(term);
	}

	//Calculate all data for performance classification. This again adds a state what is not realy good!
	public void finishTraining() {
		inverseDocumentFrequency = calculateIDF();
		reduceIdfToMaxNumberOfTerms();
		termWeightVectorsPerClass = calculateTermWeigthVectorPerClass();
		termWeightVectorLengthPerClass = calculateTermWeightVectorLengthPerClass();
		trainingFinished = true;
	}

	/**
	 * Use {@link TextClassifier#getIDF(String)} after a call to {@link TextClassifier#finishTraining()} instead!</br></br>
	 * 
	 * This method will become private soon!
	 * @param term to get its IDF (inverse document frequency) in this classifier.
	 * @return the IDF.
	 */
	@Deprecated
	public Double calculateIDF(String term) {
		Integer docFrequency = docClassFrequencies.getFrequency(term);
		if (0 == docFrequency) {
			return docFrequency.doubleValue();
		} else {
			Double docFraction = classes.size() / docFrequency.doubleValue();
			return Math.log10(docFraction);
		}
	}
	
	//Do not add zero calculated IDFs!? Then remove "&& idf > 0d" within calculateTfIdf(DocClass terms) bellow.
	private Map<String, Double> calculateIDF() {
		Map<String, Double> inverseDocumentFrequency = new HashMap<>(docClassFrequencies.getNumberOfTerms());
		for (String term : docClassFrequencies.getTerms()) {
			inverseDocumentFrequency.put(term, calculateIDF(term));
		}
		return inverseDocumentFrequency;
	}

	//Should return absolute already here? So no Math.abs needed in classification calculation.
	private Map<String, Double> calculateTfIdf(DocumentClass terms) {
		Map<String, Double> tfIdfs = new HashMap<>(); 
		for (String term : terms.getTerms()) {
			Double idf = inverseDocumentFrequency.get(term);
			Double tf;
			if (useNormalizedFrequences) {
				tf = terms.getNormalizedFrequency(term);
			} else {
				tf = terms.getFrequency(term);
			}
			if (tf > 0f && idf != null && idf > 0d) {
				tfIdfs.put(term, tf * idf);
			}
		}
		return tfIdfs;
	}

	private Map<String, Map<String, Double>> calculateTermWeigthVectorPerClass() {
		Map<String, Map<String, Double>> weightedTermsPerClass = new HashMap<>();
		for (Entry<String, DocumentClass> currentClass : classes.entrySet()) {
			weightedTermsPerClass.put(currentClass.getKey(), calculateTfIdf(currentClass.getValue()));
		}
		return weightedTermsPerClass;
	}
	
	private Double calculateTermWeightVectorLength(Collection<Double> weightVector) {
		Double vectorLength = 0d;
		for (Double weight : weightVector) {
			vectorLength += Math.pow(weight, 2);
		}
		return Math.sqrt(vectorLength);
	}

	private Map<String, Double> calculateTermWeightVectorLengthPerClass() {
		Map<String, Double> weightVectorLenghtsPerClass = new HashMap<>();
		for (Entry<String, Map<String, Double>> classVector : termWeightVectorsPerClass.entrySet()) {
			weightVectorLenghtsPerClass.put(classVector.getKey(), calculateTermWeightVectorLength(classVector.getValue().values()));
		}
		return weightVectorLenghtsPerClass;
	}

	private Double calculateDotProduct(Map<String, Double> queryWeights, Map<String, Double> weightsOfClass) {
		Double dotProduct = 0d;
		for (String term : queryWeights.keySet()) {
			if (weightsOfClass.containsKey(term)) {
				dotProduct += queryWeights.get(term) * weightsOfClass.get(term);
			}
		}
		return dotProduct;
	}
	
	//Could also remove 0 values here!?
	private void reduceIdfToMaxNumberOfTerms() {
		if (! maxNumberOfTerms.equals(Integer.MAX_VALUE)) {
			List<Entry<String, Double>> sortedList = MapUtil.sortByValuesDescending(inverseDocumentFrequency);
			Map<String, Double> reducedMap = new HashMap<>(maxNumberOfTerms);
			for (int i = 0; i < maxNumberOfTerms && i < sortedList.size(); i++) {
				Entry<String, Double> entry = sortedList.get(i);
				reducedMap.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private Double getIDFOrZero(String term) {
		Double idf = inverseDocumentFrequency.get(term);
		if (null == idf) {
			return 0d;
		} else {
			return idf;
		}
	}

	private void checkReadyForClassification() {
		if (! trainingFinished) {
			throw new IllegalStateException("The training of this classifier was not yet finished! Use finishTrainig() first.");
		}
	}
}
