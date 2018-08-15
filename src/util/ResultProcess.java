package util;

import java.util.ArrayList;
import java.util.List;

public class ResultProcess {
	public int successSize;

	public ResultProcess(int imageSize) {
		this.successSize = imageSize;
	}

	public List<String> resultProcess(String result, String fileName) {
		int beginIndexEr;
		int endIndexQi;
		int endIndexEr;
		String busName = null;
		String busId = null;
		// static int successSize;
		List<String> list = new ArrayList<String>();
		beginIndexEr = result.indexOf("二");
		endIndexQi = result.lastIndexOf("企");
		endIndexEr = result.lastIndexOf("二");

		if (endIndexEr == -1) {
			busName = "企业名称识别失败";
			successSize--;
		} else {
			busName = result.substring(endIndexEr + 1, result.length() - 2);
			busName = RemoveChar4Str.removeChar4Str(busName, ' ');
			if (busName.contains("B艮")) {
				busName = busName.replaceAll("B艮", "限");
			}
			if (busName.contains("眼饰")) {
				busName = busName.replaceAll("眼饰", "服饰");
			}
			if (busName.contains("有眼")) {
				busName = busName.replaceAll("有眼", "有限");
			}
			if (busName.contains("〕")) {
				busName = busName.replaceAll("〕", ")");
			}
			if (busName.contains("_")) {
				busName = busName.replaceAll("_", "一");
			}

		}

		if ((beginIndexEr == -1) || (endIndexQi == -1)) {
			busId = "识别注册号失败";
		} else {
			busId = result.substring(beginIndexEr + 1, endIndexQi - 1);
			busId = RemoveChar4Str.removeChar4Str(busId, ' ');
			if (busId.contains("丁")) {
				busId = busId.replaceAll("丁", "T");
			}
		}

		list.add(fileName);
		list.add(busName);
		list.add(busId);

		return list;
	}
}
