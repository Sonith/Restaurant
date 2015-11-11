package rest.api.model;

import java.sql.Date;

public class DiningTables {

	private int id;
	private int size;


	public DiningTables() {

	}

	public DiningTables(int id, int size) {
		super();
		this.id = id;
		this.size = size;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}



}
