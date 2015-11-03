package info.pkern.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class Variouse {

	
	@Test
	public void formatDoubleAsScientificString() {
		Double d = 0.0000012354252342346231463452134d;
		System.out.println(String.format("%g",d));
		System.out.println(String.format("%4g",d));
		System.out.println(String.format("%.6g",d));
		System.out.println(String.format("%4.6g",d));
		System.out.println(String.format("%E",d));
		System.out.println(String.format("%4E",d));
		System.out.println(String.format("%.6e",d));
		System.out.println(String.format("%4.6e",d));
		System.out.println(String.format("%.10e",d));
		System.out.println(String.format("%4.10e",d));
	}
	
	@Test
	public void sortList() {
		List<Double> classification = Arrays.asList(new Double[]{1d,2d,3d});
		Collections.sort(classification, new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {
//				return o1.compareTo(o2);
				System.out.println("Compare o1 to o2: " + o1 + " to " + o2 + " = " + o1.compareTo(o2));
				System.out.println("Compare o2 to o1: " + o2 + " to " + o1 + " = " + o2.compareTo(o1));
				return o2.compareTo(o1);
			}
		});
		System.out.println(classification);
	}
	
	@Test
	public void setRemoveAllTest() {
		Set<String> firstSet = new HashSet<>(Arrays.asList(new String[]{"eins","zwei","fünf"}));
		Set<String> secondSet = new HashSet<>(Arrays.asList(new String[]{"eins","zwei","drei","vier"}));
		System.out.println(secondSet);
		System.out.println(secondSet.removeAll(firstSet));
		System.out.println(secondSet);
		secondSet = new HashSet<>(Arrays.asList(new String[]{"eins","zwei","fünf"}));
		System.out.println(secondSet);
		System.out.println(secondSet.removeAll(firstSet));
		System.out.println(secondSet);
	}
	
	@Test
	public void listTest() {
		List<Integer> ints = Arrays.asList(new Integer[]{1,2,3});
		System.out.println(ints.indexOf(2));
		ints.set(1, null);
		System.out.println(ints.indexOf(2));
		System.out.println(ints.indexOf(null));
	}
	
	
	@Test
	public void intAsFloat() {
		float floatNumber = 1.00000000000f;
		System.out.println(floatNumber);
		floatNumber = 123432f;
		System.out.println(floatNumber);
	}
	
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
