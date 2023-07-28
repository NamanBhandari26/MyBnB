package Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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
  public Listing(Connection connection, String Type, double Longitude, double Latitude,
      String Address, String PostalCode, String City, String Country, int HostUID) {
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

  // Static method to create (Insert) operation
  public static int insert(Connection connection, String Type, double Longitude, double Latitude,
      String Address, String PostalCode, String City, String Country, int HostUID) {
    boolean isHost = false;

    try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE UID = ?")) {
      statement.setInt(1, HostUID);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          isHost = resultSet.getBoolean("isHost");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (!isHost) {
      return -1;
    }

    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO Listing (Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, Type);
      statement.setDouble(2, Longitude);
      statement.setDouble(3, Latitude);
      statement.setString(4, Address);
      statement.setString(5, PostalCode);
      statement.setString(6, City);
      statement.setString(7, Country);
      statement.setInt(8, HostUID);
      statement.executeUpdate();

      // Get the generated LID (if applicable)
      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
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
          listing = new Listing(connection, Type, Longitude, Latitude, Address, PostalCode, City,
              Country, HostUID);
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
    try (PreparedStatement statement = connection.prepareStatement(
        "UPDATE Listing SET Type = ?, Longitude = ?, Latitude = ?, Address = ?, PostalCode = ?, City = ?, Country = ?, HostUID = ? WHERE LID = ?")) {
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

  public void delete() {
    try {
      // Delete records from dependent tables first

      // 1. Delete from Rented table
      try (PreparedStatement rentedStatement = connection.prepareStatement("DELETE FROM Rented WHERE LID = ?")) {
        rentedStatement.setInt(1, this.LID);
        rentedStatement.executeUpdate();
      }

      // 2. Delete from ListingReview table
      try (PreparedStatement reviewStatement = connection.prepareStatement("DELETE FROM ListingReview WHERE LID = ?")) {
        reviewStatement.setInt(1, this.LID);
        reviewStatement.executeUpdate();
      }

      // 3. Delete from Has table
      try (PreparedStatement hasStatement = connection.prepareStatement("DELETE FROM Has WHERE LID = ?")) {
        hasStatement.setInt(1, this.LID);
        hasStatement.executeUpdate();
      }

      // 4. Delete from Availability table
      try (PreparedStatement availabilityStatement = connection
          .prepareStatement("DELETE FROM Availability WHERE LID = ?")) {
        availabilityStatement.setInt(1, this.LID);
        availabilityStatement.executeUpdate();
      }

      // After deleting dependent records, delete the record from the Listing table
      try (PreparedStatement listingStatement = connection.prepareStatement("DELETE FROM Listing WHERE LID = ?")) {
        listingStatement.setInt(1, this.LID);
        listingStatement.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to create the "Listing" table if it doesn't exist (static approach)
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
          "FOREIGN KEY (HostUID) REFERENCES User(UID))";

      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to drop the "Listing" table if it exists (static approach)
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

  // Functions to support Operations

  // Function to check if the listing is available for a specific date range
  public boolean isAvailable(Connection connection, String startDate, String endDate) {
    try (PreparedStatement statement = connection.prepareStatement(
        "SELECT COUNT(*) FROM Availability WHERE LID = ? AND Date BETWEEN ? AND ? AND isAvailable = true")) {
      statement.setInt(1, this.LID);
      statement.setString(2, startDate);
      statement.setString(3, endDate);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int count = resultSet.getInt(1);
          return count == getNumberOfDaysInclusive(startDate, endDate);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private int getNumberOfDaysInclusive(String startDate, String endDate) {
    // Convert string dates to java.util.Date objects
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.util.Date start = null;
    java.util.Date end = null;
    try {
      start = sdf.parse(startDate);
      end = sdf.parse(endDate);
    } catch (java.text.ParseException e) {
      e.printStackTrace();
      return 0;
    }

    // Calculate the number of days between start and end date inclusively
    long diff = end.getTime() - start.getTime();
    return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
  }

  // Function to set the price for a specific date range
  public void setPrice(Connection connection, String startDate, String endDate, double newPrice) {
    try {
      // Convert the string dates to LocalDate objects for easier date manipulation
      LocalDate start = LocalDate.parse(startDate);
      LocalDate end = LocalDate.parse(endDate);

      // Loop through each day inclusively between the start and end date
      LocalDate currentDay = start;
      while (!currentDay.isAfter(end)) {
        try (PreparedStatement statement = connection.prepareStatement(
            "UPDATE Availability SET Price = ? WHERE LID = ? AND Date = ? AND isAvailable = true")) {
          statement.setDouble(1, newPrice);
          statement.setInt(2, this.LID);
          statement.setString(3, currentDay.toString());
          statement.executeUpdate();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        // Move to the next day
        currentDay = currentDay.plus(1, ChronoUnit.DAYS);
      }
    } catch (java.time.format.DateTimeParseException e) {
      e.printStackTrace();
    }
  }

  // Function to update the availability of the listing for a specific date range
  public void updateAvailability(Connection connection, String startDate, String endDate,
      boolean isAvailable) {
    try {
      // Convert the string dates to LocalDate objects for easier date manipulation
      LocalDate start = LocalDate.parse(startDate);
      LocalDate end = LocalDate.parse(endDate);

      // Loop through each day inclusively between the start and end date
      LocalDate currentDay = start;
      while (!currentDay.isAfter(end)) {
        try (PreparedStatement statement = connection.prepareStatement(
            "UPDATE Availability SET isAvailable = ? WHERE LID = ? AND Date = ?")) {
          statement.setBoolean(1, isAvailable);
          statement.setInt(2, this.LID);
          statement.setString(3, currentDay.toString());
          statement.executeUpdate();
        } catch (SQLException e) {
          e.printStackTrace();
        }

        // Move to the next day
        currentDay = currentDay.plus(1, ChronoUnit.DAYS);
      }
    } catch (java.time.format.DateTimeParseException e) {
      e.printStackTrace();
    }
  }

  // Check if the listing has any future bookings
  public boolean hasFutureBookings(Connection connection) {
    String sql = "SELECT COUNT(*) AS count FROM Rented WHERE LID = ? AND StartDate > ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, this.LID);

      // Get the current date
      Calendar calendar = Calendar.getInstance();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String currentDateStr = dateFormat.format(calendar.getTime());
      stmt.setString(2, currentDateStr);

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        int count = rs.getInt("count");
        return count > 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  // Check if the listing is booked in the date range [startDate, endDate]
  public boolean isBooked(Connection connection, String startDate, String endDate) {
    String sql = "SELECT COUNT(*) AS count FROM Rented WHERE LID = ? AND StartDate <= ? AND EndDate >= ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setInt(1, this.LID);
      stmt.setString(2, endDate);
      stmt.setString(3, startDate);

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        int count = rs.getInt("count");
        return count > 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

}
