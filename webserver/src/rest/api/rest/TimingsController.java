package rest.api.rest;

import java.util.List;

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
import rest.api.dao.TimingsDAO;
import rest.api.model.Timings;

@Path("/timings")
@Api(tags = { "timings" })
public class TimingsController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find Timings", notes = "Finds timings from the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public List<Timings> getAll() {
		List<Timings> timings = null;
		try {
			TimingsDAO dao = new TimingsDAO();
			timings = dao.getAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return timings;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find One", notes = "Finds an timings with its id in the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public Timings getById(@PathParam("id") int custId) {
		Timings cust;

		try {
			TimingsDAO dao = new TimingsDAO();
			cust = dao.get(custId);
			if (cust == null) {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		return cust;
	}

	// @POST
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// @ApiOperation(value = "Create", notes = "Creates a timings")
	// @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
	// @ApiResponse(code = 500, message = "Internal Server Error") })
	// public Timings createOne(Timings cust) {
	// try {
	// TimingsDAO dao = new TimingsDAO();
	// cust = dao.create(cust);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
	// }
	// return cust;
	// }

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create", notes = "Updates a timings")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public boolean updateOne(Timings cust) {
		boolean flag = false;
		try {
			TimingsDAO dao = new TimingsDAO();
			flag = dao.update(cust);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return flag;
	}

//	@DELETE
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiOperation(value = "Create", notes = "Deletes a timings")
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
//			@ApiResponse(code = 500, message = "Internal Server Error") })
//	public boolean deleteOne(Timings cust) {
//		boolean flag = false;
//		try {
//			TimingsDAO dao = new TimingsDAO();
//			flag = dao.delete(cust);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
//		}
//		return flag;
//	}

}
