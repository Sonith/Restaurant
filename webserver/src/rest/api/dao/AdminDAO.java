package rest.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rest.api.exception.AppException;
import rest.api.model.Admin;
import rest.api.util.DBUtils;

public class AdminDAO {

	private static Admin admin = null;
	public static boolean auto = false;

	static {

		try {
			getAdmin();
			auto = admin.isAuto_assign_table();
		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	public static Admin getAdmin() throws AppException {

		Connection conn = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DBUtils.connect();
			ps = conn.prepareStatement("SELECT * FROM admin");
			rs = ps.executeQuery();

			while (rs.next()) {
				admin = new Admin(rs.getString("email"), rs.getString("address"), rs.getString("contact"),
						String.valueOf(rs.getInt("auto_assign_table")), rs.getString("password"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return admin;
	}

	public Admin validateAdmin(String email, String pass) throws AppException {

		if (admin.getEmail().equalsIgnoreCase(email) && admin.getPassword().equals(pass)) {
			admin.setPassword("");
			return admin;

		}

		return null;
	}

	public static String getEmail() {

		return admin.getEmail();

	}

	public static String getContact() {

		return admin.getContact();

	}

	public static String getAddress() {

		return admin.getAddress();

	}

	public boolean update(Admin admin) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"UPDATE admin set  email=?, address=?, contact=?, auto_assign_table=? where id=?",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(5, 1);
			ps.setString(1, admin.getEmail());
			ps.setString(2, admin.getAddress());
			ps.setString(3, admin.getContact());
			ps.setInt(4, admin.isAuto_assign_table() ? 1 : 0);
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			// throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}
		getAdmin();
		return true;
	}

}
