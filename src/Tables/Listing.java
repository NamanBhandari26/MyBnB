package Tables;

import java.sql.*;

public class Listing {
    // Database connection attribute
    private Connection connection;

    // Table fields
    private int LID;
    private String Type;
    private double Longitude;
    private double Latitude;
    private String Address;
    private String PostalCode;
    private String City;
    private String Country;
    private int HostUID;

    // Constructor with database connection
    public Listing(Connection connection, String Type, double Longitude, double Latitude, String Address, String PostalCode, String City, String Country, int HostUID) {
        this.connection = connection;
        this.Type = Type;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.Address = Address;
        this.PostalCode = PostalCode;
        this.City = City;
        this.Country = Country;
        this.HostUID = HostUID;
    }

    // Create (Insert) operation
    public void insert() {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Listing (Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, this.Type);
            statement.setDouble(2, this.Longitude);
            statement.setDouble(3, this.Latitude);
            statement.setString(4, this.Address);
            statement.setString(5, this.PostalCode);
            statement.setString(6, this.City);
            statement.setString(7, this.Country);
            statement.setInt(8, this.HostUID);
            statement.executeUpdate();

            // Get the generated LID (if applicable)
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.LID = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (Select) operation by LID
    public static Listing selectById(Connection connection, int LID) {
        Listing listing = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Listing WHERE LID = ?")) {
            statement.setInt(1, LID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String Type = resultSet.getString("Type");
                    double Longitude = resultSet.getDouble("Longitude");
                    double Latitude = resultSet.getDouble("Latitude");
                    String Address = resultSet.getString("Address");
                    String PostalCode = resultSet.getString("PostalCode");
                    String City = resultSet.getString("City");
                    String Country = resultSet.getString("Country");
                    int HostUID = resultSet.getInt("HostUID");
                    listing = new Listing(connection, Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID);
                    listing.setLID(LID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listing;
    }

    // Update operation
    public void update() {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE Listing SET Type = ?, Longitude = ?, Latitude = ?, Address = ?, PostalCode = ?, City = ?, Country = ?, HostUID = ? WHERE LID = ?")) {
            statement.setString(1, this.Type);
            statement.setDouble(2, this.Longitude);
            statement.setDouble(3, this.Latitude);
            statement.setString(4, this.Address);
            statement.setString(5, this.PostalCode);
            statement.setString(6, this.City);
            statement.setString(7, this.Country);
            statement.setInt(8, this.HostUID);
            statement.setInt(9, this.LID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete operation
    public void delete() {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Listing WHERE LID = ?")) {
            statement.setInt(1, this.LID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to create the "Listing" table if it doesn't exist
    public static void createTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Listing (" +
                    "LID INT AUTO_INCREMENT PRIMARY KEY," +
                    "Type VARCHAR(255) NOT NULL," +
                    "Longitude DOUBLE NOT NULL," +
                    "Latitude DOUBLE NOT NULL," +
                    "Address VARCHAR(255) NOT NULL," +
                    "PostalCode VARCHAR(20) NOT NULL," +
                    "City VARCHAR(100) NOT NULL," +
                    "Country VARCHAR(100) NOT NULL," +
                    "HostUID INT NOT NULL," +
                    "FOREIGN KEY (HostUID) REFERENCES User(UID))"; // "User" should be replaced with the actual table name of the User table
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to drop the "Listing" table if it exists
    public static void dropTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = "DROP TABLE IF EXISTS Listing";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public int getLID() {
        return LID;
    }

    public void setLID(int LID) {
        this.LID = LID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String PostalCode) {
        this.PostalCode = PostalCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public int getHostUID() {
        return HostUID;
    }

    public void setHostUID(int HostUID) {
        this.HostUID = HostUID;
    }
}
