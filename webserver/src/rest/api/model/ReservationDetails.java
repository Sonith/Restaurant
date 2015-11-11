/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.api.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import rest.api.dao.CustomerDAO;
import rest.api.exception.AppException;

/**
 *
 * @author Sonith
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationDetails {
	// reservation
	private int id;

	private String time;
	private int party_size;
	private int confirmation_code;
	private int status;

	// customer
	private int cid;
	private String name;
	private String email;
	private String phone;
	
	private int table_id;

	public ReservationDetails(int id, String time, int party_size, int confirmation_code, int status, int cid,
			String name, String email, String phone, int table_id) {
		super();
		this.id = id;
		this.time = time;
		this.party_size = party_size;
		this.confirmation_code = confirmation_code;
		this.status = status;
		this.cid = cid;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.table_id = table_id;
	}

	public ReservationDetails() {

	}

	public ReservationDetails(int id, String time, int party_size, int confirmation_code, int status, String name,
			String email, String phone, int cid) {
		super();
		this.id = id;
		this.time = time;
		this.party_size = party_size;
		this.confirmation_code = confirmation_code;
		this.status = status;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.cid = cid;
	}

	public ReservationDetails(String time, int party_size, int confirmation_code, int status, String name, String email,
			String phone, int cid) {
		super();
		this.time = time;
		this.party_size = party_size;
		this.confirmation_code = confirmation_code;
		this.status = status;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.cid = cid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getcId() {
		return cid;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getTable_id() {
		return table_id;
	}

	public void setTable_id(int table_id) {
		this.table_id = table_id;
	}

}
