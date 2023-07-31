package JDBC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Queries.Operations;
import Queries.Display;
import Queries.HostToolKit;
import Queries.Queries;
import Queries.Report;

public class Service {
    public MySQL database;
    private Scanner scanner;

    // Enum for user input options
    private enum UserOption {
        _1_,
        _2_,
        _3_,
        _4_,
        _5_,
        _6_,
        _7_,
        _8_,
        _9_,
        _10_,
        _11_,
        _12_,
        _13_,
        _14_,
        _15_,
        _16_,
        _17_,
        _18_,
        _19_,
        _20_,
        _21_,
        _22_,
        _23_,
        _24_,
        _25_,
        _26_,
        _27_,
        _28_,
        _29_,
        _30_,
        _31_,
        _32_,
        _33_,
        _34_,
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
        System.out.println("Starting Services...");
        performOperations();
    }

    public void performOperations() throws SQLException {
        boolean quit = false;
        while (!quit) {
            printMenu();
            String userInput = scanner.nextLine();

            try {
                UserOption userOption = UserOption.valueOf("_" + userInput + "_");

                switch (userOption) {
                    case _1_:
                        System.out.println("Are you sure you want to create tables? (y/n):");
                        String confirmCreateTables = scanner.nextLine().trim().toLowerCase();
                        if (confirmCreateTables.equals("y")) {
                            database.createTables();
                            System.out.println("Operation completed.");
                        } else {
                            System.out.println("Operation canceled.");
                        }
                        break;
                    case _2_:
                        System.out.println("Are you sure you want to drop tables? (y/n):");
                        String confirmDropTables = scanner.nextLine().trim().toLowerCase();
                        if (confirmDropTables.equals("y")) {
                            database.dropTables();
                            System.out.println("Operation completed.");
                        } else {
                            System.out.println("Operation canceled.");
                        }
                        break;
                    case _3_:
                        System.out.println("Are you sure you want to drop the database? (y/n):");
                        String confirmDropDatabase = scanner.nextLine().trim().toLowerCase();
                        if (confirmDropDatabase.equals("y")) {
                            database.dropDatabase();
                            System.out.println("Operation completed.");
                        } else {
                            System.out.println("Operation canceled.");
                        }
                        break;
                    case _4_:
                    	database.addData();
                    	System.out.println("Data Added...");
                    	break;
                    case _5_:
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

                    case _6_:
                        // Prompt user for input for deleteUser operation
                        System.out.println("Enter the UID of the user to delete:");
                        int uidToDelete = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        Operations.deleteUser(database.getConnection(), uidToDelete);
                        System.out.println("User with UID " + uidToDelete + " deleted.");
                        break;

                    case _7_:
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

                    case _8_:
                        // Prompt user for input for removeListing operation
                        System.out.println("Enter listing ID (LID) to remove:");
                        int lidToRemove = scanner.nextInt();
                        System.out.println("Enter user ID (UID) for authorization:");
                        int uidForAuthorization = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        Operations.removeListing(database.getConnection(), lidToRemove, uidForAuthorization);
                        System.out.println("Listing with LID " + lidToRemove + " removed.");
                        break;

                    case _9_:
                        // Prompt user for input for cancelBooking operation
                        System.out.println("Enter booking ID (RID) to cancel:");
                        int ridToCancel = scanner.nextInt();
                        System.out.println("Enter user ID (UID) for authorization:");
                        int uidForCancelAuthorization = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        Operations.cancelBooking(database.getConnection(), ridToCancel, uidForCancelAuthorization);
                        System.out.println("Booking with RID " + ridToCancel + " canceled.");
                        break;

                    case _10_:
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

                    case _11_:
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

                    case _12_:
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

                    case _13_:
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

                    case _14_:
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

                    case _15_:
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

                        List<Display> listingsByLocation = Queries.searchListingsByLocation(database.getConnection(),
                                searchLatitude, searchLongitude, maxDistance, sortByPriceAscending);
                        System.out.println("Listings found within " + maxDistance + " km of the specified location:");
                        displayListings(listingsByLocation);
                        break;

                    case _16_:
                        // Prompt user for input for searchListingsByPostalCode operation
                        System.out.println("Enter postal code for search:");
                        String searchPostalCode = scanner.nextLine();
                        System.out.println("Sort by price ascending? (true/false):");
                        boolean sortByPriceAscendingByPostal = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        List<Display> listingsByPostalCode = Queries.searchListingsByPostalCode(
                                database.getConnection(), searchPostalCode, sortByPriceAscendingByPostal);
                        System.out.println("Listings found within the same and adjacent postal codes:");
                        displayListings(listingsByPostalCode);
                        break;

                    case _17_:
                        // Prompt user for input for searchListingByAddress operation
                        System.out.println("Enter address for search:");
                        String searchAddress = scanner.nextLine();

                        Display foundListingByAddress = Queries.searchListingByAddress(database.getConnection(),
                                searchAddress);
                        if (foundListingByAddress != null) {
                            System.out.println("Listing found with the specified address:");
                            foundListingByAddress.displayDetails();
                        } else {
                            System.out.println("No listing found with the specified address.");
                        }
                        break;

                    // Inside the performOperations() method
                    case _18_:
                        // Prompt user for input for searchListingsByDateRange operation
                        System.out.println("Enter start date (yyyy-mm-dd) for search:");
                        String startDateByDate = scanner.nextLine();
                        System.out.println("Enter end date (yyyy-mm-dd) for search:");
                        String endDateByDate = scanner.nextLine();
                        System.out.println("Sort by price ascending? (true/false):");
                        boolean sortByPriceAscendingByDate = scanner.nextBoolean();
                        scanner.nextLine(); // Consume the newline character

                        List<Display> listingsByDateRange = Queries.searchListingsByDateRange(database.getConnection(),
                                startDateByDate, endDateByDate, sortByPriceAscendingByDate);
                        System.out.println("Listings available for booking between " + startDateByDate + " and "
                                + endDateByDate + ":");
                        displayListings(listingsByDateRange);
                        break;

                    case _19_:
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

                        List<Display> listingsWithFilters = Queries.searchListingsWithFilters(database.getConnection(),
                                searchLatWithFilters, searchLongWithFilters, searchPostalCodeWithFilters, amenityIds,
                                startDateWithFilters,
                                endDateWithFilters, minPriceWithFilters, maxPriceWithFilters,
                                sortByPriceAscendingWithFilters);
                        System.out.println("Listings with applied filters:");
                        displayListings(listingsWithFilters);
                        break;
                    
                    case _20_:
                        Report.totalBookingByDateAndCity(database.getConnection(), scanner);
                        break;
                    case _21_:
                        Report.totalBookingByDateAndPostal(database.getConnection(), scanner);
                        break;
                    case _22_:
                        Report.totalListingByCountry(database.getConnection(), scanner);
                        break;
                    case _23_:
                        Report.totalListingByCountryAndCity(database.getConnection(), scanner);
                        break;
                    case _24_:
                        Report.totalListingByPostal(database.getConnection(), scanner);
                        break;
                    case _25_:
                        Report.rankHostsPerCountry(database.getConnection(), scanner);
                        break;
                    case _26_:
                        Report.rankHostsPerCountryAndCity(database.getConnection(), scanner);
                        break;
                    case _27_:
                        Report.commercialHosts(database.getConnection(), scanner);
                        break;
                    case _28_:
                        Report.rankRenterBetweenDate(database.getConnection(), scanner);
                        break;
                    case _29_:
                        Report.rankRenterBetweenDateAndCity(database.getConnection(), scanner);
                        break;
                    case _30_:
                        Report.mostHostCancels(database.getConnection());
                        break;
                    case _31_:
                        Report.mostRenterCancels(database.getConnection());
                        break;
                    case _32_:
                        Report.getNounPrasesForlisting(database.getConnection());
                        break;
                    case _33_:
                        HostToolKit.suggestedPrice(database.getConnection(), scanner);
                        break;
                    case _34_:
                        HostToolKit.suggestedAmenities(database.getConnection(), scanner);
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
        System.out.println("Service Ended.....");
        System.out.println("Thank you!");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("========= Menu =========");
        System.out.println("1. Create Tables");
        System.out.println("2. Drop Tables");
        System.out.println("3. Drop Database");
        System.out.println("4. Add Data");
        System.out.println("5. Create User Profile");
        System.out.println("6. Delete User Profile");
        System.out.println("7. Book Listing");
        System.out.println("8. Remove Listing");
        System.out.println("9. Cancel Booking");
        System.out.println("10. Create Listing");
        System.out.println("11. Update Listing Price");
        System.out.println("12. Update Listing Availability");
        System.out.println("13. Insert Renter Comment");
        System.out.println("14. Insert Host Comment");
        System.out.println("15. Search Listings by Location");
        System.out.println("16. Search Listings by Postal Code");
        System.out.println("17. Search Listing by Address");
        System.out.println("18. Search Listings by Date Range");
        System.out.println("19. Search Listings with Filters");
        System.out.println("20. Report Total Booking by Date and City");
        System.out.println("21. Report Total Booking by Date and Postal");
        System.out.println("22. Report Total Listing by Country");
        System.out.println("23. Report Total Listing by Country and City");
        System.out.println("24. Report Total Listing by Postal");
        System.out.println("25. Report Rank Hosts Per Country");
        System.out.println("26. Report Rank Hosts Per Country and City");
        System.out.println("27. Report Commercial Hosts");
        System.out.println("28. Report Rank Renter Between Date");
        System.out.println("29. Report Rank Renter Between Date and City");
        System.out.println("30. Report Most Host Cancels");
        System.out.println("31. Report Most Renter Cancels");
        System.out.println("32. Report Get Noun Prases for Listing");
        System.out.println("33. Host Tool Kit Suggested Price");
        System.out.println("34. Host Tool Kit Suggested Amenities");
        System.out.println("35. Quit");
        System.out.println("=========================");
        System.out.println("Enter the number of the operation you want to perform:");
    }


    private void displayListings(List<Display> listings) {
        for (Display listing : listings) {
            listing.displayDetails();
        }
    }

}
