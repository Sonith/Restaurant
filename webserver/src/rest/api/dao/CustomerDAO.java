package rest.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rest.api.exception.AppException;
import rest.api.model.Customers;
import rest.api.util.DBUtils;

public class CustomerDAO {

	public List<Customers> getAll() throws AppException {

		List<Customers> customer = new ArrayList<Customers>();

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM customers");
			rs = ps.executeQuery();

			while (rs.next()) {
				Customers a = new Customers(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
						rs.getString("phone"));

				customer.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return customer;
	}

	public Customers get(int id) throws AppException {

		Customers customer = null;

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM customers WHERE ID=?");
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				customer = new Customers(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
						rs.getString("phone"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return customer;
	}
        public Customers get(String email) throws AppException {

		Customers customer = null;

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM customers WHERE EMAIL=?");
			ps.setString(1, email);

			rs = ps.executeQuery();

			if (rs.next()) {
				customer = new Customers(rs.getInt("ID"), rs.getString("NAME"), rs.getString("EMAIL"),
						rs.getString("PHONE"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return customer;
	}

	public Customers create(Customers customer) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("INSERT INTO customers ( name, email, phone) VALUES ( ?, ?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, customer.getName());
			ps.setString(2, customer.getEmail());
			ps.setString(3, customer.getPhone());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				customer.setId(rs.getInt(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return customer;
	}

	public boolean update(Customers customer) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("UPDATE customer set  name=?, email=?, phone=? where id=?",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(4, customer.getId());
			ps.setString(1, customer.getName());
			ps.setString(2, customer.getEmail());
			ps.setString(3, customer.getPhone());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				customer.setId(rs.getInt(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			// throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return true;
	}

	public boolean delete(Customers customer) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("DELETE FROM CUSTOMERS WHERE ID=? LIMIT 1");
			ps.setInt(1, customer.getId());

			rs = ps.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			// throw new AppException(e.getMessage(), e.getCause());

		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return true;
	}

}
