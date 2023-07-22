package Tables;

import java.sql.*;

public class ListingReview {
    // Database connection attribute
    private Connection connection;

    // Table fields
    private int UID;
    private int LID;
    private int rating;
    private String comment;

    // Constructor with database connection
    public ListingReview(Connection connection, int UID, int LID, int rating, String comment) {
        this.connection = connection;
        this.UID = UID;
        this.LID = LID;
        this.rating = rating;
        this.comment = comment;
    }

    // Create (Insert) operation
    public void insert() {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO ListingReview (UID, LID, rating, comment) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, this.UID);
            statement.setInt(2, this.LID);
            statement.setInt(3, this.rating);
            statement.setString(4, this.comment);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (Select) operation by UID and LID
    public static ListingReview selectById(Connection connection, int UID, int LID) {
        ListingReview review = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ListingReview WHERE UID = ? AND LID = ?")) {
            statement.setInt(1, UID);
            statement.setInt(2, LID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int rating = resultSet.getInt("rating");
                    String comment = resultSet.getString("comment");
                    review = new ListingReview(connection, UID, LID, rating, comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return review;
    }

    // Update operation
    public void update() {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE ListingReview SET rating = ?, comment = ? WHERE UID = ? AND LID = ?")) {
            statement.setInt(1, this.rating);
            statement.setString(2, this.comment);
            statement.setInt(3, this.UID);
            statement.setInt(4, this.LID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete operation
    public void delete() {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ListingReview WHERE UID = ? AND LID = ?")) {
            statement.setInt(1, this.UID);
            statement.setInt(2, this.LID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to create the "ListingReview" table if it doesn't exist
    public static void createTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS ListingReview (" +
                    "UID INT NOT NULL," +
                    "LID INT NOT NULL," +
                    "rating INT NOT NULL," +
                    "comment VARCHAR(255)," +
                    "PRIMARY KEY (UID, LID)," +
                    "FOREIGN KEY (UID) REFERENCES User(UID)," +
                    "FOREIGN KEY (LID) REFERENCES Listing(LID))"; // "User" and "Listing" should be replaced with the actual table names of the User and Listing tables, respectively
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to drop the "ListingReview" table if it exists
    public static void dropTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = "DROP TABLE IF EXISTS ListingReview";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getLID() {
        return LID;
    }

    public void setLID(int LID) {
        this.LID = LID;
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
