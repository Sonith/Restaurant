package rest.api.rest;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import javax.ws.rs.Consumes;
import rest.api.dao.CustomerDAO;
import rest.api.exception.AppException;
import rest.api.model.Customers;

@Path("/customer")
@Api(tags = { "customer" })
public class CustomerController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "List Customers", notes = "Lists all customers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public List<Customers> getAll() {
		List<Customers> customer = null;
		try {
			CustomerDAO dao = new CustomerDAO();
			customer = dao.getAll();
		} catch (AppException e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		return customer;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find Customer with id", notes = "Finds an customer with its id in the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public Customers getById(@PathParam("id") int custId) {
		Customers cust;

		try {
			CustomerDAO dao = new CustomerDAO();
			cust = dao.get(custId);
			if (cust == null) {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		return cust;
	}

	// @POST
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// @ApiOperation(value = "Create customer", notes = "Creates a customer")
	// @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
	// @ApiResponse(code = 500, message = "Internal Server Error") })
	// public Customers createOne(Customers cust) {
	// try {
	// CustomerDAO dao = new CustomerDAO();
	// cust = dao.create(cust);
	// } catch (AppException e) {
	// e.printStackTrace();
	// throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
	// }
	// return cust;
	// }

	// @PUT
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// @ApiOperation(value = "update customer", notes = "Updates a customer")
	// @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
	// @ApiResponse(code = 500, message = "Internal Server Error") })
	// public boolean updateOne(Customers cust) {
	// boolean flag = false;
	// try {
	// CustomerDAO dao = new CustomerDAO();
	// flag = dao.update(cust);
	// } catch (AppException e) {
	// e.printStackTrace();
	// throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
	// }
	// return flag;
	// }

	// @DELETE
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// @ApiOperation(value = "Create", notes = "Deletes a customer")
	// @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
	// @ApiResponse(code = 500, message = "Internal Server Error") })
	// public boolean deleteOne(Customers cust) {
	// boolean flag = false;
	// try {
	// CustomerDAO dao = new CustomerDAO();
	// flag = dao.delete(cust);
	// } catch (AppException e) {
	// e.printStackTrace();
	// throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
	// }
	// return flag;
	// }

}
