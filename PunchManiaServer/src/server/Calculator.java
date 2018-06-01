package server;


import java.util.ArrayList;
/**
 * Score calculations class 
 * @author Jake O´Donnell
 * @author Marcus Nordström
 *
 */
public class Calculator {
	private String newString ="";
	private char semi = ';';
	@SuppressWarnings("unused")
	private char col= ',';
	int X;
	int Y;
	int Z;
	private int score;
	private String section;
	private ArrayList<String> splittedString = new ArrayList<String>();
	private Server server;
	private ArrayList<Integer> x = new ArrayList<Integer>();
	private ArrayList<Integer> y = new ArrayList<Integer>();
	private ArrayList<Integer> z = new ArrayList<Integer>();
	/**
	 * Connect calculator to server
	 * @param server
	 */
	public Calculator(Server server) {
		this.server = server;
	}
	/**
	 * Return next X in queue
	 * @return
	 */
	public int popX() {
		if(x.size() == 0) {
			return 0;
		}
		int xint = x.get(0);
		x.remove(0);
		return xint;
	}
	/**
	 * Return next Y in queue
	 * @return
	 */
	public int popY() {
		if(y.size() == 0) {
			return 0;
		}
		int yint = y.get(0);
		y.remove(0);
		return yint;
	}
	/**
	 * Return next Z in queue
	 * @return
	 */
	public int popZ() {
		if(z.size() == 0) {
			return 0;
		}
		int zint = z.get(0);
		z.remove(0);
		return zint;
	}
	/**
	 * Check if inputted string is an integer
	 * @param s
	 * @return
	 */
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
	/**
	 * Split inputted string into 3 int arrays X,Y,Z
	 * @param values
	 */
	public void splitter(String values) {

		for(int i=0; i < values.length(); i++) {
			newString += values.charAt(i);

			if(semi == values.charAt(i)) {
				newString = newString.replaceAll(";", "");
				newString = newString.replaceAll("\n", "");
				splittedString.add(newString);
				newString = "";

			}
		}
		
		for(int i=0; i < splittedString.size()-1; i++) {
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
	}
	/**
	 * Calculate the Score
	 */
	public void force() {
		int avrgX = 0;
		for(int i=0; i< x.size(); i++) {
			avrgX += Math.abs(x.get(i));
		}
		avrgX = avrgX/x.size();
		int avrgY = 0;
		for(int o=0; o< y.size(); o++) {
			avrgY += Math.abs(y.get(o));
		}
		avrgY = avrgY/y.size();
		double componentXY = Math.sqrt((Math.pow(avrgX, 2) + Math.pow(avrgY, 2)));
		System.out.println("Component of x and y: " + componentXY);
		int avrgZ = 0;
		for(int p=0; p< z.size(); p++) {
			//avrgZ += Math.abs(z.get(p));
			avrgZ += z.get(p);
		}
		avrgZ = avrgZ/z.size();
		double componentXYZ = Math.sqrt((Math.pow(avrgZ, 2) + Math.pow(componentXY, 2)));
		System.out.println("Component of x, y and z: " + componentXYZ);
		score = (int)(componentXYZ * 10);
		System.out.println("SCORE: " + score);
	}
	/**
	 * Return the score
	 * @return
	 */
	public int getScore() {
		return score;
	}
	/**
	 * Reset the Calculator
	 */
	private void clearPrevious() {
		newString = "";
		section = "";
		splittedString.clear();
		x.clear();
		y.clear();
		z.clear();
		score = 0;
	}
	/**
	 * Use input string to calculate your score
	 * @param values 
	 * @return score
	 */
	public int calculateScore(String values) {
		clearPrevious();
		splitter(values);
		force();
		String xx = "";
		for(int i=0; i < x.size(); i++) {
			xx += popX()  + ",";
		}
		String yy = "";
		for(int i=0; i < y.size(); i++) {
			yy += popY()  + ",";
		}
		String zz = "";
		for(int i=0; i < z.size(); i++) {
			zz += popZ() + ",";
		}
		server.setScore(getScore(), xx, yy, zz);
		return getScore();
	}

	public ArrayList<Integer> getX() {
		return x;
	}
	public ArrayList<Integer> getY() {
		return y;
	}
	public ArrayList<Integer> getZ() {
		return z;
	}
}

