package rest.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import rest.api.exception.AppException;
import rest.api.model.Assignments;
import rest.api.model.Customers;
import rest.api.model.DiningTables;
import rest.api.model.Reservation;
import rest.api.model.ReservationDetails;
import rest.api.util.DBUtils;

public class DiningTablesDAO {

	private static List<DiningTables> allTables = null;

	static {

		try {
			allTables = getAll();
		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	public static List<DiningTables> getAll() throws AppException {

		List<DiningTables> seatingArea = new ArrayList<DiningTables>();

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM tables");
			rs = ps.executeQuery();

			while (rs.next()) {
				DiningTables a = new DiningTables(rs.getInt("id"), rs.getInt("capacity"));

				seatingArea.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return seatingArea;
	}

	// get list of free tables at the time of a reservation
	public List<DiningTables> getFreeTable(ReservationDetails r) throws AppException {

		String timestamp = r.getTime();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z");
		DateTime restime = formatter.parseDateTime(timestamp); // 5pm 8
		DateTime resplus = restime.plusHours(1); // 6pm 9
		ReservationDAO rdao = new ReservationDAO();
		AssignmentDAO adao = new AssignmentDAO();
		CustomerDAO cdao = new CustomerDAO();

		List<Assignments> assignmentList = new ArrayList<Assignments>();
		List<DiningTables> usedTableList = new ArrayList<DiningTables>();
		try {
			assignmentList = adao.getAll();
			for (int i = 0; i < assignmentList.size(); i++) {

				Reservation res = rdao.get(assignmentList.get(i).getReservation_id());
				if (res == null)
					continue;
				String time = res.getTime();
				DateTime booked = formatter.parseDateTime(time); // 5:30pm
				DateTime bookplus = booked.plusHours(1); // 6:30pm
				// 6:30 < 5:00 6 > 5:30
				// 6:30 < 8 9 > 5:30

				// if (!(resplus.isBefore(booked) && restime.isAfter(bookplus)
				// )){
				// && assignmentList.get(i).getTable_id() != 0)) {
				if (((restime.isAfter(booked) && restime.isBefore(bookplus))
						|| (resplus.isAfter(booked) && resplus.isBefore(bookplus)))
						&& assignmentList.get(i).getTable_id() != 0) {

					usedTableList.add(allTables.get(assignmentList.get(i).getTable_id() - 1)); // tables
																								// are
																								// not
																								// zero
																								// indexed
				}

			}
		} catch (AppException e) {
			throw new AppException(e.getMessage(), e.getCause());
		}

		List<DiningTables> freeTables = new ArrayList<>();

		for (int i = 0; i < allTables.size(); i++) {
			if (!usedTableList.contains(allTables.get(i))) {

				freeTables.add(allTables.get(i));
			}

		}
		return freeTables;
	}

	public DiningTables assignTable(ReservationDetails r, List<DiningTables> tableList) {
		int size = r.getParty_size();
		for (int i = 0; i < tableList.size(); i++) {
			if (tableList.get(i).getSize() >= size) {
				return tableList.get(i);
			}
		}
		return null;
	}

	public Assignments autoAssign(ReservationDetails r, Customers c) throws AppException {
		DiningTables t = null;

		List<DiningTables> tableList = this.getFreeTable(r);
		if (tableList != null && tableList.size() >= 1) { // >=??
			t = this.assignTable(r, tableList);
			if (t != null) {
				return new Assignments(r.getId(), c.getId(), t.getId());
			} else {
				return new Assignments(r.getId(), c.getId(), 0);
			}
		}

		return new Assignments(r.getId(), c.getId(), 0);

	}

	public Assignments adminAssign(ReservationDetails r, Customers c, int table_id) throws AppException {
		DiningTables t = null;
		Assignments a = null;
		List<DiningTables> tableList = this.getFreeTable(r);
		if (tableList != null && tableList.size() > 1) {
			for (int i = 0; i < tableList.size(); i++) {
				if (tableList.get(i).getId() == table_id)
					a = new Assignments(r.getId(), c.getId(), table_id);
			}
		}

		return a;
	}

	public Assignments adminAssign(ReservationDetails r) throws AppException {

		Assignments a = null;
		List<DiningTables> tableList = this.getFreeTable(r);
		if (tableList != null && tableList.size() > 1) {
			for (int i = 0; i < tableList.size(); i++) {
				if (tableList.get(i).getId() == r.getTable_id())
					a = new Assignments(r.getId(), r.getcId(), r.getTable_id());
			}
		}

		return a;
	}

}
