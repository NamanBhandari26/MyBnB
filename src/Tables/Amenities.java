package Tables;

import java.sql.*;

public class Amenities {
    // Database connection attribute
    private Connection connection;

    // Table fields
    private int AID;
    private String Type;

    // Constructor with database connection
    public Amenities(Connection connection, String Type) {
        this.connection = connection;
        this.Type = Type;
    }

    // Create (Insert) operation
    public void insert() {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Amenities (Type) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, this.Type);
            statement.executeUpdate();

            // Get the generated AID (if applicable)
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.AID = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (Select) operation by AID
    public static Amenities selectById(Connection connection, int AID) {
        Amenities amenities = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Amenities WHERE AID = ?")) {
            statement.setInt(1, AID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String Type = resultSet.getString("Type");
                    amenities = new Amenities(connection, Type);
                    amenities.setAID(AID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amenities;
    }

    // Update operation
    public void update() {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE Amenities SET Type = ? WHERE AID = ?")) {
            statement.setString(1, this.Type);
            statement.setInt(2, this.AID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete operation
    public void delete() {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Amenities WHERE AID = ?")) {
            statement.setInt(1, this.AID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to create the "Amenities" table if it doesn't exist
    public static void createTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Amenities (" +
                    "AID INT AUTO_INCREMENT PRIMARY KEY," +
                    "Type VARCHAR(255) NOT NULL)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to drop the "Amenities" table if it exists
    public static void dropTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = "DROP TABLE IF EXISTS Amenities";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
}
