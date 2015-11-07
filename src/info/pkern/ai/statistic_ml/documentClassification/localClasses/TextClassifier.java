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
	
	private boolean trainingFinished = false;

	//Not the proper OOP way! This violates the SRP of the BagOfWords. Here not words/terms where counted instead doc classes!
	private  BagOfWords docClassFrequencies = new BagOfWords();
	
	//For littlest caching
	private List<Entry<String, Double>> lastClassificationResult;
	private int lastClassifiedBag;
	
	//TODO Remove all not normalized Methods! Use only L2-Norm over all classes!!!
	@Deprecated
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
			
			query.weightFrequenciesWithIDF(inverseDocumentFrequency);
			
			classifications = new ArrayList<>(docClasses.size());
			
			for (DocumentClass docClass : docClasses.values()) {
				Double dotProduct = VectorMath.dotProduct(query.getTfIdfWeightedFrequencies(), docClass.getTfIdfWeightedFrequencies());
				Double classificationProbability = dotProduct /
						query.getWeightedEuclidianNorm() * docClass.getWeightedEuclidianNorm();
				classifications.add(new SimpleEntry<>(docClass.getName(), classificationProbability));
			}
			lastClassificationResult = new ArrayList<>(classifications);
			lastClassifiedBag = queryBag.hashCode();
		}
		return classifications;
	}
	
	@Deprecated
	public boolean isUseNormalizedFrequences() {
		return useNormalizedFrequences;
	}

	@Deprecated
	public void setUseNormalizedFrequences(boolean useNormalizedFrequences) {
		this.useNormalizedFrequences = useNormalizedFrequences;
	}

	@Deprecated
	public Double getIDF(String term) {
		checkReadyForClassification();
		return getIDFOrZero(term);
	}

	//Calculate all data for performance classification. This again adds a state what is not realy good!
	public void finishTraining() {
		inverseDocumentFrequency = calculateIDF();
		reduceIdfToMaxNumberOfTerms();
		processDocumentClasses();
		trainingFinished = true;
	}

	private void processDocumentClasses() {
		for (DocumentClass docClass : docClasses.values()) {
			docClass.weightFrequenciesWithIDF(inverseDocumentFrequency);
		}
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
			for (DocumentClass docClass : docClasses.values()) {
				String className = docClass.getName();
				fwLables.append(className.toUpperCase());
				Double currentValue;
				for (int i = 0, j = 1; i < terms.size(); i++, j++) {
					currentValue = docClass.getTfIdfWeightedFrequency(terms.get(i));
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
//				docClasses.get(className).cleanUpTfPerBag(termWeightVectorsPerClass.get(className).keySet());
				for (List<Double> currentValues : docClass.getAllTermFrequencyBags()) {
					fwLables.append(className.substring(0, 2)).append(System.lineSeparator());
					writeClassOfTfAsRow(fwVertices, currentValues);
					fwVertices.append(System.lineSeparator());
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not write lables or vertices file!", ex);
		}
	}
	
	//TODO Use only terms from the idf vector and only the weighted vectors in the classes.
	//As noted in the doc class clean the bags in the classes.
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
	}
}
