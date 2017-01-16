package cn.fansunion.executorservice;

public class Main {
	public static void main(String[] args) {

		int[] numbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 10, 11 };
		ConcurrentCalculator2 calc = new ConcurrentCalculator2();
		Long sum = calc.sum(numbers);
		System.out.println(sum);
		calc.close();
	}

}
