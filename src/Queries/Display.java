package Queries;

public class Display {
    private int LID;
    private String type;
    private double longitude;
    private double latitude;
    private String address;
    private String postalCode;
    private String city;
    private String country;
    private int hostUID;
    private double price;
    private boolean isAvailable;
    private String date;
    private double distance;

    public Display(int LID, String type, double longitude, double latitude, String address, String postalCode,
            String city, String country, int hostUID, double price, boolean isAvailable, String date, double distance) {
        this.LID = LID;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.hostUID = hostUID;
        this.price = price;
        this.isAvailable = isAvailable;
        this.date = date;
        this.distance = distance;
    }

    // Getters and Setters (omitted for brevity)

    // Display method for the Display object
    public void displayDetails() {
        System.out.println("Listing ID (LID): " + LID);
        System.out.println("Type: " + type);
        System.out.println("Longitude: " + longitude);
        System.out.println("Latitude: " + latitude);
        System.out.println("Address: " + address);
        System.out.println("Postal Code: " + postalCode);
        System.out.println("City: " + city);
        System.out.println("Country: " + country);
        System.out.println("Host UID: " + hostUID);
        System.out.println("Price: " + price);
        System.out.println("Availability: " + (isAvailable ? "Available" : "Not Available"));
        System.out.println("Date: " + date);
        System.out.println("Distance: " + distance);
        System.out.println("-------------------------");
    }
}
