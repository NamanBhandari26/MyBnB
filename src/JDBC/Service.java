package JDBC;
import java.sql.*;

public class Service {
	public MySQL database;
	
	public Service() throws SQLException, ClassNotFoundException {
		try {
			this.database = new MySQL();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void startServices() throws SQLException {
		database.createTables();
		System.out.println("Starting Services...");
	}
	
}
