package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.commons.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	
	public SimpleTableNamedColumnAdapter(Class<T> type) {
		this(new SimpleTable<T>(0, 0, type), new ArrayList<String>());
	}
	
	public SimpleTableNamedColumnAdapter(SimpleTable<T> table, List<String> columnNames) {
		if (table.getColumnsCount() != columnNames.size()) {
			throw new IllegalArgumentException("The table and the given column names must have the same count!"
					+ "[table="+table.getColumnsCount()+", columnNames="+columnNames.size()+"]");
		}
		this.table = table.copy();
		List<String> header = new ArrayList<>();
		header.addAll(columnNames);
		this.header = header;
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
	
	private Integer removeIndexFor(String name) {
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
			throw new IllegalArgumentException("One or more column names are not yet in this table! Extend the table "
					+ "first with extendTableColumns(). [new names="+names+"]");
		}
		List<T> orderedRow = new ArrayList<>();
		ListIterator<String> headerIter = header.listIterator();
		while (headerIter.hasNext()) {
			String name = headerIter.next();
			T value = row.get(name);
			if (null == value) {
				value = NumberUtil.<T>zeroValueCreator(table.getType());
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
		return table.removeColumn(removeIndexFor(name));
	}

	/**
	 * Should return a new {@link SimpleTableNamedColumnAdapter} with only the given columns.
	 * @param columnNames
	 * @return
	 */
	//TODO TEST!!! Was with List<String> before! Order doesn't matter? Yes if one will track the old index/position outside this class!
	public SimpleTableNamedColumnAdapter<T> filterColumns(Collection<String> columnNames) {
		SimpleTableNamedColumnAdapter<T> copy = copy();
		List<String> toRemove = new ArrayList<>();
		toRemove.addAll(copy.header);
		toRemove.removeAll(columnNames);
		copy.removeColumns(toRemove);
		return copy;
	}
	
	/**
	 * Removes the given columns and return's them!
	 * @param columnNames
	 * @return
	 */
	//TODO TEST!!! ALso with Collection as above?! Order doesn't matter? Yes? if one will track the old index/position outside this class!
	public SimpleTableNamedColumnAdapter<T> removeColumns(List<String> columnNames) {
		List<Integer> indicesToRemove = new ArrayList<>();
		for (String name : columnNames) {
			indicesToRemove.add(removeIndexFor(name));
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
		return new Integer(table.getRowsCount());
	}

	public Integer getColumnsCount() {
		return new Integer(table.getColumnsCount());
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
		table.extendTabltColumnsBy(newNames.size());
	}

	public SimpleTable<T> getSimpleTable() {
		return table.copy();
	}
	
	public Collection<String> getHeader() {
		List<String> headerCopy = new ArrayList<>();
		headerCopy.addAll(header);
		return headerCopy;
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
		return sbHeader.append(System.lineSeparator()).append(table.dumpTable(precision, fieldWith));
	}
	
	@Override
	public String toString() {
		return table.toString() + System.lineSeparator() + "header= " + header;
	}

	public SimpleTableNamedColumnAdapter<T> copy() {
		return new SimpleTableNamedColumnAdapter<>(table, header);
	}
}
