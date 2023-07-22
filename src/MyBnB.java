import java.sql.*;

import JDBC.Service;

public class MyBnB {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("Hello, welcome to MyBnB");
        Service service = new Service();
        service.startServices();
	}

}
