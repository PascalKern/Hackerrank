package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.commons.ListTypeConverter;
import info.pkern.hackerrank.commons.MapUtil;

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

public class TextClassifier {

	//Maybe make changeable?
	private final Integer maxNumberAllowedTerms;
	private Map<String, DocumentClass> docClasses = new HashMap<>();

	private Map<String, Double> inverseDocumentFrequency;
	private boolean trainingFinished = false;
	//Dictionary of the clasifier
	private  BagOfWords docClassFrequencies = new BagOfWords();
	
	private final boolean keepDetailsOfDocumentsInClasses;
	
	//For littlest caching
	private List<Entry<String, Double>> lastClassificationResult;
	//Better keep bag itselfe to use the equals() later!
	private int lastClassifiedBag;
	
	public TextClassifier() {
		this(null, false);
	}
	
	public TextClassifier(Integer maxNumberOfTerms) {
		this(maxNumberOfTerms, false);
	}

	public TextClassifier(Integer maxNumberOfTerms, boolean keepDetailsOfDocumentsInClasses) {
		this.keepDetailsOfDocumentsInClasses = keepDetailsOfDocumentsInClasses;
		this.maxNumberAllowedTerms = maxNumberOfTerms;
	}

	public void train(BagOfWords bag, String docClassName) {
		DocumentClass docClass = docClasses.get(docClassName);
		if (null == docClass) {
			docClass = new DocumentClass(docClassName, keepDetailsOfDocumentsInClasses);
			docClass.add(bag);
			docClasses.put(docClassName, docClass);
			docClassFrequencies.addTerms(docClass.getTerms());
		} else {
			docClassFrequencies.addTerms(bag.termsNotIn(docClass.getTerms()));
			docClass.add(bag);
			docClasses.put(docClassName, docClass);
		}
		trainingFinished = false;
	}
	
	//Due to issues with the keepDocClassDetails state of the classifier! 
	public void train(DocumentClass documentClass) {
		throw new RuntimeException("Not yet implemented!");
	}
	
	//TODO Create external analyzer component(s) to be used for this analyzer (create Interface). 
	// CosinSimilarity (VSM), KNN, ...
	// If more than one analyzer merge results to the best fitting probability!
	// Interface: getProbabilities(), analyze(). Use a set/list/map of DocumentClasses as input dataSet's.
	
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
							//Only used by BiggerExample - better result without StopWordFilter!
							//Result better about 20% false
//							VectorMath.normlizeVectorEuclideanNorm(docClass.getTermFrequencies().getFrequencies())
							
							//Used currently with best results
							//Result better about 10-20% false. Only with stopWordFilter and on real trainings data?!
							VectorMath.normlizeVectorEuclideanNorm(docClass.getWeightedFrequencies())
						);
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

	public Integer getDictionarySize() {
		if (null != maxNumberAllowedTerms) {
			return maxNumberAllowedTerms;
		} else {
			return docClassFrequencies.getNumberOfTerms();
		}
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
		if (docClasses.isEmpty()) {
			throw new IllegalStateException("Can not finish training! The classifier does not contain any document classes.");
		}
		//TODO Keep all frequencies - not reduced!
		inverseDocumentFrequency = calculateIDF();
		//TODO Keep separated
		reduceIdfToMaxNumberOfTerms();
		processDocumentClasses();
		trainingFinished = true;
	}

	private void processDocumentClasses() {
		for (DocumentClass docClass : docClasses.values()) {
			docClass.getWeightedFrequencies();
		}
	}
	
	private Map<String, Double> calculateIDF() {
		Map<String, Double> inverseDocumentFrequency = new HashMap<>(docClassFrequencies.getNumberOfTerms());
		for (String term : docClassFrequencies.getTerms()) {
			Integer docFrequency = docClassFrequencies.getFrequency(term);
			Double docFraction = docClasses.size() / (1 + docFrequency.doubleValue());
			inverseDocumentFrequency.put(term, Math.log10(docFraction));
		}
		return inverseDocumentFrequency;
	}

	//TODO Should also be used to filter the frequency vectors from the docClasses used for the probability calculation!
	private void reduceIdfToMaxNumberOfTerms() {
		if (null != maxNumberAllowedTerms) {
			int maxTerms = Math.min(maxNumberAllowedTerms, inverseDocumentFrequency.size());
			List<Entry<String, Double>> sortedList = MapUtil.sortAsListByValuesDescending(inverseDocumentFrequency);
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
	
	//TODO Should deep copy the doc classes list!
	public List<DocumentClass> getDocumenClasses() {
		List<DocumentClass> docClasses = new ArrayList<>();
		docClasses.addAll(this.docClasses.values());
		return docClasses;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TextClassifier [maxNumberAllowedTerms=");
		builder.append(maxNumberAllowedTerms);
		builder.append(", docClasses=");
		builder.append(docClasses.keySet());
		builder.append(", trainingFinished=");
		builder.append(trainingFinished);
		builder.append(", keepDetailsOfDocumentsInClasses=");
		builder.append(keepDetailsOfDocumentsInClasses);
		builder.append(",").append(System.lineSeparator());
		builder.append("docClassFrequencies=");
		builder.append(docClassFrequencies);
		builder.append(",").append(System.lineSeparator());
		builder.append("inverseDocumentFrequency=");
		builder.append(inverseDocumentFrequency);
		builder.append(",").append(System.lineSeparator());
		builder.append("lastClassificationResult=");
		builder.append(lastClassificationResult);
		builder.append(", lastClassifiedBag=");
		builder.append(lastClassifiedBag);
		builder.append("]");
		return builder.toString();
	}
}
