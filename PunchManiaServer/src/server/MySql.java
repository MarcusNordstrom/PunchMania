package server;

import java.sql.*;

import common.UserList;

public class MySql {
	private String URL = "jdbc:mysql://localhost:8889/HSList";
	private String Password = "root";
	private String UserName = "root";

	private Connection myConn;

	public MySql() {
		try {

			myConn = DriverManager.getConnection(URL, UserName, Password);
			System.out.println("SQL Connected");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setMySql(String name, int score) {
		try {
			Statement Stmt = myConn.createStatement();
			String update = "INSERT INTO HSList(Name, Score) VALUES ('" + name + "', " + score + ")";
			Stmt.execute(update);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new MySql();
	}

}


