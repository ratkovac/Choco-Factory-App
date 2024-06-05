package beans;

public class Factory {
	private String id;
	private String name;
	private String status;
	private String location;
	private String pathToLogo;
	private double rate;
	private boolean isDeleted;
	private double workingTime;
	
	public Factory() {
    }
	
	public Factory(String id, String name, String status, String location, String pathToLogo, double rate,
			boolean isDeleted, double workingTime) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.location = location;
		this.pathToLogo = pathToLogo;
		this.rate = rate;
		this.isDeleted = isDeleted;
		this.workingTime = workingTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPathToLogo() {
		return pathToLogo;
	}

	public void setPathToLogo(String pathToLogo) {
		this.pathToLogo = pathToLogo;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public double getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(double workingTime) {
		this.workingTime = workingTime;
	}
	
	
	
}