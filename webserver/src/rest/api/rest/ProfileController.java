package rest.api.rest;

import java.util.ArrayList;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import rest.api.dao.AdminDAO;
import rest.api.dao.CustomerDAO;
import rest.api.dao.ReservationDAO;
import rest.api.dao.TimingsDAO;
import rest.api.model.Admin;
import rest.api.model.Customers;
import rest.api.model.ReservationDetails;
import rest.api.model.RestaurantProfile;
import rest.api.model.Timings;
import rest.api.model.Reservation;

@Path("/profile")
@Api(tags = { "restaurant profile" })

public class ProfileController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Fetch Restaurant Profile", notes = "Fetches the restaurant profile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public RestaurantProfile getProfile() {
		RestaurantProfile rProfile = null;

		try {
			List<Timings> timings = null;

			TimingsDAO dao = new TimingsDAO();
			timings = dao.getAll();

			rProfile = new RestaurantProfile(AdminDAO.getEmail(), AdminDAO.getAddress(), AdminDAO.getContact(),
					AdminDAO.auto, timings);

		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return rProfile;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Restaurant Profile Update", notes = "Updates the restaurant profile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public RestaurantProfile saveProfile(RestaurantProfile restaurantProfile) {

		try {

			Admin a = new Admin(restaurantProfile.getEmail(), restaurantProfile.getAddress(),
					restaurantProfile.getContact(), restaurantProfile.getAuto_assign_table() ? "1" : "0", null);
			new AdminDAO().update(a);
			List<Timings> timings = restaurantProfile.getTimings();
			// Update every day
			for (int i = 0; i < timings.size(); i++)
				new TimingsDAO().update(restaurantProfile.getTimings().get(i));

		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return restaurantProfile;
	}

}
