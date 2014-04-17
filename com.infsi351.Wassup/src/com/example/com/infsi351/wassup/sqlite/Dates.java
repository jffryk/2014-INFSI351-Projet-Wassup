package com.example.com.infsi351.wassup.sqlite;


public class Dates {

	private int id;
	private int idEvent;
	private String date;
	
	public Dates(int idEvent, String date) {
		super();
		this.idEvent = idEvent;
		this.date = date;
	}
	
	public Dates() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}	
}
