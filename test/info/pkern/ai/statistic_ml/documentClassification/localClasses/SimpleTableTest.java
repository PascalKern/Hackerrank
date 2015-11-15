package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SimpleTableTest {

	private static SimpleTable table;

	@Test
	public void testExtendTableShape() throws Exception {
		table = new SimpleTable(5, 2);
		assertEquals(new Integer(5), table.getColumnsCount());
		assertEquals(new Integer(2), table.getRowsCount());
		Integer newColumns = 8;
		Integer newRows = 3;
		table.extendTableColumns(newColumns);
		assertEquals(newColumns, table.getColumnsCount());
		table.extendTableRows(newRows);
		assertEquals(newRows, table.getRowsCount());
		newColumns = 10;
		newRows = 5;
		table.extendTableShape(newColumns, newRows);
		assertEquals(newColumns, table.getColumnsCount());
		assertEquals(newRows, table.getRowsCount());
		newRows = 8;
		table.extendTableShape(null, newRows);
		assertEquals(newColumns, table.getColumnsCount());
		assertEquals(newRows, table.getRowsCount());
	}

	@Test
	public void testAddRow() throws Exception {
		table = new SimpleTable(5, 2);
		List<Double> newRow = new ArrayList<>();
		newRow.add(1d);
		newRow.add(2d);
		newRow.add(3d);
		try {
			table.addRow(newRow);
			assertEquals(new Integer(3), table.getRowsCount());
			System.out.println(table.dumpTable(3));
		} catch (IllegalArgumentException ex) {
			fail(ex.getMessage());
		}
		newRow.add(4d);
		newRow.add(5d);
		try {
			table.addRow(newRow);
			assertEquals(new Integer(4), table.getRowsCount());
			System.out.println(table.dumpTable(3));
		} catch (IllegalArgumentException ex) {
			fail(ex.getMessage());
		}
		newRow.add(6d);
		try {
			table.addRow(newRow);
			System.out.println(table.dumpTable(3));
			fail("Should not reach here!");
		} catch (IllegalArgumentException ex) {
			assertEquals("Max elements/columns in row excteded! [Columns: row=6, table=5]", ex.getMessage());
		}
	}

	
	@Test
	public void testAddColumn() throws Exception {
		table = new SimpleTable(5, 2);
		List<Double> newColumn = new ArrayList<>();
		newColumn.add(1d);
		try {
			table.addColumn(newColumn);
			assertEquals(new Integer(6), table.getColumnsCount());
			System.out.println(table.dumpTable(3));
		} catch (IllegalArgumentException ex) {
			fail(ex.getMessage());
		}
		newColumn.add(2d);
		try {
			table.addColumn(newColumn);
			assertEquals(new Integer(7), table.getColumnsCount());
			System.out.println(table.dumpTable(3));
		} catch (IllegalArgumentException ex) {
			fail(ex.getMessage());
		}
		newColumn.add(3d);
		try {
			table.addColumn(newColumn);
			System.out.println(table.dumpTable(3));
			fail("Should not reach here!");
		} catch (IllegalArgumentException ex) {
			assertEquals("Max elements/columns in row excteded! [Rows: column=3, table=2]", ex.getMessage());
		}
	}

	@Test
	public void testRemoveRow() throws Exception {
		table = new SimpleTable(5, 2);
		table.removeRow(0);
		assertEquals(new Integer(1), table.getRowsCount());
		List<Double> newRow = Arrays.asList(1d,2d,3d,4d,5d);
		table.addRow(newRow);
		table.removeRow(0);
		assertEquals(new Integer(1), table.getRowsCount());
		assertEquals(newRow, table.getRow(0));
	}

	@Test
	public void testRemoveColumn() throws Exception {
		table = new SimpleTable(5, 1);
		List<Double> newRow = Arrays.asList(1d,2d,3d,4d,5d);
		table.addRow(newRow);
		table.removeColumn(0);
		assertEquals(newRow.subList(1, newRow.size()), table.getRow(1));
		table.removeColumn(3);
		assertEquals(new Double(2d), table.getRow(1).get(0));
		assertEquals(new Double(4d), table.getRow(1).get(2));
		assertEquals(new Double(3d), table.getColumn(1).get(1));
		System.out.println(table.dumpTable(3));
	}

	@Test
	public void testSetRow() throws Exception {
		table = new SimpleTable(5, 3);
		List<Double> siblingRow = Arrays.asList(0d,0d,0d,0d,0d);
		List<Double> newRow = Arrays.asList(1d,2d,3d,4d,5d);
		table.setRow(newRow, 1);
		assertEquals(siblingRow, table.getRow(0));
		assertEquals(newRow, table.getRow(1));
		assertEquals(siblingRow, table.getRow(2));
	}
	
	@Test
	public void emptyTableInstantiation() {
		table = new SimpleTable(0, 0);
		assertTrue(0 == table.getColumnsCount());
		assertTrue(0 == table.getRowsCount());
		System.out.println(table.dumpTable(3));
		table.extendTableShape(2, 3);
		assertTrue(2 == table.getColumnsCount());
		assertTrue(3 == table.getRowsCount());
		System.out.println(table.dumpTable(3));
	}
}
