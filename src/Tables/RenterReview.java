package Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RenterReview {
  // Database connection attribute
  private Connection connection;

  // Table fields
  private int HostUID;
  private int RenterUID;
  private int rating;
  private String comment;

  // Constructor with database connection
  public RenterReview(Connection connection, int HostUID, int RenterUID, int rating,
      String comment) {
    this.connection = connection;
    this.HostUID = HostUID;
    this.RenterUID = RenterUID;
    this.rating = rating;
    this.comment = comment;
  }

  // Create (Insert) operation
  public void insert() {

      boolean hasHosted = false;

      try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Rented NATRUAL JOIN Listing WHERE UID = ? AND HostUID = ?")) {
        statement.setInt(1, this.RenterUID);
        statement.setInt(2, this.HostUID);
        try (ResultSet resultSet = statement.executeQuery()) {
          if (resultSet.next()) {
            hasHosted = true;
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

      if (!hasHosted) {
        return;
      }

    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO RenterReview (HostUID, RenterUID, rating, comment) VALUES (?, ?, ?, ?)")) {
      statement.setInt(1, this.HostUID);
      statement.setInt(2, this.RenterUID);
      statement.setInt(3, this.rating);
      statement.setString(4, this.comment);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void insert(Connection connection, int HostUID, int RenterUID, int rating,
      String comment) {

      boolean hasHosted = false;

      try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Rented NATRUAL JOIN Listing WHERE UID = ? AND HostUID = ?")) {
        statement.setInt(1, RenterUID);
        statement.setInt(2, HostUID);
        try (ResultSet resultSet = statement.executeQuery()) {
          if (resultSet.next()) {
            hasHosted = true;
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

      if (!hasHosted) {
        return;
      }
    
    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO RenterReview (HostUID, RenterUID, rating, comment) VALUES (?, ?, ?, ?)")) {
      statement.setInt(1, HostUID);
      statement.setInt(2, RenterUID);
      statement.setInt(3, rating);
      statement.setString(4, comment);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Read (Select) operation by HostUID and RenterUID
  public static RenterReview selectById(Connection connection, int HostUID, int RenterUID) {
    RenterReview review = null;
    try (PreparedStatement statement = connection
        .prepareStatement("SELECT * FROM RenterReview WHERE HostUID = ? AND RenterUID = ?")) {
      statement.setInt(1, HostUID);
      statement.setInt(2, RenterUID);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int rating = resultSet.getInt("rating");
          String comment = resultSet.getString("comment");
          review = new RenterReview(connection, HostUID, RenterUID, rating, comment);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return review;
  }

  // Update operation
  public void update() {
    try (PreparedStatement statement = connection.prepareStatement(
        "UPDATE RenterReview SET rating = ?, comment = ? WHERE HostUID = ? AND RenterUID = ?")) {
      statement.setInt(1, this.rating);
      statement.setString(2, this.comment);
      statement.setInt(3, this.HostUID);
      statement.setInt(4, this.RenterUID);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Delete operation
  public void delete() {
    try (PreparedStatement statement = connection
        .prepareStatement("DELETE FROM RenterReview WHERE HostUID = ? AND RenterUID = ?")) {
      statement.setInt(1, this.HostUID);
      statement.setInt(2, this.RenterUID);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to create the "RenterReview" table if it doesn't exist
  public static void createTable(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS RenterReview (" + "HostUID INT NOT NULL,"
          + "RenterUID INT NOT NULL," + "rating INT NOT NULL," + "comment VARCHAR(255),"
          + "PRIMARY KEY (HostUID, RenterUID)," + "FOREIGN KEY (HostUID) REFERENCES User(UID),"
          + "FOREIGN KEY (RenterUID) REFERENCES User(UID))"; // "User" should be replaced with the
                                                             // actual table name of the User table
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to drop the "RenterReview" table if it exists
  public static void dropTable(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String sql = "DROP TABLE IF EXISTS RenterReview";
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Getters and Setters
  public int getHostUID() {
    return HostUID;
  }

  public void setHostUID(int HostUID) {
    this.HostUID = HostUID;
  }

  public int getRenterUID() {
    return RenterUID;
  }

  public void setRenterUID(int RenterUID) {
    this.RenterUID = RenterUID;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
