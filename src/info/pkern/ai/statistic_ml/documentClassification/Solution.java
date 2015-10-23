package info.pkern.ai.statistic_ml.documentClassification;
/* ============================================================================
 * Copyright (c) 2015 Pascal Kern
 * 
 * http://www.pkern.info/
 * ============================================================================
 */

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/**
 * @version 0.0.1
 * @author Pascal Kern
 * @category Statistics and Machine Learning - Document Classification
 * @see https://www.hackerrank.com/challenges/document-classification
 */
public class Solution {
	
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    	Random random = new Random();
    	for (int i = 0; i < args.length - 1; i++) {
    		System.out.println(1 + random.nextInt(8));
		}
    }
    
    
}
