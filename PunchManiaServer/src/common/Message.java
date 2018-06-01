package common;

import java.io.Serializable;
/**
 * A serializable message class used to send all kinds of data between "hosts"
 *@author Sebastian Carlsson, Benjamin Zakrisson
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 3569987578043775700L;
    private Object payload;
    private int instruction;
    /**
     * Instruction used in the message
     */
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
	/**
	 * New message containing a Payload and an instruction of what to do with the payload
	 * @param payload
	 * @param instruction
	 */
	public Message(Object payload, int instruction) {
		this.payload = payload;
		this.instruction = instruction;
	}
	/**
	 * Return the payload
	 * @return
	 */
	public Object getPayload() {
		return payload;
	}
	/**
	 * Return the instruction
	 * @return
	 */
	public int getInstruction() {
		return this.instruction;
	}
	
}
