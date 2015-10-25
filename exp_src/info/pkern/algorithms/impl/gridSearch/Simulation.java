package info.pkern.algorithms.impl.gridSearch;

import info.pkern.TestdataHandler;
import info.pkern.algorithms.impl.gridSearch.localClasses.DataGridTest;
import info.pkern.algorithms.impl.gridSearch.localClasses.Testdata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Simulation {

	public static void main(String[] args) throws IOException {
		Path file = Paths.get("test", DataGridTest.class.getPackage().getName().replace(".", "/"), 
				TestdataHandler.TESTDATA_FILE_NAME);
		TestdataHandler<Testdata> testdataHandler = new TestdataHandler<>(Testdata.class, file.toFile());
		
		InputStream oldSystemIn = System.in;
		InputStream simulatedSystemIn = new ByteArrayInputStream(testdataHandler.getTestdataForSimulation().getBytes(Charset.defaultCharset()));
		
		
		Simulator simulator = new Simulator();
		
		System.setIn(simulatedSystemIn);

		Thread runnerT = new Thread(simulator);
		runnerT.start();
		
		simulator.simulate();
		
		System.setIn(oldSystemIn);
		
		System.exit(0);
	}
	
	
	static class Simulator implements Runnable {
		
		public void simulate() {
			Solution.main(new String[]{});
		}
		
		@Override
		public void run() {
			while (true){
				/**/
			}
		}
	}
}
