package info.pkern.data_structures.arrays.twoDArrayDS.localClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HourglassFinder {

	private final int gridSize;
	private List<Integer> grid;
	
	private Integer[] hourglassShape;
	
	private Map<Integer, Integer> hourglassSums = new HashMap<>();
	
	public HourglassFinder(List<Integer> grid, int gridSize) {
		this.gridSize = gridSize;
		this.grid = grid;
		hourglassShape = createHourglassShape(gridSize);
		hourglassSums = findHourglassesAndSum();
	}
	
	public Integer sumOfHourglassAt(int topLeftHourglassIndex) {
		if (hourglassSums.keySet().contains(topLeftHourglassIndex)) {
			return hourglassSums.get(topLeftHourglassIndex);
		} else { 
			return null;
		}
	}
	
	public Set<Integer> indicesOfHourglasses() {
		return hourglassSums.keySet();
	}

	public Integer getMaxHourglasSum() {
		List<Integer> values = new ArrayList<>(hourglassSums.values()); 
		Collections.sort(values);
		return values.get(0);
	}

	private boolean isHourglassAt(int topLeftHourglassIndex) {
		boolean isHourglass = false;
		int bottomLeftHourglasIndex = 2 * gridSize + topLeftHourglassIndex;
		
		isHourglass = grid.get(topLeftHourglassIndex) > 0;
		isHourglass = grid.size() >= bottomLeftHourglasIndex;
		
		if (isHourglass) {
			isHourglass = grid.get(gridSize + topLeftHourglassIndex) > 0;
//					&& grid.get(gridSize + topLeftHourglassIndex + 2) == 0; and, or
//					&& grid.get(gridSize + topLeftHourglassIndex + 1) > 0 
//					&& grid.get(bottomLeftHourglasIndex) > 0;
		}
		return isHourglass;
	}
	
	private Map<Integer, Integer> findHourglassesAndSum() {
		Map<Integer, Integer> hourglassSums = new HashMap<>();
		for (int i = 0; i < grid.size(); i++) {
//			int currentHourglassIndex = grid.get(i);
			if (isHourglassAt(i)) {
				hourglassSums.put(i, calculateHourglassSum(i));
			}
		}
		return hourglassSums;
	}
	
	private Integer calculateHourglassSum(int topLeftHourglassIndex) {
		int sum = 0;
		for (int i = 0; i < hourglassShape.length; i++) {
			sum += grid.get(topLeftHourglassIndex + hourglassShape[i]);
		}
		return sum;
	}
	
	private Integer[] createHourglassShape(int gridSize) {
		return new Integer[] {0,1,2,gridSize + 1, 2 * gridSize, 2 * gridSize + 1, 2 * gridSize + 2};
	}
}
