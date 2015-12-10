package rest.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import rest.api.exception.AppException;
import rest.api.model.Reservation;
import rest.api.model.ReservationDetails;
import rest.api.model.Timings;
import rest.api.util.DBUtils;

public class TimingsDAO {

	public List<Timings> getAll() throws AppException {

		List<Timings> timings = new ArrayList<Timings>();

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM timings");
			rs = ps.executeQuery();

			while (rs.next()) {
				Timings a = new Timings(rs.getInt("id"), rs.getString("day"), rs.getString("from"), rs.getString("to"));

				timings.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return timings;
	}

	public Timings get(int id) throws AppException {

		Timings timings = null;

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("SELECT * FROM timings WHERE ID=?");
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				timings = new Timings(rs.getInt("id"), rs.getString("day"), rs.getString("from"), rs.getString("to"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return timings;
	}

	public Timings create(Timings timings) throws AppException {
		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("INSERT INTO timings ( `day`, `from`, `to`) VALUES ( ?, ?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, timings.getDay());
			ps.setString(2, timings.getFrom());
			ps.setString(3, timings.getTo());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				timings.setId(rs.getInt(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e.getCause());
		} finally {
			DBUtils.closeResource(ps, rs, conn);
		}

		return timings;
	}

	public boolean update(Timings timings) throws AppException {
		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("UPDATE timings set day=?, `from`=?, `to`=? where id=?",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(4, timings.getId());
			ps.setString(1, timings.getDay());
			ps.setString(2, timings.getFrom());
			ps.setString(3, timings.getTo());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				timings.setId(rs.getInt(1));
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

	public boolean delete(Timings timings) throws AppException {

		Connection conn = DBUtils.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("DELETE * FROM timings WHERE ID=?");
			ps.setInt(1, timings.getId());

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

    public boolean checkTimeValid(ReservationDetails r) throws AppException {
        List <Timings>allTimings = getAll();
        String timestamp = r.getTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z");
        DateTime datetime = formatter.parseDateTime(timestamp);
        LocalTime restime =datetime.toLocalTime();
        for (int i=0; i<allTimings.size(); i++)
        {
            if(datetime.getDayOfWeek()==allTimings.get(i).getId())
            {
                String from_time = allTimings.get(i).getFrom();
                String to_time = allTimings.get(i).getTo();
                DateTimeFormatter formatter2 = DateTimeFormat.forPattern("HH:mm");
                LocalTime time_from = formatter2.parseDateTime(from_time).toLocalTime();
                LocalTime time_to = formatter2.parseDateTime(to_time).toLocalTime();
                if(restime.isAfter(time_from)&& restime.isBefore(time_to))
                    return true;
                else
                    return false;
            }   
        }
        return false;
        }

}
