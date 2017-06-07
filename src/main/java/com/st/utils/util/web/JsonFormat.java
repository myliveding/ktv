package com.st.utils.util.web;
/**
 * json实现缩进format
 * @author gaozhanglei
 *
 */
public class JsonFormat {
	public static String format(String json) {
		if (json == null || json.trim().length() == 0) {
			return "";
		}
		json = json.replaceAll(" ","");
		StringBuffer result = new StringBuffer();
		int pos = 0;
		int len = json.length();
		String indentStr = "    ";
		String newLine = "\n";
		char prevChar = ' ';
		boolean outOfQuotes = true;
		char currChar;

		for (int i = 0; i < len; i++) {
			// Grab the next character in the string.
			currChar = json.charAt(i);
			// Are we inside a quoted string?
			if (currChar == ' ' && prevChar != '\\') {
				outOfQuotes = !outOfQuotes;
				// If this character is the end of an element,
                // output a new line and indent the next line.
			}else if((currChar==']'||currChar=='}')&&outOfQuotes){
				result.append(newLine);
				pos--;
				for(int j=0;j<pos;j++){
					result.append(indentStr);
				}
			}
			// Add the character to the result string.
			result.append(currChar);
			// If the last character was the beginning of an element,
            // output a new line and indent the next line.
			if((currChar==','||currChar=='{'||currChar=='[')&&outOfQuotes){
				result.append(newLine);
				if(currChar=='{'||currChar=='['){
					pos++;
				}
				for(int j=0;j<pos;j++){
					result.append(indentStr);
				}
			}
			prevChar = currChar;
		}
		return result.toString();
	}
	
}
