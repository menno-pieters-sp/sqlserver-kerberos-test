package sailpoint.pse.jdbc;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class KerberosJDBCClient {

  public static String readConsoleString(String prompt) throws IOException {
		Console console = System.console();
        if (console == null) {
        	String error = "Couldn't get Console instance";
            System.out.println(error);
            throw new IOException(error);
        }
        return console.readLine(prompt);
	}

	public static String readConsolePassword(String prompt) throws IOException {
		Console console = System.console();
        if (console == null) {
        	String error = "Couldn't get Console instance";
            System.out.println(error);
            throw new IOException(error);
        }
        return new String(console.readPassword(prompt));
	}

  public static boolean isNumeric(String s) {
    return s != null && s.matches("^[0-9]+$");
  }

  public static boolean isNullOrEmpty(String s) {
    return s == null || s.length() == 0;
  }

  public static boolean isNotNullOrEmpty(String s) {
    return s != null && s.length() > 0;
  }

  public static void main(String[] args) throws IOException {
    Options options = new Options();

    String hostname = null;
    String port = "1433";
    String user = null;
    String password = null;

    Option option_hostname = new Option("h", "host", true, "Database host");
    option_hostname.setRequired(true);
    options.addOption(option_hostname);

    Option option_port = new Option("P", "port", true, "Database port");
    option_port.setRequired(false);
    options.addOption(option_port);

    Option option_user = new Option("u", "user", true, "Database user");
    option_user.setRequired(false);
    options.addOption(option_user);

    Option option_password = new Option("p", "password", true, "Database user password");
    option_password.setRequired(false);
    options.addOption(option_password);

    Option option_help = new Option("?", "help", false, "Show help");
    option_help.setRequired(false);
    options.addOption(option_help);

    CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			formatter.printHelp("SQL Server JDBC Kerberos Test Tool", options);
			System.exit(1);
		}

    boolean help = cmd.hasOption(option_help);
    if (help) {
			formatter.printHelp("SQL Server JDBC Kerberos Test Tool", options);
			System.exit(0);
    }

    if (cmd.hasOption(option_hostname)) {
      hostname = cmd.getOptionValue(option_hostname);
    }

    if (cmd.hasOption(option_port)) {
      port = cmd.getOptionValue(option_port);
      if (!isNumeric(port)) {
        System.err.println("Numeric port number required!");
        System.exit(2);
      }
    }

    if (cmd.hasOption(option_user)) {
      user = cmd.getOptionValue(option_user);
    }

    if (cmd.hasOption(option_password)) {
      password = cmd.getOptionValue(option_password);
    }

    if (isNullOrEmpty(user)) {
      user = readConsoleString("Username: ");
    }

    if (isNullOrEmpty(password)) {
      password = readConsolePassword("Password: ");
    }

    if (isNullOrEmpty(hostname)) {
      System.err.println("Hostname missing!");
      System.exit(2);
    }

    if (isNullOrEmpty(user)) {
      System.err.println("Username missing!");
      System.exit(2);
    }

    if (isNullOrEmpty(password)) {
      System.err.println("Password missing!");
      System.exit(2);
    }

    String connUrl = String.format("jdbc:sqlserver://%s:%s;integratedSecurity=true;authenticationScheme=JavaKerberos;trustServerCertificate=true;userName=%s;password=%s", hostname, port, user, password);
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      Connection conn = DriverManager.getConnection(connUrl);
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery("select name as username from sys.database_principals");
      while (resultSet.next()) {
        System.out.println("Database User " + resultSet.getString(1));
      }
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}