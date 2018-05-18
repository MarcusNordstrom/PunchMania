package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Queue implements Serializable {
    public static final int HARDPUNCH = 0;
    public static final int FASTPUNCH = 1;
    /**
     *
     */
    private static final long serialVersionUID = -6483005274886522243L;
    private ArrayList<UserList> list;

    public Queue() {
        list = new ArrayList<UserList>();
    }

    /**
     * this method adds a name to the queue list.
     *
     * @param name
     */
    public void add(String name) {
        list.add(new UserList(0, name));
        //syso("added user : " + name + " Default gamemode HardPunch ");
    }

    public void add(String name, int mode) {
        list.add(new UserList(mode, name));
        switch (mode) {
            case HARDPUNCH:
                syso("added user : " + name + " gamemode HardPunch");
                break;
            case FASTPUNCH:
                syso("added user : " + name + " gamemode FastPunch");
                break;
        }

    }

    /**
     * This method is used to look at top of the queue list.
     *
     * @return returns the object at the top of the queue list.
     */
    public String peek() {
        return list.get(0).getUser();
    }

    public UserList peekMode() {
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
        String temp = list.get(0).getUser();
        list.remove(0);
        return temp;
    }

    public UserList popMode() {
        UserList unknown = new UserList(0, "NO NAME");
        if (list.size() == 0) {
            return unknown;
        }
        UserList temp = list.get(0);
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
        return list.get(i).getUser();
    }

    public UserList peekAtMode(int i) {
        return list.get(i);
    }

    /**
     * this method removes a name from the list.
     *
     * @param name
     */
    public void remove(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUser().equals(name)) {
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
        UserList temp = list.get(0);
        list.set(0, list.get(1));
        list.set(1, temp);
    }

    public ArrayList<String> getList() {
        ArrayList<String> temp = new ArrayList<String>();
        for (UserList s : list) {
            temp.add(s.getUser());
        }
        return temp;
    }

    public ArrayList<UserList> getListMode() {
        return list;
    }

    private void syso(String string) {
        System.out.println(string);
    }
}
