package info.pkern.sandbox.ai.statistic_ml.documentClassification.localClasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import org.math.plot.FrameView;
import org.math.plot.Plot2DPanel;
import org.math.plot.plots.ColoredScatterPlot;

import com.jujutsu.tsne.FastTSne;
import com.jujutsu.tsne.MatrixOps;
import com.jujutsu.tsne.TSne;
import com.jujutsu.tsne.demos.TSneDemo;
import com.jujutsu.utils.MatrixUtils;

public class TsnePlotExample {

	private double perplexity = 20.0;
	private int initialDims = 50;
	
	private List<String> labels = new ArrayList<>();
	private List<double[]> vertices = new ArrayList<>();
	
	public TsnePlotExample() {
		this(50);
	}
	
	public TsnePlotExample(int initialDims) {
		this.initialDims = initialDims;
	}
	
	public void addVector(String label, double[] vector) {
		labels.add(label.trim());
		int isLength = 0;
		if (!vertices.isEmpty() && (isLength = vertices.get(0).length) != vector.length) {
			throw new IllegalArgumentException("The vectors to plot must have the same length! [existing="+isLength
					+ ", vector= "+vector.length+"]");
		}
		vertices.add(vector);
	}
	
	public void addVertices(List<String> labels, List<double[]> vertices) {
		if (labels.size() != vertices.size()) {
			throw new IllegalArgumentException("Count of labels and vertices must be equal! [counts: "
					+ "labels="+labels.size()+", vertices="+vertices.size()+"]");
		}
		for (int i = 0; i < labels.size(); i++) {
			addVector(labels.get(i), vertices.get(i));
		}
	}
	
	public void addVertices(String label, List<double[]> vertices) {
		for (double[] vector : vertices) {
			addVector(label, vector);
		}
	}
	
	public void display() {
		TSne tsne = new FastTSne();
    	int iters = 1000;
    	
    	double[][] vertices = new double[this.vertices.size()][];
    	for (int i = 0; i < this.vertices.size(); i++) {
			vertices[i] = this.vertices.get(i);
		}
        System.out.println("Shape is: " + vertices.length + " x " + vertices[0].length);
        System.out.println("Starting TSNE: " + new Date());
        double [][] tsneResult = tsne.tsne(vertices, 2, initialDims, perplexity, iters);
        System.out.println("Finished TSNE: " + new Date());
        System.out.println("Result is = " + tsneResult.length + " x " + tsneResult[0].length);
        saveFile(new File("./noGit/Java-tsne-result.txt"), MatrixOps.doubleArrayToString(tsneResult), false);
        
        Plot2DPanel plot = new Plot2DPanel();

        String[] labels = new String[this.labels.size()];
        for (int i = 0; i < this.labels.size(); i++) {
			labels[i] = this.labels.get(i);
		}
        ColoredScatterPlot setosaPlot = new ColoredScatterPlot("setosa", tsneResult, labels);
        plot.plotCanvas.setNotable(true);
        plot.plotCanvas.setNoteCoords(true);
        plot.plotCanvas.addPlot(setosaPlot);
                
        FrameView plotframe = new FrameView(plot);
        plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plotframe.setVisible(true);
	}

	public void saveFile(File file, String text, boolean append) {
        try (FileWriter fw = new FileWriter(file, append);
            BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(text);
//            bw.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

	public static void main(String[] args) {
			TSneDemo.main(new String[]{"./noGit/example.txt", "./noGit/labels_example.txt"});
//			TsnePlotExample plotter = new TsnePlotExample();
//			plotter.display("./noGit/example.txt", "./noGit/labels_example.txt");
		}
}
