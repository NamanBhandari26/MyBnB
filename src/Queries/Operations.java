package Queries;

import java.sql.Connection;

import Tables.Listing;
import Tables.ListingReview;
import Tables.Rented;
import Tables.RenterReview;
import Tables.User;

public class Operations {

  // Create User Profile
  public static int createUser(Connection connection, String Name, String Address, String DOB,
      String Occupation, String SIN, String Payment, boolean isRenter, boolean isHost) {
    return User.insert(connection, Name, Address, DOB, Occupation, SIN, Payment, isRenter, isHost);
  }

  // Delete User Profile
  public static void deleteUser(Connection connection, int UID) {
    User user = User.selectById(connection, UID);
    if (user != null) {
      user.delete();
    }
  }

  // Book Listing
  public static int bookListing(Connection connection, int LID, int UID, double Total,
      String StartDate, String EndDate, boolean isCanceled) {
    return Rented.insert(connection, LID, UID, Total, StartDate, EndDate, isCanceled);
  }

  // Create Listing
  public static int createListing(Connection connection, String Type, double Longitude, double Latitude,
      String Address, String PostalCode, String City, String Country, int HostUID) {
    return Listing.insert(connection, Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID);
  }

  // Cancel Booking
  public static void cancelBooking(Connection connection, int RID) {
    Rented rented = Rented.selectById(connection, RID);
    if (rented != null) {
      rented.delete();
    }
  }

  // Remove Listing
  public static void removeListing(Connection connection, int LID) {
    Listing listing = Listing.selectById(connection, LID);
    if (listing != null) {
      listing.delete();
    }
  }

  // Update Listing Price
  public static void updateListingPrice(Connection connection, int LID, double newPrice, String startDate,
      String endDate) {
    Listing listing = Listing.selectById(connection, LID);
    if (listing != null) {
      if (listing.isAvailable(connection, startDate, endDate)) {
        listing.setPrice(connection, startDate, endDate, newPrice);
        listing.update();
      } else {
        System.out.println("Cannot update price. The listing is currently booked.");
      }
    } else {
      System.out.println("Listing not found.");
    }
  }

  // Update Listing Availability
  public static void updateListingAvailability(Connection connection, int LID, String startDate,
      String endDate, boolean isAvailable) {
    Listing listing = Listing.selectById(connection, LID);
    if (listing != null) {
      listing.updateAvailability(connection, startDate, endDate, isAvailable);
      listing.update();
    } else {
      System.out.println("Listing not found.");
    }
  }

  // Insert Comments for Renter
  public static void insertRenterComment(Connection connection, int HostUID, int RenterUID,
      int rating, String comment) {
    RenterReview.insert(connection, HostUID, RenterUID, rating, comment);
  }

  // Insert Comments for Host
  public static void insertHostComment(Connection connection, int HostUID, int RenterUID,
      int rating, String comment) {
    ListingReview.insert(connection, HostUID, RenterUID, rating, comment);
  }

}
