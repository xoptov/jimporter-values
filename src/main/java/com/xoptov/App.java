package com.xoptov;

import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.cli.CommandLineParser;
import com.xoptov.manager.PropertyValueManager;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import com.xoptov.manager.ValueManager;
import org.apache.commons.cli.Options;
import com.xoptov.model.PropertyValue;
import com.xoptov.model.Value;
import java.io.IOException;
import java.io.FileReader;
import java.util.HashMap;
import java.io.Reader;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

public class App {
    private static int loadedCount = 0;

    private static int importedCount = 0;

    private static int duplicatesCount = 0;

    private static ValueManager vm;

    private static PropertyValueManager pvm;

    public static void main(String[] args) {

        Options options = new Options();
        options.addRequiredOption("i", "input", true, "File for import");
        options.addRequiredOption("c", "category", true, "Category for relation");
        options.addRequiredOption("p", "property", true, "Property for relation");
        options.addRequiredOption("P", "parent-property", true, "Parent property for relation");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        String filename = cmd.getOptionValue('i');

        try (Connection conn = getDBConnection()) {
            vm = new ValueManager(conn);
            pvm = new PropertyValueManager(conn);

            int categoryId = Integer.parseInt(cmd.getOptionValue('c'));
            int propertyId = Integer.parseInt(cmd.getOptionValue('p'));
            int parentPropertyId = Integer.parseInt(cmd.getOptionValue('P'));

            HashMap<String, Value> list = null;

            try (Reader in = new FileReader(filename)) {
                list = loadFromFile(in, categoryId, propertyId, parentPropertyId);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            if (list == null || list.isEmpty()) {
                System.out.println("Nothing to import");
            }



        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static HashMap<String, Value> loadFromFile(Reader in, int categoryId, int propertyId, int parentPropertyId) throws IOException {

        HashMap<String, Value> list = new HashMap<>();
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);

        records.forEach((CSVRecord record) -> {
            Value value;

            PropertyValue propertyValue = new PropertyValue(categoryId, propertyId);
            PropertyValue parentPropertyValue = new PropertyValue(categoryId, parentPropertyId);

            if (record.size() > 1) {
                value = vm.create(record.get(0), propertyValue);
                Value parentValue = vm.create(record.get(1), parentPropertyValue);
                value.setParent(parentValue);
            } else {
                value = vm.create(record.get(0), propertyValue);
            }

            list.put(value.getValue().toString(), value);
        });

        return list;
    }

    private static Connection getDBConnection() throws ConfigurationException, SQLException {

        Configurations configs = new Configurations();
        Configuration config = configs.properties(new File("database.properties"));

        String host = config.getString("database.host");
        int port = config.getInt("database.port");
        String name = config.getString("database.name");
        String user = config.getString("database.user");
        String password = config.getString("database.password");
        String url = String.format("jdbc:mysql://%s:%d/%s", host, port, name);

        Properties properties = new Properties();
        properties.setProperty("serverTimezone", "Europe/Moscow");
        properties.setProperty("useSSL", "false");
        properties.setProperty("characterEncoding", "utf8");
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        return DriverManager.getConnection(url, properties);
    }

    private static LinkedList<Value> loadFromDB() {

    }
}
