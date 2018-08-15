package util;

import java.util.Comparator;
import java.util.List;

/**
 * 根据List集合的List元素的第index个元素来排序
 * 
 * @author 12044
 *
 */
public abstract class ListListSortComparator implements Comparator<List<? extends Object>> {

	private int index;
	private String order;

	public ListListSortComparator(int index, String order) {
		this.index = index;
		this.order = order;
	}

	@Override
	public int compare(List<? extends Object> l1, List<? extends Object> l2) {

		Object obj1 = l1.get(index);
		Object obj2 = l2.get(index);
		// 升序
		if (order.equals("asc")) {

			// if (obj1 instanceof Integer) {
			// return Integer.valueOf(obj1.toString())
			// - Integer.valueOf(obj2.toString());
			// } else if (obj1 instanceof String) {
			// String str1 = ((String) obj1).substring(0,
			// ((String) obj1).indexOf("."));
			// String str2 = ((String) obj2).substring(0,
			// ((String) obj2).indexOf("."));
			// return Integer.valueOf(str1) - Integer.valueOf(str2);
			// } else {
			// return 0;
			// }
			return CompareFileName.compareFileName((String) obj1, (String) obj2);
		} else {
			// if (obj2 instanceof Integer) {
			// return Integer.valueOf(obj2.toString()) -
			// Integer.valueOf(obj1.toString());
			// } else if (obj1 instanceof String) {
			// String str1 = ((String) obj1).substring(0, ((String)
			// obj1).indexOf("."));
			// String str2 = ((String) obj2).substring(0, ((String)
			// obj2).indexOf("."));
			// return Integer.valueOf(str2) - Integer.valueOf(str1);
			// } else {
			// return 0;
			// }
			return CompareFileName.compareFileName((String) obj2, (String) obj1);

		}
	}
}
