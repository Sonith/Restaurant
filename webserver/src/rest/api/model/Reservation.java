package rest.api.model;

import java.sql.Date;

public class Reservation {
	private int id;
	private String time;
	private int party_size;
	private int confirmation_code;
	private int status;


	public Reservation() {

	}

	public Reservation(int id, String time, int party_size) {
		super();
		this.id = id;
		this.time = time;
		this.party_size = party_size;


	}

	public Reservation(int id, String when, int party_size, int confirmation_code, int status) {
		
		this(id, when, party_size);
		this.confirmation_code = confirmation_code;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setWhen(String time) {
		this.time = time;
	}

	public int getParty_size() {
		return party_size;
	}

	public void setParty_size(int party_size) {
		this.party_size = party_size;
	}



	public int getConfirmation_code() {
		return confirmation_code;
	}

	public void setConfirmation_code(int confirmation_code) {
		this.confirmation_code = confirmation_code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



}
