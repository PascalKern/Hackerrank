package info.pkern.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class Variouse {

	@Test
	public void listAddAtIndex() {
//		List<Integer> theList = new ArrayList<>(3);
		List<Integer> theList = Arrays.asList(new Integer[3]);
		
		System.out.println("Empty list: " + theList);
		System.out.println("Size: " + theList.size());
		
//		Collections.fill(theList, 5);
//		
//		System.out.println("Filled with 5: " + theList);
//		System.out.println("Size: " + theList.size());
		
		theList.set(1, 0);
		
		System.out.println("Middle element set to 0: " + theList);
		System.out.println("Size: " + theList.size());
	}
	
	
}
