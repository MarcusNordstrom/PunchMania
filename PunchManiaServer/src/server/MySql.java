

package server;

import java.sql.*;
import java.util.ArrayList;
import common.HighScoreList;
import common.Queue;

/**
 * 
 *
 * This class connects Java-server and SQL-server together. The SQL-backend contains and handles Users, Queue and Highscore. 
 * The Users table consists of ID, Username and Password. The queue table consists of ID and Name. 
 * The Highscore table consists of ID, Name, Score, Timestamps, and X,Y,Z vectors. 
 * This class takes advantage of ther SQL commands to either send info to server or receive and act on each request. 
 * Examples of requests: Upload a name to the queue/highscore in SQL-Table, remove, select by name(wildcard, does not have to be full name), get top 10, top 1....
 */
public class MySql{
	private String URL = "jdbc:mysql://ddwap.mah.se:3306/ah7115";
	private String Password = "Grupp1";
	private String UserName = "ah7115";

	private ArrayList<Integer> X = new ArrayList<Integer>();
	private ArrayList<Integer> Y = new ArrayList<Integer>();
	private ArrayList<Integer> Z = new ArrayList<Integer>();
	private ArrayList<ArrayList<Integer>> XYZ = new ArrayList<ArrayList<Integer>>();
	private Connection myConn;

	/*
	 * Constructor makes connection to SQL-Server with given URL/Username/password to access it.
	 */
	public MySql() {
		try {
			myConn = DriverManager.getConnection(URL, UserName, Password);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	/*
	 * Name and Score are set in hslist table in SQL. Mainly used for testing since it does not require components of force as in-parameters.
	 */
	public synchronized void setMySql(String name, int score) {
		try {
			PreparedStatement stmt = myConn.prepareStatement("INSERT INTO hslist(Name, Score) VALUES (?,?)");
			stmt.setString(1, name);
			stmt.setInt(2, score);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Used when a real hit on the punching bag, all possible parameters that are measured and given are used to document on 
	 * SQL-server(Name, Score and components of force).
	 */
	public synchronized void setMySql(String name, int score, String x, String y, String z) {
		try {
			PreparedStatement stmt = myConn.prepareStatement("INSERT INTO hslist(Name, Score, X, Y, Z) VALUES (?,?,?,?,?)");
			stmt.setString(1, name);
			stmt.setInt(2, score);
			stmt.setString(3, x);
			stmt.setString(4, y);
			stmt.setString(5, z);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Selects users from the hslist-table on the SQL-server limited by 10 users ordered in the column Score sorted descendingly 
	 * this gives us top 10 users with the highest score selected from the table.
	 */
	public synchronized void getTop10() {
		Statement Stmt;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM hslist ORDER BY Score DESC LIMIT 10";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Returns what the highest score ever is.
	 */
	public synchronized int getTop1() {
		Statement Stmt;
		int hs = 0;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM hslist ORDER BY Score DESC LIMIT 1";
			ResultSet rs = Stmt.executeQuery(sql);
			rs.next() ;
			hs = rs.getInt(3);
		} catch (SQLException e) {
			System.err.println("queue empty");
		}
		return hs;
	}
	
	public synchronized int getTop1Fast() {
		Statement Stmt;
		int hs = 0;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM fastpunch ORDER BY Score DESC LIMIT 1";
			ResultSet rs = Stmt.executeQuery(sql);
			rs.next() ;
			hs = rs.getInt(3);
		} catch (SQLException e) {
			System.err.println("queue empty");
		}
		return hs;
	}

	/*
	 * Used when a user gets the new TOP 1 score, we return the name and send it to client to display it when the highscore is beaten
	 */
	public synchronized String getTop1Name() {
		Statement Stmt;
		String name = "";
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM hslist ORDER BY Score DESC LIMIT 1";
			ResultSet rs = Stmt.executeQuery(sql);
			rs.next() ;
			name = rs.getString(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	/*
	 * Selects all the users from the list where name = what you entered + wildcard limited by 100 users.
	 */
	public synchronized HighScoreList getUserScore(String name) {
		HighScoreList hsl = new HighScoreList();
		try {
			PreparedStatement stmt = myConn.prepareStatement("SELECT * FROM hslist WHERE Name LIKE ? ORDER BY Score DESC LIMIT 100");
			stmt.setString(1, name + '%');
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				hsl.add(rs.getString(2), rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hsl;
	}

	/*
	 * Returns all the scores in a highscore object
	 */
	public synchronized HighScoreList getAllScore() {
		Statement Stmt;
		HighScoreList hsl = new HighScoreList();
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM HSList";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
				hsl.add(rs.getString(2), rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hsl;
	}

	/*
	 * Deletes specific users data.
	 */
	public synchronized void Delete(String name) {
		try {
			PreparedStatement pstmt = myConn.prepareStatement("DELETE FROM `hslist` WHERE Name=?");
			pstmt.setString(1, name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Deletes all data from hslist table
	 */
	public synchronized void DeleteHSList() {
		Statement Stmt;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM hslist";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
				String sql1 = "DELETE FROM hslist";
				PreparedStatement pstmt = myConn.prepareStatement(sql1);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Used to add someone to the table queue in SQL
	 */
	public synchronized void toQueue(String name) {
		try {
			PreparedStatement stmt = myConn.prepareStatement("INSERT INTO queue(Name) VALUES (?)");
			stmt.setString(1, name);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Returns the full queue as a queue object
	 */
	public synchronized Queue getQueue() {
		Statement Stmt;
		Queue queue = new Queue();
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM queue";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
				queue.add(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queue;
	}

	/*
	 * Removes specific users queue.
	 */
	public synchronized void deleteQueue(String name) {
		try {
			PreparedStatement pstmt = myConn.prepareStatement("DELETE FROM queue WHERE Name=?");
			pstmt.setString(1, name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Used when someone hits the punching bag, selects the user and the deletes it.
	 */
	public synchronized String popQueue() {
		Statement Stmt;
		String name = "";
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM queue";
			ResultSet rs = Stmt.executeQuery(sql);
			rs.next();
			name = rs.getString(2);
			deleteQueue(name);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	/*
	 * Deletes all data in queue table
	 */
	public synchronized void DeleteQueueList() {
		Statement Stmt;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM queue";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
				String sql1 = "DELETE FROM queue";
				PreparedStatement pstmt = myConn.prepareStatement(sql1);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized ArrayList<ArrayList<Integer>> getXYZ(String name, int Score) {
		String x = "";
		String y = "";
		String z = "";
		X = new ArrayList<Integer>();
		Y = new ArrayList<Integer>();
		Z = new ArrayList<Integer>();
		XYZ = new ArrayList<ArrayList<Integer>>();
		try {
			PreparedStatement stmtx = myConn.prepareStatement("SELECT * FROM hslist WHERE Name=? AND Score =?");
			stmtx.setString(1, name);
			stmtx.setInt(2, Score);
			ResultSet rsx = stmtx.executeQuery();
			while(rsx.next()){
				x = rsx.getString(5);
				String[] xx = x.split(",");
				for(int i=0; i < xx.length; i++) {
					int o = Integer.parseInt(xx[i]);
					X.add(o);
				}
			}	
			PreparedStatement stmty = myConn.prepareStatement("SELECT * FROM hslist WHERE Name=? AND Score =?");
			stmty.setString(1, name);
			stmty.setInt(2, Score);
			ResultSet rsy = stmty.executeQuery();
			while(rsy.next()){
				y = rsy.getString(6);
				String[] yy = y.split(",");
				for(int i=0; i < yy.length; i++) {
					Y.add(Integer.parseInt(yy[i]));
				}
			}
			PreparedStatement stmtz = myConn.prepareStatement("SELECT * FROM hslist WHERE Name=? AND Score =?");
			stmtz.setString(1, name);
			stmtz.setInt(2, Score);
			ResultSet rs = stmtz.executeQuery();
			while(rs.next()){
				z = rs.getString(7);
				String[] zz = z.split(",");
				for(int i=0; i < zz.length; i++) {
					Z.add(Integer.parseInt(zz[i]));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		XYZ.add(X);
		XYZ.add(Y);
		XYZ.add(Z);
		
		System.out.println(X.toString());
		System.out.println(Y.toString());
		System.out.println(Z.toString());
		
		System.out.println(X.size());
		System.out.println(Y.size());
		System.out.println(Z.size());
		
		System.out.println(name + Score);
		
		
		return XYZ;
	}

	/*
	 * Returns size of queue, used to send info to embedded system to be active or not.
	 */
	public synchronized int queueSize() {
		Statement Stmt;
		int size = 0;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM queue";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()){
				size++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * Returns the current checksum value
	 * @return
	 */
	public synchronized Long getCheckSum() {
		Long checkSum = null;
		Statement Stmt;
		try {
			Stmt = myConn.createStatement();
			String sql = "CHECKSUM TABLE ah7115.queue";
			ResultSet rs = Stmt.executeQuery(sql);
			rs.next();
			checkSum = rs.getLong(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return checkSum;
	}
	/**
	 * Add user to Fastpunch highscore list with set score
	 * @param name
	 * @param score
	 */
	public synchronized void setFastPunch(String name, int score) {
		try {
			PreparedStatement stmt = myConn.prepareStatement("INSERT INTO fastpunch(Name, Score) VALUES (?,?)");
			stmt.setString(1, name);
			stmt.setInt(2, score);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get score from specific user
	 * @param name
	 * @return
	 */
	public synchronized HighScoreList getFastPunch(String name){
		HighScoreList hsl = new HighScoreList();
		try {
			PreparedStatement stmt = myConn.prepareStatement("SELECT * FROM fastpunch WHERE Name LIKE ? ORDER BY Score DESC LIMIT 100");
			stmt.setString(1, name + '%');
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				hsl.add(rs.getString(2), rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hsl;
	}
	/**
	 * Return the Fastpunch highscorelist
	 * @return
	 */
	public synchronized HighScoreList getAllScoreFastPunch() {
		Statement Stmt;
		HighScoreList hsl = new HighScoreList();
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM fastpunch";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
				hsl.add(rs.getString(2), rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hsl;
	}
	/**
	 * Init SQL
	 * @param args
	 */
	public static void main(String[] args) {
		MySql ms = new MySql();
		ms.getFastPunch("Jake");	
		}
}