package info.pkern;
/* ============================================================================
 * Copyright (c) 2015 Pascal Kern
 * 
 * http://github.com/PascalKern/Hackerrank_java7
 * http://www.pkern.info/
 * ============================================================================
 */

import static org.junit.Assert.*;
import info.pkern......Solution;
import info.pkern.hackerrank.environment.TestdataHandler;
import info.pkern.hackerrank.tools.LoggerHandler;

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

/**
 * @version 0.1 - (Hackerrank Solution-Environment)
 * @author Pascal Kern
 */
public class ....Test {
	
	private static TestdataHandler<Testdata> testdataHandler;
	private Testdata testdata;
	
	@BeforeClass
	public static void init() throws IOException {
		LoggerHandler.enableSingleLineFineConsoleLogging();
//		LoggerHandler.disableLogging();
		
		Path file = Paths.get("test", DataGridTest.class.getPackage().getName().replace(".", "/"), 
				TestdataHandler.TESTDATA_FILE_NAME);
		assertTrue("The file with the testdata does not exist! [file: " + file.normalize().toAbsolutePath() +"]",
				Files.exists(file, LinkOption.NOFOLLOW_LINKS));
		testdataHandler = new TestdataHandler<>(Testdata.class, file.toFile());
	}
}
