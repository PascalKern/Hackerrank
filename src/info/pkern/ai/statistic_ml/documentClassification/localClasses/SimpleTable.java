package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.commons.NumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Row and column indices are always <strong>zero based</strong>!</br></br>
 * 
 * @author pkern
 *
 */
public class SimpleTable<T extends Number> {

	private List<List<T>> table = new ArrayList<>();
	private Integer rows = 0;
	private Integer columns = 0;
	
	private Class<T> type;
	
	public SimpleTable(Class<T> type) {
		this(0,0, type);
	}
	
	public SimpleTable(Integer initColumnSize, Integer initRowSize, Class<T> type) {
		this.type = type;
		List<T> baseRow = NumberUtil.populateZeroedList(initColumnSize, type);
		for (int i = 0; i < initRowSize; i++) {
			List<T> row = new ArrayList<>();
			row.addAll(baseRow);
			table.add(row);
		}
		rows = initRowSize;
		columns = initColumnSize;
	}
	
	public Integer addRow(List<T> row) {
		return addRow(row, rows);
	}
	
	public Integer addRow(List<T> row, Integer index) {
		checkColumnsInRow(row);
		checkTableRowSize(index);
		List<T> newRow = NumberUtil.extendListWithZeroToFixElementCount(row, columns);
		table.add(index, newRow);
		rows++;
		return table.size();
	}

	public List<T> setRow(List<T> row, Integer index) {
		checkColumnsInRow(row);
		List<T> oldRow = removeRow(index);
		addRow(row, index);
		return oldRow;
	}
	
	public List<T> getRow(Integer rowIndex) {
		checkTableRowSize(rowIndex);
		return table.get(rowIndex);
	}
	
	public List<T> removeRow(Integer rowIndex) {
		checkTableRowSize(rowIndex);
		List<T> oldRow = table.remove((int)rowIndex);
		rows--;
		return oldRow;
	}
	
	public void addColumn(List<T> column) {
		addColumn(column, columns);
	}

	public void addColumn(List<T> column, Integer index) {
		checkRowsInColumn(column);
		checkTableColumnSize(index);
		List<T> newColumn = NumberUtil.extendListWithZeroToFixElementCount(column, rows);
		for (int i = 0; i < newColumn.size(); i++) {
			getRow(i).add(index, newColumn.get(i));
		}
		columns++;
	}
	
	public List<T> setColumn(List<T> column, Integer index) {
		checkRowsInColumn(column);
		List<T> oldColumn = removeColumn(index);
		addColumn(column, index);
		return oldColumn;
	}
	
	public List<T> getColumn(Integer columnIndex) {
		checkTableColumnSize(columnIndex);
		List<T> column = new ArrayList<>();
		for (int i = 0; i < table.size(); i++) {
			column.add(get(i, columnIndex));
		}
		return column;
	}
	
	public List<T> removeColumn(Integer columnIndex) {
		checkTableColumnSize(columnIndex);
		List<T> oldColumn = new ArrayList<>();
		for (List<T> row : table) {
			oldColumn.add(row.remove((int)columnIndex));
		}
		columns--;
		return oldColumn;
	}
	
	/**
	 * Should return return all columns not in the given list. Means keep all columns in the list if they exist 
	 * in this table. There is no information about the order of the remaining columns.
	 * @param columnIndices
	 * @return
	 */
	public SimpleTable<T> filterColumns(List<Integer> columnIndices) {
		throw new RuntimeException("Not yet implemented!");
	}
	
	/**
	 * Removes all given columns from this table and return the "deleted" columns as a new table. The order of the
	 * columns in the returned table is the same as within the given list of indices. 
	 * @param columnIndices
	 * @return
	 */
	public SimpleTable<T> removeColumns(List<Integer> columnIndices) {
		checkTableColumnSize(columnIndices);
		List<List<T>> removedColumns = new ArrayList<>();
		for (List<T> row : this.table) {
			List<T> removedElements = new ArrayList<>();
			for (Integer column : columnIndices) {
				removedElements.add(row.remove((int)column));
			}
		}
		SimpleTable<T> table = copy();
		table.table = removedColumns;
		table.columns = columnIndices.size();
		table.rows = rows;

		columns -= columnIndices.size();
		return table;
	}

	public T get(Integer row, Integer column) {
		checkTableSize(row, column);
		return table.get(row).get(column);
	}
	
	public void set(Integer row, Integer column, T value) {
		checkTableSize(row, column);
		table.get(row).set(column, value);
	}
	
	public Integer getRowsCount() {
		return rows;
	}
	
	public Integer getColumnsCount() {
		return columns;
	}
	
	public void extendTableToShape(Integer newColumnCount, Integer newRowCount) {
		if (null != newColumnCount) {
			extendTableToColumnsCount(newColumnCount);
		}
		if (null != newRowCount) {
			extendTableRows(newRowCount);
		}
	}

	public void extendTableRows(Integer newRowCount) {
		if (newRowCount > rows) {
			List<T> zeroedRow = NumberUtil.populateZeroedList(columns, type);
			while (rows < newRowCount) {
				List<T> newRow = new ArrayList<>();
				newRow.addAll(zeroedRow);
				addRow(newRow);
			}
		}
	}

	public void extendTabltColumnsBy(Integer additionalColumnsCount) {
		extendTableToColumnsCount(additionalColumnsCount + columns);
	}
	
	public void extendTableToColumnsCount(Integer newColumnCount) {
		if (newColumnCount > columns) {
			List<T> zeroedColumn = NumberUtil.populateZeroedList(rows, type);
			while (columns < newColumnCount) {
				List<T> newColumn = new ArrayList<>();
				newColumn.addAll(zeroedColumn);
				addColumn(newColumn);
			}
		}
	}
	
	private void checkTableSize(Integer row, Integer column) {
		checkTableRowSize(row);
		checkTableColumnSize(column);
	}

	private void checkTableRowSize(Integer rowIndex) {
		if (null == rowIndex || rowIndex > rows) {
			throw new IllegalArgumentException("Specified row index exceeds the table! "
					+ "[rows=" + rows + ", row=" + rowIndex + "]");
		}
	}

	private void checkTableColumnSize(Integer columnIndex) {
		if (null == columnIndex || columnIndex > columns) {
			throw new IllegalArgumentException("Specified column index exceeds the table! "
					+ "[columns=" + columns + ", column=" + columnIndex + "]");
		}
	}
	
	private void checkTableColumnSize(List<Integer> columnIndices) {
		Throwable throwable = new Throwable();
		for (Integer index : columnIndices) {
			try {
				checkTableColumnSize(index);
			} catch (IllegalArgumentException ex) {
				throwable.addSuppressed(ex);
			}
		}
		if (throwable.getSuppressed().length > 0) {
			throw new IllegalArgumentException("One or more of the specified indices are impractical!", throwable);
		}
	}

	private void checkColumnsInRow(List<T> row) {
		if (row.size() > columns) {
			throw new IllegalArgumentException("Max elements/columns in row excteded! [Columns: row="
					+ row.size()+", table="+columns+"]");
		}
	}

	private void checkRowsInColumn(List<T> column) {
		if (column.size() > rows) {
			throw new IllegalArgumentException("Max elements/columns in row excteded! [Rows: column="
					+ column.size()+", table="+rows+"]");
		}
	}
	
	//Could use the StringArrayNumberConverter#joinWithSeparator() but this introduces a new dependency!
	private StringBuilder formatTableRow(List<T> row, Integer precision, Integer fieldWith) {
		String[] formatedRow = NumberUtil.toFormatedStringArray(row, precision, fieldWith);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < formatedRow.length; i++) {
			sb.append(formatedRow[i]).append(", ");
		}
		sb.delete(sb.lastIndexOf(", "), sb.length());
		return sb;
	}
	
	public StringBuilder dumpTable(Integer precision) {
		return dumpTable(precision, null);
	}
	
	public StringBuilder dumpTable(Integer precision, Integer fieldWith) {
		StringBuilder sb = new StringBuilder();
		for (List<T> row : table) {
			sb.append(formatTableRow(row, precision, fieldWith)).append(System.lineSeparator());
		}
		return sb;
	}
	
	@Override
	public String toString() {
		return "Shape: columns=" + columns + ", rows=" + rows + ", type=" + type;
	}

	public SimpleTable<T> copy() {
		SimpleTable<T> copy = new SimpleTable<>(type);
		copy.columns = columns;
		copy.rows = rows;
		copy.table = new ArrayList<List<T>>();
		for (List<T> row : table) {
			List<T> newRow = new ArrayList<>();
			newRow.addAll(row);
			copy.table.add(newRow);
		}
		return copy;
	}

	public Class<T> getType() {
		return type;
	}

	public List<List<T>> getContent() {
		return copy().table;
	}
}
