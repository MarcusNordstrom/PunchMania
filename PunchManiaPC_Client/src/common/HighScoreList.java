package common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScoreList {

	private ArrayList<UserList> ul;

	public HighScoreList() {
		ul = new ArrayList<UserList>();
	}
	
	/**
	 * Sorts the list of users according to their score, highest first
	 */
	public void sort() {
		Collections.sort(ul, new Comparator<UserList>() {
			@Override
			public int compare(UserList arg0, UserList arg1) {
				return arg1.getScore() - arg0.getScore();
			}
		});
	}
	
	/**
	 * Adds a single user
	 * @param user
	 * @param score
	 */
	public void add(String user, int score) {
		ul.add(new UserList(score, user));
		sort();
	}
	
	/**
	 * Adds an array of users
	 * @param arg
	 */
	
	public void add(ArrayList<UserList> arg) {
		ul.addAll(arg);
	}
	
	/**
	 * Removes all users whose name matches with the given string
	 * @param user
	 */
	public void remove(String user) {
		ArrayList<UserList> temp = new ArrayList<UserList>();
		int i = 0;
		for (UserList ul : ul) {
			if (ul.getUser().equals(user)) {
				temp.add(ul);
			}
		}
		for (UserList t : temp) {
			ul.remove(t);
		}
	}
	
	public int size() {
		return ul.size();
	}
	
	/**
	 * Prints all the stored users and their score
	 */
	
	public void syso() {
		for (UserList u : ul) {
			System.out.println(u.getUser() + " " + u.getScore());
		}
		System.out.println();
	}
	
	/**
	 * Returns a specific user from the list
	 * @param i : Location in the list
	 * @return
	 */
	public UserList getUser(int i) {
		return ul.get(i);
	}
	
	public ArrayList<UserList> getTopTen() {
		ArrayList<UserList> ret = new ArrayList<UserList>();
		if (ul.size() >= 10) {
			for (int i = 0; i < 10; i++) {
				ret.add(ul.get(i));
			}
		} else {
			for (int i = 0; i < ul.size(); i++) {
				ret.add(ul.get(i));
			}
		}
		return ret;
	}


	public static void main(String[] args) {
		HighScoreList hl = new HighScoreList();
		HighScoreList top10 = new HighScoreList();


		
		hl.add("Sebbe", 10);
		hl.add("Sebbe", 10);
		hl.add("Sebbe", 15);
		hl.add("Benji", 5);
		hl.add("Stefan", 15);
		hl.syso();
		
		top10.add(hl.getTopTen());
		top10.syso();
		
		hl.remove("Benji");
		hl.syso();

		hl.remove("Sebbe");
		hl.syso();
		
		for(int i = 0; i < 20; i++) {
			hl.add("Cyka", 10);
		}
		
		top10 = new HighScoreList();
		top10.add(hl.getTopTen());
		top10.syso();
	}
}
