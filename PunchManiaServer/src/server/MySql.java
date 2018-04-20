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
			System.out.println("SQL running");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public void setMySql(String name, int score) {
		System.out.println("SQL " + name + "  " + score);
		try {
			Statement Stmt = myConn.createStatement();
			String update = "INSERT INTO HSList(Name, Score) VALUES ('" + name + "',  "+ score +" )";
			Stmt.execute(update);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getTop10() {
		Statement Stmt;
		try {
			Stmt = myConn.createStatement();
			String sql = "SELECT * FROM HSList ORDER BY Score DESC LIMIT 10";
			ResultSet rs = Stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println(rs.getString(2) + " : " + rs.getInt(3));
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


