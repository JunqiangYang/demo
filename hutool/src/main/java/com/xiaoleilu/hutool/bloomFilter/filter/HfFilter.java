package com.xiaoleilu.hutool.bloomFilter.filter;


public class HfFilter extends AbstractFilter {

	public HfFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}
	
	public HfFilter(long maxValue) {
		super(maxValue);
	}
	
	@Override
	public long hash(String str) {
		int length = str.length() ;
		long hash = 0;

		for (int i = 0; i < length; i++)
			hash += str.charAt(i) * 3 * i;

		if (hash < 0)
			hash = -hash;

		return hash % size;
	}

}
