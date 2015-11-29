package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleTableNamedColumnAdapterTest {

	private static SimpleTable<Double> table;
	private static Map<String, Double> newRow;
	private static List<Double> column1 = Arrays.asList(0d,0d,1d);
	private static List<Double> column2 = Arrays.asList(0d,0d,2d);
	private static List<Double> column3 = Arrays.asList(0d,0d,3d);
	private static List<String> header;
	
	@BeforeClass
	public static void initClass() {
		newRow = new HashMap<>();
		newRow.put("one", 1d);
		newRow.put("two", 2d);
		newRow.put("three", 3d);
	}
	
	@Before
	public void init() {
		header = new ArrayList<>();
		header.add("one");
		header.add("two");
		header.add("three");
		table = new SimpleTable<>(3, 2, Double.class);
	}
	
	@Test
	public void testHumanVisible() {
		SimpleTableNamedColumnAdapter<Double> tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
		System.out.println(tableNamed);
		System.out.println(tableNamed.dumpTableWithHeader(3));
	}
	
	
	@Test
	public void testAddColumn() throws Exception {
		SimpleTableNamedColumnAdapter<Double> tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
		List<Double> newColumn = new ArrayList<>();
		newColumn.add(1d);
		try {
			tableNamed.addColumn(newColumn, "four");
			assertEquals(new Integer(4), tableNamed.getColumnsCount());
			System.out.println(tableNamed.dumpTableWithHeader(3));
		} catch (IllegalArgumentException ex) {
			fail(ex.getMessage());
		}
		newColumn.add(2d);
		try {
			tableNamed.addColumn(newColumn, "five");
			assertEquals(new Integer(5), tableNamed.getColumnsCount());
			System.out.println(tableNamed.dumpTableWithHeader(3));
		} catch (IllegalArgumentException ex) {
			fail(ex.getMessage());
		}
		newColumn.add(3d);
		try {
			tableNamed.addColumn(newColumn, "five");
			System.out.println(tableNamed.dumpTableWithHeader(3));
			fail("Should not reach here!");
		} catch (IllegalArgumentException ex) {
			assertEquals("The table already contains a column with this name! [name=five]", ex.getMessage());
		}
	}

	@Test
	public void testRemoveRow() throws Exception {
		SimpleTableNamedColumnAdapter<Double> tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
		tableNamed.removeRow(0);
		assertEquals(new Integer(1), tableNamed.getRowsCount());
		tableNamed.addRow(newRow);
		tableNamed.removeRow(0);
		assertEquals(new Integer(1), tableNamed.getRowsCount());
		assertEquals(newRow, tableNamed.getRow(0));
	}

	@Test
	public void testAddRow() {
		SimpleTableNamedColumnAdapter<Double> tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
		assertEquals(tableNamed.getRowsCount(), new Integer(2));
		tableNamed.addRow(newRow);
		assertEquals(new Integer(3), tableNamed.getRowsCount());
		assertEquals(column1, tableNamed.getColumn("one"));
		assertEquals(column2, tableNamed.getColumn("two"));
		assertEquals(column3, tableNamed.getColumn("three"));
	}
	
	@Test
	public void testRemoveColumn() throws Exception {
		SimpleTableNamedColumnAdapter<Double> tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
		tableNamed.addRow(newRow);
		tableNamed.removeColumn("one");
		Map<String, Double> exptectedRow = new HashMap<>();
		exptectedRow.put("two", 2d);
		exptectedRow.put("three", 3d);
		assertEquals(exptectedRow, tableNamed.getRow(2));
		assertEquals(Arrays.asList(0d,0d,3d), tableNamed.getColumn("three"));
		tableNamed.removeColumn("three");
		tableNamed.removeRow(0);
		assertEquals(new Double(2d), tableNamed.getColumn("two").get(1));
		System.out.println(tableNamed.dumpTableWithHeader(3));
	}

	@Test
	public void testSetColumn() throws Exception {
		SimpleTableNamedColumnAdapter<Double> tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
		tableNamed.addRow(newRow);
		List<Double> newColumn = Arrays.asList(1d,2d,3d);
		tableNamed.setColumn(newColumn, "two");
		assertEquals(new Double(3d), tableNamed.get(2, "two"));
	}

	@Test
	public void testExtendTableColumns() throws Exception {
		SimpleTableNamedColumnAdapter<Double> tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
		tableNamed.addRow(newRow);
		tableNamed.extendTableColumns(Arrays.asList("four", "five"));
		System.out.println(tableNamed.dumpTableWithHeader(3));
		List<String> expectedHeader = Arrays.asList("one", "two", "three", "four", "five");
		List<String> header = new ArrayList<>(tableNamed.getHeader());
		Collections.sort(header);
		Collections.sort(expectedHeader);
		assertEquals(expectedHeader, header);
	}

}
