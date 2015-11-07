package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VectorMath {

	/**
	 * Calculates the "length" of a vector with the L2-Norm. The so called</br>
	 * euclidian norm.</br></br>
	 * 
	 * <code>||v|| = sqrt(v1^2 + v2^2 + ... vn^2)</code>
	 * @param vector to calculate the norm.
	 * @return the L2-Norm value.
	 */
	public static <T extends Number> Double euclidianNorm(Collection<T> vector) {
		Double length= 0d;
		for (T element : vector) {
			if (null != element) {
				length += Math.pow(doubleExtractor(element), 2d);
			}
		}
		return Math.sqrt(length);
	}

	
	/**
	 * Calculates the dot product of two vectors.</br></br>
	 * 
	 * <code>dotProd = (a1 * b1) + (a2 * b2) + (an * bn)</code>
	 * 
	 * @param vectorA list of elements.
	 * @param vectorB list of elements.
	 * @return the dot product.
	 * @throws IllegalArgumentException if both vectors do not have the same count of elements.
	 */
	public static Double dotProduct(List<Double> vectorA, List<Double> vectorB) {
		if (vectorA.size() != vectorB.size()) {
			throw new IllegalArgumentException("Vectors must have the same count of elements! "
					+ "[vectorA="+vectorA.size()+", vectorB="+vectorB.size()+"]");
		}
		Double dotProd = 0d;
		for (int i = 0; i < vectorA.size(); i++) {
			Double elementA = vectorA.get(i);
			Double elementB = vectorB.get(i);
			if (null != elementA && null != elementB) {
				dotProd += vectorA.get(i) * vectorB.get(i);
			}
		}
		return dotProd;
	}
	
	public static Double dotProduct(Map<String, Double> vectorA, Map<String, Double> vectorB) {
		if (vectorA.size() != vectorB.size()) {
			throw new IllegalArgumentException("Vectors must have the same count of elements! "
					+ "[vectorA="+vectorA.size()+", vectorB="+vectorB.size()+"]");
		}
		Double dotProd = 0d;
		for (String term : vectorA.keySet()) {
			Double elementA = vectorA.get(term);
			Double elementB = vectorB.get(term);
			if (null != elementA && null != elementB) {
				dotProd += elementA * elementB;
			}
			
		}
		return dotProd;
	}
	
	/**
	 * Normalizes a vector with the L2-Norm (euclidian norm).</br></br>
	 * 
	 * <code>^v = (v1, v2, vn) / euclidianNorm of v</code>
	 * 
	 * @param vector
	 * @return
	 */
	public static <T extends Number> List<Double> normlizeVectorWithEuclidianNorm(Collection<T> vector) {
		return normlizeVectorWithEuclidianNorm(vector, euclidianNorm(vector));
	}
	
	/**
	 * As {@link VectorMath#normlizeVectorWithEuclidianNorm(Collection)} but with a given euclidienNorm</br>
	 * calculated with ie. {@link VectorMath#euclidianNorm(Collection)}.</br></br>
	 * 
	 * <code>^v = (v1, v2, vn) / euclidianNorm of v</code>
	 * 
	 * @param vector to calculate.
	 * @param norm to be used for calculation (2 for euclidian).
	 * @return normlized vector.
	 */
	public static <T extends Number> List<Double> normlizeVectorWithEuclidianNorm(Collection<T> vector, Double norm) {
		List<Double> normalized = new ArrayList<>(vector.size());
		for (Number element : vector) {
			if (null != element) {
				normalized.add(doubleExtractor(element) / norm);
			}
		}
		return normalized;
	}
	
	public static Double cosineSimilarity(List<Double> vectorA, List<Double> vectorB) throws InvalidObjectException {
		Double dotProd = dotProduct(vectorA, vectorB);
		Double euclidianNormProduct = euclidianNorm(vectorA) * euclidianNorm(vectorB);
		if (null != euclidianNormProduct && 0 != euclidianNormProduct) {
			return dotProd / euclidianNormProduct;
		} else {
			throw new InvalidObjectException("Division by zero not allowed! [deonomiator euclidianNormProduct="+euclidianNormProduct+"]");
		}
	}
	
	
	private static <T extends Number> Double doubleExtractor(T number) {
		if (number instanceof Double) {
			return number.doubleValue();
		} else {
			Double result;
			if (number instanceof Integer) {
				result = new Double(number.intValue());
			} else if (number instanceof Float) {
				result = new Double(number.floatValue());
			} else {
				throw new RuntimeException("Support for type not yet supported! [type="+number.getClass().getSimpleName()+"]");
			}
			return result;
		}
	}
	
}
