package Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Has {
  // Database connection attribute
  private Connection connection;

  // Table fields
  private int LID;
  private int AID;

  // Constructor with database connection
  public Has(Connection connection, int LID, int AID) {
    this.connection = connection;
    this.LID = LID;
    this.AID = AID;
  }

  // Create (Insert) operation
  public void insert() {
    try (PreparedStatement statement =
        connection.prepareStatement("INSERT INTO Has (LID, AID) VALUES (?, ?)")) {
      statement.setInt(1, this.LID);
      statement.setInt(2, this.AID);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void insert(Connection connection, int LID, int AID) {
    try (PreparedStatement statement =
        connection.prepareStatement("INSERT INTO Has (LID, AID) VALUES (?, ?)")) {
      statement.setInt(1, LID);
      statement.setInt(2, AID);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Read (Select) operation by LID and AID
  public static Has selectById(Connection connection, int LID, int AID) {
    Has has = null;
    try (PreparedStatement statement =
        connection.prepareStatement("SELECT * FROM Has WHERE LID = ? AND AID = ?")) {
      statement.setInt(1, LID);
      statement.setInt(2, AID);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          has = new Has(connection, LID, AID);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return has;
  }

  // Delete operation
  public void delete() {
    try (PreparedStatement statement =
        connection.prepareStatement("DELETE FROM Has WHERE LID = ? AND AID = ?")) {
      statement.setInt(1, this.LID);
      statement.setInt(2, this.AID);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to create the "Has" table if it doesn't exist
  public static void createTable(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS Has (" + "LID INT NOT NULL," + "AID INT NOT NULL,"
          + "PRIMARY KEY (LID, AID)," + "FOREIGN KEY (LID) REFERENCES Listing(LID),"
          + "FOREIGN KEY (AID) REFERENCES Amenities(AID))"; // "Listing" and "Amenities" should be
                                                            // replaced with the actual table names
                                                            // of the Listing and Amenities tables,
                                                            // respectively
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to drop the "Has" table if it exists
  public static void dropTable(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String sql = "DROP TABLE IF EXISTS Has";
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

  public int getAID() {
    return AID;
  }

  public void setAID(int AID) {
    this.AID = AID;
  }
}
