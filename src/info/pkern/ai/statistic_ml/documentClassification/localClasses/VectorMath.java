package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.commons.NumberUtil;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//TODO Split into three classes one per List, Array (both primitive and Object types) and Map or
//extend in here. Do not convert between types due of performance and unwanted iterations!
public class VectorMath {

	/**
	 * Calculates the dot product of two vectors.</br></br>
	 * 
	 * <code>dotProd = (a1 * b1) + (a2 * b2) + ... + (an * bn)</code>
	 * 
	 * @param vectorA list of elements.
	 * @param vectorB list of elements.
	 * @return the dot product.
	 * @throws IllegalArgumentException if both vectors do not have the same count of elements.
	 */
	public static <T extends Number, E extends Number> Double dotProduct(List<T> vectorA, List<E> vectorB) {
		checkIdenticalVectorElementCounts(vectorA, vectorB);
		Double dotProd = 0d;
		for (int i = 0; i < vectorA.size(); i++) {
			Double elementA = doubleValueOrZero(vectorA.get(i));
			Double elementB = doubleValueOrZero(vectorB.get(i));
			dotProd += elementA * elementB;
		}
		return dotProd;
	}

	/**
	 * Calculates the dot product of two vectors. The vectors can have different counts of elements.</br>
	 * The method will take the longer one and use its keys to calculate all possible element products.</br></br>
	 * 
	 * See: {@link VectorMath#dotProduct(List, List)} for further explanations.
	 * @param vectorA map of elements.
	 * @param vectorB map of elements.
	 * @return the dot product.
	 */
	public static <T extends Number, E extends Number> Double dotProduct(Map<String, T> vectorA, Map<String, E> vectorB) {
		Set<String> longer = (vectorA.size() >= vectorB.size())?vectorA.keySet():vectorB.keySet();
		Double dotProd = 0d;
		for (String term : longer) {
			Double elementA = doubleValueOrZero(vectorA.get(term));
			Double elementB = doubleValueOrZero(vectorB.get(term));
			dotProd += elementA * elementB;
		}
		return dotProd;
	}

	public static Double cosineSimilarityEuclideanNorm(List<Double> vectorA, List<Double> vectorB) throws InvalidObjectException {
		checkIdenticalVectorElementCounts(vectorA, vectorB);
		Double dotProd = dotProduct(vectorA, vectorB);
		Double euclidianNormProduct = lengthEuclideanNorm(vectorA) * lengthEuclideanNorm(vectorB);
		if (null != euclidianNormProduct && 0 != euclidianNormProduct) {
			return dotProd / euclidianNormProduct;
		} else {
			throw new InvalidObjectException("Division by zero not allowed! [deonomiator euclidianLengthProduct="+euclidianNormProduct+"]");
		}
	}

	public static Double cosineSimilarityEuclideanNorm(Map<String, Double> vectorA, Map<String, Double> vectorB) throws InvalidObjectException {
		Double dotProd = dotProduct(vectorA, vectorB);
		Double lengthProduct = lengthEuclideanNorm(vectorA.values()) * lengthEuclideanNorm(vectorB.values());
		if (null != lengthProduct && 0 != lengthProduct) {
			return dotProd / lengthProduct;
		} else {
			throw new InvalidObjectException("Division by zero not allowed! [deonomiator euclidianLengthProduct="+lengthProduct+"]");
		}
	}
	
	public static <T extends Number, E extends Number> Double distanceBetweenEuclideanNorm(Map<String, T> vectorA, Map<String, E> vectorB) {
		Double elementDiffSum = 0d;
		for (String term : vectorA.keySet()) {
			Double elementA = doubleValueOrZero(vectorA.get(term));
			Double elementB = doubleValueOrZero(vectorB.get(term));
			elementDiffSum +=  Math.pow(elementA - elementB, 2);
		}
		return Math.sqrt(elementDiffSum);
	}

	/**
	 * Normalizes a vector with the L2-Norm (euclidean norm).</br></br>
	 * 
	 * <code>^v = (v1, v2, vn) / euclidianLength of v</code>
	 * 
	 * @param vector
	 * @return normalized vector (Unity-Vector).
	 */
	public static <T extends Number> List<Double> normlizeVectorEuclideanNorm(Collection<T> vector) {
		return normlizeVectorWithNorm(vector, 2);
	}

	/**
	 * Normalizes a vector with the L2-Norm (euclidean norm).</br></br>
	 * 
	 * <code>^v = (v1, v2, vn) / euclidianLength of v</code>
	 * 
	 * @param vector.
	 * @return normalized vector.
	 */
	public static <T extends Number> Map<String, Double> normlizeVectorEuclideanNorm(Map<String, T> vector) {
		return normlizeVectorWithNorm(vector, 2);
	}

	/**
	 * As {@link VectorMath#normlizeVectorEuclideanNorm(Collection)} but with a given norm</br>
	 * calculated with ie. {@link VectorMath#lengthEuclideanNorm(Collection)}.</br></br>
	 * 
	 * <code>^v = (v1, v2, vn) / length of v for given norm</code>
	 * 
	 * @param vector to calculate.
	 * @param norm to be used for calculation (2 for euclidean).
	 * @return normalized vector (Unity-vector).
	 */
	public static <T extends Number> List<Double> normlizeVectorWithNorm(Collection<T> vector, Integer norm) {
		List<Double> normalized = new ArrayList<>(vector.size());
		Double length = lengthWithNorm(vector, norm);
		for (Number element : vector) {
			if (null != element) {
				normalized.add(doubleValueOrZero(element) / length);
			}
		}
		return normalized;
	}

	/**
	 * As {@link VectorMath#normlizeVectorEuclideanNorm(Collection)} but with a given norm</br>
	 * calculated with ie. {@link VectorMath#lengthEuclideanNorm(Collection)}.</br></br>
	 * 
	 * <code>^v = (v1, v2, vn) / length of v for given norm</code>
	 * 
	 * @param vector to calculate.
	 * @param norm to be used for calculation (2 for euclidean).
	 * @return normalized vector.
	 */
	public static <T extends Number> Map<String, Double> normlizeVectorWithNorm(Map<String, T> vector, Integer norm) {
		Map<String, Double> normalized = new HashMap<>(vector.size());
		Double length = lengthWithNorm(vector.values(), norm);
		for (String element : vector.keySet()) {
			if (null != element) {
				normalized.put(element, doubleValueOrZero(vector.get(element)) / length);
			}
		}
		return normalized;
	}

	/**
	 * Calculates the "length" of a vector with the L2-Norm. The so called</br>
	 * euclidean norm.</br></br>
	 * 
	 * <code>||v|| = sqrt(v1^2 + v2^2 + ... vn^2)</code>
	 * @param vector to calculate the L2-Norm.
	 * @return the L2-Norm value.
	 */
	public static <T extends Number> Double lengthEuclideanNorm(Collection<T> vector) {
		Double length= 0d;
		for (T element : vector) {
			if (null != element) {
				length += Math.pow(doubleValueOrZero(element), 2d);
			}
		}
		return Math.sqrt(length);
	}

	/**
	 * Calculates the "length" of a vector with the given norm. Therefore a call of</br>
	 * with norm of <code>2</code> is equal to {@link VectorMath#lengthEuclideanNorm(Collection)}</br></br>
	 * 
	 * @param vector to calculate the norm.
	 * @param norm to use for calculation.
	 * @return the length.
	 */
	public static <T extends Number> Double lengthWithNorm(Collection<T> vector, Integer norm) {
		Double length= 0d;
		for (T element : vector) {
			if (null != element) {
				length += Math.pow(doubleValueOrZero(element), norm);
			}
		}
		return nthroot(norm, length, 0.0001);
	}

	//http://rosettacode.org/wiki/Category:Java
	private static double nthroot(int n, double A, double p) {
		if(A < 0) {
			System.err.println("A < 0");// we handle only real positive numbers
			return -1;
		} else if(A == 0) {
			return 0;
		}
		double x_prev = A;
		double x = A / n;  // starting "guessed" value...
		while(Math.abs(x - x_prev) > p) {
			x_prev = x;
			x = ((n - 1.0) * x + A / Math.pow(x, n - 1.0)) / n;
		}
		return x;
	}
	
	private static Double doubleValueOrZero(Number number) {
		if (null == number) {
			return 0d;
		} else {
			return number.doubleValue();
		}
	}
	
	private static <T extends Number, E extends Number> void checkIdenticalVectorElementCounts(
			Collection<T> vectorA, Collection<E> vectorB) {
		if (vectorA.size() != vectorB.size()) {
			throw new IllegalArgumentException(
					"Vectors must have the same count of elements! "
							+ "[vectorA=" + vectorA.size() + ", vectorB="
							+ vectorB.size() + "]");
		}
	}
	
}
