package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleTableNamedColumnAdapterTest {

	private static SimpleTableNamedColumnAdapter<Double> tableNamed;
	private static SimpleTable<Double> table;
	private static Map<String, Double> newRow;
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
		tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
	}
	
	@Test
	public void testHumanVisible() {
		tableNamed = new SimpleTableNamedColumnAdapter<>(table, header);
		System.out.println(tableNamed);
		System.out.println(tableNamed.dumpTableWithHeader(3));
	}
	
//	@Test
//	public void testAddRow() throws Exception {
//		tableNamed = new SimpleTableNamedColumnAdapter(table, Arrays.asList("one","two","three"));
//		List<Double> newRow = new ArrayList<>();
//		newRow.add(1d);
//		newRow.add(2d);
//		newRow.add(3d);
//		try {
//			tableNamed.addRow(newRow);
//			assertEquals(new Integer(3), tableNamed.getRowsCount());
//			System.out.println(tableNamed.dumpTable(3));
//		} catch (IllegalArgumentException ex) {
//			fail(ex.getMessage());
//		}
//		newRow.add(4d);
//		newRow.add(5d);
//		try {
//			tableNamed.addRow(newRow);
//			assertEquals(new Integer(4), tableNamed.getRowsCount());
//			System.out.println(tableNamed.dumpTable(3));
//		} catch (IllegalArgumentException ex) {
//			fail(ex.getMessage());
//		}
//		newRow.add(6d);
//		try {
//			tableNamed.addRow(newRow);
//			System.out.println(tableNamed.dumpTable(3));
//			fail("Should not reach here!");
//		} catch (IllegalArgumentException ex) {
//			assertEquals("Max elements/columns in row excteded! [Columns: row=6, table=5]", ex.getMessage());
//		}
//	}

	
	@Test
	public void testAddColumn() throws Exception {
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
		tableNamed.removeRow(0);
		assertEquals(new Integer(1), tableNamed.getRowsCount());
		tableNamed.addRow(newRow);
		tableNamed.removeRow(0);
		assertEquals(new Integer(1), tableNamed.getRowsCount());
		assertEquals(newRow, tableNamed.getRow(0));
	}

	@Test
	public void testRemoveColumn() throws Exception {
		System.out.println(tableNamed.dumpTableWithHeader(3));
		tableNamed.addRow(newRow);
		System.out.println(tableNamed.dumpTableWithHeader(3));
		tableNamed.removeColumn("one");
		System.out.println(tableNamed.dumpTableWithHeader(3));
		Map<String, Double> exptected = new HashMap<>();
		exptected.put("two", 2d);
		exptected.put("three", 3d);
		assertEquals(exptected, tableNamed.getRow(2));
		assertEquals(Arrays.asList(0d,0d,3d), tableNamed.getColumn("three"));
		tableNamed.removeColumn("three");
		tableNamed.removeRow(0);
		assertEquals(new Double(2d), tableNamed.getColumn("two").get(1));
		System.out.println(tableNamed.dumpTableWithHeader(3));
	}

	@Test
	public void testSetColumn() throws Exception {
		tableNamed.addRow(newRow);
		List<Double> newColumn = Arrays.asList(1d,2d,3d);
		tableNamed.setColumn(newColumn, "two");
		assertEquals(new Double(3d), tableNamed.get(2, "two"));
	}

}
