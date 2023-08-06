package Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import Tables.Listing;
import Tables.ListingReview;
import Tables.Rented;
import Tables.RenterReview;
import Tables.User;

public class Operations {

    // Create User Profile
    public static int createUser(Connection connection, String Name, String Address, String DOB, String Occupation,
            String SIN, String Payment, boolean isRenter, boolean isHost) {
        if (Name == null || Address == null || DOB == null || Occupation == null || SIN == null || Payment == null) {
            throw new IllegalArgumentException("All user information is required.");
        }

        if (!isValidCanadianSIN(SIN)) {
            throw new IllegalArgumentException("Invalid Canadian Social Insurance Number (SIN).");
        }

        if (!isUniqueSIN(connection, SIN)) {
            throw new IllegalArgumentException("The provided SIN already exists in the database.");
        }

        if (!isValidCanadianPayment(Payment)) {
            throw new IllegalArgumentException("Invalid Canadian payment method.");
        }

        return User.insert(connection, Name, Address, DOB, Occupation, SIN, Payment, isRenter, isHost);
    }

    private static boolean isUniqueSIN(Connection connection, String SIN) {
        try {
            String query = "SELECT COUNT(*) FROM user WHERE SIN = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, SIN);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isValidCanadianSIN(String SIN) {
        return SIN.matches("\\d{9}");
    }

    private static boolean isValidCanadianPayment(String Payment) {
        String[] validPaymentMethods = {
                "Visa", "Mastercard", "American Express", "Interac"
        };

        for (String method : validPaymentMethods) {
            if (Payment.equalsIgnoreCase(method)) {
                return true;
            }
        }

        return false;
    }

    // Delete User Profile
    public static void deleteUser(Connection connection, int UID) {
        User user = User.selectById(connection, UID);
        if (user != null) {
            user.delete();
        } else {
            throw new IllegalArgumentException("User not found.");
        }
    }

    // Book Listing
    public static int bookListing(Connection connection, int LID, int UID, double Total, String StartDate,
            String EndDate, boolean isCanceled) {
        Listing listing = Listing.selectById(connection, LID);
        if (listing == null) {
            throw new IllegalArgumentException("Listing not found.");
        }

        if (!listing.isAvailable(connection, StartDate, EndDate)) {
            throw new IllegalStateException("The listing is not available in the desired date range.");
        }

        int RID = Rented.insert(connection, LID, UID, Total, StartDate, EndDate, isCanceled);
        listing.updateAvailability(connection, StartDate, EndDate, false);
        listing.update();

        return RID;
    }

    // Remove Listing
    public static void removeListing(Connection connection, int LID, int UID) {
        Listing listing = Listing.selectById(connection, LID);
        if (listing != null && listing.getHostUID() == UID) {
            if (listing.hasFutureBookings(connection)) {
                throw new IllegalStateException("Cannot remove the listing. It has future bookings.");
            }

            listing.delete();
        } else {
            throw new IllegalArgumentException("Listing not found or you are not authorized to remove it.");
        }
    }

    // Cancel Booking
    public static void cancelBooking(Connection connection, int RID, int UID) {
        Rented rented = Rented.selectById(connection, RID);
        if (rented != null && rented.getUID() == UID) {
            Listing listing = Listing.selectById(connection, rented.getLID());
            if (listing != null) {
                listing.updateAvailability(connection, rented.getStartDate(), rented.getEndDate(), true);
                listing.update();
            }

            rented.delete();
        } else {
            throw new IllegalArgumentException("Booking not found or you are not authorized to cancel it.");
        }
    }

    public static int createListing(Connection connection, String Type, double Longitude, double Latitude,
            String Address, String PostalCode, String City, String Country, int HostUID) {
        if (Type == null || Address == null || PostalCode == null || City == null || Country == null) {
            throw new IllegalArgumentException("All listing information is required.");
        }

        if (!isValidListingType(Type)) {
            throw new IllegalArgumentException("Invalid Listing type.");
        }

        if (!isValidCanadianPostalCode(PostalCode)) {
            throw new IllegalArgumentException("Invalid Canadian postal code.");
        }

        if (!isValidCanadianCity(City)) {
            throw new IllegalArgumentException("Invalid Canadian city.");
        }

        if (!isInCanada(Country)) {
            throw new IllegalArgumentException("Invalid Country.");
        }

        return Listing.insert(connection, Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID);
    }

    private static boolean isValidCanadianPostalCode(String PostalCode) {
        String postalCodePattern = "[ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ] [0-9][ABCEGHJKLMNPRSTVWXYZ][0-9]";
        return Pattern.matches(postalCodePattern, PostalCode.toUpperCase());
    }

    private static boolean isInCanada(String country) {
        return country.equalsIgnoreCase("canada");
    }

    private static boolean isValidListingType(String type) {
        String[] validListingTypes = { "Home", "Apartment", "Studio" };
        for (String validType : validListingTypes) {
            if (validType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidCanadianCity(String City) {
        String[] validCanadianCities = {
                "Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa", "Edmonton", "Quebec City", "Winnipeg",
                "Halifax"
        };

        for (String city : validCanadianCities) {
            if (City.equalsIgnoreCase(city)) {
                return true;
            }
        }

        return false;
    }

    // Update Listing Price
    public static void updateListingPrice(Connection connection, int LID, double newPrice, String startDate,
            String endDate) {
        Listing listing = Listing.selectById(connection, LID);
        if (listing != null) {
            if (!listing.isAvailable(connection, startDate, endDate)) {
                throw new IllegalStateException("The listing is not available in the specific date range.");
            }

            if (listing.isBooked(connection, startDate, endDate)) {
                throw new IllegalStateException(
                        "The listing is booked in the specific date range. Price cannot be updated.");
            }

            listing.setPrice(connection, startDate, endDate, newPrice);
            listing.update();
        } else {
            throw new IllegalArgumentException("Listing not found.");
        }
    }

    // Change Listing Availability
    public static void updateListingAvailability(Connection connection, int LID, String date, boolean isAvailable) {
        Listing listing = Listing.selectById(connection, LID);
        if (listing != null) {
            if (listing.isBooked(connection, date, date)) {
                throw new IllegalStateException(
                        "The listing is booked on the specified date. Availability cannot be changed.");
            }

            listing.updateAvailability(connection, date, date, isAvailable);
            listing.update();
        } else {
            throw new IllegalArgumentException("Listing not found.");
        }
    }

    // Insert Comments for Renter
    public static void insertRenterComment(Connection connection, int HostUID, int RenterUID, int rating,
            String comment) {
        if (!Rented.hasRecentStay(connection, HostUID, RenterUID)) {
            throw new IllegalStateException("The renter has not completed a stay recently. Cannot comment and rate.");
        }

        RenterReview.insert(connection, HostUID, RenterUID, rating, comment);
    }

    // Insert Comments for Host
    public static void insertHostComment(Connection connection, int RenterUID, int LID, int rating,
            String comment) {
        if (!Rented.hasRecentBooking(connection, RenterUID, LID)) {
            throw new IllegalStateException("The host has not rented the listing recently. Cannot comment and rate.");
        }

        ListingReview.insert(connection, RenterUID, LID, rating, comment);
    }

}
