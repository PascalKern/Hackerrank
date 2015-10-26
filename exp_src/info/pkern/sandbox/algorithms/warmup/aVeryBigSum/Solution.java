package info.pkern.sandbox.algorithms.warmup.aVeryBigSum;
/* ============================================================================
 * Copyright (c) 2015 Pascal Kern
 * 
 * http://github.com/PascalKern/Hackerrank_java7
 * http://www.pkern.info/
 * ============================================================================
 */

import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.reflect.Method;
import java.math.*;
import java.util.regex.*;

/**
 * @version 0.1 - (Hackerrank Solution-Environment)
 * @author Pascal Kern
 * @category Algorithms - Warmup: A Very Big Sum
 * @see https://www.hackerrank.com/challenges/a-very-big-sum
 */
public class Solution {
	
	public static void main(String[] args) {

		try (Scanner scanner = new Scanner(System.in)) {
			int numberOfElements = scanner.nextInt();	//Could also be skipped the whole line like with the next call.
			scanner.nextLine();	//Skips the rest of the line especially the newline character after the above read int.
			String testdata = scanner.nextLine();
			GenericArrayTotalizer<Long> totalizer = new GenericArrayTotalizer<>(testdata);
			System.out.println(totalizer.sumListRecursive());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public static class GenericArrayTotalizer<T extends Number> {

		private final List<T> numbers;
		
		public GenericArrayTotalizer(String testdata) {
//			numbers = convertStringToNumbersArray(testdata.split(" "));
			numbers = null;
			throw new RuntimeException("Not yet and will never be implemented!");
		}

		public T sumListIterative() {

			/*
			 * Some ideas. Ways to complicated and not rally usable nor needed for this!
			 */
			/*
			T sum = clazz.newInstance();

			//Find parseYXZ() method
			Method parseMethod;
			Method[] methods = clazz.getMethods();
			List<Class<?>> parameters;
			for (int i = 0; i < methods.length; i++) {
				parseMethod = methods[i];
				parameters = Arrays.asList(parseMethod.getParameterTypes());
				if (parseMethod.getName().startsWith("parse") && parameters.contains(String.class)) {
//					parseMethod = methods[i];
					break;
				}
			}
			
			//Find xyzValue() method
			String typeSuffixName = sum.getClass().getName().substring(0,4).toLowerCase();
			Method valueMethod;
			for (int i = 0; i < methods.length; i++) {
				valueMethod = methods[i];
				parameters = Arrays.asList(valueMethod.getParameterTypes());
				if (valueMethod.getName().startsWith(typeSuffixName)) {
//					valueMethod = methods[i];
					break;
				}
			}
			
			for (T number : numbers) {
//				sum = (T) valueMethod.invoke(sum, new Object[]{});
				sum = sum + valueMethod.invoke(number, new Object[]{});
//				sum = valueMethod.invoke(number, new Object[]{}) + parseMethod.invoke(sum, number);
//				sum = sum + number;
			}
			*/
			
			Number sum = 0;
			for (T number : numbers) {
				sum = numberAdder(number, sum);
			}
			
			return (T) sum;
		}

		/**
		 * Calculates the sum in a <code>List</code> of integers. Almost the fastest approach 
		 * <br/>
		 * <strong>Important:</strong> this method carries the risk of a <code>StackOverflowError</code>
		 * when the size of the List exceeds the maximal allowed Stack entries/size.
		 * 
		 * @param numbers list of numbers.
		 * @return the sum of all integers in the list.
		 */
		public T sumListRecursive() {
			return sumListRecursive(numbers);
		}
		
//		private T sumListRecursive(List<T> numbers, Class<T> clazz) {
		private T sumListRecursive(List<T> numbers) {
//			T sum = clazz.newInstance();
			Number sum = 0;
			if (numbers.size() == 1) {
				return numberAdder(numbers.get(0), sum);
//				return sum + numbers.get(0);
			} else {
				sum = numberAdder(numbers.get(0), sum);
				return numberAdder(sumListRecursive(numbers.subList(1, numbers.size())), sum);
//				sum += numbers.get(0);
//				return sum + sumListRecursive(numbers.subList(1, numbers.size()));
			}
		}

		/*
		 * Totally not working!
		 */
		/*
		private List<T> convertStringToNumbersArray(String[] strings) {
			List<T> numbers = new ArrayList<>(strings.length);
			for (int i = 0; i < strings.length; i++) {
				
				if (numbers instanceof Integer) {
					Integer sum = numberA.intValue() + numberB.intValue();
					return (T) sum;
				} else if (numberA instanceof Long) {
					Long sum = numberA.longValue() + numberB.longValue();
					return (T) sum;
					//And so on for all known Number Subtypes like Float, Double ...
				} else {
					throw new IllegalStateException("");
				}
				
				numbers.add(Integer.parseInt(strings[i]));
			}
			return numbers;
		}
		*/

		/*
		 * This would work but as noted above this is a total overhead for this use case!
		 */
		private T numberAdder(T numberA, Number numberB) {
			
			if (numberA instanceof Integer) {
				Integer sum = numberA.intValue() + numberB.intValue();
				return (T) sum;
			} else if (numberA instanceof Long) {
				Long sum = numberA.longValue() + numberB.longValue();
				return (T) sum;
				//And so on for all known Number Subtypes like Float, Double ...
			} else {
				throw new IllegalStateException("");
			}
		}
	}
}
