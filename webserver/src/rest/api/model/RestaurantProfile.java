package rest.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantProfile {

	private String email;
	private String address;
	private String contact;

	private boolean auto_assign_table;

	private List<Timings> timings;

	public RestaurantProfile() {

	}

	public RestaurantProfile(String email, String address, String contact, boolean auto_assign_table,
			List<Timings> timings) {
		super();
		this.email = email;
		this.address = address;
		this.contact = contact;
		this.auto_assign_table = auto_assign_table;
		this.timings = timings;
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

	public boolean getAuto_assign_table() {
		return auto_assign_table;
	}

	public void setAuto_assign_table(boolean auto_assign_table) {
		this.auto_assign_table = auto_assign_table;
	}

	public List<Timings> getTimings() {
		return timings;
	}

	public void setTimings(List<Timings> timings) {
		this.timings = timings;
	}

}
