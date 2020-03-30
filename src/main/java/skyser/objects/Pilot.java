package skyser.objects;

public class Pilot {

    private int experience_hours;
    private String id, license_number;
    
    public Pilot(){}

    public Pilot(String id, int hours, String license) {
    	this.experience_hours = hours;
    	this.license_number = license;
    	this.id = id;
    }
    
	public String getId() {

    	return id;
	}
	public int getExperience_hours() {

    	return experience_hours;
	}
	public void setExperience_hours(int experience_hours) {
		this.experience_hours = experience_hours;
	}

	public String getLicense_number() {
		return license_number;
	}

	public void setLicense_number(String license_number) {
		this.license_number = license_number;
	}

}