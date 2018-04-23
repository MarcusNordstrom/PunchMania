package server;

import java.sql.*;

import common.UserList;

public class MySql {
	private String URL = "jdbc:mysql://localhost:3306/hslist";
	private String Password = "root";
	private String UserName = "root";

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
			Statement Stmt = myConn.createStatement();
			String update = "INSERT INTO hslist(Name, Score) VALUES ('" + name + "',  "+ score +" )";
			Stmt.execute(update);
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
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM HSList WHERE Name='" + name + "'";
			ResultSet rs = Stmt.executeQuery(sql);
			System.out.println("Getting score for user: " + name);
			score = rs.getString(2) + " : " + rs.getInt(3) + "  " + rs.getString(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return score;
	}
	
	public synchronized String getAllScore() {
		Statement Stmt;
		String score = "";
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM HSList";
			ResultSet rs = Stmt.executeQuery(sql);
			System.out.println("------------------ALL SOCORE-----------------");
			while(rs.next()) {
				System.out.println(rs.getString(2) + " : " + rs.getInt(3) + "  " + rs.getString(4));
				score += rs.getString(2) + " : " + rs.getInt(3) + "  " + rs.getString(4);
			}
			System.out.println("--------------------------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return score;
	}

	public synchronized void Delete(String name) {
		Statement stmt;
		try {
			stmt = myConn.createStatement();
			String sql = "DELETE FROM `hslist` WHERE Name='" + name +"'";
			PreparedStatement pstmt = myConn.prepareStatement(sql);
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
				Stmt1 = myConn.createStatement();
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


