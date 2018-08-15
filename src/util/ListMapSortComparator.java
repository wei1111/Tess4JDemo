package util;

import java.util.Comparator;
import java.util.Map;

public abstract class ListMapSortComparator implements
		Comparator<Map<String, Object>> {

	private String key;

	private String order;

	public ListMapSortComparator(String key, String order) {
		this.key = key;
		this.order = order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */

	// 以上排序方法有个缺陷：就是误将数字当作字符串进行了排序，导致类似于12比2小的错误逻辑。
	// 因此在重写compare方法的时候需要对比较的对象先进行类型分析再比较，例如：
	// if(o1.get(key) instanceOf Integer){
	// return
	// Integer.valueOf(o1.get(key).toString())-Integer.valueOf(o2.get(key).toString());
	// }
	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		if (order.equals("asc")) {
			if (o1.get(key) instanceof Integer) {
				return Integer.valueOf(o1.get(key).toString())
						- Integer.valueOf(o2.get(key).toString());
			} else {
				return o1.get(key).toString().compareTo(o2.get(key).toString());
			}
		} else {
			if (o2.get(key) instanceof Integer) {
				return Integer.valueOf(o2.get(key).toString())
						- Integer.valueOf(o1.get(key).toString());
			} else {
				return o2.get(key).toString().compareTo(o1.get(key).toString());
			}
		}
	}

}
