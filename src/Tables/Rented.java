package Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Rented {
  // Database connection attribute
  private Connection connection;

  // Table fields
  private int RID;
  private int LID;
  private int UID;
  private double Total;
  private String StartDate;
  private String EndDate;
  private boolean isCanceled;

  // Constructor with database connection
  public Rented(Connection connection, int LID, int UID, double Total, String StartDate,
      String EndDate, boolean isCanceled) {
    this.connection = connection;
    this.LID = LID;
    this.UID = UID;
    this.Total = Total;
    this.StartDate = StartDate;
    this.EndDate = EndDate;
    this.isCanceled = isCanceled;
  }

  // Static method to create (Insert) operation
  public static int insert(Connection connection, int LID, int UID, double Total, String StartDate,
      String EndDate, boolean isCanceled) {

    boolean isRenter = false;

    try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE UID = ?")) {
      statement.setInt(1, UID);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          isRenter = resultSet.getBoolean("isRenter");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (!isRenter) {
      return -1;
    }

    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO Rented (LID, UID, Total, StartDate, EndDate, isCanceled) VALUES (?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS)) {
      statement.setInt(1, LID);
      statement.setInt(2, UID);
      statement.setDouble(3, Total);
      statement.setString(4, StartDate);
      statement.setString(5, EndDate);
      statement.setBoolean(6, isCanceled);
      statement.executeUpdate();

      // Get the generated RID (if applicable)
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

  // Read (Select) operation by RID
  public static Rented selectById(Connection connection, int RID) {
    Rented rented = null;
    try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Rented WHERE RID = ?")) {
      statement.setInt(1, RID);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          int LID = resultSet.getInt("LID");
          int UID = resultSet.getInt("UID");
          double Total = resultSet.getDouble("Total");
          String StartDate = resultSet.getString("StartDate");
          String EndDate = resultSet.getString("EndDate");
          boolean isCanceled = resultSet.getBoolean("isCanceled");
          rented = new Rented(connection, LID, UID, Total, StartDate, EndDate, isCanceled);
          rented.setRID(RID);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return rented;
  }

  // Update operation
  public void update() {
    try (PreparedStatement statement = connection.prepareStatement(
        "UPDATE Rented SET LID = ?, UID = ?, Total = ?, StartDate = ?, EndDate = ?, isCanceled = ? WHERE RID = ?")) {
      statement.setInt(1, this.LID);
      statement.setInt(2, this.UID);
      statement.setDouble(3, this.Total);
      statement.setString(4, this.StartDate);
      statement.setString(5, this.EndDate);
      statement.setBoolean(6, this.isCanceled);
      statement.setInt(7, this.RID);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Delete operation
  public void delete() {
    try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Rented WHERE RID = ?")) {
      statement.setInt(1, this.RID);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to create the "Rented" table if it doesn't exist (static approach)
  public static void createTable(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String sql = "CREATE TABLE IF NOT EXISTS Rented (" +
          "RID INT AUTO_INCREMENT PRIMARY KEY," +
          "LID INT NOT NULL," +
          "UID INT NOT NULL," +
          "Total DOUBLE NOT NULL," +
          "StartDate DATE NOT NULL," +
          "EndDate DATE NOT NULL," +
          "isCanceled BOOLEAN NOT NULL," +
          "FOREIGN KEY (LID) REFERENCES Listing(LID)," +
          "FOREIGN KEY (UID) REFERENCES User(UID))"; // "Listing" and "User" should be replaced
      // with the actual table names of the Listing and User tables, respectively
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Function to drop the "Rented" table if it exists (static approach)
  public static void dropTable(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String sql = "DROP TABLE IF EXISTS Rented";
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Getters and Setters
  public int getRID() {
    return RID;
  }

  public void setRID(int RID) {
    this.RID = RID;
  }

  public int getLID() {
    return LID;
  }

  public void setLID(int LID) {
    this.LID = LID;
  }

  public int getUID() {
    return UID;
  }

  public void setUID(int UID) {
    this.UID = UID;
  }

  public double getTotal() {
    return Total;
  }

  public void setTotal(double Total) {
    this.Total = Total;
  }

  public String getStartDate() {
    return StartDate;
  }

  public void setStartDate(String StartDate) {
    this.StartDate = StartDate;
  }

  public String getEndDate() {
    return EndDate;
  }

  public void setEndDate(String EndDate) {
    this.EndDate = EndDate;
  }

  public boolean isCanceled() {
    return isCanceled;
  }

  public void setCanceled(boolean isCanceled) {
    this.isCanceled = isCanceled;
  }
}
