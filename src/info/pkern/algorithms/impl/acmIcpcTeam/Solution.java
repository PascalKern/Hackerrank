package info.pkern.algorithms.impl.acmIcpcTeam;
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
 * @category Algorithms - Implementation: ACM ICPC Team
 * @see https://www.hackerrank.com/challenges/acm-icpc-team
 */
public class Solution {
	
    public static void main(String[] args) {
    	
    	Solution solution = new Solution();
    	TeamTopicAnalyzer teamTopicAnalyzer = solution.new TeamTopicAnalyzer();
    	
    	try (Scanner scanner = new Scanner(System.in)) {
    		int people = scanner.nextInt();
    		int topics = scanner.nextInt();
    		while (scanner.hasNextLine()) {
    			String personTopics = scanner.nextLine();
    			teamTopicAnalyzer.addPersonTopics(personTopics);
    		}
    	}
    	System.out.println(teamTopicAnalyzer.getMaximalPossibleTopicsForTeams());
    	System.out.println(teamTopicAnalyzer.getTeamsWithMaxTopicsCount());
    }
    
	
	
	
	/* ************************************************************************************************************
	 * Classes to solve this hackerrank challenge. When working on the solution locally this
	 * classes can be separately unit tested.
	 **************************************************************************************************************/
    private class TeamTopicAnalyzer {

    	private List<BitSet> personTopics = new ArrayList<>();
    	private int maxPossibleTopicsForTeams = 0;
    	private int teamCountWithMaxTopics = 0;
    	
    	private boolean isDirty = true;
    	
    	public void addPersonTopics(String personTopicsData) {
    		BitSet personTopics = new BitSet(personTopicsData.length());
    		for (int i = 0, j = personTopicsData.length() - 1; j >= 0; i++, j--) {
    			personTopics.set(i, personTopicsData.charAt(j) == '1');
    		}
    		this.personTopics.add(personTopics);
    		isDirty = true;
    	}
    	
    	private void analyzeTeamTopics() {
    		for (int i = 0; i < personTopics.size(); i++) {
    			BitSet personA = personTopics.get(i);
    			for (int j = i + 1; j < personTopics.size(); j++) {
    				//Not nice! But missing copy constructor in BitSet.
    				BitSet personB = (BitSet) personTopics.get(j).clone();
    				personB.or(personA);
    				int topics = personB.cardinality();
    				if (topics > maxPossibleTopicsForTeams) {
    					maxPossibleTopicsForTeams = topics;
    					teamCountWithMaxTopics = 1;
    				} else if (topics == maxPossibleTopicsForTeams) {
    					teamCountWithMaxTopics++;
    				}
    			}
    		}
    		isDirty = false;
    	}
    	
    	public Integer getMaximalPossibleTopicsForTeams() {
    		ifDirtyAnalyzeData();
    		return maxPossibleTopicsForTeams;
    	}
    	
    	public Integer getTeamsWithMaxTopicsCount() {
    		ifDirtyAnalyzeData();
    		return teamCountWithMaxTopics;
    	}
    	
    	private void ifDirtyAnalyzeData() {
    		if (isDirty) {
    			analyzeTeamTopics();
    		}
    	}
    	
    	@Override
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		for (BitSet person : personTopics) {
    			StringBuilder parts = new StringBuilder();
    			for (Long part : person.toLongArray()) {
    				parts.append(Long.toBinaryString(part));
    			}
    			sb.append(String.format("%"+person.size()+"s",parts.toString())).append(System.lineSeparator());
    		}
    		sb.replace(sb.lastIndexOf(System.lineSeparator()), sb.length(), "");
    		return sb.toString();
    	}
    }
    
	/* ************************************************************************************************************
	 * General helper classes for the submission of a hackerrank solution. When working on the solution locally
	 * those classes are available within the project. 
	 **************************************************************************************************************/
//    public class ListTypeConverter {
}
