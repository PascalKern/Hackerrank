package info.pkern.algorithms.impl.gridSearch.manasAndStones;
/* ============================================================================
 * Copyright (c) 2015 Pascal Kern
 * 
 * http://github.com/PascalKern/Hackerrank_java7
 * http://www.pkern.info/
 * ============================================================================
 */

import info.pkern.hackerrank.tools.ListTypeConverter;

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
    	
    	List<Integer> values = getPossibleValues(3, 1, 2);
    	Collections.sort(values);
    	System.out.println("Vals 1: " + ListTypeConverter.toSpaceSeparatedString(values));
    	values = getPossibleValues(4, 10, 100);  
    	Collections.sort(values);
    	System.out.println("Vals 2: " + ListTypeConverter.toSpaceSeparatedString(values));
    	
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
//    public class ListTypeConverter {
    
    
}
