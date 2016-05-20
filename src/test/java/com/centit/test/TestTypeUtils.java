package com.centit.test;

import java.util.ArrayList;
import java.util.List;

import com.centit.support.algorithm.ListOpt;

public class TestTypeUtils {

	public static void main(String[] args) {
		List<String> ls = new ArrayList<>();
		ls.add("hello");
		ls.add("world");
		ls.add("ok!");
		
		String [] as = ListOpt.listToArray(ls);
		for(String s:as)
			System.out.println(s);
				
	}

}
