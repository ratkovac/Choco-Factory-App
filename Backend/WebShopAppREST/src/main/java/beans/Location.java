package beans;

public class Location {
    private String id;
    private double lat;
    private double lng;
    private String address; //Street and number, city/place, postal code

    public Location() {
        super();
    }
    public Location(String id, double lat, double lng, String address) {
        super();
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String adress) {
        this.address = adress;
    }
    @Override
    public String toString() {
        return "Location [id=" + id + ", lat=" + lat + ", lng=" + lng + ", address=" + address + "]";
    }


}
