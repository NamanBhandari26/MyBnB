package Queries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Tables.Listing;

public class Queries {

    // Helper method to create a PreparedStatement
    private static PreparedStatement prepareStatement(Connection connection, String query, Object... params)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement;
    }

    // Helper method to execute a query and return a list of listings
    private static List<Listing> executeListingQuery(Connection connection, String query, Object... params) {
        List<Listing> listings = new ArrayList<>();
        try (PreparedStatement statement = prepareStatement(connection, query, params);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                resultSet.getInt("LID");
                String Type = resultSet.getString("Type");
                double Longitude = resultSet.getDouble("Longitude");
                double Latitude = resultSet.getDouble("Latitude");
                String Address = resultSet.getString("Address");
                String PostalCode = resultSet.getString("PostalCode");
                String City = resultSet.getString("City");
                String Country = resultSet.getString("Country");
                int HostUID = resultSet.getInt("HostUID"); // Convert to int
                Listing listing = new Listing(connection, Type, Longitude, Latitude, Address, PostalCode, City, Country,
                        HostUID);
                listings.add(listing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    public static List<Listing> searchListingsByLocation(Connection connection, double latitude, double longitude,
            double distance, boolean sortByPriceAscending) {
        // Implement the query to return all listings within the specified distance of
        // the given location
        String sortBy = sortByPriceAscending ? "Price ASC" : "Distance ASC";
        String query = "SELECT LID, Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID, " +
                "SQRT(POW((Latitude - ?), 2) + POW((Longitude - ?), 2)) AS Distance " +
                "FROM Listing " +
                "HAVING Distance <= ? " +
                "ORDER BY " + sortBy;
        return executeListingQuery(connection, query, latitude, longitude, distance);
    }

    public static List<Listing> searchListingsByPostalCode(Connection connection, String postalCode,
            boolean sortByPriceAscending) {
        // Implement the query to return all listings in the same and adjacent postal
        // codes
        String sortBy = sortByPriceAscending ? "Price ASC" : "Distance ASC";
        String query = "SELECT LID, Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID, " +
                "SQRT(POW((Latitude - ?), 2) + POW((Longitude - ?), 2)) AS Distance " +
                "FROM Listing " +
                "WHERE PostalCode LIKE ? OR PostalCode LIKE ? OR PostalCode LIKE ? " +
                "ORDER BY " + sortBy;
        return executeListingQuery(connection, query, postalCode, postalCode + " %", "% " + postalCode,
                postalCode + "%", postalCode + " %");
    }

    public static Listing searchListingByAddress(Connection connection, String address) {
        // Implement the query to return the listing with the specified address if it
        // exists.
        String query = "SELECT LID, Type, Longitude, Latitude, Address, PostalCode, City, Country, HostUID " +
                "FROM Listing " +
                "WHERE Address = ?";
        List<Listing> listings = executeListingQuery(connection, query, address);
        return listings.isEmpty() ? null : listings.get(0);
    }

    public static List<Listing> searchListingsByDateRange(Connection connection, double latitude, double longitude,
            String startDate, String endDate, boolean sortByPriceAscending) {
        // Implement the query to return all listings available for booking in the
        // specified date range
        String sortBy = sortByPriceAscending ? "Price ASC" : "Distance ASC";
        String query = "SELECT L.LID, L.Type, L.Longitude, L.Latitude, L.Address, L.PostalCode, L.City, L.Country, L.HostUID, "
                +
                "SQRT(POW((L.Latitude - ?), 2) + POW((L.Longitude - ?), 2)) AS Distance " +
                "FROM Listing L " +
                "WHERE NOT EXISTS (" +
                "   SELECT * FROM Rented R " +
                "   WHERE R.LID = L.LID AND ((R.StartDate <= ? AND R.EndDate >= ?) OR (R.StartDate <= ? AND R.EndDate >= ?))"
                +
                ") " +
                "ORDER BY " + sortBy;
        return executeListingQuery(connection, query, latitude, longitude, endDate, startDate, startDate, endDate);
    }

    public static List<Listing> searchListingsWithFilters(Connection connection, double latitude, double longitude,
            String postalCode, List<Integer> amenityIds, String startDate, String endDate, double minPrice,
            double maxPrice, boolean sortByPriceAscending) {
        // Implement the query to return listings based on multiple filters:
        // postalCode: Listings in the same and adjacent postal codes
        // amenityIds: Listings with specified amenities
        // startDate and endDate: Listings available for booking in the specified date
        // range
        // minPrice and maxPrice: Listings with prices within the specified range
        String sortBy = sortByPriceAscending ? "Price ASC" : "Distance ASC";
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT L.LID, L.Type, L.Longitude, L.Latitude, L.Address, L.PostalCode, L.City, L.Country, L.HostUID, "
                        +
                        "SQRT(POW((L.Latitude - ?), 2) + POW((L.Longitude - ?), 2)) AS Distance " +
                        "FROM Listing L " +
                        "JOIN Has H ON L.LID = H.LID " +
                        "LEFT JOIN Availability A ON L.LID = A.LID " +
                        "WHERE PostalCode LIKE ? OR PostalCode LIKE ? OR PostalCode LIKE ? " +
                        "AND (? IS NULL OR A.Date IS NULL OR (A.Date >= ? AND A.Date <= ?)) " +
                        "AND (L.Type IS NULL OR L.Type IN (SELECT Type FROM Listing WHERE Type = L.Type)) " +
                        "AND (L.Price >= ? AND L.Price <= ?) ");
        if (!amenityIds.isEmpty()) {
            queryBuilder.append("AND H.AID IN (");
            for (int i = 0; i < amenityIds.size(); i++) {
                queryBuilder.append("?, ");
            }
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length()); // Remove the last comma and space
            queryBuilder.append(") ");
        }
        queryBuilder.append("ORDER BY ").append(sortBy);
        String query = queryBuilder.toString();

        List<Object> params = new ArrayList<>();
        params.add(latitude);
        params.add(longitude);
        params.add(postalCode);
        params.add(postalCode + " %");
        params.add("% " + postalCode);
        params.add(startDate);
        params.add(endDate);
        params.add(startDate);
        params.add(endDate);
        params.add(minPrice);
        params.add(maxPrice);
        params.addAll(amenityIds);

        return executeListingQuery(connection, query, params.toArray());
    }
}
