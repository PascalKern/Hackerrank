package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.tools.MapUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
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

//TODO Use the VectorMath where possible!
public class TextClassifier {

	//Maybe make changeable?
	private final Integer maxNumberAllowedTerms;
	private Map<String, DocumentClass> docClasses = new HashMap<>();

	//TODO Not nice to keep the terms multiple times! Once in each bag and Map.
	/*
	 * Optional to cache the calculated IDF if the calculation is to slow else! After
	 * training the classifier this can be calculated for faster classification of bags/documents. 
	 * BUT! This implements a state for this classifier which is not preferred. Especially not
	 * for to use this classifier in a multithreading environment!
	 */
	//NOTE: This is some kind of "cached"! 
	private Map<String, Double> inverseDocumentFrequency;
	//Could maybe added to the document class itself! Or better calculate it inside the document class with a 
	//given IDF Map. Also the vector length!
	private Map<String, Map<String, Double>> termWeightVectorsPerClass;
	private Map<String, Double> termWeightVectorLengthPerClass;
	
	private boolean trainingFinished = false;

	//Not the proper OOP way! This violates the SRP of the BagOfWords. Here not words/terms where counted instead doc classes!
	private  BagOfWords docClassFrequencies = new BagOfWords();
	
	//For littlest caching
	private List<Entry<String, Double>> lastClassificationResult;
	private int lastClassifiedBag;
	
	//Just experimental
	private boolean useNormalizedFrequences = false;

	
	public TextClassifier() {
		this(null);
	}
	
	public TextClassifier(Integer maxNumberOfTerms) {
		if (null == maxNumberOfTerms) {
			this.maxNumberAllowedTerms = Integer.MAX_VALUE;
		} else {
			this.maxNumberAllowedTerms = maxNumberOfTerms;
		}
	}
	
	//Deprecated due the not yet MultiDimBag implementation of add() within the DocumentClass! 
	@Deprecated
	public void train(DocumentClass docClass) {
		String className = docClass.getName();
		DocumentClass docClassEdit = docClasses.get(className);
		if (null == docClassEdit) {
			 docClasses.put(className, docClass);
			 docClassFrequencies.addTerms(docClass.getTerms());
		} else {
			docClassEdit.add(docClass);
			docClasses.put(className, docClassEdit);
			Set<String> newTerms = docClass.getTerms();
			newTerms.removeAll(docClassEdit.getTerms());
			if (! newTerms.isEmpty()) {
				docClassFrequencies.addTerms(newTerms);
			}
		}
		docClassEdit.setTrained(false);
		trainingFinished = false;
	}
	
	//Old when used with the newly deprecated train(DocumentClass) method!
//	public void train(BagOfWords bag, String docClassName) {
//		DocumentClass docClass = new DocumentClass(docClassName);
//		docClass.add(bag);
//		train(docClass);
//	}

	public void train(BagOfWords bag, String docClassName) {
		DocumentClass docClass = docClasses.get(docClassName);
		if (null == docClass) {
			docClass = new DocumentClass(docClassName);
			docClass.add(bag);
			docClasses.put(docClassName, docClass);
			docClassFrequencies.addTerms(docClass.getTerms());
		} else {
			docClassFrequencies.addTerms(bag.termsNotIn(docClass.getTerms()));
			docClass.add(bag);
			docClasses.put(docClassName, docClass);
		}
		docClass.setTrained(false);
		trainingFinished = false;
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
			
			classifications = new ArrayList<>(docClasses.size());
			for (Entry<String, Map<String, Double>> classWeights : termWeightVectorsPerClass.entrySet()) {
				Double dotProduct = calculateDotProduct(queryWeights, classWeights.getValue());
				
				Double classificationProbability = dotProduct / 
//						(Math.abs(vectorLength) * Math.abs(termWeightVectorLengthPerClass.get(classWeights.getKey())));
						vectorLength * termWeightVectorLengthPerClass.get(classWeights.getKey());
				
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
		Double docFraction = docClasses.size() / (1 + docFrequency.doubleValue());
		return Math.log10(docFraction);
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
		
		//TODO: Must also use the L2-Normed frequency from the document class?!
		//Ignore zeros because 0^2 still is 0. Also 0/x is still 0 so there is no need to include them!
		Double denominatorL2Norm = 0d;
		for (String term : terms.getTerms()) {
			denominatorL2Norm += Math.pow(docClassFrequencies.getFrequency(term), 2);
		}
		denominatorL2Norm = Math.sqrt(denominatorL2Norm);
		
		for (String term : terms.getTerms()) {
			Double idf = inverseDocumentFrequency.get(term);
			Double tf;
			if (useNormalizedFrequences) {
//				tf = terms.getNormalizedFrequency(term);
				tf = terms.getL2NormFromNormalizedFrequency(term);
			} else {
//				tf = terms.getFrequency(term);
				tf = terms.getL2NormFromFrequency(term);
			}
			//TODO Better only:
//			if (idf != null) { //OR much better above always set it to 0d when map returns null!
			if (tf > 0f && idf != null && idf > 0d) {
				//Calculate L2-Norm
				idf = idf / denominatorL2Norm;
				tfIdfs.put(term, tf * idf);
			}
		}
		return tfIdfs;
	}

	private Map<String, Map<String, Double>> calculateTermWeigthVectorPerClass() {
		Map<String, Map<String, Double>> weightedTermsPerClass = new HashMap<>();
		for (Entry<String, DocumentClass> currentClass : docClasses.entrySet()) {
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
		if (! maxNumberAllowedTerms.equals(Integer.MAX_VALUE)) {
			List<Entry<String, Double>> sortedList = MapUtil.sortByValuesDescending(inverseDocumentFrequency);
			Map<String, Double> reducedMap = new HashMap<>(maxNumberAllowedTerms);
			for (int i = 0; i < maxNumberAllowedTerms && i < sortedList.size(); i++) {
				Entry<String, Double> entry = sortedList.get(i);
				reducedMap.put(entry.getKey(), entry.getValue());
			}
			inverseDocumentFrequency = reducedMap;
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
	
	public void writeOutVectorsForVisualisation(Path location, String filename) {
		Path vertices = location.resolve(filename);
		Path lables = location.resolve("labels_" + filename);
		try (FileWriter fwLables = new FileWriter(lables.toFile()); FileWriter fwVertices = new FileWriter(vertices.toFile())) {
			List<String> terms = new ArrayList<>(docClassFrequencies.getTerms());
			//Header. Only right for doc class term weights!
			/*
			StringBuilder header = new StringBuilder();
			for (String term : terms) {
				header.append(String.format("%-16s", term + ",")).append("  ");
			}
			header.delete(header.lastIndexOf(","),header.length());
			fwVertices.append(header);
			fwVertices.append(System.lineSeparator());
			*/
			
//			int classCounter = 1;
			for (Entry<String, Map<String, Double>> currentClass : termWeightVectorsPerClass.entrySet()) {
				String className = currentClass.getKey();
				fwLables.append(className);
				Double currentValue;
				for (int i = 0, j = 1; i < terms.size(); i++, j++) {
					currentValue = currentClass.getValue().get(terms.get(i));
					if (null == currentValue) {
						fwVertices.append(String.format("%.10E", 0d)); 
					} else {
						fwVertices.append(String.format("%.10E", currentValue));
					}
					if (j < terms.size()) {
						fwVertices.append(", ");
					}
				}
				fwLables.append(System.lineSeparator());
				fwVertices.append(System.lineSeparator());
				docClasses.get(className).cleanUpTfPerBag(termWeightVectorsPerClass.get(className).keySet());
				for (List<Double> currentValues : docClasses.get(className).getAllTermFrequencyBags()) {
					fwLables.append(className).append(System.lineSeparator());
					writeClassOfTfAsRow(fwVertices, currentValues);
					fwVertices.append(System.lineSeparator());
//					completeMissingTermsAndNullValues(currentValues);
//					for (int i = 0; i < currentValues.size(); i++) {
//						fwVertices.append(String.format("%.10E", currentValues.get(i))); 
//						if (i < currentValues.size()) {
//							fwVertices.append(", ");
//						}
//					}
				}
//				if (classCounter < termWeightVectorsPerClass.size()) {
//					fwLables.append(System.lineSeparator());
//					fwVertices.append(System.lineSeparator());
//				}
//				classCounter++;
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not write lables or vertices file!", ex);
		}
	}
	
	private void writeClassOfTfAsRow(Writer writer, List<Double> values) {
		completeMissingTermsAndNullValues(values);
		for (int i = 0; i < values.size(); i++) {
			try {
				writer.append(String.format("%.10E", values.get(i)));
				if (i < values.size()) {
					writer.append(", ");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

	//TODO Does the order matter!?
	public void completeMissingTermsAndNullValues(List<Double> termFrequencyVector) {
		int nullIndex = -1;
		while (-1 != (nullIndex = termFrequencyVector.indexOf(null))) {
			termFrequencyVector.set(nullIndex, 0d);
		}
		int missingTermsCount = docClassFrequencies.getTerms().size() - termFrequencyVector.size();
		while (missingTermsCount > 0) {
			termFrequencyVector.add(0d);
			missingTermsCount--;
		}
		System.out.println(termFrequencyVector.size() + " -> " + docClassFrequencies.getTerms().size());
	}
}
