package Queries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Queries {

    private static PreparedStatement prepareStatement(Connection connection, String query, Object... params)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        int numParams = params.length;
        int numPlaceholders = countPlaceholders(query);

        if (numParams != numPlaceholders) {
            throw new IllegalArgumentException("Number of parameters does not match number of placeholders in the query.");
        }

        for (int i = 0; i < numParams; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement;
    }

    private static int countPlaceholders(String query) {
        int count = 0;
        int index = query.indexOf('?');
        while (index != -1) {
            count++;
            index = query.indexOf('?', index + 1);
        }
        return count;
    }

    private static List<Display> executeListingQuery(Connection connection, String query, Object... params) {
        List<Display> listings = new ArrayList<>();
        try (PreparedStatement statement = prepareStatement(connection, query, params);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int LID = resultSet.getInt("LID");
                String Type = resultSet.getString("Type");
                double Longitude = resultSet.getDouble("Longitude");
                double Latitude = resultSet.getDouble("Latitude");
                String Address = resultSet.getString("Address");
                String PostalCode = resultSet.getString("PostalCode");
                String City = resultSet.getString("City");
                String Country = resultSet.getString("Country");
                int HostUID = resultSet.getInt("HostUID"); // Convert to int
                double price = resultSet.getDouble("Price");
                boolean isAvailable = resultSet.getBoolean("isAvailable");
                String date = resultSet.getString("Date");
                double distance = resultSet.getDouble("Distance");
                Display listing = new Display(LID, Type, Longitude, Latitude, Address, PostalCode, City, Country,
                        HostUID, price, isAvailable, date, distance);
                listings.add(listing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    public static List<Display> searchListingsByLocation(Connection connection, double latitude, double longitude,
            double distance, boolean sortByPriceAscending) {
        String sortBy = sortByPriceAscending ? "A.Price ASC" : "Distance ASC";
        String query = "SELECT L.LID, L.Type, L.Longitude, L.Latitude, L.Address, L.PostalCode, L.City, L.Country, L.HostUID, "
                +
                "SQRT(POW((L.Latitude - ?), 2) + POW((L.Longitude - ?), 2)) AS Distance, A.Price, A.Date, A.isAvailable "
                +
                "FROM Listing AS L " +
                "INNER JOIN Availability AS A ON L.LID = A.LID " +
                "HAVING Distance <= ? " +
                "ORDER BY " + sortBy;
        return executeListingQuery(connection, query, latitude, longitude, distance);
    }

    public static List<Display> searchListingsByPostalCode(Connection connection, String postalCode,
            boolean sortByPriceAscending) {
        double originLatitude = 0.0;
        double originLongitude = 0.0;

        if (postalCode == null || postalCode.length() == 7) {
            throw new IllegalArgumentException("Invalid postal code format.");
        }

        String firstThreeLetters = postalCode.substring(0, 3) + "%";

        String sortBy = sortByPriceAscending ? "A.Price ASC" : "Distance ASC";
        String query = "SELECT L.LID, L.Type, L.Longitude, L.Latitude, L.Address, L.PostalCode, L.City, L.Country, L.HostUID, "
                +
                "SQRT(POW((L.Latitude - ?), 2) + POW((L.Longitude - ?), 2)) AS Distance, A.Price, A.Date, A.isAvailable "
                +
                "FROM Listing AS L " +
                "INNER JOIN Availability AS A ON L.LID = A.LID " +
                "WHERE L.PostalCode LIKE ? " +
                "ORDER BY " + sortBy;
        return executeListingQuery(connection, query, originLatitude, originLongitude, firstThreeLetters);
    }

    public static Display searchListingByAddress(Connection connection, String address) {
        double originLatitude = 0.0;
        double originLongitude = 0.0;

        String query = "SELECT L.LID, L.Type, L.Longitude, L.Latitude, L.Address, L.PostalCode, L.City, L.Country, L.HostUID, "
                +
                "A.Price, A.Date, A.isAvailable, " +
                "SQRT(POW((L.Latitude - ?), 2) + POW((L.Longitude - ?), 2)) AS Distance " +
                "FROM Listing AS L " +
                "INNER JOIN Availability AS A ON L.LID = A.LID " +
                "WHERE L.Address = ?";
        List<Display> listings = executeListingQuery(connection, query, originLatitude, originLongitude, address);
        return listings.isEmpty() ? null : listings.get(0);
    }

    public static List<Display> searchListingsByDateRange(Connection connection, String startDate, String endDate,
            boolean sortByPriceAscending) {
        double originLatitude = 0.0;
        double originLongitude = 0.0;
        String sortBy = sortByPriceAscending ? "A.Price ASC" : "Distance ASC";
        String query = "SELECT L.LID, L.Type, L.Longitude, L.Latitude, L.Address, L.PostalCode, L.City, L.Country, L.HostUID, "
                +
                "SQRT(POW((L.Latitude - ?), 2) + POW((L.Longitude - ?), 2)) AS Distance, A.Price, A.Date, A.isAvailable "
                +
                "FROM Listing AS L " +
                "INNER JOIN Availability AS A ON L.LID = A.LID " +
                "WHERE A.Date >= ? AND A.Date <= ? " +
                "AND NOT EXISTS (" +
                "   SELECT * FROM Rented R " +
                "   WHERE R.LID = L.LID AND R.EndDate >= ? AND R.StartDate <= ?" +
                ") " +
                "ORDER BY " + sortBy;
        return executeListingQuery(connection, query, originLatitude, originLongitude, startDate, endDate, startDate,
                endDate);
    }

    public static List<Display> searchListingsWithFilters(Connection connection, double latitude, double longitude,
            String postalCode, List<Integer> amenityIds, String startDate, String endDate, double minPrice,
            double maxPrice, boolean sortByPriceAscending) {
    	
        if (postalCode == null || postalCode.length() == 7) {
            throw new IllegalArgumentException("Invalid postal code format.");
        }

        String firstThreeLetters = postalCode.substring(0, 3) + "%";
        
        String sortBy = sortByPriceAscending ? "A.Price ASC" : "Distance ASC";
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT L.LID, L.Type, L.Longitude, L.Latitude, L.Address, L.PostalCode, L.City, L.Country, L.HostUID, "
                        +
                        "SQRT(POW((L.Latitude - ?), 2) + POW((L.Longitude - ?), 2)) AS Distance, A.Price, A.Date, A.isAvailable "
                        +
                        "FROM Listing L " +
                        "JOIN Has H ON L.LID = H.LID " +
                        "LEFT JOIN Availability A ON L.LID = A.LID " +
                        "WHERE (L.PostalCode LIKE ?) " +
                        "AND (? IS NULL OR ? IS NULL OR A.Date IS NULL OR (A.Date >= ? AND A.Date <= ?)) " + 
                        "AND (L.Type IS NULL OR L.Type IN (SELECT Type FROM Listing WHERE Type = L.Type)) " +
                        "AND (A.Price >= ? AND A.Price <= ?) ");
        if (amenityIds != null && !amenityIds.isEmpty()) {
            for (int i = 0; i < amenityIds.size(); i++) {
                queryBuilder.append("AND H.AID = ? ");
            }
        }
        queryBuilder.append("ORDER BY ").append(sortBy);
        String query = queryBuilder.toString();

        List<Object> params = new ArrayList<>();
        params.add(latitude);
        params.add(longitude);
        params.add(firstThreeLetters);
        params.add(startDate);
        params.add(endDate);
        params.add(startDate);
        params.add(endDate);
        params.add(minPrice);
        params.add(maxPrice);
        if (amenityIds != null) {
            params.addAll(amenityIds);
        }

        return executeListingQuery(connection, query, params.toArray());
    }


}
