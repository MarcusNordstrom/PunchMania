package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Queue implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6483005274886522243L;
    private ArrayList<String> list;

    public Queue() {
        list = new ArrayList<String>();
    }

    /**
     * this method adds a name to the queue list.
     *
     * @param name
     */
    public void add(String name) {
        list.add(name);
        syso("added user : " + name);
    }
    
    public void add(String name, int mode) {
        list.add(name);
        syso("added user : ");
    }

    /**
     * This method is used to look at top of the queue list.
     *
     * @return returns the object at the top of the queue list.
     */
    public String peek() {
        return list.get(0);
    }

    /**
     * This method is used to remove the object at the top of the queue list.
     *
     * @return
     */
    public String pop() {
        String unknown = "NO NAME";
        if (list.size() == 0) {
            return unknown;
        }
        String temp = list.get(0);
        list.remove(0);
        return temp;
    }

    /**
     * this method is used to look at any position of the queue list.
     *
     * @param i : is the position of the element.
     * @return returns the object of the given position i.
     */
    public String peekAt(int i) {
        return list.get(i);
    }

    /**
     * this method removes a name from the list.
     *
     * @param name
     */
    public void remove(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(name)) {
                list.remove(i);
            }
        }
    }

    /**
     * This method returns the number of elements in the queue list.
     *
     * @return
     */
    public int size() {
        return list.size();
    }

    /**
     * This method exchange places between the first and second elements.
     */
    public void dropOne() {
        String temp = list.get(0);
        list.set(0, list.get(1));
        list.set(1, temp);
    }

    public ArrayList<String> getList() {
        return list;
    }

    private void syso(String string) {
        System.out.println(string);
    }
}
