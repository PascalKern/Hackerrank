package sandbox.info.pkern;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import info.pkern.TestdataHandler;
import info.pkern.algorithms.impl.gridSearch.localClasses.DataGridTest;
import info.pkern.algorithms.impl.gridSearch.localClasses.Testdata;

import org.junit.Test;

public class TestdataHandlerExp {

	private String testData = "2\n"
							+ "10 10\n"
							+ "7283455864\n"
							+ "6731158619\n"
							+ "8988242643\n"
							+ "3830589324\n"
							+ "2229505813\n"
							+ "5633845374\n"
							+ "6473530293\n"
							+ "7053106601\n"
							+ "0834282956\n"
							+ "4607924137\n"
							+ "3\t4\n"
							+ "9505\n"
							+ "3845\n"
							+ "3530\n"
							+ "15 15\n"
							+ "400453592126560\n"
							+ "114213133098692\n"
							+ "474386082879648\n"
							+ "522356951189169\n"
							+ "887109450487496\n"
							+ "252802633388782\n"
							+ "502771484966748\n"
							+ "075975207693780\n"
							+ "511799789562806\n"
							+ "404007454272504\n"
							+ "549043809916080\n"
							+ "962410809534811\n"
							+ "445893523733475\n"
							+ "768705303214174\n"
							+ "650629270887160\n"
							+ "2\t2\n"
							+ "99\n"
							+ "99";
	
	@Test
	public void simpleTest() throws IOException {
		
		InputStream in = new ByteArrayInputStream(testData.getBytes());
//		InputStream in = new ByteArrayInputStream(Charset.forName("UTF-16").encode(testData).array());
		
//		InputStreamReader isr = new InputStreamReader(in);
//		int charr;
//		while (-1 != (charr = isr.read())) {
//			System.out.println(charr);
//		}
		
		TestdataHandler<Testdata> inputHandler = new TestdataHandler<>(Testdata.class, in);
		
		System.out.println(inputHandler.getTestdata(0).getGrid());
		System.out.println(inputHandler.getTestdata(0).getPattern());
		
	}
	
	@Test
	public void testReadingInputWithExpectedResultsFromFile() throws IOException, URISyntaxException {
		
		Path file = Paths.get("test", DataGridTest.class.getPackage().getName().replace(".", "/"), "test_input.txt");
		assertTrue(Files.exists(file, LinkOption.NOFOLLOW_LINKS));
		
		TestdataHandler<Testdata> inputHandler = new TestdataHandler<>(Testdata.class, file);
		
		System.out.println(inputHandler.getTestdata(0).getGrid());
		System.out.println(inputHandler.getTestdata(0).getPattern());
		System.out.println(inputHandler.getTestdata(0).containsExptectedResults());
		System.out.println(inputHandler.getTestdata(0).getExpectedString());
		System.out.println(inputHandler.getTestdata(1).getGrid());
		System.out.println(inputHandler.getTestdata(1).getPattern());
		System.out.println(inputHandler.getTestdata(1).containsExptectedResults());
		System.out.println(inputHandler.getTestdata(1).getExpectedString());
	}
	
}
