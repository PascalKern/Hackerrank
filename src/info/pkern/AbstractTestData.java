package info.pkern;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

/*
 * TODO Better use a interface? Use a factory method to create the instances and force the InputStream as parameter!
 */
public abstract class AbstractTestData {
	
	public abstract <T extends AbstractTestData> T newInstance(Scanner scanner);
	
}
