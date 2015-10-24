package info.pkern.tools;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class RecursiveSimpleFileVisitor extends SimpleFileVisitor<Path> {

	private List<Path> files = new ArrayList<>();
	private final String fileSuffix;
	
	public RecursiveSimpleFileVisitor(String fileSuffix) {
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
