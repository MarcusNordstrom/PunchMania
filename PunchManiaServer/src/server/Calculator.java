package server;

import java.sql.Array;
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

	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	public void splitter(String values) {

		for(int i=1; i < values.length(); i++) {
			newString += values.charAt(i);

			if(semi == values.charAt(i)) {
				newString = newString.replaceAll(";", "");
				newString = newString.replaceAll("\n", "");
				splittedString.add(newString);
				newString = "";

			}
		}

		for(int i=0; i < splittedString.size(); i++) {
			section = splittedString.get(i);
			String[] dev = section.split(",");
			boolean validValues = true;
			for(int x1 = 0; x1 < dev.length; x1+=3) {
				String xx1 = dev[x1];
				if(isInteger(xx1) && validValues) {
					x.add(Integer.parseInt(xx1));
				} else {
					validValues = false;
				}

			}
			for(int y1= 1; y1 <dev.length; y1+=3) {
				String yy1 = dev[y1];
				if(isInteger(yy1) && validValues) {
					y.add(Integer.parseInt(yy1));
				} else {
					validValues = false;
				}
				
			}
			for(int z1= 2; z1 <dev.length; z1+=3) {
				String zz1 = dev[z1];
				if(isInteger(zz1) && validValues) {
					z.add(Integer.parseInt(zz1));
				} else {
					validValues = false;
				}
				
			}
		}
		System.out.println(x.toString());
		System.out.println(y.toString());
		System.out.println(z.toString());
		System.out.println(x.size());
		System.out.println(y.size());
		System.out.println(z.size());
	}
}

