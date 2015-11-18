package info.pkern.sandbox;

import info.pkern.sandbox.ai.statistic_ml.documentClassification.localClasses.TsnePlotExample;

public class TSNEExperiment {

	
	public static void main(String[] args) {
		double[] rowA = new double[]{0.1d,0.5d,0.75d,1d,1.1d};
		double[] rowB = new double[]{0.1d,0.5d,0.75d,1d,1.1d};
		double[] rowB2 = new double[]{0.75d,1d,1.1d,0.1d,0.5d};
		double[] rowC = new double[]{0.1d,0.5d,0.75d,1d,1.1d};
		double[] rowC2 = new double[]{1.1d,0.1d,0.5d,0.75d,1d};
		
		
		TsnePlotExample plotter = new TsnePlotExample(3);
		plotter.addVector("A", rowA);
		plotter.addVector("A", rowB);
		plotter.addVector("A", rowC);
		plotter.display();
		
		plotter = new TsnePlotExample(3);
		plotter.addVector("A", rowA);
		plotter.addVector("B", rowB);
		plotter.addVector("C", rowC);
		plotter.display();
		
		plotter = new TsnePlotExample(3);
		plotter.addVector("A", rowA);
		plotter.addVector("A", rowB2);
		plotter.addVector("A", rowC2);
		plotter.display();

		plotter = new TsnePlotExample(3);
		plotter.addVector("A", rowA);
		plotter.addVector("B2", rowB2);
		plotter.addVector("C2", rowC2);
		plotter.display();
		
	}
	
	
}
