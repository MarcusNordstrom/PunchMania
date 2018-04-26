package common;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 3569987578043775700L;
	private Object payload;
	private int instruction;
	
	public static final int NEW_QUEUE = 1;
	public static final int NEW_HIGHSCORELIST = 2;
	public static final int NEW_USER_TO_QUEUE = 3;
	public static final int NEW_HS = 4;
	public static final int REQUEST_PLAYERSCORES = 5;
	public static final int PLAYERSCORES = 6;
	
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
