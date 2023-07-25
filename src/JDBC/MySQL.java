package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

  @SuppressWarnings("unused")
  public void addData() throws SQLException {
    int uid1 = User.insert(connection, "Bob Smith", "1 fake st.", "1998-06-15", "Retail Operations",
        "123456789123", "124312341234", true, false);
    int uid2 = User.insert(connection, "Jhon Doe", "123 notreal st.", "2002-10-25", "CEO",
        "123456799111", "124312312334", true, false);
    int uid3 = User.insert(connection, "Ye Haw", "321 fake st.", "2001-09-03", "Sleeps",
        "111111111111", "125123423456", true, false);
    int uid4 = User.insert(connection, "Jim Job", "14 fake st.", "1997-04-01", "Works",
        "456337282646", "234524554353", true, true);
    int uid5 = User.insert(connection, "Ligma Ben", "15 fake st.", "1999-08-30", "Tims",
        "197209758924", "none", false, true);

    int aid1 = Amenities.insert(connection, "Wifi");
    int aid2 = Amenities.insert(connection, "Washer");
    int aid3 = Amenities.insert(connection, "Air conditioning");
    int aid4 = Amenities.insert(connection, "Dedicated workspace");
    int aid5 = Amenities.insert(connection, "Hair dryer");
    int aid6 = Amenities.insert(connection, "Iron");
    int aid7 = Amenities.insert(connection, "TV");
    int aid8 = Amenities.insert(connection, "Heating");
    int aid9 = Amenities.insert(connection, "Dryer");
    int aid10 = Amenities.insert(connection, "Kitchen");
    int aid11 = Amenities.insert(connection, "Pool");
    int aid12 = Amenities.insert(connection, "Free parking");
    int aid13 = Amenities.insert(connection, "Hot tub");
    int aid14 = Amenities.insert(connection, "EV charger");
    int aid15 = Amenities.insert(connection, "Crib");
    int aid16 = Amenities.insert(connection, "Gym");
    int aid17 = Amenities.insert(connection, "BBQ grill");
    int aid18 = Amenities.insert(connection, "Breakfast");
    int aid19 = Amenities.insert(connection, "Indoor fireplace");
    int aid20 = Amenities.insert(connection, "Smoking allowed");
    int aid21 = Amenities.insert(connection, "Beachfront");
    int aid22 = Amenities.insert(connection, "Waterfront");
    int aid23 = Amenities.insert(connection, "Smoke alarm");
    int aid24 = Amenities.insert(connection, "Carbon monoxide alarm");


    int lid1 = Listing.insert(connection, "Studio", 124, 124, "1 list rd.", "M4X1G6", "Toronto",
        "Canada", uid4);
    int lid2 = Listing.insert(connection, "Apartment", 123, 123, "2 list rd.", "M4X1G5", "Toronto",
        "Canada", uid4);
    int lid3 = Listing.insert(connection, "Home", 122, 122, "3 list rd.", "M4X1G4", "Toronto",
        "Canada", uid5);

    Rented.insert(connection, lid1, uid1, 100, "2020-01-01", "2020-01-04", false);
    Rented.insert(connection, lid2, uid2, 120, "2020-01-03", "2020-01-04", false);
    Rented.insert(connection, lid1, uid3, 1000, "2020-02-01", "2020-03-01", true);
    Rented.insert(connection, lid3, uid3, 150, "2020-02-01", "2020-02-03", false);

    ListingReview.insert(connection, uid1, lid1, 5, "good");
    ListingReview.insert(connection, uid2, lid2, 1, "bad");

    RenterReview.insert(connection, uid5, uid3, 5, "very good guy");
    RenterReview.insert(connection, uid4, uid2, 1, "very bad guy");

    Has.insert(connection, lid1, aid1);
    Has.insert(connection, lid1, aid2);
    Has.insert(connection, lid1, aid4);
    Has.insert(connection, lid1, aid5);
    Has.insert(connection, lid1, aid6);
    Has.insert(connection, lid1, aid11);
    Has.insert(connection, lid1, aid13);
    Has.insert(connection, lid1, aid14);

    Has.insert(connection, lid2, aid21);
    Has.insert(connection, lid2, aid22);
    Has.insert(connection, lid2, aid1);
    Has.insert(connection, lid2, aid2);
    Has.insert(connection, lid2, aid3);
    Has.insert(connection, lid2, aid15);
    Has.insert(connection, lid2, aid13);
    Has.insert(connection, lid2, aid14);

    Has.insert(connection, lid3, aid11);
    Has.insert(connection, lid3, aid20);
    Has.insert(connection, lid3, aid7);
    Has.insert(connection, lid3, aid1);
    Has.insert(connection, lid3, aid6);
    Has.insert(connection, lid3, aid9);
    Has.insert(connection, lid3, aid13);
    Has.insert(connection, lid3, aid14);

    Availability.insert(connection, lid1, "2020-04-07", 55);
    Availability.insert(connection, lid1, "2020-04-08", 55);
    Availability.insert(connection, lid1, "2020-04-09", 55);
    Availability.insert(connection, lid1, "2020-04-10", 55);
    Availability.insert(connection, lid1, "2020-04-11", 55);

    Availability.insert(connection, lid2, "2020-04-07", 65);
    Availability.insert(connection, lid2, "2020-04-08", 65);
    Availability.insert(connection, lid2, "2020-04-09", 65);
    Availability.insert(connection, lid2, "2020-04-10", 65);
    Availability.insert(connection, lid2, "2020-04-11", 65);

    Availability.insert(connection, lid3, "2020-04-07", 75);
    Availability.insert(connection, lid3, "2020-04-08", 75);
    Availability.insert(connection, lid3, "2020-04-09", 75);
    Availability.insert(connection, lid3, "2020-04-10", 95);
    Availability.insert(connection, lid3, "2020-04-11", 99);



  }
}
