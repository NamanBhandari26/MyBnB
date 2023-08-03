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

    // Function to drop the tables
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
        int uid1 = User.insert(connection, "Bob Smith", "123 Main St", "1998-06-15", "Retail Operations",
                "124312341", "Visa", true, false);
        int uid2 = User.insert(connection, "John Doe", "456 Oak St", "2002-10-25", "CEO",
                "124312342", "Interac", true, false);
        int uid3 = User.insert(connection, "Ye Haw", "789 Elm St", "2001-09-03", "Sleeps",
                "124312343", "Mastercard", true, false);
        int uid4 = User.insert(connection, "Jim Job", "101 Maple St", "1997-04-01", "Works",
                "124312344", "Visa", true, true);
        int uid5 = User.insert(connection, "Ligma Ben", "102 Birch St", "1999-08-30", "Tims",
                "124312345", "Interac", false, true);
        int uid6 = User.insert(connection, "Simga Den", "103 Cedar St", "1999-08-30", "Prof",
                "124312346", "Mastercard", false, true);

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

        int lid1 = Listing.insert(connection, "Studio", 43.651070, -79.347015, "123 Queen St W", "M4X 1G6", "Toronto",
                "Canada", uid4);
        int lid2 = Listing.insert(connection, "Apartment", 43.651070, -79.347015, "456 Yonge St", "M4X 1G5", "Toronto",
                "Canada", uid4);
        int lid3 = Listing.insert(connection, "Home", 43.651070, -79.347015, "789 Bay St", "M4X 1G4", "Toronto",
                "Canada",
                uid5);

        Listing.insert(connection, "Apartment", 49.282729, -123.120738, "1010 West Georgia St", "A1B 2C3", "Vancouver",
                "Canada", uid5);
        Listing.insert(connection, "Apartment", 45.501690, -73.567253, "200 René-Lévesque Blvd W", "D4E 5F6",
                "Montreal",
                "Canada", uid6);
        Listing.insert(connection, "Apartment", 51.048615, -114.070846, "300 4 Ave SW", "G7H 8I9", "Vancouver", "Canada",
                uid6);
        Listing.insert(connection, "Apartment", 45.421530, -75.697193, "400 Rideau St", "J1K 2L3", "Vancouver", "Canada",
                uid6);
        Listing.insert(connection, "Apartment", 53.544389, -113.490927, "500 Jasper Ave", "M2N 3O4", "Vancouver",
                "Canada",
                uid6);
        Listing.insert(connection, "Apartment", 46.813878, -71.207981, "600 Grande Allée E", "P5Q 6R7", "Vancouver",
                "Canada", uid6);
        Listing.insert(connection, "Apartment", 49.895077, -97.138451, "700 Portage Ave", "S8T 9U0", "Vancouver",
                "Canada",
                uid6);
        Listing.insert(connection, "Apartment", 44.648618, -63.585948, "800 Barrington St", "V1W 2X3", "Vancouver",
                "Canada",
                uid6);
        Listing.insert(connection, "Apartment", 43.651070, -79.347015, "900 Dundas St W", "Y4Z 5A6", "Vancouver",
                "Canada",
                uid6);
        Listing.insert(connection, "Apartment", 43.651070, -79.347015, "100 King St W", "B1C 2D3", "Vancouver", "Canada",
                uid6);
        Listing.insert(connection, "Apartment", 43.651070, -79.347015, "1100 Queen St E", "E4F 5G6", "Vancouver",
                "Canada",
                uid6);
        Listing.insert(connection, "Apartment", 43.651070, -79.347015, "1200 Bloor St W", "H7I 8J9", "Vancouver",
                "Canada",
                uid6);

        Rented.insert(connection, lid1, uid1, 100, "2020-01-01", "2020-01-04", false);
        Rented.insert(connection, lid2, uid2, 120, "2020-01-03", "2020-01-04", false);
        Rented.insert(connection, lid1, uid3, 1000, "2020-02-01", "2020-03-01", true);
        Rented.insert(connection, lid3, uid3, 150, "2020-02-01", "2020-02-03", false);

        ListingReview.insert(connection, uid1, lid1, 5,
                "Great space to spend a peaceful weekend but close enough to town to not feel isolated.");
        ListingReview.insert(connection, uid3, lid1, 5,
                "Great little stay in a cozy cabin set on a peninsula overlooking both sides of the lake. Would highly recommend to another couple or 2 person getaway!");
        ListingReview.insert(connection, uid2, lid2, 1,
                "Not as remote as made out to be. On the grid. Plenty of neighbours and noise. Cannot easily swim on lake due to lillypads/weeds. Outdoor kitchen is a fire pit.");

        RenterReview.insert(connection, uid5, uid3, 5,
                "Ligma was always very quick to respond and help with any questions we might have. Thank you!");
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
