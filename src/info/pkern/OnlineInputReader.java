package info.pkern;

import java.util.Arrays;
import java.util.List;

public class OnlineInputReader {

	private final List<String> inputs;
	private final Integer inputElementSize;
	private final Integer headerSize;

	/*
	 * TODO Maybe create (interface) a "node" class which reads from a InputStream some fields. One to be used as "header" and 
	 * the others to be used as value nodes!
	 */
	public OnlineInputReader(String[] args, int inputElementSize, int headerSize) {
		inputs = Arrays.asList(args);
		this.inputElementSize = inputElementSize;
		this.headerSize = headerSize;
	}
	
	public Object getFirstInputLine() {
		return inputs.get(0);
	}
	
	public List<String> getHeader() {
		return inputs.subList(0, headerSize);
	}
	
	public List<String> getInputElement(int index) {
		return inputs.subList(headerSize + (index*inputElementSize), inputElementSize);
	}
}
