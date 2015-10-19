package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.junit.Test;

public class PoolTest {

	@Test
	public void testLearn() throws Exception {

		Path learnBase = Paths.get("/Users/pkern/Google Drive/BOW/learn_and_test/learn");
		Path testBase = Paths.get("/Users/pkern/Google Drive/BOW/learn_and_test/test");
		Pool pool = new Pool();
		
		FileVisitor<Path> fileProcessor = new ProcessFile("txt");
		Files.walkFileTree(learnBase, fileProcessor);
		
		String fileContent;
		for (Path file : ((ProcessFile) fileProcessor).getFiles()) {
			fileContent = new String(Files.readAllBytes(file)); // readAllLines(file, Charset.defaultCharset());
			Document document = new Document();
			document.read(Arrays.asList(fileContent.split("[\\s,\\.;:]")));
			pool.learn(document, file.getParent().getFileName().toString());
		}

		fileProcessor = new ProcessFile("txt");
		Files.walkFileTree(testBase, fileProcessor);
		Float probability;
		List<String> classes = Arrays.asList(new String[] {"clinton",  "lawyer",  "math",  "medical",  "music",  "sex"});
		
		for (String dclass : classes) {
			for (Path file : ((ProcessFile) fileProcessor).getFiles()) {
				fileContent = new String(Files.readAllBytes(file)); // readAllLines(file, Charset.defaultCharset());
				Document document = new Document();
				document.read(Arrays.asList(fileContent.split("\\s\\.,;:")));
				probability = pool.probability(document, dclass);
				Map<String, Float> probAllClasses = pool.getProbabilityForAllDocumentClasses(document);
				System.out.println(dclass + " = " + file.getFileName().toString() + ", Probabilities: " + probAllClasses.entrySet());
			}
		}
		
		
		
	}

	
	
	
	private final class ProcessFile extends SimpleFileVisitor<Path> {
		
		private List<Path> files = new ArrayList<>();
		private final String fileSuffix;
		
		public ProcessFile(String fileSuffix) {
			this.fileSuffix = fileSuffix;
		}
		
		public List<Path> getFiles() {
			return files;
		}
		
		@Override
		public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs)
				throws IOException {
			String fileName = aFile.getFileName().toString();
			if (fileName.substring(fileName.lastIndexOf(".")+1).equals(fileSuffix)) {
				files.add(aFile);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path aDir,
				BasicFileAttributes aAttrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	}
}
