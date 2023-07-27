package JDBC;

import java.sql.*;

public interface SQL {
	public void createTables() throws SQLException;

	public void dropTables() throws SQLException;

	public void addData() throws SQLException;

	public Connection getConnection();
}
