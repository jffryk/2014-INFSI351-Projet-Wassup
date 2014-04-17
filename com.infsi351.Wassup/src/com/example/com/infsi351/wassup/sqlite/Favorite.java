package com.example.com.infsi351.wassup.sqlite;


public class Favorite {

	private int id;
	private int idEvent;
	private int idProfil;
	
	public Favorite() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public Favorite(int idEvent, int idProfil) {
		super();
		this.idEvent = idEvent;
		this.idProfil = idProfil;
	}

	public int getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}

	public int getIdProfil() {
		return idProfil;
	}

	public void setIdProfil(int idProfil) {
		this.idProfil = idProfil;
	}
}