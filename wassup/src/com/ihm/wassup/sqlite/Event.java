package com.ihm.wassup.sqlite;



public class Event {

	private int id;
	private String category;
	private String type1;
	private String type2;
	private String type3;
	
	private String title;
	private String description;
	private String price;
	private String location;

	public Event(String category,String type1,String type2,String type3, String title,
			String description, String price, String location) {
		super();
		this.category = category;
		this.type1 = type1;
		this.type2 = type2;
		this.type3 = type3;
		this.title = title;
		this.description = description;
		this.price = price;
		this.location = location;
	}

	public Event() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getType3() {
		return type3;
	}

	public void setType3(String type3) {
		this.type3 = type3;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titre) {
		this.title = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String prix) {
		this.price = prix;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String toString(){
		return "ID : "+id+"\nTitre : "+title+"\nCategorie : "+category+"\nType1 : "+type1+"\nType2 : "+type2+"\nType3 : "+type3+"\nDescription : "+description+"\nPrix : "+price+"\nLieu : "+location;
	}
}