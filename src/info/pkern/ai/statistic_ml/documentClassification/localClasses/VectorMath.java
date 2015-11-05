package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.List;

public class VectorMath {

	/**
	 * Calculates the "length" of a vector with the L2-Norm. The so called</br>
	 * euclidian norm.</br></br>
	 * 
	 * <code>||v|| = (v1^2 + v2^2 + ... vn^2)^1/2</code>
	 * @param vector to calculate the norm.
	 * @return the L2-Norm value.
	 */
	public static Double euclidianNorm(List<Double> vector) {
		Double length= 0d;
		for (Double element : vector) {
			length += Math.pow(element, 2d);
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
			dotProd += vectorA.get(i) * vectorB.get(i);
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
	public static List<Double> normlizeVectorWithEuclidianNorm(List<Double> vector) {
		return normlizeVectorWithEuclidianNorm(vector, euclidianNorm(vector));
	}
	
	/**
	 * As {@link VectorMath#normlizeVectorWithEuclidianNorm(List)} but with a given euclidienNorm</br>
	 * calculated with ie. {@link VectorMath#euclidianNorm(List)}.</br></br>
	 * 
	 * <code>^v = (v1, v2, vn) / euclidianNorm of v</code>
	 * 
	 * @param vector to calculate.
	 * @param euclidianNorm to be used for calculation.
	 * @return normlized vector.
	 */
	public static List<Double> normlizeVectorWithEuclidianNorm(List<Double> vector, Double euclidianNorm) {
		List<Double> normalized = new ArrayList<>(vector.size());
		for (Double element : vector) {
			normalized.add(element / euclidianNorm);
		}
		return normalized;
	}
	
	public static Double cosineSimilarity(List<Double> vectorA, List<Double> vectorB) {
		Double dotProd = dotProduct(vectorA, vectorB);
		Double euclidianNormProduct = euclidianNorm(vectorA) * euclidianNorm(vectorB);
		return dotProd / euclidianNormProduct;
	}
}
