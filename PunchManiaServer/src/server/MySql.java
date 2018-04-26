
package server;

import java.sql.*;

import javax.swing.JOptionPane;

import common.HighScoreList;
import common.Queue;

/**
 * 
 * @author Jake
 *
 * This class connects Java-server and SQL-server together. The SQL-backend contains and handles Users, Queue and Highscore. 
 * The Users table consists of ID, Username and Password. The queue table consists of ID and Name. 
 * The Highscore table consists of ID, Name, Score, Timestamps, and X,Y,Z vectors. 
 * This class takes advantage of ther SQL commands to either send info to server or receive and act on each request. 
 * Examples of requests: Upload a name to the queue/highscore in SQL-Table, remove, select by name(wildcard, does not have to be full name), get top 10, top 1....
 */
public class MySql {
	private String URL = "jdbc:mysql://ddwap.mah.se:3306/ah7115";
	private String Password = "Grupp1";
	private String UserName = "ah7115";

	private Connection myConn;

	/*
	 * Constructor makes connection to SQL-Server with given URL/Username/password to access it.
	 */
	public MySql() {
		try {
			myConn = DriverManager.getConnection(URL, UserName, Password);
			System.out.println("SQL running");
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
			System.out.println("---------------ADDED TO HSLIST-------------- \n" + name + "		" + score);
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
			System.out.println("---------------ADDED TO HSLIST-------------- \n" + name + "		" + score);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("-------------------------------------------");
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
			System.out.println("------------TOP10-------------");
			while(rs.next()) {
				System.out.println(rs.getString(2) + " : " + rs.getInt(3));
			}
			System.out.println("------------------------------");
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
			System.out.println("------------TOP1-------------");
			rs.next() ;
			hs = rs.getInt(3);
			System.out.println(hs);
			System.out.println("------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
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
			System.out.println("------------TOP1-------------");
			rs.next() ;
			name = rs.getString(2);
			System.out.println(name);
			System.out.println("------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	/*
	 * Selects all the users from the list where name = what you entered + wildcard limited by 100 users.
	 */
	public synchronized HighScoreList getUserScore(String name) {
		Statement Stmt;
		HighScoreList hsl = new HighScoreList();
		String score = "";
		try {
			PreparedStatement stmt = myConn.prepareStatement("SELECT * FROM hslist WHERE Name LIKE ? ORDER BY Score DESC LIMIT 100");
			stmt.setString(1, name + '%');
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				System.out.println(rs.getString(2) + " : " + rs.getInt(3) + "  " + rs.getString(4) + "\n");
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
			System.out.println("------------------ALL SOCORE-----------------");
			while(rs.next()) {
				System.out.println(rs.getString(2) + " : " + rs.getInt(3) + "  " + rs.getString(4));
				hsl.add(rs.getString(2), rs.getInt(3));
			}
			System.out.println("--------------------------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hsl;
	}

	/*
	 * Deletes specific users data.
	 */
	public synchronized void Delete(String name) {
		Statement stmt;
		try {
			PreparedStatement pstmt = myConn.prepareStatement("DELETE FROM `hslist` WHERE Name=?");
			pstmt.setString(1, name);
			System.out.println("-----------DELETING USER--------- \n" + name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("-------------------------------- ");
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
			System.out.println("HSLIST CLEARED");
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
			System.out.println("------------------QUEUE-----------------");
			while(rs.next()) {
				System.out.println(rs.getString(2));
				queue.add(rs.getString(2));
			}
			System.out.println("--------------------------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queue;
	}

	/*
	 * Removes specific users queue.
	 */
	public synchronized void deleteQueue(String name) {
		Statement stmt;
		try {
			PreparedStatement pstmt = myConn.prepareStatement("DELETE FROM queue WHERE Name=?");
			pstmt.setString(1, name);
			System.out.println("-----------DELETING USER FROM QUEUE--------- \n" + name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("-------------------------------- ");
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
			System.out.println("------------------QUEUE-----------------");
			System.out.println(rs.getString(2));
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
			System.out.println("QUEUE CLEARED");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Returns size of queue, used to send info to embedded system to be active or not.
	 */
	public synchronized int isEmpty() {
		Statement Stmt;
		int size = 0;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM queue";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()){
				size++;
			}
			System.out.println(size);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return size;
	}
	public static void main(String[] args) {
		MySql ms = new MySql();
		ms.getUserScore(JOptionPane.showInputDialog("enter name"));
	}
}