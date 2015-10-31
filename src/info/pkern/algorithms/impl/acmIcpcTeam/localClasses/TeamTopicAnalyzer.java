package info.pkern.algorithms.impl.acmIcpcTeam.localClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class TeamTopicAnalyzer {

	private List<BitSet> personTopics = new ArrayList<>();
	private int maxPossibleTopicsForTeams = 0;
	private int teamCountWithMaxTopics = 0;
	
	private boolean isDirty = true;
	
	public void addPersonTopics(String personTopicsData) {
		BitSet personTopics = new BitSet(personTopicsData.length());
		//Better use personTopicsData.toCharArray() and iterate over it 
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
