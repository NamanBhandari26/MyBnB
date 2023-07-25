package Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Availability {
  // Database connection attribute
  private Connection connection;

  // Table fields
  private int LID;
  private String Date;
  private double Price;

  // Constructor with database connection
  public Availability(Connection connection, int LID, String Date, double Price) {
    this.connection = connection;
    this.LID = LID;
    this.Date = Date;
    this.Price = Price;
  }

  // Create (Insert) operation
  public void insert() {
    try (PreparedStatement statement = connection
        .prepareStatement("INSERT INTO Availability (LID, Date, Price) VALUES (?, ?, ?)")) {
      statement.setInt(1, this.LID);
      statement.setString(2, this.Date);
      statement.setDouble(3, this.Price);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void insert(Connection connection, int LID, String Date, double Price) {
    try (PreparedStatement statement = connection
        .prepareStatement("INSERT INTO Availability (LID, Date, Price) VALUES (?, ?, ?)")) {
      statement.setInt(1, LID);
      statement.setString(2, Date);
      statement.setDouble(3, Price);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Read (Select) operation by LID and Date
  public static Availability selectById(Connection connection, int LID, String Date) {
    Availability availability = null;
    try (PreparedStatement statement =
        connection.prepareStatement("SELECT * FROM Availability WHERE LID = ? AND Date = ?")) {
      statement.setInt(1, LID);
      statement.setString(2, Date);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          double Price = resultSet.getDouble("Price");
          availability = new Availability(connection, LID, Date, Price);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return availability;
  }

  // Update operation
  public void update() {
    try (PreparedStatement statement = connection
        .prepareStatement("UPDATE Availability SET Price = ? WHERE LID = ? AND Date = ?")) {
      statement.setDouble(1, this.Price);
      statement.setInt(2, this.LID);
      statement.setString(3, this.Date);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Delete operation
  public void delete() {
    try (PreparedStatement statement =
        connection.prepareStatement("DELETE FROM Availability WHERE LID = ? AND Date = ?")) {
      statement.setInt(1, this.LID);
      statement.setString(2, this.Date);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to create the "Availability" table if it doesn't exist
  public static void createTable(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS Availability (" + "LID INT NOT NULL,"
          + "Date DATE NOT NULL," + "Price DOUBLE NOT NULL," + "PRIMARY KEY (LID, Date),"
          + "FOREIGN KEY (LID) REFERENCES Listing(LID))"; // "Listing" should be replaced with the
                                                          // actual table name of the Listing table
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to drop the "Availability" table if it exists
  public static void dropTable(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String sql = "DROP TABLE IF EXISTS Availability";
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

  public String getDate() {
    return Date;
  }

  public void setDate(String Date) {
    this.Date = Date;
  }

  public double getPrice() {
    return Price;
  }

  public void setPrice(double Price) {
    this.Price = Price;
  }
}
