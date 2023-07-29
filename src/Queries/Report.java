package Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

public class Report {

    public static void totalBookingByDateAndCity(Connection connection, Scanner scanner) {
        System.out.println("Enter Start Date (yyyy-mm-dd):");
        String firstDate = scanner.nextLine();
        System.out.println("Enter End Date (yyyy-mm-dd):");
        String endDate = scanner.nextLine();
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();
        System.out.println("Enter City:");
        String city = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("Select count(*) from rented R natural join listing L where R.StartDate>=? AND R.EndDate<=? AND L.Country = ? AND L.city = ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, firstDate);
            statement.setString(2, endDate);
            statement.setString(3, Country);
            statement.setString(4, city);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Total Bookings: "+ resultSet.getInt("count(*)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void totalBookingByDateAndPostal(Connection connection, Scanner scanner) {
        System.out.println("Enter Start Date (yyyy-mm-dd):");
        String firstDate = scanner.nextLine();
        System.out.println("Enter End Date (yyyy-mm-dd):");
        String endDate = scanner.nextLine();
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();
        System.out.println("Enter PostalCode:");
        String PostalCode = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("Select count(*) from rented R natural join listing L where R.StartDate>=? AND R.EndDate<=? AND  L.Country = ? AND L.PostalCode Like ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, firstDate);
            statement.setString(2, endDate);
            statement.setString(3, Country);
            statement.setString(4, PostalCode + "%");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Total Bookings: "+ resultSet.getInt("count(*)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void totalListingByCountry(Connection connection, Scanner scanner) {
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();
        try (PreparedStatement statement = connection.prepareStatement("Select count(*) from listing L where L.Country = ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, Country);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Total Listing: "+resultSet.getInt("count(*)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void totalListingByCountryAndCity(Connection connection, Scanner scanner) {
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();
        System.out.println("Enter City:");
        String city = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("Select count(*) from listing L where L.Country = ? AND L.City = ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, Country);
            statement.setString(2, city);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Total Listing: "+resultSet.getInt("count(*)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void totalListingByPostal(Connection connection, Scanner scanner) {
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();
        System.out.println("Enter Postal:");
        String Postal = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("Select count(*) from listing L where L.Country = ? AND L.PostalCode LIKE ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, Country);
            statement.setString(2, Postal + "%");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Total Listing: "+resultSet.getInt("count(*)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rankHostsPerCountry(Connection connection, Scanner scanner) {
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("Select hostuid, count(*) from listing L where L.country = ? GROUP BY hostuid ORDER BY count(*) DESC", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, Country);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("HostID: "+ resultSet.getInt("hostuid") + ", with " + resultSet.getInt("count(*)") + " listings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rankHostsPerCountryAndCity(Connection connection, Scanner scanner) {
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();
        System.out.println("Enter City:");
        String city = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("Select hostuid, count(*) from listing L where L.country = ? AND L.City = ? GROUP BY hostuid ORDER BY count(*) DESC", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, Country);
            statement.setString(2, city);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("HostID: "+ resultSet.getInt("hostuid") + ", with " + resultSet.getInt("count(*)") + " listings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commercialHosts(Connection connection, Scanner scanner) {
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();
        System.out.println("Enter City:");
        String city = scanner.nextLine();

        int total = 0;
        try (PreparedStatement statement = connection.prepareStatement("Select count(*) from listing L where L.Country = ? AND L.City = ?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, Country);
            statement.setString(2, city);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // System.out.println(resultSet.getInt("count(*)"));
                total = resultSet.getInt("count(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try (PreparedStatement statement = connection.prepareStatement("Select hostuid, count(*) from listing L where L.country = ? AND L.city = ? GROUP BY hostuid Having count(*)>?*0.1 ORDER BY count(*) DESC", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, Country);
            statement.setString(2, city);
            statement.setInt(3, total);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("HostID: "+ resultSet.getInt("hostuid") + ", with " + resultSet.getInt("count(*)") + " listings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rankRenterBetweenDate(Connection connection, Scanner scanner) {
        System.out.println("Enter Start Date (yyyy-mm-dd):");
        String firstDate = scanner.nextLine();
        System.out.println("Enter End Date (yyyy-mm-dd):");
        String endDate = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("Select uid, count(*) from rented R where R.StartDate>=? AND R.EndDate<=? Group by uid ORDER BY count(*) DESC", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, firstDate);
            statement.setString(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("RenterID: "+resultSet.getInt("uid") + ", with " + resultSet.getInt("count(*)")+ " bookings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rankRenterBetweenDateAndCity(Connection connection, Scanner scanner) {
        System.out.println("Enter Start Date (yyyy-mm-dd):");
        String firstDate = scanner.nextLine();
        System.out.println("Enter End Date (yyyy-mm-dd):");
        String endDate = scanner.nextLine();
        System.out.println("Enter Country:");
        String Country = scanner.nextLine();
        System.out.println("Enter City:");
        String city = scanner.nextLine();

        try (PreparedStatement statement = connection.prepareStatement("Select uid, count(*) from rented R natural join listing L where R.StartDate>=? AND R.EndDate<=? AND L.country = ? AND L.city = ? Group by uid ORDER BY count(*) DESC", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, firstDate);
            statement.setString(2, endDate);
            statement.setString(3, Country);
            statement.setString(4, city);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("RenterID: "+resultSet.getInt("uid") + ", with " + resultSet.getInt("count(*)")+ " bookings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mostHostCancels(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("Select hostuid, count(*) from rented R natural join listing L where R.iscanceled group by hostuid", Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("HostID: "+ resultSet.getInt("hostuid") + ", with " + resultSet.getInt("count(*)") + " cancels");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mostRenterCancels(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("Select uid, count(*) from rented R natural join listing L where R.iscanceled group by uid", Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("RenterID: "+ resultSet.getInt("uid") + ", with " + resultSet.getInt("count(*)") + " cancels");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getNounPhrases(Parse p, HashMap<String, Integer> nounPhrases) {
        //
        if (p.getType().equals("NP")) { // NP=noun phrase
          if (nounPhrases.get(p.getCoveredText()) == null) {
            nounPhrases.put(p.getCoveredText(), 1);
          }
          else {
            nounPhrases.put(p.getCoveredText(), nounPhrases.get(p.getCoveredText()) + 1);
          }
        }
        for (Parse child : p.getChildren())
          getNounPhrases(child, nounPhrases);
      }

    public static void getNounPrasesForlisting(Connection connection){
        try (PreparedStatement statement = connection.prepareStatement("SELECT lid, GROUP_CONCAT(L.comment SEPARATOR '. ') as allComment FROM listingreview L group by L.lid", Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String sentence = resultSet.getString("allComment");
                HashMap<String, Integer> nounPhrases = new HashMap<>();
                InputStream modelInParse = null;
                try {
                    modelInParse = new FileInputStream("en-parser-chunking.bin");
                    
                    ParserModel model = new ParserModel(modelInParse);

                    Parser parser = ParserFactory.create(model);
                    Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

                    for (Parse p : topParses)
                        getNounPhrases(p, nounPhrases);

                    System.out.println("--------------------");
                    System.out.print("LID: " + resultSet.getString("lid") + "\n");
                    for (String s : nounPhrases.keySet())
                        System.out.println("Noun: " + s + ", Freq: " + nounPhrases.get(s));
                    System.out.println("--------------------");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (modelInParse != null) {
                        try {
                        modelInParse.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }
}
