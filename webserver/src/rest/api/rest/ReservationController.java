package rest.api.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import rest.api.dao.AdminDAO;
import rest.api.dao.AssignmentDAO;
import rest.api.dao.CustomerDAO;
import rest.api.dao.DiningTablesDAO;
import rest.api.dao.ReservationDAO;
import rest.api.dao.TimingsDAO;
import rest.api.exception.AppException;
import rest.api.model.Admin;
import rest.api.model.Assignments;
import rest.api.model.Customers;
import rest.api.model.DiningTables;
import rest.api.model.ReservationDetails;
import rest.api.model.Reservation;

@Path("/reservation")
@Api(tags = { "reservation" })

public class ReservationController {

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Fetch all Reservations", notes = "Finds all future reservations from the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public List<ReservationDetails> getAll() {
		List<ReservationDetails> reservationList = new ArrayList<ReservationDetails>();
		List<Assignments> assignmentList = new ArrayList<Assignments>();
		ReservationDetails newReservation = new ReservationDetails();
		try {
			ReservationDAO rdao = new ReservationDAO();
			CustomerDAO cdao = new CustomerDAO();
			AssignmentDAO adao = new AssignmentDAO();
			// List<Reservation> res = rdao.getAll();
			assignmentList = adao.getAll();

			for (Assignments a : assignmentList) {
				Reservation r = new Reservation();
				r = rdao.getFutureReservation(a.getReservation_id());
				if (r == null)
					continue;
				Customers c = new Customers();
				c = cdao.get(a.getCustomer_id());

				newReservation = new ReservationDetails(r.getId(), r.getTime(), r.getParty_size(),
						r.getConfirmation_code(), r.getStatus(), c.getName(), c.getEmail(), c.getPhone(), 0);
				reservationList.add(newReservation);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return reservationList;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Fetch one reservation", notes = "Fetch a reservation with its confirmation code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "Not Found") })

	public ReservationDetails getById(@PathParam("id") int confirmation_code) {
		ReservationDetails resv = null;

		try {
			ReservationDAO dao = new ReservationDAO();
			Reservation res = new Reservation();
			res = dao.getbyCode(confirmation_code);
			if (res != null) {
				AssignmentDAO adao = new AssignmentDAO();

				Assignments a = adao.getByReservation(res.getId());
				Customers cust = new Customers();
				CustomerDAO cust_dao = new CustomerDAO();
				cust = cust_dao.get(a.getCustomer_id());

				resv = new ReservationDetails(res.getTime(), res.getParty_size(), res.getConfirmation_code(),
						res.getStatus(), cust.getName(), cust.getEmail(), cust.getPhone(), 0);
			} else {
				throw new WebApplicationException(Status.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		if (resv == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		return resv;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Create Reservation", notes = "Creates a reservation and returns details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ReservationDetails createOne(ReservationDetails resv) {
		ReservationDetails r = null;
		try {
			ReservationDAO rdao = new ReservationDAO();
			AssignmentDAO adao = new AssignmentDAO();
			CustomerDAO cdao = new CustomerDAO();
			DiningTablesDAO ddao = new DiningTablesDAO();
			Customers c = cdao.get(resv.getEmail());
			if (c == null) {
				c = new Customers(resv.getName(), resv.getEmail(), resv.getPhone());
				c = cdao.create(c);
			}

			r = new ReservationDetails(resv.getTime(), resv.getParty_size(), resv.getConfirmation_code(), 0,
					c.getName(), c.getEmail(), c.getPhone(), c.getId());

			boolean valid_time = new TimingsDAO().checkTimeValid(r);
			if (!valid_time) {

			}

			// keep it large and add alpha numerics to avoid collision.. //TODO
			r.setConfirmation_code(ThreadLocalRandom.current().nextInt(1000, 9999 + 1));
			r = rdao.create(r);

			// status = 1 if confirmed, 0 if wait list

			if (AdminDAO.auto) {
				Assignments a = ddao.autoAssign(r, c);

				adao.create(a);
				if (a.getTable_id() != 0) {
					r.setStatus(1);
					r.setTable_id(a.getTable_id());
				} else {
					r.setStatus(0);
				}
				rdao.update(r);

			} else {

				Assignments a = new Assignments(r.getId(), c.getId(), 0);
				adao.create(a);

			}

			return r;

		} catch (Exception e) {
			e.printStackTrace();

			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create", notes = "Updates a reservation")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ReservationDetails updateOne(ReservationDetails resv) {

		try {
			ReservationDAO dao = new ReservationDAO();
			Reservation r = new Reservation(0, resv.getTime(), resv.getParty_size(), resv.getConfirmation_code(),
					resv.getStatus());

			r = dao.update(r);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return resv;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create", notes = "Deletes a reservation")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public boolean deleteOne(@PathParam("id") int confirmation_code) {
		boolean flag = false;
		try {
			ReservationDAO dao = new ReservationDAO();
			flag = dao.delete(confirmation_code);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return flag;
	}

	@GET
	@Path("/now")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Fetch all Reservations", notes = "Finds all reservations from the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public List<Assignments> getReservationNow() {
		// List<ReservationDetails> reservationList = new
		// ArrayList<ReservationDetails>();
		List<Assignments> assignmentList = new ArrayList<Assignments>();
		List<Assignments> currentAssignmentList = new ArrayList<Assignments>();
		ReservationDetails newReservation = new ReservationDetails();
		try {
			ReservationDAO rdao = new ReservationDAO();
			CustomerDAO cdao = new CustomerDAO();
			AssignmentDAO adao = new AssignmentDAO();
			// List<Reservation> res = rdao.getAll();
			assignmentList = adao.getAll();

			for (Assignments a : assignmentList) {
				Reservation r = new Reservation();
				r = rdao.get(a.getReservation_id());
				if (r == null) {
					continue;
				}
				Customers c = new Customers();
				c = cdao.get(a.getCustomer_id());

				newReservation = new ReservationDetails(r.getTime(), r.getParty_size(), r.getConfirmation_code(),
						r.getStatus(), c.getName(), c.getEmail(), c.getPhone(), 0);

				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z");
				DateTime datetime = formatter.parseDateTime(newReservation.getTime());
				LocalTime restime = datetime.toLocalTime();
				LocalTime now = new LocalTime();
				if (restime.isAfter(now.minusHours(1)) && restime.isBefore(now.plusHours(1)))
					currentAssignmentList.add(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return currentAssignmentList;
	}

	// TODO Should be a GET
	@PUT
	@Path("/free")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Free Tables", notes = "Get list of free tables for a reservation")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public List<DiningTables> freeTable(ReservationDetails res) throws AppException {
		List<DiningTables> tables;
		// Assignments a = new DiningTablesDAO().adminAssign(res, new
		// CustomerDAO().get(res.getcId()), res.getTable_id());
		try {
			tables = new DiningTablesDAO().getFreeTable(res);

		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return tables;
	}

	@PUT
	@Path("/assign")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Assign Table", notes = "Assign a table selected by the owner")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public Assignments assignTable(ReservationDetails res) {
		Assignments a;
		Reservation r;
		AssignmentDAO adao = new AssignmentDAO();
		ReservationDAO rdao = new ReservationDAO();
		try {
			a = adao.getByReservation(res.getId());
			a.setTable_id(res.getTable_id());

			r = rdao.get(res.getId());
			r.setStatus(1);
			rdao.update(r);
			adao.update(a);

		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return a;
	}

}
