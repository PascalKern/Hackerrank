package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.tools.ListTypeConverter;
import info.pkern.hackerrank.tools.MapUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
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
//	@Deprecated
//	private boolean useNormalizedFrequences = false;

	
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
	

	public void train(DocumentClass docClass) {
		throw new RuntimeException("Not yet implemented!");
	}
	

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
			classifications = new ArrayList<>(docClasses.size());
			
			for (DocumentClass docClass : docClasses.values()) {

				Double classificationProbability = null;
				try {
					classificationProbability = VectorMath.cosineSimilarityEuclideanNorm(
							VectorMath.normlizeVectorEuclideanNorm(queryBag.getFrequencies()), 
							VectorMath.normlizeVectorEuclideanNorm(docClass.getTfIdfWeightedFrequencies()));
				} catch (InvalidObjectException e) {
					e.printStackTrace();
				}
				classifications.add(new SimpleEntry<>(docClass.getName(), classificationProbability));
			}
			lastClassificationResult = new ArrayList<>(classifications);
			lastClassifiedBag = queryBag.hashCode();
		}
		return classifications;
	}
	
	public Integer getMaxNumberAllowedTerms() {
		return maxNumberAllowedTerms;
	}

	public Double getIDFOrZero(String term) {
		checkReadyForClassification();
		Double idf = inverseDocumentFrequency.get(term);
		if (null == idf) {
			return 0d;
		} else {
			return idf;
		}
	}

	//Calculate all data for performance classification. This again adds a state what is not really good!
	public void finishTraining() {
		inverseDocumentFrequency = calculateIDF();
		reduceIdfToMaxNumberOfTerms();
		processDocumentClasses();
		trainingFinished = true;
	}

	private void processDocumentClasses() {
		for (DocumentClass docClass : docClasses.values()) {
			docClass.calculateWeightedFrequenciesWithIDF(inverseDocumentFrequency);
		}
	}

	private Map<String, Double> calculateIDF() {
		Map<String, Double> inverseDocumentFrequency = new HashMap<>(docClassFrequencies.getNumberOfTerms());
		for (String term : docClassFrequencies.getTerms()) {
			inverseDocumentFrequency.put(term, calculateIDF(term));
		}
		return inverseDocumentFrequency;
	}

	private Double calculateIDF(String term) {
		Integer docFrequency = docClassFrequencies.getFrequency(term);
		Double docFraction = docClasses.size() / (1 + docFrequency.doubleValue());
		return Math.log10(docFraction);
	}

	private void reduceIdfToMaxNumberOfTerms() {
		if (! maxNumberAllowedTerms.equals(Integer.MAX_VALUE)) {
			int maxTerms = Math.min(maxNumberAllowedTerms, inverseDocumentFrequency.size());
			List<Entry<String, Double>> sortedList = MapUtil.sortByValuesDescending(inverseDocumentFrequency);
			Map<String, Double> reducedMap = new HashMap<>(maxTerms);
			for (Entry<String, Double> entry : sortedList.subList(0, maxTerms)) {
				reducedMap.put(entry.getKey(), entry.getValue());
			}
			inverseDocumentFrequency = reducedMap;
		}
	}
	
	private void checkReadyForClassification() {
		if (! trainingFinished) {
			throw new IllegalStateException("The training of this classifier was not yet finished! Use finishTrainig() first.");
		}
	}
	
	
	public List<DocumentClass> getDocumenClasses() {
		List<DocumentClass> docClasses = new ArrayList<>();
		docClasses.addAll(this.docClasses.values());
		return docClasses;
	}
	
	//TODO Ordering matters?
	public double[] normalizeBagWithVocabularyForVisualization(BagOfWords bag) {
		List<Integer> bagVector = new ArrayList<>();
		for (String term : inverseDocumentFrequency.keySet()) {
			Integer value;
			if (null != (value = bag.getFrequency(term))) {
				bagVector.add(value);
			} else {
				bagVector.add(0);
			}
		}
		List<Double> bagVectorNorm = VectorMath.normlizeVectorEuclideanNorm(bagVector);
		return ListTypeConverter.toPrimitiveDouble(bagVectorNorm);
	}

	
	
	
	
	
	
	
	
	
	@Deprecated
	public void writeOutVectorsForVisualisation(Path location, String filename) {
		Path vertices = location.resolve(filename);
		Path lables = location.resolve("labels_" + filename);
		try (FileWriter fwLables = new FileWriter(lables.toFile()); FileWriter fwVertices = new FileWriter(vertices.toFile())) {
//		List<String> terms = new ArrayList<>(docClassFrequencies.getTerms());
			List<String> terms = new ArrayList<>(inverseDocumentFrequency.keySet());
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
				SimpleTable<Double> simpleTable = docClass.getNormalizedTermFrequenciesOfAllBags().getSimpleTable();
				fwVertices.append(simpleTable.dumpTable(10));
				int rows = simpleTable.getRowsCount();
				while (rows > 0) {
					fwLables.append(className.substring(0, 2)).append(System.lineSeparator());
					rows--;
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Could not write lables or vertices file!", ex);
		}
	}
}
