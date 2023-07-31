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
        // Validate required user information
        if (Name == null || Address == null || DOB == null || Occupation == null || SIN == null || Payment == null) {
            throw new IllegalArgumentException("All user information is required.");
        }

        // Validate Canadian SIN
        if (!isValidCanadianSIN(SIN)) {
            throw new IllegalArgumentException("Invalid Canadian Social Insurance Number (SIN).");
        }

        // Check if SIN is unique
        if (!isUniqueSIN(connection, SIN)) {
            throw new IllegalArgumentException("The provided SIN already exists in the database.");
        }

        // Validate Canadian Address
        if (!isValidCanadianAddress(Address)) {
            throw new IllegalArgumentException("Invalid Canadian address.");
        }

        // Validate Canadian Payment
        if (!isValidCanadianPayment(Payment)) {
            throw new IllegalArgumentException("Invalid Canadian payment method.");
        }

        return User.insert(connection, Name, Address, DOB, Occupation, SIN, Payment, isRenter, isHost);
    }
    
    // Function to check if SIN is unique in the database
    private static boolean isUniqueSIN(Connection connection, String SIN) {
        try {
            // Assuming your database table for users is named 'users', and the column for SIN is named 'sin_number'
            String query = "SELECT COUNT(*) FROM user WHERE SIN = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, SIN);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0; // If count is 0, SIN is unique
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false in case of any database error
    }

    // Function to validate Canadian SIN
    private static boolean isValidCanadianSIN(String SIN) {
        // SIN must be exactly 9 digits
        return SIN.matches("\\d{9}");
    }

    // Function to validate Canadian Address
    private static boolean isValidCanadianAddress(String Address) {
        // Basic check for Canadian provinces/territories
        String[] canadianProvincesAndTerritories = {
            "AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON", "PE", "QC", "SK", "YT"
        };
        
        for (String province : canadianProvincesAndTerritories) {
            if (Address.toUpperCase().contains(province)) {
                return true;
            }
        }
        
        return false;
    }

    // Function to validate Canadian Payment (Assuming common payment methods in Canada)
    private static boolean isValidCanadianPayment(String Payment) {
        // Basic check for common payment methods in Canada
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
		// Check if the user exists before deleting
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

		// Ensure that the listing is available for rent in the desired date range
		if (!listing.isAvailable(connection, StartDate, EndDate)) {
			throw new IllegalStateException("The listing is not available in the desired date range.");
		}

		// Book the listing and update its availability in the calendar
		int RID = Rented.insert(connection, LID, UID, Total, StartDate, EndDate, isCanceled);
		listing.updateAvailability(connection, StartDate, EndDate, false);
		listing.update();

		return RID;
	}

	// Remove Listing
	public static void removeListing(Connection connection, int LID, int UID) {
		Listing listing = Listing.selectById(connection, LID);
		if (listing != null && listing.getHostUID() == UID) {
			// Check if the listing has any bookings in the future
			if (listing.hasFutureBookings(connection)) {
				throw new IllegalStateException("Cannot remove the listing. It has future bookings.");
			}

			// Delete the listing and update its availability in the calendar
			listing.delete();
		} else {
			throw new IllegalArgumentException("Listing not found or you are not authorized to remove it.");
		}
	}

	// Cancel Booking
	public static void cancelBooking(Connection connection, int RID, int UID) {
		Rented rented = Rented.selectById(connection, RID);
		if (rented != null && rented.getUID() == UID) {
			// Update the listing availability in the calendar
			Listing listing = Listing.selectById(connection, rented.getLID());
			if (listing != null) {
				listing.updateAvailability(connection, rented.getStartDate(), rented.getEndDate(), true);
				listing.update();
			}

			// Cancel the booking
			rented.delete();
		} else {
			throw new IllegalArgumentException("Booking not found or you are not authorized to cancel it.");
		}
	}

    public static int createListing(Connection connection, String Type, double Longitude, double Latitude,
            String Address, String PostalCode, String City, String Country, int HostUID) {
        // Validate required listing information
        if (Type == null || Address == null || PostalCode == null || City == null || Country == null) {
            throw new IllegalArgumentException("All listing information is required.");
        }
        
        if(!isValidListingType(Type)) {
            throw new IllegalArgumentException("Invalid Listing type.");
        }

        // Validate Canadian Address
        if (!isValidCanadianAddress(Address)) {
            throw new IllegalArgumentException("Invalid Canadian address.");
        }

        // Validate Canadian Postal Code
        if (!isValidCanadianPostalCode(PostalCode)) {
            throw new IllegalArgumentException("Invalid Canadian postal code.");
        }

        // Validate Canadian City
        if (!isValidCanadianCity(City)) {
            throw new IllegalArgumentException("Invalid Canadian city.");
        }
        
        // Validate Canadian City
        if (!isInCanada(Country)) {
            throw new IllegalArgumentException("Invalid Country.");
        }

        return Listing.insert(connection, Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID);
    }

    // Function to validate Canadian Postal Code
    private static boolean isValidCanadianPostalCode(String PostalCode) {
        // Canadian postal codes follow this format: "A1A 1A1"
        // The first character must be a letter (excluding D, F, I, O, Q, or U)
        // The second character must be a digit
        // The third character must be a letter
        // The fourth character must be a space
        // The fifth character must be a digit
        // The sixth character must be a letter
        // The postal code must have a total length of 7 characters
        String postalCodePattern = "[ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ] [0-9][ABCEGHJKLMNPRSTVWXYZ][0-9]";
        return Pattern.matches(postalCodePattern, PostalCode.toUpperCase());
    }
    
    // Function to validate Canadian Postal Code
    private static boolean isInCanada(String country) {
        return country.equalsIgnoreCase("canada");
    }
    
 // Function to validate listing Type
    private static boolean isValidListingType(String type) {
        String[] validListingTypes = { "Home", "Apartment", "Studio" };
        for (String validType : validListingTypes) {
            if (validType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }


    // Function to validate Canadian City (Assuming common Canadian city names)
    private static boolean isValidCanadianCity(String City) {
        // Basic check for common Canadian city names
        String[] validCanadianCities = {
            "Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa", "Edmonton", "Quebec City", "Winnipeg", "Halifax"
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
			// Ensure that the listing is available for rent in the specific date range
			if (!listing.isAvailable(connection, startDate, endDate)) {
				throw new IllegalStateException("The listing is not available in the specific date range.");
			}

			// Check if the listing is booked on any date in the specific range
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
			// Check if the listing is booked on the specified date
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
		// Check if the renter has completed a stay recently
		if (!Rented.hasRecentStay(connection, HostUID, RenterUID)) {
			throw new IllegalStateException("The renter has not completed a stay recently. Cannot comment and rate.");
		}

		RenterReview.insert(connection, HostUID, RenterUID, rating, comment);
	}

	// Insert Comments for Host
	public static void insertHostComment(Connection connection, int RenterUID, int LID, int rating,
			String comment) {
		// Check if the host has rented the listing recently
		if (!Rented.hasRecentBooking(connection, RenterUID, LID)) {
			throw new IllegalStateException("The host has not rented the listing recently. Cannot comment and rate.");
		}

		ListingReview.insert(connection, RenterUID, LID, rating, comment);
	}

}
