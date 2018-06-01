package common;

import java.io.Serializable;

/**
 * @author Petar Novkovic
 */

public class Message implements Serializable {

    private static final long serialVersionUID = 3569987578043775700L;
    private Object payload;
    private int instruction;
    
    public static final int NEW_QUEUE = 1;
    public static final int NEW_USER_TO_QUEUE = 3;
    public static final int NEW_HS = 4;

    public static final int SERVER_SEND_PLAYERSCORES_HARDPUNCH = 6;
    public static final int SERVER_SEND_PLAYERSCORES_FASTPUNCH = 12;
    
    public static final int CLIENT_REQUEST_HSDETAILS = 7;
    public static final int SERVER_SEND_HSDETAILS = 8;
    public static final int GAMEMODE = 9;
    
    public static final int NEW_HIGHSCORELIST_HARDPUNCH = 2;
    public static final int NEW_HIGHSCORELIST_FASTPUNCH = 10;
    
    public static final int CLIENT_REQUEST_PLAYERSCORES_HARDPUNCH = 5;
    public static final int CLIENT_REQUES_PLAYERSCORES_FASTPUNCH = 11;
	
	public Message(Object payload, int instruction) {
		this.payload = payload;
		this.instruction = instruction;
	}
	
	public Object getPayload() {
		return payload;
	}
	
	public int getInstruction() {
		return this.instruction;
	}
	
}
