
package rest.api.model;

/**
 *
 * @author Sonith
 */
public class Assignments {

	private int id;
	private int reservation_id;
	private int customer_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReservation_id() {
		return reservation_id;
	}

	public void setReservation_id(int reservation_id) {
		this.reservation_id = reservation_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getTable_id() {
		return table_id;
	}

	public void setTable_id(int table_id) {
		this.table_id = table_id;
	}

	private int table_id;

	public Assignments(int reservation_id, int customer_id, int table_id) {
		super();

		this.reservation_id = reservation_id;
		this.customer_id = customer_id;
		this.table_id = table_id;
	}

	public Assignments(int id, int reservation_id, int customer_id, int table_id) {
		this(reservation_id, customer_id, table_id);
		this.id = id;

	}

}
