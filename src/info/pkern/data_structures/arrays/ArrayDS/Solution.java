package info.pkern.data_structures.arrays.ArrayDS;
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
 * @category Data Structures - Arrays: Arrays-DS
 * @see https://www.hackerrank.com/challenges/arrays-ds
 */
public class Solution {
	
    public static void main(String[] args) {
    	
    	List<String> values;
    	try (Scanner scanner = new Scanner(System.in)) {
    		scanner.nextLine();
    		values = Arrays.asList(scanner.nextLine().split(" "));
    	}
    	Collections.reverse(values);
    	String result = values.toString().replace(" ", "").replace(",", " ");
    	result = result.replace("[", "").replace("]", "");
    	System.out.println(result);
    	
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
