package info.pkern.sandbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.DocumentTokanizer;
import info.pkern.ai.statistic_ml.documentClassification.localClasses.NGrammCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;

public class Variouse {

	@Test
	public void regexPatterns() {
		String text = "A new text writen by me@gmail.com. This isn't a long text; so what! It should "
				+ "be enought to (check) if all \"does what it should do\". Does it? Also this	tab should be done! "
				+ "And what is with muliple spaces? Here are three   , and again five     .\nWhat about newlines and "
				+ "what is with String-Concatenated words. On the other side just space-sparated words should be connected";
		//Punctuation: One of !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
		Pattern removeCharPattern = Pattern.compile("\\p{Punct}");
		//A whitespace character: [ \t\n\x0B\f\r]
//		removeCharPattern = Pattern.compile(removeCharPattern+"\\p{Space}");
		removeCharPattern = Pattern.compile("["+removeCharPattern+"\\p{Space}]");
		//Exclude: One of
		Pattern excludedRemoveCharPattern = Pattern.compile("'.");
		System.out.println("["+removeCharPattern+"&&[^"+excludedRemoveCharPattern+"]]");
		
		String text2 = text.replaceAll("["+removeCharPattern+"&&[^"+excludedRemoveCharPattern+"]]", " ");
		System.out.println(text2);
		System.out.println(text2.replaceAll("\\p{Space}{2,}", " "));
		
		
		
		DocumentTokanizer tokanizer = new DocumentTokanizer(text);
		System.out.println(tokanizer.getTokens());
		NGrammCreator creator = new NGrammCreator(5);
		System.out.println(creator.process(tokanizer.getTokens()));
	}
	
	@Test
	public void listAsString() {
		List<String> theList = Arrays.asList("A", "word", "is", "here");
		System.out.println(theList);
		System.out.println(theList.toString().replaceAll("[\\[\\],]", ""));
	}
	
	@Test
	public void stringBuilder() {
		StringBuilder sb = new StringBuilder();
		System.out.println("Empty string builder: |"+sb.toString() +"|");
		sb.append("Line one").append(System.lineSeparator());
		sb.append("Line two, a bit longer");
		System.out.println("All:\n|"+sb+"|");
		StringBuilder line = sb.delete(0, sb.indexOf(System.lineSeparator())+1);
		System.out.println("Second line only:\n|"+sb+"|");
		System.out.println("First line was: "+line);
		sb.delete(sb.lastIndexOf(" "), sb.length());
		System.out.println("Second line without last word (longer) |"+sb+"|");
	}
	
	@Test
	public void numberDouble() {
		Number num = new Integer(10);
		System.out.println(num.intValue());
		System.out.println(num.doubleValue());
	}
	
	@Test
	public void copyList() {
		List<List<Integer>> baseList = new ArrayList<>();
		List<Integer> listA = Arrays.asList(1,2,3,4);
		List<Integer> listB = Arrays.asList(10,20,30,40);
		baseList.add(listA);
		baseList.add(listB);
		
		System.out.println("BaseList: " + baseList);
		
		List<List<Integer>> copyList = new ArrayList<>();
		copyList.addAll(baseList);
		copyList.get(0).set(1, 20);
		copyList.get(1).set(1, 2);
		
		System.out.println("BaseList: " + baseList);
		System.out.println("CopyList: " + copyList);
		
		List<List<Integer>> copyDeepList = new ArrayList<>();
		for (List<Integer> list : baseList) {
			List<Integer> newList = new ArrayList<>();
			newList.addAll(list);
			copyDeepList.add(newList);
		}
		
		copyDeepList.get(0).set(2, 30);
		copyDeepList.get(1).set(2, 3);

		System.out.println("BaseList: " + baseList);
		System.out.println("CopyDeep: " + copyDeepList);
	}
	
	
	
	@Test
	public void stringMatching() {
		String testString = "My <brother> brother@me.com is worth 10$ per day! But what about"
				+ " my (stiff) mother's?";
		String exptected = "My _brother_ brother@me.com is worth 10_ per day_ But what about"
				+ " my (stiff) mother's_";
		
		//All punctuation except on in the char group [()@.']. Punctuation is from:
		//descripted in javadoc of the Pattern.class and contains: !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~ 
		String actual = testString.replaceAll("[\\p{Punct}&&[^\\(\\)@\\.']]", "_");
		
		assertEquals(exptected, actual);
	}
	
	@Test
	public void things() {
		Map<String, List<String>> test = new HashMap<>();
		List<String> testList = new ArrayList<>();
		testList.add("one");
		testList.add("two");
		test.put("test", testList);
		System.out.println(test);
		testList.add("three");
		System.out.println(test);
	}

	
	@Test
	public void setRemove() {
		Set<String> setA = new HashSet<>(Arrays.asList(new String[]{"one","two","three"}));
		Set<String> setB = new HashSet<>(setA);
		setB.remove("two");
		System.out.println(setA);
	}
	
	@Test
	public void addElementToArrysGeneratedList() {
		List<String> list = Arrays.asList(new String[2]);
		try {
			list.add("test");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(list);
		List<Double> listDouble = Arrays.asList(new Double[]{0d,1d});
		try {
			listDouble.add(2d);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(listDouble);
		assertTrue(list.size() == 2);
		assertTrue(list.get(2).equals("test"));
	}
	
	@Test
	public void noValueInitializedList() {
		List<Double> list = Arrays.asList(new Double[10]);
		System.out.println(list);
	}
	
	@Test
	public void listGetIndexOfNull() {
		List<String> list = Arrays.asList(new String[]{"one","two",null,"four",null,"five"});
		assertTrue(list.indexOf(null) == 2);
		assertTrue(list.contains(null));
	}
	
	@Test
	public void setEmpty() {
		Set<String> set = new HashSet<>(3);
		assertFalse(set.size() > 0);
		assertTrue(set.isEmpty());
	}
	
	@Test
	public void setRetainAll() {
		Set<String> setA = new HashSet<>(Arrays.asList(new String[]{"eins","zwei","drei"}));
		Set<String> setB = new HashSet<>(Arrays.asList(new String[]{"zwei","drei","vier"}));
		System.out.println(setA);
		System.out.println(setA.retainAll(setB));
		System.out.println(setA);
		System.out.println(setB);
	}
	
	@Test
	public void normVectorLength() {
		List<Double> vector = Arrays.asList(new Double[]{0.5d,1d,0d,0.25,0.75d,0.2d,0.3d});
		
		Double vectorLength = 0d;
		for (Double element : vector) {
			vectorLength += Math.pow(element, 2);
		}
		vectorLength = Math.sqrt(vectorLength);
		System.out.println("Vector: " + vector);
		System.out.println("Vector length: " + vectorLength);
		
		List<Double> vectorNormalized = new ArrayList<>(vector.size());
		for (Double element : vector) {
//			System.out.println(element);
			vectorNormalized.add(element / vectorLength);
		}
		
		vectorLength = 0d;
		for (Double element : vectorNormalized) {
			vectorLength += Math.pow(element, 2);
		}
		vectorLength = Math.sqrt(vectorLength);
		System.out.println("Unity vector: " + vectorNormalized);
		System.out.println("Unity (Euclidean-Norm / L2-Norm) vector length: " + vectorLength);
	}
	
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
