package tess4j.test;

import org.junit.Test;

import util.CompareFileName;

public class testCompareFileName {
	@Test
	public void testCompFileName() {
		String str1 = "cccc";
		String str2 = "aaaa";
		String str3 = "bbbb";

		String person1 = "³ÂÎ¬";
		String person2 = "ÎâºÆÈ»";
		String person3 = "ÕÔ×ÓÂ¶";

		String num1 = "1213";
		String num2 = "1341";
		String num3 = "123513";

		int r1 = CompareFileName.compareFileName(str1, str2);
		int r2 = CompareFileName.compareFileName(person1, person2);
		int r3 = CompareFileName.compareFileName("1213", "1341");
		System.out.println(r1);
		System.out.println(r2);
		System.out.println(r3);
		
		System.out.println(person1.compareTo(person3));
	}

}
