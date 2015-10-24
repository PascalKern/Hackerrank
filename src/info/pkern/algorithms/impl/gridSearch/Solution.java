package info.pkern.algorithms.impl.gridSearch;
/* ============================================================================
 * Copyright (c) 2015 Pascal Kern
 * 
 * http://www.pkern.info/
 * ============================================================================
 */

import info.pkern.TestdataHandler;
import info.pkern.algorithms.impl.gridSearch.localClasses.Testdata;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/**
 * @version 0.0.1
 * @author Pascal Kern
 * @category Algorithms - Implementation
 * @see https://www.hackerrank.com/challenges/the-grid-search
 */
public class Solution {
	
    public static void main(String[] args) {
    	
    	TestdataHandler<Testdata> inputProcessor = new TestdataHandler<>(Testdata.class, System.in);
    	
    	boolean testResult;
    	for (Testdata testdata : inputProcessor.getAllTestdata()) {
    		testResult = testdata.getGrid().contains(testdata.getPattern());
    		System.out.println(evaluateHackerrankResultString(testResult));
    	}
    	
    }
    
	public static String evaluateHackerrankResultString(boolean result) {
		if (result) {
			return "YES";
		} else {
			return "NO";
		}
	}
    
}
