package rest.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import rest.api.dao.AdminDAO;
import rest.api.exception.AppException;
import rest.api.model.Admin;
import rest.api.model.Login;

@Path("/admin")
@Api(tags = { "admin" })
public class AdminController {

	
	
	@GET
	//@Path("/login")

	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find Admin", notes = "Finds admin from the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	public Admin listAdmin() {

		Admin admin = null;
		try {
			admin = AdminDAO.getAdmin();
		} catch (AppException e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		
		return admin;
	}
	
	
	@POST
	//@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Validate owner", notes = "Validates if user is owner")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized User")})
	public Admin validateAdmin(Login login) {

		Admin admin = null;
		try {
			AdminDAO dao = new AdminDAO();
			admin = dao.validateAdmin(login.getEmail(), login.getPassword());
		} catch (AppException e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		if (admin==null)
			throw new WebApplicationException(Status.UNAUTHORIZED);

		return admin;
	}

}
