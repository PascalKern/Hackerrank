package info.pkern.algorithms.impl.gridSearch;

import info.pkern.algorithms.impl.gridSearch.Solution;
import info.pkern.algorithms.impl.gridSearch.localClasses.DataGridTest;
import info.pkern.algorithms.impl.gridSearch.localClasses.Testdata;
import info.pkern.hackerrank.environment.SolutionSimulator;
import info.pkern.hackerrank.environment.TestdataHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimulationApp {

	public static void main(String[] args) throws IOException {
		Path file = Paths.get("test", DataGridTest.class.getPackage().getName().replace(".", "/"), 
				TestdataHandler.TESTDATA_FILE_NAME);
		TestdataHandler<Testdata> testdataHandler = new TestdataHandler<>(Testdata.class, file.toFile());
		
		InputStream simulatedSystemIn = new ByteArrayInputStream(testdataHandler.getTestdataForSimulation().getBytes());
		SolutionSimulator simulator = new SolutionSimulator(new Solution(), simulatedSystemIn);
		simulator.simulate();
	}
}
