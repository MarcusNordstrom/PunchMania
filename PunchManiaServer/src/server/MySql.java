package server;

import java.sql.*;

import common.HighScoreList;
import common.UserList;

public class MySql {
	private String URL = "jdbc:mysql://ddwap.mah.se:3306/ah7115";
	private String Password = "Grupp1";
	private String UserName = "ah7115";

	private Connection myConn;

	public MySql() {
		try {
			myConn = DriverManager.getConnection(URL, UserName, Password);
			System.out.println("SQL running");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
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

	public synchronized String getUserScore(String name) {
		Statement Stmt;
		String score = "";
		try {
			PreparedStatement stmt = myConn.prepareStatement("SELECT * FROM hslist WHERE Name=?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			System.out.println("Getting score for user: " + name);
			while(rs.next()) {
				score += rs.getString(2) + " : " + rs.getInt(3) + "  " + rs.getString(4) + "\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return score;
	}

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

	public synchronized void DeleteHSList() {
		Statement Stmt;
		Statement Stmt1;
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

	public static void main(String[] args) {
		MySql ms = new MySql();
		ms.getTop10();
	}

}


