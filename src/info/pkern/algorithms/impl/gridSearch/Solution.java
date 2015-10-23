package info.pkern.algorithms.impl.gridSearch;
/* ============================================================================
 * Copyright (c) 2015 Pascal Kern
 * 
 * http://www.pkern.info/
 * ============================================================================
 */

import info.pkern.TestdataInputProcessor;
import info.pkern.algorithms.impl.gridSearch.localClasses.GridSearchTestData;

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
    	
    	TestdataInputProcessor<GridSearchTestData> inputProcessor = new TestdataInputProcessor<>(GridSearchTestData.class, System.in);
    	
    	for (GridSearchTestData testdata : inputProcessor.getTestDatas()) {
    		if (testdata.getGrid().contains(testdata.getPattern())) {
    			System.out.println("YES");
    		} else {
    			System.out.println("NO");
    		}
    	}
    	
    }
    
    
}
