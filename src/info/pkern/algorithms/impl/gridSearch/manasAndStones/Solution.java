package info.pkern.algorithms.impl.gridSearch.manasAndStones;
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
import java.math.*;
import java.util.regex.*;

/**
 * @version 0.1 - Simple (Hackerrank Solution-Environment)
 * @author Pascal Kern
 * @category Algorithm - Implementation: Manasa and Stones
 * @see https://www.hackerrank.com/challenges/manasa-and-stones
 */
public class Solution {
	
    public static void main(String[] args) {
    	
    	List<Integer> values;
    	try (Scanner scanner = new Scanner(System.in)) {
    		int nrTestCasses = scanner.nextInt();
    		while (nrTestCasses > 0) {
    			values = getPossibleValues(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
    			Collections.sort(values);
    			System.out.println(ListTypeConverter.toSpaceSeparatedString(values));
    			nrTestCasses--;
    		}
    	}
    	
    }
    
    
	private static List<Integer> getPossibleValues(int numberOfStones, int diffA, int diffB) {
		Set<Integer> possibleValues = new HashSet<>();
		for (int i = 0, j = numberOfStones -1; i < numberOfStones; i++, j--) {
			possibleValues.add((diffA * i) + (diffB * j));
		}
		return new ArrayList<Integer>(possibleValues);
	}
	
	
	
	/* ************************************************************************************************************
	 * Classes to solve this hackerrank challenge. When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/

	
    
	/* ************************************************************************************************************
	 * General helper classes for the submission of a hackerrank solution. When working on the solution locally
	 * those classes are available within the project. 
	 **************************************************************************************************************/
    private static class ListTypeConverter {
    	public static <E> String toSpaceSeparatedString(List<E> list) {
    		StringBuilder sb = new StringBuilder();
    		for (E entry : list) {
    			sb.append(entry);
    			if (list.indexOf(entry) < list.size()) {
    				sb.append(" ");
    			}
    		}
    		return sb.toString();
    	}
    }
    
    
}
