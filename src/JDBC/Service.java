package JDBC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Queries.Operations;
import Queries.Queries;
import Tables.Listing;

public class Service {
    public MySQL database;
    private Scanner scanner;

    // Enum for user input options
    private enum UserOption {
        CREATE_USER_PROFILE,
        DELETE_USER_PROFILE,
        BOOK_LISTING,
        REMOVE_LISTING,
        CANCEL_BOOKING,
        CREATE_LISTING,
        UPDATE_LISTING_PRICE,
        UPDATE_LISTING_AVAILABILITY,
        INSERT_RENTER_COMMENT,
        INSERT_HOST_COMMENT,
        SEARCH_LISTINGS_BY_LOCATION,
        SEARCH_LISTINGS_BY_POSTAL_CODE,
        SEARCH_LISTING_BY_ADDRESS,
        SEARCH_LISTINGS_BY_DATE_RANGE,
        SEARCH_LISTINGS_WITH_FILTERS,
        QUIT
    }

    public Service() throws SQLException, ClassNotFoundException {
        try {
            this.database = new MySQL();
            this.scanner = new Scanner(System.in);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startServices() throws SQLException {
        database.createTables();
        System.out.println("Starting Services...");
        database.addData();
        performOperations();
    }

    public void performOperations() throws SQLException {
        boolean quit = false;
        while (!quit) {
            printMenu();
            String userInput = scanner.nextLine();

            try {
                UserOption userOption = UserOption.valueOf(userInput.toUpperCase());

                switch (userOption) {
                    case CREATE_USER_PROFILE:
                        // Prompt user for input for createUser operation
                        System.out.println("Enter user name:");
                        String userName = scanner.nextLine();
                        System.out.println("Enter address:");
                        String address = scanner.nextLine();
                        System.out.println("Enter date of birth (yyyy-mm-dd):");
                        String dob = scanner.nextLine();
                        System.out.println("Enter occupation:");
                        String occupation = scanner.nextLine();
                        System.out.println("Enter SIN:");
                        String sin = scanner.nextLine();
                        System.out.println("Enter payment method:");
                        String payment = scanner.nextLine();
                        System.out.println("Is the user a renter? (true/false):");
                        boolean isRenter = scanner.nextBoolean();
                        System.out.println("Is the user a host? (true/false):");
                        boolean isHost = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        int result = Operations.createUser(database.getConnection(), userName, address, dob, occupation,
                                sin, payment, isRenter, isHost);
                        System.out.println("User created with UID: " + result);
                        break;

                    case DELETE_USER_PROFILE:
                        // Prompt user for input for deleteUser operation
                        System.out.println("Enter the UID of the user to delete:");
                        int uidToDelete = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        Operations.deleteUser(database.getConnection(), uidToDelete);
                        System.out.println("User with UID " + uidToDelete + " deleted.");
                        break;

                    case BOOK_LISTING:
                        // Prompt user for input for bookListing operation
                        System.out.println("Enter listing ID (LID) to book:");
                        int lidToBook = scanner.nextInt();
                        System.out.println("Enter user ID (UID) for booking:");
                        int uidForBooking = scanner.nextInt();
                        System.out.println("Enter total amount for booking:");
                        double totalAmount = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Enter start date (yyyy-mm-dd) for booking:");
                        String startDate = scanner.nextLine();
                        System.out.println("Enter end date (yyyy-mm-dd) for booking:");
                        String endDate = scanner.nextLine();
                        System.out.println("Is the booking canceled? (true/false):");
                        boolean isCanceled = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        int bookingResult = Operations.bookListing(database.getConnection(), lidToBook, uidForBooking,
                                totalAmount, startDate, endDate, isCanceled);
                        System.out.println("Booking created with RID: " + bookingResult);
                        break;

                    case REMOVE_LISTING:
                        // Prompt user for input for removeListing operation
                        System.out.println("Enter listing ID (LID) to remove:");
                        int lidToRemove = scanner.nextInt();
                        System.out.println("Enter user ID (UID) for authorization:");
                        int uidForAuthorization = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        Operations.removeListing(database.getConnection(), lidToRemove, uidForAuthorization);
                        System.out.println("Listing with LID " + lidToRemove + " removed.");
                        break;

                    case CANCEL_BOOKING:
                        // Prompt user for input for cancelBooking operation
                        System.out.println("Enter booking ID (RID) to cancel:");
                        int ridToCancel = scanner.nextInt();
                        System.out.println("Enter user ID (UID) for authorization:");
                        int uidForCancelAuthorization = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        Operations.cancelBooking(database.getConnection(), ridToCancel, uidForCancelAuthorization);
                        System.out.println("Booking with RID " + ridToCancel + " canceled.");
                        break;

                    case CREATE_LISTING:
                        // Prompt user for input for createListing operation
                        System.out.println("Enter listing type:");
                        String listingType = scanner.nextLine();
                        System.out.println("Enter longitude:");
                        double longitude = scanner.nextDouble();
                        System.out.println("Enter latitude:");
                        double latitude = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Enter address:");
                        String listingAddress = scanner.nextLine();
                        System.out.println("Enter postal code:");
                        String postalCode = scanner.nextLine();
                        System.out.println("Enter city:");
                        String city = scanner.nextLine();
                        System.out.println("Enter country:");
                        String country = scanner.nextLine();
                        System.out.println("Enter user ID (UID) for listing host:");
                        int hostUid = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        int listingResult = Operations.createListing(database.getConnection(), listingType, longitude,
                                latitude, listingAddress, postalCode, city, country, hostUid);
                        System.out.println("Listing created with LID: " + listingResult);
                        break;

                    case UPDATE_LISTING_PRICE:
                        // Prompt user for input for updateListingPrice operation
                        System.out.println("Enter listing ID (LID) to update price:");
                        int lidToUpdatePrice = scanner.nextInt();
                        System.out.println("Enter new price:");
                        double newPrice = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Enter start date (yyyy-mm-dd) for price update:");
                        String updateStartDate = scanner.nextLine();
                        System.out.println("Enter end date (yyyy-mm-dd) for price update:");
                        String updateEndDate = scanner.nextLine();

                        Operations.updateListingPrice(database.getConnection(), lidToUpdatePrice, newPrice,
                                updateStartDate, updateEndDate);
                        System.out.println("Price updated for listing with LID " + lidToUpdatePrice);
                        break;

                    case UPDATE_LISTING_AVAILABILITY:
                        // Prompt user for input for updateListingAvailability operation
                        System.out.println("Enter listing ID (LID) to update availability:");
                        int lidToUpdateAvailability = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Enter date (yyyy-mm-dd) for availability update:");
                        String availabilityDate = scanner.nextLine();
                        System.out.println("Is the listing available on this date? (true/false):");
                        boolean isAvailable = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        Operations.updateListingAvailability(database.getConnection(), lidToUpdateAvailability,
                                availabilityDate, isAvailable);
                        System.out.println("Availability updated for listing with LID " + lidToUpdateAvailability);
                        break;

                    case INSERT_RENTER_COMMENT:
                        // Prompt user for input for insertRenterComment operation
                        System.out.println("Enter host ID (UID) for renter comment:");
                        int hostUidForRenterComment = scanner.nextInt();
                        System.out.println("Enter renter ID (UID) for renter comment:");
                        int renterUidForRenterComment = scanner.nextInt();
                        System.out.println("Enter rating (1-5) for renter comment:");
                        int renterRating = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Enter comment for renter:");
                        String renterComment = scanner.nextLine();

                        Operations.insertRenterComment(database.getConnection(), hostUidForRenterComment,
                                renterUidForRenterComment, renterRating, renterComment);
                        System.out.println("Renter comment inserted.");
                        break;

                    case INSERT_HOST_COMMENT:
                        // Prompt user for input for insertHostComment operation
                        System.out.println("Enter renter ID (UID) for host comment:");
                        int renterUidForHostComment = scanner.nextInt();
                        System.out.println("Enter Listing ID (LID) for host comment:");
                        int listingLidForHostComment = scanner.nextInt();
                        System.out.println("Enter rating (1-5) for host comment:");
                        int hostRating = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Enter comment for host:");
                        String hostComment = scanner.nextLine();

                        Operations.insertHostComment(database.getConnection(), renterUidForHostComment,
                                listingLidForHostComment, hostRating, hostComment);
                        System.out.println("Host comment inserted.");
                        break;

                    case SEARCH_LISTINGS_BY_LOCATION:
                        // Prompt user for input for searchListingsByLocation operation
                        System.out.println("Enter latitude for search:");
                        double searchLatitude = scanner.nextDouble();
                        System.out.println("Enter longitude for search:");
                        double searchLongitude = scanner.nextDouble();
                        System.out.println("Enter maximum distance (in km) for search:");
                        double maxDistance = scanner.nextDouble();
                        System.out.println("Sort by price ascending? (true/false):");
                        boolean sortByPriceAscending = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        List<Listing> listingsByLocation = Queries.searchListingsByLocation(database.getConnection(),
                                searchLatitude, searchLongitude, maxDistance, sortByPriceAscending);
                        System.out.println("Listings found within " + maxDistance + " km of the specified location:");
                        displayListings(listingsByLocation);
                        break;

                    case SEARCH_LISTINGS_BY_POSTAL_CODE:
                        // Prompt user for input for searchListingsByPostalCode operation
                        System.out.println("Enter postal code for search:");
                        String searchPostalCode = scanner.nextLine();
                        System.out.println("Sort by price ascending? (true/false):");
                        boolean sortByPriceAscendingByPostal = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        List<Listing> listingsByPostalCode = Queries.searchListingsByPostalCode(
                                database.getConnection(), searchPostalCode, sortByPriceAscendingByPostal);
                        System.out.println("Listings found within the same and adjacent postal codes:");
                        displayListings(listingsByPostalCode);
                        break;

                    case SEARCH_LISTING_BY_ADDRESS:
                        // Prompt user for input for searchListingByAddress operation
                        System.out.println("Enter address for search:");
                        String searchAddress = scanner.nextLine();

                        Listing foundListingByAddress = Queries.searchListingByAddress(database.getConnection(),
                                searchAddress);
                        if (foundListingByAddress != null) {
                            System.out.println("Listing found with the specified address:");
                            displayListing(foundListingByAddress);
                        } else {
                            System.out.println("No listing found with the specified address.");
                        }
                        break;

                    // Inside the performOperations() method
                    case SEARCH_LISTINGS_BY_DATE_RANGE:
                        // Prompt user for input for searchListingsByDateRange operation
                        System.out.println("Enter latitude for search:");
                        double searchLatByDate = scanner.nextDouble();
                        System.out.println("Enter longitude for search:");
                        double searchLongByDate = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Enter start date (yyyy-mm-dd) for search:");
                        String startDateByDate = scanner.nextLine();
                        System.out.println("Enter end date (yyyy-mm-dd) for search:");
                        String endDateByDate = scanner.nextLine();
                        System.out.println("Sort by price ascending? (true/false):");
                        boolean sortByPriceAscendingByDate = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        List<Listing> listingsByDateRange = Queries.searchListingsByDateRange(database.getConnection(),
                                searchLatByDate,
                                searchLongByDate, startDateByDate, endDateByDate, sortByPriceAscendingByDate);
                        System.out.println("Listings available for booking between " + startDateByDate + " and "
                                + endDateByDate + ":");
                        displayListings(listingsByDateRange);
                        break;

                    case SEARCH_LISTINGS_WITH_FILTERS:
                        // Prompt user for input for searchListingsWithFilters operation
                        System.out.println("Enter latitude for search:");
                        double searchLatWithFilters = scanner.nextDouble();
                        System.out.println("Enter longitude for search:");
                        double searchLongWithFilters = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Enter postal code for search:");
                        String searchPostalCodeWithFilters = scanner.nextLine();
                        System.out.println("Enter start date (yyyy-mm-dd) for search:");
                        String startDateWithFilters = scanner.nextLine();
                        System.out.println("Enter end date (yyyy-mm-dd) for search:");
                        String endDateWithFilters = scanner.nextLine();
                        System.out.println("Enter minimum price for search:");
                        double minPriceWithFilters = scanner.nextDouble();
                        System.out.println("Enter maximum price for search:");
                        double maxPriceWithFilters = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character
                        System.out.println("Sort by price ascending? (true/false):");
                        boolean sortByPriceAscendingWithFilters = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        List<Integer> amenityIds = new ArrayList<>();
                        boolean addAmenity = true;
                        while (addAmenity) {
                            System.out.println("Enter amenity ID to filter (Enter -1 to stop adding amenities):");
                            int amenityId = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character

                            if (amenityId == -1) {
                                addAmenity = false;
                            } else {
                                amenityIds.add(amenityId);
                            }
                        }

                        List<Listing> listingsWithFilters = Queries.searchListingsWithFilters(database.getConnection(),
                                searchLatWithFilters, searchLongWithFilters, searchPostalCodeWithFilters, amenityIds,
                                startDateWithFilters,
                                endDateWithFilters, minPriceWithFilters, maxPriceWithFilters,
                                sortByPriceAscendingWithFilters);
                        System.out.println("Listings with applied filters:");
                        displayListings(listingsWithFilters);
                        break;

                    case QUIT:
                        quit = true;
                        break;

                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            } catch (IllegalArgumentException | InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        // When the user chooses to quit, call the dropDatabase() function
        database.dropTables();
        scanner.close();
    }

    private void printMenu() {
        System.out.println("========= Menu =========");
        System.out.println("1. Create User Profile");
        System.out.println("2. Delete User Profile");
        System.out.println("3. Book Listing");
        System.out.println("4. Remove Listing");
        System.out.println("5. Cancel Booking");
        System.out.println("6. Create Listing");
        System.out.println("7. Update Listing Price");
        System.out.println("8. Update Listing Availability");
        System.out.println("9. Insert Renter Comment");
        System.out.println("10. Insert Host Comment");
        System.out.println("11. Search Listings by Location");
        System.out.println("12. Search Listings by Postal Code");
        System.out.println("13. Search Listing by Address");
        System.out.println("14. Search Listings by Date Range");
        System.out.println("15. Search Listings with Filters");
        System.out.println("16. Quit");
        System.out.println("=========================");
        System.out.println("Enter the name of the operation you want to perform(separated by _):");
    }

    private void displayListings(List<Listing> listings) {
        for (Listing listing : listings) {
            displayListing(listing);
        }
    }

    private void displayListing(Listing listing) {
        System.out.println("Listing ID (LID): " + listing.getLID());
        System.out.println("Type: " + listing.getType());
        System.out.println("Longitude: " + listing.getLongitude());
        System.out.println("Latitude: " + listing.getLatitude());
        System.out.println("Address: " + listing.getAddress());
        System.out.println("Postal Code: " + listing.getPostalCode());
        System.out.println("City: " + listing.getCity());
        System.out.println("Country: " + listing.getCountry());
        System.out.println("Host UID: " + listing.getHostUID());
        System.out.println("-------------------------");
    }
}
