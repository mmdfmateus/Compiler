package src.lexical;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// System.out.println("Hello World");
		
		String file = "int a, b;";
		String fileComputed = file;
		ArrayList<String> tokens = new ArrayList<String>();
		
		while(!fileComputed.isEmpty()) {
			char[] token = new char[200];
			for(int i = 0; i < fileComputed.length(); i++) {
				if(!CharIsNullOrEmpty(fileComputed.toCharArray()[i])) {
					// token[i] = 
				}
				fileComputed = fileComputed.replace(fileComputed.toCharArray()[i], ' ').trim();
				System.out.println(fileComputed);
			}
		}
	}
	
	public static boolean CharIsNullOrEmpty(char c) {
		return true;
	}

}
