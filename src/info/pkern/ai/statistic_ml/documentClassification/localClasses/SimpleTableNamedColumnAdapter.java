package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class SimpleTableNamedColumnAdapter<T extends Number> {

	private SimpleTable<T> table;
	private List<String> header;
	private Integer maxHeaderNameLength = 0;
	
	public SimpleTableNamedColumnAdapter() {
		this(new SimpleTable<T>(0, 0), new ArrayList<String>());
	}
	
	public SimpleTableNamedColumnAdapter(SimpleTable<T> table, List<String> columnNames) {
		if (table.getColumnsCount() != columnNames.size()) {
			throw new IllegalArgumentException("The table and the given column names must have the same count!"
					+ "[table="+table.getColumnsCount()+", columnNames="+columnNames.size()+"]");
		}
		this.table = table;
		this.header = columnNames;
		for (String name : columnNames) {
			setMaxHeaderNameLength(name);
		}
	}
	
	
	private Integer addToHeaderAndGetIndex(String name) {
		checkColumnNotExists(name);
		if (! header.contains(name)) {
			header.add(name);
			setMaxHeaderNameLength(name);
		}
		return getIndexOf(name);
	}
	
	private Integer getIndexOf(String name) {
		checkColumnExists(name);
		return header.indexOf(name);
	}
	
	private Integer removeIndexOf(String name) {
		Integer index = getIndexOf(name);
		header.remove((int)index);
		return index;
	}
	
	private void setMaxHeaderNameLength(String name) {
		maxHeaderNameLength = Math.max(maxHeaderNameLength, name.length());
	}
	
	public void addRow(Map<String, T> row) {
		Set<String> names = new HashSet<>(row.keySet());
		names.removeAll(header);
		if (! names.isEmpty()) {
			throw new IllegalArgumentException("One or more column names are not yet in this table! [new names="+names+"]");
		}
		List<T> orderedRow = new ArrayList<>();
		ListIterator<String> headerIter = header.listIterator();
		while (headerIter.hasNext()) {
			String name = headerIter.next();
			T value = row.get(name);
			if (null == value) {
				//TODO Write numberZeroExtractor(T) -> returns 0 for the given type T
//				value = 0d;
				value = null;
			}
			orderedRow.add(value);
		}
		table.addRow(orderedRow);
	}

	public Map<String, T> getRow(Integer rowIndex) {
		Map<String, T> newRow = new HashMap<>();
		List<T> valueRow = table.getRow(rowIndex); 
		for (int i = 0; i < header.size(); i++) {
			newRow.put(header.get(i), valueRow.get(i));
		}
		return newRow;
	}

	public List<T> removeRow(Integer rowIndex) {
		return table.removeRow(rowIndex);
	}

	public void addColumn(List<T> column, String name) {
		table.addColumn(column, addToHeaderAndGetIndex(name));
	}

	public List<T> setColumn(List<T> column, String name) {
		return table.setColumn(column, getIndexOf(name));
	}

	public List<T> getColumn(String name) {
		return table.getColumn(getIndexOf(name));
	}

	public List<T> removeColumn(String name) {
		return table.removeColumn(removeIndexOf(name));
	}

	public SimpleTableNamedColumnAdapter<T> removeColumns(List<String> columnNames) {
		List<Integer> indicesToRemove = new ArrayList<>();
		for (String name : columnNames) {
			indicesToRemove.add(removeIndexOf(name));
		}
		SimpleTable<T> removedTable = table.removeColumns(indicesToRemove); 
		return new SimpleTableNamedColumnAdapter<T>(removedTable, columnNames);
	}

	public T get(Integer row, String columnName) {
		return table.get(row, getIndexOf(columnName));
	}

	public void set(Integer row, String columnName, T value) {
		table.set(row, getIndexOf(columnName), value);
	}

	public Integer getRowsCount() {
		return table.getRowsCount();
	}

	public Integer getColumnsCount() {
		return table.getColumnsCount();
	}

	public void extendTableRows(Integer newRowCount) {
		table.extendTableRows(newRowCount);
	}

	public void extendTableColumns(Collection<String> headerNames) {
		List<String> newNames = new ArrayList<>();
		newNames.addAll(headerNames);
		newNames.removeAll(header);
		for (String name : newNames) {
			addToHeaderAndGetIndex(name);
		}
		table.extendTableColumns(newNames.size());
	}

	public SimpleTable getSimpleTable() {
		return table.copy();
	}
	
	private void checkColumnExists(String name) {
		if (! header.contains(name)) {
			throw new IllegalArgumentException("The table does not contain a column with this name! [name="+name+"]");
		}
	}

	private void checkColumnNotExists(String name) {
		if (header.contains(name)) {
			throw new IllegalArgumentException("The table already contains a column with this name! [name="+name+"]");
		}
	}

	public StringBuilder dumpTableWithHeader(Integer precision) {
		StringBuilder sbHeader = new StringBuilder();
		Integer fieldWith = Math.max((6+precision), maxHeaderNameLength);
		for (String term : header) {
			sbHeader.append(String.format("%-"+ fieldWith +"s", term)).append(", ");
		}
		sbHeader.delete(sbHeader.lastIndexOf(","),sbHeader.length());
		return sbHeader.append(System.lineSeparator()).append(table.dumpTable(precision));
	}
	
	@Override
	public String toString() {
		return table.toString();
	}
}
