package info.pkern.hackerrank.commons;

import java.util.List;

public class ListTypeConverter {

	public static double[] toPrimitive(Double[] doubleObjects) {
		double[] primitive = new double[doubleObjects.length];
		for (int i = 0; i < primitive.length; i++) {
			primitive[i] = doubleObjects[i];
		}
		return primitive;
	}
	public static double[] toPrimitive(List<Double> doubleObjects) {
		return toPrimitive(doubleObjects.toArray(new Double[]{}));
	}
}
