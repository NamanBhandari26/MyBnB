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

  public void addData() throws SQLException {
    User uid1 = new User(connection, "Bob Smith", "1 fake st.", "1998-06-15", "Retail Operations",
        "123456789123", "124312341234", true, false);
    uid1.insert();
    User uid2 = new User(connection, "Jhon Doe", "123 notreal st.", "2002-10-25", "CEO",
        "123456799111", "124312312334", true, false);
    uid2.insert();
    User uid3 = new User(connection, "Ye Haw", "321 fake st.", "2001-09-03", "Sleeps",
        "111111111111", "125123423456", true, false);
    uid3.insert();
    User uid4 = new User(connection, "Jim Job", "14 fake st.", "1997-04-01", "Works",
        "456337282646", "234524554353", true, true);
  uid4.insert();
    User uid5 = new User(connection, "Ligma Ben", "15 fake st.", "1999-08-30", "Tims",
        "197209758924", "none", false, true);
    uid5.insert();

    Amenities aid1 = new Amenities(connection, "Wifi");
    aid1.insert();
    Amenities aid2 = new Amenities(connection, "Washer");
    aid2.insert();
    Amenities aid3 = new Amenities(connection, "Air conditioning");
    aid3.insert();
    Amenities aid4 = new Amenities(connection, "Dedicated workspace");
    aid4.insert();
    Amenities aid5 = new Amenities(connection, "Hair dryer");
    aid5.insert();
    Amenities aid6 = new Amenities(connection, "Iron");
    aid6.insert();
    Amenities aid7 = new Amenities(connection, "TV");
    aid7.insert();
    Amenities aid8 = new Amenities(connection, "Heating");
    aid8.insert();
    Amenities aid9 = new Amenities(connection, "Dryer");
    aid9.insert();
    Amenities aid10 = new Amenities(connection, "Kitchen");
    aid10.insert();
    Amenities aid11 = new Amenities(connection, "Pool");
    aid11.insert();
    Amenities aid12 = new Amenities(connection, "Free parking");
    aid12.insert();
    Amenities aid13 = new Amenities(connection, "Hot tub");
    aid13.insert();
    Amenities aid14 = new Amenities(connection, "EV charger");
    aid14.insert();
    Amenities aid15 = new Amenities(connection, "Crib");
    aid15.insert();
    Amenities aid16 = new Amenities(connection, "Gym");
    aid16.insert();
    Amenities aid17 = new Amenities(connection, "BBQ grill");
    aid17.insert();
    Amenities aid18 = new Amenities(connection, "Breakfast");
    aid18.insert();
    Amenities aid19 = new Amenities(connection, "Indoor fireplace");
    aid19.insert();
    Amenities aid20 = new Amenities(connection, "Smoking allowed");
    aid20.insert();
    Amenities aid21 = new Amenities(connection, "Beachfront");
    aid21.insert();
    Amenities aid22 = new Amenities(connection, "Waterfront");
    aid22.insert();
    Amenities aid23 = new Amenities(connection, "Smoke alarm");
    aid23.insert();
    Amenities aid24 = new Amenities(connection, "Carbon monoxide alarm");
    aid24.insert();


    Listing lid1 = new Listing(connection, "Studio", 124, 124, "1 list rd.", "M4X1G6", "Toronto",
        "Canada", uid4.getUID());
    lid1.insert();
    Listing lid2 = new Listing(connection, "Apartment", 123, 123, "2 list rd.", "M4X1G5", "Toronto",
        "Canada", uid4.getUID());
    lid2.insert();
    Listing lid3 = new Listing(connection, "Home", 122, 122, "3 list rd.", "M4X1G4", "Toronto",
        "Canada", uid5.getUID());
    lid3.insert();

    Rented r1 =new Rented(connection, lid1.getLID(), uid1.getUID(), 100, "2020-01-01", "2020-01-04", false);
    r1.insert();
    Rented r2 =new Rented(connection, lid2.getLID(), uid2.getUID(), 120, "2020-01-03", "2020-01-04", false);
    r2.insert();
    Rented r3 =new Rented(connection, lid1.getLID(), uid3.getUID(), 1000, "2020-02-01", "2020-03-01", true);
    r3.insert();
    Rented r4 =new Rented(connection, lid3.getLID(), uid3.getUID(), 150, "2020-02-01", "2020-02-03", false);
    r4.insert();


    ListingReview.insert(connection, uid1.getUID(), lid1.getLID(), 5, "good");
    ListingReview.insert(connection, uid2.getUID(), lid2.getLID(), 1, "bad");

    RenterReview.insert(connection, uid5.getUID(), uid3.getUID(), 5, "very good guy");
    RenterReview.insert(connection, uid4.getUID(), uid2.getUID(), 1, "very bad guy");

    Has.insert(connection, lid1.getLID(), aid1.getAID());
    Has.insert(connection, lid1.getLID(), aid2.getAID());
    Has.insert(connection, lid1.getLID(), aid4.getAID());
    Has.insert(connection, lid1.getLID(), aid5.getAID());
    Has.insert(connection, lid1.getLID(), aid6.getAID());
    Has.insert(connection, lid1.getLID(), aid11.getAID());
    Has.insert(connection, lid1.getLID(), aid13.getAID());
    Has.insert(connection, lid1.getLID(), aid14.getAID());

    Has.insert(connection, lid2.getLID(), aid21.getAID());
    Has.insert(connection, lid2.getLID(), aid22.getAID());
    Has.insert(connection, lid2.getLID(), aid1.getAID());
    Has.insert(connection, lid2.getLID(), aid2.getAID());
    Has.insert(connection, lid2.getLID(), aid3.getAID());
    Has.insert(connection, lid2.getLID(), aid15.getAID());
    Has.insert(connection, lid2.getLID(), aid13.getAID());
    Has.insert(connection, lid2.getLID(), aid14.getAID());

    Has.insert(connection, lid3.getLID(), aid11.getAID());
    Has.insert(connection, lid3.getLID(), aid20.getAID());
    Has.insert(connection, lid3.getLID(), aid7.getAID());
    Has.insert(connection, lid3.getLID(), aid1.getAID());
    Has.insert(connection, lid3.getLID(), aid6.getAID());
    Has.insert(connection, lid3.getLID(), aid9.getAID());
    Has.insert(connection, lid3.getLID(), aid13.getAID());
    Has.insert(connection, lid3.getLID(), aid14.getAID());

    Availability.insert(connection, lid1.getLID(), "2020-04-07", 55);
    Availability.insert(connection, lid1.getLID(), "2020-04-08", 55);
    Availability.insert(connection, lid1.getLID(), "2020-04-09", 55);
    Availability.insert(connection, lid1.getLID(), "2020-04-10", 55);
    Availability.insert(connection, lid1.getLID(), "2020-04-11", 55);

    Availability.insert(connection, lid2.getLID(), "2020-04-07", 65);
    Availability.insert(connection, lid2.getLID(), "2020-04-08", 65);
    Availability.insert(connection, lid2.getLID(), "2020-04-09", 65);
    Availability.insert(connection, lid2.getLID(), "2020-04-10", 65);
    Availability.insert(connection, lid2.getLID(), "2020-04-11", 65);

    Availability.insert(connection, lid3.getLID(), "2020-04-07", 75);
    Availability.insert(connection, lid3.getLID(), "2020-04-08", 75);
    Availability.insert(connection, lid3.getLID(), "2020-04-09", 75);
    Availability.insert(connection, lid3.getLID(), "2020-04-10", 95);
    Availability.insert(connection, lid3.getLID(), "2020-04-11", 99);



  }
}
