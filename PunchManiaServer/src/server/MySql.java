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

	public void setMySql(String name, int score) {
		try {
			Statement Stmt = myConn.createStatement();
			String update = "INSERT INTO hslist(Name, Score) VALUES ('" + name + "',  "+ score +" )";
			Stmt.execute(update);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getTop10() {
		Statement Stmt;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM hslist ORDER BY Score DESC LIMIT 10";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println(rs.getString(2) + " : " + rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getUserScore(String name) {
		Statement Stmt;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM HSList WHERE Name='" + name + "'";
					ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println(rs.getString(2) + " : " + rs.getInt(3) + "  " + rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	
	public static void main(String[] args) {
		MySql ms = new MySql();
		ms.getTop10();
	}

}


