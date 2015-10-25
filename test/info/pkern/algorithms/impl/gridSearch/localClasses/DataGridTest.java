package info.pkern.algorithms.impl.gridSearch.localClasses;

import static org.junit.Assert.*;
import info.pkern.TestdataHandler;
import info.pkern.algorithms.impl.gridSearch.Solution;
import info.pkern.tools.LoggerHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.BeforeClass;
import org.junit.Test;

public class DataGridTest {
	
	private static TestdataHandler<Testdata> testdataHandler;
	private Testdata testdata;
	
	@BeforeClass
	public static void init() throws IOException {
		LoggerHandler.enableSingleLineFineConsoleLogging();
		
		Path file = Paths.get("test", DataGridTest.class.getPackage().getName().replace(".", "/"), 
				TestdataHandler.TESTDATA_FILE_NAME);
		assertTrue("The file with the testdata does not exist! [file: " + file.normalize().toAbsolutePath() +"]",
				Files.exists(file, LinkOption.NOFOLLOW_LINKS));
		testdataHandler = new TestdataHandler<>(Testdata.class, file.toFile());
	}
	
	@Test
	public void testPatternContained() {
		testdata = testdataHandler.getAllTestdata().get(0);
		String exptected  = testdata.getExpectedString();
		String actual = Solution.evaluateHackerrankResultString(testdata.getGrid().contains(testdata.getPattern()));
		assertEquals("Test #" + testdata.getTestNr(), actual, exptected);
	}
	
	@Test
	public void testPatternNotContained() {
		testdata = testdataHandler.getAllTestdata().get(1);
		String exptected  = testdata.getExpectedString();
		String actual = Solution.evaluateHackerrankResultString(testdata.getGrid().contains(testdata.getPattern()));
		assertEquals("Test #" + testdata.getTestNr(), actual, exptected);
	}
	
	@Test
	public void testWrongTestResultInTestdataFile() {
		testdata = testdataHandler.getAllTestdata().get(2);
		String exptected  = testdata.getExpectedString();
		String actual = Solution.evaluateHackerrankResultString(testdata.getGrid().contains(testdata.getPattern()));
		assertNotEquals("Test #" + testdata.getTestNr(), actual, exptected);
	}
}
