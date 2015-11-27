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
//	private List<String> header;
	private Integer maxHeaderNameLength = 0;

	private int lastIndex = 0;
	private Map<String, Integer> headerToIndex;
	private Map<Integer, String> indexToHeader;
	
	public SimpleTableNamedColumnAdapter(Class<T> type) {
		this(new SimpleTable<T>(0, 0, type), new ArrayList<String>());
	}
	
	public SimpleTableNamedColumnAdapter(SimpleTable<T> table, List<String> columnNames) {
		if (table.getColumnsCount() != columnNames.size()) {
			throw new IllegalArgumentException("The table and the given column names must have the same count!"
					+ "[table="+table.getColumnsCount()+", columnNames="+columnNames.size()+"]");
		}
		this.table = table.copy();
		headerToIndex = new HashMap<>();
		indexToHeader = new HashMap<>();
		for (int i = 0; i < columnNames.size(); i++) {
			String name = columnNames.get(i);
			headerToIndex.put(name, i);
			indexToHeader.put(i, name);
			lastIndex = i;
			setMaxHeaderNameLength(name);
		}
	}
	
	private Integer addToHeaderAndOrGetIndex(String name) {
		Integer index = headerToIndex.get(name);
		if (null == index) {
			index = lastIndex + 1;
			headerToIndex.put(name, index);
			indexToHeader.put(index, name);
			lastIndex = index;
		}
		return index;
	}
	
	private String getHeaderFor(Integer index) {
		checkIndexExists(index);
		return indexToHeader.get(index);
	}
	
	private Integer getIndexOf(String name) {
		checkColumnExists(name);
		return headerToIndex.get(name);
	}
	
	private Integer removeIndexFor(String name) {
		Integer indexToRemove = headerToIndex.remove(name);
		indexToHeader.remove(indexToRemove);
		int index = indexToRemove + 1;
		while (lastIndex >= index) {
			String nameToMove = indexToHeader.remove(index); 
			indexToHeader.put(index-1, nameToMove);
			headerToIndex.put(nameToMove, index-1);
			index++;
		};
		lastIndex--;
		return indexToRemove;
	}
	
	private void setMaxHeaderNameLength(String name) {
		maxHeaderNameLength = Math.max(maxHeaderNameLength, name.length());
	}
	
	public void addRow(Map<String, T> row) {
		if (!headerToIndex.keySet().containsAll(row.keySet())) {
			Set<String> different = new HashSet<>(row.keySet());
			different.removeAll(headerToIndex.keySet());	//Expensive operation but because to be thrown not a issue! 
			throw new IllegalArgumentException("One or more column names are not yet in this table! Extend the table "
					+ "first with extendTableColumns() or use addRowAutoExtendTable() instead. "
					+ "[differneces="+different+"]");
		}
		List<T> orderedRow = new ArrayList<>();
		int columnCounter = 0;
		while (columnCounter <= lastIndex) {
			String name = indexToHeader.get(columnCounter);
			T value = row.get(name);
			if (null == value) {
				value = NumberUtil.<T>zeroValueCreator(table.getType());
			}
			orderedRow.add(value);
			columnCounter++;
		}
		table.addRow(orderedRow);
	}
	
	public Map<String, T> getRow(Integer rowIndex) {
		Map<String, T> newRow = new HashMap<>();
		List<T> valueRow = table.getRow(rowIndex);
		for (int i = 0; i <= lastIndex; i++) {			
			newRow.put(indexToHeader.get(i), valueRow.get(i));
		}
		return newRow;
	}

	public List<T> removeRow(Integer rowIndex) {
		return table.removeRow(rowIndex);
	}

	public void addColumn(List<T> column, String name) {
		checkColumnNotExists(name);
		table.addColumn(column, addToHeaderAndOrGetIndex(name));
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
	 * Filters the table by removing all columns from this table which where not in the given columns. The removed<br/>
	 * are returned as a new {@link SimpleTableNamedColumnAdapter} with only the given columns.
	 * @param columnNames
	 * @return
	 */
	public SimpleTableNamedColumnAdapter<T> filterColumns(Collection<String> columnNames) {
		SimpleTableNamedColumnAdapter<T> copy = copy();
		Set<String> toRemove = new HashSet<>(headerToIndex.keySet());
		toRemove.removeAll(columnNames);
		copy.removeColumns(toRemove);
		return copy;
	}
	
	/**
	 * Removes the given columns and return's them!
	 * @param columnNames
	 * @return
	 */
	public SimpleTableNamedColumnAdapter<T> removeColumns(Collection<String> columnNames) {
		List<Integer> indicesToRemove = new ArrayList<>();
		List<String> removedColumnHeaders = new ArrayList<>();
		for (String name : columnNames) {
			removedColumnHeaders.add(name);
			indicesToRemove.add(removeIndexFor(name));
		}
		SimpleTable<T> removedTable = table.removeColumns(indicesToRemove); 
		return new SimpleTableNamedColumnAdapter<T>(removedTable, removedColumnHeaders);
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

	//TODO Ok to start by 1?!?
	public void extendTableColumns(Collection<String> headerNames) {
		int newColumnCounter = 1;
		for (String name : headerNames) {
			newColumnCounter += (null == headerToIndex.get(name))?1:0;
			addToHeaderAndOrGetIndex(name);
		}
		table.extendTabltColumnsBy(newColumnCounter);
	}

	public SimpleTable<T> getSimpleTable() {
		return table.copy();
	}
	
	public Collection<String> getHeader() {
		return new HashSet<>(headerToIndex.keySet());
	}

	private void checkColumnExists(String name) {
		if (null == headerToIndex.get(name)) {
			throw new IllegalArgumentException("The table does not contain a column with this name! [name="+name+"]");
		}
	}

	private void checkIndexExists(Integer index) {
		if (null == indexToHeader.get(index)) {
			throw new IllegalArgumentException("The table does not contain a column for the index! [index="+index+"]");
		}
	}
	
	private void checkColumnNotExists(String name) {
		if (null != headerToIndex.get(name)) {
			throw new IllegalArgumentException("The table already contains a column with this name! [name="+name+"]");
		}
	}

	public StringBuilder dumpTableWithHeader(Integer precision) {
		StringBuilder sbHeader = new StringBuilder();
		Integer fieldWith = Math.max((6+precision), maxHeaderNameLength);
		for (int i = 0; i <= lastIndex; i++) {
			String term = indexToHeader.get(i);
			sbHeader.append(String.format("%-"+ fieldWith +"s", term)).append(", ");
		}
		sbHeader.delete(sbHeader.lastIndexOf(","),sbHeader.length());
		return sbHeader.append(System.lineSeparator()).append(table.dumpTable(precision, fieldWith));
	}
	
	@Override
	public String toString() {
		return table.toString() + System.lineSeparator() + "headerToIndex= " + headerToIndex;
	}

	public SimpleTableNamedColumnAdapter<T> copy() {
		SimpleTableNamedColumnAdapter<T> copy = new SimpleTableNamedColumnAdapter<>(table.getType());
		copy.table = table.copy();
		copy.headerToIndex = new HashMap<>(headerToIndex);
		copy.indexToHeader = new HashMap<>(indexToHeader);
		copy.lastIndex = lastIndex;
		copy.maxHeaderNameLength = maxHeaderNameLength;
		return copy;
	}
}
