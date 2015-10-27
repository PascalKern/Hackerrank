package info.pkern.sandbox;

public class NumberAdder {

	public static <T extends Number>  T add(T leftValue, T rightValue) {
		if (leftValue instanceof Integer) {
			Integer result = leftValue.intValue() + rightValue.intValue();
//			return Integer.valueOf(leftValue.intValue() + rightValue.intValue());
			return (T) result;
		} else {
			throw  new IllegalStateException();
		}
	}
}
