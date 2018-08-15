package util;

public class RemoveChar4Str {
	public static String removeChar4Str(String resource, char ch) {
		StringBuffer buffer = new StringBuffer();
		int position = 0;
		char currentChar;

		while (position != resource.length()) {
			currentChar = resource.charAt(position++);
			if (currentChar != ch)
				buffer.append(currentChar);
		}
		return buffer.toString();
	}

}
