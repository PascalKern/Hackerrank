package info.pkern.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
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
	
	@Test
	public void testBitSet2() {
		BitSet testSet = new BitSet();
		testSet.set(1,true);
		testSet.set(2,false);
		testSet.set(3,true);
		System.out.println(testSet);
		System.out.println(Long.toBinaryString(testSet.toLongArray()[0]));
		testSet.set(0);
		System.out.println(Long.toBinaryString(testSet.toLongArray()[0]));
	}
	
	@Test
	public void testBitSet() {
		String bits = "11101011010110110001101010101101010101010110100101";
		
		BitSet fromByteArray = BitSet.valueOf(bits.getBytes());
		System.out.println(fromByteArray);
		System.out.println(Long.toString(fromByteArray.toLongArray()[0], 2));
		System.out.println(fromByteArray.length());
		
		BitSet fromLongValue = BitSet.valueOf(new long[]{Long.parseLong(bits,2)});
		System.out.println(fromLongValue);
		System.out.println(Long.toString(fromLongValue.toLongArray()[0], 2));
		System.out.println(fromLongValue.length());

		System.out.println(Long.toString(Long.MAX_VALUE,2));
		
		String bitsB = "100101011010110110001101010101101010101";
		String bitsA = "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101"
				+ "11101011010110110001101010101101010101010110100101";
		
		BitSet verlyLong = new BitSet();
		for (int i = 0; i < bitsA.length(); i++) {
			verlyLong.set(i,bitsA.charAt(i) == '1');
		}
		System.out.println(verlyLong.length());
		System.out.println(Arrays.toString(verlyLong.toLongArray()));
		
		
		
		//OR - Not yet working because of the substring boundaries... But no need to fix at the moment!
		int partsCount = (bitsA.length() / 63) + ((bitsA.length() % 63 != 0)?1:0);
		long[] parts = new long[partsCount];
		int partCounter = 0;
		while (partCounter < partsCount) {
			parts[partCounter] = Long.parseLong(bitsA.substring(partCounter * 63, partCounter + 1 * 63),2);
			partCounter++;
		}
		verlyLong = BitSet.valueOf(parts);
		//But much better use BitSet.valueOf(LongBuffer long);
		
		StringBuilder sb = new StringBuilder();
		StringBuilder partsSB = new StringBuilder();
		for (Long part : verlyLong.toLongArray()) {
			partsSB.append(Long.toBinaryString(part));
		}
		sb.append(String.format("%"+verlyLong.size()+"s",partsSB.toString())).append(System.lineSeparator());
		sb.replace(sb.lastIndexOf(System.lineSeparator()), 1, "");
		System.out.println(verlyLong);
		System.out.println(sb.toString());
		System.out.println(verlyLong.length());
	}
	
}
