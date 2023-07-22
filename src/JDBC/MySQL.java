package JDBC;

import java.sql.*;

import Tables.Amenities;
import Tables.Availability;
import Tables.Has;
import Tables.Listing;
import Tables.ListingReview;
import Tables.Rented;
import Tables.RenterReview;
import Tables.User;

public class MySQL implements SQL {

	private Connection connection;
	
	private final String db_url = "jdbc:mysql://localhost:3306/MyBnB";
	private final String username = "root";
	private final String password = "1234";

	public MySQL() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		this.connection = DriverManager.getConnection(this.db_url, this.username, this.password);
	}

	public void createTables() throws SQLException {
		User.createTable(connection);
		Amenities.createTable(connection);
		Listing.createTable(connection);
		Rented.createTable(connection);
		ListingReview.createTable(connection);
		RenterReview.createTable(connection);
		Has.createTable(connection);
		Availability.createTable(connection);
	}
	
	public void dropTables() throws SQLException {
		User.dropTable(connection);
		Amenities.dropTable(connection);
		Listing.dropTable(connection);
		Rented.dropTable(connection);
		ListingReview.dropTable(connection);
		RenterReview.dropTable(connection);
		Has.dropTable(connection);
		Availability.dropTable(connection);
	}
}
