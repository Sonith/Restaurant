/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author Sonith
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Admin {

	private String email;
	private String address;
	private String contact;

	private String auto_assign_table;
	private String password;

	public Admin() {

	}

	public Admin(String email, String address, String contact, String auto_assign_table, String password) {
		super();

		this.email = email;
		this.address = address;
		this.contact = contact;
		this.auto_assign_table = auto_assign_table;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Boolean isAuto_assign_table() {
		return auto_assign_table.equals("1") ? true : false;
	}

	public void setAuto_assign_table(String auto_assign_table) {
		this.auto_assign_table = auto_assign_table;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
