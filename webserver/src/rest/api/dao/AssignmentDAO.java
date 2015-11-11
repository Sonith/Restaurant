package rest.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rest.api.exception.AppException;
import rest.api.model.Assignments;
import rest.api.util.DBUtils;

public class AssignmentDAO {

	public List<Assignments> getAll() throws AppException {

		List<Assignments> assignment = new ArrayList<Assignments>();

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM assignments");
			rs = ps.executeQuery();

			while (rs.next()) {
				Assignments a = new Assignments(rs.getInt("reservation_id"), rs.getInt("customer_id"),
						rs.getInt("table_id"));

				assignment.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return assignment;
	}

	public Assignments getByReservation(int id) throws AppException {

		Assignments assignment = null;

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM assignments WHERE reservation_id=?");
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				assignment = new Assignments(rs.getInt("id"), rs.getInt("reservation_id"), rs.getInt("customer_id"),
						rs.getInt("table_id"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return assignment;
	}

	public Assignments getByCustomer(int id) throws AppException {

		Assignments assignment = null;

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM assignments WHERE customer_id=?");
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				assignment = new Assignments(rs.getInt("reservation_id"), rs.getInt("customer_id"),
						rs.getInt("table_id"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return assignment;
	}

	public Assignments create(Assignments assignment) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"INSERT INTO Assignments ( customer_id, reservation_id, table_id) VALUES ( ?, ?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, assignment.getCustomer_id());
			ps.setInt(2, assignment.getReservation_id());
			ps.setInt(3, assignment.getTable_id());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				assignment.setId(rs.getInt(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return assignment;
	}

	public boolean update(Assignments assignment) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("UPDATE assignments set customer_id=?, reservation_id=?, table_id=? where id=?",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(4, assignment.getId());
			ps.setInt(1, assignment.getCustomer_id());
			ps.setInt(2, assignment.getReservation_id());
			ps.setInt(3, assignment.getTable_id());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				assignment.setId(rs.getInt(1));
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

	public boolean delete(Assignments assignment) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		int rs = 0;

		try {
			ps = conn.prepareStatement("DELETE FROM assignments WHERE reservation_id=? LIMIT 1");
			ps.setInt(1, assignment.getReservation_id());

			rs = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			// throw new AppException(e.getMessage(), e.getCause());

		} finally {
			DBUtils.closeResource(ps, conn);
		}

		return rs == 0 ? false : true;
	}

}
