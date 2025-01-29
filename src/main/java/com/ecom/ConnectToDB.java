package com.ecom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectToDB {
	final protected static Connection create_connection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connect=DriverManager.getConnection("jdbc:mysql://localhost:3310/ECommerce_DB","root","ecom@1234");
			connect.setAutoCommit(false);
			return connect;
		}
		catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		return null;
	}
}
