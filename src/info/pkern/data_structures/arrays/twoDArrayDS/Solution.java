package info.pkern.data_structures.arrays.twoDArrayDS;
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
 * @version 0.1 - (Hackerrank Solution-Environment)
 * @author Pascal Kern
 * @category Data Structurs - Arrays: 2D Array - DS
 * @see https://www.hackerrank.com/challenges/2d-array
 */
public class Solution {
	
    public static void main(String[] args) {
    	
    	
    	
    }
    
    
	
	
	
	
	/* ************************************************************************************************************
	 * Classes to solve this hackerrank challenge. When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/

    private class LargestHourglassFinder {
    	
    }
    
	/* ************************************************************************************************************
	 * General helper classes for the submission of a hackerrank solution. When working on the solution locally
	 * those classes are available within the project. 
	 **************************************************************************************************************/

    private static class ListTypeConverter {

    	public static List<Integer> toInteger(String[] stringArray) {
    		List<Integer> integerList = new ArrayList<>(stringArray.length);
    		for (int i = 0; i < stringArray.length; i++) {
    			integerList.add(Integer.parseInt(stringArray[i]));
    		}
    		return integerList;
    	}
    	
    }
}