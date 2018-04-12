package server;

import java.util.ArrayList;

public class Calculator {
	private String newString ="";
	private char semi = ';';
	private char col= ',';
	int X;
	int Y;
	int Z;
	private String section;
	private ArrayList<String> splittedString = new ArrayList<String>();
	private ArrayList<Integer> x = new ArrayList<Integer>();
	private ArrayList<Integer> y = new ArrayList<Integer>();
	private ArrayList<Integer> z = new ArrayList<Integer>();


	public Calculator (String values) {

		splitter(values);
	}

	public void splitter(String values) {

		for(int i=1; i < values.length(); i++) {
			newString += values.charAt(i);

			if(semi == values.charAt(i)) {
				newString = newString.replaceAll(";", "");
				splittedString.add(newString);
				newString = "";

			}
		}

		for(int i=0; i < splittedString.size(); i++) {
			section = splittedString.get(i);
	
			for(int o=0; o <section.length(); o++) {
				int last_colon = 0;
				
//				if(col == section.charAt(o)) {
//					String subx = section.substring(last_colon, o);
//					System.out.println(subx + "XXX");
//					last_colon = o;
//				}
//				if(col == section.charAt(o)) {
//					String suby = section.substring(last_colon, ',');
//					System.out.println(suby + "YYY");
//					last_colon = o;
//				}
//				if(col == section.charAt(o)) {
//					String subz = section.substring(last_colon+1, section.length());
//					System.out.println(subz + "ZZZ");
//					last_colon = o;
//					
//				}
			}
		}
	}
}
