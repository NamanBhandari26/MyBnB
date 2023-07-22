package Tables;

import java.sql.*;

public class User {
    // Database connection attribute
    private Connection connection;

    // Table fields
    private int UID;
    private String Name;
    private String Address;
    private String DOB;
    private String Occupation;
    private String SIN;
    private String Payment;
    private boolean isRenter;
    private boolean isHost;

    // Constructor with database connection
    public User(Connection connection, String Name, String Address, String DOB, String Occupation, String SIN, String Payment, boolean isRenter, boolean isHost) {
        this.connection = connection;
        this.Name = Name;
        this.Address = Address;
        this.DOB = DOB;
        this.Occupation = Occupation;
        this.SIN = SIN;
        this.Payment = Payment;
        this.isRenter = isRenter;
        this.isHost = isHost;
    }

    // Create (Insert) operation
    public void insert() {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO User (Name, Address, DOB, Occupation, SIN, Payment, isRenter, isHost) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, this.Name);
            statement.setString(2, this.Address);
            statement.setString(3, this.DOB);
            statement.setString(4, this.Occupation);
            statement.setString(5, this.SIN);
            statement.setString(6, this.Payment);
            statement.setBoolean(7, this.isRenter);
            statement.setBoolean(8, this.isHost);
            statement.executeUpdate();

            // Get the generated UID (if applicable)
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.UID = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (Select) operation by UID
    public static User selectById(Connection connection, int UID) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE UID = ?")) {
            statement.setInt(1, UID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String Name = resultSet.getString("Name");
                    String Address = resultSet.getString("Address");
                    String DOB = resultSet.getString("DOB");
                    String Occupation = resultSet.getString("Occupation");
                    String SIN = resultSet.getString("SIN");
                    String Payment = resultSet.getString("Payment");
                    boolean isRenter = resultSet.getBoolean("isRenter");
                    boolean isHost = resultSet.getBoolean("isHost");
                    user = new User(connection, Name, Address, DOB, Occupation, SIN, Payment, isRenter, isHost);
                    user.setUID(UID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Update operation
    public void update() {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE User SET Name = ?, Address = ?, DOB = ?, Occupation = ?, SIN = ?, Payment = ?, isRenter = ?, isHost = ? WHERE UID = ?")) {
            statement.setString(1, this.Name);
            statement.setString(2, this.Address);
            statement.setString(3, this.DOB);
            statement.setString(4, this.Occupation);
            statement.setString(5, this.SIN);
            statement.setString(6, this.Payment);
            statement.setBoolean(7, this.isRenter);
            statement.setBoolean(8, this.isHost);
            statement.setInt(9, this.UID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete operation
    public void delete() {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM User WHERE UID = ?")) {
            statement.setInt(1, this.UID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to create the "User" table if it doesn't exist
    public static void createTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS User (" +
                    "UID INT AUTO_INCREMENT PRIMARY KEY," +
                    "Name VARCHAR(255) NOT NULL," +
                    "Address VARCHAR(255) NOT NULL," +
                    "DOB VARCHAR(10) NOT NULL," +
                    "Occupation VARCHAR(100) NOT NULL," +
                    "SIN VARCHAR(12) NOT NULL," +
                    "Payment VARCHAR(100) NOT NULL," +
                    "isRenter BOOLEAN NOT NULL," +
                    "isHost BOOLEAN NOT NULL)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to drop the "User" table if it exists
    public static void dropTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = "DROP TABLE IF EXISTS User";
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

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String Occupation) {
        this.Occupation = Occupation;
    }

    public String getSIN() {
        return SIN;
    }

    public void setSIN(String SIN) {
        this.SIN = SIN;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String Payment) {
        this.Payment = Payment;
    }

    public boolean isRenter() {
        return isRenter;
    }

    public void setRenter(boolean isRenter) {
        this.isRenter = isRenter;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }
}
