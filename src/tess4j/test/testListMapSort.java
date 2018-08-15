package tess4j.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import util.ListMapSortComparator;

public class testListMapSort {
	@Test
	public void test() {
		String str1 = "cccc";
		String str2 = "aaaa";
		String str3 = "bbbb";

		String person1 ="³ÂÎ¬";
		String person2 = "ÎâºÆÈ»";
		String person3 = "ÕÔ×ÓÂ¶";

		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		map1.put(str1, person1);
		map2.put(str2, person2);
		map3.put(str3, person3);
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map1);
		list.add(map2);
		list.add(map3);
		System.out.println(list);
		
		Collections.sort(list,new ListMapSortComparator("","desc") {
		});
		System.out.println(list);
	}
}