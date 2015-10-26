package info.pkern.algorithms.warmup.aVeryBigSum;
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
			scanner.nextLine();
			Long sum = 0l;
			while (scanner.hasNextInt()) {
				sum += scanner.nextInt();
			}
			System.out.println(sum);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
