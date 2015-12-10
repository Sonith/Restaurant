package rest.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PathParam;

import rest.api.exception.AppException;
import rest.api.model.ReservationDetails;
import rest.api.model.Assignments;
import rest.api.model.Reservation;
import rest.api.util.DBUtils;

public class ReservationDAO {

	public List<Reservation> getAll() throws AppException {

		List<Reservation> reservation = new ArrayList<Reservation>();

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM reservations");
			rs = ps.executeQuery();

			while (rs.next()) {

				Reservation a = new Reservation(rs.getInt("id"), rs.getString("time"), rs.getInt("Party_size"),
						rs.getInt("confirmation_code"), rs.getInt("status"));

				reservation.add(a);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return reservation;
	}

	
	public Reservation get(int id) throws AppException {

		Reservation reservation = null;

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM reservations WHERE id=?");
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				reservation = new Reservation(rs.getInt("id"), rs.getString("time"), rs.getInt("Party_size"),
						rs.getInt("confirmation_code"), rs.getInt("status"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return reservation;
	}

	public Reservation getFutureReservation(int code) throws AppException {

		Reservation reservation = null;

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM reservations WHERE id=? and DATE(time) >= DATE(NOW())");
			ps.setInt(1, code);

			rs = ps.executeQuery();

			if (rs.next()) {
				reservation = new Reservation(rs.getInt("id"), rs.getString("time"), rs.getInt("Party_size"),
						rs.getInt("confirmation_code"), rs.getInt("status"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return reservation;
	}

	public Reservation getbyCode(int code) throws AppException {

		Reservation reservation = null;

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM reservations WHERE confirmation_code=?");
			ps.setInt(1, code);

			rs = ps.executeQuery();

			if (rs.next()) {
				reservation = new Reservation(rs.getInt("id"), rs.getString("time"), rs.getInt("Party_size"),
						rs.getInt("confirmation_code"), rs.getInt("status"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return reservation;
	}

	public ReservationDetails create(ReservationDetails reservation) throws AppException {
		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"INSERT INTO reservations ( time, party_size, confirmation_code, status) VALUES ( ?, ?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);

			ps.setString(1, reservation.getTime());
			ps.setInt(2, reservation.getParty_size());
			ps.setInt(3, reservation.getConfirmation_code());
			ps.setInt(4, reservation.getStatus());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				reservation.setId(rs.getInt(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return reservation;
	}

	public ReservationDetails update(ReservationDetails reservation) throws AppException {
		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"UPDATE reservations set time=?, party_size=? ,status=? where confirmation_code=?");
			ps.setInt(4, reservation.getConfirmation_code());
			ps.setString(1, reservation.getTime());
			ps.setInt(2, reservation.getParty_size());
			ps.setInt(3, reservation.getStatus());

			ps.executeUpdate();
			// TODO get table info and fill the reservation object before
			// returning

		} catch (SQLException e) {
			e.printStackTrace();

			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return reservation;
	}

	public Reservation update(Reservation reservation) throws AppException {
		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"UPDATE reservations set time=?, party_size=? ,status=? where confirmation_code=?");
			ps.setInt(4, reservation.getConfirmation_code());
			ps.setString(1, reservation.getTime());
			ps.setInt(2, reservation.getParty_size());
			ps.setInt(3, reservation.getStatus());

			ps.executeUpdate();
			// TODO get table info and fill the reservation object before
			// returning

		} catch (SQLException e) {
			e.printStackTrace();

			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return reservation;
	}

	public boolean delete(int code) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		int rs = 0;

		try {

			ReservationDAO rdao = new ReservationDAO();
			Reservation r = rdao.getbyCode(code);
			AssignmentDAO adao = new AssignmentDAO();
			Assignments a = adao.getByReservation(r.getId());
			adao.delete(a);

			ps = conn.prepareStatement("DELETE FROM reservations WHERE confirmation_code=? LIMIT 1");
			ps.setInt(1, code);

			rs = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			// throw new AppException(e.getMessage(), e.getCause());

		} finally {
			DBUtils.closeResource(ps, conn);
		}

		return rs == 1 ? true : false;
	}

}
