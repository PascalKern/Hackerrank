package info.pkern.sandbox;

import info.pkern.hackerrank.commons.NumberUtil;

import java.util.Arrays;
import java.util.List;

public class SimpleToGenericType {

	
	public static void main(String[] args) {
		SimpleToGenericType env = new SimpleToGenericType();
		
		List<Integer> left = Arrays.asList(new Integer[]{1,2,3,4});
		List<Integer> right = Arrays.asList(new Integer[]{1,2,3,4});
		
		int index = 2;
		SimpleNumType st = env.new SimpleNumType(left, right);
		System.out.println("Add left and right at index "+ index +" = " + st.addLeftAndRightAt(index));
		
		GenericNumType<Integer> gt = env.new GenericNumType<>(left, right);
		System.out.println("Add left and right at index "+ index +" = " + gt.addLeftAndRightAt(index));
	}
	
	
	
	
	class SimpleNumType {
		
		private List<Integer> leftColumn;
		private List<Integer> rightColumn;
		
		public SimpleNumType(List<Integer> leftColumn, List<Integer> rightColumn) {
			this.leftColumn = leftColumn;
			this.rightColumn = rightColumn;
		}
		
		public Integer addLeftAndRightAt(int index) {
			return leftColumn.get(index) + rightColumn.get(index);
		}
	}
	
	
	

	class GenericNumType<T extends Number> {
		
		private List<T> leftColumn;
		private List<T> rightColumn;
		
		public GenericNumType(List<T> leftColumn, List<T> rightColumn) {
			this.leftColumn = leftColumn;
			this.rightColumn = rightColumn;
		}
		
		public T addLeftAndRightAt(int index) {
			return NumberUtil.numberAdder(leftColumn.get(index) , rightColumn.get(index));
		}
	}

}
