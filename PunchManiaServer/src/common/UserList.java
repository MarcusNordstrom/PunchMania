package common;

import java.io.Serializable;
/**
 * Contains a serializable user that is used in lists
 *
 */
public class UserList implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5202818878808929934L;
    private int score;
    private String user;

    public UserList(int score, String user) {
        this.score = score;
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public String getUser() {
        return user;
    }
}
