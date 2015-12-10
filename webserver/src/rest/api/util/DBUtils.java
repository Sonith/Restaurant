package rest.api.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import rest.api.dao.AdminDAO;
import rest.api.dao.TimingsDAO;
import rest.api.model.Admin;
import rest.api.model.Timings;

public class DBUtils {

	private final static String DB_URL = "jdbc:mysql://localhost:3306/restaurant_db";
	private final static String DB_USER = "root";
	private final static String DB_PASSWORD = "";

	static {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("MySQL JDBC Driver has loaded.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Error in loading MySQL JDBC Driver");
		}
	}

	public static Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			//System.out.println("Connection Successful");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Connection NOT Successful");
		}

		return conn;
	}

	public static void closeResource(PreparedStatement ps, ResultSet rs, Connection conn) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}

			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeResource(PreparedStatement ps, Connection conn) {
		try {
			if (ps != null) {
				ps.close();
			}

			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


}
