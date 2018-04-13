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
			String[] dev = section.split(",");
			
			for(int x1 = 0; x1 < dev.length; x1+=3) {
				String xx1 = dev[x1];
				x.add(Integer.parseInt(xx1));
			}
			for(int y1= 1; y1 <dev.length; y1+=3) {
				String yy1 = dev[y1];
				y.add(Integer.parseInt(yy1));
			}
			for(int z1= 2; z1 <dev.length; z1+=3) {
				String zz1 = dev[z1];
				z.add(Integer.parseInt(zz1));
			}
		}
		System.out.println(x.toString());
		System.out.println(y.toString());
		System.out.println(z.toString());
	}

}
