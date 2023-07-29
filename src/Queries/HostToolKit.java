package Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HostToolKit {
    public static void suggestedPrice(Connection connection, Scanner scanner) {
        System.out.println("Enter Date (yyyy-mm-dd):");
        String firstDate = scanner.nextLine();
        System.out.println("Enter City:");
        String city = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("SELECT avg(price) as suggested FROM listing L natural join availability A Where L.City = ? and A.Date = ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, city);
            statement.setString(2, firstDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println( "Suggested Price: " + resultSet.getFloat("suggested"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void suggestedAmenities(Connection connection, Scanner scanner) {
        System.out.println("Enter LID:");
        String lid = scanner.nextLine();
        
        try (PreparedStatement statement = connection.prepareStatement("select A.type from ((SELECT distinct aid FROM has as H1 WHERE NOT EXISTS (( SELECT H2.aid FROM has as H2 where H2.lid=?) EXCEPT (SELECT H3.aid FROM has H3 WHERE H3.lid = H1.lid ))) EXCEPT (SELECT H2.aid FROM has as H2 where H2.lid=?)) AS D natural join amenities A;", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, lid);
            statement.setString(2, lid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Amenity Recommended: " + resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
