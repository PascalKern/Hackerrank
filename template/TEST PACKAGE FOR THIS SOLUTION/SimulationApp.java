package info.pkern.algorithms.impl.gridSearch;
/* ============================================================================
 * Copyright (c) 2015 Pascal Kern
 * 
 * http://github.com/PascalKern/Hackerrank_java7
 * http://www.pkern.info/
 * ============================================================================
 */

import info.pkern.hackerrank.environment.TestdataHandler;
import info.pkern.hackerrank.tools.SolutionSimulator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @version 0.1 - (Hackerrank Solution-Environment)
 * @author Pascal Kern
 */
public class SimulationApp {

	public static void main(String[] args) throws IOException {
		Path file = Paths.get("test", .....class.getPackage().getName().replace(".", "/"), 
				TestdataHandler.TESTDATA_FILE_NAME);
		TestdataHandler<Testdata> testdataHandler = new TestdataHandler<>(Testdata.class, file.toFile());
		
		InputStream simulatedSystemIn = new ByteArrayInputStream(testdataHandler.getTestdataForSimulation().getBytes());
		SolutionSimulator simulator = new SolutionSimulator(new Solution(), simulatedSystemIn);
		simulator.simulate();
	}
}
