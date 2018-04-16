package common;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 3569987578043775700L;
	private Object payload;
	private int instruction;
	
	public final int NEW_QUEUE = 1;
	public final int NEW_HIGHSCORELIST = 2;
	public final int NEW_USER_TO_QUEUE = 3;
	
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
