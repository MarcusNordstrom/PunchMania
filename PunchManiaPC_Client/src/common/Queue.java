package common;

import java.util.ArrayList;

public class Queue {
	private ArrayList<String> list;
	
	public Queue() {
		list = new ArrayList<String>();
	}
	
	
	public void add(String name) {
		list.add(name);
		syso("added user : " + name);
	}
	
	public String peek() {
		return list.get(0);
	}
	
	public String pop() {
		String temp = list.get(0);
		list.remove(0);
		return temp;
	}
	
	public String peekAt(int i) {
		return list.get(i);
	}

	public void remove(String name) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(name)) {
				list.remove(i);
			}
		}
	}
	
	public int size() {
		return list.size();
	}
	
	public void dropOne() {
		String temp = list.get(0);
		list.set(0, list.get(1));
		list.set(1, temp);
	}

	private void syso(String string) {
		System.out.println(string);
	}
}
