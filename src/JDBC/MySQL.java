package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Queries.HostToolKit;
import Queries.Report;
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

	//Function to drop the tables
	public void dropTables() {
	   try (Statement statement = connection.createStatement()) {
	       statement.executeUpdate("DROP TABLE IF EXISTS Availability;");
	       statement.executeUpdate("DROP TABLE IF EXISTS Has;");
	       statement.executeUpdate("DROP TABLE IF EXISTS RenterReview;");
	       statement.executeUpdate("DROP TABLE IF EXISTS ListingReview;");
	       statement.executeUpdate("DROP TABLE IF EXISTS Rented;");
	       statement.executeUpdate("DROP TABLE IF EXISTS Amenities;");
	       statement.executeUpdate("DROP TABLE IF EXISTS Listing;");
	       statement.executeUpdate("DROP TABLE IF EXISTS User;");
	   } catch (SQLException e) {
	       e.printStackTrace();
	   }
	}


  // Function to drop the database
  public void dropDatabase() {
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate("DROP DATABASE IF EXISTS MyBnB;");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  

  public void addData() throws SQLException {
    int uid1 = User.insert(connection, "Bob Smith", "1 fake st.", "1998-06-15", "Retail Operations",
        "123456789123", "124312341234", true, false);
    int uid2 = User.insert(connection, "John Doe", "123 notreal st.", "2002-10-25", "CEO",
        "123456799111", "124312312334", true, false);
    int uid3 = User.insert(connection, "Ye Haw", "321 fake st.", "2001-09-03", "Sleeps",
        "111111111111", "125123423456", true, false);
    int uid4 = User.insert(connection, "Jim Job", "14 fake st.", "1997-04-01", "Works",
        "456337282646", "234524554353", true, true);
    int uid5 = User.insert(connection, "Ligma Ben", "15 fake st.", "1999-08-30", "Tims",
        "197209758924", "none", false, true);
    int uid6 = User.insert(connection, "Simga Den", "13 fake st.", "1999-08-30", "Prof",
        "197229758924", "none", false, true);

    Amenities.insert(connection, "Wifi");
    Amenities.insert(connection, "Washer");
    Amenities.insert(connection, "Air conditioning");
    Amenities.insert(connection, "Dedicated workspace");
    Amenities.insert(connection, "Hair dryer");
    Amenities.insert(connection, "Iron");
    Amenities.insert(connection, "TV");
    Amenities.insert(connection, "Heating");
    Amenities.insert(connection, "Dryer");
    Amenities.insert(connection, "Kitchen");
    Amenities.insert(connection, "Pool");
    Amenities.insert(connection, "Free parking");
    Amenities.insert(connection, "Hot tub");
    Amenities.insert(connection, "EV charger");
    Amenities.insert(connection, "Crib");
    Amenities.insert(connection, "Gym");
    Amenities.insert(connection, "BBQ grill");
    Amenities.insert(connection, "Breakfast");
    Amenities.insert(connection, "Indoor fireplace");
    Amenities.insert(connection, "Smoking allowed");
    Amenities.insert(connection, "Beachfront");
    Amenities.insert(connection, "Waterfront");
    Amenities.insert(connection, "Smoke alarm");
    Amenities.insert(connection, "Carbon monoxide alarm");

    int lid1 = Listing.insert(connection, "Studio", 124, 124, "1 list rd.", "M4X1G6", "Toronto",
        "Canada", uid4);
    int lid2 = Listing.insert(connection, "Apartment", 123, 123, "2 list rd.", "M4X1G5", "Toronto",
        "Canada", uid4);
    int lid3 = Listing.insert(connection, "Home", 122, 122, "3 list rd.", "M4X1G4", "Toronto",
        "Canada", uid5);

    Listing.insert(connection, "Apartment", 129, 129, "7 list rd.", "M1X1G5", "New York",
        "USA", uid5);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 1", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 2", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 3", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 4", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 5", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 6", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 7", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 8", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 9", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 10", "M1X1G4", "New York",
        "USA", uid6);
    Listing.insert(connection, "Apartment", 124, 124, "3 list rd. apt 11", "M1X1G4", "New York",
        "USA", uid6);

    Rented.insert(connection, lid1, uid1, 100, "2020-01-01", "2020-01-04", false);
    Rented.insert(connection, lid2, uid2, 120, "2020-01-03", "2020-01-04", false);
    Rented.insert(connection, lid1, uid3, 1000, "2020-02-01", "2020-03-01", true);
    Rented.insert(connection, lid3, uid3, 150, "2020-02-01", "2020-02-03", false);

    ListingReview.insert(connection, uid1, lid1, 5, "Great space to spend a peaceful weekend but close enough to town to not feel isolated.");
    ListingReview.insert(connection, uid3, lid1, 5, "Great little stay in a cozy cabin set on a peninsula overlooking both sides of the lake. Would highly recommend to another couple or 2 person getaway!");
    ListingReview.insert(connection, uid2, lid2, 1, "Not as remote as made out to be. On the grid. Plenty of neighbours and noise. Cannot easily swim on lake due to lillypads/weeds. Outdoor kitchen is a fire pit.");

    RenterReview.insert(connection, uid5, uid3, 5, "Ligma was always very quick to respond and help with any questions we might have. Thank you!");
    RenterReview.insert(connection, uid4, uid2, 1, "Bad stay. We will not be back!");

    Has.insert(connection, lid1, 1);
    Has.insert(connection, lid1, 2);
    Has.insert(connection, lid1, 4);
    Has.insert(connection, lid1, 5);
    Has.insert(connection, lid1, 6);
    Has.insert(connection, lid1, 11);
    Has.insert(connection, lid1, 13);
    Has.insert(connection, lid1, 14);

    Has.insert(connection, lid2, 21);
    Has.insert(connection, lid2, 22);
    Has.insert(connection, lid2, 1);
    Has.insert(connection, lid2, 2);
    Has.insert(connection, lid2, 3);
    Has.insert(connection, lid2, 15);
    Has.insert(connection, lid2, 13);
    Has.insert(connection, lid2, 14);

    Has.insert(connection, lid3, 19);
    Has.insert(connection, lid3, 18);
    Has.insert(connection, lid3, 17);
    Has.insert(connection, lid3, 24);
    Has.insert(connection, lid3, 1);
    Has.insert(connection, lid3, 2);
    Has.insert(connection, lid3, 4);
    Has.insert(connection, lid3, 5);
    Has.insert(connection, lid3, 6);
    Has.insert(connection, lid3, 11);
    Has.insert(connection, lid3, 13);
    Has.insert(connection, lid3, 14);

    Availability.insert(connection, lid1, "2020-04-07", 55, true);
    Availability.insert(connection, lid1, "2020-04-08", 55, true);
    Availability.insert(connection, lid1, "2020-04-09", 55, true);
    Availability.insert(connection, lid1, "2020-04-10", 55, true);
    Availability.insert(connection, lid1, "2020-04-11", 55, true);

    Availability.insert(connection, lid2, "2020-04-07", 65, true);
    Availability.insert(connection, lid2, "2020-04-08", 65, true);
    Availability.insert(connection, lid2, "2020-04-09", 65, true);
    Availability.insert(connection, lid2, "2020-04-10", 65, true);
    Availability.insert(connection, lid2, "2020-04-11", 65, true);

    Availability.insert(connection, lid3, "2020-04-07", 75, true);
    Availability.insert(connection, lid3, "2020-04-08", 75, true);
    Availability.insert(connection, lid3, "2020-04-09", 75, true);
    Availability.insert(connection, lid3, "2020-04-10", 95, true);
    Availability.insert(connection, lid3, "2020-04-11", 99, true);
  }

  public Connection getConnection() {
    return this.connection;
  }
  
  public void test() throws SQLException {
    Scanner scanInt;
    scanInt = new Scanner(System.in);
    Report.totalBookingByDateAndCity(connection, scanInt);
    Report.totalBookingByDateAndPostal(connection, scanInt);
    Report.totalListingByCountry(connection, scanInt);
    Report.totalListingByCountryAndCity(connection, scanInt);
    Report.totalListingByPostal(connection, scanInt);
    Report.rankHostsPerCountry(connection, scanInt);
    Report.rankHostsPerCountryAndCity(connection, scanInt);
    Report.commercialHosts(connection, scanInt);
    Report.rankRenterBetweenDate(connection, scanInt);
    Report.rankRenterBetweenDateAndCity(connection, scanInt);
    Report.mostHostCancels(connection);
    Report.mostRenterCancels(connection);
    Report.getNounPrasesForlisting(connection);
    HostToolKit.suggestedPrice(connection, scanInt);
    HostToolKit.suggestedAmenities(connection, scanInt);
    scanInt.close();
  }

}
