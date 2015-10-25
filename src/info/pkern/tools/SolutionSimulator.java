package info.pkern.tools;

import java.io.InputStream;
import java.lang.reflect.Method;

public class SolutionSimulator implements Runnable {

	private InputStream oldSystemIn = System.in;
	private boolean simulationRunning = false;
	private Object solution;
	
	public SolutionSimulator(Object solution, InputStream in) {
		System.setIn(in);
		this.solution = solution;
	}
	
	public void simulate() {
		try {
			Thread runnerT = new Thread(this);
			runnerT.start();
			
			Method solutionMain = solution.getClass().getMethod("main", String[].class);
			solutionMain.invoke(solution, (Object) new String[]{});
			
			System.setIn(oldSystemIn);
		} catch (Exception ex) {
			System.err.println("Simulation failed! Reason:");
			ex.printStackTrace();
		} finally {
			System.setIn(oldSystemIn);
			stop();
			System.exit(0);
		}
	}
	
	public void stop() {
		simulationRunning = false;
		System.out.println("Simulation for: " + solution.getClass().getCanonicalName() + " stopped!");
	}
	
	@Override
	public void run() {
		simulationRunning = true;
		System.out.println("Simulation for: " + solution.getClass().getCanonicalName() + " started...");
		while (simulationRunning){
			/**/
		}
	}
}
