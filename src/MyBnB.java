import java.sql.*;

public class MyBnB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Test","root", "1234");
			Statement stmt = con.createStatement();
			System.out.println("inserting records");
			String sql="Insert into test1 values(43)";
			stmt.executeUpdate(sql);
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
	}

}
