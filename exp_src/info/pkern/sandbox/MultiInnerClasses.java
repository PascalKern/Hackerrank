package info.pkern.sandbox;

import java.util.Random;

public class MultiInnerClasses {

	public static void main(String[] args) {
		MultiInnerClasses mic = new MultiInnerClasses();
		InnerClass innerClass = mic.new InnerClass();
		System.out.println(innerClass.getRandom());
		System.out.println(innerClass.getText());
	}
	
	
	private class InnerClass {
		private final int random;
		private final InnerInnerClass innerInner = new InnerInnerClass();
		public InnerClass() {
			Random random = new Random();
			this.random = random.nextInt();
		}
		public int getRandom() {
			return random;
		}
		public String getText() {
			return innerInner.getText(random);
		}
		
		private class InnerInnerClass {
			private String even = "This is a even number!";
			private String odd = "This is a odd number!";
			public String getText(int number) {
				if (number % 2 == 0) {
					return even;
				} else {
					return odd;
				}
			}
		}
	}
	
}
