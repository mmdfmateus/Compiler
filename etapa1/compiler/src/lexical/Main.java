package src.lexical;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		String file = "int a, b;";
		String fileComputed = file;
		ArrayList<String> tokens = new ArrayList<String>();
		
		while(!file.isEmpty()) {
			char[] token = file.toCharArray();
			for(int i = 0; i < token.length; i++) {
				if(!CharIsNullOrEmpty(token[i])) {
					
				}
			}
		}
	}
	
	public static boolean CharIsNullOrEmpty(char c) {
		return true;
	}

}
