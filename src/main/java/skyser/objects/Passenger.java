package skyser.objects;

import java.util.Date;

public class Passenger {

	private String id, first_name, last_name, email, password, phone_number, home_address, billing_information, picture_path;

	public Passenger(){}

	public Passenger(String id, String first_name, String last_name, String email, String password, String phone_number,String home,
					 String billing_information, String picture) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
		this.phone_number = phone_number;
		this.home_address = home;
		this.billing_information = billing_information;
		this.picture_path = picture;
	}

	public String getId() {
		return id;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getHome_address() {
		return home_address;
	}
	public void setHome_address(String home_address) {
		this.home_address = home_address;
	}
	public String getBilling_information() {
		return billing_information;
	}
	public void setBilling_information(String billing_information) {
		this.billing_information = billing_information;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getPicture() {
		return picture_path;
	}

	public void setPicture(String picture_path) {
		this.picture_path = picture_path;
	}
}
